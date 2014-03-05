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

import java.util.Date;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;

import org.spiffyui.client.JSDateUtil;

/**
 * This widget uses a simple GWT TextBox and attaches the JQuery 
 * time picker.  When the field gets focus the date picker will become
 * visible.
 */
public class TimePickerTextBox extends TextBox implements Event.NativePreviewHandler
{
    private String m_timePickerDivId;
    
    /**
     * Create a TimePickerTextBox with a random ID.  The ID is required
     * because the JQuery data picker needs a specific ID to connect to. 
     * 
     */
    public TimePickerTextBox()
    {
        this(HTMLPanel.createUniqueId());
    }
    
    
    /**
     * Create a TimePickerTextBox with the specified ID.  The ID is required
     * because the JQuery data picker needs a specific ID to connect to.
     * 
     * @param id     the id for the text box
     */
    public TimePickerTextBox(String id)
    {
        super();
        getElement().setId(id);
        m_timePickerDivId = id + "-ui-timepicker-div";
        //A time picker div ID (tpDivId) was added by rpt because there may be more than one
        //time picker on a single page so each id would need to be unique.  The id was added in rpt
        //in order for this control to be fully functional in IE (see onPreviewNativeEvent).
        
        Event.addNativePreviewHandler(this);
    }

    @Override
    public void onAttach()
    {
        super.onAttach();

        addTimePickerJS(getElement().getId(), JSDateUtil.is24Time(), m_timePickerDivId);
    }

    private native void addTimePickerJS(String id, boolean is24Hour, String tpDivId) /*-{ 
        try {
            $wnd.jQuery("#" + id).timePicker({
                startTime: is24Hour ? "0:00" : "12:00", // Using string. Can take string or Date object.
                show24Hours: is24Hour,
                step: 30,
                timeFormat: $wnd.Date.CultureInfo.formatPatterns.shortTime,
                amDesignator: $wnd.Date.CultureInfo.amDesignator,
                pmDesignator: $wnd.Date.CultureInfo.pmDesignator,
                tpDivId: tpDivId});
        } catch(err) {
            $wnd.console.error(err);
        }
    }-*/;
    
    /**
     * Sets the value to be the short time of the date
     *
     * @param date the date value to set
     */
    public void setDateValue(Date date)
    {
        setValue(JSDateUtil.getShortTime(date));
    }
    
    /**
     * Sets the value to be the time of the date on the or rounded up to 
     * the nearest hour or half hour
     *
     * @param date the date value to set
     */
    public void setDateValueRounded(Date date)
    {
        setValue(JSDateUtil.getShortTimeRounded(date.getTime() + ""));
    }
    
    /**
     * Gets the current hours value of the text box
     * 
     * @return the hours value or -1 if it isn't specified
     */
    public int getHours()
    {
        if (getValue().length() == 0) {
            return -1;
        } else {
            try {
                return JSDateUtil.getHours(getValue());
            } catch (Exception e) {
                //if unparseable return -1
                return -1;                
            }
        }
    }
    
    /**
     * Gets the current minutes value of the text box
     * 
     * @return the hours value or -1 if it isn't specified
     */
    public int getMinutes()
    {
        if (getValue().length() == 0) {
            return -1;
        } else {
            try {
                return JSDateUtil.getMinutes(getValue());
            } catch (Exception e) {
                //if unparseable return -1;
                return -1;                
            }
        }
    }
    
    /**
     * Convenience method to determine if the value is empty
     * since the getHours and getMinutes will return -1 for empty as well as unparseable.
     * @return boolean true if empty
     */
    public boolean isEmpty()
    {
        return getValue().length() == 0;
    }

    @Override
    public void onPreviewNativeEvent(NativePreviewEvent event)
    {
        /*
         * This is necessary for IE since an onchange event is not fired when a time is selected.
         * And trying to have the jquery call a JSNI would call the correct Java method, but it would not be able
         * to fireEvent because it did not think it had any Handlers.  Even a call to getValue would not work,
         * resulting in an AssertionError saying that the Widget may not have been initialized.
         */
        if (event.getTypeInt() != Event.ONCLICK) {
            return;
        }
        Element target = (Element) com.google.gwt.dom.client.Element.as(event.getNativeEvent().getEventTarget());
        if (null == target) {
            return;
        }

        /*
         * See if it is a child LI tag within ui-timepicker-div because all times are in <LI>
         */
        if (DOM.isOrHasChild(DOM.getElementById(m_timePickerDivId), target) &&
                "LI".equalsIgnoreCase(target.getTagName())) {

            NativeEvent nativeEvent = Document.get().createChangeEvent();
            ChangeEvent.fireNativeEvent(nativeEvent, this);
            
            return;
        }
    }
}
