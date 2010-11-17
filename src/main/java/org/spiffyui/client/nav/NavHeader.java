/*
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
package org.spiffyui.client.nav;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;

/**
 * <p>
 * This widget represents a single header element <code>H2</code> to the navigation menu.
 * </p>
 * 
 * <h3>CSS Style Rules</h3>
 * 
 * <ul>
 * <li>.main-menuHeader { primry style }</li>
 * </ul>
 */
public class NavHeader extends Widget
{
    /**
     * Creates a new navigation header
     * 
     * @param text   the text for the header element
     */
    public NavHeader(String text)
    {
        setElement(Document.get().createHElement(2));
        getElement().setInnerText(text);
        setStyleName("main-menuHeader");
    }

    /**
     * Set the text of this header item.
     * 
     * @param text   the text to set
     */
    public void setText(String text)
    {
        getElement().setInnerText(text);
    }

    /**
     * Get the text of this item.
     * 
     * @return the text of the item
     */
    public String getText()
    {
        return getElement().getInnerText();
    }
}
