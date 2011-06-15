package org.spiffyui.maven.plugins;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.cli.CommandLineUtils;

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
     * @parameter default-value=true
     */
    private boolean debug;
    
    /**
     * The -source argument for the Java compiler.
     * 
     * @parameter expression="${maven.compiler.source}" default-value="1.6"
     */
    private String source;

    /**
     * The -target argument for the Java compiler.
     * 
     * @parameter expression="${maven.compiler.target}" default-value="1.6"
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
     * @since 2.2
     */
    private File generatedSourcesDirectory;
    
    @Override
    public void execute()
        throws MojoExecutionException,
            MojoFailureException
    {
        CommandLine command = new CommandLine("javac");
        if (debug) {
            command.addArgument("-g");
        }
        
        String classpath = classpathElements.toString()
            .replace(", ", File.pathSeparator)
            .replace("[", "")
            .replace("]", "");
        
        command.addArgument("-d").addArgument(outputDirectory.getAbsolutePath());
        command.addArgument("-s").addArgument(generatedSourcesDirectory.getAbsolutePath());
        command.addArgument("-encoding").addArgument(encoding);
        command.addArgument("-cp").addArgument(classpath);
        command.addArgument("-source").addArgument(source);
        command.addArgument("-target").addArgument(target);
        
        String[] exts = {
            "java"
        };
        
        List<String> sources = compileSourceRoots;
        sources.add(htmlPropsPath);
        
        for (String source : sources) {
            File path = new File(source);
        
            if (!path.exists()) {
                continue;
            }
            
            List<File> files = new ArrayList<File>(FileUtils.listFiles(path, exts, true));
            for (File file : files) {
                command.addArgument(file.getAbsolutePath());
            }
        }
        
        ensureExists(outputDirectory);
        ensureExists(generatedSourcesDirectory);
        
        runCommand(command);
    }

    private void ensureExists(File dir)
    {
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    private void runCommand(CommandLine cmdLine)
        throws MojoExecutionException
    {

        try {
            DefaultExecutor executor = new DefaultExecutor();

            getLog().debug("Exec: " + cmdLine.toString());

            int ret = executor.execute(cmdLine, CommandLineUtils.getSystemEnvVars());
            if (ret != 0) {
                throw new MojoExecutionException("Exec failed: " + Integer.toString(ret));
            }
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage());
        }
    }
}
