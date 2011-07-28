package org.spiffyui.maven.plugins;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * Invokes the Java compiler for the project .java source
 * 
 * @goal java-compile
 * @phase process-classes
 * @requiresDependencyResolution compile
 */
public class JavaCompileMojo extends AbstractMojo
{
    /**
     * @parameter default-value="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * Enable debug output
     * 
     * @parameter expression="${spiffyui.javac.debug}" default-value=true
     */
    private boolean debug;

    /**
     * Enable verbose output
     * 
     * @parameter expression="${spiffyui.javac.verbose}" default-value=false
     */
    private boolean verbose;

    /**
     * Enable deprecation warnings
     * 
     * @parameter expression="${spiffyui.javac.deprecation}" default-value=false
     */
    private boolean deprecation;

    /**
     * Disable warnings
     * 
     * @parameter expression="${spiffyui.javac.nowarn}" default-value=false
     */
    private boolean nowarn;

    /**
     * Set Xlint value passed to compiler "all" = enable all "none" = disable
     * all $value = pass to compiler as -Xlint:$value
     * 
     * @parameter expression="${spiffyui.javac.xlint}" default-value=none
     */
    private String xlint;

    /**
     * Extra arguments to pass to the compiler
     * 
     * @parameter expression="${spiffyui.javac.extraArgs}"
     */
    private List<String> extraArgs;

    /**
     * The -source argument for the Java compiler.
     * 
     * @parameter expression="${spiffyui.javac.source}" default-value="1.6"
     */
    private String source;

    /**
     * The -target argument for the Java compiler.
     * 
     * @parameter expression="${spiffyui.javac.target}" default-value="1.6"
     */
    private String target;

    /**
     * The source directories containing the sources to be compiled.
     * 
     * @parameter default-value="${project.compileSourceRoots}"
     * @required
     * @readonly
     */
    private List<String> compileSourceRoots;

    /**
     * The path to our generated htmlproperties
     * 
     * @parameter expression="${spiffyui.htmlprops.path}"
     * @required
     * @readonly
     */
    private String htmlPropsPath;

    /**
     * Project classpath.
     * 
     * @parameter default-value="${project.compileClasspathElements}"
     * @required
     * @readonly
     */
    private List<String> classpathElements;

    /**
     * The character encoding scheme to be applied when filtering resources.
     * 
     * @parameter expression="${project.build.sourceEncoding}"
     *            default-value="UTF-8"
     */
    private String encoding;

    /**
     * The output directory into which to copy the resources.
     * 
     * @parameter default-value="${project.build.outputDirectory}"
     * @required
     */
    private File outputDirectory;

    /**
     * <p>
     * Specify where to place generated source files created by annotation
     * processing. Only applies to JDK 1.6+
     * </p>
     * 
     * @parameter 
     *            default-value="${project.build.directory}/generated-sources/annotations"
     */
    private File generatedSourcesDirectory;

    /**
     * Convenience class that makes it easier to build a string of arguments for
     * passing to the compiler
     */
    class ArgBuilder
    {
        private List<String> m_args = new ArrayList<String>();

        ArgBuilder add(String arg)
        {
            m_args.add(arg);
            return this;
        }

        @Override
        public String toString()
        {
            return flatten(m_args.toString(), " ");
        }

        public String[] get()
        {
            return m_args.toArray(new String[m_args.size()]);
        }
    }

    @Override
    public void execute()
        throws MojoExecutionException,
            MojoFailureException
    {
        ArgBuilder args = new ArgBuilder();

        String classpath = flatten(classpathElements.toString(), File.pathSeparator);

        if (debug) {
            args.add("-g");
        }

        if (verbose) {
            args.add("-verbose");
        }

        if (deprecation) {
            args.add("-deprecation");
        }

        if (nowarn) {
            args.add("-nowarn");
        }

        if (!xlint.equals("none")) {
            if (xlint.equals("all")) {
                args.add("-Xlint");
            } else {
                args.add("-Xlint:" + xlint);
            }
        }

        args.add("-d").add(outputDirectory.getAbsolutePath()).add("-s").add(generatedSourcesDirectory.getAbsolutePath()).add("-encoding")
            .add(encoding).add("-cp").add(classpath).add("-source").add(source).add("-target").add(target);

        String[] exts = {
            "java"
        };

        for (String extraArg : extraArgs) {
            args.add(extraArg);
        }

        List<String> sources = compileSourceRoots;
        sources.add(htmlPropsPath);

        for (String source : sources) {
            File path = new File(source);

            if (!path.exists()) {
                continue;
            }

            List<File> files = new ArrayList<File>(FileUtils.listFiles(path, exts, true));
            for (File file : files) {
                args.add(file.getAbsolutePath());
            }
        }

        ensureExists(outputDirectory);
        ensureExists(generatedSourcesDirectory);

        JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
        
        if (javac == null) {
            throw new MojoExecutionException("The Java compiler could not be found.  " +
                                             "Make sure to point your Maven build to a full Java development environment.");
        }

        getLog().debug("Exec: javac " + args.toString());
        if (javac.run(null, null, null, args.get()) != 0) {
            throw new MojoExecutionException("Compilation failed");
        }
    }

    private void ensureExists(File dir)
    {
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private String flatten(String list, String sep)
    {
        return list.replace(", ", sep).replace("[", "").replace("]", "");
    }
}
