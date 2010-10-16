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
import java.text.DateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This is a sample servlet which will return back data for requests from authenticated user,
 * defined as requests carrying authentication token in request header "Authorization"
 * For unauthenticated user, it will return back authentication server url letting caller know how to authenticate.
 */
public class SampleDataServlet extends HttpServlet
{
    private static final long serialVersionUID = -1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("application/json");
        ServletOutputStream out = response.getOutputStream();
        StringBuffer buff = new StringBuffer();
        String origURL = request.getRequestURL().toString(); //this is http://localhost:8081/spsample/authdata
        String servletPath = request.getServletPath().substring(1); //this is /authdata

        String authServerURL = origURL.replace(servletPath, "authserver");

        //check for Authorization request header, if it is not there, then need to login
        String authToken = request.getHeader("Authorization");
        if (authToken == null || authToken.length() <= 0) {
            response.setHeader("WWW-Authenticate", "X-OPAQUE uri=\"" + authServerURL + "\", signOffUri=\"" + "\"");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            buff.append(generateFault("Sender", "NoAuthHeader", ""));
        } else {
            String token = authToken.replace("X-OPAQUE", "").trim(); //remove X-OPAQUE
            Date date = new Date();
            buff.append("{\"message\":\""  + "received a security token.\",").
                 append("\"name\":\""  + token + "\",").
                 append("\"date\":\"" + DateFormat.getDateInstance(DateFormat.SHORT).format(date) + "\"}");
        }
        out.println(buff.toString());
    }

    private String generateFault(String code, String subcode, String reason)
    {
        StringBuffer buff = new StringBuffer();

        buff.append("{\"Fault\":{\"Code\":{\"Value\":\"").
                append(code).
                append("\",\"Subcode\":{\"Value\":\"").
                append(subcode).
                append("\"}},\"Reason\":{\"Text\":\"").
                append(reason).
                append("\"}}}");
        return buff.toString();
    }
}
