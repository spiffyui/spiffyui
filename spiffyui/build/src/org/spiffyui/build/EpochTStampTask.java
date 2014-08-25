/*******************************************************************************
 * 
 * Copyright 2011-2014 Spiffy UI Team   
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

import java.util.Date;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;


/**
 * This is a custom task for setting the epoch date as a property in the project
 */
public class EpochTStampTask extends Task
{
    
    /**
     * The property to set the date as
     */
    private String m_propName;
    
    
    /**
     * Sets the property name
     * 
     * @param prop    the property name
     */
    public void setProperty(String prop)
    {
        m_propName = prop;
    }

    /**
     * Reset state to default.
     */
    public void reset()
    {
        m_propName = null;
    }
    
    /**
     * Executes this task to run the compiler.
     * 
     * @exception BuildException
     *                   if there isn't enough information to run the GWT compiler
     */
    public void execute() throws BuildException
    {
        if (m_propName == null) {
            throw new BuildException("Must specify a property name");
        }
        
        getProject().setNewProperty(m_propName, "" + new Date().getTime());
    }
}
