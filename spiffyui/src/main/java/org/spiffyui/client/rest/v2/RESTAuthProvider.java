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
package org.spiffyui.client.rest.v2;

import org.spiffyui.client.rest.RESTCallback;
import org.spiffyui.client.rest.RESTException;

import com.google.gwt.http.client.Response;

/**
 * <p>
 * This interface allows clients to perform custom login handling.
 * </p>
 * 
 * <p>
 * The first version of this interface works well for standard Spiffy UI authentication, 
 * but it doesn't give clients access to the entire request which prevent them from 
 * handling more complex authentication schemes like some types of SAML.  This interface
 * gives the client access to the full response from the server when prompting for a 
 * login.
 * </p>
 */
public interface RESTAuthProvider extends org.spiffyui.client.rest.RESTAuthProvider
{
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
    void showLogin(RESTCallback callback, String tokenServerUrl, Response response, RESTException exception);
}
