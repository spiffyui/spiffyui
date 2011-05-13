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
package org.spiffyui.server;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.spiffyui.server.i18n.BasicBestLocaleMatcher;

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


    @Override
    public void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        Locale bestMatchLocale = getBestMatchLocale(request, response, getServletConfig().getServletContext());
        response.setContentType(CONTENT_TYPE_JS);

        String resourceName = request.getServletPath().indexOf("jquery.ui.datepicker") > 0 ?
            "jquery.ui.datepicker" : "date";
        String file = JSLocaleUtil.getFile(resourceName, bestMatchLocale, getServletConfig().getServletContext());


        if (file != null) {
            response.sendRedirect(file);
        }
    }

    /**
     * Override this method to use customized algorithm for determining best match locale.
     * @param request - the HttpServletRequest
     * @param response - the HttpServletResponse
     * @param context - the ServletContext
     * @return the best match Locale
     * @throws ServletException thrown in case of error
     */
    protected Locale getBestMatchLocale(HttpServletRequest request, HttpServletResponse response, ServletContext context) throws ServletException
    {
        return BasicBestLocaleMatcher.getBestMatchLocale(request, response, context);
    }
}