/*
 * Copyright (c) 2010, 2011 Unpublished Work of Novell, Inc. All Rights Reserved.
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
package org.spiffyui.client.widgets.multivaluesuggest;

import com.google.gwt.json.client.JSONValue;

import org.spiffyui.client.rest.RESTCallback;
import org.spiffyui.client.rest.RESTException;
import org.spiffyui.client.rest.RESTObjectCallBack;
import org.spiffyui.client.rest.RESTility;

/**
* A SuggestBox that uses the rest package and allows for multiple values and autocomplete.
* It will also allow for browsing, assuming that the REST endpoint supports idxFrom and idxTo url parameters.
* The payload of the REST endpoint should have the following format, but the JSON keys are configurable, by specifying them in the 
* MultivalueSuggestRESTHelper:
* <pre>
* { 
*  "TotalSize" : 133, 
*  "Options" : [
*   {"Value" : "#EFDECD", "DisplayName" : "Almond"},
*   {"Value" : "#CD9575", "DisplayName" : "Antique Brass"},
*   {"Value" : "#FDD9B5", "DisplayName" : "Apricot"}
*  ]
* }
* </pre>
*/
public class MultivalueSuggestBox extends MultivalueSuggestBoxBase
{
    /**
     * Create a new MultivalueSuggestBox
     * 
     * @param restHelper the REST helper for getting remote values
     * @param isMultivalued
     *                   true if this suggest box supports multiple values and false otherwise
     */
    public MultivalueSuggestBox(MultivalueSuggestRESTHelper restHelper, boolean isMultivalued)
    {
        super(restHelper, isMultivalued, true);
    }
    
    /**
     * Create a new MultivalueSuggestBox
     * 
     * @param restHelper the REST helper for getting remote values
     * @param isMultivalued
     *                   true if this suggest box supports multiple values and false otherwise
     * @param placeFormFeedback
     *                   true if this control should place a form feedback and false otherwise
     */
    public MultivalueSuggestBox(MultivalueSuggestHelper restHelper, boolean isMultivalued, boolean placeFormFeedback)
    {
        super(restHelper, isMultivalued, placeFormFeedback);
    }

    /**
     * Retrieve Options (name-value pairs) that are suggested from the REST endpoint
     * @param query - the String search term 
     * @param from - the 0-based begin index int
     * @param to - the end index inclusive int
     * @param callback - the RESTObjectCallBack to handle the response
     */
    protected void queryOptions(final String query, final int from, final int to, final RESTObjectCallBack<OptionResultSet> callback)
    {
        String url = ((MultivalueSuggestRESTHelper) getHelper()).buildUrl(query, from, to);
        RESTility.callREST(url, null, RESTility.GET, new RESTCallback() {

            @Override
            public void onSuccess(JSONValue val) 
            {
                handleQueryResponse(callback, val);
            }

            @Override
            public void onError(int statusCode, String errorResponse) 
            {
                callback.error(errorResponse);
            }

            @Override
            public void onError(RESTException e) 
            {
                callback.error(e);
            }
        });   
    }  
  
}
