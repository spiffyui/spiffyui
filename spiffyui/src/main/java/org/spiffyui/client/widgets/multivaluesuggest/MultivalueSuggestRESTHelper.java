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
