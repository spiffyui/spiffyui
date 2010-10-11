/*
 * ========================================================================
 *
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

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Anchor;
import com.novell.spiffyui.client.SpiffyUIStrings;

/**
 * This an Anchor that shows the refresh icon.  It has a disabled look as well as a loading look.
 */
public class RefreshAnchor extends Anchor
{
    private static final SpiffyUIStrings STRINGS = (SpiffyUIStrings) GWT.create(SpiffyUIStrings.class);
    
    private boolean m_enabled = true;
    private boolean m_loading = false;
    private Element m_iconDiv;
    
    /**
     * Creates a new RefreshAnchor
     * 
     * @param id - The element ID
     */
    public RefreshAnchor(String id)
    {
        super();
        m_iconDiv = Document.get().createDivElement();
        m_iconDiv.setClassName("spiffy-refresh-icon");
        m_iconDiv.setId(id + "_refreshIconDiv");     
        setTitle(STRINGS.refresh());
        getElement().appendChild(m_iconDiv);
        setHref("#");
        getElement().setId(id);
    }
    
    @Override
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        m_enabled = enabled;
        
        if (enabled) {
            m_iconDiv.setClassName("spiffy-refresh-icon");
        } else {
            m_iconDiv.setClassName("spiffy-refresh-icon-disabled");
        }
    }
    
    
    @Override
    public boolean isEnabled()
    {
        return m_enabled;
    }
    

    /**
     * Sets the anchor to appear as loading
     * @param loading - boolean true to be loading
     */
    public void setLoading(boolean loading)
    {
        super.setEnabled(!loading);
        m_loading = loading;
        
        if (loading) {
            m_iconDiv.setClassName("spiffy-refresh-icon-loading");
        } else {
            m_iconDiv.setClassName("spiffy-refresh-icon");
        }
    }

    /**
     * Returns true if loading
     * @return boolean true if loading
     */
    public boolean isLoading()
    {
        return m_loading;
    }
    
}
