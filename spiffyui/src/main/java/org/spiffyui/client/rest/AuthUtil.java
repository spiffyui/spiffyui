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
package org.spiffyui.client.rest;

import org.spiffyui.client.MessageUtil;
import org.spiffyui.client.i18n.SpiffyUIStrings;
import org.spiffyui.client.login.LoginPanel;
import org.spiffyui.client.login.LoginStringHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;

/**
 * <p> 
 * This is the default class for handling authentication during REST calls. 
 * </p> 
 *  
 * <p> 
 * The default authentication handler follows the Novell standards for REST 
 * authentication.  This class implements that handler.  You can override this 
 * class and create your own authentication handler to satisfy any authentication 
 * scheme your application may require. 
 * </p> 
 */
public class AuthUtil implements org.spiffyui.client.rest.v2.RESTAuthProvider
{
    private static final SpiffyUIStrings STRINGS = (SpiffyUIStrings) GWT.create(SpiffyUIStrings.class);


    /**
     * The string helper for providing strings to the login dialog
     */
    protected static final LoginStringHelper HELPER = new LoginStringHelper();


    /**
     * Create a new AuthUtil
     */
    public AuthUtil()
    {
        
    }
    
    /**
     * <p>
     * Show the login dialog.
     * </p><p>
     * <p>
     * This method is called when a REST call results in a 401 which requires login.
     * The default implementation of this method shows a login dialog, but it could
     * redirect to another page or simply show an error message.
     * </p>
     * 
     * @param callback  the callback for the original REST call
     * @param tokenServerUrl
     *                  the URL for the authentication server
     * @param response  the server response that came with this 401
     * @param exception the RESTException representation of the JSON response from the server if available
     */
    public void showLogin(RESTCallback callback, String tokenServerUrl, Response response, RESTException exception)
    {
        /*
         We don't need to implement this method here because we don't use any of the extend objects,
         but we want to implement this interface since this is a popular class to subclass and we
         want to give those child classes access to the extended objects.
         */
        String code = null;
        if (exception != null) {
            code = exception.getSubcode();
        }
        
        if (tokenServerUrl == null) {
            throw new IllegalArgumentException(STRINGS.invalidAuthHeader(response.getHeader("WWW-Authenticate")));
        }
        
        showLogin(callback, tokenServerUrl, code);
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
                    handleLogoutError(callback, e);
                }
            });
    }
    
    private void handleLogoutError(RESTObjectCallBack<String> callback, RESTException e)
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
        if (RESTility.getTokenServerLogoutUrl() != null) {
            credentials.put(AUTH_LOGOUT_URL_TOKEN, new JSONString(RESTility.getTokenServerLogoutUrl()));
        } else {
            credentials.put(AUTH_LOGOUT_URL_TOKEN, new JSONString(authUrl));
        }

        RESTility.callREST(getServletContext() + "/auth", credentials.toString(), RESTility.POST,
                           new RESTCallback() {
                @Override
                public void onSuccess(JSONValue val)
                {
                    doLogin(callback, val, username, authUrl);
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
    
    private void doLogin(final RESTObjectCallBack<String> callback, JSONValue val, String username, String authUrl)
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
        RESTility.fireLoginSuccess();
        callback.success(o.get("Token").isString().stringValue());
    }

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

