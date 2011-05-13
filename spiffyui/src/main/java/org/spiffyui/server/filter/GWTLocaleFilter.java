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
package org.spiffyui.server.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
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
    public final void doFilter(final ServletRequest request,
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
        return ret.replace("<head>", "<head>\n\n\t\t<meta name=\"gwt:property\" content=\"locale=" + bestMatchLocale + "\">");
    }

    /**
     * Get the list of locales supported for this application.
     * 
     * @return the list of supported locales
     */
    protected abstract List<Locale> getMinimumSupportedLocales();
}

