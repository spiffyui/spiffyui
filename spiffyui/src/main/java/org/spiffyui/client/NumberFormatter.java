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
package org.spiffyui.client;

import org.spiffyui.client.i18n.SpiffyUIStrings;

import com.google.gwt.core.client.GWT;


/**
 * This is a set of utility methods for formatting numbers in a locale-sensitive way.  
 * 
 * The logic to match formatting with locale was initially taken from 
 * <a href="http://code.google.com/p/jquery-numberformatter/">jquery-numberformatter 1.2.1</a>, which
 * maps a country to a format.
 * 
 * There are 4 different formatting types depending on whether the locale is like US, DE, FR or CH.
 * Locales like US (English) use comma (,) as the number separator and period (.) as the decimal separator.
 * Locales like DE (German) use period (.) as the number separator and comma (,) as the decimal separator.
 * Locales like FR (French) use space ( ) as the number separator and comma (,) as the decimal separator.
 * Locales like CH (Switzerland) use apostrophe (') as the number separator and period (.) as the decimal separator.
 * 
 * This utility requires JSDateUtil to determine the locale.  For some locales there is no country,
 * in which case we use the language.  If none are found it defaults to be like US format.  
 * 
 * The following countries and languages are supported:
 <pre>
 Countries:
 Arab Emirates -> "AE"
 Australia -> "AU"
 Austria -> "AT"
 Brazil -> "BR"
 Canada -> "CA"
 China -> "CN"
 Czech -> "CZ"
 Denmark -> "DK"
 Egypt -> "EG"
 Finland -> "FI"
 France  -> "FR"
 Germany -> "DE"
 Greece -> "GR"
 Great Britain -> "GB"
 Hong Kong -> "HK"
 India -> "IN"
 Israel -> "IL"
 Japan -> "JP"
 Russia -> "RS"
 South Korea -> "KR"
 Spain -> "ES"
 Sweden -> "SE"
 Switzerland -> "CH"
 Taiwan -> "TW"
 Thailand -> "TH"
 United States -> "US"
 Vietnam -> "VN"
 
 Languages:
 Chinese -> "zh"
 Danish -> "da"
 Dutch -> "nl"
 English -> "en"
 French -> "fr"
 German -> "de"
 Italian -> "it"
 Japanese -> "ja"
 Portuguese -> "pt"
 Russian -> "ru"
 Spanish -> "es"
 Slovak -> "sk"
 Swedish -> "sv"
 
 </pre>
 **/

public final class NumberFormatter
{
    private static final String[] LIKE_US_LOCS = {"AE", "AU", "CA", "CN", "EG", "GB", "HK", "IL", "IN", "JP", "TH", "TW", "US", "sk", "zh", "en", "ja"};
    private static final String[] LIKE_DE_LOCS = {"AT", "BR", "DE", "DK", "ES", "GR", "IT", "NL", "PT", "TR", "VN", "nl", "de", "pt", "es"};
    private static final String[] LIKE_FR_LOCS = {"CZ", "FI", "FR", "RU", "SE", "pl", "fr", "ru", "se"};
    private static final String[] LIKE_CH_LOCS = {"CH"};
    
    private static final String[] GROUPING_SEPARATORS = {",", ".", " ", "'"};
    private static final String[] DECIMAL_SEPARATORS = {".", ",", ",", "."};
    
    /** A constant representing locales that are like US (English) */
    public static final int LIKE_US = 0;
    /** A constant representing locales that are like DE (German) */
    public static final int LIKE_DE = 1;
    /** A constant representing locales that are like FR (French) */
    public static final int LIKE_FR = 2;
    /** A constant representing locales that are like CH (Switzerland) */
    public static final int LIKE_CH = 3;
    
    private static final SpiffyUIStrings STRINGS = (SpiffyUIStrings) GWT.create(SpiffyUIStrings.class);
    /**
     * Making sure this class can't be instantiated.
     */
    private NumberFormatter()
    {
    }
    
