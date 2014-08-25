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

/**
 * A listener for events on the navigation bar or the header (for any panel changing including logout).
 *
 */
public interface NavBarListener
{
    /**
     * Give the listener a chance to cancel the itemSelected event
     * @param item   the item that was selected 
     * @return boolean true if NavBar should continue on to itemSelected, false to cancel
     */
    boolean preItemSelected(NavItem item);
    
    /**
     * Called when a navigation item is selected
     * 
     * @param item   the item that was selected
     */
    void itemSelected(NavItem item);
}
