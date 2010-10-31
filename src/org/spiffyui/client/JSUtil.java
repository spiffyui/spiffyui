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

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.novell.spiffyui.client.nav.MainNavBar;
import com.novell.spiffyui.client.nav.NavItem;

/**
 * A set of static JavaScript utilities.
 */
public final class JSUtil
{
    private static int g_uniqueCounter = 0;
      
    static {
        bindJavaScript();
    }
    
    /**
     * This method is the listener that responds to events in the browser
     * history like the user moving forward or back.  This method is called
     * from JavaScript.
     * 
     * @param panel the main nav bar panel to move
     * @param id the id of the item to select
     */
    private static void doHistory(MainNavBar panel, String id)
    {
        NavItem item = panel.getItem(id);
        if (item != null) {
            panel.selectItem(item, false, true);
        }
    }
    
    /**
     * Adds an item to the browser's history.  We add page changes to the browser's
     * history so the user can use the browser forward and back buttons to move
     * between pages in the application.  This method is called by the MainNavBar
     * to add page changes.
     * 
     * @param panel  the MainNavBar panel
     * @param id     the id of the item that was selected
     */
    public static native void addHistoryItem(MainNavBar panel, String id) /*-{ 
        var item = {
            panel: panel,
            id: id
        };
        $wnd.dsHistory.addFunction($wnd.spiffyui.handleHistoryEvent, this, item); 
    }-*/;
    
    private static final native void bindJavaScript() /*-{ 
        $wnd.spiffyui.handleHistoryEvent = function(contentObject, historyObject) {
            @com.novell.spiffyui.client.JSUtil::doHistory(Lcom/novell/spiffyui/client/nav/MainNavBar;Ljava/lang/String;)(contentObject.panel, contentObject.id);
        }
     
        $wnd.dsHistory.addFunction($wnd.spiffyui.handleHistoryEvent);
    }-*/;
    
    
    /**
     * Generate a probably unique ID.  It is based on current time.
     * 
     * @return the unique ID as a string
     */
    public static final String generateUniqueId()
    {
        return Long.toHexString(new Date().getTime()) + "-" + g_uniqueCounter++;
    }
    
    /**
     * Shows an element with a slide down effect.
     * 
     * @param id
     *        the id for the element to show
     * @param speed
     *        the speed to fade in. options are slow, normal, fast or a number
     */
    public static void slideDown(String id, String speed)
    {
        doSlideDown(id, speed);
    }

    private static native void doSlideDown(String id, String speed) /*-{
                                $wnd.$(id).slideDown(speed);
                            }-*/;

    /**
     * Hides an element with a slide up effect.
     * 
     * @param id
     *        the id for the element to show
     * @param speed
     *        the speed to fade in. options are slow, normal, fast or a number
     */
    public static void slideUp(String id, String speed)
    {
        doSlideUp(id, speed);
    }

    private static native void doSlideUp(String id, String speed) /*-{
                                $wnd.$(id).slideUp(speed);
                            }-*/;
    
    /**
     * Toggles the visibility of the specified element with a vertical slide effect
     * 
     * @param id
     *        the id for the element to show
     * @param speed
     *        the speed to fade in. options are slow, normal, fast or a number
     */
    public static void toggleSlide(String id, String speed)
    {
        doToggleSlide(id, speed);
    }

    private static native void doToggleSlide(String id, String speed) /*-{
        if ($wnd.$(id).is(':visible')) {
            $wnd.$(id).slideUp(speed);
        } else {
            $wnd.$(id).slideDown(speed);
        }
    }-*/;
    
    /**
     * Toggles the visibility of the specified element with a horizontal slide effect 
     * sliding from the left side. 
     * 
     * @param id
     *        the id for the element to show (#myControl)
     */

    public static native void horizontalToggleSlide(String id)  /*-{
        $wnd.$(id).animate({width: 'toggle'});
    }-*/;
    
    /**
     * Causes the specified element to bounce the specified number of times 
     * at the specified speed. 
     * 
     * @param id the ID of the element to bounce
     * @param times the number of times to bounce the element
     * @param speed the speed of the bounce animation in milliseconds 
     * @param distance the distance in pixels to bounds the element - default is 20 
     */
    public static native void bounce(String id, int times, int speed, int distance)  /*-{
        $wnd.$(id).effect("bounce", { times:times, distance: distance }, speed);
    }-*/;
    
    /**
     * Hides an element with a fade effect.
     * 
     * @param id
     *        the id for the element to show
     * @param speed
     *        the speed to fade in. options are slow, normal, fast or a number
     */
    public static native void hide(String id, String speed) /*-{
        $wnd.$(id).hide(speed);
    }-*/;
    
    /**
     * Hides the specified element.
     * 
     * @param id
     *        the id for the element to show
     * 
     */
    public static native void hide(String id) /*-{
        $wnd.$(id).hide();
    }-*/;
    
    /**
     * Set the text for an element
     * 
     * @param id
     *        the id for the element
     * @param txt
     *        text to set for the element
     */
    public static native void setText(String id, String txt) /*-{
        $wnd.$(id).text(txt);
    }-*/;

    /**
     * Shows an element with a fade effect.
     *
     * @param id
     *        the id for the element to show
     * @param speed
     *        the speed to fade in. options are slow, normal, fast or a number
     */
    public static native void show(String id, String speed) /*-{
        $wnd.$(id).show(speed);
    }-*/;
    
    /**
     * Shows the specified element.
     *
     * @param id
     *        the id for the element to show
     * 
     */
    public static native void show(String id) /*-{
        $wnd.$(id).show();
    }-*/;

