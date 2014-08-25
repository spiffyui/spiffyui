/*******************************************************************************
 * 
 * Copyright 2011-2014 Spiffy UI Team   
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
package org.spiffyui.client.widgets.dialog;

import com.google.gwt.user.client.ui.Label;

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
     * If you want to use HTML, then use replaceDialogBodyContents
     * directly.
     * @param text - HTML is not allowed
     */
    public void setText(String text)
    {
        replaceDialogBodyContents(new Label(text));
    }
    
}
