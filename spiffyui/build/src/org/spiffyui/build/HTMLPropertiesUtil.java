/*******************************************************************************
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
package org.spiffyui.build;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Properties;


/**
 * This is generic task for generating HTML properties files
 */
public final class HTMLPropertiesUtil
{
    private HTMLPropertiesUtil()
    {
        /*
         Our constructor is private
         */
    }
    
    /**
     * We need to figure out the locale of the file based on the file name.  The file can be
     * something like foo.html, foo_fr.html foo_fr-FR.html, or foo_fr_FR.html.  We parse that
     * name and create a locale object based on the language and country codes in the name
     * 
     * @param name   the name of the file
     * 
     * @return the locale for this file name
     */
    private static Locale findLocale(String name)
    {
        if (name == null || name.length() < 4) {
            return null;
        }
        
        name = name.substring(0, name.lastIndexOf('.'));
        String country = null;
        String language = null;
        
        if (name.length() > 7 && 
            (name.charAt(name.length() - 6) == '-' ||
            name.charAt(name.length() - 6) == '_')) {
            language = name.substring(name.length() - 5,
                                      name.length() - 3);
        }
        
        if (name.length() > 4 && 
            (name.charAt(name.length() - 3) == '-' ||
            name.charAt(name.length() - 3) == '_')) {
            if (language == null) {
                /*
                 Then they specified a language without a country
                 */
                language = name.substring(name.length() - 2,
                                          name.length());
            } else {
                /*
                 Then we already have the language and this is the country
                 */
                country =  name.substring(name.length() - 2,
                                          name.length());
            }
        }
        
        if (language == null && country == null) {
            /*
             In this case there was no locale information and we
             return null to indicate this is the default locale.
             */
            return null;
        } else if (country == null) {
            /*
             In this case they have a language code, but no country
             code.  This means we want a locale like French or English
             with no country code.
             */
            return new Locale(language);
        } else {
            /*
             This case is where there was a language and a country
             */
            return new Locale(language, country);
        }
    }
    
    private static Properties getProperties(Locale locale, HashMap<String, Properties> props)
    {
        String key = null;
        if (locale == null) {
            key = "";
        } else {
            key = "_" + locale.toString();
        }
        
        if (!props.containsKey(key)) {
            props.put(key, new Properties());
        }
        
        return props.get(key);
    }
    
    /**
     * Generate the HTML properties file based on the input source files
     * 
     * @param files  the source files
     * @param destinationFile
     *               the destination properties file
     * @param packageName
     *               the name of the package for the Java file.  If the package
     *               isn't specified the Java file won't be generated.
     * 
     * @throws IOException
     *                if there is an error writing the file
     */
    public static void generatePropertiesFiles(final List<File> files, final File destinationFile, final String packageName)
        throws IOException
    {
        if (destinationFile == null) {
            throw new IllegalArgumentException("Must specify a Properties file destination");
        }
        
        if (files == null ||
            files.size() == 0) {
            /*
             If there are no input files then there is nothing to do
             */
            return;
        }
        
        HashMap<String, Properties> props = new HashMap<String, Properties>();
        ArrayList<String> methods = new ArrayList<String>();
        
        for (File f : files) {
            Reader in = null;
            try {
                in = new InputStreamReader(new FileInputStream(f), "UTF-8");
                StringBuffer sb = new StringBuffer();
                int c = -1;
                while ((c = in.read()) > -1) {
                    if (c == '\'') {
                        sb.append("&#39;");
                    } else if (c == '{' ||
                               c == '}') {
                        sb.append('\'');
                        sb.append((char) c);
                        sb.append('\'');
                    } else {
                        sb.append((char) c);
                    }
                }
                
                Locale loc = findLocale(f.getName());
                String name = f.getName();
                if (loc != null) {
                    /*
                     Then we want to take the locale out of the file name
                     */
                    name = name.substring(0, name.lastIndexOf('.') - loc.toString().length() - 1) + 
                        name.substring(name.lastIndexOf('.'), name.length());
                }
                
                if (!methods.contains(name)) {
                    methods.add(name);
                }
                
                getProperties(loc, props).setProperty(name.replace(' ', '_').replace('.', '_'), sb.toString());
                
            } finally {
                if (in != null) {
                    in.close();
                }
            }
            
            if (packageName != null) {
                File javaFile = new File(destinationFile.getParentFile(),
                                         destinationFile.getName().substring(0, destinationFile.getName().lastIndexOf('.')) + ".java");
                
                generateJavaFile(methods, javaFile, packageName);
            }
        }
        
        /*
         Now we need to write out all the properties files
         */
        for (String loc : props.keySet()) {
            PrintWriter out = null;
            try {
                String name = destinationFile.getName();
                name = name.substring(0, name.lastIndexOf('.')) + loc + 
                    name.substring(name.lastIndexOf('.'), name.length());
                
                out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(
                    new File(destinationFile.getParentFile(), name)), "UTF-8"));
                props.get(loc).store(out, "");
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        }
        
    }
    
    private static final String START = 
        "import com.google.gwt.i18n.client.Messages;\n\n" + 
        "/**\n" + 
        " * This is a generated localized message bundle for accessing HTML string\n" + 
        " *\n" + 
        " */\n" + 
        "public interface ";
    
    /**
     * The message bundle we need has a set of properties files and a single Java file.  
     * This method handles generating that Java file with a separate method for each 
     * properties file.
     * 
     * @param methods the list of methods to add
     * @param destinationFile
     *                the destination file to write the Java file to
     * @param packageName
     *                the package name of the Java file
     * 
     * @throws IOException if there is an exception writing the file
     */
    private static void generateJavaFile(final List<String> methods, final File destinationFile, final String packageName)
        throws IOException
    {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(destinationFile), "UTF-8"));
            
            /*
             Output the package name
             */
            out.write("package " + packageName + ";\n\n");
            
            /*
             Write out the import and the class declaration
             */
            out.write(START);
            
            /*
             Now the class name
             */
            out.write(destinationFile.getName().substring(0, destinationFile.getName().lastIndexOf('.')));
            out.write(" extends Messages {\n");
            
            /*
             Write out a method for each file
             */
            for (String m : methods) {
                out.write("    public String " + m.replace('.', '_') + "();\n");
            }
            
            /*
             Write out the ending curly brace
             */
            out.write("}\n");
            
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
