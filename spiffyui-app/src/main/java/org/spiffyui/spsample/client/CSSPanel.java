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

import org.spiffyui.client.JSUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * This is the CSS panel
 *
 */
public class CSSPanel extends HTMLPanel
{
    private static final SPSampleStrings STRINGS = (SPSampleStrings) GWT.create(SPSampleStrings.class);
    
    private Button m_sausageNav;
    
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
        final Button tabNavButton = new Button(Index.getStrings().tabNavigation());
        tabNavButton.addClickHandler(new ClickHandler()
            {
                @Override
                public void onClick(ClickEvent event)
                {
                    if (tabNavButton.getText().equals(Index.getStrings().tabNavigation())) {
                        RootPanel.get("mainWrap").getElement().addClassName("tabnav");
                        RootPanel.get("mainFooter").getElement().addClassName("tabnav");
                        tabNavButton.setText(Index.getStrings().menuNavigation());
                    } else {
                        RootPanel.get("mainWrap").getElement().removeClassName("tabnav");
                        RootPanel.get("mainFooter").getElement().removeClassName("tabnav");
                        tabNavButton.setText(Index.getStrings().tabNavigation());
                    }
                }
            });
        add(tabNavButton, "tabnavbutton");
        
        /*
         This button handles showing and hiding sausage menu
         */
        m_sausageNav = new Button(Index.getStrings().sausageMenuOn());
        m_sausageNav.setEnabled(!isIE());
        m_sausageNav.addClickHandler(new ClickHandler()
            {
                @Override
                public void onClick(ClickEvent event)
                {
                    doSausage();
                }
            });
        add(m_sausageNav, "sausagenavbutton");
        
        /*
         The grid button can show and hide the grid background
         */
        final Button gridButton = new Button(Index.getStrings().gridOn());
        
        /*
         The fixed grid button is a tab that sticks to the left
         side of the window so you can switch off the grid even
         if you switch to another panel
         */
        final FlowPanel fixedPanel = new FlowPanel();
        final Button fixedgridButton = new Button(" ");
        fixedgridButton.setTitle(Index.getStrings().turnOffGrid());
        fixedPanel.getElement().setId("fixedGridTab");
        
        gridButton.addClickHandler(new ClickHandler()
            {
                @Override
                public void onClick(ClickEvent event)
                {
                    if (gridButton.getText().equals(Index.getStrings().gridOn())) {
                        RootPanel.get("main").getElement().addClassName("grid");
                        gridButton.setText(Index.getStrings().gridOff());
                        JSUtil.horizontalToggleSlide("#fixedGridTab");
                    } else {
                        RootPanel.get("main").getElement().removeClassName("grid");
                        gridButton.setText(Index.getStrings().gridOn());
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
                    gridButton.setText(Index.getStrings().gridOn());
                    JSUtil.horizontalToggleSlide("#fixedGridTab");
                }
            });
        
        fixedPanel.setVisible(false);
        RootPanel.get("mainWrap").add(fixedPanel);
        
    }
    
    private void doSausage()
    {
        if (m_sausageNav.getText().equals(Index.getStrings().sausageMenuOn())) {
            RootPanel.get("mainWrap").getElement().addClassName("sausagenav");
            JSUtil.show("#mainContent > div");
            Window.scrollTo(0, sausageJS());
            m_sausageNav.setText(Index.getStrings().sausageMenuOff());
            Index.setSausageMode(true);
        } else {
            RootPanel.get("mainWrap").getElement().removeClassName("sausagenav");
            m_sausageNav.setText(Index.getStrings().sausageMenuOn());
            Index.setSausageMode(false);
            destroySausageJS();
            Index.selectItem(Index.CSS_NAV_ITEM_ID);
            Window.scrollTo(0, 0);
        }
        
    }
    
    private static native boolean isIE() /*-{
        var ua = navigator.userAgent.toLowerCase();
    
        if (ua.indexOf("msie ") != -1) {
                return true;
        } else {
            return false;
        }
    }-*/;
    
    
    private native void destroySausageJS() /*-{ 
        $wnd.$($wnd.window).sausage("destroy");
    }-*/;
    
    private native int sausageJS() /*-{ 
        $wnd.$($wnd.window).sausage( {
            page: '#mainContent > div',
            heightSelector: '#mainContent > div',
            content: function (i, $page) {
                return '<span class="sausage-span">' + $page.find('h1').first().text() + '</span>';
            }
        });
        
        return $wnd.$('#cssPanel').position().top;
    }-*/;
}
