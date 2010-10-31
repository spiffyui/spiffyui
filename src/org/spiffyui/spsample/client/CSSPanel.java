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
package org.spiffyui.spsample.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;

import org.spiffyui.client.JSUtil;

/**
 * This is the CSS panel
 *
 */
public class CSSPanel extends HTMLPanel
{
    private static final SPSampleStrings STRINGS = (SPSampleStrings) GWT.create(SPSampleStrings.class);
    
    /**
     * Creates a new panel
     */
    public CSSPanel()
    {
        super("div", STRINGS.CSSPanel_html());
        
        getElement().setId("cssPanel");
        
        RootPanel.get("mainContent").add(this);
        
        setVisible(false);
        
        /*
         This button handles showing and hiding the tab navigation CSS
         */
        final Button tabNavButton = new Button("tab navigation");
        tabNavButton.addClickHandler(new ClickHandler()
            {
                @Override
                public void onClick(ClickEvent event)
                {
                    if (tabNavButton.getText().equals("tab navigation")) {
                        RootPanel.get("mainWrap").getElement().addClassName("tabnav");
                        RootPanel.get("mainFooter").getElement().addClassName("tabnav");
                        tabNavButton.setText("menu navigation");
                    } else {
                        RootPanel.get("mainWrap").getElement().removeClassName("tabnav");
                        RootPanel.get("mainFooter").getElement().removeClassName("tabnav");
                        tabNavButton.setText("tab navigation");
                    }
                }
            });
        add(tabNavButton, "tabnavbutton");
        
        /*
         The grid button can show and hide the grid background
         */
        final Button gridButton = new Button("Grid On");
        
        /*
         The fixed grid button is a tab that sticks to the left
         side of the window so you can switch off the grid even
         if you switch to another panel
         */
        final FlowPanel fixedPanel = new FlowPanel();
        final Button fixedgridButton = new Button(" ");
        fixedgridButton.setTitle("Turn off the grid");
        fixedPanel.getElement().setId("fixedGridTab");
        
        gridButton.addClickHandler(new ClickHandler()
            {
                @Override
                public void onClick(ClickEvent event)
                {
                    if (gridButton.getText().equals("Grid On")) {
                        RootPanel.get("main").getElement().addClassName("grid");
                        gridButton.setText("Grid Off");
                        JSUtil.horizontalToggleSlide("#fixedGridTab");
                    } else {
                        RootPanel.get("main").getElement().removeClassName("grid");
                        gridButton.setText("Grid On");
                        JSUtil.horizontalToggleSlide("#fixedGridTab");
                    }
                }
            });
        add(gridButton, "gridbutton");
        
        fixedPanel.add(fixedgridButton);
        
        fixedgridButton.addClickHandler(new ClickHandler()
            {
                @Override
                public void onClick(ClickEvent event)
                {
                    RootPanel.get("main").getElement().removeClassName("grid");
                    gridButton.setText("Grid On");
                    JSUtil.horizontalToggleSlide("#fixedGridTab");
                }
            });
        
        fixedPanel.setVisible(false);
        RootPanel.get("mainWrap").add(fixedPanel);
        
    }
}
