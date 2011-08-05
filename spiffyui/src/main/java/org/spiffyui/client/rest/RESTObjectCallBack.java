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

/**
 * <p>
 * This interface is implemented by request callers to get the bean or Object
 * back from a REST call.
 * </p>
 * 
 * <p>
 * RESTObjectCallBack is an optional interface.  You can just implement RESTCallBack.
 * The purpose of the RESTObjectCallBack object is to abstract away the knowledge
 * of JSON, HTTP, or AJAX REST.  A class calling the RESTObjectCallBack only has
 * to deal with well formed Java objects in GWT and doesn't need to parse JSON
 * or handle HTTP errors.
 * </p>
 * 
 * @param <T> - The object type for the callback
 * @see RESTCallback
 * @see RESTility
 */
public interface RESTObjectCallBack<T>
{
    /**
     * <p>
     * Called when your REST call succeeds.
     * </p>
     * 
     * <p>
     * Success is defined as a call that contacted the server and the 
     * server returned a valid JSON response.
     * </p>
     * 
     * @param o      The marshalled object from the REST call
     */
    void success(T o);

    /**
     * <p>
     * Called when there is an unexpected error.
     * </p>
     * 
     * <p>
     * These errors are results like network failures and other reasons the server 
     * can't be contacted.  This method is also called when the server returns a 
     * result that isn't valid JSON data.
     * </p>
     * 
     * @param message The error message from the server
     */
    void error(String message);
    
    /**
     * <p>
     * Called when the REST endpoint return successfully with an error
     * message in the payload.
     * </p><p>
     * <p>
     * This method is called when the server returned valid JSON in the format of
     * a Spiffy UI RESTException
     * </p>
     * 
     * @param e
     *               the RESTException returned from the server
     * 
     * @see RESTException
     */
    void error(RESTException e);
}
