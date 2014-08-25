/*******************************************************************************
 *
 * Copyright 2011-2014 Spiffy UI Team   
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
        response.setContentType("application/json");

        ServletOutputStream out = response.getOutputStream();
        StringBuffer buff = new StringBuffer();

        if (authHeader == null) {
            /*
             * This means someone tried to get a token but didn't specify any credentials
             */
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            buff.append(generateFault("Sender", "NoAuthHeader", ""));
            out.println(buff.toString());
            return;
        }

        String[] fields = authHeader.trim().split(" ");
        if (fields.length != 2) {
            /*
             * This means someone specified an invalid authorization header
             */
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            buff.append(generateFault("Sender", "InvalidAuthHeader", 
                                      "The authorization header '" + authHeader + "' is invalid.  " + 
                                      "The format should be BASIC <username:password> base64 encoded."));
            out.println(buff.toString());
            return;
        }
        String cred;
        try {
            cred = new String(Base64.decodeBase64(fields[1].getBytes("UTF-8")), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
        String[] creds = cred.split(":");
        if (creds.length != 2) {
            /*
             * This means someone specified an invalid authorization header
             */
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            buff.append(generateFault("Sender", "InvalidAuthHeader", 
                                      "The authorization header '" + authHeader + "' is invalid.  " + 
                                      "The format should be BASIC <username:password> base64 encoded."));
            out.println(buff.toString());
            return;
        }

        /*
         * At this point we can generate our token.  In our case we just use the username followed
         * by a random number.  The token can be any format.
         */
        String random = Long.toHexString(Double.doubleToLongBits(Math.random()));
        buff.append("{\"Token\":\""  + creds[0] + "-" + random + "\"}");
       
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

    /**
     * Generate a JSON fault following the Spiffy UI exception format recommendations.
     * 
     * @param code    the code to generate
     * @param subcode the subcode to generate
     * @param reason  the reason for the error
     * 
     * @return the string representation of the error
     */
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
