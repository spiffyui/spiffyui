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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;

import com.novell.spiffyui.client.JSUtil;
import com.novell.spiffyui.client.MainHeader;
import com.novell.spiffyui.client.MessageUtil;
import com.novell.spiffyui.client.rest.RESTException;
import com.novell.spiffyui.client.rest.RESTObjectCallBack;
import com.novell.spiffyui.client.rest.RESTility;

/**
 * This is the header for SPSample.
 *
 */
public class SPSampleHeader extends MainHeader
{
    /**
     * Creates a new SPSampleHeader panel
     */
    public SPSampleHeader()
    {
        Anchor logout = new Anchor("Logout", "#");
        logout.getElement().setId("header_logout");
        setLogout(logout);
        JSUtil.hide("#header_logout", "fast");
        logout.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event)
                {
                    event.preventDefault();
                    doLogout();
                }
            });
    }

    /**
     * Logout of the application
     */
    public static void doLogout()
    {
        RESTility.getAuthProvider().logout(new RESTObjectCallBack<String>()
                        {
            public void success(String message)
            {
                Window.Location.reload();
            }

            public void error(String message)
            {
                Window.Location.reload();
            }

            public void error(RESTException e)
            {
                MessageUtil.showFatalError(e.getReason());
            }
        });
    }
}

