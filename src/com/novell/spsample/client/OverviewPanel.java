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

/**
 * This is the overview panel
 *
 */
public class OverviewPanel extends HTMLPanel
{
	/**
     * Creates a new panel
     */
    public OverviewPanel()
    {
        super("div", 
             "<h1>Spiffy UI Framework</h1><br /><br />" + 
              "<div id=\"overviewPanelText\">" + 
                  "<p>" + 
                    "Welcome to SPSample the sample application for the Spiffy UI Framework.  " +
                    "Spiffy UI gives you a place to start with GWT, JQuery, CSS, REST, security, " +
                    "and a set of helpful widgets.  This sample application shows you how to use " +
                    "the framework and gives you a large amount of sample code." +
                  "</p>" +
    
                  "<h2>Spiffy philosophy</h2>" +
                  "<p>" +
                    "The Spiffy UI framework started as part of the Identity Manager Reporting Module and was " +
                    "later extracted into a reusable framework.  The philosophy of the framework is best explained " +
                    "in the Spiffy UI video." +
                  "</p>" +

                  "<p>" + 
                  "<object width=\"400\" height=\"225\"><param name=\"allowfullscreen\" value=\"true\" /><param " +
                  "name=\"allowscriptaccess\" value=\"always\" /><param name=\"movie\" value=\"" +
                  "http://vimeo.com/moogaloop.swf?clip_id=13950436&amp;server=vimeo.com&amp;show_title=1&amp;color=&amp;fullscreen=1&amp;autoplay=0&amp;loop=0\" />" +
                  "<embed src=\"http://vimeo.com/moogaloop.swf?clip_id=13950436&amp;server=vimeo.com&amp;show_title=1&amp;color=&amp;fullscreen=1&amp;autoplay=0&amp;loop=0\" " +
                  "type=\"application/x-shockwave-flash\" allowfullscreen=\"true\" allowscriptaccess=\"always\" " +
                  "width=\"580\" height=\"326\"></embed></object>" +
                  "</p>" +
    
                  "<p>" +
                    "The rules we follow in this framework are explained in <a " +
                    "href=\"http://www.zackgrossbart.com/hackito/rpt-css/\">Fluid Grids, Vertical Rhythm, and CSS " +
                    "Blocking</a>." +
                  "</p>" +
    
                  "<p>" +
                    "The mechanism we use for calling REST APIs from JavaScript and GWT is discussed in <a " +
                    "href=\"http://www.zackgrossbart.com/hackito/gwt-rest/\">Calling REST from GWT with a little " +
                    "bit of JQuery</a>." +
                  "</p>" +
    
                  "<p>" +
                    "The multi-valued auto-complete control in this framework is explained in detail in <a " +
                    "href=\"http://www.zackgrossbart.com/hackito/gwt-rest-auto/\">Creating a Multi-Valued Auto-Complete " +
                    "Field Using GWT SuggestBox and REST</a>" +
                  "</p>" +
              "</div>"
             );
        
        getElement().setId("overviewPanel");
        
        RootPanel.get("mainContent").add(this);
    }
}
