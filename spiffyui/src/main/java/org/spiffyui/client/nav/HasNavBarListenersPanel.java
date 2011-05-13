/*
 * ========================================================================
 *
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
package org.spiffyui.client.nav;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * This is a FlowPanel that calls events by listeners listening for changes in navigation
 * such as MainNavBar or logout on MainHeader.
 */
public class HasNavBarListenersPanel extends FlowPanel
{

    private List<NavBarListener> m_listeners = new ArrayList<NavBarListener>();   
    private boolean m_enabled = true;
    /**
     * @return Returns the enabled.
     */
    public boolean isEnabled()
    {
        return m_enabled;
    }

    /**
     * @param enabled The enabled to set.
     */
    public void setEnabled(boolean enabled)
    {
        m_enabled = enabled;
    }

    /**
     * Notifies listeners of a request to select an
     * item, giving listeners a change to cancel the event
     * 
     * @param item   the nav item that was clicked
     * @return boolean true to continue, false to cancel
     */
    public boolean firePreEvent(NavItem item)
    {
        boolean go = true;
        if (m_enabled) {
            for (NavBarListener listener : m_listeners) {
                if (!listener.preItemSelected(item)) {
                    go = false;
                }
            }
        }
        return go;
    }

    /**
     * Fires the click event on the specified nav item
     * 
     * @param item   the nav item that was clicked
     */
    public void fireEvent(NavItem item)
    {
        fireEvent(item, true);
    }
    
    /**
     * Fires the click event on the specified nav item
     * 
     * @param item   the nav item that was clicked
     * @param addToHistory
     *               true if this item should be added to the browser history and false otherwise
     */
    protected void fireEvent(NavItem item, boolean addToHistory)
    {
        if (m_enabled) {
            for (NavBarListener listener : m_listeners) {
                listener.itemSelected(item);
            }
        }
    }
    
    /**
     * Adds a NavBarListener to this navigation bar
     * 
     * @param listener the listener to add
     */
    public void addListener(NavBarListener listener)
    {
        m_listeners.add(listener); 
    }
    
    /**
     * Removes a NavBarListener from this navigation bar.
     * 
     * @param listener the listener to remove
     */
    public void removeListener(NavBarListener listener)
    {
        m_listeners.remove(listener);
    }
    

}
