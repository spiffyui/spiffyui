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
 * The main JavaScript file for the SPSample framework
 */
 
spsample = {
    /**
     * This functions gets a list of the GitHub projects for Spiffy
     * UI and adds them to our UI.  This is a nice dynamic way of
     * showing the projects without having a hard-coded list of the
     * projects we need to update.
     * 
     * We could have made this call in GWT, but it doesn't have good
     * support for JSONP and it is much easier to do it with JQuery.
     */
    getGitHubProjects: function() {
        jQuery.ajax({
             url: 'http://github.com/api/v1/json/spiffyui',
             dataType: 'jsonp',
             success: function(res) {
                 var user = res.user;
                 
                 var info = '';
                 
                 /*
                  * We are sorting the repos alphabetically by 
                  * name for now.
                  */
                 user.repositories.sort(function(a, b) {
                     if (a.name < b.name) {
                         return -1;
                     }

                     if (a.name > b.name) {
                         return 1;
                     }

                     return 0;

                 });
                 
                 for (var i = 0; i < user.repositories.length; i++) {
                    var repo = user.repositories[i];
                    
                    info += '<li><a href="' + repo.url + '" id="' + spsample.genRepoId(repo.name) + '">' + repo.name + '</a>' + repo.description + '</li>';
                 }
                 
                 info += '';
                 
                 $('#samplesList').append(info);
                 
                 /*
                  * spsample.addSamplesToc() defined in SamplesPanel.java
                  */
                 spsample.addSamplesToc();
             },
             error: function(e) {
                  console.log("e: " + e);

             }
         });
    },
    
    /**
     * Some repository names may contain period and jQuery can have problems with them, so remove.
     */
    genRepoId: function(name) {
    	return name.replace(/\./g, '_');
    },
    
    /**
     * This is a helper function to add a CSS link to the HEAD of the current
     * document.
     */
    addCSSLink: function(/*string*/ file) {
         jQuery("head").append("<link>");
            css = $("head").children(":last");
            css.attr({
                rel:  "stylesheet",
                type: "text/css",
                href: file
            });
    },
    
    init: function() {
         
        jQuery('#landingPanelText').remove();
        
        /*
         * Almost all of our CSS is in spiffyui.css, but there are always a few
         * tweaks you need to add for IE.  This special style sheet is added only
         * if the browser is IE and contains just those tweaks.
         */
        if (navigator.appName === 'Microsoft Internet Explorer') {
            spsample.addCSSLink('spiffyui.ie.css');
        }
        
        /*
         * Now we add the spiffyui.min.css file.  If the URL ends with -debug.html
         * then we'll add the uncompressed version.  We add these files dynamically
         * so users of the framework have less to add to their page and because IE8
         * give precedence to files loaded in the page over those added with JavaScript
         * and we can't do that because we override styles in spiffyui.ie.css.
         */
        if (window.location.href.substr(-11) !== '-debug.html') {
            spsample.addCSSLink('spsample.min.css');
        }
        
        if (navigator.userAgent.indexOf('Chrome') > -1 &&
            navigator.userAgent.indexOf('Windows NT 5.1') > -1) {
            /*
             * There is an issue with Chrome on Windows XP with our fonts.  The issue is
             * that they don't have proper hinting on XP and the Chrome rendering engine
             * makes them look pretty bad.  In that case we use a special stylesheet to
             * substitute Verdana which has proper hinting and renders much better on
             * that one platform.
             */
            spsample.addCSSLink('spsample_chromexp.css');
        }
        
        $(window).scroll(function() {
            
            var offset = $(window).scrollTop();
            
            /*
             * We only want to turn the back to top link on if we are scrolled down
             */
            if ($('#mainWrap').hasClass('sausagenav')) {
                /*
                 * We never show the top link when we are showing the sausage
                 * navigation because the links handle it well already.
                 */
                return;
            }
            
            /*
             * spsample.shouldShowTopLink() defined in Index.java
             */
            if (spsample.shouldShowTopLink() && offset > 250) {
                $('#backToTop').show();
            } else {
                $('#backToTop').hide();
            }
        });
    }
};

jQuery(document).ready(function() {
    spsample.init();
});
