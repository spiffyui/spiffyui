package org.spiffyui.maven.plugins;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Forks a spiffui-client build lifecycle.
 * @goal spiffyui-client
 * @execute lifecycle="spiffyui-client" phase="prepare-package"
 */
public class ClientBuildMojo extends AbstractMojo
{

    @Override
    public void execute()
        throws MojoExecutionException,
            MojoFailureException
    {
        // no-op, we use the lifecycle fork above to do the work

    }

}
