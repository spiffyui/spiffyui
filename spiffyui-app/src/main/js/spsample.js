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
    init: function() {
        
        jQuery('#landingPanelText').remove();
        
        /*
         * Almost all of our CSS is in spiffyui.css, but there are always a few
         * tweaks you need to add for IE.  This special style sheet is added only
         * if the browser is IE and contains just those tweaks.
         */
        if (navigator.appName === 'Microsoft Internet Explorer') {
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
        if (window.location.href.substr(-11) !== "-debug.html") {
            jQuery("head").prepend("<link>");
            css = $("head").children(":first");
            css.attr({
                rel:  "stylesheet",
                type: "text/css",
                href: "spsample.min.css"
            });
        }
    }
};

jQuery(document).ready(function() {
    spsample.init();
});
