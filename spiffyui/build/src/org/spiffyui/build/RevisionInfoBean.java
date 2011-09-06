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

/**
 * This simple beans handles the revision number and revision date for 
 * the revision info task.
 */
public class RevisionInfoBean
{
    private String m_revNumber;
    private String m_revDate;
    
    /**
     * Create a new revision info bean.
     * 
     * @param revNumber the revision number
     * @param revDate   the revision date
     */
    public RevisionInfoBean(String revNumber, String revDate)
    {
        m_revNumber = revNumber;
        m_revDate = revDate;
    }
    
    /**
     * Get the revision date from this bean.
     * 
     * @return the revision date
     */
    public String getRevDate()
    {
        return m_revDate;
    }
    
    /**
     * Get the revision number from this bean.
     * 
     * @return the revision number
     */
    public String getRevNumber()
    {
        return m_revNumber;
    }
}
