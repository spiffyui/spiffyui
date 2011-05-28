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

import java.util.HashMap;

import org.spiffyui.client.JSDateUtil;
import org.spiffyui.client.JSUtil;
import org.spiffyui.client.MainFooter;
import org.spiffyui.client.MainHeader;
import org.spiffyui.client.MessageUtil;
import org.spiffyui.client.nav.MainNavBar;
import org.spiffyui.client.nav.NavBarListener;
import org.spiffyui.client.nav.NavItem;
import org.spiffyui.client.nav.NavSection;
import org.spiffyui.client.nav.NavSeparator;
import org.spiffyui.client.rest.RESTException;
import org.spiffyui.client.rest.RESTLoginCallBack;
import org.spiffyui.client.rest.RESTObjectCallBack;
import org.spiffyui.client.rest.RESTility;
import org.spiffyui.spsample.client.i18n.SpiffyRsrc;
import org.spiffyui.spsample.client.rest.SampleAuthUtil;
import org.spiffyui.spsample.client.rest.VersionInfo;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;


/**
 * This class is the main entry point for our GWT module.
 */
public class Index implements EntryPoint, NavBarListener, RESTLoginCallBack
{
    private static final SpiffyRsrc STRINGS = (SpiffyRsrc) GWT.create(SpiffyRsrc.class);
    
    private static final String NAV_COOKIE = "SpiffyUI_Sample_Navigation";

    private static Index g_index;

    private SPSampleHeader m_header;

    private MainNavBar m_navBar;

    private MainFooter m_footer;

    private final HashMap<NavItem, ComplexPanel> m_panels = new HashMap<NavItem, ComplexPanel>();

    private boolean m_isVisible = false;
    
    private boolean m_isSausage = false;

    /** NavItem ID for Landing */
    public static final String LANDING_NAV_ITEM_ID = "landing";
    /** NavItem ID for Overview */
    public static final String OVERVIEW_NAV_ITEM_ID = "overview";
    /** NavItem ID for Get Started Intro*/
    public static final String GET_STARTED_NAV_ITEM_ID = "getStarted";
    /** NavItem ID for Hosted Mode */
    public static final String HOSTED_MODE_NAV_ITEM_ID = "hostedMode";
    /** NavItem ID for CSS */
    public static final String CSS_NAV_ITEM_ID = "css";
    /** NavItem ID for Build */
    public static final String SPEED_NAV_ITEM_ID = "speed";
    /** NavItem ID for REST */
    public static final String REST_NAV_ITEM_ID = "rest";
    /** NavItem ID for JavaDoc */
    public static final String JAVADOC_NAV_ITEM_ID = "javaDoc";
    /** NavItem ID for Help */
    public static final String HELP_NAV_ITEM_ID = "help";
    /** NavItem ID for Get Involved */
    public static final String GET_INVOLVED_NAV_ITEM_ID = "getInvolved";
    /** NavItem ID for Authentication */
    public static final String AUTH_NAV_ITEM_ID = "auth";
    /** NavItem ID for Dates */
    public static final String DATES_NAV_ITEM_ID = "l10n";
    /** NavItem ID for Form */
    public static final String FORM_NAV_ITEM_ID = "forms";
    /** NavItem ID for Widgets */
    public static final String WIDGETS_NAV_ITEM_ID = "widgets";
    /** NavItem ID for Auth Test */
    public static final String AUTH_TEST_NAV_ITEM_ID = "authTest";
    /** NavItem ID for License */
    public static final String LICENSE_NAV_ITEM_ID = "license";

    /**
     * The Index page constructor
     */
    public Index()
    {
        g_index = this;
    }
    
    /**
     * Get the message bundle object for this application
     * 
     * @return the message bundle
     */
    public static final SpiffyRsrc getStrings()
    {
        return STRINGS;
    }


