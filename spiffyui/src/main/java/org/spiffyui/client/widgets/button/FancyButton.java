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
package org.spiffyui.client.widgets.button;

import com.google.gwt.user.client.ui.Button;

/**
 * This is the base class for fancy buttons with or without icons in them
 */
public abstract class FancyButton extends Button
{
    private boolean m_enabled = true;
    private boolean m_inProgress = false;
    
    /**
     * Creates a new FancyButton
     *  
     */
    public FancyButton()
    {
        super();
    }
    
    /**
     * Creates a new FancyButton with the specified text
     * 
     * @param text   the text for the button
     */
    public FancyButton(String text)
    {
        super(text);
    }
    
    @Override
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        m_enabled = enabled;
        
        if (enabled) {
            getElement().removeClassName("disabled");
        } else {
            getElement().addClassName("disabled");
        }
    }
    
    @Override
    public boolean isEnabled()
    {
        return m_enabled;
    }
    
    /**
     * Sets this button to be in progress.  In progress buttons show a different
     * image and are disabled
     * 
     * @param inprogress true if in progress and false otherwise
     */
    public void setInProgress(boolean inprogress)
    {
        m_inProgress = inprogress;
        setEnabled(!inprogress);
        
        if (inprogress) {
            getElement().addClassName("inprogress");
        } else {
            getElement().removeClassName("inprogress");
        }
    }
    
    /**
     * Determines if this button is in progress
     * 
     * @return true if it is in progress and false otherwise
     */
    public boolean isInProgress()
    {
        return m_inProgress;
    }
}
