/*******************************************************************************
 *
 * Copyright 2011 Spiffy UI Team   
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
package org.spiffyui.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * The JSLocaleUtil can handle finding the right filename of a JavaScript
 * library for a given locale.
 */
public final class JSLocaleUtil 
{
    
    private JSLocaleUtil()
    {
        /*
         * no-op
         */
    }

    /**
     * This is the map of files and locales.  There is a map for each resource name with
     * the key of the locale and value of the file name.  We cache the results on the first
     * request so we only have to build the map once.
     */
    private static final Map<String, Map<Locale, String>> RESOURCES = new HashMap<String, Map<Locale, String>>();
    
    private static final List<String> ALL_RESOURCES = new ArrayList<String>();
    
    private static final List<Locale> MINIMUM_LOCALES = new ArrayList<Locale>();

    /**
     * Get the right file name for the specified resource name and the locale
     * (which was already determined to be the best match locale).
     * 
     * @param resourceName
     *                The resource name.  If your looking for a file like date-en-US.js the the resource name is date
     * @param locale  The locale of the requested file
     * @param context the servlet context
     * 
     * @return the file name
     * @exception ServletException
     *                   if there is an error accessing the servlet context
     */
    public static String getFile(String resourceName, Locale locale, ServletContext context)
        throws ServletException
    {
        populateMap(context);

        String resource = resourceName + "-" + locale.toString() + ".js";
        
        if (ALL_RESOURCES.contains(resource)) {
            return resource;
        } else if (ALL_RESOURCES.contains(resource.replace('_', '-'))) {
            /*
             * I wish everyone would agree if it is en-US or en_US
             */
            return resource.replace('_', '-');
        } else if (ALL_RESOURCES.contains(resourceName + "-" + locale.getLanguage() + ".js")) {
            /*
             * We don't have files for every language and country combination
             * so we'll just try the language without the country if we don't
             * have a file for the full locale.
             */
            return resourceName + "-" + locale.getLanguage() + ".js";
        } else {
            /*
             There is the possibility that the browser is asking for a language
             without a country and we only have the file with a language and a
             country.
     
             For example, the user could request a Finnish file as fi which
             would result in a resource like date-fi.js when we only have
             date-fi-FI.js.  In this case we will find the first file that matches
             the language code with any country.
             */
            for (String f : ALL_RESOURCES) {
                if (f.startsWith(resourceName + "-" + locale.getLanguage())) {
                    return f;
                }
            }
            
            /*
             If we don't have the file that we want then we return English
             */
            return resourceName + "-en-US.js";
        }
    }

    private static synchronized void populateMap(ServletContext context)
    {
        if (RESOURCES.size() > 0) {
            /*
             * Then we have already populated the map
             */
            return;
        }

        Set set = context.getResourcePaths("/js/lib/i18n");
        Iterator iter = set.iterator();
        while (iter.hasNext()) {
            String file = iter.next().toString();
            if (file.endsWith(".js.gz")) {
                continue;
            }
            file = file.substring(file.lastIndexOf('/') + 1);
            
            ALL_RESOURCES.add(file);

            /*
             * At this point the file should look like this:
             * date-en-JM.js or like this date-en.js.
             */

            int dash = file.indexOf('-');

            String fileName = file.substring(0, dash);
            String language = null;
            String country = null;

            int index = dash + 1;
            int dashCount = 0;

            for (int i = dash + 1; i < file.length(); i++) {
                char c = file.charAt(i);

                if (c == '-') {
                    if (dashCount == 0) {
                        /*
                         * Then we are after the first dash and before the second
                         * one.  That makes this the language code.
                         */
                        language = file.substring(index, i);
                    } else {
                        /*
                         * This means the file has more than two dashes.  That means
                         * we don't know how to deal with it and we will ignore it.
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
            } else if (country == null) {
                getMap(fileName).put(new Locale(language), file);
            } else {
                getMap(fileName).put(new Locale(language, country), file);
            }
        }
    }

    /**
     * Get the list of minimum supported locales.  This list is the smallest number of
     * locales supported by one of the JavaScript files is the js/lib/i18n directory.
     * 
     * @param context the servlet context for loading the available locales
     * 
     * @return The minimum list of supported locales
     */
    public static List<Locale> getMinimumSupportedLocales(ServletContext context)
    {
        if (MINIMUM_LOCALES.size() == 0) {
            synchronized (MINIMUM_LOCALES) {
                calculateMinimumSupportedLocales(context);
            }
        }
        
        /*
         The minimum list of locale doesn't change so we just return the
         cached version. 
         */
        return MINIMUM_LOCALES;
        
    }
    
    private static void calculateMinimumSupportedLocales(ServletContext context)
    {
        populateMap(context);
        Map<Locale, String> map = null;

        for (String file : RESOURCES.keySet()) {
            Map<Locale, String> m = RESOURCES.get(file);

            if (map == null) {
                map = m;
            } else if (m.size() > 1 && m.size() < map.size()) {
                /*
                 This method is looking for the minimum supported locales.  That means
                 we want to find the file with the smallest number of supported locales
                 and use the list of locales it supports.
                 */
                map = m;
            }
        }

        if (map == null) {
            /*
             This means we couldn't load the files.  This is often
             caused by a configuration problem, but it is tricky for
             us to figure out what to do here.  The best we can do
             is return an empty list and let the servlet return a
             404.
             */
            return;
        }

        for (Locale l : map.keySet()) {
            MINIMUM_LOCALES.add(l);
        }
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
}
