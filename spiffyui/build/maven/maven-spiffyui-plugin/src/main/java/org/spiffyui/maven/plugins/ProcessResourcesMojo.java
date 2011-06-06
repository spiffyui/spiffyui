package org.spiffyui.maven.plugins;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * No-op goal which forks a parallel lifecycle
 * @goal resources
 * @execute lifecycle="spiffyui" phase="process-resources"
 */
public class ProcessResourcesMojo extends AbstractMojo
{

    @Override
    public void execute()
        throws MojoExecutionException,
            MojoFailureException
    {
        // No-op (has lifecycle side effect)
    }

}
