/*
 * Copyright (c) 2010 Unpublished Work of Novell, Inc. All Rights Reserved.
 *
 * THIS WORK IS AN UNPUBLISHED WORK AND CONTAINS CONFIDENTIAL,
 * PROPRIETARY AND TRADE SECRET INFORMATION OF NOVELL, INC. ACCESS TO
 * THIS WORK IS RESTRICTED TO (I) NOVELL, INC. EMPLOYEES WHO HAVE A NEED
 * TO KNOW HOW TO PERFORM TASKS WITHIN THE SCOPE OF THEIR ASSIGNMENTS AND
 * (II) ENTITIES OTHER THAN NOVELL, INC. WHO HAVE ENTERED INTO
 * APPROPRIATE LICENSE AGREEMENTS. NO PART OF THIS WORK MAY BE USED,
 * PRACTICED, PERFORMED, COPIED, DISTRIBUTED, REVISED, MODIFIED,
 * TRANSLATED, ABRIDGED, CONDENSED, EXPANDED, COLLECTED, COMPILED,
 * LINKED, RECAST, TRANSFORMED OR ADAPTED WITHOUT THE PRIOR WRITTEN
 * CONSENT OF NOVELL, INC. ANY USE OR EXPLOITATION OF THIS WORK WITHOUT
 * AUTHORIZATION COULD SUBJECT THE PERPETRATOR TO CRIMINAL AND CIVIL
 * LIABILITY.
 *
 * ========================================================================
 */
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