    @Override
    public void onModuleLoad()
    {
        DOM.getElementById("main").addClassName("landing");
        
        /*
         We are setting a custom authentication provider.  Custom authentication
         providers can override the UI of the login or provide access to totally
         new authentication mechanisms.  Ours just overrides a string in the
         login panel
         */
        RESTility.setAuthProvider(new SampleAuthUtil());

        m_header = new SPSampleHeader();
        Anchor title = new Anchor(getStrings().mainTitle(), "#");
        m_header.addHeaderTitleWidget(title);
        title.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event)
            {
                event.preventDefault();
                selectItem(LANDING_NAV_ITEM_ID);
            }
        });
        InlineLabel subtitle = new InlineLabel(getStrings().mainSubtitle());
        m_header.addHeaderTitleWidget(subtitle);
        subtitle.getElement().setId("mainsubtitle");
        
        m_footer = new MainFooter();
        loadFooter();

        m_navBar = new MainNavBar();
        
        if (isRunningUnitTests()) {
            loadUnitTests();
            return;
        }
        
        /*
         The landing panel
         */
        NavItem item = new NavItem(LANDING_NAV_ITEM_ID, "",
                                   getStrings().landing_tt());
        m_navBar.add(item);
        m_panels.put(item, new LandingPanel());

        /*
         The overview panel
         */
        item = new NavItem(OVERVIEW_NAV_ITEM_ID, getStrings().overview(),
                                   getStrings().overview_tt());
        m_navBar.add(item);
        m_panels.put(item, new OverviewPanel());
        
        /*
        The Getting started panels
        */
        item = new NavItem(GET_STARTED_NAV_ITEM_ID, getStrings().getStarted(), getStrings().getStarted_tt());
        m_navBar.add(item);
        m_panels.put(item, new GetStartedPanel());

        /*
         * Collapsible Features Nav Section
         */
        NavSection featureSection = new NavSection("featuresNavSection", getStrings().features());
        featureSection.setTitle(getStrings().features_tt());
        m_navBar.add(featureSection);

        /*
         The CSS panel
         */
        item = new NavItem(CSS_NAV_ITEM_ID, getStrings().css(), getStrings().css_tt());
        featureSection.add(item);
        m_panels.put(item, new CSSPanel());

        /*
        The date info panel
        */
        item = new NavItem(DATES_NAV_ITEM_ID, getStrings().l10n(), getStrings().l10n_tt());
        featureSection.add(item);
        m_panels.put(item, new DatePanel());

        /*
         The rest info panel
         */
        item = new NavItem(REST_NAV_ITEM_ID, getStrings().restTitle(), getStrings().restTitle_tt());
        featureSection.add(item);
        m_panels.put(item, new RESTPanel());
        
        /*
        The authentication info panel
        */
        item = new NavItem(AUTH_NAV_ITEM_ID, getStrings().auth(),
                          getStrings().auth_tt());
        featureSection.add(item);
        m_panels.put(item, new AuthPanel());
        
        /*
        The build info panel
        */
        item = new NavItem(SPEED_NAV_ITEM_ID, getStrings().speed(), getStrings().speed_tt());
        featureSection.add(item);
        m_panels.put(item, new BuildPanel());

        addSamplePanels();

        /*
        A separator
        */
        m_navBar.add(new NavSeparator(HTMLPanel.createUniqueId()));

        addDocPanels();
        m_navBar.addListener(this);

        /*
         * If the user has loaded this application in their
         * current session then we want to bring them back to the
         * same page when the application reloads.
         */
        if (Window.Location.getHash() != null &&
            Window.Location.getHash().length() > 0 &&
            m_navBar.getItem(Window.Location.getHash().substring(3)) != null) {
            /*
             * Then there is a hash from the history and that trumps the cookie
             */
            m_navBar.selectItem(m_navBar.getItem(Window.Location.getHash().substring(3)), false, false, false);
            itemSelected(m_navBar.getItem(Window.Location.getHash().substring(3)));
        } else if (Cookies.getCookie(NAV_COOKIE) != null &&
            m_navBar.getItem(Cookies.getCookie(NAV_COOKIE)) != null) {
            m_navBar.selectItem(m_navBar.getItem(Cookies.getCookie(NAV_COOKIE)), false, false, false);
            itemSelected(m_navBar.getItem(Cookies.getCookie(NAV_COOKIE)));
        } else {
            m_navBar.selectItem(m_navBar.getItem(LANDING_NAV_ITEM_ID), false, false, false);
        }

        RESTility.addLoginListener(this);
        m_navBar.setBookmarkable(true);
        
    }
    
    private void loadUnitTests()
    {
        UnitTestPanel.runUnitTests();
    }

    private void addSamplePanels()
    {
        /*
         * Collapsible Features Nav Section
         */
        NavSection demoSection = new NavSection("demoNavSection", getStrings().demos());
        demoSection.setTitle(getStrings().demos_tt());
        m_navBar.add(demoSection);

        /*
        The sample form panel
         */
        NavItem item = new NavItem(FORM_NAV_ITEM_ID, getStrings().form(), getStrings().form_tt());
        demoSection.add(item);
        m_panels.put(item, new FormPanel());

        /*
        The sample widgets panel
         */
        item = new NavItem(WIDGETS_NAV_ITEM_ID, getStrings().widgets(), getStrings().widgets_tt());
        demoSection.add(item);
        m_panels.put(item, new WidgetsPanel());

        /*
        The auth test panel
         */
        item = new NavItem(AUTH_TEST_NAV_ITEM_ID, getStrings().login(), getStrings().login_tt());
        demoSection.add(item);
        m_panels.put(item, new AuthTestPanel());
    }

    private void addDocPanels()
    {
        NavItem item = new NavItem(HOSTED_MODE_NAV_ITEM_ID, getStrings().devMode(),
                           getStrings().devMode_tt());
        m_navBar.add(item);
        m_panels.put(item, new HostedModePanel());
        
        /*
        The get involved panel
        */
        item = new NavItem(GET_INVOLVED_NAV_ITEM_ID, getStrings().getInvolved(), getStrings().getInvolved_tt());
        m_navBar.add(item);
        m_panels.put(item, new GetInvolvedPanel());
        
        /*
        The help panel
        */
        item = new NavItem(HELP_NAV_ITEM_ID, getStrings().help(), getStrings().help_tt());
        m_navBar.add(item);
        m_panels.put(item, new HelpPanel());
        
        /*
        The JavaDoc panel
        */
        item = new NavItem(JAVADOC_NAV_ITEM_ID, getStrings().javadoc(), getStrings().javadoc_tt());
        m_navBar.add(item);
        m_panels.put(item, new JavaDocPanel());
        
        /*
        The license panel
        */
        item = new NavItem(LICENSE_NAV_ITEM_ID, getStrings().lic(), getStrings().lic_tt());
        m_navBar.add(item);
        m_panels.put(item, new LicensePanel());
    }

    @Override
    public boolean preItemSelected(NavItem item)
    {
        return true;
    }

    @Override
    public void itemSelected(NavItem item)
    {
        if (item.getId().equals(LANDING_NAV_ITEM_ID)) {
            DOM.getElementById("main").addClassName("landing");
        } else {
            DOM.getElementById("main").removeClassName("landing");
        }
        
        Cookies.setCookie(NAV_COOKIE, item.getElement().getId());
        
        if (!m_isSausage) {
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
            
            Window.scrollTo(0, 0);
        } else {
            for (NavItem key : m_panels.keySet()) {
                /*
                 In the sausage mode we just scroll things to be visible
                 */
                if (key.equals(item)) {
                    Window.scrollTo(0, getTop(m_panels.get(key).getElement().getId()));
                    return;
                }
            }
        }
    }
    
    /**
     * Set the sausage mode.
     * 
     * @param sausage true if we are showing the sausage menu and false otherwise
     */
    public static void setSausageMode(boolean sausage)
    {
        g_index.m_isSausage = sausage;
    }
    
    private native int getTop(String id) /*-{ 
        return $wnd.$('#' + id).position().top;
    }-*/;

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
        try {
            VersionInfo.getVersionInfo(new RESTObjectCallBack<VersionInfo>() {
                public void success(VersionInfo info)
                {
                    if ("-1".equals(info.getRevDate())) {
                        m_footer.setFooterString(getStrings().footer(info.getVersion(), 
                                                                     JSDateUtil.getLongDate(info.getDate()),
                                                                     info.getRevision()));
                    } else {
                        m_footer.setFooterString(getStrings().footer2(info.getVersion(), 
                                                                      JSDateUtil.getLongDate(info.getDate()),
                                                                      info.getRevision(),
                                                                      JSDateUtil.getLongDate(info.getRevDate())));
                    }
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
        } catch (Exception e) {
            /*
             This sometimes happens when you are running Spiffy on a machine that also
             runs Sentinel.
             */
        }
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
        String name = token.substring(0, token.indexOf('-'));
        g_index.m_header.setWelcomeString(getStrings().welcome(name));
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
      JSUtil.setText("authTestBtn", getStrings().secData());
    }

    private void updateLoginWidgetButton()
    {
      JSUtil.setText("doLoginBtn", getStrings().secData());
    }
    
    /**
     * We host the sample of this application on Google App Engine.  It will run almost everything
     * we need, but it can't handle the HTTP connections we need for authentication.  We change 
     * our UI in that case so it doesn't look broken.
     * 
     * @return true if we are running on Google App Engine and false otherwise
     */
    public static boolean isAppEngine()
    {
        return Window.Location.getHref().startsWith("http://spiffyui.appspot.com") ||
            Window.Location.getHref().startsWith("http://spiffyui-staging.appspot.com") ||
            Window.Location.getHref().startsWith("http://spiffyui.org") ||
            Window.Location.getHref().startsWith("http://www.spiffyui.org");
    }
    
    /**
     * This method indicates if the SPSample is running in unit test mode.
     * 
     * @return true if it is running in unit test mode and false otherwise
     */
    public static native boolean isRunningUnitTests() /*-{ 
        if ($wnd.spiffyui.runUnitTests) {
            return true;
        } else {
            return false;
        }
    }-*/;
}
