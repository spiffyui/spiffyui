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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;

import com.novell.spiffyui.client.MessageUtil;
import com.novell.spiffyui.client.widgets.LongMessage;
import com.novell.spiffyui.client.widgets.ProgressBar;
import com.novell.spiffyui.client.widgets.SmallLoadingIndicator;
import com.novell.spiffyui.client.widgets.StatusIndicator;
import com.novell.spiffyui.client.widgets.button.RefreshAnchor;

/**
 * This is the page 1 panel
 *
 */
public class Page1Panel extends HTMLPanel
{
    /**
     * Creates a new panel
     */
    public Page1Panel()
    {
        super("div", "<h1>Some example Widgets</h1><br /><br />" + 
                     "<div id=\"Page1LongMessage\"></div><br /><br />" + 
                     "<div>Progress bar:<br />" + 
                        "<span id=\"Page1ProgressSpan\"></span>" + 
                     "</div><br /><br />" +
                     "<div>Small loading indicator:<br />" +
                     	"<span id=\"Page1SmallLoading\"></span>" + 
                     "</div><br /><br />" +
                     "<div>3 states of the status indicator:<br />" +
                   		"<span id=\"Page1Status\"></span>" + 
                   	 "</div><br /><br />" +
                     "<div>Refresh anchor:<br />" +
                     "<span id=\"Page1RefreshAnchor\"></span>" +
                     "</div><br /><br />");
        
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
        
        /*
         * Add a small loading indicator to our page
         */
        SmallLoadingIndicator loading = new SmallLoadingIndicator();
        add(loading, "Page1SmallLoading");
        
        /*
         * Add 3 status indicators 
         */
        StatusIndicator status1 = new StatusIndicator(StatusIndicator.IN_PROGRESS);
        StatusIndicator status2 = new StatusIndicator(StatusIndicator.SUCCEEDED);
        StatusIndicator status3 = new StatusIndicator(StatusIndicator.FAILED);
        add(status1, "Page1Status");
        add(status2, "Page1Status");
        add(status3, "Page1Status");
        /*
         * Add a refresh anchor to our page
         */
        final RefreshAnchor refresh = new RefreshAnchor("Page1_refreshAnchor");
        add(refresh, "Page1RefreshAnchor");
        refresh.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) 
            {
            	refresh.setLoading(true);
                MessageUtil.showMessage("Call to refresh!");
                //a little timer to set loading back to false
                Timer t = new Timer() {

					@Override
					public void run() {
						refresh.setLoading(false);
					}
                	
                };
                t.schedule(2000);
            }
            
        });
    }
}
