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
package org.spiffyui.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * This is the footer for the main page.
 *
 */
public class MainFooter extends Composite
{

    private HTML m_html;
    
    private static final String ID = "mainFooter";
    
    /**
     * Creates a new main footer panel
     */
    public MainFooter()
    {
        m_html = new HTML();
        m_html.getElement().setId("mainFooterContainer");
        
        if (RootPanel.get(ID) != null) {
            RootPanel.get(ID).add(m_html);
        } else {
            throw new IllegalStateException("Unable to locate the mainFooter element.  You must import spiffyui.min.js before using the Main Footer.");
        }
        
        
    }
    
    /**
     * Set the footer string.  This string may contain HTML
     * 
     * @param string the footer string
     */
    public void setFooterString(String string)
    {
        m_html.setHTML(string);
    }
    
    /**
     * Get the current string in the footer
     * 
     * @return the current footer string
     */
    public String getFooterString()
    {
        return m_html.getHTML();
    }
    
    @Override
    public void setVisible(boolean visible) 
    {
        if (visible) {
            JSUtil.show(ID);
        } else {
            JSUtil.hide(ID);
        }        
    }    
}
