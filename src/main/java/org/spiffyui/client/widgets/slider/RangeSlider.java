/*
 * ========================================================================
 *
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
package org.spiffyui.client.widgets.slider;

import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;



/**
 * This is a convenient sub class of Slider just for ranges
 */
public class RangeSlider extends Slider
{    
    /**
     * Constructor for the RangeSlider
     * @param id - element ID
     * @param min - the minimum possible value of the slider
     * @param max - the maximum possible value of the slider
     * @param defaultMin - the default value of the lowest anchor
     * @param defaultMax - the default value of the highest anchor
     */
    public RangeSlider(String id, int min, int max, int defaultMin, int defaultMax)
    {
        super(id, getOptions(min, max, defaultMin, defaultMax));
    }

    /**
     * A convenient way to create an options JSONObject for the RangeSlider.
     * @param min - default minimum of the slider
     * @param max - default maximum of the slider
     * @param defaultMin - the default value of the lowest anchor
     * @param defaultMax - the default value of the highest anchor
     * @return a JSONObject of RangeSlider options
     */
    public static JSONObject getOptions(int min, int max, int defaultMin, int defaultMax) 
    {
        JSONObject options = Slider.getOptions(min, max, new int[]{defaultMin, defaultMax});
        options.put(SliderOption.RANGE.toString(), JSONBoolean.getInstance(true));        
        return options;
    }
    
    /**
     * Convenience method for when range is true, gets the minimum of the selected range, or in other words,
     * gets the value of the lower anchor
     * @return the value
     */
    public int getValueMin()
    {
        return getValueAtIndex(0);
    }
    
    /**
     * Convenience method for when range is true, gets the maximum of the selected range, or in other words,
     * gets the value of the higher anchor
     * @return the value
     */
    public int getValueMax()
    {
        return getValueAtIndex(1);
    }

    /**
     * Convenience method for when range is true, sets both the min and max anchors
     * @param min - the lower anchor's value
     * @param max - the upper anchor's value
     */
    public void setValues(int min, int max)
    {
        setValues(new int[]{min, max});
    }
    
}
