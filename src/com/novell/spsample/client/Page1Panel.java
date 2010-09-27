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
package com.novell.spsample.client;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;

import com.novell.spiffyui.client.widgets.LongMessage;
import com.novell.spiffyui.client.widgets.ProgressBar;

/**
 * This is the page 1 panel
 *
 */
public class Page1Panel extends HTMLPanel
{
    /**
     * Creates a new import panel
     */
    public Page1Panel()
    {
        super("div", "<h1>Some example Widgets</h1><br /><br />" + 
                     "<div id=\"Page1LongMessage\"></div><br /><br />" + 
                     "<div>Progress bar:<br />" + 
                        "<span id=\"Page1ProgressSpan\"></span>" + 
                     "</div>");
        
        getElement().setId("page1Panel");
        
        RootPanel.get("mainContent").add(this);
        
        /*
         Add a progress bar to our page
         */
        ProgressBar bar = new ProgressBar("Page1PanelProgressBar");
        bar.setValue(65);
        add(bar, "Page1ProgressSpan");
        
        /*
         Add a long message to our page
         */
        LongMessage message = new LongMessage("Page1LongMessageWidget");
        add(message, "Page1LongMessage");
        message.setHTML("<b>Long Message</b><br />" + 
                             "Long messages are useful for showing information messages " +
                             "with more content than the standard messages but are still " +
                             "transient messages.");
    }
}
