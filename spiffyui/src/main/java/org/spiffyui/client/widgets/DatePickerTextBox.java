/*******************************************************************************
 * 
 * Copyright 2011-2012 Spiffy UI Team   
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

import org.spiffyui.client.JSDateUtil;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * This widget uses a simple GWT TextBox and attaches the JQuery UI
 * date picker.  When the field gets focus the date picker will become
 * visible.
 */
public class DatePickerTextBox extends TextBox
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
        
    }

    @Override
    public void onLoad()
    {
        super.onLoad();

        addDatePickerJS(this, getElement().getId(), m_format);
        if (m_minDate != null ||
            m_maxDate != null) {
            setDateJS(getElement().getId(), m_minDate, m_maxDate);
        }
    }
    
    
    @Override
    protected void onUnload()
    {
        destroyDatePickerJS(getElement().getId());
        super.onUnload();        
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
    
    private void fireOnChangeEvent()
    {
        /*
         * This is necessary for IE since an onchange event is not fired when a calendar date is selected.
         * This is cleaner than doing it the onPreviewNativeEvent way done in previous versions.
         */
        NativeEvent nativeEvent = Document.get().createChangeEvent();
        ChangeEvent.fireNativeEvent(nativeEvent, this);

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
    

    private static native void addDatePickerJS(DatePickerTextBox x, String id, String format) /*-{
        $wnd.jQuery('#' + id).datepicker({
            dateFormat: format,
            changeMonth: true,
            changeYear: true,
            onSelect: function(dateText, inst) {
                return x.@org.spiffyui.client.widgets.DatePickerTextBox::fireOnChangeEvent()();
            }
        });
    }-*/;
    
    private native void destroyDatePickerJS(String id) /*-{
        $wnd.jQuery("#" + id).datepicker("destroy");
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


}
