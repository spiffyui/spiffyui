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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;

/**
 * Specialized ServletOutputStream, exposing functionality that will be utilized
 * by the filter response wrapper.
 */
public class FilterServletOutputStream extends ServletOutputStream
{
    private final DataOutputStream stream;

    /**
     * Create a new filter servlet outputstream
     * 
     * @param output the output stream to wrap
     */
    public FilterServletOutputStream(OutputStream output)
    {
        stream = new DataOutputStream(output);
    }

    @Override
    public void write(int b) throws IOException
    {
        stream.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException
    {
        stream.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException
    {
        stream.write(b, off, len);
    }

}

