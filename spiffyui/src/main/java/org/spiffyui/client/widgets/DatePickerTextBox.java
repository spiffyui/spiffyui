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
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;

import org.spiffyui.client.JSDateUtil;

/**
 * This widget uses a simple GWT TextBox and attaches the JQuery UI
 * date picker.  When the field gets focus the date picker will become
 * visible.
 */
public class DatePickerTextBox extends TextBox implements Event.NativePreviewHandler
{
    private String m_minDate;
    private String m_maxDate;

    private String m_format;    
    
    /**
     * Create a DatePickerTextBox with a rendomly generated ID.  The ID is required
     * because the JQuery data picker needs a specific ID to connect to.
     *  
     */
    public DatePickerTextBox()
    {
        this(HTMLPanel.createUniqueId());
    }

    /**
     * Create a DatePickerTextBox with the specified ID.  The ID is required
     * because the JQuery data picker needs a specific ID to connect to.
     * 
     * @param id     the id for the text box
     */
    public DatePickerTextBox(String id)
    {
        super();
        getElement().setId(id);        

        /*
         * The JQuery date picker follows slightly different rules for date
         * formats than our date library does.  We need to do a little conversion
         * to make it work.  See the date format here:
         *
         * http://docs.jquery.com/UI/Datepicker/formatDate
         */
        m_format = JSDateUtil.getShortDateFormat().replace('M', 'm').replaceAll("yyyy", "yy");
        
        Event.addNativePreviewHandler(this);
    }

    @Override
    public void onAttach()
    {
        super.onAttach();

        addDatePickerJS(getElement().getId(), m_format);
        if (m_minDate != null ||
            m_maxDate != null) {
            setDateJS(getElement().getId(), m_minDate, m_maxDate);
        }
    }
    
    /**
     * Set a minimum selectable date via a Date object or as a string in 
     * the current dateFormat, or a number of days from today (e.g. +7) 
     * or a string of values and periods ('y' for years, 'm' for months, 
     * 'w' for weeks, 'd' for days, e.g. '-1y -1m'), or null for no limit. 
     *  
     * <b>Code examples</b>
     * 
     * setMinimumDate("-20"); 
     * setMinimumDate("+1M +10D");
     * 
     * @param dateSpecifier
     *               the date specifier
     */
    public void setMinimumDate(String dateSpecifier)
    {
        m_minDate = dateSpecifier;
        setDateJS(getElement().getId(), m_minDate, m_maxDate);
    }

    /**
     * Set a minimum selectable date via a java.util.Date object
     * 
     * @param date
     *               the date specifier
     */
    public void setMinimumDate(Date date)
    {
        m_minDate = JSDateUtil.getDate(date);
        setDateJS(getElement().getId(), m_minDate, m_maxDate);       
    }
    
    /**
     * Gets the minimum date specifier for this date picker
     * 
     * @return the minimum date specifier
     */
    public String getMinimumDate()
    {
        return m_minDate;
    }
    
    /**
     * Set a maximum selectable date via a Date object or as a string in 
     * the current dateFormat, or a number of days from today (e.g. +7) 
     * or a string of values and periods ('y' for years, 'm' for months, 
     * 'w' for weeks, 'd' for days, e.g. '-1y -1m'), or null for no limit. 
     *  
     * <b>Code examples</b>
     * 
     * setMaximumDate("-20"); 
     * setMaximumDate("+1M +10D");
     * 
     * @param dateSpecifier
     *               the date specifier
     */
    public void setMaximumDate(String dateSpecifier)
    {
        m_maxDate = dateSpecifier;
        setDateJS(getElement().getId(), m_minDate, m_maxDate);
    }

    /**
     * Set a maximum selectable date via a java.util.Date object
     * 
     * @param date
     *               the date specifier
     */
    public void setMaximumDate(Date date)
    {
        m_maxDate = JSDateUtil.getDate(date);
        setDateJS(getElement().getId(), m_minDate, m_maxDate);
    }
    
    /**
     * Gets the minimum date specifier for this date picker
     * 
     * @return the minimum date specifier
     */
    public String getMaximumDate()
    {
        return m_maxDate;
    }
    
    private static native void setDateJS(String id, String mindate, String maxdate)  /*-{ 
        if (maxdate) {
            $wnd.jQuery('#' + id).datepicker('option', 'maxDate',
                   $wnd.Date.parseExact(maxdate, $wnd.Date.CultureInfo.formatPatterns.shortDate));
        }
     
        if (mindate) {
            $wnd.jQuery('#' + id).datepicker('option', 'minDate',
                   $wnd.Date.parseExact(mindate, $wnd.Date.CultureInfo.formatPatterns.shortDate));
        }
    }-*/;
    

    private static native void addDatePickerJS(String id, String format) /*-{
        $wnd.jQuery('#' + id).datepicker({
            dateFormat: format,
            changeMonth: true,
            changeYear: true
        });
    }-*/;
    
    /**
     * Get the value as a type-safe java.util.Date
     * @return the value in the TextBox as a java.util.Date or null if it was unparseable.
     */
    public Date getDateValue() 
    {
        if (isEmpty()) {
            return null;
        } else {
            try {
                return JSDateUtil.parseShortDate(getValue());
            } catch (IllegalArgumentException e) {
                //the date was unparseable also return null
                return null;
            }
        }
    }

    /**
     * Sets the current Date value
     *
     * @param date the date value to set
     */
    public void setDateValue(Date date)
    {
        setValue(JSDateUtil.getDate(date));
    }
    
    /**
     * Convenience method to determine if the value is empty
     * since getDateValue will return null for empty as well as unparseable.
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
         * This is necessary for IE since an onchange event is not fired when a calendar date is selected.
         * And trying to add onSelect with JSNI would call the correct Java method, but it would not be able
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
         * See if it is a child <A> tag of ui-datepicker-div because all the selectable dates are anchors
         */
        if (DOM.getElementById("ui-datepicker-div") != null &&
            DOM.isOrHasChild(DOM.getElementById("ui-datepicker-div"), target) &&
                "A".equalsIgnoreCase(target.getTagName())) {

            NativeEvent nativeEvent = Document.get().createChangeEvent();
            ChangeEvent.fireNativeEvent(nativeEvent, this);

            return;
        }
    }
    

}
