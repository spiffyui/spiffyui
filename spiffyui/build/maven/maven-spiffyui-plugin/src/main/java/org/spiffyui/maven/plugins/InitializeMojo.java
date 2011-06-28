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
import org.codehaus.mojo.gwt.utils.DefaultGwtModuleReader;

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

    private static final String ATTR_WWW = "spiffyui.www";
    private static final String ATTR_HTMLPROPS = "spiffyui.htmlprops.path";
    private static final String ATTR_SOURCE = "maven.compiler.source";
    private static final String ATTR_TARGET = "maven.compiler.target";
    private static final Float MIN_COMPILER = new Float(1.6);
    
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
        
        setDefaultPath(ATTR_HTMLPROPS, "generated-sources/htmlprops");
        
        GwtModuleReader gmr = new DefaultGwtModuleReader(project, getLog(), new ClasspathBuilder());
        
        List<String> modules = gmr.getGwtModules();

        /* ensure there is only one module, and record it for posterity */
        switch (modules.size()) {
            case 0:
                throw new MojoExecutionException("No GWT modules detected");
            case 1:
                try {
                    GwtModule module = gmr.readModule(modules.get(0));
                    String[] sources = module.getSources();
                    File path = new File(p.getProperty(ATTR_WWW), module.getPath());
 
                    p.setProperty("spiffyui.gwt.module.name", module.getName());
                    p.setProperty("spiffyui.gwt.module.path",  path.getAbsolutePath());
                    
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
                    throw new MojoExecutionException(e.getMessage());
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
