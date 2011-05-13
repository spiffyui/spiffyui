/*******************************************************************************
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
     * Determines if the specified JSONObject contains the specified key.
     * 
     * @param obj    the object containing the key
     * @param key    the key to look for
     * 
     * @return true if this object contains the specified key and false otherwise
     */
    public static boolean containsKeyIgnoreCase(JSONObject obj, String key)
    {
        if (obj != null) {
            String lowerKey = key.toLowerCase();
            for (String k : obj.keySet()) {
                if (lowerKey.equals(k.toLowerCase())) {
                    return true;
                }
            }
        }
        
        return false;
    }

    /**
     * Get the JSONValue from the specified object ignoring the case of the key.
     * 
     * @param obj    the object containing the value
     * @param key    the key of the value
     * 
     * @return the value for the specified key or null if the key can't be found
     */
    public static JSONValue getIgnoreCase(JSONObject obj, String key)
    {
        if (obj != null) {
            String lowerKey = key.toLowerCase();
            for (String k : obj.keySet()) {
                if (lowerKey.equals(k.toLowerCase())) {
                    return obj.get(k);
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
            containsKeyIgnoreCase(val.isObject(), "Fault")) {
            
            JSONObject fault = getIgnoreCase(val.isObject(), "Fault").isObject();
            JSONObject objCode = getIgnoreCase(fault, "Code").isObject();
            
            String code = getStringValueIgnoreCase(objCode, "Value");
            String subcode = null;
            
            if (getIgnoreCase(objCode, "Subcode") != null) {
                subcode = getStringValueIgnoreCase(getIgnoreCase(objCode, "Subcode").isObject(), "Value");
            }

            String reason = null;
            if (getIgnoreCase(fault, "Reason") != null && getIgnoreCase(fault, "Reason").isObject() != null &&
                getIgnoreCase(getIgnoreCase(fault, "Reason").isObject(), "Text") != null) {
                reason = getStringValueIgnoreCase(getIgnoreCase(fault, "Reason").isObject(), "Text");
            }

            HashMap<String, String> detailMap = new HashMap<String, String>();
            if (getIgnoreCase(fault, "Detail") != null) {
                JSONObject details = getIgnoreCase(fault, "Detail").isObject();
                for (String key : details.keySet()) {
                    detailMap.put(key, getIgnoreCase(details, key).isString().stringValue());
                }
            }
            
            
            return new RESTException(code, subcode, reason,
                                     detailMap, statusCode, url);
        } else {
            return null;
        }
    }
}
