/*******************************************************************************
 *
 * Copyright 2011 Spiffy UI Team   
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
 * This is the landing panel
 *
 */
public class LandingPanel extends HTMLPanel
{
    private static final SPSampleStrings STRINGS = (SPSampleStrings) GWT.create(SPSampleStrings.class);
        
    /**
     * Creates a new panel
     */
    public LandingPanel()
    {
        super("div", STRINGS.LandingPanel_html());

        getElement().setId("landingPanel");

        RootPanel.get("mainContent").add(this);
        
        addAnchor(Index.getStrings().documentation(), Index.OVERVIEW_NAV_ITEM_ID, "landingDoc");
        addAnchor(Index.getStrings().samples(), Index.SAMPLES_NAV_ITEM_ID, "landingSample");
        addAnchor(Index.getStrings().download(), Index.GET_STARTED_NAV_ITEM_ID, "landingDownload");
        
        getElementById("mobileCalloutText").setInnerHTML(Index.getStrings().mobileCallout(
            "<span id=\"mobileCSSLink\"></span>", "<span id=\"mobileSpeedLink\"></span>"));
        addAnchor(Index.getStrings().flexibleCSS(), Index.CSS_NAV_ITEM_ID, "mobileCSSLink");
        addAnchor(Index.getStrings().downloadSize(), Index.SPEED_NAV_ITEM_ID, "mobileSpeedLink");
        
        getElementById("restCalloutText").setInnerHTML(Index.getStrings().restCallout(
            "<span id=\"restRestLink\"></span>"));
        addAnchor(Index.getStrings().restility(), Index.REST_NAV_ITEM_ID, "restRestLink");
        
        getElementById("secCalloutText").setInnerHTML(Index.getStrings().secCallout(
            "<span id=\"secSecLink\"></span>"));
        addAnchor(Index.getStrings().secureLink(), Index.AUTH_NAV_ITEM_ID, "secSecLink");
        
        getElementById("licCalloutText").setInnerHTML(Index.getStrings().licCallout(
            "<span id=\"licLicLink\"></span>"));
        addAnchor(Index.getStrings().apacheLic(), Index.LICENSE_NAV_ITEM_ID, "licLicLink");
        
        
        /*
         * Add Project Creator
         */
        ProjectCreatorPanel projCreator = new ProjectCreatorPanel("landingPanelForm");
        add(projCreator, "createSpiffy");
    }
    
    private void addAnchor(final String text, final String id, final String location)
    {
        Anchor link = new Anchor(text, "#");
        link.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event)
            {
                event.preventDefault();
                Index.selectItem(id);
            }
        });
        add(link, location);
    }
    
}
