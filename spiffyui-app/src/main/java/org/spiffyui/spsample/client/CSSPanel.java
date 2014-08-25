/*******************************************************************************
 *
 * Copyright 2011-2014 Spiffy UI Team   
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

import org.spiffyui.client.JSUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
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
                        DOM.getElementById("mainWrap").addClassName("tabnav");
                        DOM.getElementById("mainFooter").addClassName("tabnav");
                        tabNavButton.setText(Index.getStrings().menuNavigation());
                    } else {
                        DOM.getElementById("mainWrap").removeClassName("tabnav");
                        DOM.getElementById("mainFooter").removeClassName("tabnav");
                        tabNavButton.setText(Index.getStrings().tabNavigation());
                    }
                }
            });
        add(tabNavButton, "tabnavbutton");
        
        final FlowPanel fixedScrollingPanel = new FlowPanel();
        final Button fixedScrollingButton = new Button(" ");
        fixedScrollingButton.setTitle(Index.getStrings().turnOffScrolling());
        fixedScrollingPanel.getElement().setId("fixedScrollingTab");
        
        fixedScrollingPanel.add(fixedScrollingButton);
        
        fixedScrollingButton.addClickHandler(new ClickHandler()
            {
                @Override
                public void onClick(ClickEvent event)
                {
                    doSausage();
                }
            });
        fixedScrollingPanel.setVisible(false);
        RootPanel.get("mainWrap").add(fixedScrollingPanel);
        
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
                        DOM.getElementById("main").addClassName("grid");
                        gridButton.setText(Index.getStrings().gridOff());
                        JSUtil.horizontalToggleSlide("#fixedGridTab");
                    } else {
                        DOM.getElementById("main").removeClassName("grid");
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
                    DOM.getElementById("main").removeClassName("grid");
                    gridButton.setText(Index.getStrings().gridOn());
                    JSUtil.horizontalToggleSlide("#fixedGridTab");
                }
            });
        
        fixedPanel.setVisible(false);
        RootPanel.get("mainWrap").add(fixedPanel);
        
        Index.addToc(this);
        
    }
    
    private void doSausage()
    {
        if (m_sausageNav.getText().equals(Index.getStrings().sausageMenuOn())) {
            DOM.getElementById("mainWrap").addClassName("sausagenav");
            JSUtil.show("#mainContent > div");
            Window.scrollTo(0, sausageJS());
            m_sausageNav.setText(Index.getStrings().sausageMenuOff());
            Index.setSausageMode(true);
            JSUtil.hide("backToTop");
            JSUtil.horizontalToggleSlide("#fixedScrollingTab");
        } else {
            DOM.getElementById("mainWrap").removeClassName("sausagenav");
            m_sausageNav.setText(Index.getStrings().sausageMenuOn());
            Index.setSausageMode(false);
            destroySausageJS();
            Index.selectItem(Index.CSS_NAV_ITEM_ID);
            Window.scrollTo(0, 0);
            JSUtil.horizontalToggleSlide("#fixedScrollingTab");
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
