/*******************************************************************************
 *
 * Copyright 2011 Spiffy UI Team   
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Servlet response wrapper that can manipulate the output from a
 * the servlet chain.
 */
public class GenericResponseWrapper extends HttpServletResponseWrapper
{
    private static final Logger LOGGER = Logger.getLogger(GWTLocaleFilter.class.getName());

    private final ByteArrayOutputStream m_output;
    private int m_contentLength;
    private String m_contentType;

    /**
     * Create a new generic response wrapper
     * 
     * @param response the response to wrap
     */
    public GenericResponseWrapper(HttpServletResponse response)
    {
        super(response);
        m_output = new ByteArrayOutputStream();
    }

    @Override
    public PrintWriter getWriter()
    {
        return new PrintWriter(m_output);
    }

    @Override
    public void setContentLength(int length)
    {
        this.m_contentLength = length;
        super.setContentLength(length);
    }

    public int getContentLength()
    {
        return m_contentLength;
    }

    @Override
    public void setContentType(String type)
    {
        this.m_contentType = type;
        super.setContentType(type);
    }

    @Override
    public String getContentType()
    {
        return m_contentType;
    }

    @Override
    public String toString()
    {
        try {
            return m_output.toString("UTF-8");
        } catch (UnsupportedEncodingException uee) {
            LOGGER.throwing(GenericResponseWrapper.class.getName(), "toString", uee);
            return m_output.toString();
        }
    }

    @Override
    public ServletOutputStream getOutputStream()
        throws IOException
    {
        return new FilterServletOutputStream(m_output);
    }

}

