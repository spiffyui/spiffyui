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
package org.spiffyui.spsample.client;

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
        Anchor rest = new Anchor(Index.getStrings().restInt(), "RESTPanel");
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
        Anchor restError = new Anchor(Index.getStrings().errHandle(), "RESTPanel");
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
        Anchor auth = new Anchor(Index.getStrings().security(), "AuthPanel");
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
        Anchor css = new Anchor(Index.getStrings().cssTemp(), "CSSPanel");
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
        Anchor dates = new Anchor(Index.getStrings().dateL10n(), "DatesPanel");
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
        Anchor form = new Anchor(Index.getStrings().formLayouts(), "FormPanel");
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
        Anchor widgets = new Anchor(Index.getStrings().widgets(), "WidgetsPanel");
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
        Anchor build = new Anchor(Index.getStrings().superSpeed(), "SpeedPanel");
        build.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event)
            {
                event.preventDefault();
                Index.selectItem(Index.SPEED_NAV_ITEM_ID);
            }
        });
        add(build, "overviewBuild");

        /*
         * Add the get started anchor
         */
        Anchor getStarted = new Anchor(Index.getStrings().gettingStarted(), "getStartedPanel");
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
