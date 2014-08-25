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
package org.spiffyui.spsample.server;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * The main purpose of this servlet is to improve our SEO (Search 
 * Engine Optimization).  To that end it server three basic functions: 
 *  
 * 1. Generate a Google Sitemap for our site.  Since 
 * the entire application is generated with JavaScript the search 
 * engines can't index the site very well.  This servlet generates 
 * a sitemap.xml file and adds the source HTML files to it.  That 
 * makes it possible to index all of the content in those files. 
 *  
 * 2. Server the index.html and index-debug.html pages.  This servet
 * handles both pages since we need to serve at those URL to fulfill 
 * the  contract for AJAX crawling outlined here: 
 *  
 *     http://code.google.com/web/ajaxcrawling/
 *  
 * This is especially complicated since Google AppEngine won't allow us 
 * to serve any page with a servlet if there is a real file at that URL. 
 *  
 *  
 * 3. Server the individual files or HTML snapshots of each page when 
 * requested with the _escaped_fragment_ fragment parameter.
 */
public class SiteMapServlet extends HttpServlet
{
    private static final Map<String, String> HASHES = new HashMap<String, String>();
    
    static {
        HASHES.put("landing", "/ajax/LandingPanel.html");
        HASHES.put("overview", "/ajax/OverviewPanel.html");
        HASHES.put("getStarted", "/ajax/GetStartedPanel.html");
        HASHES.put("hostedMode", "/ajax/HostedModePanel.html");
        HASHES.put("css", "/ajax/CSSPanel.html");
        HASHES.put("speed", "/ajax/BuildPanel.html");
        HASHES.put("rest", "/ajax/RESTPanel.html");
        HASHES.put("samples", "/ajax/SamplesPanel.html");
        HASHES.put("help", "/ajax/HelpPanel.html");
        HASHES.put("getInvolved", "/ajax/GetInvolvedPanel.html");
        HASHES.put("auth", "/ajax/AuthPanel.html");
        HASHES.put("l10n", "/ajax/DatePanel.html");
        HASHES.put("forms", "/ajax/FormPanel.html");
        HASHES.put("widgets", "/ajax/WidgetsPanel.html");
        HASHES.put("authTest", "/ajax/AuthPanel.html");
        HASHES.put("license", "/ajax/LicensePanel.html");
    }
    
    private static final Logger LOGGER = Logger.getLogger(SiteMapServlet.class.getName());
    private static final long serialVersionUID = -1L;
    
    private static final ResourceBundle BUILD_BUNDLE = 
        ResourceBundle.getBundle("org/spiffyui/spsample/server/buildnum", Locale.getDefault());
    
