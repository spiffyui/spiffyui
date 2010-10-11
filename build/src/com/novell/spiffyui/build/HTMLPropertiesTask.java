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
package com.novell.spiffyui.build;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;

import java.io.IOException;
import java.util.Iterator;
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
        
        ResourceCollection rc = getResources();
        Iterator i = rc.iterator();
        
        try {
            Properties props = new Properties();
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
                    
                    props.setProperty(f.getName().replace(' ', '_').replace('.', '_'), sb.toString());
                    
                } finally {
                    if (in != null) {
                        in.close();
                    }
                }
            }
            
            PrintWriter out = null;
            try {
                out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(m_destinationFile), "UTF-8"));
                props.store(out, "");
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
