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
package org.spiffyui.client.widgets.dialog;

import com.google.gwt.user.client.ui.HTML;

/**
 * This is a generic Confirm Dialog, which is modal.  It can
 * be extended depending on needs.
 */
public class ConfirmDialog extends Dialog
{
    
    /**
     * Create a new confirm dialog with the specified ID and title. 
     *  
     * @param id - the id of this element
     * @param title - the title of the dialog, which shows up as the "caption"
     */
    public ConfirmDialog(String id, String title)
    {
        super(id, title, "spiffy-dialog-confirm-caption");
        
        getDialogBody().addStyleName("spiffy-dialog-confirm-text");
        getDialogBody().getElement().setId(id + "_confirmText"); //needed for QA automation        
    } 


    /**
     * Set the text for the body of the confirm dialog.
     * @param text - HTML is allowed
     */
    public void setText(String text)
    {
        replaceDialogBodyContents(new HTML(text));
    }
    
}