    /**
     * Prints an info message to the Firebug console.  If the Firebug 
     * console is not enable this method doesn't do anything. 
     * 
     * @param msg
     *        the message to print
     */
    public static native void println(String msg) /*-{
        if ($wnd.window.console) {
            $wnd.console.info(msg);
        }
    }-*/;

    /**
     * logs an object to the Firebug console for debugging purposes. If the Firebug
     * console is not enabled this method doesn't do anything.
     *
     * @param o
     *        the object to log to firebug console
     */
    public static native void logObjForDebug(Object o) /*-{
        if ($wnd.window.console) {
            $wnd.console.log(o);
        }
    }-*/;
    
    /**
     * Prints an error message to the Firebug console.  If the Firebug 
     * console is not enable this method doesn't do anything. 
     * 
     * @param msg
     *        the message to print
     */
    public static native void printError(String msg) /*-{
        if ($wnd.window.console) {
            $wnd.console.error(msg);
        }
    }-*/;
    
    /**
     * Determines if the element(s) defined by the specified selector is 
     * visible.  This doesn't always mean that is is on the screen, only 
     * that it would be visible if it were. 
     * 
     * @param selector
     *        the elements to inspect
     *  
     * @return true if any of the selected elements are visible and false otherwise
     */
    public static native boolean isVisible(String selector) /*-{
        var visible = $wnd.$(selector + ":visible");
        if (visible && visible.length > 0) {
            return true;
        } else {
            return false;
        }
    }-*/;
    
    /**
     * Toggle hiding or showing a section
     * @param target - the event's onclick target
     * @param panel - the parent HTMLPanel of the target
     * @param targetId - the ID of the target (not the selector so no # necessary)
     * @param sectionId - the ID of the section to toggle (not the selector so no # necessary)
     * @return boolean true if the target does match the element with the targetId
     */
    public static boolean toggleSection(Element target, HTMLPanel panel, String targetId, String sectionId)
    {
        if (!((target != null) && DOM.isOrHasChild(panel.getElementById(targetId), 
                                                   target))) {
            return false;
        }
        if (JSUtil.isVisible("#" + sectionId)) {
            collapseSection(panel, targetId, sectionId, true);
        } else {
            expandSection(panel, targetId, sectionId, true);
        }
        return true;
    }

    /**
     * Expands a section
     * @param panel - HTMLPanel holding the target
     * @param targetId - the ID of the target (not the selector so no # necessary)
     * @param sectionId - the ID of the section to toggle (not the selector so no # necessary)
     * @param animate - boolean true to use slide down effect, false to just display:none
     */
    public static void expandSection(HTMLPanel panel, String targetId, String sectionId, boolean animate)
    {
        if (animate) {
            JSUtil.slideDown("#" + sectionId, "fast");
        } else {
            Style style = panel.getElementById(sectionId).getStyle();
            style.setDisplay(Display.BLOCK);
        }

        prependClassName(panel.getElementById(targetId), "expanded");
    }
    
    private static void prependClassName(Element element, String classname)
    {
        String clazz = element.getClassName();
        int index = clazz.indexOf(classname);
        if (index > -1) {
            /*
             * Then it was already there and we are done
             */
            return;
        }
        element.setClassName(classname + " " + element.getClassName());
    }

    /**
     * Collapses a section
     * @param panel - HTMLPanel holding the target
     * @param targetId - the ID of the target (not the selector so no # necessary)
     * @param sectionId - the ID of the section to toggle (not the selector so no # necessary)
     * @param animate - boolean true to use slide down effect, false to just display:none
     */
    public static void collapseSection(HTMLPanel panel, String targetId, String sectionId, boolean animate)
    {
        if (animate) {
            JSUtil.slideUp("#" + sectionId, "fast");
        } else {
            Style style = panel.getElementById(sectionId).getStyle();
            style.setDisplay(Display.NONE);
        }
        panel.getElementById(targetId).removeClassName("expanded");
    }    
    
    /**
     * Base64 encode a string
     * 
     * @param s      the string to encode
     * 
     * @return the encoded string
     */
    public static native String base64Encode(String s)  /*-{
        return $wnd.Base64.encode(s);
    }-*/;
    
    /**
     * Base64 decode a string
     * 
     * @param s      the string to decode
     * 
     * @return the decoded string
     */
    public static native String base64Decode(String s)  /*-{
        return $wnd.Base64.decode(s);
    }-*/;
    
    /**
     * Validate an email address entry such that both the
     * following will pass: Allison Blake <ablake@novell.com> or ablake@novell.com
     * @param s - the String to test
     * @return - true if valid
     */
    public static boolean validateEmail(String s) 
    {
        int ltIndex = s.indexOf('<');
        int gtIndex = s.indexOf('>');
        if (ltIndex > 0 && s.indexOf('>') == s.length() - 1) {
            String email = s.substring(ltIndex + 1, gtIndex - 1);
            return validateEmailAddress(email);
        } else {
            return validateEmailAddress(s);
        }
    }
    
    private static native boolean validateEmailAddress(String s) /*-{
        var filter = /^([a-zA-Z0-9_.-])+@(([a-zA-Z0-9-])+.)+([a-zA-Z0-9]{2,4})+$/;
        return filter.test(s);
    }-*/;
    
    /**
     * Returns a String without the last delimiter
     * @param s - String to trim
     * @param delim - the delimiter
     * @return the String without the last delimter
     */
    public static String trimLastDelimiter(String s, String delim)
    {
        if (s.length() > 0) {
            s = s.substring(0, s.length() - delim.length());
        } 
        return s;
    }
}
