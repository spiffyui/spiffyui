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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileList;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.Resources;
import org.apache.tools.ant.types.resources.Restrict;
import org.apache.tools.ant.types.resources.selectors.Exists;
import org.apache.tools.ant.types.resources.selectors.Not;
import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
import org.apache.tools.ant.util.FileUtils;


/**
 * This is a custom task for generating properties files from HTML files
 */
public class HTMLPropertiesTask extends Task
{
    
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    private static final ResourceSelector EXISTS = new Exists();
    private static final ResourceSelector NOT_EXISTS = new Not(EXISTS);
    
    /**
     * The destination of the stream. If <code>null</code>, the system
     * console is used.
     */
    private File m_destinationFile;
    
    
    /**
     * Stores a collection of file sets and/or file lists, used to
     * select multiple files for compilation.
     */
    private ResourceCollection m_rc;
    
    
    /**
     * Adds the file set of JavaScript files to compile
     * 
     * @param set    the set of files
     */
    public void addFileset(FileSet set)
    {
        add(set);
    }

    /**
     * List of files to compile.
     * 
     * @param list   the list of files
     */
    public void addFilelist(FileList list)
    {
        add(list);
    }
    
    /**
     * Sepcify the location of the properties output file.  This is required
     * 
     * @param file the output file name
     */
    public void setProperties(File file)
    {
        this.m_destinationFile = file;
    }
    
    /**
     * Add an arbitrary ResourceCollection.
     * @param c the ResourceCollection to add.
     * @since Ant 1.7
     */
    public synchronized void add(ResourceCollection c)
    {
        if (m_rc == null) {
            m_rc = c;
            return;
        }
        if (!(m_rc instanceof Resources)) {
            Resources newRc = new Resources();
            newRc.setProject(getProject());
            newRc.add(m_rc);
            m_rc = newRc;
        }
        ((Resources) m_rc).add(c);
    }
    
    private ResourceCollection getResources()
    {
        if (m_rc == null) {
            return null;
        }
        Restrict noexistRc = new Restrict();
        noexistRc.add(NOT_EXISTS);
        noexistRc.add(m_rc);
        for (Iterator i = noexistRc.iterator(); i.hasNext();) {
            log(i.next() + " does not exist.", Project.MSG_ERR);
        }
        if (m_destinationFile != null) {
            for (Iterator i = m_rc.iterator(); i.hasNext();) {
                Object o = i.next();
                if (o instanceof FileResource) {
                    File f = ((FileResource) o).getFile();
                    if (FILE_UTILS.fileNameEquals(f, m_destinationFile)) {
                        throw new BuildException("Input file \"" + f + "\" is the same as the output file.");
                    }
                }
            }
        }
        Restrict result = new Restrict();
        result.add(EXISTS);
        result.add(m_rc);
        return result;
    }
    
    /**
     * Reset state to default.
     */
    public void reset()
    {
        m_rc = null;
        m_destinationFile = null;
    }
    
    /**
     * We need to figure out the locale of the file based on the file name.  The file can be
     * something like foo.html, foo_fr.html foo_fr-FR.html, or foo_fr_FR.html.  We parse that
     * name and create a locale object based on the language and country codes in the name
     * 
     * @param name   the name of the file
     * 
     * @return the locale for this file name
     */
    private Locale findLocale(String name)
    {
        if (name == null || name.length() < 4) {
            return null;
        }
        
        name = name.substring(0, name.lastIndexOf('.'));
        String country = null;
        String language = null;
        
        if (name.length() > 7 && 
            (name.charAt(name.length() - 6) == '-' ||
            name.charAt(name.length() - 6) == '_')) {
            language = name.substring(name.length() - 5,
                                      name.length() - 3);
        }
        
        if (name.length() > 4 && 
            (name.charAt(name.length() - 3) == '-' ||
            name.charAt(name.length() - 3) == '_')) {
            if (language == null) {
                /*
                 Then they specified a language without a country
                 */
                language = name.substring(name.length() - 2,
                                          name.length());
            } else {
                /*
                 Then we already have the language and this is the country
                 */
                country =  name.substring(name.length() - 2,
                                          name.length());
            }
        }
        
        if (language == null && country == null) {
            /*
             In this case there was no locale information and we
             return null to indicate this is the default locale.
             */
            return null;
        } else if (country == null) {
            /*
             In this case they have a language code, but no country
             code.  This means we want a locale like French or English
             with no country code.
             */
            return new Locale(language);
        } else {
            /*
             This case is where there was a language and a country
             */
            return new Locale(language, country);
        }
        
    }
    
    /**
     * Executes this task to run the compiler.
     * 
     * @exception BuildException
     *                   if there isn't enough information to run the GWT compiler
     */
    public void execute() throws BuildException
    {
        if (m_destinationFile == null) {
            throw new BuildException("Must specify a Properties file output");
        }
        
        if (getResources() == null) {
            return;
        }
        
        HashMap<String, Properties> props = new HashMap<String, Properties>();
        
        ResourceCollection rc = getResources();
        Iterator i = rc.iterator();
        
        try {
            while (i.hasNext()) {
                File f = ((FileResource) i.next()).getFile();
                Reader in = null;
                try {
                    in = new InputStreamReader(new FileInputStream(f), "UTF-8");
                    StringBuffer sb = new StringBuffer();
                    int c = -1;
                    while ((c = in.read()) > -1) {
                        if (c == '\'') {
                            sb.append("&#39;");
                        } else if (c == '{' ||
                                   c == '}') {
                            sb.append('\'');
                            sb.append((char) c);
                            sb.append('\'');
                        } else {
                            sb.append((char) c);
                        }
                    }
                    
                    Locale loc = findLocale(f.getName());
                    String name = f.getName();
                    if (loc != null) {
                        /*
                         Then we want to take the locale out of the file name
                         */
                        name = name.substring(0, name.lastIndexOf('.') - loc.toString().length() - 1) + 
                            name.substring(name.lastIndexOf('.'), name.length());
                    }
                    
                    getProperties(loc, props).setProperty(name.replace(' ', '_').replace('.', '_'), sb.toString());
                    
                } finally {
                    if (in != null) {
                        in.close();
                    }
                }
            }
            
            /*
             Now we need to write out all the properties files
             */
            for (String loc : props.keySet()) {
                PrintWriter out = null;
                try {
                    String name = m_destinationFile.getName();
                    name = name.substring(0, name.lastIndexOf('.')) + loc + 
                        name.substring(name.lastIndexOf('.'), name.length());
                    
                    out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(
                        new File(m_destinationFile.getParentFile(), name)), "UTF-8"));
                    props.get(loc).store(out, "");
                } finally {
                    if (out != null) {
                        out.close();
                    }
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    private Properties getProperties(Locale locale, HashMap<String, Properties> props)
    {
        String key = null;
        if (locale == null) {
            key = "";
        } else {
            key = "_" + locale.toString();
        }
        
        if (!props.containsKey(key)) {
            props.put(key, new Properties());
        }
        
        return props.get(key);
    }
}
