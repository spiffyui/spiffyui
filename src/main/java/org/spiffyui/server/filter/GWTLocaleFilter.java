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
package org.spiffyui.server.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.spiffyui.server.i18n.BasicBestLocaleMatcher;

/**
 * Filter that will substitute the various markers in retrieved
 * xml and jnlp files with values retrieved from the servlet
 * context.
 */
public abstract class GWTLocaleFilter implements Filter
{
    @Override
    public void init(final FilterConfig filterConfig)
    {
    }

    @Override
    public void destroy()
    {
        // do nothing
    }

    @Override
    public void doFilter(final ServletRequest request,
        final ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        PrintWriter out = new PrintWriter(response.getOutputStream());
        GenericResponseWrapper wrapper = new GenericResponseWrapper((HttpServletResponse) response);
        chain.doFilter(request, wrapper);

        response.setContentType(wrapper.getContentType());
        String transformedContent = addLocaleInfo(wrapper.toString(), (HttpServletRequest) request, (HttpServletResponse) response);
        response.setContentLength(transformedContent.length());
        out.write(transformedContent);
        out.flush();
        out.close();
    }
    
    

    private String addLocaleInfo(String origContent, HttpServletRequest request, HttpServletResponse response)
    {
        Locale bestMatchLocale = BasicBestLocaleMatcher.getBestMatchLocale(request, response, getMinimumSupportedLocales());
        String ret = origContent;
        return ret.replace("<head>", "<head>\n\n<meta name=\"gwt:property\" content=\"locale=" + bestMatchLocale + "\">");
    }

    /**
     * Get the list of locales supported for this application.
     * 
     * @return the list of supported locales
     */
    protected abstract List<Locale> getMinimumSupportedLocales();
}

