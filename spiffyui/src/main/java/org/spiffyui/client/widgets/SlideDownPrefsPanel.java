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
package org.spiffyui.client.widgets;

import java.util.ArrayList;
import java.util.List;

import org.spiffyui.client.JSUtil;
import org.spiffyui.client.i18n.SpiffyUIStrings;
import org.spiffyui.client.widgets.listener.SlideDownPrefsPanelToggleListener;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Panel;

/**
 * The SlideDownPrefsPanel shows a tab that sticks to the bottom of the 
 * header of the page.  It slides down when clicked to reveal a preferences
 * panel for the current page. It will also fire event and notify
 * SlideDownPrefsPanelToggleListeners when the panel is toggled (expanded or collapsed)
 */
public class SlideDownPrefsPanel extends HTMLPanel
{
    private Panel m_panel;
    private Anchor m_anchor;
    
    private boolean m_visible = false;

    private static final SpiffyUIStrings STRINGS = (SpiffyUIStrings) GWT.create(SpiffyUIStrings.class);

    /**
     * List of listeners for the dropsownselection change widget specific event
     */
    private final List<SlideDownPrefsPanelToggleListener> m_listeners = new ArrayList<SlideDownPrefsPanelToggleListener>();

    
    /**
     * Creates a new panel with a randomly generated ID
     * 
     */
    public SlideDownPrefsPanel()
    {
        this(HTMLPanel.createUniqueId(), null);
    }
    
    /**
     * Creates a new panel
     * @param id - String the id of the panel
     */
    public SlideDownPrefsPanel(String id)
    {
        this(id, null);
    }
    
    /**
     * Create a new panel
     * 
     * @param id - String the id of the panel
     * @param headerText - String the title of the panel and the label on the pull down tab, if null will use the default.
     */
    public SlideDownPrefsPanel(String id, String headerText)
    {       
        super("div", "<div style=\"display: none;\" id=\"" + id + "-panel\" class=\"prefspaneltab\">" +
                         "<h3>" + (headerText == null ? STRINGS.displayOptions() : headerText) + "</h3>" + 
                     "</div>" + 
                     "<div id=\"" + id + "-tab\" class=\"prefspulltab\">" + 
                         "<div id=\"" + id + "-link\"></div>" + 
                     "</div>");

        getElement().setId(id);
        getElement().setClassName("pulldownprefspanel");

        m_anchor = new Anchor(headerText == null ? STRINGS.displayOptions() : headerText);
        m_anchor.addClickHandler(new ClickHandler()
            {
                @Override
                public void onClick(ClickEvent event)
                {
                    event.preventDefault();
                    toggle();
                }
            });
        add(m_anchor, id + "-link");
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
    
    private void toggle()
    {
        JSUtil.toggleSlide(SlideDownPrefsPanel.this.getElement().getId() + "-panel", "");
        m_visible = !m_visible;
        
        if (m_visible) {
            getElementById(SlideDownPrefsPanel.this.getElement().getId() + "-link").addClassName("prefspulltabexpanded");
            fireSlideDownPrefsPanelToggleEvent(true);
        } else {
            getElementById(SlideDownPrefsPanel.this.getElement().getId() + "-link").removeClassName("prefspulltabexpanded");
            fireSlideDownPrefsPanelToggleEvent(false);
        }
    }

    /**
     * Set the panel displayed when this panel is expanded
     * 
     * @param panel  the panel to display
     */
    public void setPanel(Panel panel)
    {
        m_panel = panel;
        add(panel, getElement().getId() + "-panel");
    }

    /**
     * get the panel displayed when this panel is expanded
     * 
     * @return the panel
     */

    public Panel getPanel()
    {
        return m_panel;
    }
    
    /**
     * Toggle the panel visible or Invisible
     */
    public void togglePanel()
    {
        toggle();
    }


    /**
     * Adds a SlideDownPrefsPanelToggleListener to the list of listeners
     *
     * @param listener - the listener to add
     */
    public void addListener(SlideDownPrefsPanelToggleListener listener)
    {
        m_listeners.add(listener);
    }

    /**
     * Removes a SlideDownPrefsPanelToggleListener from the list of listeners
     *
     * @param listener the listener to remove
     */
    public void removeListener(SlideDownPrefsPanelToggleListener listener)
    {
        m_listeners.remove(listener);
    }

    /**
     * Fires SlideDownPrefsPanelToggle event, and invokes onSlideDownPrefsPanelToggle on all listeners
     *
     * @param expand - boolean flag indicating whether the panel is being expanded or collapsed
     */
    public void fireSlideDownPrefsPanelToggleEvent(boolean expand)
    {
        for (SlideDownPrefsPanelToggleListener listener : m_listeners) {
            listener.onSlideDownPrefsPanelToggle(expand);
        }
    }

}
