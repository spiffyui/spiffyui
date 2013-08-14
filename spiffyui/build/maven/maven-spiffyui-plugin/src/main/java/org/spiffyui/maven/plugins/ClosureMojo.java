/*******************************************************************************
 * 
 * Copyright 2011 Spiffy UI Team   
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

/* forked from https://github.com/gli/closure-compiler-maven-plugin/blob/34c2d4395db1b88e606e06bb19e9a6e6cdf9d9f0/src/main/java/glisoft/ClosureCompilerMojo.java */

package org.spiffyui.maven.plugins;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

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
     * @parameter expression="${spiffyui.src.js}" 
     *            default-value="${basedir}/src/main/js"
     * @required
     */
    private File sourceDirectory;

    /**
     * The compressed/aggregate javascript output file
     * 
     * @parameter expression="${spiffyui.js.output.file}" 
     *            default-value="${spiffyui.www}/${project.artifactId}.min.js"
     * @required
     */
    private File outputFile;
    
    /**
     * @parameter default-value="${spiffyui.gwt.module.path}"
     * @required
     * @readonly
     */
    private File gwtModulePath;
    
    @Override
    public void execute()
        throws MojoExecutionException,
            MojoFailureException
    {
        if (!sourceDirectory.exists()) {
            getLog().debug("Source directory doesn't exist, skipping");
            return;
        }

        /*
         * The first step is to copy the uncompressed files into the
         * target directory so users can see the real source for debugging
         */
        File jsDir = new File(outputFile.getParentFile(), "js");
        jsDir.mkdirs();

        try {
            FileUtils.copyDirectory(sourceDirectory, jsDir);
        } catch (IOException ioe) {
            throw new MojoFailureException("Unable to copy JavaScripot source" + ioe.getMessage());
        }
        
        String[] exts = {
            "js"
        };
        List<File> files = new ArrayList<File>(FileUtils.listFiles(sourceDirectory, exts, true));
        if (files.size() == 0) {
            getLog().debug("No source files detected, skipping");
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

        List<JSSourceFile> sources = new ArrayList<JSSourceFile>();
        
        long lastMod = -1;

        /* spiffyui.min.js _must_ be first */
        sources.add(JSSourceFile.fromFile(new File(gwtModulePath, "spiffyui.min.js")));
        for (File file : files) {
            lastMod = Math.max(lastMod, file.lastModified());
            sources.add(JSSourceFile.fromFile(file));
        }
        
        if (outputFile.exists() && lastMod < outputFile.lastModified()) {
            /*
             If the newest source file is older then the output file then
             everything is up to date and we don't need to compile anything.
             */
            getLog().info("Skipping JavaScript compilation. The compiled JavaScript file is up to date.");
            return;
        }

        Compiler compiler = new Compiler();
        compiler.setLoggingLevel(Level.WARNING);
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
