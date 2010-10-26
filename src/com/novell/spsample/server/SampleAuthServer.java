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

package com.novell.spsample.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

/**
 * This servlet acts as a stub authentication server, it will return user name parameter as authentication token
 */
public class SampleAuthServer extends HttpServlet
{
    private static final long serialVersionUID = -1L;
    private static final String AUTHORIZATION = "Authorization";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader == null) {
            throw new IllegalArgumentException("No auth header");    
        }
        String[] fields = authHeader.trim().split(" ");
        if (fields.length != 2) {
            throw new IllegalArgumentException("Auth header format wrong");
        }
        String cred;
        try {
            cred = new String(Base64.decodeBase64(fields[1].getBytes("UTF-8")), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
        String[] creds = cred.split(":");
        if (creds.length != 2) {
            throw new IllegalArgumentException("Auth header credential format wrong");
        }

        response.setContentType("application/json");
        ServletOutputStream out = response.getOutputStream();

        StringBuffer buff = new StringBuffer();
        buff.append("{\"Token\":\""  + creds[0] + "\"}");
       
        out.println(buff.toString());
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        doGet(request, response);
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("application/json");
        ServletOutputStream out = response.getOutputStream();

        StringBuffer buff = new StringBuffer();
        buff.append("{\"Status\":\"OK\"}");

        out.println(buff.toString());
    }
    
}
