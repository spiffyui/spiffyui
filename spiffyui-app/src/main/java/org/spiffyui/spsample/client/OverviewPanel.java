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
        
        /*
         * Add TOC anchors
         */
        Index.addTocAnchor(this, "liOV_Who");
        Index.addTocAnchor(this, "liOV_Browsers");
        Index.addTocAnchor(this, "liOV_Servers");
        Index.addTocAnchor(this, "liOV_Flexibility");
        Index.addTocAnchor(this, "liOV_Mobile");
        Index.addTocAnchor(this, "liOV_Libs");
    }
}
