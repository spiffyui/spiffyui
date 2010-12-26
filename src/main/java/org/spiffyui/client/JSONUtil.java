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
import java.util.HashMap;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

import org.spiffyui.client.rest.RESTException;

/**
 * A set of static JavaScript utilities for handling JSON data structures.
 */
public final class JSONUtil
{
    
    /**
     * Making sure this class can't be instantiated.
     */
    private JSONUtil()
    {
    }
    
    /**
     * Get a string from the JSON object or null if it doesn't exist or
     * isn't a string
     * 
     * @param obj    the object with the value
     * @param key    the key for the object
     * 
     * @return the value or null if it could not be decoded
     */
    public static String getStringValue(JSONObject obj, String key) 
    {
        return getStringValue(obj, key, null);
    }

    /**
     * Get a string from the JSON object or defaultValue if it doesn't exist or
     * isn't a string
     * 
     * @param obj    the object with the value
     * @param key    the key for the object 
     * @param defaultValue  the default value to return if the key could not be found 
     * 
     * @return the value or the defaultValue if it could not be decoded
     */
    public static String getStringValue(JSONObject obj, String key, String defaultValue) 
    {
        if (!obj.containsKey(key)) {
            return defaultValue;
        }

        JSONValue v = obj.get(key);
        if (v != null) {
            JSONString s = v.isString();
            if (s != null) {
                return s.stringValue();
            }
        }
        
        return defaultValue;
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
        return getStringValueIgnoreCase(obj, key, null);
    }
    
