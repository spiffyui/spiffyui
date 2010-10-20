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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * This is the overview panel
 *
 */
public class OverviewPanel extends HTMLPanel
{
    private static final SPSampleStrings STRINGS = (SPSampleStrings) GWT.create(SPSampleStrings.class);
    
	/**
     * Creates a new panel
     */
    public OverviewPanel()
    {
        super("div", STRINGS.OverviewPanel_html());
        
        getElement().setId("overviewPanel");
        
        RootPanel.get("mainContent").add(this);
        
        /*
         * Add the REST anchor
         */
        Anchor rest = new Anchor("Framework and patterns for calling REST with JSON", "RESTPanel");
        rest.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event)
            {
                event.preventDefault();
                Index.selectItem(Index.REST_NAV_ITEM_ID);
            }
        });
        add(rest, "overviewRest");
        
        /*
         * Add the REST error handling anchor
         */
        Anchor restError = new Anchor("REST error handling framework", "RESTPanel");
        restError.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event)
            {
                event.preventDefault();
                Index.selectItem(Index.REST_NAV_ITEM_ID);
            }
        });
        add(restError, "overviewRestError");
     
        /*
         * Add the authentication anchor
         */
        Anchor auth = new Anchor("Authentication and security UI and infrastructure", "AuthPanel");
        auth.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event)
            {
                event.preventDefault();
                Index.selectItem(Index.AUTH_NAV_ITEM_ID);
            }
        });
        add(auth, "overviewAuth");
        
        /*
         * Add the css anchor
         */
        Anchor css = new Anchor("CSS templates and reusable styles", "CSSPanel");
        css.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event)
            {
                event.preventDefault();
                Index.selectItem(Index.CSS_NAV_ITEM_ID);
            }
        });
        add(css, "overviewCss");
        
        /*
         * Add the date anchor
         */
        Anchor dates = new Anchor("Date handling and localization", "DatesPanel");
        dates.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event)
            {
                event.preventDefault();
                Index.selectItem(Index.DATES_NAV_ITEM_ID);
            }
        });
        add(dates, "overviewDate");
        
        /*
         * Add the form anchor
         */
        Anchor form = new Anchor("Form layouts", "FormPanel");
        form.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event)
            {
                event.preventDefault();
                Index.selectItem(Index.FORM_NAV_ITEM_ID);
            }
        });
        add(form, "overviewForm");
        
        /*
         * Add the widgets anchor
         */
        Anchor widgets = new Anchor("Reusable widgets", "WidgetsPanel");
        widgets.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event)
            {
                event.preventDefault();
                Index.selectItem(Index.WIDGETS_NAV_ITEM_ID);
            }
        });
        add(widgets, "overviewWidgets");
        
        /*
         * Add the build anchor
         */
        Anchor build = new Anchor("Build integration with JavaScript and CSS compression", "BuildPanel");
        build.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event)
            {
                event.preventDefault();
                Index.selectItem(Index.BUILD_NAV_ITEM_ID);
            }
        });
        add(build, "overviewBuild");
        
        /*
         * Add the get started anchor
         */
        Anchor getStarted = new Anchor("getting started", "GetStartedPanel");
        getStarted.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event)
            {
                event.preventDefault();
                Index.selectItem(Index.GET_STARTED_NAV_ITEM_ID);
            }
        });
        add(getStarted, "overviewGetStarted");
    }
}
