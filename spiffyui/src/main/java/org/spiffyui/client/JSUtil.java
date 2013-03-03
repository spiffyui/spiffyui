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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * A set of static JavaScript utilities that provide access to functions from
 * JQuery and some other JavaScript libraries.
 */
@SuppressWarnings("unused")     // supress unused private method warnings from Sonar
public final class JSUtil
{
    /**
     * Making sure this class can't be instantiated.
     */
    private JSUtil()
    {
    }
    
    private static final Map<String, HistoryCallback> HIST_CALLBACKS = new HashMap<String, HistoryCallback>();
    private static final List<HistoryTokenListener> TOKEN_LISTENERS = new ArrayList<HistoryTokenListener>();

    private static int g_uniqueCounter = 0;
    private static final String TITLE = Window.getTitle();
    
    private static boolean g_historyEnabled = true;

    private static String g_previousId;

    static {
        bindJavaScript();
    }

    /**
     * This method is the listener that responds to events in the browser
     * history like the user moving forward or back.  This method is called
     * from JavaScript.
     *
     * @param id the id of the item to select
     */
    private static void doHistory(String id)
    {
        if (g_previousId != null &&
            !g_previousId.equals(getCurrentHistoryStateId())) {

            if (HIST_CALLBACKS.containsKey(id)) {
                HIST_CALLBACKS.get(id).historyChanged(id);
            } else {
                for (HistoryTokenListener listener : TOKEN_LISTENERS) {
                    listener.tokenTrigger(id);
                }
            }
        }

        g_previousId = id;
    }

    /**
     * Add a new HistoryTokenListener token listener to getting access to history tokens.
     * 
     * @param listener the listener to add
     */
    public static void addHistoryTokenListener(HistoryTokenListener listener)
    {
        TOKEN_LISTENERS.add(listener);
    }

    /**
     * Removed the specified history token listener.
     * 
     * @param listener the listener to remove
     * 
     * @return true if the object was found and removed and false otherwise
     */
    public static boolean removeHistoryTokenListener(HistoryTokenListener listener)
    {
        return TOKEN_LISTENERS.remove(listener);
    }

    /**
     * Get the current Spiffy UI history id if it is available.
     * 
     * @return the current history id or null if it is unavailable
     */
    public static native String getCurrentHistoryStateId() /*-{ 
        return $wnd.spiffyui.getHistoryState();
    }-*/;

    /**
     * <p>
     * Adds an item to the browser's history.
     * </p>
     *
     * <p>
     * History items can be added at any item with any unique ID.  When the user
     * navigates the browser's history there will be a collection of items added
     * programmatically and items representing traditional page changes.  When the
     * item added with this method is reached the callback will be called with the
     * specified ID.
     * </p>
     *
     * <p>
     * Spiffy UI uses this method to page changes in the main navigation bar to the
     * browser's history so the user can use the browser forward and back buttons to move
     * between pages in the application.  This method is called by the MainNavBar
     * to add page changes.
     * </p>
     *
     * @param callback  the history callback for this item
     * @param id     the id of the item that was selected
     */
    public static void addHistoryItem(HistoryCallback callback, String id)
    {
        addHistoryItem(callback, id, false);
    }

    /**
     * <p>
     * Adds an item to the browser's history.
     * </p>
     *
     * <p>
     * History items can be added at any item with any unique ID.  When the user
     * navigates the browser's history there will be a collection of items added
     * programmatically and items representing traditional page changes.  When the
     * item added with this method is reached the callback will be called with the
     * specified ID.
     * </p>
     *
     * <p>
     * Spiffy UI uses this method to page changes in the main navigation bar to the
     * browser's history so the user can use the browser forward and back buttons to move
     * between pages in the application.  This method is called by the MainNavBar
     * to add page changes.
     * </p>
     *
     * @param id     the id of the item that was selected
     */
    public static void addHistoryItem(String id)
    {
        addHistoryItem(id, false);
    }

