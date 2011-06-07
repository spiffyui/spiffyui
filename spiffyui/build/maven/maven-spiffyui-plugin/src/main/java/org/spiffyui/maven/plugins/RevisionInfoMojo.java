package org.spiffyui.maven.plugins;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.tools.ant.BuildException;
import org.spiffyui.build.RevisionInfoBean;
import org.spiffyui.build.RevisionInfoUtil;


/**
 * Goal which determines various revision details about the build
 * 
 * @goal build-info
 * 
 * @phase initialize
 */
public class RevisionInfoMojo extends AbstractMojo
{
    /**
     * The maven project.
     * 
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;

    /**
     * The maven project.
     * 
     * @parameter expression="${basedir}"
     * @required
     * @readonly
     */
    protected File basedir;

    @Override
    public void execute()
        throws MojoExecutionException,
            MojoFailureException
    {
        Properties p = project.getProperties();
        
        try {
            RevisionInfoBean revInfo = RevisionInfoUtil.getRevisionInfo(basedir);
            
            getLog().debug("revInfo.getRevNumber(): " + revInfo.getRevNumber());
            
            p.setProperty("revision.number", revInfo.getRevNumber());
            p.setProperty("revision.date", revInfo.getRevDate());
        } catch (IOException ioe) {
            throw new BuildException(ioe);
        } catch (InterruptedException ie) {
            throw new BuildException(ie);
        }

    }

}
