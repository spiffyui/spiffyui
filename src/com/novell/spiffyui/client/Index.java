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
package com.novell.spiffyui.client;

import com.google.gwt.core.client.EntryPoint;


/**
 * This class is the main entry point for our GWT module. 
 */
public class Index implements EntryPoint
{

    private static Index g_index;
    private boolean m_isVisible = false;

    /**
     * The Index page constructor
     */
    public Index()
    {
        g_index = this;
    }
    
    
    @Override
    public void onModuleLoad()
    {
        
    }

    /**
     * Sets the application visible once we have logged in.
     * People worry about security when they see controls
     * before we've logged in.
     */
    public static void showApplication()
    {
        if (!g_index.m_isVisible) {
            JSUtil.show("#mainFooter", "fast");
            JSUtil.show("body > #mainWrap > #main", "fast");
            g_index.m_isVisible = true;
        }
    }

    /**
     * Sets the application invisible.  This is called by the
     * authentication framework to hide the UI when we need
     * to show the login dialog again.
     */
    public static void hideApplication()
    {
        if (g_index.m_isVisible) {
            JSUtil.hide("#mainFooter", "fast");
            JSUtil.hide("body > #mainWrap > #main", "fast");
            g_index.m_isVisible = false;
        }
    }

     /**
     * This is a utility method to get the name of the current logged in
     * user and apply it to the main header of the application.
     *
     */
    public static void updateMainHeader()
    {
        //todo: update welcome message
    }
}
