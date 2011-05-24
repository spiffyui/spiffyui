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
import java.util.Arrays;
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
    private static final long serialVersionUID = -1L;

    private static final String MY_PROJECT = "MY_PROJECT";
    private static final String MY_PACKAGE = "MY_PACKAGE";
    private static final String MY_FILE_PATH = "MY_FILE_PATH";
    
    private static final String TEMPLATE = "spiffyui-template-";
    
    private static final int BUFFER = 4096;
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException,
            IOException
    {
        try {
            /*
             * Get the correct zip
             */
            String projectType = request.getParameter("type");
            ZipInputStream zis = new ZipInputStream(getServletContext().getResourceAsStream("/WEB-INF/classes/" + TEMPLATE + projectType + ".zip"));
            
            /*
             * Get the project name 
             */
            String projectName = request.getParameter("projectName");
            /*
             * Get the package path
             */
            String packagePath = request.getParameter("packagePath");
            /*
             * Change . to / for directories 
             */
            String filePath = packagePath.replaceAll("\\.", "/");
            
            /*
             * Send the new zip
             */
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "inline; filename=" + projectName + ".zip;");
    
            ServletOutputStream dest = response.getOutputStream();  
            ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(dest));  
    
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                
                String entryName = entry.getName();
                entryName = entryName.replaceAll(MY_PACKAGE, filePath);
                entryName = entryName.replaceAll(MY_PROJECT, projectName);
                System.out.println("Putting new entry: " + entryName);
                
                ZipEntry newEntry = new ZipEntry(entryName);
                zos.putNextEntry(newEntry);
                
                int count;
                byte[] data = new byte[BUFFER];
                if (entry.isDirectory()) {
                    while ((count = zis.read(data, 0, BUFFER)) != -1) {                    
                        zos.write(data, 0, count);                        
                    }
                } else {
                    while ((count = zis.read(data, 0, BUFFER)) != -1) {
                        
                        byte[] copy = Arrays.copyOf(data, count);
                        String string = new String(copy, "UTF-8");
                        string = string.replaceAll(MY_PACKAGE, packagePath);
                        string = string.replaceAll(MY_PROJECT, projectName);
                        string = string.replaceAll(MY_FILE_PATH, filePath);
                        copy = string.getBytes("UTF-8");                                                

                        zos.write(copy, 0, copy.length);   
                    }
                }
            }
            zos.flush();
            zos.close();
            zis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