    /**
     * <p>
     * Adds an item to the browser's history.
     * </p>
     *
     * <p>
     * History items can be added at any item with any unique ID.  When the user
     * navigates the browser's history there will be a collection of items added
     * programmatically and items representing traditional page changes.  When the
     * item added with this method is reached the callback will be called with the
     * specified ID.
     * </p>
     *
     * <p>
     * Spiffy UI uses this method to page changes in the main navigation bar to the
     * browser's history so the user can use the browser forward and back buttons to move
     * between pages in the application.  This method is called by the MainNavBar
     * to add page changes.
     * </p>
     * 
     * <p>
     * This method support bookmarking.  When a history item is bookmarkable it will cause
     * a change in the URL which can then be bookmarked by the user to return to that
     * specific history state.
     * </p>
     *
     * @param callback  the history callback for this item
     * @param id     the id of the item that was selected
     * @param bookmarkable  true if this history item should be bookmarkable and false otherwise
     */
    public static void addHistoryItem(HistoryCallback callback, String id, boolean bookmarkable)
    {
        addHistoryItem(callback, id, bookmarkable, TITLE + " - " + id);
    }

    /**
     * <p>
     * Adds an item to the browser's history.
     * </p>
     *
     * <p>
     * History items can be added at any item with any unique ID.  When the user
     * navigates the browser's history there will be a collection of items added
     * programmatically and items representing traditional page changes.  When the
     * item added with this method is reached the callback will be called with the
     * specified ID.
     * </p>
     *
     * <p>
     * Spiffy UI uses this method to page changes in the main navigation bar to the
     * browser's history so the user can use the browser forward and back buttons to move
     * between pages in the application.  This method is called by the MainNavBar
     * to add page changes.
     * </p>
     * 
     * <p>
     * This method support bookmarking.  When a history item is bookmarkable it will cause
     * a change in the URL which can then be bookmarked by the user to return to that
     * specific history state.
     * </p>
     *
     * @param id     the id of the item that was selected
     * @param bookmarkable  true if this history item should be bookmarkable and false otherwise
     */
    public static void addHistoryItem(String id, boolean bookmarkable)
    {
        addHistoryItem(id, bookmarkable, TITLE + " - " + id);
    }
    
    /**
     * <p>
     * Adds an item to the browser's history.
     * </p><p>
     * <p>
     * History items can be added at any item with any unique ID.  When the user
     * navigates the browser's history there will be a collection of items added
     * programmatically and items representing traditional page changes.  When the
     * item added with this method is reached the callback will be called with the
     * specified ID.
     * </p><p>
     * <p>
     * Spiffy UI uses this method to page changes in the main navigation bar to the
     * browser's history so the user can use the browser forward and back buttons to move
     * between pages in the application.  This method is called by the MainNavBar
     * to add page changes.
     * </p><p>
     * <p>
     * This method support bookmarking.  When a history item is bookmarkable it will cause
     * a change in the URL which can then be bookmarked by the user to return to that
     * specific history state.
     * </p>
     * 
     * @param callback the history callback for this item
     * @param id       the id of the item that was selected
     * @param bookmarkable
     *                 true if this history item should be bookmarkable and false otherwise
     * @param title    the browser window title for this history item
     */
    public static void addHistoryItem(HistoryCallback callback, String id, boolean bookmarkable, String title)
    {
        if (g_historyEnabled) {
            HIST_CALLBACKS.put(id, callback);
        }
        addHistoryItemJS(id, "?" + id, title, bookmarkable, false);
    }

