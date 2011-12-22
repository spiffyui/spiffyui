package org.spiffyui.maven.plugins;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import com.googlecode.jslint4java.JSLint;
import com.googlecode.jslint4java.JSLintBuilder;
import com.googlecode.jslint4java.JSLintResult;
import com.googlecode.jslint4java.Option;
import com.googlecode.jslint4java.UnicodeBomInputStream;
import com.googlecode.jslint4java.formatter.JSLintResultFormatter;
import com.googlecode.jslint4java.formatter.JSLintXmlFormatter;
import com.googlecode.jslint4java.formatter.PlainFormatter;
import com.googlecode.jslint4java.maven.FileLister;
import com.googlecode.jslint4java.maven.ReportWriter;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.ScriptableObject;

/**
 * Validates JavaScript using jslint4java.  Initially copied from: 
 * https://github.com/happygiraffe/jslint4java/blob/master/jslint4java-maven-plugin/src/main/java/com/googlecode/jslint4java/maven/JSLintMojo.java 
 *
 * @goal spiffy-jslint
 * @phase verify
 */
public class JSLintMojo extends AbstractMojo 
{

    private static final String DEFAULT_INCLUDES = "**/*.js";

    private static final String JSLINT_XML = "jslint.xml";
    
    private static final String JSLINT_FILE = "org/spiffyui/maven/plugins/jslint.js";
    
    private static final Charset UTF8 = Charset.forName("UTF-8");
    
    private ContextFactory contextFactory = new ContextFactory();

    /**
     * Specifies the the source files to be excluded for JSLint (relative to
     * {@link #defaultSourceFolder}). Maven applies its own defaults.
     *
     * @parameter expression="${jslint.excludes}" 
     *            property="excludes"
     */
    private final List<String> excludes = new ArrayList<String>();

    /**
     * Specifies the the source files to be used for JSLint (relative to
     * {@link #defaultSourceFolder}). If none are given, defaults to
     * <code>**&#47;*.js</code>.
     *
     * @parameter expression="${jslint.includes}" 
     *            property="includes"
     */
    private final List<String> includes = new ArrayList<String>();

    /**
     * Specifies the location of the default source folder to be used for
     * JSLint. Note that this is just used for filling in the default, as it
     * resolves the default value correctly. Anything you specify will override
     * it.
     *
     * @parameter expression="${spiffyui.src.js}" 
     *            default-value="${basedir}/src/main/js" 
     * @required
     */
    private File defaultSourceFolder;

    /**
     * Which locations should JSLint look for JavaScript files in? Defaults to
     * ${basedir}/src/main/js.
     *
     * @parameter
     */
    private File[] sourceFolders = new File[] {};

    /**
     * Which JSLint {@link Option}s to set.
     *
     * @parameter
     */
    private final Map<String, String> options = new HashMap<String, String>();

    /**
     * What encoding should we use to read the JavaScript files?  Defaults to UTF-8.
     *
     * @parameter expression="${encoding}"
     *            default-value="${project.build.sourceEncoding}"
     */
    private String encoding = "UTF-8";

    /**
     * Base folder for report output.
     *
     * @parameter expression="${jslint.outputFolder}"
     *            default-value="${project.build.directory}"
     */
    private File outputFolder = new File("target");

    /**
     * Fail the build if JSLint detects any problems.
     *
     * @parameter expression="${jslint.failOnError}" default-value="true"
     */
    private boolean failOnError = true;

    /**
     * An alternative JSLint to use.
     *
     * @parameter expression="${jslint.source}"
     */
    private File jslintSource;

    /**
     * How many seconds JSLint is allowed to run.
     *
     * @parameter expression="${jslint.timeout}"
     */
    private long timeout;

    /** Add a single option.  For testing only. */
    void addOption(Option sloppy, String value)
    {
        options.put(sloppy.name().toLowerCase(Locale.ENGLISH), value);
    }

    /**
     * Set the default includes.
     *
     * @param includes
     */
    private void applyDefaults()
    {
        if (includes.isEmpty()) {
            includes.add(DEFAULT_INCLUDES);
        }
        if (sourceFolders.length == 0) {
            sourceFolders = new File[] { 
                defaultSourceFolder
            };
        }
    }

