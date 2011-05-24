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
package org.spiffyui.spsample.server;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet takes the appropriate template zip file,
 * changes it to reflect the project and package names requested
 * and serves up a new zip.
 */
public class ProjectCreatorServlet extends HttpServlet
{
    private static final Logger LOGGER = Logger.getLogger(CrayonColorsServlet.class.getName());

    private static final long serialVersionUID = -1L;

    private static final String MY_PROJECT = "MY_PROJECT";
    private static final String MY_PACKAGE = "MY_PACKAGE";
    private static final String TEMPLATE_ANT = "spiffyui-template-ant.zip";
    
    private static final int BUFFER = 4096;
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException,
            IOException
    {
        try {
            /*
             * Get the correct zip
             * (For now always spiffyui-template-ant.zip)
             */
            ZipInputStream zis = new ZipInputStream(getServletContext().getResourceAsStream("/WEB-INF/classes/" + TEMPLATE_ANT));
            
            /*
             * Change the project name 
             */
            String projectName = request.getParameter("projectName");
            /*
             * Change the package path
             * (Right now just doing name)
             */
            String packagePath = request.getParameter("packagePath");
    
            /*
             * Send the new zip
             */
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "inline; filename=" + projectName + ".zip;");
    
            ServletOutputStream dest = response.getOutputStream();  
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));  
    
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                System.out.println("Extracting: " + entry);
                int count;
                byte data[] = new byte[BUFFER];
                
                String entryName = entry.getName();
                entryName = entryName.replaceAll(MY_PACKAGE, packagePath);
                entryName = entryName.replaceAll(MY_PROJECT, projectName);
                ZipEntry newEntry = new ZipEntry(entryName);
                out.putNextEntry(newEntry);
                while ((count = zis.read(data, 0, BUFFER)) != -1) {
                    
                    out.write(data, 0, count);
                }
            }
            out.flush();
            out.close();
            zis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
