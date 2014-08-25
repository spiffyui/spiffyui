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
package org.spiffyui.server.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

/**
 * Filter that will substitute the various markers in retrieved
 * xml and jnlp files with values retrieved from the servlet
 * context.
 */
public abstract class GWTLocaleBundleFilter extends GWTLocaleFilter
{

    /**
     * This is the map of files and locales.  There is a map for each resource name with
     * the key of the locale and value of the file name.  We cache the results on the first
     * request so we only have to build the map once.
     */
    private static final Map<String, Map<Locale, String>> RESOURCES = new HashMap<String, Map<Locale, String>>();
    
    private static final List<String> ALL_RESOURCES = new ArrayList<String>();
    
    @Override
    public void init(final FilterConfig filterConfig)
    {
        populateMap(filterConfig.getServletContext(), getResourcePath());
    }
    
    /**
     * Get the path to the localization resources to determine the supported locales from
     * 
     * @return the localization path
     */
    protected abstract String getResourcePath();

    /**
     * Get the list of locales supported for this application.
     * 
     * @return the list of supported locales
     */
    @Override
    protected List<Locale> getMinimumSupportedLocales()
    {
        Map<Locale, String> map = null;

        for (String file : RESOURCES.keySet()) {
            Map<Locale, String> m = RESOURCES.get(file);

            if (map == null) {
                map = m;
            } else if (m.size() > 1 && m.size() < map.size()) {
                map = m;
            }
        }
        
        if (map == null) {
            throw new IllegalArgumentException("No files found at " + getResourcePath());
        }

        ArrayList<Locale> locales = new ArrayList<Locale>();

        for (Locale l : map.keySet()) {
            locales.add(l);
        }

        return locales;
    }

    private static Map<Locale, String> getMap(String fileName)
    {
        Map<Locale, String> map = RESOURCES.get(fileName);
        if (map == null) {
            map = new HashMap<Locale, String>();
            RESOURCES.put(fileName, map);
        }

        return map;
    }

    private static synchronized void populateMap(ServletContext context, String resourcePath)
    {
        if (RESOURCES.size() > 0) {
            /*
             * Then we have already populated the map
             */
            return;
        }

        Set<String> set = context.getResourcePaths(resourcePath);
        Iterator<String> iter = set.iterator();
        while (iter.hasNext()) {
            String file = iter.next().toString();
            file = file.substring(file.lastIndexOf('/') + 1);
            
            ALL_RESOURCES.add(file);

            /*
             * At this point the file should look like this:
             * UIStrings.properties or like this UIStrings_fr.properties.
             */
            int dash = file.indexOf('_');

            if (dash == -1) {
                /*
                 * The file with no locale is English.
                 */
                if (file.indexOf('.') > -1) {
                    getMap(file.substring(0, file.indexOf('.'))).put(new Locale("en"), file);
                }
                continue;
            }

            String fileName = file.substring(0, dash);
            String language = null;
            String country = null;

            int index = dash + 1;
            int dashCount = 0;

            for (int i = dash + 1; i < file.length(); i++) {
                char c = file.charAt(i);

                if (c == '-' || c == '_') {
                    if (dashCount == 0) {
                        /*
                         * Then we are after the first dash and before the second
                         * one.  That makes this the language code.
                         */
                        language = file.substring(index, i);
                    } else {
                        /*
                         * This means the file has more than two dashes.  That means
                         * we don't know hoe to deal with it and we will ignore it.
                         */

                        fileName = null;
                        break;
                    }

                    index = i + 1;
                    dashCount++;
                } else if (c == '.') {
                    if (dashCount == 0) {
                        /*
                         * Then we are after the first dash and before the ending
                         * dot.  That makes this the language code in a file with 
                         * no country code.
                         */
                        language = file.substring(index, i);
                    } else if (dashCount == 1) {
                        /*
                         * Then we are after the second dash and before the ending
                         * dot.  That makes this the country code
                         */
                        country = file.substring(index, i);
                    }

                    /*
                     * Either way, when we hit the dot we are done with 
                     * the file.
                     */

                    break;
                }
            }
            
            if (fileName == null) {
                continue;
            } 
            
            if (language != null) {
                if (country == null) {
                    getMap(fileName).put(new Locale(language), file);
                } else {
                    getMap(fileName).put(new Locale(language, country), file);
                }
            }
        }
    }
}

