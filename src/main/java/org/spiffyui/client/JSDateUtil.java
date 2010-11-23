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
package org.spiffyui.client;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * This is a collection of utility methods for manipulating dates in the browser.
 * All of these methods are locale aware and use the current locale of the browser. 
 * These date functions use the libraries from <a href="http://www.datejs.com">http://www.datejs.com</a>. 
 * This package has much better date support than the current GWT date support. 
 */
public final class JSDateUtil
{
    
    /**
     * Making sure this class can't be instantiated.
     */
    private JSDateUtil()
    {
    }
    
    /**
     * Convert java.util.Date to Date format in the current locale
     * @param date - the Date object to convert
     * @return - Date format in String
     */    
    public static String getDate(Date date)
    {
        return getDate(date.getTime() + "");
    }
    
    /**
     * Convert UTC epoch format to Date format in the current locale 
     *  
     * @param epochDate - the time in milliseconds since Jan , 1, 1970
     * @return - Date format in String
     */    
    public static native String getDate(String epochDate) /*-{
        return $wnd.spiffyui.getDate(parseInt(epochDate));
    }-*/;
    
    /**
     * Convert UTC epoch format to Date format in the current locale 
     *  
     * @param epochDate - the time in milliseconds since Jan , 1, 1970
     * @return - Date format in String
     */    
    public static native String getLongDate(String epochDate) /*-{
        return $wnd.spiffyui.getLongDate(parseInt(epochDate));
    }-*/;

    /**
     * Convert UTC epoch format to Date format in the current locale 
     *  
     * @return - Date format in String
     */    
    public static native String getToday() /*-{
        return String($wnd.spiffyui.getToday());
    }-*/;
    
    /**
     * Adds the 1 day to today 
     *  
     * @return - epochDate time in milliseconds String
     */    
    public static native String nextDay() /*-{
        return String($wnd.spiffyui.nextDay());
    }-*/;
    
    
    /**
     * Convert java.util.Date to Short Time format in the current locale
     * @param date - the Date object to convert
     * @return - Time format in String
     */    
    public static String getShortTime(Date date)
    {
        return getShortTime(date.getTime() + "");
    }
    
    /**
     * Convert UTC epoch format to short time format in the current locale 
     *  
     * @param epochDate - the time in milliseconds since Jan , 1, 1970
     * @return - Time format in String
     */    
    public static native String getShortTime(String epochDate) /*-{
        return $wnd.spiffyui.getShortTime(parseInt(epochDate));
    }-*/;
    
    /**
     * Convert UTC epoch format to short time format in the current locale 
     * rounded up to the nearest 30 minutes. 
     *  
     * @param epochDate - the time in milliseconds since Jan , 1, 1970
     * @return - Date format in String
     */    
    public static native String getShortTimeRounded(String epochDate) /*-{
        return $wnd.spiffyui.getShortTimeRounded(parseInt(epochDate));
    }-*/;
    
    /**
     * Gets the hours from the specified short time string
     *  
     * @param time - the time 11:30pm
     * @return - the hours
     */    
    public static native int getHours(String time) /*-{ 
        return $wnd.spiffyui.getDateFromShortTime(time).getHours();
    }-*/;
    
    /**
     * Gets the minutes from the specified short time string
     *  
     * @param time - the time 11:30pm
     * @return - the hours
     */    
    public static native int getMinutes(String time) /*-{ 
        return $wnd.spiffyui.getDateFromShortTime(time).getMinutes();
    }-*/;
    
  
    /**
     * Convert Date to UTC Epoch format
     * @param dateString - the String to test
     * @return - Time in epoch format
     */    
    public static native String getEpoch(String dateString) /*-{
        return $wnd.spiffyui.getEpoch(dateString);
    }-*/;

    /**
     * Gets the short date format string in the current locale.  In en-US 
     * this is M/d/yyyy in fr_FR it is dd/MM/yyyy 
     *
     * @return - The date format
     */    
    public static native String getShortDateFormat() /*-{
        return $wnd.Date.CultureInfo.formatPatterns.shortDate;
    }-*/;

    /**
     * Gets the long date format string in the current locale.  In en-US 
     * this is dddd, MMMM dd, yyyy in fr_FR it is dddd d MMMM yyyy 
     *
     * @return - The date format
     */    
    public static native String getLongDateFormat() /*-{
        return $wnd.Date.CultureInfo.formatPatterns.longDate;
    }-*/;


