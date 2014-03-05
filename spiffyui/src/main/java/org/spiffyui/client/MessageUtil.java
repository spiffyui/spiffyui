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
package org.spiffyui.client;

import java.util.Date;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * <p>
 * This class is a set of static utility methods for showing messages and errors
 * in the browser and handle them all in a consistent way.
 * </p>
 * 
 * <p>
 * Applications have a large variety of messages they show to users.  They can vary 
 * from "That name is already taken" to "Your database server crashed."  These 
 * messages have a very different level of severity.  To support that the Spiffy
 * UI framework supports four levels of user messages.
 * </p>
 * 
 * <p>
 * <b>Informational messages</b> tell the user something they need to know but don't
 * need to respond to.  "Your changes are saved" is a common example of this type
 * of message.
 * </p>
 * 
 * <p>
 * <b>Warning messages</b> tell the user something they need to respond to like 
 * "That username is already taken."  
 * </p>
 * 
 * <p>
 * <b>Error messages</b> tell the user something went wrong.  For example, 
 * "Unable to contact your email server."
 * </p>
 * 
 * <p>
 * <b>Fatal error messages</b> tell the user something went so wrong the system
 * probably can't continue.  "Unable to contact your server" is a typical fatal
 * error message.
 * </p>
 * 
 * <p>
 * The first three types of messages are transient.  The UI follows the Humanized
 * Messages idiom layed out by <a href="http://en.wikipedia.org/wiki/Jef_Raskin">
 * Jef Raskin</a>.  They show up on top of the content and fade in a small amount 
 * of time.  Wiggling your mouse on them will make them fade faster.  
 * </p>
 * 
 * <p>
 * Error messages and some warnings will also show up in the error log at the bottom
 * of the page.  This makes it easy to find them again if the user needs to.
 * </p>
 * 
 * <p>
 * Fatal errors are different since they normally prevent the program from continuing.
 * These errors show up on top of the screen and do not move until the user clicks on
 * them.  
 * </p>
 * 
 * <p>
 * Each of these messages can be seen on the Widgets page of the Spiffy UI Application.
 * </p>
 * 
 * <p>
 * Many of the methods use the Humanized Message Panels and require the Spiffy UI
 * JavaScript libraries.  This class also depends on the Spiffy UI HTML structure.
 * </p>
 */
public final class MessageUtil
{
    private static boolean g_shouldShow = true;
    
    /**
     * Making sure this class can't be instantiated.
     */
    private MessageUtil()
    {
    }
    
    static {
        createJSFunctions();
    }

    /**
     * This is the singleton instance of the error panel.
     */
    public static final ErrorPanel ERROR_PANEL = new ErrorPanel();

    /**
     * This is the singleton instance of the page warning panel.
     */
    public static final PageWarningPanel PAGE_WARNING_PANEL = new PageWarningPanel();

    static {
        setLogTitleJS("Error Log");
    }

    /**
     * Show a fatal error message in the error panel with the ID errorPanel.
     * 
     * This message has a default style of 
     * 
     * @param msg the message to show in HTML
     */
    public static void showFatalError(String msg)
    {
        if (g_shouldShow) {
            ERROR_PANEL.setErrorMessage(msg);
        }
        logError(msg);
    }

    /**
     * This method shows a page level warning for the page.  These warnings
     * are meant to indicate that the page is fundamentally broken or unavailable.
     * The message shows up large in the middle of the page and hide all of
     * the contents of the page except for the header and footer.
     * 
     * @param msg    the message to show in HTML
     */
    public static void showPageLevelWarning(String msg)
    {
        PAGE_WARNING_PANEL.setErrorMessage(msg);
    }

    /**
     * <p>
     * Show a warning message.
     * </p>
     * 
     * <p> 
     * This message has a default style of white text with a red background.
     * </p>
     * 
     * <h3>CSS Style Ryles</h3>
     * 
     * <ul>
     * <li>.humanMsgErr</li>
     * </ul>
     * 
     * @param msg the message to show in HTML
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
     * <p>
     * Show a warning message.
     * </p>
     * 
     * <p> 
     * This message has a default style of black text with a yellow background.
     * </p>
     * 
     * <h3>CSS Style Ryles</h3>
     * 
     * <ul>
     * <li>.humanMsgWarn</li>
     * </ul>
     * 
     * @param msg the message to show in HTML
     * @param shouldLog indicates if this warning should be sent to the in-browser error log
     */
    public static void showWarning(String msg, boolean shouldLog)
    {
        showWarningJS(msg);
        if (shouldLog) {
            logWarning(msg);
        }
    }
    
