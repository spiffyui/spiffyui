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
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

/**
 * A set of static JavaScript utilities.
 */
public final class JSONUtil
{

    /**
     * Get a string from the JSON object or null if it doesn't exist or
     * isn't a string
     * 
     * @param obj    the object with the value
     * @param key    the key for the object
     * 
     * @return the value or null it could not be decoded
     */
    public static String getStringValue(JSONObject obj, String key) 
    {
        if (!obj.containsKey(key)) {
            return null;
        }

        JSONValue v = obj.get(key);
        if (v != null) {
            JSONString s = v.isString();
            if (s != null) {
                return s.stringValue();
            }
        }
        
        return null;
    }
    
    /**
     * Get a string from the JSON object or null if it doesn't exist or
     * isn't a string
     * 
     * @param obj    the object with the value
     * @param key    the key for the object
     * 
     * @return the value or null it could not be decoded
     */
    public static String getStringValueIgnoreCase(JSONObject obj, String key) 
    {
        JSONValue v = obj.get(key);
        
        if (v == null) {
            key = key.toLowerCase();
            for (String k : obj.keySet()) {
                if (key.equals(k.toLowerCase())) {
                    v = obj.get(k);
                    break;
                }
            }
        }
        
        if (v != null) {
            JSONString s = v.isString();
            if (s != null) {
                return s.stringValue();
            }
        }
        
        return null;
    }
    
    /**
     * Get a boolean from the JSON object false if it doesn't exist or
     * isn't a boolean
     * 
     * @param obj    the object with the value
     * @param key    the key for the object
     * 
     * @return the value or false it could not be decoded
     */
    public static boolean getBooleanValue(JSONObject obj, String key) 
    {
        if (!obj.containsKey(key)) {
            return false;
        }

        JSONValue v = obj.get(key);
        if (v != null) {
            JSONBoolean b = v.isBoolean();
            if (b != null) {
                return b.booleanValue();
            } else {
                JSONString s = v.isString();
                if (s != null) {
                    return Boolean.parseBoolean(s.stringValue());
                }
            }
        }
        
        return false;
    }
    
    /**
     * Get a JSONArray from the JSON object or null if it doesn't exist or
     * isn't a JSONArray
     * 
     * @param obj    the object with the value
     * @param key    the key for the object
     * 
     * @return the value or null it could not be decoded
     */
    public static JSONArray getJSONArray(JSONObject obj, String key) 
    {
        JSONValue v = obj.get(key);
        if (v != null) {
            JSONArray a = v.isArray();
            if (a != null) {
                return a;
            }
        }
        
        return null;
    }
    /**
     * Get a int from the JSON object or -1 if it doesn't exist or
     * isn't an int.  This will handle JSON numbers like this:
     * 
     *      "val": 5
     * 
     * It will also handle numbers in strings like:
     * 
     *      "val": "5"
     * 
     * @param obj    the object with the value
     * @param key    the key for the object
     * 
     * @return the value or -1 it could not be decoded
     */
    public static int getIntValue(JSONObject obj, String key) 
    {
        if (!obj.containsKey(key)) {
            return -1;
        }

        try {
            JSONValue v = obj.get(key);
            if (v != null) {
                JSONNumber n = v.isNumber();
                if (n != null) {
                    return (int) n.doubleValue();
                } else {
                    /*
                     * If this isn't a number, then it might be a string
                     * like "5" so we try to parse it as a number.
                     */
                    return Integer.parseInt(getStringValue(obj, key));
                }
            }
        } catch (Exception e) {
            JSUtil.println(e.getMessage());
        }
        
        return -1;
    }
    
    /**
     * Get a JSONObject from the JSON object or null if it doesn't exist or
     * isn't a JSONObject
     * 
     * @param obj    the object with the value
     * @param key    the key for the object
     * 
     * @return the value or null it could not be decoded
     */
    public static JSONObject getJSONObject(JSONObject obj, String key) 
    {
        JSONValue v = obj.get(key);
        if (v != null) {
            JSONObject a = v.isObject();
            if (a != null) {
                return a;
            }
        }
        
        return null;
    }
    
    /**
     * Get a java.util.Date from the JSON object as an epoch time in milliseconds
     * or return null if it doesn't exist or
     * cannot be converted from epoch to Date
     * 
     * @param obj    the object with the value
     * @param key    the key for the object
     * 
     * @return the value or null it could not be decoded/converted
     */
    public static Date getDateValue(JSONObject obj, String key) 
    {
        JSONValue v = obj.get(key);
        if (v != null) {
            JSONNumber n = v.isNumber();
            if (n != null) {
                return new Date(Double.valueOf(n.doubleValue()).longValue());
            }
        }
        
        return null;
    }
    
    /**
     * Get a java.util.Date from the JSON object in medium date time format
     * or return null if it doesn't exist or to a Date
     * 
     * @param obj    the object with the value
     * @param key    the key for the object
     * 
     * @return the value or null it could not be decoded/converted
     */
    public static Date getDateValueFromMediumDateTime(JSONObject obj, String key) 
    {
        JSONValue v = obj.get(key);
        if (v != null) {
            JSONString s = v.isString();
            if (s != null) {
                try {
                    return DateTimeFormat.getMediumDateTimeFormat().parse(s.stringValue());
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return null;
    }
    
    
}
