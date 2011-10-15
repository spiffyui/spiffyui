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
package org.spiffyui.maven.plugins;

import static org.apache.maven.artifact.Artifact.SCOPE_COMPILE;
import static org.apache.maven.artifact.Artifact.SCOPE_PROVIDED;
import static org.apache.maven.artifact.Artifact.SCOPE_SYSTEM;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.lang.SystemUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.cli.CommandLineUtils;

/**
 * Invokes the GWTCompiler for the project source
 * 
 * @goal gwt-compile
 * @phase compile
 */
public class GwtCompileMojo extends AbstractMojo
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
     * @parameter expression="${gwt.compiler.skip}" default-value="false"
     */
    private boolean skip;

    /**
     * Don't try to detect if GWT compilation is up-to-date and can be skipped.
     * <p>
     * Can be set from command line using '-Dgwt.compiler.force=true'.
     * </p>
     * 
     * @parameter expression="${gwt.compiler.force}" default-value="false"
     */
    private boolean force;

    /**
     * On GWT 1.6+, number of parallel processes used to compile GWT
     * permutations. Defaults to platform available processors number.
     * <p>
     * Can be unset from command line using '-Dgwt.compiler.localWorkers=n'.
     * </p>
     * 
     * @parameter expression="${gwt.compiler.localWorkers}"
     */
    private int localWorkers;

    /**
     * Whether or not to enable assertions in generated scripts (-ea).
     * 
     * @parameter default-value="false"
     */
    private boolean enableAssertions;

    /**
     * Logs output in a graphical tree view.
     * <p>
     * Can be set from command line using '-Dgwt.treeLogger=true'.
     * </p>
     * 
     * @parameter default-value="false" expression="${gwt.treeLogger}"
     */
    private boolean treeLogger;

    /**
     * The source directories containing the sources to be compiled.
     * 
     * @parameter default-value="${project.compileSourceRoots}"
     * @required
     * @readonly
     */
    private List<String> compileSourceRoots;

    /**
     * The source directory
     * 
     * @parameter default-value="src/main/resources"
     * @required
     */
    private File resources;

    /**
     * EXPERIMENTAL: Disables some java.lang.Class methods (e.g. getName()).
     * <p>
     * Can be set from command line using '-Dgwt.disableClassMetadata=true'.
     * </p>
     * 
     * @parameter default-value="false" expression="${gwt.disableClassMetadata}"
     */
    private boolean disableClassMetadata;

    /**
     * EXPERIMENTAL: Disables run-time checking of cast operations.
     * <p>
     * Can be set from command line using '-Dgwt.disableCastChecking=true'.
     * </p>
     * 
     * @parameter default-value="false" expression="${gwt.disableCastChecking}"
     */
    private boolean disableCastChecking;

    /**
     * Validate all source code, but do not compile.
     * <p>
     * Can be set from command line using '-Dgwt.validateOnly=true'.
     * </p>
     * 
     * @parameter default-value="false" expression="${gwt.validateOnly}"
     */
    private boolean validateOnly;

    /**
     * Enable faster, but less-optimized, compilations.
     * <p>
     * Can be set from command line using '-Dgwt.draftCompile=true'.
     * </p>
     * 
     * @parameter default-value="false" expression="${gwt.draftCompile}"
     */
    private boolean draftCompile;

    /**
     * The directory into which extra, non-deployed files will be written.
     * 
     * @parameter default-value="${project.build.directory}/extra"
     */
    private File extra;

    /**
     * The temp directory is used for temporary compiled files (defaults is
     * system temp directory).
     * 
     * @parameter
     */
    private File workDir;

    /**
     * add -extra parameter to the compiler command line
     * <p>
     * Can be set from command line using '-Dgwt.extraParam=true'.
     * </p>
     * 
     * @parameter default-value="false" expression="${gwt.extraParam}"
     */
    private boolean extraParam;

    /**
     * add -compileReport parameter to the compiler command line
     * <p>
     * Can be set from command line using '-Dgwt.compiler.compileReport=true'.
     * </p>
     * 
     * @parameter default-value="false"
     *            expression="${gwt.compiler.compileReport}"
     */
    private boolean compileReport;

    /**
     * add -optimize parameter to the compiler command line the value must be
     * between 0 and 9 by default -1 so no arg to the compiler
     * <p>
     * Can be set from command line using '-Dgwt.compiler.optimizationLevel=n'.
     * </p>
     * 
     * @parameter default-value="-1"
     *            expression="${gwt.compiler.optimizationLevel}"
     */
    private int optimizationLevel;

    /**
     * add -XsoycDetailed parameter to the compiler command line
     * <p>
     * Can be set from command line using '-Dgwt.compiler.soycDetailed=true'.
     * </p>
     * 
     * @parameter default-value="false"
     *            expression="${gwt.compiler.soycDetailed}"
     */
    private boolean soycDetailed;

    /**
     * add -strict parameter to the compiler command line
     * <p>
     * Can be set from command line using '-Dgwt.compiler.strict=true'.
     * </p>
     * 
     * @parameter default-value="false" expression="${gwt.compiler.strict}"
     */
    private boolean strict;

    /**
     * Location on filesystem where GWT will write generated content for review (-gen option to GWTCompiler).
     * <p>
     * Can be set from command line using '-Dgwt.gen=...'
     * </p>
     * @parameter default-value="${project.build.directory}/.generated" expression="${gwt.gen}"
     */
    private File gen;

    /**
     * GWT logging level (-logLevel ERROR, WARN, INFO, TRACE, DEBUG, SPAM, or ALL).
     * <p>
     * Can be set from command line using '-Dgwt.logLevel=...'
     * </p>
     * @parameter default-value="INFO" expression="${gwt.logLevel}"
     */
    private String logLevel;

    /**
     * GWT JavaScript compiler output style (-style OBF[USCATED], PRETTY, or DETAILED).
     * <p>
     * Can be set from command line using '-Dgwt.style=...'
     * </p>
     * @parameter default-value="OBF" expression="${gwt.style}"
     */
    private String style;

    /**
     * The output directory
     * 
     * @parameter expression="${spiffyui.www}"
     * @required
     */
    private File outputDirectory;
    
    /**
     * Extra JVM arguments that are passed to the GWT-Maven generated scripts
     * (for compiler, shell, etc - typically use -Xmx512m here, or -XstartOnFirstThread, etc)
     * 
     * @parameter expression="${gwt.extraJvmArgs}" default-value="-Xmx512m"
     */
    private String extraJvmArgs;
    
    /**
     * @parameter default-value="${spiffyui.gwt.module.name}"
     * @required
     * @readonly
     */
    private String gwtModuleName;

    /**
     * @parameter default-value="${spiffyui.gwt.module.path}"
     * @required
     * @readonly
     */
    private File gwtModulePath;
    
    private final Set<String> compileScope =
           new HashSet<String>(Arrays.asList(SCOPE_COMPILE, SCOPE_PROVIDED, SCOPE_SYSTEM)); 
    /**
     * Convenience class that makes it easier to build a string of arguments for
     * passing to the compiler
     * 
     */
    protected class ClassBuilder
    {
        private List<String> m_classes = new ArrayList<String>();
        
        ClassBuilder(MavenProject proj)
        {
            Set<Artifact> artifacts = proj.getDependencyArtifacts();
            
            if (artifacts != null) {
                for (Artifact artifact : artifacts) {
                    if (compileScope.contains(artifact.getScope())) {
                        /*
                         Some plugins add artifacts without associated JAR
                         files in that case the file is null and we don't
                         need to worry about adding it to the classpath.
                         */
                        if (artifact.getFile() != null) {
                            add(artifact.getFile().getAbsolutePath());
                        }
                    }
                }
            }
        }
        
        ClassBuilder add(String arg)
        {
            m_classes.add(arg);
            return this;
        }

        @Override
        public String toString()
        {        
            return m_classes.toString()
            .replace(", ", File.pathSeparator)
            .replace("[", "")
            .replace("]", "");
        }             
    }
    
    @Override
    public void execute()
        throws MojoExecutionException,
            MojoFailureException
    {
        Properties p = project.getProperties();
        
        if (skip || "pom".equals(project.getPackaging())) {
            getLog().debug("GWT compilation is skipped");
            return;
        }

        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        } else if (!force) {
            String name = gwtModuleName;
            if (name.endsWith(InitializeMojo.SPIFFY_TMP_SUFFIX)) {
                name = name.substring(0, name.length() - InitializeMojo.SPIFFY_TMP_SUFFIX.length());
            }

            if (Math.max(getNewestModifiedTime(resources, -1), getNewestModifiedTime(new File(compileSourceRoots.get(0)), -1)) < 
                getOldestModifiedTime(new File(outputDirectory, name), -1)) {
                /*
                 Then the GWT build is up to date and we can skip it
                 */
                getLog().info("GWT files are up to date. Skipping GWT build.");
                return;
            }
        }
        
        CommandLine cmd = new CommandLine("java");
        ClassBuilder cb = new ClassBuilder(project);
        
        cb.add(p.getProperty("spiffyui.generated-source"));
        cb.add(resources.getAbsolutePath());

        for (String sourceRoot : compileSourceRoots) {
            cb.add(sourceRoot);
        }
        
        cmd.addArgument("-cp").addArgument(cb.toString())
            .addArgument(extraJvmArgs)
            .addArgument("com.google.gwt.dev.Compiler")
            .addArgument("-gen").addArgument(gen.getAbsolutePath())
            .addArgument("-logLevel").addArgument(logLevel)
            .addArgument("-style").addArgument(style)
            .addArgument("-war").addArgument(outputDirectory.getAbsolutePath())
            .addArgument("-localWorkers").addArgument(String.valueOf(getLocalWorkers()));
        
        // optional advanced arguments
        if (enableAssertions) {
            cmd.addArgument("-ea");
        }
        
        if (draftCompile) {
            cmd.addArgument("-draftCompile");
        }
 
        if (validateOnly) {
            cmd.addArgument("-validateOnly");
        }
        
        if (treeLogger) {
            cmd.addArgument("-treeLogger");
        }
        
        if (disableClassMetadata) {
            cmd.addArgument("-XdisableClassMetadata");
        }
        
        if (disableCastChecking) {
            cmd.addArgument("-XdisableCastChecking");
        }
        
        if (strict) {
            cmd.addArgument("-strict");
        }
        
        if (soycDetailed) {
            cmd.addArgument("-XsoycDetailed");
        }
 
        if (optimizationLevel >= 0) {
            cmd.addArgument("-optimize").addArgument(Integer.toString(optimizationLevel));
        }

        if (extraParam || compileReport) {
            getLog().debug("create extra directory ");
            if (!extra.exists()) {
                extra.mkdirs();
            }
            cmd.addArgument("-extra").addArgument(extra.getAbsolutePath());
        } else {
            getLog().debug("NOT create extra directory ");
        }

        if (compileReport) {
            cmd.addArgument("-compileReport");
        }

        if (workDir != null) {
            cmd.addArgument("-workDir").addArgument(String.valueOf(workDir));
        }
        
        cmd.addArgument(gwtModuleName);
                
        try {
            DefaultExecutor executor = new DefaultExecutor();

            getLog().debug("Exec: " + cmd.toString());

            int ret = executor.execute(cmd, CommandLineUtils.getSystemEnvVars());
            if (ret != 0) {
                throw new MojoExecutionException("Exec failed: " + Integer.toString(ret));
            }
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage());
        }
        moveJSDir();
    }
    
    private static long getOldestModifiedTime(File dir, long current)
    {
        if (dir == null || !dir.exists()) {
            return current;
        }
        
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                current = Math.min(getOldestModifiedTime(f, current), current);
            } else if (f.getName().endsWith(".cache.html")) {
                if (current == -1) {
                    current = f.lastModified();
                }
                
                current = Math.min(current, f.lastModified());
            }
        }
        
        return current;
    }
    
    private static long getNewestModifiedTime(File dir, long current)
    {
        for (File f : dir.listFiles()) {
            if (current == -1) {
                current = f.lastModified();
            }
            
            if (f.isDirectory()) {
                current = Math.max(getNewestModifiedTime(f, current), current);
            } else {
                current = Math.max(current, f.lastModified());
            }
        }
        
        return current;
    }
    
    /**
     * By default GWT puts everything in a directory with the same name as the module.  
     * This causes a problem because we need to find the JavaScript files as runtime so
     * we can determine the supported locales and there is no good way to find the module
     * name from the server-side at runtime.  
     * 
     * The solution is that we move the localized JavaScript files to the root of the WAR
     * so we can find them at runtime and figure out the locale that way.  This all feels
     * a little hacky, but I can't think of a better solution.
     * 
     * @param outputDirectory
     *                the GWT compiler output directory
     * @param targets the list of modules that were compiled
     * 
     * @exception IOException
     */
    private void moveJSDir()
    {
        File jslib = new File(gwtModulePath, "js" + File.separator + "lib" + File.separator + "i18n");
        if (jslib.exists()) {
            File newJslib = new File(outputDirectory, "js" + File.separator + "lib");
            newJslib.mkdirs();
            jslib.renameTo(new File(newJslib, "i18n"));
        }
    }

    private int getLocalWorkers()
    {
        if (localWorkers > 0) {
            return localWorkers;
        }
        // workaround to GWT issue 4031 whith IBM JDK
        // @see
        // http://code.google.com/p/google-web-toolkit/issues/detail?id=4031
        if (System.getProperty("java.vendor").startsWith("IBM")) {
            StringBuilder sb = new StringBuilder("Build is using IBM JDK, localWorkers set to 1 as a workaround");
            sb.append(SystemUtils.LINE_SEPARATOR);
            sb.append("see http://code.google.com/p/google-web-toolkit/issues/detail?id=4031");
            getLog().info(sb.toString());
            return 1;
        }
        return Runtime.getRuntime().availableProcessors();
    }

}