    /**
     * Get a string from the JSON object or defaultValue if it doesn't exist or
     * isn't a string
     * 
     * @param obj    the object with the value
     * @param key    the key for the object 
     * @param defaultValue  the default value to return if the key could not be found 
     * 
     * @return the value or the defaultValue if it could not be decoded
     */
    public static String getStringValueIgnoreCase(JSONObject obj, String key, String defaultValue) 
    {
        JSONValue v = obj.get(key);
        
        if (v == null) {
            String lowerKey = key.toLowerCase();
            for (String k : obj.keySet()) {
                if (lowerKey.equals(k.toLowerCase())) {
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
        
        return defaultValue;
    }
    
    /**
     * Get a boolean from the JSON object false if it doesn't exist or
     * isn't a boolean
     * 
     * @param obj    the object with the value
     * @param key    the key for the object 
      
     * @return the value or false if it could not be decoded
     */
    public static boolean getBooleanValue(JSONObject obj, String key) 
    {
        return getBooleanValue(obj, key, false);
    }
    
    /**
     * Get a boolean from the JSON object or defaultValue if it doesn't exist or
     * isn't a boolean
     * 
     * @param obj    the object with the value
     * @param key    the key for the object 
     * @param defaultValue  the default value if the key can not be found  
     * 
     * @return the value or false it could not be decoded
     */
    public static boolean getBooleanValue(JSONObject obj, String key, boolean defaultValue) 
    {
        if (!obj.containsKey(key)) {
            return defaultValue;
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
        
        return defaultValue;
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
     * Get an int from the JSON object or -1 if it doesn't exist or
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
     * @return the value or -1 if it could not be decoded
     */
    public static int getIntValue(JSONObject obj, String key) 
    {
        return getIntValue(obj, key, -1);
    }
    
    /**
     * Get an int from the JSON object or the defaultValue if it doesn't exist or
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
     * @param defaultValue the default value if the specified key isn't found. 
     * 
     * @return the value or the defaultValue if it could not be decoded
     */
    public static int getIntValue(JSONObject obj, String key, int defaultValue) 
    {
        if (!obj.containsKey(key)) {
            return defaultValue;
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
        
        return defaultValue;
    }

/**
     * Get a double from the JSON object or 0 if it doesn't exist or
     * isn't a double.  This will handle JSON numbers like this:
     *
     *      "val": 0.5 or "val": -0.5 or "val": +0.5
     *
     * It will also handle numbers in strings like:
     *
     *      "val": "0.5" or "val": "-0.5"  or "val": "+0.5"
     *
     * @param obj    the object with the value
     * @param key    the key for the object
     *
     * @return the value or 0 if it could not be decoded
     */
    public static double getDoubleValue(JSONObject obj, String key)
    {
        return getDoubleValue(obj, key, 0);
    }

    /**
     * Get a double from the JSON object or the defaultValue if it doesn't exist or
     * isn't a double.  This will handle JSON numbers like this:
     *
     *      "val": 0.5 or "val": -0.5 or "val": +0.5
     *
     * It will also handle numbers in strings like:
     *
     *      "val": "0.5" or "val": "-0.5" or "val": "+0.5"
     *
     * @param obj    the object with the value
     * @param key    the key for the object
     * @param defaultValue the default value if the specified key isn't found.
     *
     * @return the value or the defaultValue if it could not be decoded
     */
    public static double getDoubleValue(JSONObject obj, String key, double defaultValue)
    {
        if (!obj.containsKey(key)) {
            return defaultValue;
        }

        try {
            JSONValue v = obj.get(key);
            if (v != null) {
                JSONNumber n = v.isNumber();
                if (n != null) {
                    return n.doubleValue();
                } else {
                    /*
                     * If this isn't a number, then it might be a string
                     * like "5" so we try to parse it as a number.
                     */
                    return Double.parseDouble(getStringValue(obj, key));
                }
            }
        } catch (Exception e) {
            JSUtil.println(e.getMessage());
        }

        return defaultValue;
    }

    
    /**
     * Get a long from the JSON object or -1 if it doesn't exist or
     * isn't a long.  This will handle JSON numbers like this:
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
     * @return the value or -1 if it could not be decoded
     */
    public static long getLongValue(JSONObject obj, String key) 
    {
        return getLongValue(obj, key, -1L);
    }
    
    /**
     * Get a long from the JSON object or the defaultValue if it doesn't exist or
     * isn't a long.  This will handle JSON numbers like this:
     * 
     *      "val": 5
     * 
     * It will also handle numbers in strings like:
     * 
     *      "val": "5"
     * 
     * @param obj    the object with the value
     * @param key    the key for the object 
     * @param defaultValue the default value if the specified key isn't found. 
     * 
     * @return the value or the defaultValue if it could not be decoded
     */
    public static long getLongValue(JSONObject obj, String key, long defaultValue) 
    {
        if (!obj.containsKey(key)) {
            return defaultValue;
        }

        try {
            JSONValue v = obj.get(key);
            if (v != null) {
                JSONNumber n = v.isNumber();
                if (n != null) {
                    return (long) n.doubleValue();
                } else {
                    /*
                     * If this isn't a number, then it might be a string
                     * like "5" so we try to parse it as a number.
                     */
                    return Long.parseLong(getStringValue(obj, key));
                }
            }
        } catch (Exception e) {
            JSUtil.println(e.getMessage());
        }
        
        return defaultValue;
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
            } else {
                String s = getStringValue(obj, key);
                if (s != null) {
                    return new Date(Long.parseLong(s));
                }
            }
        }
        
        return null;
    }
    
    /**
     * Get the RESTException containing the information in the specified JSON or null if the
     * JSON object does not correspond to the NCAC exception format.
     * 
     * @param val        the JSON value containing the exception
     * @param statusCode the status code of the response that generated this data
     * @param url        the URL that was called to get this JSON
     * 
     * @return the RESTException if this JSON represented an NCAC fault or null if it wasn't an NCAC fault
     */
    public static RESTException getRESTException(JSONValue val, int statusCode, String url)
    {
        if (val.isObject() != null &&
            val.isObject().containsKey("Fault")) {

            JSONObject fault = val.isObject().get("Fault").isObject();
            String code = fault.get("Code").isObject().get("Value").isString().stringValue();
            String subcode = null;
            if (fault.get("Code").isObject().get("Subcode") != null) {
                subcode = fault.get("Code").isObject().get("Subcode").isObject().get("Value").isString().stringValue();
            }

            String reason = null;
            if (fault.get("Reason") != null && fault.get("Reason").isObject() != null &&
                fault.get("Reason").isObject().get("Text") != null) {
                reason = fault.get("Reason").isObject().get("Text").isString().stringValue();
            }

            HashMap<String, String> detailMap = new HashMap<String, String>();
            if (fault.get("Detail") != null) {
                JSONObject details = fault.get("Detail").isObject();
                for (String key : details.keySet()) {
                    detailMap.put(key, details.get(key).isString().stringValue());
                }
            }
            
            
            return new RESTException(code, subcode, reason,
                                     detailMap, statusCode, url);
        } else {
            return null;
        }
    }
}
