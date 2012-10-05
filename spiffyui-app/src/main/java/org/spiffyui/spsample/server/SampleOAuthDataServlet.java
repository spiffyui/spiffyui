/*******************************************************************************
 *
 * Copyright 2012 Spiffy UI Team   
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
package org.spiffyui.spsample.server;

import java.io.IOException;
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
public class SampleOAuthDataServlet extends HttpServlet
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
        System.out.println("authToken: " + authToken);
        if (authToken == null || authToken.length() <= 0) {
            response.setHeader("WWW-Authenticate", "Bearer");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            buff.append(generateFault("Sender", "NoAuthHeader", ""));
        } else {
            String token = authToken;
            Date date = new Date();
            buff.append("{\"message\":\""  + "you received Spiffy's secret slogan - Fast, Flexible, Friendly and You-Can-REST medicine!\",").
                 append("\"token\":\""  + token + "\",").
                 append("\"date\":\"" + date.getTime() + "\"}");
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
