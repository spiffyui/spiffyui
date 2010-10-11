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
package com.novell.spiffyui.client.widgets.dialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.novell.spiffyui.client.SpiffyUIStrings;

/**
 * This is a common ancestor class where we can change
 * the behavior or look of all Dialogs.  It handles the ESC key and the close X icon.
 * Buttons are added to a button bar.
 */
public abstract class Dialog extends DialogBox
{
    private static final SpiffyUIStrings STRINGS = (SpiffyUIStrings) GWT.create(SpiffyUIStrings.class);
    
    private String m_buttonClicked = "";
    private FlowPanel m_buttonBar;
    private HTMLPanel m_dialogBody;
    private String m_id;
    private int m_defaultButton = -1; //default to the last button added

    /**
     * Constructor defaulting to autohide false and modal true.
     * @param id - the id of this element
     * @param title - the title of the dialog.  Send in an empty String if the title needs to be set
     * later.  Use replaceTitle after calling show()
     * @param titleStyle - the style of the title
     */
    public Dialog(String id, String title, String titleStyle)
    {
        //autohide = false, modal = true;
        this(id, title, titleStyle, false, true);
    }
    /**
     * Constructor.
     * @param id - the id of this element
     * @param title - the title of the dialog.  Send in an empty String if the title needs to be set
     * later.  Use replaceTitle after calling show()
     * @param titleStyle - the style of the title
     * @param autohide - boolean true for if the dialog should be automatically hidden when the user clicks outside of it
     * @param modal - boolean true for modal
     */
    public Dialog(String id, String title, String titleStyle, boolean autohide, boolean modal)
    {
        super(autohide, modal);
        m_id = id;
        getElement().setId(m_id);             

        final String captionDiv = 
            "<div class=\"spiffy-dialog-caption-close\"><a href=\"#\" id=\"" + m_id + "_close\" title=\"" + getCloseText() + "\">" +
                "<div class=\"spiffy-dialog-caption-close-icon\"></div></a></div>" +
            "<div id=\"" + m_id + "_title\" class=\"spiffy-dialog-caption " + titleStyle + "\">" + title + "</div>"; 
        setHTML(captionDiv);
        
        FlowPanel main = new FlowPanel();        
        m_dialogBody = new HTMLPanel("<div><div id=\"" + m_id + "_mainDlgBody\"></div><div>");
        main.add(m_dialogBody);
        add(main);
        
        m_buttonBar = new FlowPanel();
        m_buttonBar.setStyleName("spiffy-dialog-button-bar");        
        main.add(m_buttonBar);
    }
    
    /**
     * Gets the close icon's tooltip text.  Override this method for custom localization
     * @return the close text
     */
    public String getCloseText()
    {
        return STRINGS.close();
    }
        
    /**
     * Replaces the inner text of the title.  Note: This can only be
     * called if the dialog is showing because otherwise it will be null.
     * @param title - the new title
     */
    public void replaceTitle(String title)
    {
        if (isVisible()) {
            Element e = DOM.getElementById(m_id + "_title");
            e.setInnerText(title);
        }
    }
    
    /**
     * A convenient way to add a button to the button bar.  Every button will hide
     * and the CloseHandler can get the value of the button and do the processing.
     * If you want your own handling done, you can
     * optionally pass in a ClickHandler, or if null, then does the default handling.
     * @param id - id of the element
     * @param text - the text shown on the button
     * @param buttonClickedValue - the value of the button returned
     * @param handler - ClickHandler, if null, just hides dialog and sets the button clicked value
     * @return Button - the Button just added
     */
    public Button addButton(final String id, final String text, final String buttonClickedValue, ClickHandler handler)
    {
        Button btn = new Button(text);
        btn.getElement().setId(id);
        btn.setStyleName("rpt-button");
        btn.addStyleName("rpt-button-margin-left");
        if (null == handler) {
            btn.addClickHandler(new ClickHandler() {
    
                @Override
                public void onClick(ClickEvent arg0)
                {
                    setButtonClicked(buttonClickedValue);
                    hide();
                }
                
            });
        } else {
            btn.addClickHandler(handler);
        }
        m_buttonBar.add(btn);
        return btn;
    }
    
    /**
     * A convenient way to add a button to the button bar.  Every button will hide
     * and the CloseHandler can get the value of the button and do the processing.
     * @param id - id of the element
     * @param text - the text shown on the button
     * @param buttonClickedValue - the value of the button returned
     */
    public void addButton(final String id, final String text, final String buttonClickedValue)
    {
        addButton(id, text, buttonClickedValue, null);
    }
    
    /**
     * @param buttonClicked The buttonClicked to set.
     */
    public void setButtonClicked(String buttonClicked)
    {
        m_buttonClicked = buttonClicked;
    }

    /**
     * @return Returns the buttonClicked.
     */
    public String getButtonClicked()
    {
        return m_buttonClicked;
    }
    /**
     * Set the focus to the button added to the button bar at the index specified.  0 is the first button.
     * If not set, then defaults to the first button.
     * @param index - the button position in the bar, 0 is first.
     */
    public void setDefaultButton(int index)
    {
        m_defaultButton = index;
    }
    
    /**
     * Overriding show so that the default button can have focus
     * and thereby be keyboard accessible.  If the dialog has an input that should have focus instead,
     * feel free to override.
     */
    public void show()
    {
        super.show();
        Button defaultBtn;
        if (m_defaultButton != -1) {
            defaultBtn = (Button) m_buttonBar.getWidget(m_defaultButton);
        } else {
            //default to the last button added to the button bar
            defaultBtn = (Button) m_buttonBar.getWidget(m_buttonBar.getWidgetCount() - 1);
        }
        defaultBtn.setFocus(true);
    }

    /**
     * @return Returns the buttonBar.
     */
    public FlowPanel getButtonBar()
    {
        return m_buttonBar;
    }

    /**
     * @param buttonBar The buttonBar to set.
     */
    public void setButtonBar(FlowPanel buttonBar)
    {
        m_buttonBar = buttonBar;
    }

    /**
     * @return Returns the dialogBody.
     */
    public HTMLPanel getDialogBody()
    {
        return m_dialogBody;
    }

    /**
     * @param dialogBody The dialogBody to set.
     */
    public void setDialogBody(HTMLPanel dialogBody)
    {
        m_dialogBody = dialogBody;
    }

    /**
     * @return Returns the id.
     */
    public String getId()
    {
        return m_id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(String id)
    {
        m_id = id;
    }

    /**
     * @return Returns the defaultButton.
     */
    public int getDefaultButton()
    {
        return m_defaultButton;
    }
    
    /**
     * Replace the entire contents of the dialog body with the Widget
     * @param w - Widget to use
     */
    public void replaceDialogBodyContents(Widget w)
    {
        getDialogBody().addAndReplaceElement(w, getId() + "_mainDlgBody");
        w.getElement().setId(getId() + "_mainDlgBody");                
    }

    /**
     * Overriding to close on pressing Esc key or clicking the close anchor.
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
            //clear button (Bug 585536)
            setButtonClicked("");
            hide();        
        }
    }
}
