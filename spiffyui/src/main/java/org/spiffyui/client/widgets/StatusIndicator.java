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
package org.spiffyui.client.widgets;

import org.spiffyui.client.i18n.SpiffyUIStrings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * This is a reusable loading icon widget
 *
 */
public class StatusIndicator extends FlowPanel
{
    /**
     * Status In Progress
     */
    public static final int IN_PROGRESS = 1;

    /**
     * Status Failed
     */
    public static final int FAILED = 2;
    
    /**
     * Status Succeeded
     */
    public static final int SUCCEEDED = 3;
    
    private static final SpiffyUIStrings STRINGS = (SpiffyUIStrings) GWT.create(SpiffyUIStrings.class);
    
    private int m_status;
    
    /** 
     * Show a generic status indicator
     * @param status - int status constant
     */
    public StatusIndicator(int status)
    {
        m_status = status;
        updateStatusStyles(m_status);
    }
    
    /** 
     * Show a generic status indicator with the default status of none
     */
    public StatusIndicator()
    {
        this(-1);
    }
    
    private void updateStatusStyles(int status)
    {
        String tooltip;
        switch (status) {
        case IN_PROGRESS:
            setStyleName("spiffy-status-inprogress");
            tooltip = STRINGS.statusInProgress();
            break;
        case FAILED:
            setStyleName("spiffy-status-failed");
            tooltip = STRINGS.statusFailed();
            break;
        case SUCCEEDED:
            setStyleName("spiffy-status-succeeded");
            tooltip = STRINGS.statusSucceeded();
            break;
        default:
            tooltip = "";
            setStyleName("");
            break;
        }
        setTitle(tooltip);
    }
    
    /**
     * Get the current status of this status indicator.
     * 
     * @return the current status
     */
    public int getStatus()
    {
        return m_status;
    }
    
    /**
     * Set the status of this status indicator.
     * 
     * @param status the new status
     */
    public void setStatus(int status)
    {
        m_status = status;
        updateStatusStyles(m_status);
    }
}
