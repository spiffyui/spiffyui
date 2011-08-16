/*******************************************************************************
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
package org.spiffyui.build;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.CommandlineJava;
import org.apache.tools.ant.types.Environment;
import org.apache.tools.ant.types.FileList;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.Resources;
import org.apache.tools.ant.types.resources.Restrict;
import org.apache.tools.ant.types.resources.selectors.Exists;
import org.apache.tools.ant.types.resources.selectors.Not;
import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
import org.apache.tools.ant.util.FileUtils;


/**
 * This is a custom task for calling the Google Closure compiler.
 */
public class ClosureTask extends Task
{
    
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    private static final ResourceSelector EXISTS = new Exists();
    private static final ResourceSelector NOT_EXISTS = new Not(EXISTS);
    
    /**
     * The destination of the stream. If <code>null</code>, the system
     * console is used.
     */
    private File m_destinationFile;
    
    
    /**
     * The compilation level to use for the compiler.  This field is optional
     */
    private String m_compilationLevel;
    
    /**
     * Stores a collection of file sets and/or file lists, used to
     * select multiple files for compilation.
     */
    private ResourceCollection m_rc;
    
    /**
     * The closure JAR file
     */
    private File m_closureJar;
    
    /**
     * This is the level of output from the compiler.
     */
    private String m_summaryDetail = "1";
    
    /**
     * The warning level for the compiler
     */
    private String m_warningLevel = "DEFAULT";
    
    /**
     * Adds the file set of JavaScript files to compile
     * 
     * @param set    the set of files
     */
    public void addFileset(FileSet set)
    {
        add(set);
    }

    /**
     * List of files to compile.
     * 
     * @param list   the list of files
     */
    public void addFilelist(FileList list)
    {
        add(list);
    }
    
    /**
     * Sepcify the name of the output file.  This is required
     * 
     * @param jsFile the output file name
     */
    public void setJSfile(File jsFile)
    {
        this.m_destinationFile = jsFile;
    }
    
    /**
     * Set the path to the closure compiler JAR file normally named compiler.jar.
     * 
     * @param jarFile the closure compiler JAR file
     */
    public void setClosureJar(File jarFile)
    {
        m_closureJar = jarFile;
    }
    
    /**
     * Controls how detailed the compilation summary is. Values: 0 (never print 
     * summary), 1 (print summary only if there are errors or warnings), 2 (print 
     * summary if type checking is on, see --check_types), 3 (always print summary). 
     * The default level is 1.; default: 1
     * 
     * @param level  the summary level
     */
    public void setSummaryLevel(String level)
    {
        m_summaryDetail = level;
    }
    
    /**
     * Specifies the warning level to use. Options: QUIET, DEFAULT, 
     * VERBOSE; default: DEFAULT
     * 
     * @param level  the warning level to use
     */

    public void setWarningLevel(String level)
    {
        if (!"QUIET".equals(level) &&
            !"VERBOSE".equals(level) &&
            !"DEFAULT".equals(level)) {
            throw new BuildException("Warning level must be either QUIET, VERBOSE, or DEFAULT");
        }
        m_warningLevel = level;
    }
    
    /**
     * Sets the compilation level
     * 
     * @param level  the compilation level
     */
    public void setCompilationLevel(String level)
    {
        m_compilationLevel = level;
    }
    
    /**
     * Add an arbitrary ResourceCollection.
     * @param c the ResourceCollection to add.
     * @since Ant 1.7
     */
    public synchronized void add(ResourceCollection c)
    {
        if (m_rc == null) {
            m_rc = c;
            return;
        }
        if (!(m_rc instanceof Resources)) {
            Resources newRc = new Resources();
            newRc.setProject(getProject());
            newRc.add(m_rc);
            m_rc = newRc;
        }
        ((Resources) m_rc).add(c);
    }
    
    private ResourceCollection getResources()
    {
        if (m_rc == null) {
            return null;
        }
        Restrict noexistRc = new Restrict();
        noexistRc.add(NOT_EXISTS);
        noexistRc.add(m_rc);
        for (Iterator i = noexistRc.iterator(); i.hasNext();) {
            log(i.next() + " does not exist.", Project.MSG_ERR);
        }
        if (m_destinationFile != null) {
            for (Iterator i = m_rc.iterator(); i.hasNext();) {
                Object o = i.next();
                if (o instanceof FileResource) {
                    File f = ((FileResource) o).getFile();
                    if (FILE_UTILS.fileNameEquals(f, m_destinationFile)) {
                        throw new BuildException("Input file \"" + f + "\" is the same as the output file.");
                    }
                }
            }
        }
        Restrict result = new Restrict();
        result.add(EXISTS);
        result.add(m_rc);
        return result;
    }
    
