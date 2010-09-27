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

import java.util.HashMap;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HTMLPanel;

import com.novell.spiffyui.client.MainFooter;
import com.novell.spiffyui.client.MainHeader;
import com.novell.spiffyui.client.MessageUtil;
import com.novell.spiffyui.client.nav.MainNavBar;
import com.novell.spiffyui.client.nav.NavBarListener;
import com.novell.spiffyui.client.nav.NavItem;
import com.novell.spiffyui.client.nav.NavSeparator;


/**
 * This class is the main entry point for our GWT module. 
 */
public class Index implements EntryPoint, NavBarListener
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
    
    private MainHeader m_header;
    
    private MainNavBar m_navBar;
    
    private MainFooter m_footer;
    
    private HashMap<NavItem, ComplexPanel> m_panels = new HashMap<NavItem, ComplexPanel>();
    
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
        m_header = new MainHeader();
        m_header.setWelcomeString("Welcome Guest");
        m_header.setHeaderTitle("Spiffy UI Sample");
        
        m_footer = new MainFooter();
        m_footer.setFooterString("This is the main footer");
        
        m_navBar = new MainNavBar();
        
        /*
         The page 1 panel
         */
        NavItem item = new NavItem("page1NavItem", "Page 1",
                                   "This is the navigation item for page 1");
        m_navBar.add(item);
        m_panels.put(item, new Page1Panel());
        
        /*
         A separator
         */
        m_navBar.add(new NavSeparator(HTMLPanel.createUniqueId()));
        
        /*
         The page 2 panel
         */
        item = new NavItem("page2NavItem", "Page 2",
                           "This is the navigation item for page 2");
        m_navBar.add(item);
        m_panels.put(item, new Page2Panel());
        
        m_navBar.selectItem(m_navBar.getItem("page1NavItem"));
        
        m_navBar.addListener(this);
        
        
        MessageUtil.showMessage("I'm a sample information message");
        
    }
    
    @Override
    public boolean preItemSelected(NavItem item)
    {
        return true;
    }
    
    @Override
    public void itemSelected(NavItem item)
    {
        for (NavItem key : m_panels.keySet()) {
            if (key.equals(item)) {
                m_panels.get(key).setVisible(true);
            } else {
                m_panels.get(key).setVisible(false);
            }
        }
    }
}
