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
package org.spiffyui.client.rest;

import org.spiffyui.client.rest.util.RESTAuthConstants;

/**
 * <p> 
 * This interface defines the necessary steps to perform a login for a REST service 
 * that follows the Novell architecture council standard for tokenized identity. 
 * </p> 
 */
public interface RESTAuthProvider extends RESTAuthConstants
{
    /**
     * <p> 
     * Show the login dialog. 
     * </p> 
     *  
     * <p> 
     * This method is called when a REST call results in a 401 which requires login. 
     * The default implementation of this method shows a login dialog, but it could 
     * redirect to another page or simply show an error message. 
     * </p> 
     *
     * @param callback the callback for the original REST call
     * @param tokenServerUrl
     *                 the URL for the authentication server
     * @param code the error code
     */
    public void showLogin(RESTCallback callback, String tokenServerUrl, String code);
    
    /**
     * <p> 
     * Once the login is completed we need to perform the original REST request
     * with the new user token. 
     * </p> 
     *  
     * <p> 
     * This gives the framework a chance to repeat the request with the new token 
     * and have it complete successfully.  This method is called after all of the 
     * login exchange is completed. 
     * </p> 
     * 
     * @param callBackKey
     *               the callback object of the original REST call
     */
    public void finishRESTCall(Object callBackKey);
    
    /**
     * <p> 
     * Logout the current user. 
     * </p> 
     *  
     * <p> 
     * This method clears all local variables, removes the session cookie, and issues 
     * the logout request to the server.  The logout request to the server is typically 
     * a REST request made to the same URL as the login request but using an HTTP DELETE. 
     * </p> 
     * 
     * @param callback the REST callback to indicate the call is completed
     */
    public void logout(final RESTObjectCallBack<String> callback);
    
    /**
     * <p> 
     * Perform login for the specified user credentials. 
     * </p> 
     *  
     * <p> 
     * This method makes a REST request to the authentication server (through the authentication) 
     * proxy and gets the token based on the specified credentials. 
     * </p> 
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
