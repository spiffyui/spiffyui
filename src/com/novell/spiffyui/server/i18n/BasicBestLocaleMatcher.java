/*
 * ========================================================================
 *
 * Copyright (c) 2010 Unpublished Work of Novell, Inc. All Rights Reserved.
 * 
 * THIS WORK IS AN UNPUBLISHED WORK AND CONTAINS CONFIDENTIAL,
 * PROPRIETARY AND TRADE SECRET INFORMATION OF NOVELL, INC. ACCESS TO
 * THIS WORK IS RESTRICTED TO (I) NOVELL, INC. EMPLOYEES WHO HAVE A NEED
 * TO KNOW HOW TO PERFORM TASKS WITHIN THE SCOPE OF THEIR ASSIGNMENTS AND
 * (II) ENTITIES OTHER THAN NOVELL, INC. WHO HAVE ENTERED INTO
 * APPROPRIATE LICENSE AGREEMENTS. NO PART OF THIS WORK MAY BE USED,
 * PRACTICED, PERFORMED, COPIED, DISTRIBUTED, REVISED, MODIFIED,
 * TRANSLATED, ABRIDGED, CONDENSED, EXPANDED, COLLECTED, COMPILED,
 * LINKED, RECAST, TRANSFORMED OR ADAPTED WITHOUT THE PRIOR WRITTEN
 * CONSENT OF NOVELL, INC. ANY USE OR EXPLOITATION OF THIS WORK WITHOUT
 * AUTHORIZATION COULD SUBJECT THE PERPETRATOR TO CRIMINAL AND CIVIL
 * LIABILITY.
 *
 * ========================================================================
 */
package com.novell.spiffyui.server.i18n;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BasicBestLocaleMatcher
{
    private static final String NOVELL_SPIFFY_LOCALE = "Novell_Spiffy_Locale";
    private static final ConcurrentHashMap<String, Locale> LOCALE_CACHE = new ConcurrentHashMap<String, Locale>();
    private static ArrayList<Locale> g_supportedLocales;
    
    static {
        // TODO: replace hard-coded locales below with a REST API call to get the information from the System Preferences
        g_supportedLocales = new ArrayList<Locale>();
        g_supportedLocales.add(Locale.SIMPLIFIED_CHINESE);
        g_supportedLocales.add(Locale.TRADITIONAL_CHINESE);
        g_supportedLocales.add(Locale.ENGLISH);
        g_supportedLocales.add(new Locale("da")); // Danish
        g_supportedLocales.add(new Locale("nl")); // Dutch
        g_supportedLocales.add(Locale.FRENCH);
        g_supportedLocales.add(Locale.GERMAN);
        g_supportedLocales.add(Locale.ITALIAN);
        g_supportedLocales.add(Locale.JAPANESE);
        g_supportedLocales.add(new Locale("pt")); // Portuguese
        g_supportedLocales.add(new Locale("ru")); // Russian
        g_supportedLocales.add(new Locale("es")); // Spanish
        g_supportedLocales.add(new Locale("sv")); // Swedish
    }
    
    /**
     * Return the best match locale by getting a list of requested locales from the request.
     * It will loop through the list of locales and attempt to match first exactly, then by language and country, then by country.
     * If no match is found it will default to English.
     * @param request - the HttpServletRequest
     * @param response - the HttpServletResponse
     * @param context - the ServletContext
     * @return the best match Locale
     * @throws ServletException
     */
    public static Locale getBestMatchLocale(HttpServletRequest request, HttpServletResponse response, ServletContext context)
            throws ServletException
    {
        // Use the Novell_Spiffy_Locale cookie if it exists -- but where is this cookie set???
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(NOVELL_SPIFFY_LOCALE)) {
                    return getLocaleFromCode(cookie.getValue());
                }
            }
        }

        // If the Locale wasn't found yet, use the browser's locale list to find the best match
        @SuppressWarnings("unchecked")
        ArrayList<Locale> requestLocales = Collections.list(request.getLocales());
        //loop through the list and see if its a supported locale by 
        //checking lang, country, variant
        //then by checking lang, country
        //then by lang
        //then by next locale
        for (Locale requestLocale : requestLocales) {
            Locale matchLocale = matchSupportedLocale(requestLocale);
            if (matchLocale != null) {
                return matchLocale;
            }
        }
        //nothing found, go with application default
        //but here we'll assume no application default is defined anywhere, so just go with en
        return Locale.ENGLISH;
    }
    
    private static Locale matchSupportedLocale(Locale loc)
    {
        for (Locale supportedLocale : g_supportedLocales) {
            if (supportedLocale.equals(loc)) {
                //exact match found
                return supportedLocale;
            }
        }
        
        String locStr = loc.toString();
        int locStrLen = locStr.length();
        //bust on exact match, if it's lang-country-variant, try matching on just lang-country
        if (locStrLen >= 5) {
            for (Locale supportedLocale : g_supportedLocales) {
                if (supportedLocale.getLanguage().equals(loc.getLanguage()) && 
                        supportedLocale.getCountry().equals(loc.getCountry())) {
                    //lang-country found
                    return supportedLocale;
                }
            }
        }
        
        //bust on lang-country, try matching on just lang
        for (Locale supportedLocale : g_supportedLocales) {
            if (supportedLocale.getLanguage().equals(loc.getLanguage())) {
                //lang found
                return supportedLocale;
            }
        }
        
        //nothing found, return null
        return null;
    }

    /**
     * Creates a Locale object from the xml:lang attribute format or the Java standard (as returned by Locale.toString()) for specifying locales.
     *
     * @param localeCode String code that conforms to the RFC 3066 standard or the Java standard (as returned by Locale.toString()).
     * @return Locale object converted from the localeCode passed in.
     */
    public static Locale getLocaleFromCode(final String localeCode)
    {
        if (localeCode == null || localeCode.length() == 0) {
            throw new IllegalArgumentException("Null or empty localeCode passed");
        }
        if (LOCALE_CACHE.containsKey(localeCode)) {
            return LOCALE_CACHE.get(localeCode);
        } else {

            Locale locale;
            String locStr;
            String ctryCd = null;
            String variant;
            switch (localeCode.length()) {
                case 2:
                    locStr = localeCode;
                    locale = new Locale(locStr.toLowerCase());
                    break;
                case 5:
                    locStr = localeCode.substring(0, 2);
                    ctryCd = localeCode.substring(3, 5);
                    locale = new Locale(locStr.toLowerCase(), ctryCd.toUpperCase());
                    break;
                default:
                    locStr = localeCode.substring(0, 2);
                    ctryCd = localeCode.substring(3, 5);
                    variant = localeCode.substring(6);
                    locale = new Locale(locStr.toLowerCase(), ctryCd.toUpperCase(), variant);
            }
            LOCALE_CACHE.putIfAbsent(localeCode, locale);
//            Locale oldLoc = LOCALE_CACHE.putIfAbsent(localeCode, locale);
//            if (oldLoc != null && !locale.equals(oldLoc)) {
//                @SuppressWarnings("ThrowableInstanceNeverThrown")
//                LocalizationException le = new LocalizationException(LocalizationRsrc.BUNDLE_ID, LocalizationRsrc.ERROR_LOCALE_MAPPING,
//                        localeCode, locale.toString(), oldLoc.toString());
//                LOGGER.warn(le);
//            }
            return locale;
        }
    }
}