    /**
     * Gets the month day format string in the current locale.  In en-US 
     * this is MMMM dd in fr_FR it is d MMMM
     *
     * @return - The date format
     */    
    public static native String getMonthDayFormat() /*-{
        return $wnd.Date.CultureInfo.formatPatterns.monthDay;
    }-*/;

    
    /**
     * Gets the short time format string in the current locale.  In en-US 
     * this is h:mm tt in fr_FR it is HH:mm 
     *
     * @return - The time format
     */    
    public static native String getShortTimeFormat() /*-{
        return $wnd.Date.CultureInfo.formatPatterns.longTime;
    }-*/;

    /**
     * Gets the user's Locale 
     *
     * @return - The Client Locale
     */    
    public static native String getLocale() /*-{
        if ($wnd.navigator.language)
            return $wnd.navigator.language;
        else if($wnd.navigator.browserLanguage)
            return $wnd.navigator.browserLanguage;
    }-*/;
    
    /**
     * Convert the specified String to a date object using the short 
     * format of the current locale
     * @param dateString - the String to convert
     * @return - The Date object
     */
    public static Date parseShortDate(String dateString)
    {
        return DateTimeFormat.getFormat(getShortDateFormat()).parseStrict(dateString);
    }
    
    /**
     * Determines if the browser's current locale uses 24 hour time
     * 
     * @return True if the locale uses 24 time and false otherwise
     */
    public static native boolean is24Time()  /*-{
        return $wnd.Date.CultureInfo.formatPatterns.shortTime.indexOf("tt") === -1;
    }-*/;
    
    
    /**
     * Gets the full date time format string in the current locale.  In en-US 
     * this is dddd, MMMM dd, yyyy h:mm:ss tt
     *
     * @return - The time format
     */    
    public static native String getFullDateTimeFormat() /*-{
        return $wnd.Date.CultureInfo.formatPatterns.fullDateTime;
    }-*/;
    
    /**
     * Convert java.util.Date to Full Date Time format in the current locale
     * @param date - the Date object to convert
     * @return - Date Time format in String
     */    
    public static String getFullDateTime(Date date)
    {
        return getFullDateTime(date.getTime() + "");
    }
    
    /**
     * Convert UTC epoch format to Date Time format in the current locale 
     *  
     * @param epochDate - the time in milliseconds since Jan , 1, 1970
     * @return - Date Time format in String
     */    
    public static native String getFullDateTime(String epochDate) /*-{
        return $wnd.spiffyui.getDateTime(parseInt(epochDate));
    }-*/;
    
    
    /**
     * Converts a java.util.Date to a concatenation of the Short Date and Short Time format 
     * in the current locale
     * @param date - the Date object to convert
     * @return - Short Date and Short Time format in String
     */
    public static String getShortDateTime(Date date) 
    {
        return getDate(date) + " " + getShortTime(date);
    }

    /**
     * Add to a specified date
     * @param date - a Date to start
     * @param amt - the amount to add
     * @param unit - the unit to add.  Can be WEEK, MONTH, YEAR, HOUR, MINUTE, SECOND or defaults to DAY
     * @return the new date
     */
    public static Date dateAdd(Date date, int amt, String unit)
    {
        String epochDate = dateAdd(String.valueOf(date.getTime()), amt, unit);
        return new Date(Long.parseLong(epochDate));
    }
    /**
     * Add to a specified epoch date
     * @param epochDate - the time in milliseconds since Jan , 1, 1970
     * @param amt - the amount to add
     * @param unit - the unit to add.  Can be WEEK, MONTH, YEAR, HOUR, MINUTE, SECOND or defaults to DAY
     * @return the new date as an epoch 
     */    
    public static native String dateAdd(String epochDate, int amt, String unit) /*-{
        return String($wnd.spiffyui.dateAdd(parseInt(epochDate), amt, unit));
    }-*/;

    /**
     * Convert UTC epoch format to Month Day format in the current locale 
     *  
     * @param epochDate - the time in milliseconds since Jan , 1, 1970
     * @param abbrev - if true, the abbreviate the month
     * @return - Date format in String
     */    
    public static native String getMonthDay(String epochDate, boolean abbrev) /*-{
        return $wnd.spiffyui.getMonthDay(parseInt(epochDate), abbrev);
    }-*/;
    
}
