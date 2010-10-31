package com.novell.spiffyui.client.widgets.button;

import com.google.gwt.user.client.ui.Button;

/**
 * A GWT button that has an inprogress status
 */
public class SimpleButton extends Button
{
    private boolean m_inProgress = false;
    
    /**
     * Constructor
     * @param text of the button
     */
    public SimpleButton(String text)
    {
        super(text);
    }
    /**
     * Sets this button to be in progress.  In progress buttons show a different
     * image and are disabled
     * 
     * @param inprogress true if in progress and false otherwise
     */
    public void setInProgress(boolean inprogress)
    {
        m_inProgress = inprogress;
        setEnabled(!inprogress);
        
        if (inprogress) {
            getElement().addClassName("spiffy-inprogress");
        } else {
            getElement().removeClassName("spiffy-inprogress");
        }
    }
    
    /**
     * Determines if this button is in progress
     * 
     * @return true if it is in progress and false otherwise
     */
    public boolean isInProgress()
    {
        return m_inProgress;
    }
}
