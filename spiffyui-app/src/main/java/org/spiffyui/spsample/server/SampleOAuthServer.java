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
import java.io.InputStream;
import java.io.PrintWriter;

import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet acts as a stub authentication server, it will return user name parameter as authentication token
 */
public class SampleOAuthServer extends HttpServlet
{
    private static final long serialVersionUID = -1L;

    /** The contents of the login form */
    private static String g_loginForm;
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        String redirectUri = request.getParameterValues("redirect_uri")[0];

        String clientId = "null";

        if (request.getParameterValues("clientId") != null) {
            clientId = request.getParameterValues("clientId")[0];
        }

        String state = "null";

        if (request.getParameterValues("state") != null) {
            state = request.getParameterValues("state")[0];
        }

        loginForm(request, response, clientId, state, redirectUri);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        StringBuffer hash = new StringBuffer();

        hash.append("#");
        hash.append("state=" + request.getParameterValues("state")[0]);
        hash.append("&token_type=Bearer");
        hash.append("&access_token=1234");

        String redirectUri = request.getParameterValues("redirect_uri")[0];

        response.sendRedirect(redirectUri + hash.toString());
    }

    private void loginForm(HttpServletRequest request, HttpServletResponse response, String clientID, String state, String redirectURI)
    {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html");
        PrintWriter out;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        out.println(loadLoginForm(request.getSession().getServletContext(), request.getLocale(), clientID, state, redirectURI));
        out.close();
        return;
    }

    private static String loadLoginForm(ServletContext context)
    {
        if (g_loginForm == null) {
            InputStream in = context.getResourceAsStream("/login.html");
            Scanner scanner =  new Scanner(in, "UTF-8");
            g_loginForm = scanner.useDelimiter("\\A").next();
        }

        return g_loginForm;
    }

    /**
     * Load the login form text and inject the strings with localized and
     * dynamic values.
     * 
     * @param context  the servlet context to load the form with
     * @param locale   the current locale
     * @param clientID the client ID to add to the form
     * @param state    the state to add to the form
     * @param redirectURI
     *                 the redirection URI once the user logs in
     * 
     * @return the localized form with all of the values injected.
     */
    private String loadLoginForm(ServletContext context, Locale locale, String clientID, String state, String redirectURI)
    {
        StringBuffer sb = new StringBuffer(loadLoginForm(context));
        
        Pattern p = Pattern.compile("\\$\\{.*\\}");
        Matcher matcher = p.matcher(sb.toString());
        int offset = 0;
        
        while (matcher.find()) {
            String key = matcher.group().substring(2, matcher.group().length() - 1);
            String val = null;

            if ("clientID".equals(key)) {
                val = clientID;
            } else if ("state".equals(key)) {
                val = state;
            } else if ("redirectURI".equals(key)) {
                val = redirectURI;
            } else {
                throw new IllegalArgumentException("They key " + key + " wasn't found in the login form");
            }

            sb.replace(matcher.start(), matcher.end(), val);

            /*
             * We have to search again since we just changed all of the offsets
             * by replacing a string with a new string of a different length.
             */
            matcher = p.matcher(sb.toString());
        }

        return sb.toString();
        
    }
}
