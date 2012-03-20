/*
 * ========================================================================
 *
 * Copyright (c) 2005 Unpublished Work of Novell, Inc. All Rights Reserved.
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
package org.spiffyui.spsample.server;

import org.spiffyui.server.filter.GWTLocaleBundleFilter;

/**
 * <p>This is a sample locale filter using the Spiffy UI locale filter. </p> 
 *  
 * <p>This filter finds the supported locales in the application using the 
 * list of properties files and adds the GWT locale property to the source 
 * of the HTML page to make sure that the client and server agree on the same 
 * locale.</p> 
 *  
 *  
 */
public class SampleLocaleFilter extends GWTLocaleBundleFilter
{
    @Override
    protected String getResourcePath()
    {
        return "/WEB-INF/classes/org/spiffyui/spsample/client/i18n";
    }
}

