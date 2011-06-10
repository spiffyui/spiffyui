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

    @Override
    public void execute()
        throws MojoExecutionException,
            MojoFailureException
    {
        Properties p = project.getProperties();

        if (!p.containsKey(ATTR_WWW)) {
            File path = new File(buildDirectory, "www");
            p.setProperty(ATTR_WWW, path.getAbsolutePath());
        }
    }

}
