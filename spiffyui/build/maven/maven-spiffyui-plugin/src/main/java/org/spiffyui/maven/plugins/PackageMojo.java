package org.spiffyui.maven.plugins;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.tar.TarArchiver;
import org.codehaus.plexus.archiver.tar.TarArchiver.TarCompressionMethod;

/**
 * Goal which packages a spiffy-ui client into an archive
 * 
 * @goal package
 * 
 * @phase package
 */
public class PackageMojo extends AbstractMojo {

	/**
	 * The maven project.
	 * 
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	protected MavenProject project;

	/**
	 * The directory to archive
	 * 
	 * @parameter 
	 *            expression="${project.build.directory}/${project.build.finalName}"
	 * @required
	 */
	private File m_directory;

	/**
	 * Location of the file.
	 * 
	 * @parameter expression="${project.build.directory}"
	 * @required
	 */
	private File m_outputDirectory;

	/**
	 * Name of archive
	 * 
	 * @parameter expression="${project.build.finalName}.spiffyui"
	 * @required
	 */
	private String m_archiveName;

	/**
	 * Whether creating the archives should be forced.
	 * 
	 * @parameter expression="${spiffyui.forceCreation}" default-value="false"
	 */
	private boolean m_forceCreation;

	/**
	 * The archiver.
	 * 
	 * @parameter 
	 *            expression="${component.org.codehaus.plexus.archiver.Archiver#tar}"
	 * @required
	 */
	private TarArchiver m_archiver;

	public void execute() throws MojoExecutionException {
		validateDirectory();

		File f = m_outputDirectory;

		if (!f.exists()) {
			f.mkdirs();
		}

		File archive = new File(f, m_archiveName);

		try {
			TarCompressionMethod method = new TarCompressionMethod();
			
			method.setValue("bzip2");
			
			m_archiver.setCompression(method);
			m_archiver.setForced(m_forceCreation);
			m_archiver.setDestFile(archive);
			m_archiver.addDirectory(m_directory);
			m_archiver.createArchive();
		} catch (Exception e) {
			throw new MojoExecutionException("Archive creation exception "
					+ e.getMessage());
		}

		project.getArtifact().setFile(archive);
	}

	/**
	 * Directory is valid if it exists, does not represent a file, and can be
	 * read.
	 */
	private void validateDirectory() throws MojoExecutionException {
		if (m_directory == null) {
			throw new IllegalArgumentException("Directory should not be null.");
		}
		if (!m_directory.exists()) {
			throw new MojoExecutionException("Directory does not exist: "
					+ m_directory);
		}
		if (!m_directory.isDirectory()) {
			throw new IllegalArgumentException("Is not a directory: "
					+ m_directory);
		}
		if (!m_directory.canRead()) {
			throw new IllegalArgumentException("Directory cannot be read: "
					+ m_directory);
		}
	}

}
