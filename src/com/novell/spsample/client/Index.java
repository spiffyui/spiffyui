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
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HTMLPanel;

import com.novell.spiffyui.client.JSUtil;
import com.novell.spiffyui.client.MainFooter;
import com.novell.spiffyui.client.MainHeader;
import com.novell.spiffyui.client.MessageUtil;
import com.novell.spiffyui.client.nav.MainNavBar;
import com.novell.spiffyui.client.nav.NavBarListener;
import com.novell.spiffyui.client.nav.NavItem;
import com.novell.spiffyui.client.nav.NavSeparator;
import com.novell.spiffyui.client.rest.RESTAuthProvider;
import com.novell.spiffyui.client.rest.RESTException;
import com.novell.spiffyui.client.rest.RESTLoginCallBack;
import com.novell.spiffyui.client.rest.RESTObjectCallBack;
import com.novell.spiffyui.client.rest.RESTility;
import com.novell.spsample.client.rest.SampleAuthBean;
import com.novell.spsample.client.rest.SampleAuthUtil;
import com.novell.spsample.client.rest.VersionInfo;


/**
 * This class is the main entry point for our GWT module. 
 */
public class Index implements EntryPoint, NavBarListener, RESTLoginCallBack
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
    private static RESTAuthProvider m_authUtil = new SampleAuthUtil();

    private static Index g_index;
    
    private MainHeader m_header;
    
    private MainNavBar m_navBar;
    
    private MainFooter m_footer;
    
    private HashMap<NavItem, ComplexPanel> m_panels = new HashMap<NavItem, ComplexPanel>();

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
        m_header = new MainHeader();
        m_header.setWelcomeString("Welcome Guest");
        m_header.setHeaderTitle("Spiffy UI Sample");
        
        m_footer = new MainFooter();
        loadFooter();
        //loadFooter2();
        
        m_navBar = new MainNavBar();

        /*
         The overview panel
         */
        NavItem item = new NavItem("overviewNavItem", "Spiffy Overview",
                                   "This is the navigation item for the spiffy overview page");
        m_navBar.add(item);
        m_panels.put(item, new OverviewPanel());
        
        /*
         The doc panel
         */
        item = new NavItem("docNavItem", "Get Started",
                           "This is the navigation item for the spiffy doc page");
        m_navBar.add(item);
        m_panels.put(item, new DocPanel());
        
        /*
         The CSS panel
         */
        item = new NavItem("cssNavItem", "Spiffy CSS",
                           "This is the navigation item for the spiffy css page");
        m_navBar.add(item);
        m_panels.put(item, new CSSPanel());

        /*
         The build info panel
         */
        item = new NavItem("buildNavItem", "Spiffy Build",
                           "This is the navigation item for the spiffy build page");
        m_navBar.add(item);
        m_panels.put(item, new BuildPanel());
        
        /*
        The authentication info panel
        */
       item = new NavItem("authNavItem", "Spiffy Authentication",
                          "This is the navigation item for the spiffy authentication page");
       m_navBar.add(item);
       m_panels.put(item, new AuthPanel());
       
        /*
        The date info panel
        */
        item = new NavItem("dateNavItem", "Spiffy Dates",
                          "This is the navigation item for the spiffy date localization page");
        m_navBar.add(item);
        m_panels.put(item, new DatePanel());
       
        /*
         A separator
         */
        m_navBar.add(new NavSeparator(HTMLPanel.createUniqueId()));
        
        /*
         The sample widgets panel
         */
        item = new NavItem("page1NavItem", "Sample Widgets",
                                   "This is the navigation item for the simple widgets page");
        m_navBar.add(item);
        m_panels.put(item, new WidgetsPanel());
        
        /*
         The sample form panel
         */
        item = new NavItem("page2NavItem", "Sample Form",
                           "This is the navigation item for the simple form page");
        m_navBar.add(item);
        m_panels.put(item, new FormPanel());
        
        m_navBar.selectItem(m_navBar.getItem("overviewNavItem"));
        
        m_navBar.addListener(this);
        RESTility.addLoginListener(this);
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

    private void loadFooter()
    {
        VersionInfo.getVersionInfo(new RESTObjectCallBack<VersionInfo>() {
            public void success(VersionInfo info)
            {
                m_footer.setFooterString("Spiffy UI Sample version " + info.getVersion() +
                                         " was built on " + DateTimeFormat.getFullDateFormat().format(info.getDate()) +
                                         " from revision " + info.getRevision());
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

    private void loadFooter2()
    {
        SampleAuthBean.getSampleAuthData(new RESTObjectCallBack<SampleAuthBean>() {
            public void success(SampleAuthBean info)
            {
                m_footer.setFooterString("Result: " + info.getMessage() +
                                         " received on " + DateTimeFormat.getFullDateFormat().format(info.getDate()));
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
    public void updateMainHeader()
    {
        if (RESTility.getUserToken() == null) {
            return;
        }
        m_header.setWelcomeString("Welcome " + RESTility.getUserToken());
    }

    /**
     * set the authentication provider used by REStitlity so that this authentication provider
     * will be used instead of the default authentication provider that comes with spiffy framework
     *
     */
    public static void setAuthProvider() {
        RESTility.setAuthProvider(m_authUtil);
    }

    /**
     * When login is successful, make the application visible
    */
    public void onLoginSuccess() {
        Index.showApplication();
        updateMainHeader();
    }

    public void loginPrompt() {
        //no-op
    }
}
