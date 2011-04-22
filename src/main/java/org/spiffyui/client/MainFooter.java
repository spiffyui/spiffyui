/*
 * Copyright (c) 2010, 2011 Unpublished Work of Novell, Inc. All Rights Reserved.
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
package org.spiffyui.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * This is the footer for the main page.
 *
 */
public class MainFooter extends Composite
{

    private HTML m_html;
    
    /**
     * Creates a new main footer panel
     */
    public MainFooter()
    {
        m_html = new HTML();
        m_html.getElement().setId("mainFooterContainer");
        
        if (RootPanel.get("mainFooter") != null) {
            RootPanel.get("mainFooter").add(m_html);
        } else {
            throw new IllegalStateException("Unable to locate the mainFooter element.  You must import spiffyui.min.js before using the Main Footer.");
        }
        
        
    }
    
    /**
     * Set the footer string.  This string may contain HTML
     * 
     * @param string the footer string
     */
    public void setFooterString(String string)
    {
        m_html.setHTML(string);
    }
    
    /**
     * Get the current string in the footer
     * 
     * @return the current footer string
     */
    public String getFooterString()
    {
        return m_html.getHTML();
    }
    
}
