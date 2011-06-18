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

/*
 * The main JavaScript file for the Spiffy UI framework
 */
 
spiffyui = {
    
    /*
     * These four functions are for showing different kinds of messages
     * in the general page UI.  They are accessor methods for implementations
     * in MessageUtil.java.  They are bound in that same class.
     */
    showFatalError: null,
    showError: null,
    showWarning: null,
    showMessage: null,

    /**
     * This field controls the auto-loading of the Spiffy UI CSS
     * references.  If this values is false those references will
     * not load.  This value is true by default.
     */
    autoloadCSS: true,
        
    /**
     * This field controls the auto-loading of the Spiffy UI DOM
     * elements.  If this values is false those DOM elements will 
     * not load. This value is true by default. 
     */
    autoloadHTML: true,
        
    /** 
     * This field gives users a chance to override the determined
     * path for the CSS files loaded by spiffyui.js.
     * 
     * If this value isn't specified then the CSS files will be
     * loaded in the same path as the nocache.js file for this GWT
     * application.
     * 
     * If the autoloadCSS field is set to false then this field is
     * not used.
     */
    cssFilePath: null,
    
    /**
     * Gets a string of the date formatted into the short date 
     * format in the current locale. 
     */
    getDate: function(/*int*/ epoch) {
        var dateVal = new Date(epoch);
        return spiffyui.chineseAposWorkaround(dateVal.toString(Date.CultureInfo.formatPatterns.shortDate));
    },

    /**
     * Gets a string of the date formatted into the specified format
     */
    getDateString: function(/*int*/ epoch, /*string*/ format) {
        var dateVal = new Date(epoch);
        return spiffyui.chineseAposWorkaround(dateVal.toString(format));
    },
    
    /**
     * Gets a string of the date formatted into the long date 
     * format in the current locale. 
     */
    getLongDate: function(/*int*/ epoch) {
        var dateVal = new Date(epoch);
        return spiffyui.chineseAposWorkaround(dateVal.toString(Date.CultureInfo.formatPatterns.longDate));
    },

    /**
     * Gets a string of the date formatted into the month day 
     * format in the current locale. If abbrev = true, then use the 
     * abbreviation for month.
     */
    getMonthDay: function(/*int*/ epoch, /*boolean*/ abbrev) {
        var dateVal = new Date(epoch);
        var dateFormat = Date.CultureInfo.formatPatterns.monthDay;
        if (abbrev) {
            dateFormat = dateFormat.replace("MMMM", "MMM");
        }
        return spiffyui.chineseAposWorkaround(dateVal.toString(dateFormat));
    },
    
    /**
     * In zh locales the formatted date strings are 
     * surrounding every Chinese character with apostrophes.
     * For example, it is showing, so this will remove all the 
     * apostrophes. 
     */
    chineseAposWorkaround: function(/*String*/ dateString) {
		if (Date.CultureInfo.name.indexOf("zh") === 0) {
			return dateString.replace(/'/g, "");
		}
		return dateString;
    },
    
    /**
     * Gets a string of the time formatted into the short time 
     * format in the current locale. 
     */
    getShortTime: function(/*int*/ epoch) {
        var dateVal = new Date(epoch);
        return spiffyui.midnightWorkaround(dateVal.toString(Date.CultureInfo.formatPatterns.shortTime));
    },
    
    /**
     * Gets a string of the time at the hour or half hour or
     * rounded up to the nearest half hour
     * and formatted into the short time format in the current 
     * locale. 
     */
    getShortTimeRounded: function(/*int*/ epoch) {
        var dateVal = new Date(epoch);
        var min = dateVal.getMinutes();
        if (min > 0) {
	        if (min < 30) {
	            dateVal.setMinutes(30);
	        } else if (min > 30) {
	            dateVal.setMinutes(0);
	            dateVal.setHours(dateVal.getHours() + 1);
	        }
        }
        
        return spiffyui.midnightWorkaround(dateVal.toString(Date.CultureInfo.formatPatterns.shortTime));        
    },
    
    /**
     * 12 AM is showing up as 0 AM, which is a bug in date.js, who is already aware of the problem.
     * http://groups.google.com/group/datejs/browse_thread/thread/2d4f5d46916920a6/5e2106f2f911ea93?lnk=gst&q=0+AM#5e2106f2f911ea93
     * Since we're still seeing the problem, applying hackito.
     */
    midnightWorkaround: function (/*String*/ formattedString) {
        if (formattedString.indexOf("0:") === 0 && formattedString.indexOf("AM") === 5) {
            formattedString = formattedString.substring(2);
            formattedString = "12:" + formattedString;
        }
        return formattedString;
    },
    
    /**
     * Gets a string of the date time formatted into the full date time 
     * format in the current locale. 
     */
    getDateTime: function(/*int*/ epoch) {
        var dateVal = new Date(epoch);
        return spiffyui.chineseAposWorkaround(dateVal.toString(Date.CultureInfo.formatPatterns.fullDateTime));
    },
    
    /**
     * This is a helper function that gets a Date object from a time
     * string like 11:30pm.  This is just used in JSDateUtil and
     * shouldn't be used anywhere else.
     */
    getDateFromShortTime: function(/*string*/ formattedTime) {
         
         /*
            The date.js library has a bug where it gives the wrong
            24 hour value for 12AM and 12PM.  I've filed a bug report,
            but we still need to work aorund the issue.  Hackito
            ergo sum.
          */
         if (formattedTime === "12:00 PM") {
             formattedTime = "12:00 AM";
         } else if (formattedTime === "12:30 PM") {
             formattedTime = "12:30 AM";
         } else if (formattedTime === "12:00 AM") {
             formattedTime = "12:00 PM";
         } else if (formattedTime === "12:30 AM") {
             formattedTime = "12:30 PM";
         }
         
        /*
           We want to make the Date class parse our time, but it does
           like to parse time without the date so we put a bogus date
           in front of it because we are just interested in the hours
           and minutes.
         */
        var date = Date.parseExact(new Date().toString("yyyy-mm-dd") + " " + formattedTime, 
                                   "yyyy-mm-dd " + Date.CultureInfo.formatPatterns.shortTime);
        return date;
    },
    /**
     * Gets a date that is set to the current date. The time is set to the start of the day (00:00 or 12:00 AM). 
     */
    
    getToday: function() {
        return Date.today().getTime();
    },
    /**
     * Adds the 1 day to today.
     */    
    nextDay: function() {
        return Date.today().add(1).days().getTime();
    },    
    
    /**
     * Adds the specified number of units to the epochDate and returns the new epochDate 
     */    
    dateAdd: function(/*int*/ epochDate, /*int*/amt, /*String*/unit) {
        var date = new Date(epochDate);
        if (unit === "WEEK") {
            return date.addWeeks(amt).getTime();
        } else if (unit === "MONTH") {
            return date.addMonths(amt).getTime();
        } else if (unit === "HOUR") {
            return date.addHours(amt).getTime();
        } else if (unit === "MINUTE") {
            return date.addMinutes(amt).getTime();
        } else if (unit === "SECOND") {
            return date.addSeconds(amt).getTime();
        } else if (unit === "YEAR") {
            return date.addYears(amt).getTime();
        } else {
            return date.add(amt).days().getTime();
        }
    },    
    /**
     * Gets the epoch string (milliseconds since 1970) from a date.
     */
    getEpoch: function(/*date string*/ formattedDate) {
        var epochObj = new Date(formattedDate);
        return epochObj.getTime();
    },

    /**
     * Get the Ordinal day (numeric day number) of the year, adjusted for leap year. 
     * Returns 1 through 365 (366 in leap years). 
     */
    getOrdinalNumber: function(/*int*/ epochDate) {
        var date = new Date(epochDate);
        return date.getOrdinalNumber();
    },
    
    /**
     * Format the ID for a JQuery selector.  This function adds a pound
     * sign to the beginning of the string if there isn't on already
     */
    formatId: function(/*string*/ id) {
         if (id.indexOf('#') === 0) {
             return id;
         } else {
             return '#' + id;
         }
    },
    
    /**
     * Get the UTC offset of the current browser timezone.  This
     * returns a number like -5 or 6
     */
    getUTCOffset: function() {
        var d = Date.today();
        var offset = d.getUTCOffset();
        if (offset.indexOf('+') === 0) {
            return parseInt(offset.substring(1, offset.length - 2), 10);
        } else if (offset.indexOf('-') === 0) {
            return -parseInt(offset.substring(1, offset.length - 2), 10);
        } else {
            /*
             * IE in some cases will return this value starting with undefined.  That means
             * negative.
             */
            return -parseInt(offset.substring(10, offset.length - 2), 10);
        }
    },
    
    /**
     * Add an item to the browser history.  This function is just
     * called from GWT.
     */
    addHistoryItem: function(/*object*/ scope, /*string*/id, /*string*/url, /*string*/title, 
                             /*boolean*/ bookmarkable, /*boolean*/ replace) {
         if (!bookmarkable) {
             url = '?-';
         } else {
             title = title + ' - ' + id;
         }
         
         if (replace) {
             spiffyui.History.replaceState({state:id}, title, url);
         } else {
             spiffyui.History.pushState({state:id}, title, url);
         }
    },
    
    /**
     * GWT often puts output files in a path with the module name.
     * We need to determine that path so we can figure out where to
     * reference our CSS files from.  There isn't a great way to do
     * this so we are looking for the nocache.js file and keying off
     * of that path.
     */
    getRelativePath: function() {
         if (spiffyui.cssFilePath) {
             return spiffyui.cssFilePath;
         }
         
         var path = '';
         jQuery('script').each(function() {
             if (jQuery(this).attr('src') && jQuery(this).attr('src').indexOf('nocache.js') > -1) {
                 if (jQuery(this).attr('src').indexOf('/') === -1) {
                     return;
                 } else {
                     path =  jQuery(this).attr('src').substring(0, jQuery(this).attr('src').lastIndexOf('/') + 1);
                 }
             }
         });
         
         return path;
    },
    
    init: function() {
         
        (function(window) {
            // Prepare
            spiffyui.History = window.History; // Note: We are using a capital H instead of a lower h
            if (!spiffyui.History.enabled) {
                // History.js is disabled for this browser.
                // This is because we can optionally choose to support HTML4 browsers or not.
                return false;
            }
            
            // Bind to StateChange Event
            spiffyui.History.Adapter.bind(window,'statechange',function() { // Note: We are using statechange instead of popstate
                var State = spiffyui.History.getState(); // Note: We are using History.getState() instead of event.state
                if (spiffyui.doHandleHistoryEvent) {
                    /*
                     * If the doHandleHistoryEvent function has been bound then we
                     * call Spiffy UI to handle the history event.  
                     */
                    spiffyui.doHandleHistoryEvent(State.data.state);
                }
            });
            
        }(window));
        
        if (spiffyui.autoloadHTML) {
         
            /*
             * We start by adding the HTML tags our CSS depends on
             */
            jQuery('body').append(
                '<div id="loginPanel"></div>' + 
                '<div id="mainWrap">' + 
                    '<div id="main" class="clearfix">' + 
                        '<div id="mainHeader"></div>' + 
                        '<div id="mainBody">' + 
                            '<div id="mainNavigation"></div>' + 
                            '<div id="mainContent"></div>' + 
                            '<div class="clear"></div>' + 
                        '</div>' + 
                        '<div class="clear"></div>' + 
                    '</div>' + 
                '</div>' + 
                '<div id="mainFooter"></div>');
        }
        
        var path = spiffyui.getRelativePath();

        if (spiffyui.autoloadCSS) {
            /*
             * Almost all of our CSS is in spiffyui.css, but there are always a few
             * tweaks you need to add for IE.  This special style sheet is added only
             * if the browser is IE and contains just those tweaks.
             */
            if (navigator.appName === 'Microsoft Internet Explorer') {
                jQuery('head').prepend('<link>');
                css = $('head').children(':first');
                css.attr({
                    rel:  'stylesheet',
                    type: 'text/css',
                    href: path + 'spiffyui.ie.css'
                });
            }
            
            /*
             * Now we add the spiffyui.min.css file.  If the URL ends with -debug.html
             * then we'll add the uncompressed version.  We add these files dynamically
             * so users of the framework have less to add to their page and because IE8
             * give precedence to files loaded in the page over those added with JavaScript
             * and we can't do that because we override styles in spiffyui.ie.css.
             */
            if (window.location.href.substr(-11) === '-debug.html') {
                jQuery('head').prepend('<link>');
                css = $('head').children(':first');
                css.attr({
                    rel:  'stylesheet',
                    type: 'text/css',
                    href: 'spiffyui.css'
                });
            } else {
                jQuery('head').prepend('<link>');
                css = $('head').children(':first');
                css.attr({
                    rel:  'stylesheet',
                    type: 'text/css',
                    href: path + 'spiffyui.min.css'
                });
            }
        }
    }
    
};

jQuery(document).ready(function() {
    spiffyui.init();
});