    /**
     * <p>
     * Show a warning message.
     * </p>
     * 
     * <p> 
     * This message has a default style of black text with a yellow background.
     * </p>
     * 
     * <h3>CSS Style Ryles</h3>
     * 
     * <ul>
     * <li>.humanMsgWarn</li>
     * </ul>
     * 
     * @param msg the message to show in HTML
     * @param shouldLog indicates if this warning should be sent to the in-browser error log
     * @param isSafe if this field is true then this just calls showMessage, otherwise it
     *               runs the specified message through the GWT SaveHtmlUtils
     */
    public static void showWarning(String msg, boolean shouldLog, boolean isSafe)
    {
        if (isSafe) {
            showWarning(msg, shouldLog);
        } else {
            showWarning(SafeHtmlUtils.fromString(msg).asString(), shouldLog);
        }        
    }

    /**
     * Sets if the message helper should show messages at all.
     * 
     * @param shouldShow true if it should and false otherwise
     */
    public static void setShouldShowMessages(boolean shouldShow)
    {
        g_shouldShow = shouldShow;
        setShouldShowMessagesJS(shouldShow);
    }

    /**
     * Gets if the message util is showing messages.
     * 
     * @return true if messages are shown and false otherwise
     */
    public static boolean getShouldShowMessages()
    {
        return g_shouldShow;
    }

    private static native void setShouldShowMessagesJS(boolean shouldShow) /*-{
        humanMsg.shouldShow = shouldShow;
    }-*/;
    
    private static native void showWarningJS(String msg) /*-{
        $wnd.humanMsg.setup();
        $wnd.jQuery("#humanMsg").removeClass('humanMsgErr').removeClass('humanMsgInfo').addClass('humanMsgWarn');
        $wnd.humanMsg.displayMsg(msg, false);
    }-*/;

    /**
     * <p>
     * Show an information message.
     * </p>
     * 
     * <p> 
     * This message has a default style of white text with a black background.
     * </p>
     * 
     * <h3>CSS Style Ryles</h3>
     * 
     * <ul>
     * <li>.humanMsgInfo</li>
     * </ul>
     * 
     * @param msg the message to show in HTML
     */
    public static native void showMessage(String msg) /*-{
        $wnd.humanMsg.setup();
        $wnd.jQuery("#humanMsg").removeClass('humanMsgErr').removeClass('humanMsgWarn').addClass('humanMsgInfo');
        $wnd.humanMsg.displayMsg(msg, false);
    }-*/;
    
    /**
     * <p>
     * Show an information message.
     * </p>
     * 
     * <p> 
     * This message has a default style of white text with a black background.
     * </p>
     * 
     * <h3>CSS Style Ryles</h3>
     * 
     * <ul>
     * <li>.humanMsgInfo</li>
     * </ul>
     * 
     * @param msg the message to show in HTML
     * @param isSafe if this field is true then this just calls showMessage, otherwise it
     *               runs the specified message through the GWT SaveHtmlUtils
     */
    public static void showMessage(String msg, boolean isSafe) 
    {
        if (isSafe) {
            showMessage(msg);
        } else {
            showMessage(SafeHtmlUtils.fromString(msg).asString());
        }
    }

    /**
     * <p>
     * Show an warning message.
     * </p>
     * 
     * <p> 
     * This message has a default style of white text with a black background.
     * </p>
     * 
     * <h3>CSS Style Ryles</h3>
     * 
     * <ul>
     * <li>.humanMsgInfo</li>
     * </ul>
     * 
     * @param msg the message to show in HTML
     * 
     * @param className
     *        the name of the class to apply
     */
    public static native void showMessage(String msg, String className) /*-{
        $wnd.humanMsg.setup();
        $wnd.jQuery("#humanMsg").removeClass('humanMsgErr').removeClass('humanMsgWarn').removeClass('humanMsgInfo').addClass(className);
        $wnd.humanMsg.displayMsg(msg, false);
    }-*/;

    /**
     * Show a temporary error message and logs it to the client-side error log
     * 
     * @param msg the message to show in HTML
     */
    public static void showError(String msg)
    {
        showErrorJS(msg);
        logError(msg);
    }

