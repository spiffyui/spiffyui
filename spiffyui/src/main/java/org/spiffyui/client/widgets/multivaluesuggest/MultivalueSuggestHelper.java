/*******************************************************************************
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
 * An abstract class that handles building of the autocomplete options
 */
public abstract class MultivalueSuggestHelper
{
    private String m_totalSizeKey;
    private String m_optionsKey;
    private String m_nameKey;
    private String m_valueKey;

    /**
     * Constructor
     * @param totalSizeKey - total size JSON key
     * @param optionsKey - options JSON key
     * @param nameKey - name JSON key for each option
     * @param valueKey - value JSON key for each option
     */
    public MultivalueSuggestHelper(String totalSizeKey, String optionsKey, String nameKey, String valueKey)
    {
        m_totalSizeKey = totalSizeKey;
        m_optionsKey = optionsKey;
        m_nameKey = nameKey;
        m_valueKey = valueKey;
    }

    /**
     * @return Returns the totalSizeKey.
     */
    public String getTotalSizeKey()
    {
        return m_totalSizeKey;
    }

    /**
     * @param totalSizeKey The totalSizeKey to set.
     */
    public void setTotalSizeKey(String totalSizeKey)
    {
        m_totalSizeKey = totalSizeKey;
    }

    /**
     * @return Returns the optionsKey.
     */
    public String getOptionsKey()
    {
        return m_optionsKey;
    }

    /**
     * @param optionsKey The optionsKey to set.
     */
    public void setOptionsKey(String optionsKey)
    {
        m_optionsKey = optionsKey;
    }

    /**
     * @return Returns the nameKey.
     */
    public String getNameKey()
    {
        return m_nameKey;
    }

    /**
     * @param nameKey The nameKey to set.
     */
    public void setNameKey(String nameKey)
    {
        m_nameKey = nameKey;
    }

    /**
     * @return Returns the valueKey.
     */
    public String getValueKey()
    {
        return m_valueKey;
    }

    /**
     * @param valueKey The valueKey to set.
     */
    public void setValueKey(String valueKey)
    {
        m_valueKey = valueKey;
    }
    
}