    /**
     * <p>
     * Adds an item to the browser's history.
     * </p><p>
     * <p>
     * History items can be added at any item with any unique ID.  When the user
     * navigates the browser's history there will be a collection of items added
     * programmatically and items representing traditional page changes.  When the
     * item added with this method is reached the callback will be called with the
     * specified ID.
     * </p><p>
     * <p>
     * Spiffy UI uses this method to page changes in the main navigation bar to the
     * browser's history so the user can use the browser forward and back buttons to move
     * between pages in the application.  This method is called by the MainNavBar
     * to add page changes.
     * </p><p>
     * <p>
     * This method support bookmarking.  When a history item is bookmarkable it will cause
     * a change in the URL which can then be bookmarked by the user to return to that
     * specific history state.
     * </p>
     * 
     * @param id       the id of the item that was selected
     * @param bookmarkable
     *                 true if this history item should be bookmarkable and false otherwise
     * @param title    the browser window title for this history item
     */
    public static void addHistoryItem(String id, boolean bookmarkable, String title)
    {
        addHistoryItemJS(id, "?" + id, title, bookmarkable, false);
    }
    
    /**
     * <p>
     * Replace the current item in the browser's history.
     * </p>
     *
     * <p>
     * History items can be added at any item with any unique ID.  When the user
     * navigates the browser's history there will be a collection of items added
     * programmatically and items representing traditional page changes.  When the
     * item added with this method is reached the callback will be called with the
     * specified ID.
     * </p>
     *
     * <p>
     * Spiffy UI uses this method to page changes in the main navigation bar to the
     * browser's history so the user can use the browser forward and back buttons to move
     * between pages in the application.  This method is called by the MainNavBar
     * to add page changes.
     * </p>
     * 
     * <p>
     * This method support bookmarking.  When a history item is bookmarkable it will cause
     * a change in the URL which can then be bookmarked by the user to return to that
     * specific history state.
     * </p>
     *
     * @param callback  the history callback for this item
     * @param id     the id of the item that was selected
     * @param bookmarkable  true if this history item should be bookmarkable and false otherwise
     */
    public static void replaceHistoryItem(HistoryCallback callback, String id, boolean bookmarkable)
    {
        replaceHistoryItem(callback, id, bookmarkable, TITLE + " - " + id);
    }

    /**
     * <p>
     * Replace the current item in the browser's history.
     * </p>
     *
     * <p>
     * History items can be added at any item with any unique ID.  When the user
     * navigates the browser's history there will be a collection of items added
     * programmatically and items representing traditional page changes.  When the
     * item added with this method is reached the callback will be called with the
     * specified ID.
     * </p>
     *
     * <p>
     * Spiffy UI uses this method to page changes in the main navigation bar to the
     * browser's history so the user can use the browser forward and back buttons to move
     * between pages in the application.  This method is called by the MainNavBar
     * to add page changes.
     * </p>
     * 
     * <p>
     * This method support bookmarking.  When a history item is bookmarkable it will cause
     * a change in the URL which can then be bookmarked by the user to return to that
     * specific history state.
     * </p>
     *
     * @param id     the id of the item that was selected
     * @param bookmarkable  true if this history item should be bookmarkable and false otherwise
     */
    public static void replaceHistoryItem(String id, boolean bookmarkable)
    {
        replaceHistoryItem(id, bookmarkable, TITLE + " - " + id);
    }
    
    /**
     * <p>
     * Replace the current item in the browser's history.
     * </p>
     *
     * <p>
     * History items can be added at any item with any unique ID.  When the user
     * navigates the browser's history there will be a collection of items added
     * programmatically and items representing traditional page changes.  When the
     * item added with this method is reached the callback will be called with the
     * specified ID.
     * </p>
     *
     * <p>
     * Spiffy UI uses this method to page changes in the main navigation bar to the
     * browser's history so the user can use the browser forward and back buttons to move
     * between pages in the application.  This method is called by the MainNavBar
     * to add page changes.
     * </p>
     * 
     * <p>
     * This method support bookmarking.  When a history item is bookmarkable it will cause
     * a change in the URL which can then be bookmarked by the user to return to that
     * specific history state.
     * </p>
     *
     * @param callback  the history callback for this item
     * @param id     the id of the item that was selected
     * @param bookmarkable  true if this history item should be bookmarkable and false otherwise 
     * @param title    the browser window title for this history item 
     */
    public static void replaceHistoryItem(HistoryCallback callback, String id, boolean bookmarkable, String title)
    {
        if (g_historyEnabled) {
            HIST_CALLBACKS.put(id, callback);
            addHistoryItemJS(id, "?" + id, title, bookmarkable, true);
        }
    }

