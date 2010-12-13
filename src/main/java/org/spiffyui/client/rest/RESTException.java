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
package org.spiffyui.client.rest;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This exception represents a successful return from a REST call with an error
 * payload.  This exception parses the payload and returns all the parts of the
 * exception.
 * </p>
 * 
 * <p> 
 * This exception follows the <a href="http://www.w3.org/TR/soap12-part1/#soapfault">SOAP fault format</a>.
 * The basic structure looks like this:
 * </p>
 * 
 * <pre>
 * {
 *     "Fault": {
 *         "Code": {
 *             "Value": "Sender",
 *             "Subcode": {
 *                 "Value": "MessageTimeout" 
 *             } 
 *         },
 *         "Reason": {
 *             "Text": "Sender Timeout" 
 *         },
 *         "Detail": {
 *             "MaxTime": "P5M" 
 *         } 
 *     }
 * }
 * </pre>
 * 
 * <p>
 * The fields in this class map to this JSON structure.
 * </p>
 * 
 */
public class RESTException extends Exception
{
    /**
     * This is a constant error code to indicate the response was not well formed JSON
     */
    public static final String UNPARSABLE_RESPONSE = "UnparsableJSONResponse";
    
    /**
     * This is a constant error code to indicate the JSON returned from the 
     * server was parsable, but still invalid. 
     */
    public static final String INVALID_RESPONSE = "InvalidJSONResponse";

    /**
     * This constant indicates that we were unable to contact the server.  That
     * might be because we are offline or the server connection timed out.
     */
    public static final String NO_SERVER_RESPONSE = "UnableToContactServer";
    
    /**
     * This constant indicates that the server returned a 401, but did not 
     * return the required authentication HTTP header 
     */
    public static final String NO_AUTH_HEADER = "NoAuthHeader";

    /**
     * This constant indicates that the server returned a 401, but the required
     * HTTP header was invalid
     */
    public static final String INVALID_AUTH_HEADER = "InvalidAuthHeader";

    /**
     * This constant indicates that the server returned a 401, but did not 
     * return the required authentication HTTP header 
     */
    public static final String XSS_ERROR = "PotentialXSSError";

    /**
     * This constant indicates that the core server was not able to contact
     * the authentication server.
     */
    public static final String AUTH_SERVER_UNAVAILABLE = "AuthServerUnavailable";

    private static final long serialVersionUID = -1L;
    
    private String m_code;
    private String m_subcode;
    private String m_reason;
    private int m_responseCode;
    private String m_url;
    private Map<String, String> m_details = new HashMap<String, String>();
    
    /**
     * Creates a new RESTException
     * 
     * @param code    the exception code
     * @param subcode the exception subcode
     * @param reason  the exception reason
     * @param details the exception details
     * @param responseCode
     *                the HTTP response code
     * @param url     the URL for this exception
     */
    public RESTException(String code, String subcode, String reason, 
                            Map<String, String> details, int responseCode,
                            String url)
    {
        m_code = code;
        m_subcode = subcode;
        m_reason = reason;
        m_details = details;
        m_responseCode = responseCode;
        m_url = url;
    }
    
    /**
     * The code for the exception
     * 
     * @return gets the return code for this exception - required
     */
    public String getCode()
    {
        return m_code;
    }
    
    /**
     * The subcode for this exception
     * 
     * @return the subcode - optional
     */
    public String getSubcode()
    {
        return m_subcode;
    }
    
    /**
     * The reason for this exception
     * 
     * @return the exception reason - required
     */
    public String getReason()
    {
        return m_reason;
    }
    
    /**
     * The details for this exception
     * 
     * @return a map of the details for this exception
     */
    public Map<String, String> getDetails()
    {
        return m_details;
    }
    
    /**
     * Gets the server response code that came with this error message
     * 
     * @return the HTTP response code
     */
    public int getResponseCode()
    {
        return m_responseCode;
    }
    
    /**
     * Gets the URL that was called when this RESTException was thrown
     * 
     * @return the URL
     */
    public String getUrl()
    {
        return m_url;
    }
    
    /**
     * Gets a generic exception representing invalid JSON
     * 
     * @param url    the URL where this bad JSON came from
     * 
     * @return the exception
     */
    public static RESTException generateInvalidJSONException(String url)
    {
        return new RESTException(RESTException.INVALID_RESPONSE, "", "", 
                                 new HashMap<String, String>(), -1, url);
    }

    @Override
    public String toString()
    {
        return "RESTException: " + m_code + ", " + m_subcode + ", " + m_reason;
    }
}
