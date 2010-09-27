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
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * This is the header for the main page.
 *
 */
public class MainHeader extends FlowPanel
{

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
                "<span class=\"headertitle\" id=\"header_title\" >Main Header</span>" +
            "</div>" +
            "<div id=\"headerright\">" +
                "<div id=\"header_actionsBlock\">" +
                    "<span id=\"headerstring\">Welcome</span> " + 
                "</div>" +
            "</div>";

        m_panel = new HTMLPanel("div", html);
        m_panel.getElement().setId("mainHeaderContainer");
        
        Anchor logout = new Anchor();
        logout.getElement().setId("header_logout");
        m_panel.add(logout, "header_actionsBlock");

        add(m_panel);
        RootPanel.get("mainHeader").add(this);

        
    }
    
    public Anchor getLogout()
    {
        return m_logout;
    }
    
    /**
     * Sets the username for display in the header
     * 
     * @param name   the user name
     */
    public void setWelcomeString(String string)
    {
        m_panel.getElementById("headerstring").setInnerText(string);
    }
    
    public String getWelcomeString()
    {
        return m_panel.getElementById("headerstring").getInnerText();
    }
    
    public void setHeaderTitle(String string)
    {
        m_panel.getElementById("header_title").setInnerText(string);
    }
    
    public String getHeaderTitle()
    {
        return m_panel.getElementById("header_title").getInnerText();
    }
}