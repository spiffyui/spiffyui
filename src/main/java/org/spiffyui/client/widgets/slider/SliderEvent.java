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

import com.google.gwt.user.client.Event;

/**
 * A class to hold event values for the Slider
 */
public class SliderEvent
{
    private int[] m_values;
    private boolean m_hasOriginalEvent = true;
    private Slider m_source;
    private Event m_event;
    
    /**
     * Create a new slider event.
     * 
     * @param event - the event received by JSNI called
     * @param source - the Slider that fires the event
     * @param values - int array of values
     */
    public SliderEvent(Event event, Slider source, int[] values)
    {
        this(event, source, values, true);
    }

    /**
     * Create a new slider event.
     * 
     * @param event - the event received by JSNI called
     * @param source - the Slider that fires the event
     * @param values - int array of values
     * @param hasOriginalEvent - boolean if the change came from a non-programmatic change such as mouse or keyboard event
     */
    public SliderEvent(Event event, Slider source, int[] values, boolean hasOriginalEvent)
    {
        m_source = source;
        m_event = event;
        
        /*
         We want to copy the array of values to make sure nobody can change
         the array from under us.
         */
        m_values = new int[values.length];
        System.arraycopy(values, 0, m_values, 0, values.length);

        m_hasOriginalEvent = hasOriginalEvent;
    }
    
    /**
     * @return Returns the JSNI returned JavaScriptObject event.
     */
    public Event getEvent()
    {
        return m_event;
    }

    /**
     * Get the source of the event.
     * 
     * @return Returns the source.
     */
    public Slider getSource()
    {
        return m_source;
    }
    
    /**
     * Get the values from the event.
     * 
     * @return Returns the value.
     */
    public int[] getValues()
    {
        return m_values;
    }

    /**
     * Does this event have an original event.
     * 
     * @return Returns the hasOriginalEvent.
     */
    public boolean hasOriginalEvent()
    {
        return m_hasOriginalEvent;
    }

}
