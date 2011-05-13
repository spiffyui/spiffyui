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
package org.spiffyui.client.widgets;

import com.google.gwt.user.client.ui.Label;

/**
 * This widget shows an icon to give feedback on form fields
 */
public class FormFeedback extends Label
{
    /**
     * Feedback status warning
     */
    public static final int WARNING = 1;

    /**
     * Feedback status error
     */
    public static final int ERROR = 2;
    
    /**
     * Feedback status valid
     */
    public static final int VALID = 3;
    
    /**
     * Feedback status loading
     */
    public static final int LOADING = 4;

    /**
     * Feedback status none
     */
    public static final int NONE = 5;
    
    private static final String WARNING_STYLE = "warning";
    private static final String ERROR_STYLE = "error";
    private static final String VALID_STYLE = "valid";
    private static final String LOADING_STYLE = "loading";
    
    private int m_status = NONE;
    
    /**
     * Creates a new FormFeedback widget
     */
    public FormFeedback()
    {
        this(true);
    }
    
    /**
     * Creates a new FormFeedback widget
     * 
     * @param inform true if this feedback widget needs our standard for styles 
     *        and false otherwise
     */
    public FormFeedback(boolean inform)
    {
        getElement().addClassName("spiffy-formfeedback");
        if (inform) {
            getElement().addClassName("spiffy-formfeedback-inform");
        }
    }
    
    private void removeStyles()
    {
        getElement().removeClassName(WARNING_STYLE);
        getElement().removeClassName(ERROR_STYLE);
        getElement().removeClassName(LOADING_STYLE);
        getElement().removeClassName(VALID_STYLE);
    }
    
    /**
     * Sets the status for this FormFeedback
     * 
     * @param status the status
     */
    public void setStatus(int status)
    {
        m_status = status;
        removeStyles();
        
        switch (status) {
        case WARNING:
            getElement().addClassName(WARNING_STYLE);
            return;
        case ERROR:
            getElement().addClassName(ERROR_STYLE);
            return;
        case LOADING:
            getElement().addClassName(LOADING_STYLE);
            return;
        case VALID:
            getElement().addClassName(VALID_STYLE);
            return;
        default:
            setTitle("");
            removeStyles();
            return;
        }
    }
    
    /**
     * Gets the status of this FormFeedback
     * 
     * @return the form feedback
     */
    public int getStatus()
    {
        return m_status;
    }
}
