/*******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/
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
