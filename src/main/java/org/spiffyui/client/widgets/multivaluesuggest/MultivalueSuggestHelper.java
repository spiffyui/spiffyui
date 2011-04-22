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
package org.spiffyui.client.widgets.multivaluesuggest;

/**
 * An abstract class that handles building of the autocomplete options
 */
public abstract class MultivalueSuggestHelper
{
    private String m_totalSizeKey;
    private String m_optionsKey;
    private String m_nameKey;
    private String m_valueKey;

    /**
     * Constructor
     * @param totalSizeKey - total size JSON key
     * @param optionsKey - options JSON key
     * @param nameKey - name JSON key for each option
     * @param valueKey - value JSON key for each option
     */
    public MultivalueSuggestHelper(String totalSizeKey, String optionsKey, String nameKey, String valueKey)
    {
        m_totalSizeKey = totalSizeKey;
        m_optionsKey = optionsKey;
        m_nameKey = nameKey;
        m_valueKey = valueKey;
    }

    /**
     * @return Returns the totalSizeKey.
     */
    public String getTotalSizeKey()
    {
        return m_totalSizeKey;
    }

    /**
     * @param totalSizeKey The totalSizeKey to set.
     */
    public void setTotalSizeKey(String totalSizeKey)
    {
        m_totalSizeKey = totalSizeKey;
    }

    /**
     * @return Returns the optionsKey.
     */
    public String getOptionsKey()
    {
        return m_optionsKey;
    }

    /**
     * @param optionsKey The optionsKey to set.
     */
    public void setOptionsKey(String optionsKey)
    {
        m_optionsKey = optionsKey;
    }

    /**
     * @return Returns the nameKey.
     */
    public String getNameKey()
    {
        return m_nameKey;
    }

    /**
     * @param nameKey The nameKey to set.
     */
    public void setNameKey(String nameKey)
    {
        m_nameKey = nameKey;
    }

    /**
     * @return Returns the valueKey.
     */
    public String getValueKey()
    {
        return m_valueKey;
    }

    /**
     * @param valueKey The valueKey to set.
     */
    public void setValueKey(String valueKey)
    {
        m_valueKey = valueKey;
    }
    
}
