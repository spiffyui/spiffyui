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

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;

import org.spiffyui.client.nav.HasNavBarListenersPanel;


/**
 * This is the header for the main page.
 *
 */
public class MainHeader extends HasNavBarListenersPanel
{
    /**
     * The ID of the header string section.  This section is often used for a welcome message.
     */
    public static final String HEADER_STRING = "headerstring";
    
    /**
     * The ID of the header title section
     */
    public static final String HEADER_TITLE = "header_title";

    /**
     * The ID of the header actions block
     */
    public static final String HEADER_ACTIONS_BLOCK = "header_actionsBlock";

    private HTMLPanel m_panel;
    private Anchor m_logout;
    
    /**
     * Creates a new MainHeader panel
     */
    public MainHeader()
    {
        String html =
            "<div id=\"headerleft\">" +
                "<div id=\"headerlogo\"> </div>" +
                "<span class=\"headertitle\" id=\"" + HEADER_TITLE + "\"></span>" +
            "</div>" +
            "<div id=\"headerright\">" +
                "<div id=\"" + HEADER_ACTIONS_BLOCK + "\">" +
                    "<span id=\"" + HEADER_STRING + "\"></span> " + 
                "</div>" +
            "</div>";

        m_panel = new HTMLPanel("div", html);
        m_panel.getElement().setId("mainHeaderContainer");

        add(m_panel);
        
        if (RootPanel.get("mainHeader") != null) {
            RootPanel.get("mainHeader").add(this);
        } else {
            throw new IllegalStateException("Unable to locate the mainHeader element.  You must import spiffyui.min.js before using the Main Header.");
        }
        
        

        
    }

    /**
     * Get the Anchor object used for the logout link in the header bar
     * 
     * @return the logout Anchor
     */
    public Anchor getLogout()
    {
        return m_logout;
    }

    /**
     * Set the Anchor used for the logout in the header
     * 
     * @param logout the logout Anchor
     */
    public void setLogout(Anchor logout)
    {
        m_logout = logout;
        m_panel.add(logout, HEADER_ACTIONS_BLOCK);
    }
    
    /**
     * Sets the username for display in the header
     * 
     * @param string the welcome string
     */
    public void setWelcomeString(String string)
    {
        m_panel.getElementById(HEADER_STRING).setInnerText(string);
    }
    
    /**
     * Gets the welcome string for the header
     * 
     * @return the welcome string
     */
    public String getWelcomeString()
    {
        return m_panel.getElementById(HEADER_STRING).getInnerText();
    }
    
    /**
     * Set the main title for this header
     * 
     * @param string The main title for this header
     */
    public void setHeaderTitle(String string)
    {
        m_panel.getElementById(HEADER_TITLE).setInnerHTML(string);
    }
    
    /**
     * Get the main title for the this header
     * 
     * @return The main title of this header
     */
    public String getHeaderTitle()
    {
        return m_panel.getElementById(HEADER_TITLE).getInnerHTML();
    }

    /**
     * Get the HTML panel for this header
     * 
     * @return the HTML panel
     */
    protected HTMLPanel getPanel()
    {
        return m_panel;
    }
}
