/*
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
package com.novell.spsample.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.novell.spiffyui.client.MessageUtil;

import com.novell.spiffyui.client.widgets.button.FancyButton;
import com.novell.spiffyui.client.widgets.button.FancySaveButton;
import com.novell.spiffyui.client.widgets.button.RefreshAnchor;
import com.novell.spiffyui.client.widgets.dialog.ConfirmDialog;
import com.novell.spiffyui.client.widgets.dialog.Dialog;
import com.novell.spiffyui.client.widgets.LongMessage;
import com.novell.spiffyui.client.widgets.ProgressBar;
import com.novell.spiffyui.client.widgets.SmallLoadingIndicator;
import com.novell.spiffyui.client.widgets.StatusIndicator;

/**
 * This is the documentation panel
 *
 */
public class DocPanel extends HTMLPanel
{
	/**
     * Creates a new panel
     */
    public DocPanel()
    {
        super("div", 
             "<h1>Spiffy UI Documentation</h1><br /><br />" + 
              "<div id=\"docPanelText\">" + 
                  "<p>" + 
                    "The Spiffy UI framework is included as a subproject and referenced in your project.  " +
                    "You can start using the framework in four simple steps." + 
                  "</p>" +
    
                  "<h2>Using spiffy</h2>" +
    
                  "<p>" +
                    "The SpiffyUI framework is included as a Subversion external reference for " +
                    "this project from <a href=\"https://svn.provo.novell.com/svn/spiffyui/trunk\">" +
                    "https://svn.provo.novell.com/svn/spiffyui/trunk</a>." +
                  "</p>" +
    
                  "<p>" +
                    "Include it in your project by setting the <code>svn:externals</code> property on " +
                    "a directory in your project.  You can set the property using UI tools or with this " +
                    "command line:" +
                  "</p>" +
    
                  "<p>" +
                      "<pre>" +
                        "svn propset svn:externals \"spiffyui https://svn.provo.novell.com/svn/spiffyui/trunk\" &lt;directory&gt;" +
                      "</pre>" +
                  "</p>" +

                  "<p>" +
                    "After you set the property, check in and then update your working copy.  The Spiffy " +
                    "UI framework will be in the directory you chose.  In this sample project the Spiffy " +
                    "UI framework is in the <code>external</code> directory." +
                  "</p>" +
    
                  "<h2>Building spiffy</h2>" +
                  "<p>" +
                    "The Spiffy UI framework uses an Ant build script to compile and compress JavaScript " +
                    "and CSS files using the <a href=\"http://code.google.com/closure/compiler/\">Google " +
                    "Closure Compiler</a> and the <a href=\"http://developer.yahoo.com/yui/compressor/\">YUI " +
                    "Compressor</a>.  You must invoke this build from your own Ant build with the <code>ant</code> " +
                    "task like this:" +
                  "</p>" +
    
                  "<p>" +
                    "<pre>" +
                        "&lt;ant antfile=\"${build.base}/external/spiffyui/build/build.xml\"\n" +
                        "     dir=\"${build.base}/external/spiffyui\"&gt;\n" +
                        "   &lt;property name=\"spiffy.dist\" value=\"${root}/bin/www\"/&gt;\n" +
                        "&lt;/ant&gt;" +
                    "</pre>" +
                  "</p>" +
    
                  "<p>" +
                    "In this sample we've placed the Spiffy UI framework in the external/spiffyui folder in our " +
                    "project.  The <code>spiffy.dist</code> property specifies the location to generate the " +
                    "Spiffy UI framework." +
                  "</p>" +

                  "<h2>Importing spiffy</h2>" +
                  "<p>" +
                    "Now the CSS and JavaScript files are included in your project when you build.  The next step " +
                    "is to reference them in your HTML file.  The Spiffy UI framework includes many JavaScript and " +
                    "CSS files, but they are all combined into two files for faster application loading.  Reference " +
                    "these three files and the JQuery library in the <code>head</code> section of your HTML files " +
                    "like this:" +
                  "</p>" + 
              
                  "<p>" +
                    "<pre>" +
                        "&lt;link type=\"text/css\" rel=\"stylesheet\" href=\"spiffyui.min.css\" /&gt;\n" + 
                        "&lt;script type=\"text/javascript\" src=\"jquery-1.4.2.min.js\"&gt;&lt;/script&gt;\n" + 
                        "&lt;script type=\"text/javascript\" src=\"spiffyui.min.js\"&gt;&lt;/script&gt;" + 
                    "</pre>" +
                  "</p>" +
              
                  "<p>" + 
              
                    "The last step is to import the Spiffy UI framework GWT module.  Add the following line " +
                    "to your GWT module file: " +
                  "</p>" +

                  "<p>" +
                    "<pre>" +
                        "&lt;inherits name=\"com.novell.spiffyui.spiffyui\" /&gt;" +
                    "</pre>" +
                  "</p>" +
    
                  "<p>" +
                    "That's all it takes to use this project.  Now that you've added the JavaScript and CSS to your " + 
                    "build and imported the GWT module you can import the Spiffy UI framework and use it in your page." + 
                  "</p>" +
              
                  "<h2>Using spiffy</h2>" +

                  "<p>" +
                    "Most of the Spiffy UI framework is Java code written in GWT.  All of the API has Java doc in the " + 
                    "<a href=\"javadoc/index.html\">Spiffy UI API JavaDoc</a>." +
                  "</p>" +
              "</div>"
             );
        
        getElement().setId("overviewPanel");
        
        RootPanel.get("mainContent").add(this);
        
        setVisible(false);
    }
}
