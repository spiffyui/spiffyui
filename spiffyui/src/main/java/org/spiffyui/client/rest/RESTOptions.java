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

import java.util.Map;

import com.google.gwt.json.client.JSONValue;

/**
 * <p>
 * This Java Bean provides options for making REST calls.
 * </p> 
 *  
 * <p> 
 * All of the <code>set</code> methods in this bean support chaining so 
 * it is possible to call <code>new RESTOptions().setURL("/api/foo").setCallback(this)</code>. 
 * </p> 
 */
public final class RESTOptions
{
    /*
     the properly encoded REST url to call
     */
    private String m_url;
    
    /*
     the data to pass to the URL
     */
    private JSONValue m_data;
    
    /*
     the HTTP method, defaults to GET
     */
    private RESTility.HTTPMethod m_method = RESTility.GET;
    
    /*
      the callback object for handling the request results
     */
    private RESTCallback m_callback;
    
    /*
     true if this is a request to login and false otherwise
     */
    private boolean m_isLoginRequest = false;
    
    /*
     true if this request should repeat after a login request
     if this request returns a 401
     */
    private boolean m_shouldReplay = true;
    
    /*
     the option etag for this request;
     */
    private String m_etag;
    
    /*
       a map containing the headers to the HTTP request.  Any item
       in this map will override the default headers.
     */
    private Map<String, String> m_headers;
    
    /**
     * Get the URL for this REST request.
     * 
     * @return the full URL for this REST request including all parameters
     */
    public String getURL()
    {
        return m_url;
    }
    
    /**
     * Set the URL for this REST request.
     * 
     * @param url    the full URL for the request including all parameters
     * 
     * @return the RESTOptions bean for method chaining
     */
    public RESTOptions setURL(String url)
    {
        m_url = url;
        return this;
    }
    
    /**
     * <p>
     * Get the JSON data sent to the server during the REST request.
     * </p>
     * 
     * <p>
     * JSON data can only be passed for POST and PUT requests and will be ignored for all
     * other request methods.
     * </p>
     * 
     * @return the JSON data for this request
     */
    public JSONValue getData()
    {
        return m_data;
    }
    
    /**
     * <p>
     * Get the JSON data sent to the server during the REST request as a string.
     * </p>
     * 
     * @return the JSON data for this request as a string
     */
    public String getDataString()
    {
        if (m_data != null) {
            return m_data.toString();
        } else {
            return "";
        }
    }
    
    /**
     * <p>
     * Set the data for this REST request.
     * </p> 
     *  
     * <p>
     * JSON data can only be passed for POST and PUT requests and will be ignored for all
     * other request methods.
     * </p> 
     * 
     * @param data   the data to send to the server with this request
     * 
     * @return the RESTOptions bean for method chaining
     */
    public RESTOptions setData(JSONValue data)
    {
        m_data = data;
        return this;
    }
    
    /**
     * Get the method of this REST request.
     * 
     * @return the REST request method
     */
    public RESTility.HTTPMethod getMethod()
    {
        return m_method;
    }
    
    /**
     * Set the REST request method.
     * 
     * @param method the HTTP method for this REST request
     * 
     * @return the RESTOptions bean for method chaining
     */
    public RESTOptions setMethod(RESTility.HTTPMethod method)
    {
        m_method = method;
        return this;
    }
    
    /**
     * Get the callback for when this request is completed.
     * 
     * @return the callback for this REST request
     */
    public RESTCallback getCallback()
    {
        return m_callback;
    }
    
    /**
     * Set the callback for this REST request.
     * 
     * @param callback the REST request callback
     * 
     * @return the RESTOptions bean for method chaining
     */
    public RESTOptions setCallback(RESTCallback callback)
    {
        m_callback = callback;
        return this;
    }
    
    /**
     * <p>
     * Set if this request is a login request or not.
     * </p>
     * 
     * <p>
     * When the server returns a 401 as the response to a REST request and the application
     * is following the Spiffy UI authentication recommendations then RESTility will handle
     * the authentication and part of that authentication includes making further REST requests
     * for the login.  Those requests are handled specially since they are part of the 
     * authentication exchange.
     * </p>
     * 
     * <p>
     * By default this value is false.  This value should only be true if this request is 
     * part of the login for a custom authentication handler.
     * </p>
     * 
     * @return true if this is a login request and false otherwise
     */
    public boolean isLoginRequest()
    {
        return m_isLoginRequest;
    }
    
