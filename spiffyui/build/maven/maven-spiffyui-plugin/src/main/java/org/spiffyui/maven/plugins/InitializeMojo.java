package org.spiffyui.maven.plugins;

import java.io.File;
import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

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
     * @parameter expression="${project.build.directory}"
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
        
        setDefaultPath(ATTR_WWW, "www");
        setDefaultPath(ATTR_HTMLPROPS, "generated-sources/htmlprops");
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
