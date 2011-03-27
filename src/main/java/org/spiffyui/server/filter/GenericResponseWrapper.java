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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Servlet response wrapper that can manipulate the output from a
 * the servlet chain.
 */
public class GenericResponseWrapper extends HttpServletResponseWrapper
{
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
        return m_output.toString();
    }

    @Override
    public ServletOutputStream getOutputStream()
        throws IOException
    {
        return new FilterServletOutputStream(m_output);
    }

}

