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

import com.google.gwt.json.client.JSONValue;

/**
 * This interface is used when calling REST APIs in conjunction with RESTility.
 */
public interface RESTCallback
{
    
    /**
     * Called when the REST call completes successfully.
     * 
     * @param val    The JSON value from the REST call
     */
    public void onSuccess(JSONValue val);
    
    /**
     * Called if there is an error calling the REST API
     * 
     * @param statusCode the HTTP status code with the error
     * @param errorResponse
     *                   the error message response from the REST endpoint
     */
    public void onError(int statusCode, String errorResponse);
    
    /**
     * Called if the REST endpoint returns a valid response with an error message
     * 
     * @param e the RESTException
     */
    public void onError(RESTException e);
    
}
