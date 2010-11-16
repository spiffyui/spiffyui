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

    autoloadCSS: true,
    autoloadHTML: true,
    
    /**
     * Gets a string of the date formatted into the short date 
     * format in the current locale. 
     */
    getDate: function(/*int*/ epoch) {
        var dateVal = new Date(epoch);
        return dateVal.toString(Date.CultureInfo.formatPatterns.shortDate);
    },
    
    /**
     * Gets a string of the date formatted into the long date 
     * format in the current locale. 
     */
    getLongDate: function(/*int*/ epoch) {
        var dateVal = new Date(epoch);
        return dateVal.toString(Date.CultureInfo.formatPatterns.longDate);
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
        if (formattedString.indexOf("0:") === 0 && formattedString.indexOf("AM") == 5) {
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
        return dateVal.toString(Date.CultureInfo.formatPatterns.fullDateTime);
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
     * Adds the specified number of days to this instance. The number can be positive or negative. 
     */    
    nextDay: function(date) {
        return Date.today().add(1).days().getTime();
    },    
    
    /**
     * Adds the specified number of units to the epochDate and returns the new epochDate 
     */    
    dateAdd: function(/*int*/ epochDate, /*int*/amt, /*String*/unit) {
        var date = new Date(epochDate);
        if (unit == "WEEK") {
            return date.addWeeks(amt).days().getTime();
        } else if (unit == "MONTH") {
            return date.addMonths(amt).days().getTime();
        } else if (unit == "HOUR") {
            return date.addHours(amt).days().getTime();
        } else if (unit == "MINUTE") {
            return date.addMinutes(amt).days().getTime();
        } else if (unit == "SECOND") {
            return date.addSeconds(amt).days().getTime();
        } else if (unit == "YEAR") {
            return date.addHours(amt).days().getTime();
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
    
    init: function() {

        if (spiffyui.autoloadHTML) {
         
            /*
             * We start by adding the HTML tags our CSS depends on
             */
            jQuery('script:last').after(
                '<div id="loginPanel"></div>' + 
                '<div id="mainWrap">' + 
                    '<div id="main" class="clearfix">' + 
                        '<div id="mainHeader"></div>' + 
                        '<div id="mainBody">' + 
                            '<div id="mainNavigation"></div>' + 
                            '<div id="mainContent">' + 
                                '<div id="errorpanel"></div>' + 
                            '</div>' + 
                            '<div class="clear"></div>' + 
                        '</div>' + 
                        '<div class="clear"></div>' + 
                    '</div>' + 
                '</div>' + 
                '<div id="mainFooter"></div>');
        }

        if (spiffyui.autoloadCSS) {
            /*
             * Almost all of our CSS is in spiffyui.css, but there are always a few
             * tweaks you need to add for IE.  This special style sheet is added only
             * if the browser is IE and contains just those tweaks.
             */
            if (navigator.appName == 'Microsoft Internet Explorer') {
                jQuery("head").prepend("<link>");
                css = $("head").children(":first");
                css.attr({
                    rel:  "stylesheet",
                    type: "text/css",
                    href: "spiffyui.ie.css"
                });
            }
            
            /*
             * Now we add the spiffyui.min.css file.  If the URL ends with -debug.html
             * then we'll add the uncompressed version.  We add these files dynamically
             * so users of the framework have less to add to their page and because IE8
             * give precedence to files loaded in the page over those added with JavaScript
             * and we can't do that because we override styles in spiffyui.ie.css.
             */
            if (window.location.href.substr(-11) === "-debug.html") {
                jQuery("head").prepend("<link>");
                css = $("head").children(":first");
                css.attr({
                    rel:  "stylesheet",
                    type: "text/css",
                    href: "spiffyui.css"
                });
            } else {
                jQuery("head").prepend("<link>");
                css = $("head").children(":first");
                css.attr({
                    rel:  "stylesheet",
                    type: "text/css",
                    href: "spiffyui.min.css"
                });
            }
        }
    }
    
};

jQuery(document).ready(function() {
    spiffyui.init();
});
