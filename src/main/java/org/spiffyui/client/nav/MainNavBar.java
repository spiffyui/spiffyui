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
package org.spiffyui.client.nav;

import java.util.ArrayList;
import java.util.List;

import org.spiffyui.client.HistoryCallback;
import org.spiffyui.client.JSUtil;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This is the navigation bar for the main page.
 *
 */
public class MainNavBar extends HasNavBarListenersPanel implements ClickHandler, HistoryCallback
{
    private final List<NavItem> m_items = new ArrayList<NavItem>();
    private NavItem m_selectedItem = null;

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

        if (RootPanel.get("mainNavigation") != null) {
            RootPanel.get("mainNavigation").add(this);
        } else {
            throw new IllegalStateException("Unable to locate the mainNavigation element.  You must import spiffyui.min.js before using this widget.");
        }
    }

    @Override
    public void add(Widget w)
    {
        if ((w instanceof NavItem) ||
            (w instanceof NavSection) ||
            (w instanceof NavHeader) ||
            (w instanceof NavSeparator) ||
            (w instanceof NavPanel)) {
            super.add(w);

            if (w instanceof NavItem) {
                m_items.add((NavItem) w);
                ((NavItem) w).getAnchor().addClickHandler(this);
            } else if (w instanceof NavSection) {
                ((NavSection) w).setNavBar(this);
            }
        } else {
            throw new IllegalArgumentException("You can only add NavItem, NavHeader, NavSection, or NavSeparator to this class");
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

        doFireEvent(event, navItem);

    }

    /**
     * Call-back when the history item is retrieved after a browser back or forward
     * button is pressed.  The default implementation will be to treat the supplied id
     * as a {@link NavItem} reference and select the one with the same ID.
     *
     * If you subclass and overload the {@link #addHistoryItem(String)} routine, you
     * will probably need to overload this method as well, to ensure that the supplied id
     * (or history token) is translated correctly for your subclass.
     *
     * @param id The history token previously stored on the history stack.
     */
    @Override
    public void historyChanged(String id)
    {
        NavItem item = getItem(id);
        if (item != null) {
            selectItem(item, false, true);
        }
    }

    private void doFireEvent(ClickEvent event, NavItem navItem)
    {
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
                    m_selectedItem = item;
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
        return selectItem(item, addToHistory, doFirePreEvent, true);
    }

    /**
     * Selects the specified navigation item and can fire the navigation event to
     * let all listeners know it was selected, if the doFireSelectedEvent parameter is set accordingly.
     * Returns true if NavItem found, false otherwise
     *
     * @param item   the item to select
     * @param addToHistory
     *               true if this item should be added to the browser's history and false otherwise
     * @param doFirePreEvent
     *               true to allow interception and cancelling of the event, false to not fire the pre-event
     * @param doFireSelectEvent
     *               true to fire the selection event to navbar listeners, false to not fire the event
     *
     * @return true if the item is one of the NavItems of the NavBar, false if not a member such as logout
     */
    public boolean selectItem(NavItem item, boolean addToHistory, boolean doFirePreEvent, boolean doFireSelectEvent)
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
                m_selectedItem = ni;
                if (doFireSelectEvent) {
                    fireEvent(item, addToHistory);
                }
                found = true;
            } else {
                ni.removeStyleName(SELECTED_CLASS);
            }
        }
        return found;
    }

    /**
     * Get the currently selected navigation item in this navigation bar.
     *
     * @return the currently selected navigation item or null if no items are selected
     */
    public NavItem getSelectedItem()
    {
        return m_selectedItem;
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
            addHistoryItem(item.getElement().getId());
        }
    }

    /**
     * A routine to store a history token that this Navbar can use when called
     * in the future when the forward or back button's are called.
     *
     * This is available for subclasses to overload if they wish to store a token
     * that is not based on the NavItem's ID.  For instance the situation where
     * there are multiple types of identifiers being used to reconstitute a past application
     * state.
     *
     * Please note that if you overload this method you will need to provide an alternative implementation
     * of the history callback routine {@link #historyChanged(String)}. That will need to translate the
     * stored token that your overloaded addHistoryItem routine stored.
     *
     * @param historyToken A string value that will server as a token about the applications state
     */
    protected void addHistoryItem(String historyToken)
    {
        if (historyToken != null && historyToken.length() > 0) {
            JSUtil.addHistoryItem(this, historyToken);
        }
    }

    /**
     * Remove all the items from this navigation bar.
     *
     */
    @Override
    public void clear()
    {
        super.clear();
        m_items.clear();
    }

    /**
     * Remove the specified NavItem from the navigation bar.
     *
     * @param item   the item to remove
     */
    public void remove(NavItem item)
    {
        super.remove(item);
        m_items.remove(item);
    }

}
