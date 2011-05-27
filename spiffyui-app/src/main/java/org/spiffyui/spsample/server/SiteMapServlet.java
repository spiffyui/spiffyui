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

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * This servlet generates a Google Sitemap for our site.  Since 
 * the entire application is generated with JavaScript the search 
 * engines can't index the site very well.  This servlet generates 
 * a sitemap.xml file and adds the source HTML files to it.  That 
 * makes it possible to index all of the content in those files. 
 */
public class SiteMapServlet extends HttpServlet
{
    private static final Logger LOGGER = Logger.getLogger(SiteMapServlet.class.getName());
    private static final long serialVersionUID = -1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        if (request.getRequestURI().indexOf("ajax.html") > -1) {
            /*
             This means someone is the special escaped fragment version.
             This is probably a search engine and we'll give them the file
             */
            if (request.getParameter("_escaped_fragment_") != null) {
                String file = request.getParameter("_escaped_fragment_");
                if (file.startsWith("b=")) {
                    file = file.substring(2);
                }
                
                returnFile(file, response);
                return;
            } else {
                /*
                 Then they are requesting the actual file and we just redirect
                 to the real app.  This only happens when following links from
                 the search results.
                 */
                response.sendRedirect("/");
                return;
            }
        } 
        
        
        response.setContentType("text/xml");
        ServletOutputStream out = response.getOutputStream();
        try {
            createSiteMap(out);
        } catch (Exception e) {
            LOGGER.throwing(SiteMapServlet.class.getName(), "service", e);
        }
        
        out.flush();
    }
    
    private void returnFile(String file, HttpServletResponse response) throws ServletException, IOException 
    {
        System.out.println("returnFile(" + file + ")");
        if (file.equals("landing")) {
            file = "LandingPanel.html";
        }
        
        response.setContentType("text/html");
        InputStream in = getServletConfig().getServletContext().getResourceAsStream("/ajax/" + file);
        byte buf[] = new byte[1024];
        ServletOutputStream out = response.getOutputStream();
        
        int numRead;
        while ((numRead = in.read(buf)) >= 0) {
            out.write(buf, 0, numRead);
        }
        
        out.flush();
        
    }
    
    private void createSiteMap(OutputStream out) throws Exception
    {
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(out);
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        
        StartDocument startDocument = eventFactory.createStartDocument();
        eventWriter.add(startDocument);

        
        StartElement configStartElement = eventFactory.createStartElement("", "", "urlset");
        eventWriter.add(configStartElement);
        eventWriter.add(end);
        findFiles(getServletConfig().getServletContext(), eventWriter);
        
        eventWriter.add(eventFactory.createEndElement("", "", "urlset"));
        eventWriter.add(end);
        eventWriter.add(eventFactory.createEndDocument());
        eventWriter.close();
    }
    
    private void findFiles(ServletContext context, XMLEventWriter eventWriter) throws XMLStreamException
    {
        Set set = context.getResourcePaths("/ajax");
        Iterator iter = set.iterator();
        while (iter.hasNext()) {
            String file = iter.next().toString();
            file = file.substring(file.lastIndexOf('/') + 1);
            createNode(eventWriter, "/ajax.html#!b=" + file);
        }
    }

    private void createNode(XMLEventWriter eventWriter, String loc) throws XMLStreamException
    {
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t");
        
        eventWriter.add(tab);
        eventWriter.add(eventFactory.createStartElement("", "", "url"));
        
        
        eventWriter.add(tab);
        eventWriter.add(eventFactory.createStartElement("", "", "loc"));
        
        Characters characters = eventFactory.createCharacters(loc);
        eventWriter.add(characters);
        
        eventWriter.add(eventFactory.createEndElement("", "", "loc"));
        eventWriter.add(end);
        
        eventWriter.add(tab);
        eventWriter.add(eventFactory.createStartElement("", "", "changefreq"));
        
        characters = eventFactory.createCharacters("daily");
        eventWriter.add(characters);
        
        eventWriter.add(eventFactory.createEndElement("", "", "changefreq"));
        eventWriter.add(end);
        
        eventWriter.add(eventFactory.createEndElement("", "", "url"));
        eventWriter.add(end);

    }
}