    /**
     * <p>
     * Replace the current item in the browser's history.
     * </p>
     *
     * <p>
     * History items can be added at any item with any unique ID.  When the user
     * navigates the browser's history there will be a collection of items added
     * programmatically and items representing traditional page changes.  When the
     * item added with this method is reached the callback will be called with the
     * specified ID.
     * </p>
     *
     * <p>
     * Spiffy UI uses this method to page changes in the main navigation bar to the
     * browser's history so the user can use the browser forward and back buttons to move
     * between pages in the application.  This method is called by the MainNavBar
     * to add page changes.
     * </p>
     * 
     * <p>
     * This method support bookmarking.  When a history item is bookmarkable it will cause
     * a change in the URL which can then be bookmarked by the user to return to that
     * specific history state.
     * </p>
     *
     * @param id     the id of the item that was selected
     * @param bookmarkable  true if this history item should be bookmarkable and false otherwise 
     * @param title    the browser window title for this history item 
     */
    public static void replaceHistoryItem(String id, boolean bookmarkable, String title)
    {
        if (g_historyEnabled) {
            addHistoryItemJS(id, "?" + id, title, bookmarkable, true);
        }
    }
    
    /**
     * <p>
     * Determines if browser history integration is enabled.
     * </p>
     * 
     * <p>
     * If history integration is enabled various part of the Spiffy UI framework 
     * and calling code will add items to the browser history.  If not then no part
     * of the framework will maniplate browser history in any way.
     * </p>
     * 
     * <p>
     * History support is enabled by default.
     * </p>
     * 
     * @return true if history support is enabled and false otherwise
     */
    public static boolean isHistoryEnabled()
    {
        return g_historyEnabled;
    }
    
    /**
     * <p>
     * Sets history integration as enabled or disabled.
     * </p><p>
     * <p>
     * If history integration is enabled various part of the Spiffy UI framework
     * and calling code will add items to the browser history.  If not then no part
     * of the framework will maniplate browser history in any way.
     * </p><p>
     * <p>
     * History support is enabled by default.
     * </p>
     * 
     * @param enabled true if history support should be enabled and false otherwise
     */
    public static void setHistoryEnabled(boolean enabled)
    {
        g_historyEnabled = enabled;
    }
    
    /**
     * Move forward one item in the browser history.  If the history is currently at the 
     * end then this method doesn't do anything.
     */
    public static native void forward() /*-{ 
        $wnd.spiffyui.forward();
    }-*/;
    
    /**
     * Move back one item in the browser history.  If the history is currently at the 
     * beginning then this method doesn't do anything.
     */
    public static native void back() /*-{ 
        $wnd.spiffyui.back();
    }-*/;
    
    /**
     * Move a variable number of items in the browser history.
     * 
     * @param steps  If steps is negative go back through history that number of times, otherwise
     *               step forward in the history that number of times
     */
    public static native void go(int steps) /*-{ 
        $wnd.spiffyui.go(steps);
    }-*/;
    
    private static native void addHistoryItemJS(String id, String url, String title, boolean bookmarkable, boolean replace) /*-{ 
        $wnd.spiffyui.addHistoryItem($wnd, id, url, title, bookmarkable, replace);
    }-*/;

    private static native void bindJavaScript() /*-{ 
        $wnd.spiffyui.doHandleHistoryEvent = function(id) {
            @org.spiffyui.client.JSUtil::doHistory(Ljava/lang/String;)(id);
        }
    }-*/;


