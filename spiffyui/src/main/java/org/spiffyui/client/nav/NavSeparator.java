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
 * This widget represents a single separator item on the navigation menu.
 * </p>
 * 
 * <h3>CSS Style Rules</h3>
 * 
 * <ul>
 * <li>.main-menuSeparator { primry style }</li>
 * </ul>
 */

public class NavSeparator extends Widget implements NavWidget
{
    /**
     * Creates a new navigation separator
     * 
     * @param id     the id of the separator
     */
    public NavSeparator(String id)
    {
        setElement(Document.get().createDivElement());
        getElement().setId(id);
        setStyleName("main-menuSeparator");
    }
}