    private void applyOptions(JSLint jsLint) throws MojoExecutionException
    {
        for (Entry<String, String> entry : options.entrySet()) {
            if (entry.getValue() == null || entry.getValue().equals("")) {
                continue;
            }
            Option option;
            try {
                option = Option.valueOf(entry.getKey().toUpperCase(Locale.ENGLISH));
            } catch (IllegalArgumentException e) {
                throw new MojoExecutionException("unknown option: " + entry.getKey());
            }
            jsLint.addOption(option, entry.getValue());
        }
        
        if (options.size() == 0) {
            jsLint.addOption(Option.UNDEF, "true");
            jsLint.addOption(Option.VARS, "true");
            jsLint.addOption(Option.REGEXP, "true");
            jsLint.addOption(Option.WHITE, "true");
            jsLint.addOption(Option.SLOPPY, "true");
        }
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        JSLint jsLint = applyJSlintSource();
        applyDefaults();
        applyOptions(jsLint);
        List<File> files = getFilesToProcess();
        int failures = 0;
        ReportWriter reporter = new ReportWriter(new File(outputFolder, JSLINT_XML), new JSLintXmlFormatter());
        try {
            reporter.open();
            for (File file : files) {
                JSLintResult result = lintFile(jsLint, file);
                failures += result.getIssues().size();
                logIssues(result, reporter);
            }
        } finally {
            reporter.close();
        }
        if (failures > 0) {
            String message = "JSLint found " + failures + " problems in " + files.size() + " files";
            if (failOnError) {
                throw new MojoFailureException(message);
            } else {
                getLog().info(message);
            }
        }
    }

    private JSLint applyJSlintSource() throws MojoExecutionException
    {
        JSLintBuilder builder = new JSLintBuilder();
        if (timeout > 0) {
            builder.timeout(timeout);
        }
        if (jslintSource != null) {
            try {
                return builder.fromFile(jslintSource, Charset.forName(encoding));
            } catch (IOException e) {
                throw new MojoExecutionException("Cant' load jslint.js", e);
            }
        } else {
            try {
                Context cx = contextFactory.enter();
                ScriptableObject scope = cx.initStandardObjects();
                Reader r = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(JSLINT_FILE), UTF8);
                cx.evaluateReader(scope, r, "jslint.js", 1, null);
                return new JSLint(contextFactory, scope);
            } catch (IOException ioe) {
                throw new MojoExecutionException("Cant' load jslint.js", ioe);
            } finally {
                Context.exit();
            }
        }
    }

    // Visible for testing only.
    String getEncoding()
    {
        return encoding;
    }

    /**
     * Process includes and excludes to work out which files we are interested
     * in. Originally nicked from CheckstyleReport, now looks nothing like it.
     *
     * @return a {@link List} of {@link File}s.
     */
    private List<File> getFilesToProcess() throws MojoExecutionException
    {
        // Defaults.
        getLog().debug("includes=" + includes);
        getLog().debug("excludes=" + excludes);

        List<File> files = new ArrayList<File>();
        for (File folder : sourceFolders) {
            getLog().debug("searching " + folder);
            try {
                files.addAll(new FileLister(folder, includes, excludes).files());
            } catch (IOException e) {
                // Looking in FileUtils, this is a "can never happen". *sigh*
                throw new MojoExecutionException("Error listing files", e);
            }
        }

        return files;
    }

    // Visible for testing only.
    Map<String, String> getOptions()
    {
        return options;
    }

    private JSLintResult lintFile(JSLint jsLint, File file) throws MojoExecutionException
    {
        getLog().debug("lint " + file);
        BufferedReader reader = null;
        try {
            InputStream stream = new UnicodeBomInputStream(new FileInputStream(file));
            reader = new BufferedReader(new InputStreamReader(stream, getEncoding()));
            return jsLint.lint(file.toString(), reader);
        } catch (FileNotFoundException e) {
            throw new MojoExecutionException("file not found: " + file, e);
        } catch (UnsupportedEncodingException e) {
            // Can never happen.
            throw new MojoExecutionException("unsupported character encoding UTF-8", e);
        } catch (IOException e) {
            throw new MojoExecutionException("problem whilst linting " + file, e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new MojoExecutionException("Error closing reader", e);
                }
            }
        }
    }

    private void logIssues(JSLintResult result, ReportWriter reporter)
    {
        reporter.report(result);
        if (result.getIssues().isEmpty()) {
            return;
        }
        logIssuesToConsole(result);
    }

    private void logIssuesToConsole(JSLintResult result)
    {
        JSLintResultFormatter formatter = new PlainFormatter();
        String report = formatter.format(result);
        for (String line : report.split("\n")) {
            getLog().info(line);
        }
    }
}
