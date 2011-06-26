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
package org.spiffyui.build;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.GZIPOutputStream;


/**
 * This is a utility for GZIPing lists of files
 */
public final class GzipListUtil
{
    private GzipListUtil()
    {
        /*
         Our constructor is private
         */
    }
    
    /**
     * <p>
     * GZIP the specified list of files.
     * </p><p>
     * <p>
     * Many web servers will send GZIP versions of static files to browsers which support it. 
     * This method will GZIP lists of files so they can be served to those browsers. 
     * </p>
     * 
     * @param files  the files to GZIP.  Each file is GZIPed separately with the same file name
     *               and a .gz extension.
     * @param destinationDir
     *               the destination directory to write the list of zipped files
     * 
     * @throws IOException
     *                if there is a problem creating the zip files
     */
    public static void zipFileList(List<File> files, File destinationDir)
        throws IOException 
    {
        for (File source : files) {
            File dest = new File(destinationDir, source.getName() + ".gz");
            zipFile(source, dest);
        }
    }
    
    private static void zipFile(File source, File dest) throws IOException
    {
        FileInputStream in = new FileInputStream(source);
        BufferedInputStream in2 = new BufferedInputStream(in);
        FileOutputStream out = new FileOutputStream(dest);
        GZIPOutputStream zipOut = new GZIPOutputStream(out);
        BufferedOutputStream out2 = new BufferedOutputStream(zipOut);

        try {
            int chunk;
            while ((chunk = in2.read()) != -1) {
                out2.write(chunk);
            }
        } finally {
            if (out2 != null) {
                out2.close();
            }

            if (zipOut != null) {
                out2.close();
            }

            if (out != null) {
                out.close();
            }
        }
    }
}
