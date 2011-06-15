/* forked from https://github.com/gli/closure-compiler-maven-plugin/blob/34c2d4395db1b88e606e06bb19e9a6e6cdf9d9f0/src/main/java/glisoft/ClosureCompilerMojo.java */

package org.spiffyui.maven.plugins;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.JSSourceFile;
import com.google.javascript.jscomp.Result;

/**
 * Invokes the Google Closure compiler for the project .js source
 * 
 * @goal closure-compile
 * @phase compile
 */
public class ClosureMojo extends AbstractMojo
{
    /**
     * {@link MavenProject} to process.
     * 
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * Set the compilation level passed to the compiler
     * 
     * @parameter expression="${spiffyui.closurecompiler.level}"
     *            default-value="SIMPLE_OPTIMIZATIONS"
     * @required
     */
    private String compilationLevel;

    /**
     * Path to the project .js source for compilation
     * 
     * @parameter default-value="src/main/js"
     * @required
     */
    private File sourceDirectory;

    /**
     * The compressed/aggregate javascript output file
     * 
     * @parameter default-value="${spiffyui.www}/${project.artifactId}.min.js"
     * @required
     */
    private File outputFile;

    @Override
    public void execute()
        throws MojoExecutionException,
            MojoFailureException
    {
        if (!sourceDirectory.exists()) {
            getLog().info("Source directory doesn't exist, skipping");
            return;
        }
        
        String[] exts = {
            "js"
        };
        List<File> files = new ArrayList<File>(FileUtils.listFiles(sourceDirectory, exts, true));
        if (files.size() == 0) {
            getLog().info("No source files detected, skipping");
            return;
        }
        
        CompilationLevel compLevel = null;
        try {
            compLevel = CompilationLevel.valueOf(compilationLevel);
        } catch (IllegalArgumentException e) {
            throw new MojoFailureException("Compilation level invalid" + e.getMessage());
        }

        CompilerOptions compilerOptions = new CompilerOptions();
        compLevel.setOptionsForCompilationLevel(compilerOptions);

        Properties p = project.getProperties();
        File module = new File(p.getProperty("spiffyui.gwt.module.path"));
        List<JSSourceFile> sources = new ArrayList<JSSourceFile>();

        /* spiffyui.min.js _must_ be first */
        sources.add(JSSourceFile.fromFile(new File(module, "spiffyui.min.js")));
        for (File file : files) {
            sources.add(JSSourceFile.fromFile(file));
        }

        Compiler compiler = new Compiler();
        Result result = compiler.compile(new ArrayList<JSSourceFile>(), sources, compilerOptions);

        for (JSError warning : result.warnings) {
            getLog().warn(warning.toString());
        }

        for (JSError error : result.errors) {
            getLog().error(error.toString());
        }

        if (!result.success) {
            throw new MojoFailureException("Compilation failure");
        }

        try {
            Files.createParentDirs(outputFile);
            Files.touch(outputFile);
            Files.write(compiler.toSource(), outputFile, Charsets.UTF_8);
        } catch (IOException e) {
            throw new MojoFailureException(outputFile != null ? outputFile.toString() : e.getMessage());
        }
    }
}
