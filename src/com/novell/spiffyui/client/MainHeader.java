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
package com.novell.spiffyui.client;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;

import com.novell.spiffyui.client.nav.HasNavBarListenersPanel;


/**
 * This is the header for the main page.
 *
 */
public class MainHeader extends HasNavBarListenersPanel
{
    /**
     * The ID of the header string section.  This section is often used for a welcome message.
     */
    public static final String HEADER_STRING = "headerstring";
    
    /**
     * The ID of the header title section
     */
    public static final String HEADER_TITLE = "header_title";

    /**
     * The ID of the header actions block
     */
    public static final String HEADER_ACTIONS_BLOCK = "header_actionsBlock";

    private HTMLPanel m_panel;
    private Anchor m_logout;
    
    /**
     * Creates a new MainHeader panel
     */
    public MainHeader()
    {
        String html =
            "<div id=\"headerleft\">" +
                "<div id=\"headerlogo\"> </div>" +
                "<span class=\"headertitle\" id=\"" + HEADER_TITLE + "\"></span>" +
            "</div>" +
            "<div id=\"headerright\">" +
                "<div id=\"" + HEADER_ACTIONS_BLOCK + "\">" +
                    "<span id=\"" + HEADER_STRING + "\"></span> " + 
                "</div>" +
            "</div>";

        m_panel = new HTMLPanel("div", html);
        m_panel.getElement().setId("mainHeaderContainer");
        
        Anchor logout = new Anchor();
        logout.getElement().setId("header_logout");
        m_panel.add(logout, HEADER_ACTIONS_BLOCK);

        add(m_panel);
        RootPanel.get("mainHeader").add(this);

        
    }

    /**
     * Get the Anchor object used for the logout link in the header bar
     * 
     * @return the logout Anchor
     */
    public Anchor getLogout()
    {
        return m_logout;
    }

    /**
     * Set the Anchor used for the logout in the header
     * 
     * @param logout the logout Anchor
     */
    public void setLogout(Anchor logout)
    {
        m_logout = logout;
        m_panel.add(logout, HEADER_ACTIONS_BLOCK);
    }
    
    /**
     * Sets the username for display in the header
     * 
     * @param string the welcome string
     */
    public void setWelcomeString(String string)
    {
        m_panel.getElementById(HEADER_STRING).setInnerText(string);
    }
    
    /**
     * Gets the welcome string for the header
     * 
     * @return the welcome string
     */
    public String getWelcomeString()
    {
        return m_panel.getElementById(HEADER_STRING).getInnerText();
    }
    
    /**
     * Set the main title for this header
     * 
     * @param string The main title for this header
     */
    public void setHeaderTitle(String string)
    {
        m_panel.getElementById(HEADER_TITLE).setInnerHTML(string);
    }
    
    /**
     * Get the main title for the this header
     * 
     * @return The main title of this header
     */
    public String getHeaderTitle()
    {
        return m_panel.getElementById(HEADER_TITLE).getInnerHTML();
    }

    /**
     * Get the HTML panel for this header
     * 
     * @return the HTML panel
     */
    protected HTMLPanel getPanel()
    {
        return m_panel;
    }
}
