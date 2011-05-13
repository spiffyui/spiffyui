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

package org.spiffyui.spsample.server;

import java.net.MalformedURLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;

import org.spiffyui.server.AuthServlet;
import org.spiffyui.server.AuthURLValidator;


/**
 * This is a sample init listener to add our URL validator which allows access to remote
 * servers.
 */
public class SampleInitListener implements ServletContextListener, AuthURLValidator
{
    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
        AuthServlet.setUrlValidator(this);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {
        /*
         Nothing to do here
         */
    }
    
    @Override
    public boolean validateURI(HttpServletRequest request, String uri)
        throws MalformedURLException
    {
        /*
         This is not very secure, but it helps remote authentication
         server testing.
         */
        return true;
    }
}
