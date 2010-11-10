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

import org.spiffyui.client.MessageUtil;
import org.spiffyui.client.SpiffyUIStrings;
import org.spiffyui.client.login.LoginPanel;
import org.spiffyui.client.login.LoginStringHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;

/**
 * A utility class for login and logout.
 */
public class AuthUtil implements RESTAuthProvider
{
    private static final SpiffyUIStrings STRINGS = (SpiffyUIStrings) GWT.create(SpiffyUIStrings.class);


    /**
     * The string helper for providing strings to the login dialog
     */
    protected static final LoginStringHelper HELPER = new LoginStringHelper();


    /**
     * constructor
     */
    public AuthUtil()
    {
    }
    /**
     * Show the login dialog.
     *
     * @param callback the callback for the original REST call
     * @param tokenServerUrl
     *                 the URL for the authentication server
     * @param code the error code
     */
    public void showLogin(RESTCallback callback, String tokenServerUrl, String code)
    {
        if (RESTility.hasUserLoggedIn()) {
            LoginPanel.showLoginPanel(getStringHelper(), STRINGS.renew(), callback, tokenServerUrl, code, true, RESTility.getUsername());
        } else {
            LoginPanel.showLoginPanel(getStringHelper(), STRINGS.loginTitle(), callback, tokenServerUrl, code, false, null);
        }

    }
    
    /**
     * Provide the LoginStringHelper class for use with the login panel.
     * 
     * @return the LoginStringHelper
     */
    protected LoginStringHelper getStringHelper()
    {
        return HELPER;
    }

    /**
     * Once the login is completed we need to perform the original REST request
     * with the new user token.
     *
     * @param callBackKey
     *               the callback object of the original REST call
     */
    public void finishRESTCall(Object callBackKey)
    {
        RESTility.finishRESTCalls();
    }

    /**
     * Logout the current user.  This clears all local variables, removes the
     * session cookie, and issues the logout request to the server.
     *
     * @param callback the REST callback to indicate the call is completed
     */
    public void logout(final RESTObjectCallBack<String> callback)
    {
        JSONObject credentials = new JSONObject();
        credentials.put(USER_TOKEN, new JSONString(RESTility.getUserToken()));
        credentials.put(AUTH_URL_TOKEN, new JSONString(RESTility.getTokenServerUrl()));
        credentials.put(AUTH_LOGOUT_URL_TOKEN, new JSONString(RESTility.getTokenServerLogoutUrl()));

        RESTility.callREST(getServletContext() + "/auth", credentials.toString(), RESTility.DELETE,
                           new RESTCallback() {
                @Override
                public void onSuccess(JSONValue val)
                {
                    RESTility.doLocalLogout();
                    callback.success("");
                }

                @Override
                public void onError(int statusCode, String errorResponse)
                {
                    RESTility.doLocalLogout();
                    callback.error(errorResponse);
                }

                @Override
                public void onError(RESTException e)
                {
                    RESTility.doLocalLogout();
                    if (e.getSubcode().equals(GONE)) {
                        /*
                         * This means the token has timed out.  That makes the logout
                         * extraneous, but we want to handle it like a normal logout
                         * in the UI.
                         */
                        callback.success("");
                    } else {
                        callback.error(e);
                    }
                }
            });
    }

    /**
     * Performs login for the specified user credentials.
     *
     * @param username the username
     * @param password the password
     * @param authUrl  the URL of the authentication server
     * @param callback the REST callback for the original REST call
     */
    public void login(final String username, final String password, final String authUrl,
                             final RESTObjectCallBack<String> callback)
    {
        JSONObject credentials = new JSONObject();
        credentials.put(USERNAME_TOKEN, new JSONString(username));
        credentials.put(PASSWORD_TOKEN, new JSONString(password));
        credentials.put(AUTH_URL_TOKEN, new JSONString(authUrl));
        credentials.put(AUTH_LOGOUT_URL_TOKEN, new JSONString(RESTility.getTokenServerLogoutUrl()));


        RESTility.callREST(getServletContext() + "/auth", credentials.toString(), RESTility.POST,
                           new RESTCallback() {
                @Override
                public void onSuccess(JSONValue val)
                {

                    RESTility.setTokenServerURL(authUrl);
                    RESTility.setUsername(username);

                    if (val == null) {
                        callback.error(STRINGS.loginDataError(""));
                        MessageUtil.showError(STRINGS.loginDataError(""));
                        return;
                    }

                    JSONObject o = val.isObject();
                    if (o == null) {
                        callback.error(STRINGS.loginDataError(val.toString()));
                        MessageUtil.showError(STRINGS.loginDataError(val.toString()));
                        return;
                    }

                    RESTility.setUserToken(o.get("Token").isString().stringValue());
                    //for (RESTLoginCallBack listener : RESTility.getLoginListeners()) {
                        //listener.onLoginSuccess();
                    //}
                    callback.success(o.get("Token").isString().stringValue());
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
            }, true, null);
    }

    /**
     * Adds a login listener to all REST calls.  This listener will get called every time
     * the user logs in.
     *
     * @param callback the callback
     */
    /*public void addLoginListener(RESTLoginCallBack callback)
    {
        RESTility.addLoginListener(callback);
    }*/

    /**
     * Removes the specified login callback from the list of callbacks when the user is
     * prompted to login.
     *
     * @param callback the callback
     */
    /*public void removeLoginListener(RESTLoginCallBack callback)
    {
        RESTility.removeLoginListener(callback);
    }*/

    /**
     * Gets the servlet context from the current URL.  The context is assumed
     * to be the string after the first single forward slash and before the next
     * forward slash.
     *
     * @return the current server servlet context
     */
    public String getServletContext()
    {
        String url = Window.Location.getHref();
        int index = url.indexOf("/", url.indexOf("//") + 2);
        //Fix array out of bound error in GWT hosted mode by checking if index2 is -1
        int index2 = url.indexOf("/", index + 2);
        if (index2 != -1) {
            String context = url.substring(index + 1, index2);
            if (context.length() > 0) {
                return "/" + context;
            } else {
                return "";
            }
        } else {
            return "";
        }        
    }
}