    /**
     * Reset state to default.
     */
    public void reset()
    {
        m_rc = null;
        m_destinationFile = null;
        m_compilationLevel = null;
    }
    
    /**
     * Executes this task to run the compiler.
     * 
     * @exception BuildException
     *                   if there isn't enough information to run the GWT compiler
     */
    public void execute() throws BuildException
    {
        if (m_destinationFile == null) {
            throw new BuildException("Must specify a JS file output");
        }
        
        if (getResources() == null) {
            return;
        }
        
        if (m_closureJar == null) {
            throw new BuildException("Must specify the closure compiler JAR file");
        }
        
        CommandlineJava cmdl = new CommandlineJava();
        cmdl.setClassname("com.google.javascript.jscomp.CommandLineRunner");
        cmdl.setCloneVm(true);
        cmdl.createClasspath(getProject());
        
        /*
         We need to find a better way to load these items on the classpath.  Hackito ergo sum
         */
        cmdl.getClasspath().add(new FileResource(m_closureJar));
        cmdl.getClasspath().add(new FileResource(new File(m_closureJar.getParent(), "args4j-2.0.12.jar")));
        cmdl.getClasspath().add(new FileResource(new File(m_closureJar.getParent(), "guava-r08.jar")));
        
        Commandline.Argument arg = cmdl.createArgument();
        
        StringBuffer sb = new StringBuffer();
        
        //sb.append("-jar ");
        //sb.append(m_closureJar.getAbsolutePath() + " ");
        
        ResourceCollection c = getResources();
        Iterator i = c.iterator();

        long lastMod = -1;

        while (i.hasNext()) {
            lastMod = Math.max(lastMod, ((FileResource) i.next()).getLastModified());
        }

        sb.append("--js_output_file ");
        sb.append(m_destinationFile.getAbsolutePath() + " ");

        if (m_destinationFile.lastModified() >= lastMod) {
            /*
             * If the generated file is newer than the source
             * files then there is no need to rebuild.
             */
            return;
        }

        i = c.iterator();
        while (i.hasNext()) {
            sb.append("--js ");
            sb.append(i.next() + " ");
        }
        
        if (m_compilationLevel != null) {
            sb.append("--compilation_level ");
            sb.append(m_compilationLevel + " ");
        }
        
        sb.append("--summary_detail_level ");
        sb.append(m_summaryDetail + " ");
        
        sb.append("--warning_level ");
        sb.append(m_warningLevel + " ");
        
        arg.setLine(sb.toString());
        
        Execute exe = new Execute();
        setupExecutable(exe, cmdl.getCommandline());
        
        int result = -1;
        try {
            result = exe.execute();
        } catch (IOException e) {
            throw new BuildException(e, getLocation());
        }
        
        if (result != 0) {
            throw new BuildException("The Closure compiler did not complete successfully.  See the output for more details.");
        }
    }
    
    private void setupExecutable(Execute exe, String[] command)
    {
        exe.setAntRun(getProject());
        exe.setWorkingDirectory(getProject().getBaseDir());
        
        setupEnvironment(exe);
        setupCommandLine(exe, command);
    }

    /**
     * Set up our environment variables.
     * @param exe executable.
     */
    private void setupEnvironment(Execute exe)
    {
        Environment env = new Environment();
        String[] environment = env.getVariables();
        if (environment != null) {
            for (int i = 0; i < environment.length; i++) {
                log("Setting environment variable: " + environment[i],
                    Project.MSG_VERBOSE);
            }
        }
        exe.setNewenvironment(true);
        exe.setEnvironment(environment);
    }

    /**
     * Set the command line for the exe.
     * On VMS, hands off to {@link #setupCommandLineForVMS(Execute, String[])}.
     * @param exe executable.
     * @param command command to execute.
     */
    private void setupCommandLine(Execute exe, String[] command)
    {
        exe.setCommandline(command);
    }
}
