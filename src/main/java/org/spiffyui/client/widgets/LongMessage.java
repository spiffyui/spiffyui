/*
 * ========================================================================
 *
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
package org.spiffyui.client.widgets;

import java.util.Collections;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;

import org.spiffyui.client.JSUtil;
import org.spiffyui.client.SpiffyUIStrings;

/**
 * A class for showing messages that are wordy and take more than 
 * a few seconds to read. 
 */
public class LongMessage extends FlowPanel implements Event.NativePreviewHandler
{
    private static final SpiffyUIStrings STRINGS = (SpiffyUIStrings) GWT.create(SpiffyUIStrings.class);

    private HTML m_html;
    private Anchor m_close;
    private boolean m_madeVisible = false; //This is to toggle makeVisible effect.  isVisible on element might have it wrong depending on the spped of the effect
    
    /**
     * Create a new LongMessage Panel with a randomly generated ID
     */
    public LongMessage()
    {
        this(HTMLPanel.createUniqueId());
    }
    
    /**
     * Create a new LongMessage panel with the specified ID 
     *  
     * @param id - the unique ID of this element
     */
    public LongMessage(String id)
    {
        getElement().setId(id);
        setStyleName("spiffy-longmessage-panel");

        m_html = new HTML("", true);
        add(m_html);
        
        //Include a close anchor
        //so that users will know it can close (but really it can be closed from anywhere)
        //and also so that we can set focus to something within the message.
        //The div is so it can float right
        SimplePanel div = new SimplePanel();
        div.addStyleName("spiffy-dialog-caption-close");
        m_close = new Anchor();
        m_close.getElement().setId(id + "longMsg_hide");
        m_close.setHref("#");
        m_close.setTitle(STRINGS.close());
        m_close.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                event.preventDefault();
                makeVisible(false);
            }
        });
        DivElement iconDiv = Document.get().createDivElement();
        iconDiv.setClassName("spiffy-longmessage-panel-hide");
        iconDiv.setId(id + "_longMsg_hide_IconDiv");     
        
        m_close.getElement().appendChild(iconDiv);
        div.add(m_close);
        add(div);

        setVisible(false);
        
        //Any click anywhere will close
        Event.addNativePreviewHandler(this);
    }

    /**
     * Set the title (tooltip) text of the close button on the LongMessage
     * 
     * @param title  the title
     */
    public void setCloseButtonTitle(String title) 
    {
        m_close.setTitle(title);
    }

    /**
     * Sets a message in the panel and makes it visible
     * 
     * @param message
     *        the message String
     */
    public void setText(String message)
    {
        makeVisible(true);
        m_html.setText(message);
    }

    /**
     * Sets a message as HTML in the panel and makes it visible
     * 
     * @param message
     *        the message
     */
    public void setHTML(String message)
    {
        makeVisible(true);
        m_html.setHTML(message);
    }

    /**
     * Get the current HTML conents of this long message.
     * 
     * @return the html contents
     */
    public String getHTML()
    {
        return m_html.getHTML();
    }

    /**
     * Clears the error panel. This method will make the panel invisible.
     */
    public void clear()
    {
        m_html.setText("");
        makeVisible(false);
    }

    private void makeVisible(boolean visible)
    {
        if (visible) {
            if (!m_madeVisible) {
                JSUtil.show(getElement().getId(), "fast");
            }
            //set the focus to m_close so that the browser window scrolls to this panel if necessary
            //and can be readily closed with a quick keyboard or click
            try {
                m_close.setFocus(true);
            } catch (Exception e) {
                /*
                 This call can cause an exception in IE if the control is visible on
                 a panel that is invisible.
                 */
            }
        } else {
            JSUtil.hide(getElement().getId(), "fast");
        }
        m_madeVisible = visible;
    }

    @Override
    public void onPreviewNativeEvent(NativePreviewEvent event)
    {
        if (event.getTypeInt() != Event.ONCLICK) {
            return;
        }
        
        Element target = (Element) com.google.gwt.dom.client.Element.as(event.getNativeEvent().getEventTarget());
        if (null == target) {
            return;
        }
        //any click on this will dismiss the panel
        if (DOM.isOrHasChild(DOM.getElementById(getElement().getId()), target)) {
            makeVisible(false);
        }
    }
    
    /**
     * Join a list of Strings together for display with the default delimiter
     * @param list - the list of Strings
     * @return - a String concatenation of names
     */
    public static String joinNames(final List<String> list)
    {
        return joinNames(list, ", ", true);
    }

    /**
     * Join a list of Strings together for display using a delimiter
     * @param list - the list of Strings
     * @param delim - the delimiter
     * @param sort - whether or not to sort the list
     * @return - a String concatenation of names
     */
    public static String joinNames(final List<String> list, final String delim, final boolean sort)
    {
        //sort this list first
        if (sort) {
            Collections.sort(list);
        }
        
        String s = "";
        for (String n : list) {
            s += n + delim;
        }
        
        return JSUtil.trimLastDelimiter(s, delim);
    }
}