    /**
     * Generate a probably unique ID.  It is based on current time.
     *
     * @return the unique ID as a string
     */
    public static String generateUniqueId()
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
        doSlideDown(id, speed, null);
    }

    /**
     * Shows an element with a slide down effect.
     *
     * @param id
     *        the id for the element to show
     * @param speed
     *        the speed to fade in. options are slow, normal, fast or a number
     * @param callback the callback called when the animation is complete
     */
    public static void slideDown(String id, String speed, JSEffectCallback callback)
    {
        doSlideDown(id, speed, callback);
    }

    private static native void doSlideDown(String id, String speed, JSEffectCallback callback) /*-{
        $wnd.jQuery($wnd.spiffyui.formatId(id)).slideDown(speed, function() {
            @org.spiffyui.client.JSUtil::doCallback(Lorg/spiffyui/client/JSEffectCallback;Ljava/lang/String;)(callback, id);
        });
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
        doSlideUp(id, speed, null);
    }

    /**
     * Hides an element with a slide up effect.
     *
     * @param id
     *        the id for the element to show
     * @param speed
     *        the speed to fade in. options are slow, normal, fast or a number
     * @param callback the callback called when the animation is complete
     */
    public static void slideUp(String id, String speed, JSEffectCallback callback)
    {
        doSlideUp(id, speed, callback);
    }

    private static native void doSlideUp(String id, String speed, JSEffectCallback callback) /*-{
        $wnd.jQuery($wnd.spiffyui.formatId(id)).slideUp(speed, function() {
            @org.spiffyui.client.JSUtil::doCallback(Lorg/spiffyui/client/JSEffectCallback;Ljava/lang/String;)(callback, id);
        });
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
        doToggleSlide(id, speed, null);
    }

    /**
     * Toggles the visibility of the specified element with a vertical slide effect
     *
     * @param id
     *                 the id for the element to show
     * @param speed
     *                 the speed to fade in. options are slow, normal, fast or a number
     * @param callback the callback called when the animation is complete
     */
    public static void toggleSlide(String id, String speed, JSEffectCallback callback)
    {
        doToggleSlide(id, speed, callback);
    }

    private static native void doToggleSlide(String id, String speed, JSEffectCallback callback) /*-{
        if ($wnd.jQuery($wnd.spiffyui.formatId(id)).is(':visible')) {
            $wnd.jQuery($wnd.spiffyui.formatId(id)).slideUp(speed, function() {
                @org.spiffyui.client.JSUtil::doCallback(Lorg/spiffyui/client/JSEffectCallback;Ljava/lang/String;)(callback, id);
            });
        } else {
            $wnd.jQuery($wnd.spiffyui.formatId(id)).slideDown(speed, function() {
                @org.spiffyui.client.JSUtil::doCallback(Lorg/spiffyui/client/JSEffectCallback;Ljava/lang/String;)(callback, id);
            });
        }
    }-*/;

    private static void doCallback(JSEffectCallback callback, String id)
    {
        if (callback != null) {
            callback.effectComplete(id);
        }
    }

    /**
     * Toggles the visibility of the specified element with a horizontal slide effect
     * sliding from the left side.
     *
     * @param id
     *        the id for the element to show
     * @param callback the callback called when the animation is complete
     */

    public static native void horizontalToggleSlide(String id, JSEffectCallback callback)  /*-{
        $wnd.jQuery($wnd.spiffyui.formatId(id)).animate({width: 'toggle'}, function() {
            @org.spiffyui.client.JSUtil::doCallback(Lorg/spiffyui/client/JSEffectCallback;Ljava/lang/String;)(callback, id);
        });
    }-*/;

    /**
     * Toggles the visibility of the specified element with a horizontal slide effect
     * sliding from the left side.
     *
     * @param id
     *        the id for the element to show
     */

    public static native void horizontalToggleSlide(String id)  /*-{
        $wnd.jQuery($wnd.spiffyui.formatId(id)).animate({width: 'toggle'});
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
        $wnd.jQuery($wnd.spiffyui.formatId(id)).effect("bounce", { times:times, distance: distance }, speed);
    }-*/;

    /**
     * Causes the specified element to bounce the specified number of times
     * at the specified speed.
     *
     * @param id the ID of the element to bounce
     * @param times the number of times to bounce the element
     * @param speed the speed of the bounce animation in milliseconds
     * @param distance the distance in pixels to bounds the element - default is 20
     * @param callback the callback called when the animation is complete
     */
    public static native void bounce(String id, int times, int speed, int distance, JSEffectCallback callback)  /*-{
        $wnd.jQuery($wnd.spiffyui.formatId(id)).effect("bounce", { times:times, distance: distance }, speed, function() {
            @org.spiffyui.client.JSUtil::doCallback(Lorg/spiffyui/client/JSEffectCallback;Ljava/lang/String;)(callback, id);
        });
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
        $wnd.jQuery($wnd.spiffyui.formatId(id)).hide(speed);
    }-*/;

    /**
     * Hides an element with a fade effect.
     *
     * @param id
     *        the id for the element to show
     * @param speed
     *        the speed to fade in. options are slow, normal, fast or a number
     * @param callback the callback called when the animation is complete
     */
    public static native void hide(String id, String speed, JSEffectCallback callback) /*-{
        $wnd.jQuery($wnd.spiffyui.formatId(id)).hide(speed, function() {
            @org.spiffyui.client.JSUtil::doCallback(Lorg/spiffyui/client/JSEffectCallback;Ljava/lang/String;)(callback, id);
        });
    }-*/;

    /**
     * Hides the specified element.
     *
     * @param id
     *        the id for the element to show
     *
     */
    public static native void hide(String id) /*-{
        $wnd.jQuery($wnd.spiffyui.formatId(id)).hide();
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
        $wnd.jQuery($wnd.spiffyui.formatId(id)).text(txt);
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
        $wnd.jQuery($wnd.spiffyui.formatId(id)).show(speed);
    }-*/;

    /**
     * Shows an element with a fade effect.
     *
     * @param id
     *        the id for the element to show
     * @param speed
     *        the speed to fade in. options are slow, normal, fast or a number
     * @param callback the callback called when the animation is complete
     */
    public static native void show(String id, String speed, JSEffectCallback callback) /*-{
        $wnd.jQuery($wnd.spiffyui.formatId(id)).show(speed, function() {
            @org.spiffyui.client.JSUtil::doCallback(Lorg/spiffyui/client/JSEffectCallback;Ljava/lang/String;)(callback, id);
        });
    }-*/;

    /**
     * Shows the specified element.
     *
     * @param id
     *        the id for the element to show
     *
     */
    public static native void show(String id) /*-{
        $wnd.jQuery($wnd.spiffyui.formatId(id)).show();
    }-*/;

    /**
     * <p>
     * Prints an info message to the Firebug console, Chrome developer
     * console, or Internet Explorer dev mode console.
     * </p>
     *
     * <p>
     * This method is very useful for debugging when you aren't using GWT
     * hosted mode.  The message print out to the console one item at a time
     * and don't do anything if the console isn't available.
     * </p>
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
        var visible = $wnd.jQuery(selector + ":visible");
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
     * @param targetId - the ID of the target
     * @param sectionId - the ID of the section to toggle
     * @return boolean true if the target does match the element with the targetId
     */
    public static boolean toggleSection(Element target, HTMLPanel panel, String targetId, String sectionId)
    {
        if (!((target != null) && DOM.isOrHasChild(panel.getElementById(targetId),
                                                   target))) {
            return false;
        }

        String trimmedSectionId = sectionId;

        if (!sectionId.startsWith("#")) {
            trimmedSectionId = "#" + sectionId;
        }

        if (JSUtil.isVisible(trimmedSectionId)) {
            collapseSection(panel, targetId, trimmedSectionId, true);
        } else {
            expandSection(panel, targetId, trimmedSectionId, true);
        }
        return true;
    }

    /**
     * Expands a section
     * @param panel - HTMLPanel holding the target
     * @param targetId - the ID of the target
     * @param sectionId - the ID of the section to toggle
     * @param animate - boolean true to use slide down effect, false to just display:none
     */
    public static void expandSection(HTMLPanel panel, String targetId, String sectionId, boolean animate)
    {
        if (animate) {
            JSUtil.slideDown(sectionId, "fast");
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
     * @param targetId - the ID of the target
     * @param sectionId - the ID of the section to toggle
     * @param animate - boolean true to use slide down effect, false to just display:none
     */
    public static void collapseSection(HTMLPanel panel, String targetId, String sectionId, boolean animate)
    {
        if (animate) {
            JSUtil.slideUp(sectionId, "fast");
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

    private static native boolean validateEmailAddress(String email) /*-{
        var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return email.match(re) !== null;
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
            return s.substring(0, s.length() - delim.length());
        }  else {
            return s;
        }
    }


    /**
     * <p>
     * Get the height of the element with the specified ID.
     * </p>
     *
     * <p>
     * This method gets the height of the specified element on the screen including padding
     * and margins.  The element must be visible and attached to the page.
     * </p>
     *
     * @param id     the id of the element example: myElement
     *
     * @return the height of the element
     */
    public static native int getHeight(String id) /*-{
        return $wnd.jQuery($wnd.spiffyui.formatId(id)).height();
    }-*/;

    /**
     * <p>
     * Get the width of the element with the specified ID.
     * </p>
     *
     * <p>
     * This method gets the width of the specified element on the screen including padding
     * and margins.  The element must be visible and attached to the page.
     * </p>
     *
     * @param id     the id of the element example: myElement
     *
     * @return the width of the element
     */
    public static native int getWidth(String id) /*-{
        return $wnd.jQuery($wnd.spiffyui.formatId(id)).width();
    }-*/;

    /**
     * <p>
     * Parse the specified JSON string into a JavaScript object.
     * </p>
     *
     * <p>
     * This method parses the specified string of well-formed JSON and returns
     * a JavaScript object.  It calls JSON.parse when it is available and calls
     * eval only on older browsers which don't support JSON.parse.
     * </p>
     *
     * @param jsonString the json string to parse
     *
     * @return the JavaScript object representing the JSON string
     */
    public static native JavaScriptObject parseJSON(String jsonString) /*-{
        return $wnd.jQuery.parseJSON(jsonString);
    }-*/;

    /**
     * Reloads the current URL while hiding any error messages.
     * 
     * When you reload the window it causes any AJAX requests in process to
     * fail and that can cause error messages to flash on the screen before
     * the window reloads.  This methods hides all error messages before 
     * reloading the window.
     */
    public static void reload()
    {
        MessageUtil.setShouldShowMessages(false);
        Window.Location.reload();
    }
    
    /**
     * Reloads the current URL while hiding any error messages.
     * 
     * When you reload the window it causes any AJAX requests in process to
     * fail and that can cause error messages to flash on the screen before
     * the window reloads.  This methods hides all error messages before
     * reloading the window.
     * 
     * @param force  true if the framework should force a refresh by adding a new parameter to the URL and
     *               false if it should just use the current URL to refresh.
     */
    public static void reload(boolean force)
    {
        if (force) {
            reload();
        } else {
            MessageUtil.setShouldShowMessages(false);
            String url = Window.Location.getHref();
            
            if (url.indexOf('?') > 0) {
                Window.Location.replace(url + "&d=" + new Date().getTime());
            } else {
                Window.Location.replace(url + "?d=" + new Date().getTime());
            }
        }
    }

    /**
     * This method detects if the browser is running on a mobile device.
     * 
     * @return returns true if the browser is running on an iPhone, iPad, iPod, or
     *         Android device and false otherwise.
     */
    public static native boolean isMobile() /*-{
        return $wnd.spiffyui.isMobile;
    }-*/;


}
