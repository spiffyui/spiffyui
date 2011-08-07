package org.spiffyui.maven.plugins.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.mojo.gwt.AbstractGwtModuleMojo;
import org.codehaus.mojo.gwt.ClasspathBuilder;
import org.codehaus.mojo.gwt.GwtModule;
import org.codehaus.mojo.gwt.GwtModuleReader;
import org.codehaus.mojo.gwt.utils.GwtModuleReaderException;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;

import org.spiffyui.maven.plugins.InitializeMojo;

/**
 * <p>
 * The SpiffyGwtModuleReader finds GWT modules in a Maven project and makes them available
 * for GWT compilation.  
 * </p>
 * 
 * <p>
 * This class finds GWT modules and creates a new module which inherits from the existing
 * module and inserts our build properties, returns the new temporary project file, and 
 * marks the temporary project file for deletion when the build is complete.
 * </p>
 */
public class SpiffyGwtModuleReader implements GwtModuleReader
{
    private static final String GWT_MODULE_EXTENSION = ".gwt.xml";

    private MavenProject m_mavenProject;
    private ClasspathBuilder m_classpathBuilder;
    private Log m_log;
    private String m_userAgents;
    private String m_locales;

    /**
     * Create a new SpiffyGwtModuleReader.
     * 
     * @param mavenProject
     *                   the maven project
     * @param log        the logging object
     * @param classpathBuilder
     *                   the classpath builder
     * @param userAgents the user agents for this project or null to leave the default agents
     * @param locales    the locales for this project or null to leave the default locales
     */
    public SpiffyGwtModuleReader(MavenProject mavenProject, Log log, ClasspathBuilder classpathBuilder, 
                                 String userAgents, String locales)
    {
        m_mavenProject = mavenProject;
        m_log = log;
        m_classpathBuilder = classpathBuilder;
        m_userAgents = userAgents;
        m_locales = locales;
    }

    /**
     * Get the GWT modules for this project.
     * 
     * @return the list of GWT modules found in this project
     */
    @SuppressWarnings("unchecked")
    public List<String> getGwtModules()
    {
        // Use a Set to avoid duplicate when user set src/main/java as
        // <resource>
        Set<String> mods = new HashSet<String>();

        Collection<String> sourcePaths = (Collection<String>) m_mavenProject.getCompileSourceRoots();
        for (String sourcePath : sourcePaths) {
            File sourceDirectory = new File(sourcePath);
            if (sourceDirectory.exists()) {
                DirectoryScanner scanner = new DirectoryScanner();
                scanner.setBasedir(sourceDirectory.getAbsolutePath());
                scanner.setIncludes(new String[] {
                    "**/*" + AbstractGwtModuleMojo.GWT_MODULE_EXTENSION
                });
                scanner.scan();

                mods.addAll(Arrays.asList(scanner.getIncludedFiles()));
            }
        }

        Collection<Resource> resources = (Collection<Resource>) m_mavenProject.getResources();
        for (Resource resource : resources) {
            File resourceDirectoryFile = new File(resource.getDirectory());
            if (!resourceDirectoryFile.exists()) {
                continue;
            }
            DirectoryScanner scanner = new DirectoryScanner();
            scanner.setBasedir(resource.getDirectory());
            scanner.setIncludes(new String[] {
                "**/*" + AbstractGwtModuleMojo.GWT_MODULE_EXTENSION
            });
            scanner.scan();
            mods.addAll(Arrays.asList(scanner.getIncludedFiles()));
        }

        if (mods.isEmpty()) {
            m_log.warn("GWT plugin is configured to detect modules, but none were found.");
        }

        List<String> modules = new ArrayList<String>(mods.size());
        for (String fileName : mods) {
            String path = fileName.substring(0, fileName.length() - AbstractGwtModuleMojo.GWT_MODULE_EXTENSION.length());
            modules.add(path.replace(File.separatorChar, '.'));
        }
        if (modules.size() > 0) {
            m_log.info("auto discovered modules " + modules);
        }
        return modules;
    }

    /**
     * Read a specified GWT module into the project.
     * 
     * @param name   the name of the module to read
     * 
     * @return the object representing this module
     * @throws GwtModuleReaderException if there an error reading the module
     */
    public GwtModule readModule(String name) throws GwtModuleReaderException
    {
        String modulePath = name.replace('.', '/') + GWT_MODULE_EXTENSION;
        Collection<String> sourceRoots = m_mavenProject.getCompileSourceRoots();
        for (String sourceRoot : sourceRoots) {
            File root = new File(sourceRoot);
            File xml = new File(root, modulePath);
            if (xml.exists()) {
                m_log.debug("GWT module " + name + " found in " + root);
                return readModule(name, xml);
            }
        }
        Collection<Resource> resources = (Collection<Resource>) m_mavenProject.getResources();
        for (Resource resource : resources) {
            File root = new File(resource.getDirectory());
            File xml = new File(root, modulePath);
            if (xml.exists()) {
                m_log.debug("GWT module " + name + " found in " + root);
                return readModule(name, xml);
            }
        }

        throw new GwtModuleReaderException("GWT Module " + name + " not found in project sources or resources.");
    }

    private GwtModule readModule(String name, File file)
        throws GwtModuleReaderException

    {
        try {
            return readModule(name, new FileInputStream(file), file);
        } catch (FileNotFoundException e) {
            throw new GwtModuleReaderException("Failed to read module file " + file);
        }
    }

    private GwtModule readModule(String name, InputStream xml, File file)
        throws GwtModuleReaderException
    {
        try {
            Xpp3Dom dom = Xpp3DomBuilder.build(ReaderFactory.newXmlReader(xml));
            
            if (name.endsWith(InitializeMojo.SPIFFY_TMP_SUFFIX)) {
                /*
                 Then we've already created the temporary GWT module and we don't need
                 to create another one.
                 */
                
                return new GwtModule(name, dom, this);
            }

            dom.addChild(new Xpp3Dom("set-property name=\"user.agent\" value=\"gecko1_8\""));

            StringBuffer tmp = new StringBuffer();
            tmp.append(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" + 
                "<!DOCTYPE module SYSTEM \"gwt-module.dtd\">" + 
                    "<module rename-to=\"" + name + "\">" + 
                    "<inherits name=\"" + name + "\"/>");

            if (m_userAgents != null) {
                tmp.append("<set-property name=\"user.agent\" value=\"" + m_userAgents + "\"/>");
            }

            if (m_locales != null) {
                tmp.append("<extend-property name=\"locale\" values=\"" + m_locales + "\" />");
            }

            tmp.append("</module>");

            Xpp3Dom tmpDom = Xpp3DomBuilder.build(new StringReader(tmp.toString()));

            String fileName = name.substring(name.lastIndexOf(".") + 1) + InitializeMojo.SPIFFY_TMP_SUFFIX + ".gwt.xml";
            File f = new File(file.getParent(), fileName);
            FileUtils.fileWrite(f.getAbsolutePath(), tmpDom.toString());
            f.deleteOnExit();
            
            return new GwtModule(name, tmpDom, this);
        } catch (Exception e) {
            String error = "Failed to read module XML file " + xml;
            m_log.error(error);
            throw new GwtModuleReaderException(error, e);
        }
    }
}
