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
package org.spiffyui.hellospiffymaven.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet returns the user and browser and server info.
 */
public class SimpleServlet extends HttpServlet
{

    private static final long serialVersionUID = -1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException,
            IOException
    {
        String user = request.getPathInfo();
        if (user.startsWith("/")) {
            user = user.substring(1);
        }
        String serverInfo = getServletContext().getServerInfo();
        String userAgent = request.getHeader("User-Agent");
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        /*
         We just need to return some simple JSON for this REST call.
         */
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append("\"user\": \"" + user + "\"");
        sb.append(",");
        sb.append("\"userAgent\": \"" + userAgent + "\"");
        sb.append(",");
        sb.append("\"serverInfo\": \"" + serverInfo + "\"");
        sb.append("}");
        out.println(sb.toString());
    }
}
