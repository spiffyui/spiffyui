/*
 * ========================================================================
 *
 * Copyright (c) 2010, 2011 Unpublished Work of Novell, Inc. All Rights Reserved.
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
package org.spiffyui.client.widgets.slider;

/**
 * The listener interface for the sliders
 *
 */
public interface SliderListener
{
    /**
     * This event is triggered when the user starts sliding
     * @param e SliderEvent
     */
    public void onStart(SliderEvent e);
    
    /**
     * This event is triggered on every mouse move during slide. 
     * Return false in order to prevent a slide, based on a value.
     * @param e SliderEvent
     * @return boolean false to prevent the slide
     */
    public boolean onSlide(SliderEvent e);
    
    /**
     * This event is triggered on slide stop, or if the value is changed programmatically (by the value method). 
     * Use SliderEvent.hasOriginalEvent() to detect whether the value changed by mouse or keyboard.  When false
     * it means the change was done programmatically. 
     * @param e SliderEvent
     */
    public void onChange(SliderEvent e);
    
    /**
     * This event is triggered when the user stops sliding.
     * @param e SliderEvent
     */
    public void onStop(SliderEvent e);
}
