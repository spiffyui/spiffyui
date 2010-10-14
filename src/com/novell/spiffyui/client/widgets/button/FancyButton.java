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
package com.novell.spiffyui.client.widgets.button;

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
        //setEnabled(!inprogress);
        
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
