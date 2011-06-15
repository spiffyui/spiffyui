package org.spiffyui.maven.plugins;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Aggregates all of the necessary steps to compile a spiffyui client application into one goal.
 * 
 * @goal spiffyui-client
 * @execute lifecycle="spiffyui-client" phase="compile"
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
