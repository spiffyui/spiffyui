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
