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
package com.novell.spiffyui.server;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.novell.spiffyui.server.i18n.BasicBestLocaleMatcher;

/**
 * This servlet redirect to a specific locale version of the 
 * JQuery date picker, date.js or returns the best match locale. 
 */
public class JSLocaleServlet extends HttpServlet
{


    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -4147481572543669440L;
    /**
     * This is the default content type for a javascript file.
     * Although obsoleted by RFC 4329 in favor of application/javascript, it does have cross browser support
     * which application/javascript lacks (IE does not support it)
     */
    private static final String CONTENT_TYPE_JS = "text/javascript";
    private static final String CONTENT_TYPE_PLAIN = "text/plain";


    @Override
    public void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        
        Locale bestMatchLocale = getBestMatchLocale(request, response, getServletConfig().getServletContext());
        response.setContentType(CONTENT_TYPE_JS);

        System.out.println("servlet path = " + request.getServletPath());
        System.out.println("request uri = " + request.getRequestURI());
        System.out.println("context path = " + request.getContextPath());
        
        String resourceName = request.getServletPath().indexOf("jquery.ui.datepicker") > 0 ?
            "jquery.ui.datepicker" : "date";
        String file = JSLocaleUtil.getFile(resourceName, bestMatchLocale, getServletConfig().getServletContext());


        if (resourceName.equals("date")) {
            String type = request.getParameter("type");
            boolean sendJS;
            if (type.equalsIgnoreCase("js")) {
                sendJS = true;
                response.setContentType(CONTENT_TYPE_JS);
            } else {
                sendJS = false;
                response.setContentType(CONTENT_TYPE_PLAIN);
            }
            if (file != null) {
                if (sendJS) {
                    response.sendRedirect("js/lib/i18n/" + file);
                } else {
                    ServletOutputStream out = response.getOutputStream();
    
                    // Get the localeString from file name. file name is e.g. "date-zh-CN.js"  then localeString would be "zh-CN";
                    String localeString = file.substring(5, file.indexOf("."));
                    out.print(localeString);
                }
            }
        } else {
            if (file != null) {
                response.sendRedirect(file);
            }            
        }
    }

    /**
     * Override this method to use customized algorithm for determining best match locale.
     * @param request - the HttpServletRequest
     * @param response - the HttpServletResponse
     * @param context - the ServletContext
     * @return the best match Locale
     * @throws ServletException
     */
    protected Locale getBestMatchLocale(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext) throws ServletException
    {
        return BasicBestLocaleMatcher.getBestMatchLocale(request, response, servletContext);
    }
}