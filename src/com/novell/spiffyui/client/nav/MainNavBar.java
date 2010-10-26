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
package com.novell.spiffyui.client.nav;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.novell.spiffyui.client.JSUtil;

/**
 * This is the navigation bar for the main page.
 *
 */
public class MainNavBar extends HasNavBarListenersPanel implements ClickHandler
{    
    private List<NavItem> m_items = new ArrayList<NavItem>();
    
    /**
     * The class used for selected navigation items
     */
    protected static final String SELECTED_CLASS = "navItemSelected";
     
    
    /**
     * Create a new MainNavBar
     */
    public MainNavBar()
    {
        getElement().setId("mainNavContainer");
        
        RootPanel.get("mainNavigation").add(this);
    }
    
    @Override
    public void add(Widget w)
    {
        if ((w instanceof NavItem) ||
            (w instanceof NavSection) ||
            (w instanceof NavSeparator)) {
            super.add(w);
            
            if (w instanceof NavItem) {
                m_items.add((NavItem) w);
                ((NavItem) w).getAnchor().addClickHandler(this);
            } else if (w instanceof NavSection) {
                ((NavSection) w).setNavBar(this);
            }
        } else {
            throw new IllegalArgumentException("You can only add NavItem, NavSection, or NavSeparator to this class");
        }
    }
    
    /**
     * Addes a nav item to the list of anchors to listen to
     * 
     * @param item   the nav item to add
     */
    protected void addNavItem(NavItem item)
    {
        if (!m_items.contains(item)) {
            item.getAnchor().addClickHandler(this);
            m_items.add(item);
        }
    }
    
    @Override
    public void onClick(ClickEvent event)
    {
        event.preventDefault();
        if (!isEnabled()) {
            return;
        }
        NavItem navItem = null;
        for (Widget w : getChildren()) {
            if (w instanceof NavItem) {
                NavItem item = (NavItem) w;
                if (item.getAnchor() == event.getSource()) {
                    navItem = item;
                    break;
                }
            } else if (w instanceof NavSection) {
                NavItem item = ((NavSection) w).getNavItem((Anchor) event.getSource());
                if (item != null) {
                    navItem = item;
                    break;
                }
            }
        }
        //if any listener wants to cancel the event
        //then do not continue
        if (!firePreEvent(navItem)) {
            return;
        }
        
        //continue if no listener returned false on the pre-event
        for (Widget w : getChildren()) {
            if (w instanceof NavItem) {
                NavItem item = (NavItem) w;
                if (item.getAnchor() == event.getSource()) {
                    item.addStyleName(SELECTED_CLASS);
                    fireEvent(item);
                } else {
                    item.removeStyleName(SELECTED_CLASS);
                }
            } else if (w instanceof NavSection) {
                ((NavSection) w).updateSelectedState((Anchor) event.getSource());
            }
        }
    }
    
    /**
     * Selects the specified navigation item and fires the navigation event to
     * let all listeners know it was selected.  This intentionally does not
     * fire a pre-event so that it cannot be intercepted. Returns true if NavItem found, false otherwise
     * 
     * @param item   the item to select
     * @return true if the item is one of the NavItems of the NavBar, false if not a member such as logout
     */
    public boolean selectItem(NavItem item)
    {
        return selectItem(item, true, false);
    }
    
    /**
     * Selects the specified navigation item and fires the navigation event to
     * let all listeners know it was selected.  Returns true if NavItem found, false otherwise
     * 
     * @param item   the item to select
     * @param addToHistory
     *               true if this item should be added to the browser's history and false otherwise
     * @param doFirePreEvent
     *               true to allow interception and cancelling of the event, false to not fire the pre-event
     * 
     * @return true if the item is one of the NavItems of the NavBar, false if not a member such as logout
     */
    public boolean selectItem(NavItem item, boolean addToHistory, boolean doFirePreEvent)
    {
        //if any listener wants to cancel the event
        //then do not continue
        if (doFirePreEvent && !firePreEvent(item)) {
            return false;
        }
        
        boolean found = false;
        for (NavItem ni : m_items) {
            if (ni == item) {
                ni.addStyleName(SELECTED_CLASS);
                fireEvent(item, addToHistory);
                found = true;
            } else {
                ni.removeStyleName(SELECTED_CLASS);
            }
        }
        return found;
    }
    
    /**
     * Gets a navigation item from the navigation menu.
     * 
     * @param id     The id of the item to get
     * 
     * @return the item with that id or null if it doesn't exist
     */
    public NavItem getItem(String id)
    {
        for (NavItem item : m_items) {
            if (item.getElement().getId().equals(id)) {
                return item;
            }
        }
        
        return null;
    }   
    
    /**
     * Set the navigation bar to be enabled or disabled.  A disabled navigation bar
     * is gray and never fires selection events.
     * 
     * @param enabled true for enabled and false for disabled
     */
    @Override
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        
        if (enabled) {
            removeStyleName("disabled");
        } else {
            /*
             TODO - We need to style the nav bar to it's clear to the user that it
             is disabled.
             */
            addStyleName("disabled");
        }
    }
    
    @Override
    public void fireEvent(NavItem item, boolean addToHistory)
    {
        super.fireEvent(item, addToHistory);
        
        if (isEnabled() && addToHistory) {
            JSUtil.addHistoryItem(this, item.getElement().getId());
        }
    }

}
