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
 
jQuery.fn.textWidth = function(){
    var val = jQuery(this).val();
    var span = jQuery('<span>' + val + '</span>');
    jQuery(this).parent().append(span);
    var width = span.width();
    
    span.remove();
    
    return width;
};

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
        if (Date.CultureInfo.name.indexOf("zh") === 0 ||
            Date.CultureInfo.name.indexOf("ja") === 0) {
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
        var spaceLoc = formattedString.indexOf(' ');
        
        if (formattedString.indexOf("0:") === 0 && formattedString.indexOf("AM") === 5) {
            formattedString = formattedString.substring(2);
            formattedString = "12:" + formattedString;
        } else if (Date.CultureInfo.amDesignator !== '' && Date.CultureInfo.amDesignator.toLowerCase() !== 'am' && formattedString.indexOf(' ' + Date.CultureInfo.amDesignator + ' 00:') === spaceLoc) {
            formattedString = formattedString.replace(Date.CultureInfo.amDesignator + ' 00:', Date.CultureInfo.amDesignator + ' 12:');
        } else {
            //If the time is not the first part of the string but instead preceded by a space
            if (formattedString.indexOf(' 0:') === spaceLoc && formattedString.indexOf('AM') === spaceLoc + 6) {
                formattedString = formattedString.replace(' 0:', ' 12:');
            } else if (Date.CultureInfo.amDesignator !== '' && Date.CultureInfo.amDesignator.toLowerCase() !== 'am' && formattedString.indexOf(' ' + Date.CultureInfo.amDesignator + ' 00:') === spaceLoc) {
                formattedString = formattedString.replace(Date.CultureInfo.amDesignator + ' 00:', Date.CultureInfo.amDesignator + ' 12:');
            }
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
            There is a bug in the date.js library where getting the time from a string with 'tt' that is not 'am' or 'pm'
            (even though the amDesignator and pmDesignator are correct).  So we change them to 'am' or 'pm'
            before parsing.  
         */
        if (Date.CultureInfo.amDesignator !== '' && 
                Date.CultureInfo.amDesignator.toLowerCase() !== 'am' && 
                formattedTime.indexOf(Date.CultureInfo.amDesignator) >= 0) {
            formattedTime = formattedTime.replace(Date.CultureInfo.amDesignator, 'AM');
        } 
        if (Date.CultureInfo.pmDesignator !== '' &&
                Date.CultureInfo.pmDesignator.toLowerCase() !== 'pm' && 
                formattedTime.indexOf(Date.CultureInfo.pmDesignator) >= 0) {
            formattedTime = formattedTime.replace(Date.CultureInfo.pmDesignator, 'PM');
        } 

        /*
            The date.js library has a bug where it gives the wrong
            24 hour value for 12AM and 12PM.  I've filed a bug report,
            but we still need to work around the issue.  Hackito
            ergo sum.
         */
        if (formattedTime.match(/12:[0-5][0-9] PM/) || formattedTime.match(/PM 12:[0-5][0-9]/)) {
            formattedTime = formattedTime.replace("PM", "AM");

        } else if (formattedTime.match(/12:[0-5][0-9] AM/) || formattedTime.match(/AM 12:[0-5][0-9]/)) {
            formattedTime = formattedTime.replace("AM", "PM");
        } 
        
        /*
            There is also a bug with the timePicker where even if time is hh, it will only show h if 
            it's not 24 hour time.  This is bogus because 01:00 AM should be allowed as well as 01:00 PM.       
            Let's make sure the hh is respected so Date.parseExact will be ok.  
         */        
        var hhLoc = Date.CultureInfo.formatPatterns.shortTime.indexOf('hh:'); 
        if (hhLoc >= 0) {
            var colonLoc = formattedTime.indexOf(':');
            if (colonLoc < hhLoc + 2) {
                var hour = formattedTime.charAt(hhLoc);
                formattedTime = formattedTime.replace(hour, '0' + hour);
            }
        }
        
        /*
           We want to make the Date class parse our time, but it does
           like to parse time without the date so we put a bogus date
           in front of it because we are just interested in the hours
           and minutes.
         */
        var date = Date.parseExact(new Date().toString("yyyy-MM-dd") + " " + formattedTime, 
                                   "yyyy-MM-dd " + Date.CultureInfo.formatPatterns.shortTime);
        return date;
    },
    
    parseDateTime: function(/*String*/dateTimeString, /*String*/formatPattern) {

        /*
            There is a bug in the date.js library where getting the time from a string with 'tt' that is not 'am' or 'pm'
            (even though the amDesignator and pmDesignator are correct).  So we change them to 'am' or 'pm'
            before parsing.  
         */
        if (Date.CultureInfo.amDesignator !== '' && 
                Date.CultureInfo.amDesignator.toLowerCase() !== 'am' && 
                dateTimeString.indexOf(Date.CultureInfo.amDesignator) >= 0) {
            dateTimeString = dateTimeString.replace(Date.CultureInfo.amDesignator, 'AM');
        } 
        if (Date.CultureInfo.pmDesignator !== '' &&
                Date.CultureInfo.pmDesignator.toLowerCase() !== 'pm' && 
                dateTimeString.indexOf(Date.CultureInfo.pmDesignator) >= 0) {
            dateTimeString = dateTimeString.replace(Date.CultureInfo.pmDesignator, 'PM');
        } 
        /*
            The date.js library has a bug where it gives the wrong
            24 hour value for 12AM and 12PM.  I've filed a bug report,
            but we still need to work around the issue.  Hackito
            ergo sum.
         */
        var orig12AM = false;
        if (dateTimeString.match(/12:[0-5][0-9] PM/) || dateTimeString.match(/PM 12:[0-5][0-9]/)) {
            dateTimeString = dateTimeString.replace("PM", "AM");
    
        } else if (dateTimeString.match(/12:[0-5][0-9] P/)) {
            dateTimeString = dateTimeString.replace("P", "AM");
            
        } else if (dateTimeString.match(/12:[0-5][0-9] AM/) || dateTimeString.match(/AM 12:[0-5][0-9]/)) {
            dateTimeString = dateTimeString.replace("AM", "PM");
            orig12AM = true;
            
        } else if (dateTimeString.match(/12:[0-5][0-9] A/)) {
            dateTimeString = dateTimeString.replace("A", "PM");
            orig12AM = true;
        }    
        /*
            There is also a bug with the timePicker where even if time is hh, it will only show h if 
            it's not 24 hour time.  This is bogus because 01:00 AM should be allowed as well as 01:00 PM.   
            An example of this is zh-TW      
         */ 
        var hhLoc = formatPattern.indexOf('hh:'); 
        if (hhLoc >= 0) {
            var colonLoc = dateTimeString.indexOf(':');
            if (colonLoc < hhLoc + 2) {
                var hour = dateTimeString.charAt(hhLoc);
                dateTimeString = dateTimeString.replace(' ' + hour + ':', ' 0' + hour + ':');
            }
        }
        
        var date = Date.parseExact(dateTimeString, formatPattern);
        if (date && orig12AM) {

            /*
             * Another bug in Date.js.  Even after all the 12 am/pm workarounds,
             * when it is originally 01/01/2012 12:00 AM
             * we have to switch it to 01/01/2012 12:00 PM in order to get the time right
             * but then it adds a whole other day!  so we actually have to subract the day if the time was 
             * originally something 12:xx AM
             */
            date = date.addDays(-1);
        }

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
        return date.getDayOfYear();
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
     * Format the date specified and return the results 
     *
     */
    formatDate: function(/*int*/ epochDate, /*string*/ formatter) {
         return new Date(epochDate).format(formatter);
    },
    
    /**
     * Add an item to the browser history.  This function is just
     * called from GWT.
     */
    addHistoryItem: function(/*object*/ scope, /*string*/id, /*string*/url, /*string*/title, 
                             /*boolean*/ bookmarkable, /*boolean*/ replace) {
         if (!bookmarkable) {
             url = '?-';
         }
         
         if (replace) {
             spiffyui.History.replaceState({state:id}, title, url);
         } else {
             spiffyui.History.pushState({state:id}, title, url);
         }
    },
    
    /**
     * Go forward in the history
     */
    forward: function() {
         spiffyui.History.forward();
    },
    
    /**
     * Go backward in the history
     */
    back: function() {
         spiffyui.History.back();
    },

    getHistoryState: function() {
         return spiffyui.History.getState().data.state;
    },
    
    /**
     * If X is negative go back through history X times, if X is 
     * positive go forwards through history X times 
     */
    go: function(/*int*/ x) {
         spiffyui.History.go(x);
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

    getCurrentUrl: function() {
        var url = window.location.protocol;

        if (url.indexOf('//', url.length - 2) === -1) {
            url += '//';
        }

        url += window.location.host;
        
        if (url.indexOf('/', url.length - 1) !== -1) {
            url += '/';
        }
        
        url += spiffyui.getRelativePath();

        return url;
    },

    oAuthAuthenticate: function(/*String*/ url, /*String*/ clientId, /*String*/ scope, /*function*/ callback) {

        window.addEventListener('message', spiffyui.oAuthAuthenticateCompleteEventListener, false);
        //spiffyui.log('oAuthAuthenticate(' + url + ', ' + clientId + ', ' + scope + ')');

        spiffyui.oauthCallback = callback;
        url += '?redirect_uri=' + spiffyui.getCurrentUrl() + '/oauth.html';

        if (clientId) {
            url += '&client_id=' + clientId;
        }

        if (scope) {
            url += '&scope=' + clientId;
        }

        url += '&response_type=token';

        spiffyui.oauthstate = 'spiffystate' + Math.random();
        url += '&state=' + spiffyui.oauthstate;

        //spiffyui.log('spiffyui.oauthstate: ' + spiffyui.oauthstate);
        //spiffyui.log('url: ' + url);

        var frame = $('<iframe id="spiffyuoauthframe" seamless="true" src="' + url + '"></iframe>');
        frame.css({
            'position': 'fixed',
            'left': '0px',
            'top': '0px',
            'z-index': '99999',
            'width': '100%',
            'height': '100%'
        });

        $('body').append(frame);
    },

    oAuthAuthenticateCompleteEventListener: function(event) {
        window.removeEventListener('message', spiffyui.oAuthAuthenticateCompleteEventListener, false);
        spiffyui.oAuthAuthenticateComplete(event.data);
    },

    oAuthAuthenticateComplete: function(/*String*/ response) {
        //spiffyui.log('oAuthAuthenticateComplete(' + response + ')');
        var callback = spiffyui.oauthCallback;
        spiffyui.oauthCallback = null;

        $('#spiffyuoauthframe').remove();

        // First, parse the query string
        var params = {};
        var queryString = response.substring(1);
        //spiffyui.log('queryString: ' + queryString);
        var regex = /([^&=]+)=([^&]*)/g;
        var m = regex.exec(queryString);
        while (m) {
            //spiffyui.log('m[1]: ' + m[1]);
            //spiffyui.log('m[2]: ' + m[2]);
            params[decodeURIComponent(m[1])] = decodeURIComponent(m[2]);
            m = regex.exec(queryString);
        }

        //spiffyui.log('params.state: ' + params.state);
        //spiffyui.log('params.access_token: ' + params.access_token);
        //spiffyui.log('params.token_type: ' + params.token_type);

        if (spiffyui.oauthstate !== params.state) {
            spiffyui.oauthstate = null;
            callback("invalidstate", "null");
        } else {
            spiffyui.oauthstate = null;
            callback(params.access_token, params.token_type);
        }
    },

    log: function(/*String*/ msg) {
        if (console) {
            console.log(msg);
        }
    },
    
    init: function() {

        spiffyui.isMobile = navigator.userAgent.match(/iPad/i) !== null ||
            navigator.userAgent.match(/iPhone/i) !== null ||
            navigator.userAgent.match(/iPad/i) !== null ||
            navigator.userAgent.match(/iPod/i) !== null ||
            navigator.userAgent.match(/android/i) !== null;
         
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
                css = jQuery('head').children(':first');
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
            if (window.location.href.indexOf('-debug.htm') > -1) {
                jQuery('head').prepend('<link>');
                css = jQuery('head').children(':first');
                css.attr({
                    rel:  'stylesheet',
                    type: 'text/css',
                    href: 'spiffyui.css'
                });
            } else {
                jQuery('head').prepend('<link>');
                css = jQuery('head').children(':first');
                css.attr({
                    rel:  'stylesheet',
                    type: 'text/css',
                    href: path + 'spiffyui.min.css'
                });
            }
        }
        
        /*
         * The last step is to initialize our history framework.
         */
        (function(window) {
            spiffyui.History = window.History; // Note: We are using a capital H instead of a lower h
            if (!spiffyui.History.enabled) {
                /*
                 * Applications aren't required to use our history support and if
                 * they haven't loaded the libraries then we don't want to try
                 * and call them.
                 */
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
    }
    
};

jQuery(document).ready(function() {
    spiffyui.init();
});
