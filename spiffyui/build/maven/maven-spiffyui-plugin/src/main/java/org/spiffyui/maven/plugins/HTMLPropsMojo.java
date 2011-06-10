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
    private MavenProject project;

    /**
     * Location of the file.
     * 
     * @parameter expression="${project.build.directory}/htmlprops"
     * @required
     */
    private File outputDirectory;

    /**
     * The name to give to the generated package
     * 
     * @parameter 
     *            expression="${project.groupId}.${project.artifactId}.htmlprops"
     * @required
     */
    private String packageName;

    /**
     * @parameter expression="src/main/html"
     * @required
     */
    private File sourceDirectory;

    /**
     * The name to give to the generated interface
     * 
     * @parameter expression="SpiffyUiHtmlProps"
     * @required
     */
    private String interfaceName;
    
    @Override
    public void execute()
        throws MojoExecutionException,
            MojoFailureException
    {
        Log log = getLog();

        if (!sourceDirectory.exists()) {
            log.debug("HTMLPROPS: " + sourceDirectory.getAbsolutePath() + " does not exist.  Skipping");
            return;
        }

        /*
         * normalize the packageName string into an java package safe variant
         * (e.g. no dashes)
         */
        String safePackageName = packageName.replace("-", "_");
        File packageDir = new File(outputDirectory, safePackageName.replace(".", File.separator));
        if (!packageDir.exists()) {
            packageDir.mkdirs();
        }

        File outputFile = new File(packageDir, interfaceName + ".properties");

        log.info("HTMLPROPS: Generating " + packageName);

        String[] exts = new String[] {
            "html"
        };
        List<File> files = new ArrayList<File>(FileUtils.listFiles(sourceDirectory, exts, true));

        try {

            if (!outputDirectory.exists()) {
                FileUtils.forceMkdir(outputDirectory);
            }

            HTMLPropertiesUtil.generatePropertiesFiles(files, outputFile, safePackageName);
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage());
        }

        project.getProperties().put("spiffyui.htmlprops.path", outputDirectory.getAbsolutePath());

    }

}
