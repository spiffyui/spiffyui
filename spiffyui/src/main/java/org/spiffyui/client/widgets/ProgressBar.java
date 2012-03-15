/*******************************************************************************
 *
 * Copyright 2011 Spiffy UI Team   
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
package org.spiffyui.client.widgets;

import org.spiffyui.client.i18n.SpiffyUIStrings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;


/**
 * This widget wraps the JQuery UI progress bar
 */
public class ProgressBar extends SimplePanel
{
    private static final SpiffyUIStrings STRINGS = (SpiffyUIStrings) GWT.create(SpiffyUIStrings.class);
    
    private int m_value = 0;
    
    /**
     * Create a progress bar with the specified ID.  The ID is required
     * because the progress bar needs a specific ID to connect to.
     * 
     */
    public ProgressBar()
    {
        this(HTMLPanel.createUniqueId());
    }
    
    /**
     * Create a progress bar with the specified ID.  The ID is required
     * because the progress bar needs a specific ID to connect to.
     * 
     * @param id     the id for the progress bar
     */
    public ProgressBar(String id)
    {
        super();
        getElement().setId(id);
    }

    @Override
    public void onAttach()
    {
        super.onAttach();
        addProgressBarJS(getElement().getId(), m_value);
    }
    
    /**
     * Set the value for this progress bar
     * 
     * @return the value of the bar
     */
    public int getValue()
    {
        return m_value;
    }
    
    /**
     * Get the title (or tooltip) string for the progress bar.  This method 
     * may be overridden to specify the value using alternative localization 
     * mechanisms. 
     * 
     * @param percentCompleted
     *               the percent completed
     * 
     * @return the title string with the percent completed
     */
    public String getTitleString(int percentCompleted)
    {
        return STRINGS.percentCompleted("" + percentCompleted);
    }
    
    /**
     * Get the value of this progress bar
     * 
     * @param value  the bar value
     */
    public void setValue(int value)
    {
        m_value = value;
        setValueJS(getElement().getId(), m_value);
        setTitle(getTitleString(value));
    }
    
    private static native void setValueJS(String id, int barValue) /*-{
        $wnd.jQuery('#' + id).progressbar('destroy');
        $wnd.jQuery('#' + id).progressbar({
            value: barValue
        });
    }-*/;
    
    private static native void addProgressBarJS(String id, int barValue) /*-{
        $wnd.jQuery("#" + id).progressbar({
            value: barValue
        });
    }-*/;
    
    
}
