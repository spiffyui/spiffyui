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
package org.spiffyui.build;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;


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
            throw new BuildException("Must specify a root directory");
        }

        try {
            RevisionInfoBean revInfo = RevisionInfoUtil.getRevisionInfo(m_rootDir);
            
            getProject().setProperty("revision.number", revInfo.getRevNumber());
            getProject().setProperty("revision.date", revInfo.getRevDate());
        } catch (InterruptedException ie) {
            throw new BuildException(ie);
        }
    }

    
}
