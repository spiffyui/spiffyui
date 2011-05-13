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
package org.spiffyui.server;

import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;


/**
 * <p>
 * This optional interface makes it possible to provide an authentication server
 * <a href="http://en.wikipedia.org/wiki/Whitelist">whitelist</a>.
 * </p>
 * 
 * <p>
 * The server trusts the client to pass the URL for the authentication server.
 * If the client is compromised (like with an XSS attack) then it could pass the 
 * URL to an untrusted authentication server and get the authentication proxy servlet
 * to forward the user's credentials there.
 * </p>
 * 
 * <p>
 * This is especially dangerous since this serverlet is not governed by the 
 * <a href="http://en.wikipedia.org/wiki/Same_origin_policy">same origin policy</a>
 * like JavaScript running in the browser.  This interface allows you to provide
 * a custom whitelist of trusted authentication servers
 * </p>
 * 
 * <p>
 * If you do not provide this interface the default behavior is to only allow requests
 * to an authentication server hosted on the same web server as the Spiffy UI framework.
 * </p>
 */
public interface AuthURLValidator
{
    /**
     * Validate the specified authentication server WAR against a custom whitelist.
     * 
     * @param request the HTTP request
     * @param uri     the URI to verify
     * 
     * @return true if this request should be allowed and false otherwise
     * @exception MalformedURLException
     *                   if the specified URI is not a valid URI
     */
    public boolean validateURI(HttpServletRequest request, String uri)
        throws MalformedURLException;
}
