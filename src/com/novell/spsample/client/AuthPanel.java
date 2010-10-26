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
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;

import com.novell.spiffyui.client.MessageUtil;
import com.novell.spiffyui.client.rest.RESTException;
import com.novell.spiffyui.client.rest.RESTObjectCallBack;
import com.novell.spiffyui.client.rest.RESTility;
import com.novell.spiffyui.client.widgets.button.SimpleButton;
import com.novell.spsample.client.rest.SampleAuthBean;

/**
 * This is the authentication documentation panel
 *
 */
public class AuthPanel extends HTMLPanel
{
    private static final SPSampleStrings STRINGS = (SPSampleStrings) GWT.create(SPSampleStrings.class);
    private static AuthPanel g_authPanel;
    
    /**
     * Creates a new panel
     */
    public AuthPanel()
    {
        super("div", STRINGS.AuthPanel_html());
        
        getElement().setId("authPanel");
        
        RootPanel.get("mainContent").add(this);

        setVisible(false);
        String buttonText = "";
        if (RESTility.hasUserLoggedIn()) {
            buttonText = "Logout";
        } else {
            buttonText = "Login and Get Some Data";
        }
        final SimpleButton authTestButton = new SimpleButton(buttonText);

        authTestButton.getElement().setId("authTestBtn");
        this.add(authTestButton, "testAuth");

        authTestButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event)
            {
                authTestButton.setInProgress(true);
                //a little timer to simulate time it takes to set in progress back to false
                Timer t = new Timer() {
                    @Override
                    public void run()
                    {
                        authTestButton.setInProgress(false);
                    }

                };
                t.schedule(2000);
                getData();
            }
        });
        g_authPanel = this;
    }

    private void getData()
    {
        SampleAuthBean.getSampleAuthData(new RESTObjectCallBack<SampleAuthBean>() {
            public void success(SampleAuthBean info)
            {
                String data = "You've logged in as " + info.getName() +
                              //" on " + DateTimeFormat.getFullDateFormat().format(info.getDate()) +
                              " and " + info.getMessage();
                g_authPanel.add(new HTML("<p>" + data + "</p>"), "testAuthResult");
                
                
                /*
                 Add a yellow highlight to show that you've logged in
                 */
                RootPanel.get("loginSection").getElement().addClassName("yellowHighlightSection");
            }

            public void error(String message)
            {
                MessageUtil.showFatalError(message);
            }

            public void error(RESTException e)
            {
                MessageUtil.showFatalError(e.getReason());
            }
        });
   }
}
