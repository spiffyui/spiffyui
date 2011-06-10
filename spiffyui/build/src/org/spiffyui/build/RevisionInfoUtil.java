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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * <p> 
 * The RevisionInfoTask gets information about the current revision being built 
 * and makes it available to the project.  This task supports Subversion and GIT. 
 * Both clients must be available on the current path.  It exports two properties: 
 * revision.number and revision.date into the project. 
 * </p> 
 */
public class RevisionInfoUtil
{
    
    /**
     * Get the revision information for the specified root directory
     * 
     * @param rootDir the root to get revision information from
     * 
     * @return the revision info bean with revision information about this directory.
     *  
     * @throws InterruptedException if there is an error waiting for the output of the version control commands
     * @throws IOException if there is an error reading the output from the version control commands
     */
    public static RevisionInfoBean getRevisionInfo(final File rootDir)
        throws InterruptedException, IOException
    {
        RevisionInfoBean revInfo = getSubversionRev(rootDir);
        if (revInfo == null) {
            revInfo = getGitRev(rootDir);
        }
        
        if (revInfo == null) {
            return new RevisionInfoBean("-1", "-1");
        } else {
            return revInfo;
        }
    }
    
    
    /**
     * This process watcher let's is know when the external processes we are executing complete.
     */
    private static class ProcessWatcher implements Runnable
    {
        private Process m_p;
        private boolean m_finished = false;
    
        public ProcessWatcher(Process p)
        {
            m_p = p;
            new Thread(this).start();
        }
    
        public boolean isFinished()
        {
            return m_finished;
        }
    
        public void run()
        {
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


    private static RevisionInfoBean getSubversionRev(final File rootDir) throws InterruptedException, IOException
    {
        System.out.println("getSubversionRev(" + rootDir + ")");
        Process proc = Runtime.getRuntime().exec("svn info " + rootDir.getAbsolutePath());

        InputStream is = proc.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        ProcessWatcher pw = new ProcessWatcher(proc);

        while (!pw.isFinished()) {
            String line;
            int exit = -1;
    
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Revision: ")) {
                    return new RevisionInfoBean(line.substring(10, line.length()), "-1");
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

            Thread.sleep(100);
        }

        return null;
    }

    private static RevisionInfoBean getGitRev(final File rootDir) throws InterruptedException, IOException
    {
        String args[] = new String[6];
        args[0] = "git";
        args[1] = "log";
        args[2] = "--pretty=Revision: %h:%ct0000";
        args[3] = "-n1";
        args[4] = "HEAD";
        args[5] = rootDir.getAbsolutePath();
        
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
                    return new RevisionInfoBean(line.substring(10, colIndex), line.substring(colIndex + 1, line.length() - 1));
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

            Thread.sleep(100);
        }

        return null;
    }
}
