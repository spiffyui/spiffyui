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
    
}
