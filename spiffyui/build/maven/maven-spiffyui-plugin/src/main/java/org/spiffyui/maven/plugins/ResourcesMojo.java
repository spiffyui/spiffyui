package org.spiffyui.maven.plugins;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.filtering.MavenFilteringException;
import org.apache.maven.shared.filtering.MavenResourcesExecution;
import org.apache.maven.shared.filtering.MavenResourcesFiltering;

/**
 * Goal which determines various revision details about the build
 * 
 * @goal resources
 * @phase process-resources
 */
public class ResourcesMojo extends AbstractMojo
{
    /**
     * The character encoding scheme to be applied when filtering resources.
     * 
     * @parameter expression="${encoding}"
     *            default-value="${project.build.sourceEncoding}"
     */
    protected String encoding;

    /**
     * The source directory
     * 
     * @parameter default-value="src/main/resources"
     * @required
     */
    private File sources;

    /**
     * The output directory into which to copy the resources.
     * 
     * @parameter default-value="${spiffyui.www}"
     * @required
     */
    private File outputDirectory;

    /**
     * @parameter default-value="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;

    /**
     * @parameter default-value="${session}"
     * @readonly
     * @required
     */
    protected MavenSession session;

    /**
     * @component 
     *            role="org.apache.maven.shared.filtering.MavenResourcesFiltering"
     *            role-hint="default"
     * @required
     */
    protected MavenResourcesFiltering filtering;

    public void execute()
        throws MojoExecutionException
    {
        try {
            Resource resource = new Resource();

            resource.setDirectory(sources.getAbsolutePath());
            resource.setFiltering(true);

            List<Resource> resources = new ArrayList<Resource>(Arrays.asList(resource));

            MavenResourcesExecution exe = new MavenResourcesExecution(resources, outputDirectory, project, encoding, Collections.EMPTY_LIST,
                Collections.EMPTY_LIST, session);

            exe.setOverwrite(true);
            filtering.filterResources(exe);
        } catch (MavenFilteringException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }

    }

}
