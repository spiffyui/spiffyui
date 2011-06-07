package org.spiffyui.maven.plugins;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.json.JSONObject;

/**
 * Goal which determines various revision details about the build
 * 
 * @goal build-info
 * @phase generate-resources
 */
public class BuildInfoMojo extends AbstractMojo
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
     * The character encoding scheme to be applied exporting the JSON document
     *
     * @parameter expression="${encoding}" default-value="UTF-8"
     */
    protected String encoding;
    
    /**
     * The character encoding scheme to be applied exporting the JSON document
     *
     * @parameter expression="${dateFormat}" default-value="yyyy.MMMMM.dd hh:mm aaa"
     */
    protected String dateFormat;
    
    /**
     * Location of the file.
     * 
     * @parameter expression="${project.build.outputDirectory}/build-info.json"
     * @required
     */
    private File outputFile;
    
    /**
     * The base directory of this project
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
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String date = sdf.format(cal.getTime());
        
        try {
            File parent = outputFile.getParentFile();
            if (!parent.exists())
                FileUtils.forceMkdir(parent);
            
            // Create file
            PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile), encoding));
            
            JSONObject info = new JSONObject();
            info.put("build.date", date);
            info.put("build.user", System.getProperties().get("user.name"));
            //info.put("build.version", version);
            info.put("build.revision", p.getProperty("revision.number"));
            info.put("build.revision.date", p.getProperty("revision.date"));
            info.put("build.dir", basedir.getAbsolutePath());
 
            out.write(info.toString());
            // Close the output stream
            out.close();
        } catch (Exception e) {// Catch exception if any
            throw new MojoExecutionException(e.getMessage());
        }

    }

}
