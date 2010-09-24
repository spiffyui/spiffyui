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

import com.google.gwt.core.client.EntryPoint;

import com.novell.spiffyui.client.MainFooter;
import com.novell.spiffyui.client.MainHeader;
import com.novell.spiffyui.client.MessageUtil;


/**
 * This class is the main entry point for our GWT module. 
 */
public class Index implements EntryPoint
{

    static {
        /*
         The order of the HTML elements matters here, but GWT doesn't
         give us easy access to the element order.  We call this to make
         sure the error panel HTML element gets added first.  Hackito
         ergo sum.
         */
        Object o = MessageUtil.ERROR_PANEL;
    }
    
    private static Index g_index;
    
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
        MainHeader header = new MainHeader();
        header.setWelcomeString("Welcome Guest");
        header.setHeaderTitle("Spiffy UI Sample");
        
        MainFooter footer = new MainFooter();
        footer.setFooterString("This is the main footer");
        
        MessageUtil.showError("I'm a sample error message");
        
    }
}
