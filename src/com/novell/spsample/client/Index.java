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
import com.novell.spiffyui.client.nav.NavSection;
import com.novell.spiffyui.client.nav.NavSeparator;
import com.novell.spiffyui.client.rest.RESTException;
import com.novell.spiffyui.client.rest.RESTLoginCallBack;
import com.novell.spiffyui.client.rest.RESTObjectCallBack;
import com.novell.spiffyui.client.rest.RESTility;
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
    
    private static Index g_index;
    
    private SPSampleHeader m_header;
    
    private MainNavBar m_navBar;
    
    private MainFooter m_footer;
    
    private HashMap<NavItem, ComplexPanel> m_panels = new HashMap<NavItem, ComplexPanel>();

    private boolean m_isVisible = false;
    
    /** NavItem ID for Overview */
    public static final String OVERVIEW_NAV_ITEM_ID = "overviewNavItem";
    /** NavItem ID for Get Started */
    public static final String GET_STARTED_NAV_ITEM_ID = "docNavItem";
    /** NavItem ID for CSS */
    public static final String CSS_NAV_ITEM_ID = "cssNavItem";
    /** NavItem ID for Build */
    public static final String BUILD_NAV_ITEM_ID = "buildNavItem";
    /** NavItem ID for REST */
    public static final String REST_NAV_ITEM_ID = "restNavItem";
    /** NavItem ID for JavaDoc */
    public static final String JAVADOC_NAV_ITEM_ID = "javaDocNavItem";
    /** NavItem ID for Help */
    public static final String HELP_ITEM_ID = "helpNavItem";
    /** NavItem ID for Authentication */
    public static final String AUTH_NAV_ITEM_ID = "authNavItem";
    /** NavItem ID for Dates */
    public static final String DATES_NAV_ITEM_ID = "dateNavItem";
    /** NavItem ID for Form */
    public static final String FORM_NAV_ITEM_ID = "page2NavItem";
    /** NavItem ID for Widgets */
    public static final String WIDGETS_NAV_ITEM_ID = "page1NavItem";
    
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
        /*
         We are setting a custom authentication provider.  Custom authentication
         providers can override the UI of the login or provide access to totally
         new authentication mechanisms.  Ours just overrides a string in the
         login panel
         */
        RESTility.setAuthProvider(new SampleAuthUtil());
        
        m_header = new SPSampleHeader();
        m_header.setHeaderTitle("SPIFFY <span id=\"mainsubtitle\">The UI/UX framework</span>");
        
        m_footer = new MainFooter();
        loadFooter();
        
        m_navBar = new MainNavBar();

        /*
         The overview panel
         */
        NavItem item = new NavItem(OVERVIEW_NAV_ITEM_ID, "Spiffy Overview",
                                   "Overview information for the Spiffy UI framework");
        m_navBar.add(item);
        m_panels.put(item, new OverviewPanel());
        
        /*
         The doc panel
         */
        item = new NavItem(GET_STARTED_NAV_ITEM_ID, "Get Started",
                           "Get started with information about how to use Spiffy UI");
        m_navBar.add(item);
        m_panels.put(item, new GetStartedPanel());
        
        /*
         * Collapsible Features Nav Section
         */
        NavSection featureSection = new NavSection("featuresNavSection", "Features");
        featureSection.setTitle("Spiffy UI features");
        m_navBar.add(featureSection);
        
        /*
        The authentication info panel
        */
        item = new NavItem(AUTH_NAV_ITEM_ID, "Authentication",
                          "Spiffy UI authentication information");
        featureSection.add(item);
        m_panels.put(item, new AuthPanel());
 
        /*
        The build info panel
        */
        item = new NavItem(BUILD_NAV_ITEM_ID, "Build",
                          "Spiffy UI build information");
        featureSection.add(item);
        m_panels.put(item, new BuildPanel());
       
        /*
         The CSS panel
         */
        item = new NavItem(CSS_NAV_ITEM_ID, "CSS",
                           "The Spiffy UI CSS classes and philosophy");
        featureSection.add(item);
        m_panels.put(item, new CSSPanel());

        /*
        The date info panel
        */
        item = new NavItem(DATES_NAV_ITEM_ID, "Dates",
                          "Date pickers, time pickers, and localization in the Spiffy UI framework");
        featureSection.add(item);
        m_panels.put(item, new DatePanel());

        /*
         The rest info panel
         */
        item = new NavItem(REST_NAV_ITEM_ID, "REST",
                           "Patterns and helpers for calling REST");
        featureSection.add(item);
        m_panels.put(item, new RESTPanel());
        
        /*
         * Collapsible Features Nav Section
         */
        NavSection demoSection = new NavSection("demoNavSection", "Demos");
        demoSection.setTitle("Spiffy UI demonstration of some features");
        m_navBar.add(demoSection);

        /*
        The sample form panel
         */
        item = new NavItem(FORM_NAV_ITEM_ID, "Form",
                            "Form styles and handling in the Spiffy UI framework");
        demoSection.add(item);
        m_panels.put(item, new FormPanel());

        /*
        The sample widgets panel
         */
        item = new NavItem(WIDGETS_NAV_ITEM_ID, "Widgets",
                            "Take a look at the widgets that come with the Spiffy UI framework");
        demoSection.add(item);
        m_panels.put(item, new WidgetsPanel());       

        /*
        A separator
        */
        m_navBar.add(new NavSeparator(HTMLPanel.createUniqueId()));

        /*
        The JavaDoc panel
        */
        item = new NavItem(JAVADOC_NAV_ITEM_ID, "JavaDoc",
                          "Java API documentation for the Spiffy UI framework");
        m_navBar.add(item);
        m_panels.put(item, new JavaDocPanel());
        
        /*
        The help panel
        */
        item = new NavItem(HELP_ITEM_ID, "Get Help",
                          "Get help with the Spiffy UI framework");
        m_navBar.add(item);
        m_panels.put(item, new HelpPanel());
       

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
            /*
             We could hide and show these panels by just calling setVisible,
             but that causes a redraw bug in IE 8 where the body extends for
             for the total height of the page below the footer.
             */
            if (key.equals(item)) {
                JSUtil.show("#" + m_panels.get(key).getElement().getId());
            } else {
                JSUtil.hide("#" + m_panels.get(key).getElement().getId(), "fast");
            }
        }
    }

    /**
     * Select the NavItem
     * @param itemId - ID of the NavItem to select
     */
    public static void selectItem(String itemId)
    {
        NavItem item = g_index.m_navBar.getItem(itemId);
        g_index.m_navBar.selectItem(item);
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
        if (RESTility.getUserToken() == null) {
            return;
        }
        String token = RESTility.getUserToken();
        String name = token.substring(0, token.indexOf("-"));
        g_index.m_header.setWelcomeString("Welcome " + name);
        JSUtil.bounce("#" + MainHeader.HEADER_ACTIONS_BLOCK, 5, 500, 30);
        JSUtil.show("#header_logout", "fast");
    }

    /**
     * returns whether the  user is logged in or not
     * @return true if the user is logged in (browser cookie is there)
     */
    public static boolean userLoggedIn()
    {
        String userToken = RESTility.getUserToken();
        if ((userToken == null) || (userToken.length() <= 0)) {
            return false;
        }
        return true;
    }

    /**
     * When login is successful, make the application visible
    */
    public void onLoginSuccess()
    {
        Index.showApplication();
        updateMainHeader();
        updateAuthTestButton();
        updateLoginWidgetButton();
    }

    /**
     * do nothing
    */
    public void loginPrompt()
    {
        //no-op
    }

    private void updateAuthTestButton()
    {
      JSUtil.setText("#authTestBtn", "Get More Secured Data");
    }

    private void updateLoginWidgetButton()
    {
      JSUtil.setText("#doLoginBtn", "Get More Secured Data");
    }
}
