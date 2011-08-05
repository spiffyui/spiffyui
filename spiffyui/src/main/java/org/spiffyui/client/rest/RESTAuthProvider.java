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
    void showLogin(RESTCallback callback, String tokenServerUrl, String code);
    
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
    void finishRESTCall(Object callBackKey);
    
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
    void logout(final RESTObjectCallBack<String> callback);
    
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
    void login(final String username, final String password, final String authUrl,
                      final RESTObjectCallBack<String> callback);

    /**
     * Gets the servlet context from the current URL.  The context is assumed
     * to be the string after the first single forward slash and before the next
     * forward slash.
     *
     * @return the current server servlet context
     */
    String getServletContext();
}