    private static String g_siteMap = null;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        if (request.getParameter("_escaped_fragment_") != null) {
            /*
             This means someone is the special escaped fragment version.
             This is probably a search engine and we'll give them the file
             */
            String file = request.getParameter("_escaped_fragment_");
            if (file.startsWith("b=")) {
                file = file.substring(2);
            }
            
            returnFile(file, response);
            return;
        } else if (request.getRequestURI().indexOf("sitemap.xml") > -1) {
            response.setContentType("text/xml");
            PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
            
            try {
                if (g_siteMap == null) {
                    /*
                     If we haven't created the sitemap yet we'll create it and cache it
                     */
                    createSiteMap(request);
                }
            } catch (Exception e) {
                LOGGER.throwing(SiteMapServlet.class.getName(), "service", e);
                e.printStackTrace();
            }
            
            out.write(g_siteMap);
            out.flush();
        } else {
            returnFile(request.getServletPath(), response);
        }
    }
    
    private void returnFile(String name, HttpServletResponse response) throws ServletException, IOException 
    {
        boolean addHeader = false;
        
        String file = null;
        
        if (name.equals("") || name.equals("/") ||
            name.equals("index.html") || name.equals("/index.html")) {
            /*
             Google AppEngine won't let us use a servlet in place if a real file.
             The solution is to serve at index.html, but return the contents of the
             real file at index.htm.  This is super hacky, but I can't find a better
             way around it.  Hackito Ergo Sum.
             */
            file = "/index.htm";
        } else if (name.equals("index-debug.html") || name.equals("/index-debug.html")) {
            file = "/index-debug.htm";
        } else if (HASHES.containsKey(name)) {
            addHeader = true;
            file = HASHES.get(name);
        }
        
        if (file == null) {
            file = name;
        }
        
        response.setContentType("text/html; charset=utf-8");
        InputStream in = getServletConfig().getServletContext().getResourceAsStream(file);

        if (in == null) {
            /*
             This means they requested a file that doesn't exist so they get a
             404
             */
            in = getServletConfig().getServletContext().getResourceAsStream("/404.html");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
        }
        
        InputStreamReader reader = new InputStreamReader(in, "UTF-8");
        
        try {
            char buf[] = new char[512];
            OutputStreamWriter out = new OutputStreamWriter(response.getOutputStream(), "UTF-8");
            
            if (addHeader) {
                addHeader(out, name);
            }
            
            int numRead;
            while ((numRead = reader.read(buf)) >= 0) {
                out.write(buf, 0, numRead);
            }
            
            if (addHeader) {
                addFooter(out);
            }
            
            out.flush();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
    
    private void addHeader(Writer out, String name) throws IOException 
    {
        /*
         The snippets are not full HTML pages so we add the header
         information to them.  
         */
        
        String header = "<!DOCTYPE html>\n\n" + 
            "<html>\n" + 
            "<head>\n" + 
            "<title>Spiffy UI Framework - " + name + "</title>\n" + 
            "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" + 
            //"<script type=\"text/javascript\" src=\"jquery.min.js\"></script>\n" + 
            //"<script type=\"text/javascript\" src=\"spsample.min.js\"></script>\n" + 
            //"<script type=\"text/javascript\">\n" + 
            //"spiffyui.autoloadCSS = false;\n" + 
            //"spiffyui.autoloadHTML = false;\n" + 
            //"</script>\n" + 
            //"</head>\n" + 
            "<body>\n";
        
        out.write(header);
    }
    
    private void addFooter(Writer out) throws IOException 
    {
        /*
         The snippets are not full HTML pages so we add the footer
         information to them.  
         */
        
        String footer = "</body>\n" + 
            "</html>";
        
        out.write(footer);
    }
    
    private synchronized void createSiteMap(HttpServletRequest request) throws Exception
    {
        if (g_siteMap != null) {
            /*
             then we've already created the sitemap
             */
            return;
        }
        
        StringWriter out = new StringWriter();
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(out);
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        
        StartDocument startDocument = eventFactory.createStartDocument();
        eventWriter.add(startDocument);
        
        eventWriter.add(end);
        
        ArrayList<Namespace> ns = new ArrayList<Namespace>();
        
        ArrayList<Attribute> atts = new ArrayList<Attribute>();
        atts.add(eventFactory.createAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance"));
        atts.add(eventFactory.createAttribute("xsi:schemaLocation", "http://www.sitemaps.org/schemas/sitemap/0.9 http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd"));
        atts.add(eventFactory.createAttribute("xmlns", "http://www.sitemaps.org/schemas/sitemap/0.9"));
        StartElement urlSetStartElement = eventFactory.createStartElement("", "", "urlset", atts.iterator(), ns.iterator());
        
        eventWriter.add(urlSetStartElement);
        eventWriter.add(end);
        findFiles(request, getServletConfig().getServletContext(), eventWriter);
        
        eventWriter.add(eventFactory.createEndElement("", "", "urlset"));
        eventWriter.add(end);
        eventWriter.add(eventFactory.createEndDocument());
        eventWriter.close();
        
        g_siteMap = out.toString();
    }
    
    private void findFiles(HttpServletRequest request, ServletContext context, XMLEventWriter eventWriter) throws XMLStreamException
    {
        /*
         First we add a node for the home page
         */
        createNode(eventWriter, request.getRequestURL().substring(0, request.getRequestURL().length() - 11), "1.0");
        
        for (String key : HASHES.keySet()) {
            createNode(eventWriter, request.getRequestURL().substring(0, request.getRequestURL().length() - 11)  + 
                       "#!" + key, "0.8");
        }
        
        /*
         Now we return all the content from the JavaDoc directory
         */
        createNode(eventWriter, request.getRequestURL().substring(0, request.getRequestURL().length() - 11) + "javadoc", "0.7");
        findStaticFiles(request, context, eventWriter, "/javadoc");
        
        /*
         We also want to include the content from the Maven plugin documentation
         */
        createNode(eventWriter, request.getRequestURL().substring(0, request.getRequestURL().length() - 11) + "maven-plugin", "0.7");
        findStaticFiles(request, context, eventWriter, "/maven-plugin");
        
    }
    
    private void findStaticFiles(HttpServletRequest request, ServletContext context, 
                                 XMLEventWriter eventWriter, String ref) throws XMLStreamException
    {
        Set set = context.getResourcePaths(ref);
        Iterator iter = set.iterator();
        while (iter.hasNext()) {
            String file = iter.next().toString();
            if (file.endsWith("/")) {
                /*
                 Then this is a directory and we want to look into it
                 */
                file = file.substring(0, file.length() - 1);
                file = file.substring(file.lastIndexOf('/') + 1);
                findStaticFiles(request, context, eventWriter, ref + "/" + file);
            } else {
                if (file.endsWith(".html") || file.endsWith(".htm")) {
                    file = file.substring(file.lastIndexOf('/'));
                    createNode(eventWriter, request.getRequestURL().substring(0, request.getRequestURL().length() - 12)  + 
                               ref + file, "0.6");
                }
            }
        }
    }

    private void createNode(XMLEventWriter eventWriter, String loc, String priority) throws XMLStreamException
    {
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t");
        
        eventWriter.add(tab);
        eventWriter.add(eventFactory.createStartElement("", "", "url"));
        
        /*
         Generate the location element
         */
        eventWriter.add(end);
        eventWriter.add(tab);
        eventWriter.add(tab);
        eventWriter.add(eventFactory.createStartElement("", "", "loc"));
        
        Characters characters = eventFactory.createCharacters(loc);
        eventWriter.add(characters);
        
        eventWriter.add(eventFactory.createEndElement("", "", "loc"));
        eventWriter.add(end);
        
        /*
         Add the last modification date of this page
         */
        eventWriter.add(tab);
        eventWriter.add(tab);
        eventWriter.add(eventFactory.createStartElement("", "", "lastmod"));
        
        /*
         We need to use the W3C date time format here
         */
        characters = eventFactory.createCharacters(
            new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.parseLong(BUILD_BUNDLE.getString("build.date")))));
        eventWriter.add(characters);
        
        eventWriter.add(eventFactory.createEndElement("", "", "lastmod"));
        eventWriter.add(end);
        
        /*
         Add the change frequency
         */
        eventWriter.add(tab);
        eventWriter.add(tab);
        eventWriter.add(eventFactory.createStartElement("", "", "changefreq"));
        
        characters = eventFactory.createCharacters("daily");
        eventWriter.add(characters);
        
        eventWriter.add(eventFactory.createEndElement("", "", "changefreq"));
        eventWriter.add(end);
        
        /*
         Add the priority of this page
         */
        eventWriter.add(tab);
        eventWriter.add(tab);
        eventWriter.add(eventFactory.createStartElement("", "", "priority"));
        
        characters = eventFactory.createCharacters(priority);
        eventWriter.add(characters);
        
        eventWriter.add(eventFactory.createEndElement("", "", "priority"));
        eventWriter.add(end);
        
        eventWriter.add(tab);
        eventWriter.add(eventFactory.createEndElement("", "", "url"));
        eventWriter.add(end);

    }
}
