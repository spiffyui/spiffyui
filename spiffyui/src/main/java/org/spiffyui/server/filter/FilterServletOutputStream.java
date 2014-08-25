/*******************************************************************************
 *
 * Copyright 2011-2014 Spiffy UI Team   
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
    private final DataOutputStream m_stream;

    /**
     * Create a new filter servlet outputstream
     * 
     * @param output the output stream to wrap
     */
    public FilterServletOutputStream(OutputStream output)
    {
        m_stream = new DataOutputStream(output);
    }

    @Override
    public void write(int b) throws IOException
    {
        m_stream.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException
    {
        m_stream.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException
    {
        m_stream.write(b, off, len);
    }

}

