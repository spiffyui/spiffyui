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
package org.spiffyui.client.widgets.button;

import org.spiffyui.client.i18n.SpiffyUIStrings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * This an Anchor that shows the refresh icon.  It has a disabled look as well as a loading look.
 */
public class RefreshAnchor extends Anchor
{
    private static final SpiffyUIStrings STRINGS = (SpiffyUIStrings) GWT.create(SpiffyUIStrings.class);
    
    private boolean m_enabled = true;
    private boolean m_loading = false;
    private Element m_iconDiv;
    
    /**
     * Creates a new RefreshAnchor with a randomly generated ID
     *  
     */
    public RefreshAnchor()
    {
        this(HTMLPanel.createUniqueId());
    }
    
    /**
     * Creates a new RefreshAnchor with the specified ID
     * 
     * @param id - The element ID
     */
    public RefreshAnchor(String id)
    {
        super();
        m_iconDiv = Document.get().createDivElement();
        m_iconDiv.setClassName("spiffy-refresh-icon");
        m_iconDiv.setId(id + "_refreshIconDiv");     
        setTitle(STRINGS.refresh());
        getElement().appendChild(m_iconDiv);
      
        /*
         * Set the href so that the anchor is keyboard accessible (user can tab to it).
         * Using "#" will make the browser page scroll back to the top if the anchor low,
         * and using ("#" + id) will set focus to the anchor, which would remove focus
         * being set to something else during the onclick event (for example the default
         * button on a ConfirmDialog).  Call setHref if something different is needed.
         * From the onclick event you may call event.preventDefault() to prevent
         * the href event.
         */
        setHref("#"); 
        getElement().setId(id);
    }
    
    @Override
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        m_enabled = enabled;
        
        if (enabled) {
            m_iconDiv.setClassName("spiffy-refresh-icon");
        } else {
            m_iconDiv.setClassName("spiffy-refresh-icon-disabled");
        }
    }
    
    
    @Override
    public boolean isEnabled()
    {
        return m_enabled;
    }
    

    /**
     * Sets the anchor to appear as loading
     * @param loading - boolean true to be loading
     */
    public void setLoading(boolean loading)
    {
        super.setEnabled(!loading);
        m_loading = loading;
        
        if (loading) {
            m_iconDiv.setClassName("spiffy-refresh-icon-loading");
        } else {
            m_iconDiv.setClassName("spiffy-refresh-icon");
        }
    }

    /**
     * Returns true if loading
     * @return boolean true if loading
     */
    public boolean isLoading()
    {
        return m_loading;
    }

    /**
     *
     * @return id of the refresh anchor
     */
    public String getId()
    {
        return getElement().getId();
    }
    
}
