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
 * The ConcurrentRESTCallback is a special form of REST callback that can
 * handle an ETag and the If-Match header for concurrency checking.
 */
public interface ConcurrentRESTCallback extends RESTCallback
{
    /**
     * <p>
     * Sets the Etag for this request.
     * </p>
     *
     * <p>
     * When the server returns a value that supports concurrency checking it
     * returns a hash value in the <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.19">etag HTTP header</a>.
     * </p>
     * 
     *<p>
     * This value is sent to the calling code with this special interface.
     * The caller must then pass this value back to RESTility when making
     * the request to save the data.  This value will then be added by
     * RESTility to the <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.24">If-Match</a>
     * HTTP header for the update request. 
     * </p>
     *
     * <p>
     * The server can use these two values to perform optimistic concurrency
     * checking on the update request.
     * </p>
     *
     * <p>
     * This method will only be called right before a call to onSuccess
     * from the parent interface and only if the server includes the ETag
     * header in the response.
     * </p>
     *
     * @param tag    the etag
     */
    public void setETag(String tag);
}

