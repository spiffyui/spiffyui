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
    private String m_inProgressTip = STRINGS.statusInProgress();
    private String m_failedTip = STRINGS.statusFailed();
    private String m_successTip = STRINGS.statusSucceeded();
    
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
            tooltip = m_inProgressTip;
            break;
        case FAILED:
            setStyleName("spiffy-status-failed");
            tooltip = m_failedTip;
            break;
        case SUCCEEDED:
            setStyleName("spiffy-status-succeeded");
            tooltip = m_successTip;
            break;
        default:
            tooltip = "";
            setStyleName("");
            break;
        }
        setTitle(tooltip);
    }
    
    /**
     * Set the in progress tooltip
     * 
     * @param tip   the new in progress tooltip
     */
    public void setInProgressTip(String tip) 
    {
        m_inProgressTip = tip;
    }
    
    /**
     * Get the in progress tooltip
     * 
     * @return the in progress tooltip
     */
    public String getInProgressTip()
    {
        return m_inProgressTip;
    }

    /**
     * Set the failed tooltip
     * 
     * @param tip   the new failed tooltip
     */
    public void setFailedTip(String tip)
    {
        m_failedTip = tip;
    }
    
    /**
     * Get the failed tooltip
     * 
     * @return the failed tooltip
     */
    public String getFailedTip()
    {
        return m_failedTip;
    }

    /**
     * Set the success tooltip
     * 
     * @param tip   the new success tooltip
     */
    public void setSuccessTip(String tip)
    {
        m_successTip = tip;
    }

    /**
     * Get the success tooltip
     * 
     * @return the success tooltip
     */
    public String getSuccessTip()
    {
        return m_successTip;
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
