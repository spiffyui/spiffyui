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
import java.util.List;
import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.mojo.gwt.ClasspathBuilder;
import org.codehaus.mojo.gwt.GwtModule;
import org.codehaus.mojo.gwt.GwtModuleReader;

import org.spiffyui.maven.plugins.utils.SpiffyGwtModuleReader;
/**
 * Goal which initializes various default property values for the plugin
 * 
 * @goal initialize
 * @phase initialize
 */
public class InitializeMojo extends AbstractMojo
{

    /**
     * {@link MavenProject} to process.
     * 
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * @parameter default-value="${project.build.directory}"
     * @required
     * @readonly
     */
    private File buildDirectory;
    
    /**
     * <p> 
     * User agents to compile for this application .
     * </p> 
     *  
     * <p> 
     * This variable provides the option to override the user agents specified 
     * in the GWT module file and build a different set of permutations for the 
     * GWT project.   
     * </p> 
     *  
     * <p> 
     * The value of this variable is a list of user agents separated by commas. 
     * For example, gecko1_8,safari 
     * </p> 
     *  
     * <p> 
     * By default this value is unspecified and the GWT compiler will just use the 
     * user agents specified in the GWT module file. 
     * </p> 
     * 
     * @parameter default-value="${spiffyui.useragents}" 
     */
    private String userAgents;
    
    /**
     * <p> 
     * Locales to compile for this application. 
     * </p> 
     *  
     * <p> 
     * This variable provides the option to override the locales specified 
     * in the GWT module file and build a different set of permutations for the 
     * GWT project.   
     * </p> 
     *  
     * <p> 
     * The value of this variable is a list of user agents separated by commas. 
     * For example, en_US,fr_FR 
     * </p> 
     *  
     * <p> 
     * By default this value is unspecified and the GWT compiler will just use the 
     * locales specified in the GWT module file. 
     * </p> 
     * 
     * @parameter default-value="${spiffyui.locales}" 
     */
    private String locales;

    private static final String ATTR_WWW = "spiffyui.www";
    private static final String ATTR_GENERATEDSRC = "spiffyui.generated-source";
    private static final String ATTR_SOURCE = "maven.compiler.source";
    private static final String ATTR_TARGET = "maven.compiler.target";
    private static final Float MIN_COMPILER = new Float(1.6);
    
    /**
     This is the string we use to name the temporary module created during compilation
     */
    public static final String SPIFFY_TMP_SUFFIX = "_spiffytmp";
    
    @Override
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        Properties p = project.getProperties();
        
        setCompilerAttr();
        
        if (project.getPackaging() == "spiffyui-client") {
            setDefaultPath(ATTR_WWW, "www");
        } else {
            setDefaultPath(ATTR_WWW, project.getArtifactId() + "-" + project.getVersion());
        }
        
        setDefaultPath(ATTR_GENERATEDSRC, "generated-sources/gwt");
        
        GwtModuleReader gmr = new SpiffyGwtModuleReader(project, getLog(), new ClasspathBuilder(), userAgents, locales);
        
        List<String> modules = gmr.getGwtModules();
        
        /*
         Our module reader will create a new module with our special suffix.  That
         means there will be two modules in the project, but we only want our new
         one so we take the other one out of the list.
         */
        if (modules.size() > 1) {
            for (int i = modules.size() - 1; i >= 0; i--) {
                if (!modules.get(i).endsWith(SPIFFY_TMP_SUFFIX)) {
                    modules.remove(modules.get(i));
                }
            }
        }
        
        /* ensure there is only one module, and record it for posterity */
        switch (modules.size()) {
            case 0:
                throw new MojoExecutionException("No GWT modules detected");
            case 1:
                try {
                    GwtModule module = gmr.readModule(modules.get(0));
                    String[] sources = module.getSources();
                    p.setProperty("spiffyui.gwt.module.name", module.getName());
                    
                    String path = new File(p.getProperty(ATTR_WWW), module.getPath()).getAbsolutePath();
                    if (path.endsWith(SPIFFY_TMP_SUFFIX)) {
                        path = path.substring(0, path.length() - SPIFFY_TMP_SUFFIX.length());
                    }
                    p.setProperty("spiffyui.gwt.module.path",  path);

                    if (sources != null && sources.length > 0) {
                        /*
                         Users can specify a different sources path in their module.
                         In that case we want to use that package.  If they haven't
                         specified that directory they get the client directory by
                         default.
                         */
                        p.setProperty("spiffyui.gwt.module.package",
                            module.getPackage() + "." + sources[0]);
                    } else {
                        p.setProperty("spiffyui.gwt.module.package",
                                module.getPackage() + ".client");
                    }
                } catch (Exception e) {
                    throw new MojoExecutionException(e.getMessage(), e);
                }
                break;
            default:
                throw new MojoExecutionException("Only one GWT module allowed, but " + modules.size() + " detected: " + modules);
        }

    }
    
    private void setCompilerAttr() throws MojoFailureException
    {
        Properties p = project.getProperties();
        String[] compilerAttrs = {ATTR_SOURCE, ATTR_TARGET};
        
        for (String attr : compilerAttrs) {
            String v = p.getProperty(attr);
            if (v != null) {
                Float val = new Float(v);
                if (val < MIN_COMPILER) {
                    throw new MojoFailureException(attr + " must be " + MIN_COMPILER + " or higher");
                }
            } else if (v == null) {
                p.setProperty(attr, MIN_COMPILER.toString());
            }
        }

    }
    
    private void setDefaultPath(String attr, String path)
    {
        Properties p = project.getProperties();
        
        if (!p.containsKey(attr)) {
            File f = new File(buildDirectory, path);
            p.setProperty(attr, f.getAbsolutePath());
        }

    }
}
