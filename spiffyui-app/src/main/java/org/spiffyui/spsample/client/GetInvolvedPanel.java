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
 * This is the get involved panel
 *
 */
public class GetInvolvedPanel extends HTMLPanel
{
    private static final SPSampleStrings STRINGS = (SPSampleStrings) GWT.create(SPSampleStrings.class);
    
    /**
     * Creates a new panel
     */
    public GetInvolvedPanel()
    {
        super("div", STRINGS.GetInvolvedPanel_html());
        
        getElement().setId("getInvolvedPanel");
        
        RootPanel.get("mainContent").add(this);
        
        setVisible(false);
        
        /*
         * Add the overview anchor
         */
        Anchor overview = new Anchor(Index.getStrings().overviewLink(), "#");
        overview.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event)
            {
                event.preventDefault();
                Index.selectItem(Index.OVERVIEW_NAV_ITEM_ID);
            }
        });
        add(overview, "supportMatrixLink");
        
    }
}
