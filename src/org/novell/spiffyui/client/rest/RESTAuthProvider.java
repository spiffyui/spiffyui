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
package com.novell.spiffyui.client.rest;

import com.novell.spiffyui.client.rest.util.RESTAuthConstants;

/**
 * This interface defines the necessary steps to perform a login for a REST service 
 * that follows the NCAC standard for tokenized identity 
 */
public interface RESTAuthProvider extends RESTAuthConstants
{
    /**
     * Show the login dialog.
     *
     * @param callback the callback for the original REST call
     * @param tokenServerUrl
     *                 the URL for the authentication server
     * @param code the error code
     */
    public void showLogin(RESTCallback callback, String tokenServerUrl, String code);
    
    /**
     * Once the login is completed we need to perform the original REST request
     * with the new user token.
     * 
     * @param callBackKey
     *               the callback object of the original REST call
     */
    public void finishRESTCall(Object callBackKey);
    
    /**
     * Logout the current user.  This clears all local variables, removes the 
     * session cookie, and issues the logout request to the server.
     * 
     * @param callback the REST callback to indicate the call is completed
     */
    public void logout(final RESTObjectCallBack<String> callback);
    
    /**
     * Performs login for the specified user credentials.
     * 
     * @param username the username
     * @param password the password
     * @param authUrl  the URL of the authentication server
     * @param callback the REST callback for the original REST call
     */
    public void login(final String username, final String password, final String authUrl,
                      final RESTObjectCallBack<String> callback);

    /**
     * Gets the servlet context from the current URL.  The context is assumed
     * to be the string after the first single forward slash and before the next
     * forward slash.
     *
     * @return the current server servlet context
     */
    public String getServletContext();
}
