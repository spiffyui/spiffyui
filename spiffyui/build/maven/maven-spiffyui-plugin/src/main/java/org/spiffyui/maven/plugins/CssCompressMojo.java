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
package org.spiffyui.maven.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.IOUtil;

import com.yahoo.platform.yui.compressor.CssCompressor;

/**
 * Invokes the YUI Compressor for the project .css source
 * 
 * @goal css-compress
 * @phase process-resources
 */
public class CssCompressMojo extends AbstractMojo
{
    /**
     * The character encoding scheme to be applied exporting the JSON document
     * 
     * @parameter expression="${encoding}" default-value="UTF-8"
     */
    private String encoding;

    /**
     * The output filename suffix.
     * 
     * @parameter expression="${spiffyui.yuicompressor.suffix}"
     *            default-value=".min"
     */
    private String suffix;

    /**
     * Insert line breaks in output after the specified column number.
     * 
     * @parameter expression="${spiffyui.yuicompressor.linebreakpos}"
     *            default-value="0"
     */
    private int linebreakpos;

    /**
     * The CSS directory name under source.  This directory is added
     * in addition to the source directory
     * 
     * @parameter expression="${spiffyui.css.source}"
     *            default-value="css"
     */
    private String css;

    /**
     * Path to the project .css sources to compress
     *  
     * @parameter default-value="src/main/webapp"
     */
    private File sourceDirectory;

    /**
     * The output directory to emit compressed artifacts
     * 
     * @parameter expression="${spiffyui.css.output}"
     *            default-value="${spiffyui.www}"
     * @required
     */
    private File outputDirectory;
    
    /**
     * The name of the generated compressed CSS file
     * 
     * @parameter expression="${spiffyui.css.output.filename}" 
     *            default-value="SpiffyUi.min.css" 
     */
    private String outputFileName;

    @Override
    public void execute()
        throws MojoExecutionException,
            MojoFailureException
    {

        if (!sourceDirectory.exists()) {
            getLog().debug("No sources, skipping");
            return;
        }
        
        String[] exts = new String[] {
            "css"
        };
        Collection<File> files = FileUtils.listFiles(sourceDirectory, exts, false);

        File cssDir = new File(sourceDirectory, css);
        if (cssDir.exists()) {
            files.addAll(FileUtils.listFiles(cssDir, exts, false));
        }

        try {
            String name = outputFileName;
            if (files.size() == 1) {
                /*
                 If there is only one file then we can use the
                 name of that file as the name of our CSS file.
                 We want this for backward compatability so existing
                 projects don't have to change their HTML.
                 */
                name = files.iterator().next().getName().replace(".css", ".min.css");
            }
            compress(concat(files), name);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MojoExecutionException(e.getMessage());
        }
    }
    
    /**
     * This method concatenates the set of CSS source files since YUI requires a 
     * single input file.
     * 
     * @param files  the files to concatenate
     * 
     * @return a temporary file containing the concatenated source files
     * @exception IOException
     */
    private File concat(Collection<File> files)
        throws IOException
    {
        File outFile = File.createTempFile("spiffy_", ".css");
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(outFile), encoding);
        try {
            for (File file : files) {
                getLog().debug("Adding to the CSS compiler list: " + file);
                InputStreamReader in = new InputStreamReader(new FileInputStream(file), encoding);
                int read;
                char[] buf = new char[1024];
        
                try {
                    while ((read = in.read(buf)) > 0) {
                        out.write(buf, 0, read);
                    }
                    
                    out.write('\n');
                } finally {
                    if (in != null) {
                        in.close();
                    }
                }
            }
        } finally {
            if (out != null) {
                out.close();
            }
        }
        
        //outFile.deleteOnExit();
        return outFile;
    }

    /**
     * Call the YUI compressor to compress our concatenated CSS file.
     * 
     * @param inFile the concatenated CSS file
     * 
     * @exception IOException
     * @exception MojoExecutionException
     */
    private void compress(File inFile, String outFileName)
        throws IOException, MojoExecutionException
    {
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }

        File outFile = new File(outputDirectory, outFileName);
        
        InputStreamReader in = null;
        OutputStreamWriter out = null;

        try {
            in = new InputStreamReader(new FileInputStream(inFile), encoding);
            out = new OutputStreamWriter(new FileOutputStream(outFile), encoding);

            if (!outFile.getParentFile().exists() && !outFile.getParentFile().mkdirs()) {
                throw new MojoExecutionException("Cannot create resource output directory: " + outFile.getParentFile());
            }

            getLog().debug("YUICOMPRESS: " + inFile.getAbsolutePath() + " -> " + outFile.getAbsolutePath());

            try {
                CssCompressor compressor = new CssCompressor(in);
                compressor.compress(out, linebreakpos);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(
                    "Unexpected characters found in CSS file. Ensure that the CSS file does not contain '$', and try again", e);
            }

        } finally {
            IOUtil.close(in);
            IOUtil.close(out);
        }

    }

}
