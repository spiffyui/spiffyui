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
 * @goal rev-info
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
    private MavenProject m_project;

    /**
     * The maven project.
     * 
     * @parameter expression="${basedir}"
     * @required
     * @readonly
     */
    private File m_basedir;
    
    public void setBasedir(File basedir)
    {
        m_basedir = basedir;
    }
    
    public void setProject(MavenProject project)
    {
        m_project = project;
    }

    @Override
    public void execute()
        throws MojoExecutionException,
            MojoFailureException
    {
        Properties p = m_project.getProperties();

        try {
            RevisionInfoBean revInfo = RevisionInfoUtil.getRevisionInfo(m_basedir);

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
