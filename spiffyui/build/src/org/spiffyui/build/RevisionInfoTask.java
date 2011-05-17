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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.zip.GZIPOutputStream;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
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
 * <p> 
 * The RevisionInfoTask gets information about the current revision being built 
 * and makes it available to the project.  This task supports Subversion and GIT. 
 * Both clients must be available on the current path.  It exports two properties: 
 * revision.number and revision.date into the project. 
 * </p> 
 */
public class RevisionInfoTask extends Task
{
    
    /**
     * The project root directory
     */
    private File m_rootDir;
    
    /**
     * Sepcify the location of the directory to get the revision of
     * 
     * @param dir the output file directory
     */
    public void setDir(File dir)
    {
        this.m_rootDir = dir;
    }
    
    /**
     * Reset state to default.
     */
    public void reset()
    {
        m_rootDir = null;
    }
    
    /**
     * Executes this task to run the compiler.
     * 
     * @exception BuildException
     *                   if there isn't enough information to run the GWT compiler
     */
    public void execute() throws BuildException
    {
        if (m_rootDir == null) {
            throw new BuildException("Must specify a directory");
        }

        try {
            if (!getSubversionRev()) {
                if (!getGitRev()) {
                    getProject().setProperty("revision.number", "-1");
                    getProject().setProperty("revision.date", "-1");
                }
            }
        } catch (IOException ioe) {
            throw new BuildException(ioe);
        }
    }

    private class ProcessWatcher implements Runnable
    {
        private Process m_p;
        private boolean m_finished = false;
    
        public ProcessWatcher(Process p) {
            m_p = p;
            new Thread(this).start();
        }
    
        public boolean isFinished() {
            return m_finished;
        }
    
        public void run() {
            try {
                m_p.waitFor();
            } catch (Exception e) {
                /*
                 * This shouldn't happen
                 */
                e.printStackTrace();
            }
            m_finished = true;
        }
    
    }


    private boolean getSubversionRev() throws BuildException, IOException
    {
        Process proc = Runtime.getRuntime().exec("svn info " + m_rootDir.getAbsolutePath());

        InputStream is = proc.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        ProcessWatcher pw = new ProcessWatcher(proc);

        while (!pw.isFinished()) {
            String line;
            int exit = -1;
    
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Revision: ")) {
                    getProject().setProperty("revision.number", line.substring(10, line.length()));
                    getProject().setProperty("revision.date", "-1");
                    return true;
                }
                try {
                    exit = proc.exitValue();
                    if (exit == 0)  {
                        // Process finished
                    }
                } catch (IllegalThreadStateException t) {
                    /*
                     * If the process isn't finished we just want to repeat the loop
                     */
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new BuildException(e);
            }
        }

        return false;
    }

    private boolean getGitRev() throws BuildException, IOException
    {
        String args[] = new String[6];
        args[0] = "git";
        args[1] = "log";
        args[2] = "--pretty=Revision: %h:%ct0000";
        args[3] = "-n1";
        args[4] = "HEAD";
        args[5] = m_rootDir.getAbsolutePath();
        
        Process proc = Runtime.getRuntime().exec(args);

        InputStream is = proc.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        ProcessWatcher pw = new ProcessWatcher(proc);

        while (!pw.isFinished()) {
            String line;
            int exit = -1;
    
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Revision: ")) {
                    int colIndex = line.indexOf(':', 11);
                    getProject().setProperty("revision.number", line.substring(10, colIndex));
                    getProject().setProperty("revision.date", line.substring(colIndex + 1, line.length() - 1));
                    return true;
                }
                try {
                    exit = proc.exitValue();
                    if (exit == 0)  {
                        // Process finished
                    }
                } catch (IllegalThreadStateException t) {
                    /*
                     * If the process isn't finished we just want to repeat the loop
                     */
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new BuildException(e);
            }
        }

        return false;
    }
}
