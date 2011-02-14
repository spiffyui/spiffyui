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

/**
 * An enumeration of all the options you can set for a slider
 */
public enum SliderOption
{
    /**
     * disabled. Type Boolean. Default: false
     * Disables (true) or enables (false) the slider. Can be set when initialising (first creating) the slider.
     * 
     */
    DISABLED("disabled"),

    /**
     * animate. Type: Boolean, String, Number. Default: false
     * Whether to slide handle smoothly when user click outside handle on the bar. 
     * Will also accept a string representing one of the three predefined speeds ("slow", "normal", or "fast") 
     * or the number of milliseconds to run the animation (e.g. 1000).
     */
    ANIMATE("animate"),

    /**
     * max. Type: Number. Default: 100
     * The maximum value of the slider.
     */
    MAX("max"),

    /**
     * min. Type: Number. Default: 0
     * The minimum value of the slider.
     */
    MIN("min"),

    /**
     * orientation. Type: String. Default: 'horizontal'
     * This option determines whether the slider has the min at the left,
     * the max at the right or the min at the bottom, the max at the top. 
     * Possible values: 'horizontal', 'vertical'..
     */
    ORIENTATION("orientation"),

    /**
     * range. Type: Boolean, String. Default: false
     * If set to true, the slider will detect if you have two handles and create a stylable range element between these two. 
     * Two other possible values are 'min' and 'max'. 
     * A min range goes from the slider min to one handle. 
     * A max range goes from one handle to the slider max.
     */
    RANGE("range"),

    /**
     * step. Type: Number. Default: 1
     * Determines the size or amount of each interval or step the slider takes between min and max. 
     * The full specified value range of the slider (max - min) needs to be evenly divisible by the step.
     */
    STEP("step"),
    
    /**
     * value. Type: Number. Default: 0
     * Determines the value of the slider, if there's only one handle. 
     * If there is more than one handle, determines the value of the first handle.
     */
    VALUE("value"),

    /**
     * values. Type: Array. Default: null
     * This option can be used to specify multiple handles. If range is set to true, the length of 'values' should be 2.
     */
    VALUES("values");

    private String m_name;

    private SliderOption(String name)
    {
        m_name = name;
    }

    @Override
    public String toString()
    {
        return m_name;
    }
}