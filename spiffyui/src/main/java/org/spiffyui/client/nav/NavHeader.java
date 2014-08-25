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
public class NavHeader extends Widget implements NavWidget
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
