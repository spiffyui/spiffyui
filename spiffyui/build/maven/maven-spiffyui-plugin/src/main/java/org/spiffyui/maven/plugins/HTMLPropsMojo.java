/*******************************************************************************
 * 
 * Copyright 2011-2014 Spiffy UI Team   
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/
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
 * Invokes the HTML-Properties compiler for the project .html source
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
     * Location where generated source will be written.
     * 
     * @parameter expression="${spiffyui.generated-source}"
     * @required
     */
    private File outputDirectory;
    
    /**
     * The output directory into which to copy the resources.
     * 
     * @parameter default-value="${project.build.outputDirectory}"
     * @required
     */
    private File classesOutputDirectory;

    /**
     * The name to give to the generated package
     * 
     * @parameter expression="${spiffyui.htmlprops.package}"
     *            default-value="${spiffyui.gwt.module.package}"
     * @required
     */
    private String packageName;

    /**
     * Path to the project .html files to process
     * 
     * @parameter default-value="src/main/html"
     * @required
     */
    private File sourceDirectory;

    /**
     * The name to give to the generated interface
     * 
     * @parameter expression="${spiffyui.htmlprops.interfacename}
     *            default-value="SpiffyUiHtml"
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
        
        try {
            /*
             First we generate the properties files and the Java file into the generated
             sources directory so they can be used in the compiler.
             */
            generateProps(new File(outputDirectory, safePackageName.replace(".", File.separator)), safePackageName);
            
            /*
             Then we call it a second time generating just the properties files for
             runtime locale resolution.
             */
            generateProps(new File(outputDirectory, safePackageName.replace(".", File.separator)), null);
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage());
        }
    }
    
    private void generateProps(File packageDir, String safePackageName)
        throws IOException
    {
        if (!packageDir.exists()) {
            packageDir.mkdirs();
        }

        File outputFile = new File(packageDir, interfaceName + ".properties");
        
        getLog().debug("HTMLPROPS: Generating " + safePackageName);

        String[] exts = new String[] {
            "html"
        };
        List<File> files = new ArrayList<File>(FileUtils.listFiles(sourceDirectory, exts, true));

        if (!outputDirectory.exists()) {
            FileUtils.forceMkdir(outputDirectory);
        }

        HTMLPropertiesUtil.generatePropertiesFiles(files, outputFile, safePackageName);
    }
}
