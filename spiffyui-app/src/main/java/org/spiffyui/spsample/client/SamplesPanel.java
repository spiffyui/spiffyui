/*******************************************************************************
 *
 * Copyright 2011-2014 Spiffy UI Team   
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
package org.spiffyui.spsample.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * This is the samples panel
 *
 */
public class SamplesPanel extends HTMLPanel
{
    private static final SPSampleStrings STRINGS = (SPSampleStrings) GWT.create(SPSampleStrings.class);    
    
    /**
     * Creates a new panel
     */
    public SamplesPanel()
    {        
        super("div", STRINGS.SamplesPanel_html());

        getElement().setId("samplesPanel");

        RootPanel.get("mainContent").add(this);

        setVisible(false);
        
        bindJavaScript(this);
    }
    
    @Override
    public void onLoad()
    {
        super.onLoad();
        loadGitHubProjects();
    }
    
    private static native void loadGitHubProjects() /*-{
        $wnd.spsample.getGitHubProjects();
    }-*/;
    
    private native void bindJavaScript(SamplesPanel p) /*-{
        $wnd.spsample.addSamplesToc = function() {
            return p.@org.spiffyui.spsample.client.SamplesPanel::addToc()();
        }
    }-*/;
    
    private void addToc()
    {
        Index.addToc(this, "#samplesList a");
    }
}
