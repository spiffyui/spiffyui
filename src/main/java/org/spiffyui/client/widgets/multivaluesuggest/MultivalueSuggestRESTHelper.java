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
package org.spiffyui.client.widgets.multivaluesuggest;

/**
 * An abstract class that handles building of the REST endpoint URL and 
 * helps with parsing the JSON payload.
 */
public abstract class MultivalueSuggestRESTHelper extends MultivalueSuggestHelper
{
    /**
     * Constructor
     * @param totalSizeKey - total size JSON key
     * @param optionsKey - options JSON key
     * @param nameKey - name JSON key for each option
     * @param valueKey - value JSON key for each option
     */
    public MultivalueSuggestRESTHelper(String totalSizeKey, String optionsKey, String nameKey, String valueKey)
    {
        super(totalSizeKey, optionsKey, nameKey, valueKey);
    }

    /**
     * Build the REST URL with the specified parameters
     * 
     * @param q         the query
     * @param indexFrom the starting index
     * @param indexTo   the ending index
     * 
     * @return the URL with the specified parameters
     */
    public abstract String buildUrl(String q, int indexFrom, int indexTo);

}