    /**
     * <p>
     * Show aan error message.
     * </p>
     * 
     * <p> 
     * This message has a default style of black text with a red background.
     * </p>
     * 
     * <h3>CSS Style Ryles</h3>
     * 
     * <ul>
     * <li>.humanMsgErr</li>
     * </ul>
     * 
     * @param errorMsg the error message for the alert in HTML
     * @param logMsg   the message for the error log in HTML
     */
    public static void showError(String errorMsg, String logMsg)
    {
        showErrorJS(errorMsg);
        logError(logMsg);
    }
    
    /**
      * <p>
      * Show aan error message.
      * </p>
      * 
      * <p> 
      * This message has a default style of black text with a red background.
      * </p>
      * 
      * <h3>CSS Style Ryles</h3>
      * 
      * <ul>
      * <li>.humanMsgErr</li>
      * </ul>
      * 
      * @param errorMsg the error message for the alert in HTML
      * @param logMsg   the message for the error log in HTML
      * @param isSafe if this field is true then this just calls showMessage, otherwise it
      *               runs the specified message through the GWT SaveHtmlUtils
      */
    public static void showError(String errorMsg, String logMsg, boolean isSafe) 
    {
        if (isSafe) {
            showError(errorMsg, logMsg);
        } else {
            showError(SafeHtmlUtils.fromString(errorMsg).asString(),
                      SafeHtmlUtils.fromString(logMsg).asString());
        }
    }

    private static native void showErrorJS(String msg) /*-{
        $wnd.humanMsg.setup();
        $wnd.jQuery("#humanMsg").removeClass('humanMsgInfo').removeClass('humanMsgWarn').addClass('humanMsgErr');
        $wnd.humanMsg.displayMsg(msg, false);
    }-*/;

    /**
     * Write a message to the error log
     * 
     * @param msg the message to show in HTML
     */
    public static void logError(String msg)
    {
        logErrorJS(escapeString(msg));
    }
    
    /**
     * Write a message to the error log
     * 
     * @param msg the message to show in HTML
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

        return "<span class=\"weak\">" + JSDateUtil.getShortTime(new Date()) + "</span> " + 
            s.replaceAll("\\<", "&lt;").replaceAll("\\>", "&gt;");
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
        $wnd.spiffyui.showPageLevelWarning = function(msg) {
            @org.spiffyui.client.MessageUtil::showPageLevelWarning(Ljava/lang/String;)(msg);
        }
        
        $wnd.spiffyui.showFatalError = function(msg) {
            @org.spiffyui.client.MessageUtil::showFatalError(Ljava/lang/String;)(msg);
        }
        
        $wnd.spiffyui.showError = function(msg) {
            @org.spiffyui.client.MessageUtil::showError(Ljava/lang/String;)(msg);
        }
        
        $wnd.spiffyui.showWarning = function(msg, shouldLog) {
            if (shouldLog === null) {
                shouldLog = true;
            }
            @org.spiffyui.client.MessageUtil::showWarning(Ljava/lang/String;Ljava/lang/Boolean;)(msg,shouldLog);
        }
        
        $wnd.spiffyui.showMessage = function(msg) {
            @org.spiffyui.client.MessageUtil::showMessage(Ljava/lang/String;)(msg);
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
        RootPanel root = RootPanel.get("mainContent");
        
        if (root == null) {
            throw new IllegalStateException("Unable to locate the errorpanel element.  You must import spiffyui.min.js before using the MessageUtil.");
        }
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

        m_panel.setVisible(false);

        root.insert(m_panel, 0);
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
        
        Element target = Element.as(event.getNativeEvent().getEventTarget());
        if (null == target) {
            return;
        }
        
        //any click on this will dismiss the panel
        if (m_panel.getElement().isOrHasChild(target)) {
            JSUtil.slideUp("#" + m_panel.getElement().getId(), "fast");
        }
    }
}

/**
 * The page warning shows page level warnings in the UI
 */
class PageWarningPanel extends Composite
{

    private Label m_label;
    private FlowPanel m_panel;

    public PageWarningPanel()
    {
        RootPanel root = RootPanel.get("main");
        
        if (root == null) {
            throw new IllegalStateException("Unable to locate the warning panel element.  You must import spiffyui.min.js before using the MessageUtil.");
        }
        m_panel = new FlowPanel();
        m_panel.getElement().setId("pagewarningpanel");

        m_label = new Label("", true);
        m_panel.add(m_label);

        m_panel.setVisible(false);

        root.add(m_panel);
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
            JSUtil.hide("mainBody");
        }
    }
}
