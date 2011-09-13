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
package org.spiffyui.client.widgets;

import org.spiffyui.client.i18n.SpiffyUIStrings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Tooltip is a PopupPanel that auto-hides when you click outside of it, click the X,
 * or when you let the auto-close time elapse (default is 1.5 seconds).
 */
public class Tooltip extends PopupPanel implements MouseOutHandler, MouseOverHandler
{
    private HTMLPanel m_body;
    private String m_id;
    private FocusPanel m_focusPanel;
    
    private Timer m_autoCloseTimer;
    private int m_autoCloseTime = 1500;
    private static final SpiffyUIStrings STRINGS = (SpiffyUIStrings) GWT.create(SpiffyUIStrings.class);
    
    /**
     * Create a tooltip
     */
    public Tooltip()
    {
        super(true);
        setAnimationEnabled(true);
        
        m_id = HTMLPanel.createUniqueId();
        getElement().setId(m_id);
        m_body = new HTMLPanel("<div class=\"tooltipClose\"><a href=\"#\" id=\"" + m_id + "_close\" title=\"" + STRINGS.close() + "\">" +
                "<div class=\"tooltipCloseIcon\"></div></a></div>" +
            "<div class=\"tooltipBody\" id=\"" + m_id + "_body\"></div>");

        m_body.setStyleName("tooltipContent");
        setWidget(m_body);
        
        m_autoCloseTimer = new Timer() {
            
            @Override
            public void run()
            {
                hide();
            }
        };
        
        m_focusPanel = new FocusPanel();
        m_body.add(m_focusPanel, m_id + "_body");
        
        m_focusPanel.addMouseOutHandler(this);
        m_focusPanel.addMouseOverHandler(this);
    }

    /**
     * Get the FocusPanel that holds the body contents
     * @return the panel containing the tooltip contents
     */
    public FocusPanel getBodyPanel()
    {
        return m_focusPanel;
    }
    /**
     * The time in milliseconds that the tooltip
     * will automatically close if the mouse moves out of it.
     * @return Returns the autoCloseTime.
     */
    public int getAutoCloseTime()
    {
        return m_autoCloseTime;
    }

    /**
     * The time in milliseconds that the tooltip
     * will automatically close if the mouse moves out of it.
     * @param autoCloseTime The autoCloseTime to set.
     */
    public void setAutoCloseTime(int autoCloseTime)
    {
        m_autoCloseTime = autoCloseTime;
    }

    /**
     * Sets the body of the tooltip with any Widget.
     * This will replace any widget previously set at the body.
     * @param w any Widget
     */
    public void setBody(Widget w)
    {
        m_focusPanel.setWidget(w);
    }
    /**
     * Overriding to close on pressing Esc key or clicking the close anchor.
     * Also to auto-close if the mouse is not over the showing popup for designated milliseconds
     * @param event - the Event.NativePreviewEvent
     */
    protected void onPreviewNativeEvent(Event.NativePreviewEvent event)
    {        
        int type = event.getTypeInt();
        if (Event.ONKEYPRESS != type && Event.ONCLICK != type) {
            return;
        }
        if (Event.ONKEYPRESS == type && KeyCodes.KEY_ESCAPE == event.getNativeEvent().getKeyCode()) {
            hide();
        } else if (Event.ONCLICK == type && 
            DOM.isOrHasChild(DOM.getElementById(m_id + "_close"), 
                (Element) com.google.gwt.dom.client.Element.as(event.getNativeEvent().getEventTarget()))) {
            event.getNativeEvent().preventDefault();
            hide();        
        }
    }
    
    /**
     * We are overriding the show method
     * so that we can cancel the auto close timer.
     * The auto close timer should be started
     * externally (like moving away from the anchor
     * that originally shows the tooltip) or
     * when the mouse moves out of the body
     * of the focus panel.
     */
    public void show()
    {
        super.show();
        m_autoCloseTimer.cancel();
    }
    
    /**
     * Cancels the auto close timer
     */
    public void cancelAutoCloseTimer()
    {
        m_autoCloseTimer.cancel();
    }
    
    /**
     * Call this if you want to start the auto close timer based on some other event.
     */
    public void startAutoCloseTimer()
    {
        m_autoCloseTimer.schedule(m_autoCloseTime);
    }

    /**
     * When the mouse is moved out of the
     * focus panel, start the auto close timer.
     * @param event - the MouseOutEvent
     */
    public void onMouseOut(MouseOutEvent event)
    {
        m_autoCloseTimer.schedule(m_autoCloseTime);
    }

    /**
     * If the mouse is over the focus panel,
     * cancel the auto close timer.
     * @param event - the MouseOverEvent
     */
    public void onMouseOver(MouseOverEvent event)
    {
        m_autoCloseTimer.cancel();
    }
}
