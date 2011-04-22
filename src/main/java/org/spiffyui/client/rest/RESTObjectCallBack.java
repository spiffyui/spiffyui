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
 * @see RESTCallBack
 * @see RESTility
 */
public interface RESTObjectCallBack<T>
{
    /**
     * Called when the REST call succeeds
     * 
     * @param o - The marshalled object from the REST call
     */
    public void success(T o);

    /**
     * Called when there is an error
     * 
     * @param message the error message
     */
    public void error(String message);
    
    /**
     * Called when the REST endpoint return successfully with 
     * an error message in the payload.
     * @param e the error
     */
    public void error(RESTException e);
}
