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
package com.novell.spiffyui.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.HTMLPanel;

import com.novell.spiffyui.client.JSUtil;
import com.novell.spiffyui.client.SpiffyUIStrings;

/**
 * The SlideDownPrefsPanel shows a tab that sticks to the bottom of the 
 * header of the page.  It slides down when clicked to reveal a preferences
 * panel for the current page.
 */
public class SlideDownPrefsPanel extends HTMLPanel
{
    private Panel m_panel;
    private Anchor m_anchor;
    
    private boolean m_visible = false;

    private static final SpiffyUIStrings STRINGS = (SpiffyUIStrings) GWT.create(SpiffyUIStrings.class);
    
    /**
     * Creates a new panel
     * @param id - String the id of the panel
     */
    public SlideDownPrefsPanel(String id)
    {
        this(id, null);
    }
    
    /**
     * Create a new panel
     * 
     * @param id - String the id of the panel
     * @param headerText - String the title of the panel and the label on the pull down tab, if null will use the default.
     */
    public SlideDownPrefsPanel(String id, String headerText)
    {       
        super("div", "<div style=\"display: none;\" id=\"" + id + "-panel\" class=\"prefspaneltab\">" +
                         "<h3>" + (headerText == null ? STRINGS.displayOptions() : headerText) + "</h3>" + 
                     "</div>" + 
                     "<div id=\"" + id + "-tab\" class=\"prefspulltab\">" + 
                         "<div id=\"" + id + "-link\"></div>" + 
                     "</div>");

        getElement().setId(id);
        getElement().setClassName("pulldownprefspanel");

        m_anchor = new Anchor(headerText == null ? STRINGS.displayOptions() : headerText);
        m_anchor.addClickHandler(new ClickHandler()
            {
                @Override
                public void onClick(ClickEvent event)
                {
                    event.preventDefault();
                    toggle();
                }
            });
        add(m_anchor, id + "-link");
    }

    @Override
    public void setTitle(String title)
    {
        m_anchor.setTitle(title);
    }

    @Override
    public String getTitle()
    {
        return m_anchor.getTitle();
    }
    
    private void toggle()
    {
        JSUtil.toggleSlide("#" + SlideDownPrefsPanel.this.getElement().getId() + "-panel", "");
        m_visible = !m_visible;
        
        if (m_visible) {
            getElementById(SlideDownPrefsPanel.this.getElement().getId() + "-link").addClassName("prefspulltabexpanded");
        } else {
            getElementById(SlideDownPrefsPanel.this.getElement().getId() + "-link").removeClassName("prefspulltabexpanded");
        }
    }

    /**
     * Set the panel displayed when this panel is expanded
     * 
     * @param panel  the panel to display
     */
    public void setPanel(Panel panel)
    {
        m_panel = panel;
        add(panel, getElement().getId() + "-panel");
    }

    /**
     * get the panel displayed when this panel is expanded
     * 
     * @return the panel
     */

    public Panel getPanel()
    {
        return m_panel;
    }
    
    /**
     * Toggle the panel visible or Invisible
     */
    public void togglePanel()
    {
        toggle();
    }

}
