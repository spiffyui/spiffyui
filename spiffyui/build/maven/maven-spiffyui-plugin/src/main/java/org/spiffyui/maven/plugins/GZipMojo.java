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
	 * Location of the file.
	 * 
	 * @parameter expression="${project.build.outputDirectory}"
	 * @required
	 */
	private File directory;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
        String[] exts = new String[] { "html", "css", "png" };
        List<File> files = new ArrayList<File>(FileUtils.listFiles(directory, exts, false));
        Log log = getLog();
        
        log.info("GZIP: Compressing artifacts from " + directory + ": " + files.toString());
        
        try {
        	GzipListUtil.zipFileList(files, directory);
        } catch(IOException e) {
        	throw new MojoExecutionException(e.getMessage());
        }
	}

}
