/* forked from https://github.com/gli/closure-compiler-maven-plugin/blob/34c2d4395db1b88e606e06bb19e9a6e6cdf9d9f0/src/main/java/glisoft/ClosureCompilerMojo.java */

package org.spiffyui.maven.plugins;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
 * Goal which executes the Google closure compiler against .js codee
 * 
 * @goal closure
 * 
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
     * @parameter expression="SIMPLE_OPTIMIZATIONS"
     * @required
     */
    private String compilationLevel;

    /**
     * @parameter expression="src/main/js"
     * @required
     */
    private File sourceDirectory;

    /**
     * @parameter expression=
     *            "${project.build.directory}/${project.build.finalName}/${project.artifactId}.min.js"
     * @required
     */
    private File outputFile;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        CompilationLevel compilationLevel = null;
        try {
            compilationLevel = CompilationLevel.valueOf(this.compilationLevel);
        } catch (IllegalArgumentException e) {
            throw new MojoFailureException("Compilation level invalid" + e.getMessage());
        }

        CompilerOptions compilerOptions = new CompilerOptions();
        compilationLevel.setOptionsForCompilationLevel(compilerOptions);
        
        Properties p = project.getProperties();
        File module = new File(p.getProperty("spiffyui.gwt.module.path"));
        List<JSSourceFile> sources = new ArrayList<JSSourceFile>();
     
        /* spiffyui.min.js _must_ be first */
        sources.add(JSSourceFile.fromFile(new File(module, "spiffyui.min.js")));   
        listJSSourceFiles(sources, sourceDirectory);
        
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

    private List<JSSourceFile> listJSSourceFiles(File directory) {
        return listJSSourceFiles(new ArrayList<JSSourceFile>(), directory);
    }

    private List<JSSourceFile> listJSSourceFiles(List<JSSourceFile> jsSourceFiles, File directory) {
        if (directory != null) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : directory.listFiles()) {
                    if (file.isFile()) {
                        if (file.getName().endsWith(".js")) {
                            jsSourceFiles.add(JSSourceFile.fromFile(file));
                        }
                    } else {
                        listJSSourceFiles(jsSourceFiles, file);
                    }
                }
            }
        }
        return jsSourceFiles;
    }

}
