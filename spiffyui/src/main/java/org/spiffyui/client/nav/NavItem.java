/*******************************************************************************
 * 
 * Copyright 2011 Spiffy UI Team   
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
package org.spiffyui.client.nav;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;


/**
 * A NavItem represents a single item on the navigation menu
 */
public class NavItem extends Widget implements NavWidget
{
    private final Anchor m_anchor;

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
        this(id, displayName, title, "#");
    }

    /**
     * Create a new NavItem
     * 
     * @param id     the id of the navigator item
     * @param displayName
     *               the display name of the navigation item
     * @param title  the title or tooltip of this navigation item
     * @param link   the link location - this affects the location if the user 
     *               right-mouse clicks and opens the link in a new tab
     */
    public NavItem(String id, String displayName, String title, String link)
    {
        setElement(Document.get().createDivElement());
        getElement().setId(id);
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
        m_anchor.setText(displayName);
        m_anchor.setHref(link);

    }

    /**
     * Retrieve the Id for the NavItem.
     * @return The Id value for this instance
     */
    public String getId()
    {
        return getElement().getId();
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
        return getAnchor().getText();
    }

    /**
     * Set the display name for this nav item
     *
     * @param displayName
     *               the new display name
     */
    public void setDisplayName(String displayName)
    {
        getAnchor().setText(displayName);

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

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((m_anchor.getHref() == null) ? 0 : m_anchor.getHref().hashCode());
        result = prime * result + ((getDisplayName() == null) ? 0 : getDisplayName().hashCode());
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        NavItem other = (NavItem) obj;
        if (m_anchor == null) {
            if (other.m_anchor != null) {
                return false;
            }
        } else if (other.m_anchor == null || !m_anchor.getHref().equals(other.m_anchor.getHref())) {
            return false;
        }
        
        return compareNameAndId(other);
    }
    
    private boolean compareNameAndId(NavItem other)
    {
        if (getDisplayName() == null) {
            if (other.getDisplayName() != null) {
                return false;
            }
        } else if (!getDisplayName().equals(other.getDisplayName())) {
            return false;
        }
        
        if (getId() == null) {
            if (other.getId() != null) {
                return false;
            }
        } else if (!getId().equals(other.getId())) {
            return false;
        }
        return true;
    }

}
