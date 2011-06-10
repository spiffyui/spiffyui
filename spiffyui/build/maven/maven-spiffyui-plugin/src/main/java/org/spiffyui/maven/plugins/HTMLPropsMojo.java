package org.spiffyui.maven.plugins;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.spiffyui.build.HTMLPropertiesUtil;

/**
 * Goal which compiles various HTML files into something consumable by GWT
 * 
 * @goal htmlprops
 * @phase generate-resources
 */
public class HTMLPropsMojo extends AbstractMojo
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
     * Location of the file.
     * 
     * @parameter expression="${project.build.directory}/htmlprops"
     * @required
     */
    private File m_outputDirectory;

    /**
     * The name to give to the generated package
     * 
     * @parameter 
     *            expression="${project.groupId}.${project.artifactId}.htmlprops"
     * @required
     */
    private String m_packageName;

    /**
     * @parameter expression="src/main/html"
     * @required
     */
    private File m_sourceDirectory;

    /**
     * The name to give to the generated interface
     * 
     * @parameter expression="SpiffyUiHtmlProps"
     * @required
     */
    private String m_interfaceName;
    
    public void setProject(MavenProject project)
    {
        m_project = project;
    }

    @Override
    public void execute()
        throws MojoExecutionException,
            MojoFailureException
    {
        Log log = getLog();

        if (!m_sourceDirectory.exists()) {
            log.debug("HTMLPROPS: " + m_sourceDirectory.getAbsolutePath() + " does not exist.  Skipping");
            return;
        }

        /*
         * normalize the packageName string into an java package safe variant
         * (e.g. no dashes)
         */
        String safePackageName = m_packageName.replace("-", "_");
        File packageDir = new File(m_outputDirectory, safePackageName.replace(".", File.separator));
        if (!packageDir.exists()) {
            packageDir.mkdirs();
        }

        File outputFile = new File(packageDir, m_interfaceName + ".properties");

        log.info("HTMLPROPS: Generating " + m_packageName);

        String[] exts = new String[] {
            "html"
        };
        List<File> files = new ArrayList<File>(FileUtils.listFiles(m_sourceDirectory, exts, true));

        try {

            if (!m_outputDirectory.exists()) {
                FileUtils.forceMkdir(m_outputDirectory);
            }

            HTMLPropertiesUtil.generatePropertiesFiles(files, outputFile, safePackageName);
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage());
        }

        m_project.getProperties().put("spiffyui.htmlprops.path", m_outputDirectory.getAbsolutePath());

    }

}
