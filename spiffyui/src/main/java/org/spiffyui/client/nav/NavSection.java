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
package org.spiffyui.client.nav;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

import org.spiffyui.client.JSUtil;

/**
 * This widget represents a single section that contains multiple items 
 * in the navigation menu. 
 */
public class NavSection extends ComplexPanel implements NavWidget
{
    
    private String m_id;
    private String m_displayName;
    
    private MainNavBar m_navBar;
    private List<Widget> m_children = new ArrayList<Widget>();

    private Anchor m_anchor;

    private boolean m_expanded = true;
    
    /**
     * Creates a new section in the navigation bar
     * 
     * @param id     the id for the section
     * @param displayName
     *               the display name for that section
     */
    public NavSection(String id, String displayName)
    {
        m_id = id;
        m_displayName = displayName;
        
        setElement(Document.get().createDivElement());
        getElement().setId(m_id);
        setStyleName("main-menuSect");
        
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
        m_anchor.getElement().addClassName("main-menuSectItem");
        m_anchor.getElement().addClassName("main-menuSectItemExpanded");
        
        m_anchor.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event)
                {
                    m_expanded = !m_expanded;
                    JSUtil.toggleSlide(m_id + " .main-menuSectItems", "fast");
                    event.preventDefault();

                    if (m_expanded) {
                        m_anchor.getElement().addClassName("main-menuSectItemExpanded");
                    } else {
                        m_anchor.getElement().removeClassName("main-menuSectItemExpanded");
                    }
                }
            });
        
        getElement().appendChild(Document.get().createDivElement()).addClassName("main-menuSectItems");
        
    }

    @Override
    public void setTitle(String title)
    {
        m_anchor.setTitle(title);
    }

    @Override
    public String getTitle()
    {
        return m_anchor.getTitle();
    }
    
    @Override
    /**
     * Adds navigation items and separators to this navigation section
     * 
     * @param w      the widget to add
     */
    public void add(Widget w)
    {
        if (w instanceof NavItem &&
            m_navBar != null) {
            m_navBar.addNavItem((NavItem) w);
        } else if (w instanceof NavSection &&
            m_navBar != null) {
            ((NavSection) w).setNavBar(m_navBar);
        }
        add(w, (com.google.gwt.user.client.Element) getElement().getFirstChildElement().getNextSiblingElement());
        m_children.add(w);
    }
    
    /**
     * Gets the display name of this section
     * 
     * @return the display name
     */
    public String getDisplayName()
    {
        return m_displayName;
    }
    
    /**
     * Set the navigation bar reference so the navigation bar can add listeners to
     * the anchors in this section
     * 
     * @param bar    the navigation bar
     */
    protected void setNavBar(MainNavBar bar)
    {
        m_navBar = bar;
        for (Widget w : m_children) {
            if (w instanceof NavItem) {
                m_navBar.addNavItem((NavItem) w);
            }
        }
    }
    
    /**
     * Updates the selection state to mark the specified anchor selected and the rest
     * unselected
     * 
     * @param a      the anchor to select
     */
    protected void updateSelectedState(Anchor a)
    {
        for (Widget w : m_children) {
            if (w instanceof NavItem) {
                NavItem item = (NavItem) w;
                if (item.getAnchor() == a) {
                    item.addStyleName(MainNavBar.SELECTED_CLASS);
                    m_navBar.fireEvent(item);
                } else {
                    item.removeStyleName(MainNavBar.SELECTED_CLASS);
                }
            } else if (w instanceof NavSection) {
                ((NavSection) w).updateSelectedState(a);
            }
        }
    }
    
    /**
     * Returns the NavItem that owns the specified Anchor
     * @param a - the Anchor of interest
     * @return the NavItem that the Anchor is a part of
     */
    public NavItem getNavItem(Anchor a)
    {
        NavItem navItem = null;
        for (Widget w : m_children) {
            if (w instanceof NavItem) {
                NavItem item = (NavItem) w; 
                if (item.getAnchor() == a) {
                    navItem = item;
                    break;
                } 
            } else if (w instanceof NavSection) {
                navItem = ((NavSection) w).getNavItem(a);
            }
        }
        return navItem;
    }
}