    /**
     * Format the absolute value of the number appending an abbreviation, if necessary.
     * If 3 digits leaves as is.
     * If 4-6 digits adds a kilo abbreviation with up to one decimal digit.
     * If 7-9 digits adds a mega abbreviation with up to two decimal digits.
     * If 10+ digits adds a giga abbreviation with up to three decimal digits.
     * @param number as a double
     * @return a formatted string
     */
    public static String formatWithAbbreviation(final double number)
    {
        String s;
        double n  = Math.abs(number);
        if (n < 1000) {
            s = format(n, "##0");
        } else if (n >= 1000 && n < 1000000) {
            s = getKiloString(format(n / 1000, "0.#"));
        } else if (n >= 1000000 && n < 1000000000) {
            s = getMegaString(format(n / 1000000, "0.##"));
        } else {
            s = getGigaString(format(n / 1000000000, "0.###"));
        }
        return s;
    }
    
    /**
     * Returns the number with the localized abbreviation for kilo.
     * Override if specialized localization string is needed
     * @param number - a number formatted as a string to be inserted into the parameterized localized string
     * @return the number with the abbreviation
     */
    protected static String getKiloString(String number)
    {
        return STRINGS.kiloAbbrev(number);
    }
    
    /**
     * Returns the number with the localized abbreviation for mega.
     * Override if specialized localization string is needed
     * @param number - a number formatted as a string to be inserted into the parameterized localized string
     * @return the number with the abbreviation
     */
    protected static String getMegaString(String number)
    {
        return STRINGS.megaAbbrev(number);
    }
    
    /**
     * Returns the number with the localized abbreviation for giga.
     * Override if specialized localization string is needed
     * @param number - a number formatted as a string to be inserted into the parameterized localized string
     * @return the number with the abbreviation
     */
    protected static String getGigaString(String number)
    {
        return STRINGS.gigaAbbrev(number);
    }
        
    /**
     * Return the number formatted to the browser's locale.
     * The browser's locale is determined using the localization utilities within JSDateUtil,
     * so the date libraries must be included in order to use this.  
     *  
     * @param number - the number to format as a String
     * @param pattern - a String pattern using the following syntax:
     * <ul>
     * <li> 0 = Digit</li>
     * <li> # = Digit after the decimal, zero shows as absent</li>
     * <li> . = Decimal separator</li>
     * </ul>
     * Valid pattern examples include:
     * <ul>
     * <li>0.#
     * <li>0.##
     * <li>0.0##
     * </ul>
     * Grouping separators will be placed every 3 digits before the decimal separator. Number of digits
     * after the separator is subject to pattern specified. You will always get at least a single whole number,
     * so specifying a 0 before the decimal is all that is necessary.
     * 
     * @return the formatted number as a String
     */
    public static String format(double number, String pattern)
    {
        int indexKey = getLikeLocale();
        /*
         * Get the decimal and group separator
         */
        String decSep = DECIMAL_SEPARATORS[indexKey];
        String groupSep = GROUPING_SEPARATORS[indexKey];
        return format(String.valueOf(number), pattern, decSep, groupSep);
    }

    /**
     * Get the constant representing the 'locale' which the browser locale is similiar to in terms of number formatting
     * @return one of the following constants LIKE_US, LIKE_DE, LIKE_FR, LIKE_CH
     */
    public static int getLikeLocale()
    {
        String loc = JSDateUtil.getLocale();         
        /*
         * Get the country or language key
         */
        String lang = null;
        String country = null;
        
        int hyphen = loc.indexOf('-');
        if (hyphen > 0) {
            lang = loc.substring(0, hyphen);
            country = loc.substring(hyphen + 1);
        } else {
            lang = loc;
        }
        String key = country == null ? lang : country;
        return getIndex(key);
    }
    
