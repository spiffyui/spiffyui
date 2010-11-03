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

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;


/**
 * This widget represents a single item on the navigation menu
 */
public class NavItem extends Widget
{
    private String m_id;
    private String m_displayName;
    
    private Anchor m_anchor;

    /**
     * Create a new NavItem
     * 
     * @param id     the id of the navigator item
     * @param displayName
     *               the display name of the navigation item
     */
    public NavItem(String id, String displayName)
    {
        this(id, displayName, null);
    }
    
    /**
     * Create a new NavItem
     * 
     * @param id     the id of the navigator item
     * @param displayName
     *               the display name of the navigation item
     * @param title  the title or tooltip of this navigation item
     */
    public NavItem(String id, String displayName, String title)
    {
        m_id = id;
        m_displayName = displayName;
        
        setElement(Document.get().createDivElement());
        getElement().setId(m_id);
        setStyleName("main-menuItem");

        if (title != null) {
            setTitle(title);
        }
        
        /*
         The Anchor widget doesn't want to wrap an element unless it is a
         direct child of the body tag.  I'm not really sure why since it
         works fine but throws an assertion error in hosted mode.  We can
         add it to the body and then move it after the widget wraps it.
         */
        Element el = Document.get().getBody().appendChild(Document.get().createAnchorElement());
        m_anchor = Anchor.wrap(el);
        getElement().appendChild(el);
        m_anchor.setText(m_displayName);
        m_anchor.setHref("#");
        
    }
    
    /**
     * Gets the anchor for this nav item
     * 
     * @return the anchor
     */
    public Anchor getAnchor()
    {
        return m_anchor;
    }
    
    /**
     * Gets the display name for this nav item
     * 
     * @return the display name
     */
    public String getDisplayName()
    {
        return m_displayName;
    }

    /**
     * Set the access key for this menu item
     * 
     * @param key    the key
     */
    public void setAccessKey(char key)
    {
        if (key != (char) -1) {
            m_anchor.setAccessKey(key);
        }
    }
}
