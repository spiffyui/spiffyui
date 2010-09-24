/*
 * The main JavaScript file for the Spiffy UI framework
 */
 
spiffyui = {
    init: function() {
        /*
         * We start by adding the HTML tags our CSS depends on
         */
        jQuery('script:last').after(
            // We add a message if the user doesn't have JavaScript turned on
            '<noscript>' + 
                '<div id="jswarning">' +
                    'Your browser does not support JavaScript.  You must enable JavaScript ' + 
                    'and refresh this page to use this tool.' + 
                '</div>' + 
            '</noscript>' + 
            
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
};

jQuery(document).ready(function() {
    spiffyui.init();
});
