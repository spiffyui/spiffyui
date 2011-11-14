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
 * The HistoryTokenListener gives applications a chance to listen for all history 
 * tokens.  This mechanism differes from the HistoryCallback since it adds a more 
 * generic mechanism and requires the listener to determine which tokens it is 
 * intertested in.  However, this mechanism persists history objects across browser
 * refresh and the HistoryCallback does not.
 * 
 * @see JSUtil.addHistoryTokenListener
 */
public interface HistoryTokenListener
{
    /**
     * Called when a history event is triggered with a history token.
     * 
     * @param id     the history token
     */
    void tokenTrigger(String id);
}