    /**
     * Set this REST request as a login request.  The default is false.
     * 
     * @param isLoginRequest
     *               true if this is a login request and false otherwise
     * 
     * @return the RESTOptions bean for method chaining
     */
    public RESTOptions setIsLoginRequest(boolean isLoginRequest)
    {
        m_isLoginRequest = isLoginRequest;
        return this;
    }
    
    /**
     * <p>
     * Set if this REST request should be replayed.
     * </p>
     * 
     * <p>
     * When the client makes a REST request which results in a 401 RESTility must perform
     * authentication and get a token before it can continue with the request.  After the
     * authentication completes successfully RESTility replays the original request and any
     * requests which have come in during the authentication process so they can complete
     * for the calling code transparent of any authentication requirements.
     * </p>
     * 
     * <p>
     * This value controls if the request should replay in that case.  The default is true.
     * </p>
     * 
     * @return the RESTOptions bean for method chaining
     */
    public boolean shouldReplay()
    {
        return m_shouldReplay;
    }
    
    /**
     * <p>
     * Set if this REST request should be replayed.
     * </p>
     * 
     * <p>
     * When the client makes a REST request which results in a 401 RESTility must perform
     * authentication and get a token before it can continue with the request.  After the
     * authentication completes successfully RESTility replays the original request and any
     * requests which have come in during the authentication process so they can complete
     * for the calling code transparent of any authentication requirements.
     * </p>
     * 
     * <p>
     * This value controls if the request should replay in that case.  The default is true.
     * </p>
     * 
     * @param shouldReplay
     *               true if the request should replay and false otherwise
     * 
     * @return the RESTOptions bean for method chaining
     */
    public RESTOptions setShouldReplay(boolean shouldReplay)
    {
        m_shouldReplay = shouldReplay;
        return this;
    }
    
    /**
     * Get the etag for this REST request.
     * 
     * @return the etag for this request
     */
    public String getEtag()
    {
        return m_etag;
    }
    
    /**
     * <p>
     * Set the etag for this REST request.
     * </p>
     * 
     * <p>
     * Etags are a common mechanism for handling concurrency checking and RESTility handles
     * them specially since they are part of the built in Spiffy UI optimistic concurrency
     * mechanism.
     * </p>
     * 
     * <p>
     * The ETag set in this method will override the <code>If-Match</code> set in the headers
     * map if it is present.
     * </p>
     * 
     * <p>
     * For more information see 
     * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.19">HTTP 1.1 header field definitions sections 14.19</a>.
     * </p>
     * 
     * @param etag   the etag for this request
     * 
     * @return the RESTOptions bean for method chaining
     * @see ConcurrentRESTCallback
     */
    public RESTOptions setEtag(String etag)
    {
        m_etag = etag;
        return this;
    }
    
    /**
     * <p>
     * Set the HTTP headers map for this request.
     * </p>
     * 
     * @return the map of HTTP headers
     * @see setEtag
     * @see getEtag
     */
    public Map<String, String> getHeaders()
    {
        return m_headers;
    }
    
    /**
     * <p>
     * Set the HTTP headers for this REST request.
     * </p>
     * 
     * <p>
     * This method gives a client the option to specify defined and custom HTTP headers
     * for the request request.  These headers will override all values except for the
     * following headers:
     * </p>
     * 
     * <ul>
     *     <li><code>Accept-Language</code> - always set to the preferred language from GWT</li>
     *     <li><code>Authorization</code> - set with the authentication token from the
     *     authentication provider</li>
     *     <li><code>TS-URL</code> - this custom token is set to the token server URL from
     *     the authenciation provider.</li>
     *     <li><code>If-Match</code> - set with the value from the ETag</li>
     *     <li><code>Content-Type</code> - always sets the content type to use JSON since
     *     RESTility requires JSON formatted data.
     * </ul>
     * 
     * @param headers the HTTP headers for this REST request
     * 
     * @return the RESTOptions bean for method chaining
     */
    public RESTOptions setHeaders(Map<String, String> headers)
    {
        m_headers = headers;
        return this;
    }
}
