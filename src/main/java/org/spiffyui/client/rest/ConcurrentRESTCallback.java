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

