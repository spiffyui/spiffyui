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

package org.spiffyui.spsample.client.widgets;

import org.spiffyui.client.JSONUtil;
import org.spiffyui.client.widgets.multivaluesuggest.MultivalueSuggestBox;
import org.spiffyui.client.widgets.multivaluesuggest.MultivalueSuggestRESTHelper;

import com.google.gwt.json.client.JSONObject;

/**
 * A subclass of MultivalueSuggestBox that shows suggestions in a fancier way.
 */
public class FancyAutocompleter extends MultivalueSuggestBox 
{

    /**
     * Constructor
     * @param restHelper the REST helper for getting remote values
     * @param isMultivalued
     *                   true if this suggest box supports multiple values and false otherwise
     */
    public FancyAutocompleter(MultivalueSuggestRESTHelper restHelper, boolean isMultivalued) 
    {
        super(restHelper, isMultivalued);
    }
    
    /**
     * Constructor
     * @param restHelper the REST helper for getting remote values
     * @param isMultivalued
     *                   true if this suggest box supports multiple values and false otherwise
     * @param placeFormFeedback
     *                   true if this control should place a form feedback and false otherwise
     */
    public FancyAutocompleter(MultivalueSuggestRESTHelper restHelper, boolean isMultivalued, boolean placeFormFeedback) 
    {
        super(restHelper, isMultivalued, placeFormFeedback);
    }
    
    /**
     * Create and return a new Option with fields populated
     * @param jsonOpt - the JSONObject to populate Option
     * @return a populated Option
     */
    protected Option createOption(JSONObject jsonOpt)
    {
        FancyOption option = new FancyOption();
        option.setName(JSONUtil.getStringValue(jsonOpt, getHelper().getNameKey()));
        option.setValue(JSONUtil.getStringValue(jsonOpt, getHelper().getValueKey()));
        option.setDescription(JSONUtil.getStringValue(jsonOpt, "Description"));
        option.setRgb(JSONUtil.getStringValue(jsonOpt, "RGB"));
        return option;
    }
    
    /**
     * Create and return a new OptionSuggestion with fields populated
     * @param o - the Option to get values to populate the OptionSuggestion
     * @param fullText - the full text in the text field of the suggest box
     * @param query - the query portion of the full text
     * @return a populated OptionSuggestion
     */
    protected OptionSuggestion createOptionSuggestion(Option o, String fullText, String query)
    {
        return new FancyOptionSuggestion((FancyOption) o, o.getName(), o.getValue(), fullText, query);
    }
    
    /**
     * A subclass of Option for extra fields to be included in the fancy suggestions list
     */
    public class FancyOption extends Option
    {
        String m_description;
        String m_rgb;
        /**
         * @return Returns the rgb.
         */
        public String getRgb()
        {
            return m_rgb;
        }
        /**
         * @param rgb The rgb to set.
         */
        public void setRgb(String rgb)
        {
            m_rgb = rgb;
        }
        /**
         * @return Returns the description.
         */
        public String getDescription()
        {
            return m_description;
        }
        /**
         * @param description The description to set.
         */
        public void setDescription(String description)
        {
            m_description = description;
        }
    }
       
        /**
     * A subclass of OptionSuggestion that uses FancyOption to generate a fancy display string
     * for the suggestions list
     */
    public class FancyOptionSuggestion extends OptionSuggestion
    {   
        FancyOption m_option;
        
        FancyOptionSuggestion(FancyOption o, String displ, String val, String replacePre, String query) 
        {
            super(displ, val, replacePre, query);
            m_option = o;
        }
        
        @Override
        public String getDisplayString()
        {
            /*
             * use the super method's default way of bolding the selection in the display string
             */
            String display = super.getDisplayString();
            /*
             * then also add the description and Base in separate divs
             */
            return "<div class=\"facItem\">" +
                        "<div class=\"facRgb\" style=\"background-color: rgb" + m_option.getRgb() + "\">" +
                        "</div>" +
                        "<div class=\"facName\">" +
                            display +
                        "</div>" +
                        "<div class=\"facDesc\">" +
                            m_option.getDescription() +
                        "</div>" +
                    "</div>";
        }
    }
}
