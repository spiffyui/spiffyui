/*******************************************************************************
 * 
 * Copyright 2012 Spiffy UI Team   
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

import com.google.gwt.http.client.Response;

import org.spiffyui.client.rest.RESTCallback;
import org.spiffyui.client.rest.RESTException;

/**
 * <p>
 * This interface allows clients to provide extra information about OAuth authentications.
 * </p>
 * 
 */
public interface RESTOAuthProvider 
{
    /**
     * <p>
     * Provide the OAuth server URL for this request
     * </p>
     * 
     * @param callback  the callback for the original REST call
     * @param tokenServerUrl
     *                  the URL for the authentication server
     * @param response  the server response that came with this 401
     * @param exception the RESTException representation of the JSON response from the server if available
     * 
     * @return the auth server URL
     */
    String getAuthServerUrl(RESTCallback callback, String tokenServerUrl, Response response, RESTException exception);

    /**
     * Get the optional client ID for this OAuth request.  If the request doesn't
     * use a client ID then this method should return null.
     * 
     * @return the client ID
     */
    String getClientId();

    /**
     * Get the optional scope for this OAuth request.  If the request doesn't
     * use a scope then this method should return null.
     * 
     * @return the scope
     */
    String getScope();
}
