package org.spiffyui.maven.plugins;

import java.io.File;
import java.util.Properties;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.mojo.gwt.GwtModule;
import org.codehaus.mojo.gwt.shell.CompileMojo;

/**
 * @author ghaskins
 * @extendsPlugin gwt-maven-plugin
 * @goal compile
 * @phase compile
 */
public class SpiffyCompileMojo extends CompileMojo
{

    public void doExecute( ) throws MojoExecutionException, MojoFailureException
    {
        String[] modules = this.getModules();
        
        /* ensure there is only one module, and record it for posterity */
        switch (modules.length) {
            case 0:
                throw new MojoExecutionException("No GWT modules detected");
            case 1:
                try {
                    GwtModule module = readModule(modules[0]);
                    File path = new File(getOutputDirectory(), module.getPath()); 
                    Properties p = getProject().getProperties();
                    p.setProperty("spiffyui.gwt.module.name", module.getName());
                    p.setProperty("spiffyui.gwt.module.path", path.getAbsolutePath());
                } catch (Exception e) {
                    throw new MojoExecutionException(e.getMessage());
                }
                break;
            default:    
                throw new MojoExecutionException("Only one GWT module allowed, but "
                    + modules.length + " detected: " + modules);
        } 
        
        super.doExecute();
    }
}
