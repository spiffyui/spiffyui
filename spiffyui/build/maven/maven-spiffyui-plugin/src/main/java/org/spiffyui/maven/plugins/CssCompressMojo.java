package org.spiffyui.maven.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Collection;

import net_alchim31_maven_yuicompressor.SourceFile;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.IOUtil;

import com.yahoo.platform.yui.compressor.CssCompressor;

/**
 * Goal which executes the YUI Compressor against .css
 * 
 * @goal css-compress
 * @phase process-resources
 */
public class CssCompressMojo extends AbstractMojo
{
    /**
     * The character encoding scheme to be applied exporting the JSON document
     * 
     * @parameter expression="${encoding}" default-value="UTF-8"
     */
    private String encoding;

    /**
     * The output filename suffix.
     * 
     * @parameter expression="${spiffyui.yuicompressor.suffix}"
     *            default-value=".min"
     */
    private String suffix;

    /**
     * Insert line breaks in output after the specified column number.
     * 
     * @parameter expression="${spiffyui.yuicompressor.linebreakpos}"
     *            default-value="0"
     */
    private int linebreakpos;

    /**
     * @parameter expression="src/main/webapp"
     */
    private File sourceDirectory;

    /**
     * @parameter expression="${spiffyui.www}"
     * @required
     */
    private File outputDirectory;

    @Override
    public void execute()
        throws MojoExecutionException,
            MojoFailureException
    {

        String[] exts = new String[] {
            "css"
        };
        Collection<File> files = FileUtils.listFiles(sourceDirectory, exts, true);

        try {
            for (File file : files) {
                compress(file);
            }
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage());
        }
    }

    private void compress(File inFile)
        throws Exception
    {
        String shortname = inFile.getAbsolutePath().substring(sourceDirectory.getAbsolutePath().length());
        SourceFile srcFile = new SourceFile(sourceDirectory, outputDirectory, shortname, false);
        File outFile = srcFile.toDestFile(suffix);

        InputStreamReader in = null;
        OutputStreamWriter out = null;

        try {
            in = new InputStreamReader(new FileInputStream(inFile), encoding);
            out = new OutputStreamWriter(new FileOutputStream(outFile), encoding);

            if (!outFile.getParentFile().exists() && !outFile.getParentFile().mkdirs()) {
                throw new MojoExecutionException("Cannot create resource output directory: " + outFile.getParentFile());
            }

            getLog().info("YUICOMPRESS: " + inFile.getAbsolutePath() + " -> " + outFile.getAbsolutePath());

            try {
                CssCompressor compressor = new CssCompressor(in);
                compressor.compress(out, linebreakpos);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(
                    "Unexpected characters found in CSS file. Ensure that the CSS file does not contain '$', and try again", e);
            }

        } finally {
            IOUtil.close(in);
            IOUtil.close(out);
        }

    }

}
