package org.spiffyui.maven.plugins;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.spiffyui.build.GzipListUtil;

/**
 * Goal which gzip compresses specific artifacts
 * 
 * @goal gzip
 * 
 * @phase process-classes
 */
public class GZipMojo extends AbstractMojo {

    /**
     * {@link MavenProject} to process.
     * 
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;
    
	/**
	 * Location of the file.
	 * 
	 * @parameter expression="${project.build.outputDirectory}"
	 * @required
	 */
	private File directory;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
        Properties p = project.getProperties();
        File module = new File(p.getProperty("spiffyui.gwt.module.path"));
        File images = new File(module, "images");
        String[] baseExts = {"css", "png", "gif", "js"};
        String[] allExts = {"html", "css", "png", "gif", "js"};
        
	    gzip(directory, baseExts);
	    gzip(module, allExts);
	    gzip(images, allExts);
	}
	
	private void gzip(File path, String[] exts) throws MojoExecutionException, MojoFailureException {
        List<File> files = new ArrayList<File>(FileUtils.listFiles(path, exts, false));
        Log log = getLog();
        
        log.info("GZIP: Compressing artifacts from " + path);
        for(File file : files)
            log.debug("GZIP: " + file.toString());
        
        try {
        	GzipListUtil.zipFileList(files, path);
        } catch(IOException e) {
        	throw new MojoExecutionException(e.getMessage());
        }
	}

}
