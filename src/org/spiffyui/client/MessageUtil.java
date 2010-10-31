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
package com.novell.spiffyui.client;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * This class is a set of static utility methods for showing messages and errors
 * to the end user.
 */
public final class MessageUtil
{
    static {
        createJSFunctions();
    }

    /**
     * This is the singleton instance of the error panel.
     */
    public static final ErrorPanel ERROR_PANEL = new ErrorPanel();

    static {
        setLogTitleJS("Error Log");
    }

    /**
     * Show a fatal error message
     * 
     * @param msg
     *        the message to show
     */
    public static void showFatalError(String msg)
    {
        ERROR_PANEL.setErrorMessage(msg);
        logError(msg);
    }

    /**
     * Show a temporary warning message
     * 
     * @param msg
     *        the message to show
     */
    public static void showWarning(String msg)
    {
        showWarning(msg, true);
    }

    private static void showWarning(String msg, Boolean shouldLog)
    {
        showWarning(msg, shouldLog.booleanValue());
    }
    
    /**
     * Show a temporary warning message
     * 
     * @param msg
     *                  the message to show
     * @param shouldLog indicates if this warning should be sent to the in-browser error log
     */
    public static void showWarning(String msg, boolean shouldLog)
    {
        showWarningJS(msg);
        if (shouldLog) {
            logWarning(msg);
        }
    }
    
    private static native void showWarningJS(String msg) /*-{
        $wnd.$("#humanMsg").css({
                    "background-color": "#F7F96A",
                    "color": "black",
                    "opacity": "0.9"
                    });
        $wnd.humanMsg.displayMsg(msg, false);
    }-*/;

    /**
     * Show a temporary message
     * 
     * @param msg
     *        the message to show
     */
    public static native void showMessage(String msg) /*-{
        $wnd.$("#humanMsg").css({
                    "background-color": "black",
                    "color": "white",
                    "opacity": "1"
                    });
        $wnd.humanMsg.displayMsg(msg, false);
    }-*/;

    /**
     * Show a temporary error message and logs it to the client-side error log
     * 
     * @param msg
     *        the message to show
     */
    public static void showError(String msg)
    {
        showErrorJS(msg);
        logError(msg);
    }

    /**
     * Shows an error alert message and logs a logging message
     * 
     * @param errorMsg the error message for the alert
     * @param logMsg   the message for the error log
     */
    public static void showError(String errorMsg, String logMsg)
    {
        showErrorJS(errorMsg);
        logError(logMsg);
    }

    private static native void showErrorJS(String msg) /*-{
        $wnd.$("#humanMsg").css({
                    "background-color": "#E00000",
                    "color": "white",
                    "opacity": "0"
                    });
        $wnd.humanMsg.displayMsg(msg, false);
    }-*/;

    /**
     * Write a message to the error log
     * 
     * @param msg
     *        the message to show
     */
    public static void logError(String msg)
    {
        logErrorJS(escapeString(msg));
    }
    
    /**
     * Write a message to the error log
     * 
     * @param msg
     *        the message to show
     */
    public static void logWarning(String msg)
    {
        logErrorJS(escapeString(msg));
    }
    
    private static String escapeString(String s) 
    {
        if (s == null) {
            return "";
        }
        s = s.replaceAll("\\<", "&lt;");
        s = s.replaceAll("\\>", "&gt;");
        
        return "<span class=\"weak\">" + JSDateUtil.getShortTime(new Date()) + "</span> " + s;
    }

    private static native void logErrorJS(String msg) /*-{
        $wnd.humanMsg.log(msg);
    }-*/;

    /**
     * Set the title of the error log.  It may not contain HTML
     * 
     * @param title  the error log title
     */
    public static native void setLogTitleJS(String title) /*-{
        $wnd.humanMsg.setLogTitle(title);
    }-*/;
    
    private static native void createJSFunctions() /*-{
        $wnd.spiffyui.showFatalError = function(msg) {
            @com.novell.spiffyui.client.MessageUtil::showFatalError(Ljava/lang/String;)(msg);
        }
        
        $wnd.spiffyui.showError = function(msg) {
            @com.novell.spiffyui.client.MessageUtil::showError(Ljava/lang/String;)(msg);
        }
        
        $wnd.spiffyui.showWarning = function(msg, shouldLog) {
            if (shouldLog === null) {
                shouldLog = true;
            }
            @com.novell.spiffyui.client.MessageUtil::showWarning(Ljava/lang/String;Ljava/lang/Boolean;)(msg,shouldLog);
        }
        
        $wnd.spiffyui.showMessage = function(msg) {
            @com.novell.spiffyui.client.MessageUtil::showMessage(Ljava/lang/String;)(msg);
        }
    }-*/;
}

/**
 * The error panel show fatal errors in the UI
 */
class ErrorPanel extends Composite implements Event.NativePreviewHandler
{

    private Label m_label;
    private FlowPanel m_panel;

    public ErrorPanel()
    {
        m_panel = new FlowPanel();
        m_panel.getElement().setId("errorpanel");

        m_label = new Label("", true);
        m_panel.add(m_label);

        Anchor clear = new Anchor("X");
        clear.getElement().setId("errorpanel_hide");
        clear.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                event.preventDefault();
                JSUtil.slideUp("#" + m_panel.getElement().getId(), "fast");
            }
        });
        m_panel.add(clear);

        RootPanel placeHolder = RootPanel.get("mainContent");
        placeHolder.add(m_panel);
        m_panel.setVisible(false);

        //Any click anywhere will close
        Event.addNativePreviewHandler(this);
    }

    /**
     * Sets a message in the error panel and makes it visible
     * 
     * @param message
     *        the message
     */
    public void setErrorMessage(String message)
    {
        makeVisible();
        m_label.setText(message);
    }

    /**
     * Appends a message to the error panel and makes it visible
     * 
     * @param message
     *        the message
     */
    public void appendErrorMessage(String message)
    {
        makeVisible();
        m_label.setText(m_label.getText() + message);
    }

    /**
     * Clears the error panel. This method will make the panel invisible.
     */
    public void clear()
    {
        m_label.setText("");
        m_panel.setVisible(false);
    }

    private void makeVisible()
    {
        if (!m_panel.isVisible()) {
            JSUtil.slideDown("#" + m_panel.getElement().getId(), "normal");
        }
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
        if (DOM.isOrHasChild(DOM.getElementById(m_panel.getElement().getId()), target)) {
            JSUtil.slideUp("#" + m_panel.getElement().getId(), "fast");
        }
    }
}
