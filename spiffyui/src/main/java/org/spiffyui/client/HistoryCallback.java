/*******************************************************************************
 * 
 * Copyright 2011 Spiffy UI Team   
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
package org.spiffyui.client;

/**
 * The history callback interface is used to interact with browser history.  Programs
 * can add history events and control application navigation.
 * 
 * @see JSUtil.addHistoryItem
 */
public interface HistoryCallback
{
    /**
     * Called when the history item is reached in this browser history.  This can be
     * triggered by a move backward or forward in the browser history.
     * 
     * @param id     the id of this history event
     */
    void historyChanged(String id);
}