    private static String format(String number, String pattern, String decSep, String groupSep)
    {
        int decimalPatPos = pattern.indexOf('.');
        int decimalPos = number.indexOf('.');
        /*
         * handle digits before decimal
         */
        String wholeNumber = decimalPos > 0 ? number.substring(0, decimalPos) : number;
        int wholeNumberLen = wholeNumber.length();
               
        //Add grouping separators by traversing the number in reverse
        StringBuffer reversed = new StringBuffer();
        for (int i = 1; i <= wholeNumberLen; i++) {
            reversed.append(wholeNumber.charAt(wholeNumberLen - i));
            if (i < wholeNumberLen && i % 3 == 0) {
                reversed.append(groupSep);
            }
        }
        //reverse back (reverse is undefined in GWT's StringBuffer)
        wholeNumber = "";
        for (int i = 1, revLen = reversed.length(); i <= revLen; i++) {
            wholeNumber += reversed.charAt(revLen - i);
        }
        if (decimalPatPos < 0) {
            return wholeNumber;
        }
        /*
         * handle decimal digits
         */
        String decimalPattern = decimalPatPos > 0 ? pattern.substring(decimalPatPos + 1) : "";
        String decimalNumber = decimalPos > 0 ? number.substring(decimalPos + 1) : "";
        int decimalPatternLength = decimalPattern.length();
        int decimalLength = decimalNumber.length();
        //truncate the decimal number to the size of the pattern
        if (decimalLength > decimalPatternLength) {
            double fullNumber = Double.valueOf(number);
            //round by the full number, because the decimal might be .07 and that would make it .7 instead of .1
            double fullRounded = Math.round(fullNumber * Math.pow(10, decimalPatternLength)) / Math.pow(10, decimalPatternLength);
            String fullRoundedStr = String.valueOf(fullRounded);
            decimalNumber = fullRoundedStr.substring(fullRoundedStr.indexOf('.') + 1);
        } 
        //if the decimal pattern ends in a 0 and the decimal number ends before it
        //continue to add zeroes until the length is the same as the pattern
        decimalNumber = addZeroes(decimalNumber, decimalPattern);
        //If the pattern is or requires at least one digit, show the decimal separator and it or a 0        
        if (decimalNumber.length() > 0) {
            decimalNumber = decSep + decimalNumber;            
        } else if (decimalPattern.startsWith("0")) {
            decimalNumber = decSep + "0";
        }
        
        /*
         * put the whole and decimal back together
         */
        return wholeNumber + decimalNumber;
    }

    /**
     * Format an integer based on the browser's locale.
     * (A convenience method equivalent to formatting a number with the pattern of "0".)
     * @param wholeNumber an integer to be formatted
     * @return the formatted integer
     */
    public static String format(int wholeNumber)
    {
        return format(wholeNumber, "0");
    }
    

    private static String addZeroes(final String decimalNumber, final String decimalPattern)
    {
        StringBuffer number = new StringBuffer(decimalNumber);
        int numberOfPlaces = decimalPattern.lastIndexOf('0') + 1;
        int decimalLength = number.length();
        if (decimalLength < numberOfPlaces) {
            int numberOfZeroes = numberOfPlaces - decimalLength;
            for (int i = 0; i < numberOfZeroes; i++) {
                number.append('0');
            }
        }
        return number.toString();
    }

    private static int getIndex(String key)
    {
        for (String s : LIKE_US_LOCS) {
            if (s.equals(key)) {
                return LIKE_US;
            }
        }
        
        for (String s : LIKE_DE_LOCS) {
            if (s.equals(key)) {
                return LIKE_DE;
            }
        }
        
        for (String s : LIKE_FR_LOCS) {
            if (s.equals(key)) {
                return LIKE_FR;
            }
        }
        
        for (String s : LIKE_CH_LOCS) {
            if (s.equals(key)) {
                return LIKE_CH;
            }
        }
                
        return LIKE_US;
    }
    
 }
