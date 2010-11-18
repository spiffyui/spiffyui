/*
 * Copyright (c) 2010 Unpublished Work of Novell, Inc. All Rights Reserved.
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
