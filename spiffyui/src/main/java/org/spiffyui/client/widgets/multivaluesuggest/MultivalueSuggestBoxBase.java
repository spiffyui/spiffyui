/*******************************************************************************
 *
 * Copyright 2011-2012 Spiffy UI Team   
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.spiffyui.client.JSONUtil;
import org.spiffyui.client.JSUtil;
import org.spiffyui.client.i18n.SpiffyUIStrings;
import org.spiffyui.client.rest.RESTException;
import org.spiffyui.client.rest.RESTObjectCallBack;
import org.spiffyui.client.widgets.FormFeedback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Callback;
import com.google.gwt.user.client.ui.SuggestOracle.Request;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;

/**
* A SuggestBox that allows for multiple values selection and autocomplete.
*/
public abstract class MultivalueSuggestBoxBase extends Composite implements SelectionHandler<Suggestion>, Focusable, KeyUpHandler, 
    HasValueChangeHandlers<String>
{
    private MultivalueSuggestHelper m_helper;

    private SuggestBox m_field;
    private Map<String, String> m_valueMap;
    private int m_indexFrom = 0;
    private int m_indexTo = 0;
    private int m_findExactMatchesTotal = 0;
    private int m_findExactMatchesFound = 0;
    private List<String> m_findExactMatchesNot = new ArrayList<String>();

    private String m_displaySeparator = ", ";
    private String m_valueDelim = ";"; 
    private String m_selectedItemEq = "~";
    
    private int m_pageSize = 15;    
    private int m_delay = 1000;
    private int m_findExactMatchQueryLimit = 20;

    private FormFeedback m_feedback;
    private boolean m_isMultivalued = false;
    private String m_validText;
    private String m_loadingText;

    private static final SpiffyUIStrings STRINGS = (SpiffyUIStrings) GWT.create(SpiffyUIStrings.class);

    private final String m_selectedItemsContainerId = HTMLPanel.createUniqueId();
    private final String m_suggestBoxContainerId = HTMLPanel.createUniqueId();
    private final Stack<SelectedItem> m_selectedItems = new Stack<SelectedItem>();
    private int m_lastCurPos = 0;
    
    private HTMLPanel m_panel;
    
    /**
     * Constructor that will place the FormFeedback for you.
     * 
     * @param restHelper the REST helper for getting remote values for this suggest box
     * @param isMultivalued
     *                   whether or not to allow multiple values
     */
    public MultivalueSuggestBoxBase(MultivalueSuggestHelper restHelper, boolean isMultivalued)
    {
        this(restHelper, isMultivalued, true);
    }
    /**
     * Constructor.
     * @param helper - a MultivalueSuggestHelper object
     * @param isMultivalued - whether or not to allow multiple values
     * @param placeFormFeedback - if false, the FormFeedback will be placed by the calling class
     */
    public MultivalueSuggestBoxBase(MultivalueSuggestHelper helper, boolean isMultivalued, boolean placeFormFeedback)
    {
        m_helper = helper;
        m_isMultivalued = isMultivalued;

        
        m_panel = createMainPanel(isMultivalued, m_selectedItemsContainerId, m_suggestBoxContainerId);
        m_panel.addStyleName("spiffy-mvsb");
        m_panel.getElement().setId(HTMLPanel.createUniqueId());
        
        final TextBoxBase textfield = new TextBox();
//        if (isMultivalued) {
//            m_panel.addStyleName("textarearow");
////            textfield = new TextArea();
//        } else {
//            m_panel.addStyleName("textfieldrow");
////            textfield = new TextBox();
//        }

        //Create our own SuggestOracle that queries REST endpoint
        SuggestOracle oracle = new RestSuggestOracle();
        //initialize the SuggestBox
        m_field = new SuggestBox(oracle, textfield);
        m_feedback = new FormFeedback();
        
        if (isMultivalued) {
            m_panel.addStyleName("wideTextField");        
            m_panel.addStyleName("multivalue");
            //have to do this here b/c gwt suggest box wipes 
            //style name if added in previous if
            textfield.addStyleName("multivalue"); 
            
            textfield.addKeyPressHandler(new KeyPressHandler()
            {
                @Override
                public void onKeyPress(KeyPressEvent event)
                {
                    adjustTextWidth();
                    m_lastCurPos = textfield.getCursorPos();
                }
            });
        } else {
            m_field.addStyleName("wideTextField");
        }
        m_field.addSelectionHandler(this);
        m_field.getTextBox().addKeyUpHandler(this);
                
        m_panel.add(m_field, m_suggestBoxContainerId);
        
        if (placeFormFeedback) {
            m_panel.add(m_feedback);
        }
        
        initWidget(m_panel);

        /*
         * Create a Map that holds the values that should be stored.
         * It will be keyed on "display value", so that any time a "display value" is added or removed
         * the valueMap can be updated.
         */
        m_valueMap = new HashMap<String, String>();

        resetPageIndices();      

        m_validText = STRINGS.valid();
        m_loadingText = STRINGS.loading();
    }

    /**
     * Get the ID of the suggest box container.
     * 
     * @return the container ID
     */
    public String getSuggestBoxContainerId()
    {
        return m_suggestBoxContainerId;
    }
    
    @Override
    public void onLoad()
    {
        /*
         * adjust the text width once this has been
         * added to the DOM, for multivalued
         */
        if (m_isMultivalued) {
            adjustTextWidth();
            bindFocusHandler(getElement().getId(), m_suggestBoxContainerId);
        }
    }
    
    private static HTMLPanel createMainPanel(boolean multivalued, String selectedContainerId, String mvsuggestContainerId)
    {
        return new HTMLPanel("<span class=\"spiffy-mvsb-selected-items\" id=\"" + selectedContainerId + "\"></span>" +
                "<span class=\"spiffy-mvsb-input\" id=\"" + mvsuggestContainerId + "\"></span>");
    }
    /**
     * Get the suggest helper for this suggest box.
     * 
     * @return the suggest helper
     */
    protected MultivalueSuggestHelper getHelper()
    {
        return m_helper;
    }


    private void resetPageIndices()
    {
        m_indexFrom = 0;
        m_indexTo = m_indexFrom + m_pageSize - 1;
    }

    /**
     * Convenience method to set the status and tooltip of the FormFeedback
     * @param status - a FormFeedback status
     * @param tooltip - a String tooltip
     */
    public void updateFormFeedback(int status, String tooltip)
    {
        m_feedback.setStatus(status);
        if (tooltip != null) {
            m_feedback.setTitle(tooltip);
        }

        if (!JSUtil.isMobile()) {
            TextBoxBase textBox = m_field.getTextBox();
            if (FormFeedback.LOADING == status) {
                textBox.setEnabled(false);
            } else {
                textBox.setEnabled(true);
                textBox.setFocus(false); //Blur then focus b/c of a strange problem with the cursor or selection highlights no longer visible within the textfield (this is a workaround) 
                textBox.setFocus(true);
            }
        }
    }

    private void putValue(Option option)
    {        
        String key = option.getName();
        String value = option.getValue();
        JSUtil.println("putting key = " + key + "; value = " + value);
        m_valueMap.put(key, value);
        
        if (m_isMultivalued) {
            createAndPushSelectedItem(option);
        }
        
        ValueChangeEvent.fire(this, value);
    }

    private void removeValue(SelectedItem item)
    {
        String key = item.getOption().getName();
        String value = m_valueMap.get(key);
        JSUtil.println("removing key = " + key + "; value = " + value);
        m_valueMap.remove(key);
        
        item.removeFromParent();
        
        ValueChangeEvent.fire(this, value);    
    }
    /**
     * Get the value(s) as a String.  If allowing multivalues, separated by the VALUE_DELIM.
     * This is different from getValuesAsString, which includes the display text as well.
     * @return value(s) as a String
     */
    public String getValue()
    {
        //String together all the values in the valueMap
        //based on the display values shown in the field
        String text = m_field.getText();

        String values = "";
        String invalids = "";
//        String newKeys = "";
        if (m_isMultivalued) {
            for (String key : m_valueMap.keySet()) {
                key = key.trim();
                if (!key.isEmpty()) {
                    String v = m_valueMap.get(key);
                    JSUtil.println("getValue for key = " + key + " is v = " + v);
                    if (null != v) {
                        values += v + m_valueDelim;
                        //rebuild newKeys removing invalids and dups
//                        newKeys += key + m_displaySeparator;
                    } else {
                        invalids += key + m_displaySeparator;
                    }
                }
            }
            values = trimLastDelimiter(values, m_valueDelim);
            //set the new display values
//            m_field.setText(newKeys);
        } else {
            values = m_valueMap.get(text);
        }

        //if there were any invalid show warning
        if (!invalids.isEmpty()) {
            //trim last separator
            invalids = trimLastDelimiter(invalids, m_displaySeparator);
            updateFormFeedback(FormFeedback.ERROR, getInvalidText(invalids));
        }
        return values;
    }

    /**
     * Get the text and values combined as a string with each
     * selected item starting with m_selectedItemStart and ending with
     * m_selectedItemEnd, and the display text and value separated by
     * m_selectedItemEq
     * 
     * @return the text representation of all the selected items
     */
    public String getValuesAsString()
    {
        if (!m_isMultivalued) {
            StringBuffer sb = new StringBuffer();
            sb.append(getText());
            sb.append(m_selectedItemEq);
            sb.append(getValue());
            return sb.toString();
        }
        
        if (m_selectedItems.size() == 0) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        for (SelectedItem si : m_selectedItems) {
            String displ = si.getOption().getName();
            sb.append(displ);
            sb.append(m_selectedItemEq);
            sb.append(m_valueMap.get(displ));
            sb.append(m_valueDelim);
        }
        
        return JSUtil.trimLastDelimiter(sb.toString(), m_valueDelim);
    }
    
    /**
     * Get the value map
     * @return value map
     */
    public Map<String, String> getValueMap()
    {
        return m_valueMap;
    }
        

    /**
     * Get the Options that were selected
     * @return Returns the List of selected Option beans.
     */
    public List<Option> getSelectedOptions()
    {
        List<Option> options = new ArrayList<Option>();
        for (SelectedItem si : m_selectedItems) {
            options.add(si.getOption());
        }
        return options;
    }
    
    /**
     * This only applies when allowing multivalues.
     * If single-valued, then this does nothing.
     * (For single-valued, use setValueMap or setValuesAsString.)
     * 
     * @param option - the Option to add
     */
    public void addSelectedOption(Option option)
    {
        if (!m_isMultivalued) {
            return;
        }
        createAndPushSelectedItem(option);
        m_field.setText("");
        m_valueMap.put(option.getName(), option.getValue());
    }
    
    /**
     * This only applies when allowing multivalues.
     * If single-valued, then this method does nothing.
     * (For single-valued, use setValueMap or setValuesAsString.)
     * @param options - List of Option beans
     */
    public void setSelectedOptions(List<Option> options)
    {
        if (!m_isMultivalued) {
            return;
        }
        m_valueMap.clear();
        
        for (SelectedItem si : m_selectedItems) {
            si.removeFromParent();
        }
        m_selectedItems.clear();
            
        for (Option option : options) {
            addSelectedOption(option);
        }
    }
    /**
     * Call this method to set the default values.
     * If it is multi-valued, SelectedItems
     * will be added for each entry.  Note: only Option base class
     * will be used for SelectedItem objects.  To add SelectedItems with descendant
     * of Option, use addSelectedOption or setSelectedOptions
     * @param valueMap the valueMap to set
     */
    public void setValueMap(Map<String, String> valueMap)
    {
        m_valueMap = valueMap;
        
        JSUtil.println("remove from parent...");
        for (SelectedItem si : m_selectedItems) {
            si.removeFromParent();
        }
        m_selectedItems.clear();
            
        for (String key : valueMap.keySet()) {
            if (m_isMultivalued) {
                Option option = new Option(key, valueMap.get(key));
                createAndPushSelectedItem(option);
                m_field.setText("");
            } else {
                m_field.setText(key);
            }
        }
            
    }
    
    /**
     * Set the text of this compound search box
     * 
     * @param text   the text to use
     */
    public void setValuesAsString(String text)
    {
        /*
         * tokenize based on m_valueDelim
         */
        String[] tokens = text.split(m_valueDelim);
        Map<String, String> valMap = new LinkedHashMap<String, String>();
        for (int i = 0, len = tokens.length; i < len; i++) {
            String[] keyValue = tokens[i].split(m_selectedItemEq);
            if (keyValue.length == 2) {
                valMap.put(keyValue[0], keyValue[1]);                
            } else if (keyValue.length == 1) {
                valMap.put(keyValue[0], keyValue[0]);  
            } else if (keyValue.length > 2) {
                valMap.put(keyValue[0], tokens[i].substring(tokens[i].indexOf(m_selectedItemEq) + 1));
            }
        }
        setValueMap(valMap);
    }
    
    private void createAndPushSelectedItem(Option option)
    {
        /*
         * Create span for this item
         */        
        SelectedItem item = createSelectedItem(option);
        m_panel.add(item, m_selectedItemsContainerId);
        m_selectedItems.push(item);
    }

    /**
     * Create and return a SelectedItem populated with the option
     * @param option - an Option bean
     * @return the SelectedItem to be pushed
     */
    protected SelectedItem createSelectedItem(Option option)
    {
        return new SelectedItem(HTMLPanel.createUniqueId(), option);
    }
    
    /**
    * If there is more than one key in the text field,
    * check that every key has a value in the map.
    * For any that do not, try to find its exact match.
    */
    private void findExactMatches()
    {
        String text = m_field.getText();
        String[] keys = text.split(m_displaySeparator.trim());
        int len = keys.length;       
        if (len < 2) {
            //do not continue.  if there's 1, it is the last one, and getSuggestions can handle it
            return;
        }

        m_findExactMatchesTotal = 0;
        m_findExactMatchesFound = 0;
        m_findExactMatchesNot.clear();
        for (int pos = 0; pos < len; pos++) {
            String key = keys[pos].trim();

            if (!key.isEmpty()) {
                String v = m_valueMap.get(key);
                if (null == v) {
                    m_findExactMatchesTotal++;
                }
            }
        }
        //then loop through again and try to find them
        /*
         * We may have invalid values due to a multi-value copy-n-paste,
         * or going back and messing with a middle or first key;
         * so for each invalid value, try to find an exact match.                     * 
         */
        for (int pos = 0; pos < len; pos++) {
            String key = keys[pos].trim();
            if (!key.isEmpty()) {
                String v = m_valueMap.get(key);
                if (null == v) {
                    findExactMatch(key, pos);
                }
            }
        }        
    }

    private void findExactMatch(final String displayValue, final int position)
    {
        updateFormFeedback(FormFeedback.LOADING, m_loadingText);

        queryOptions(displayValue, 0, 
                     m_findExactMatchQueryLimit, //return a relatively small amount in case wanted "Red" and "Brick Red" is the first thing returned               \
                     new RESTObjectCallBack<OptionResultSet>() {

                         @Override
                         public void error(String message)
                         {
                             // an exact match couldn't be found, just increment not found
                             m_findExactMatchesNot.add(displayValue);
                             finalizeFindExactMatches();
                         }

                         @Override
                         public void error(RESTException e)
                         {
                             // an exact match couldn't be found, just increment not found
                             m_findExactMatchesNot.add(displayValue);
                             finalizeFindExactMatches();
                         }

                         @Override
                         public void success(OptionResultSet optResults)
                         {
                             handleExactMatch(displayValue, position, optResults);
                         }
                     });
    }

    private void handleExactMatch(String displayValue, int position, OptionResultSet optResults)
    {
        int totSize = optResults.getTotalSize();
        if (totSize == 1) {
            //an exact match was found, so place it in the value map
            Option option = optResults.getOptions()[0];                        
            exactMatchFound(displayValue, position, option);
        } else {
            //try to find the exact matches within the results
            boolean found = false;
            for (Option option : optResults.getOptions()) {
                if (displayValue.equalsIgnoreCase(option.getName())) {
                    exactMatchFound(displayValue, position, option);
                    found = true;
                    break;
                }
            }
            if (!found) {
                m_findExactMatchesNot.add(displayValue);
                JSUtil.println("RestExactMatchCallback -- exact match not found for displ = " + displayValue);
            }
        }
        finalizeFindExactMatches();

    }

    private void exactMatchFound(String displayValue, final int position, Option option)
    {
        putValue(option);
        JSUtil.println("extactMatchFound ! exact match found for displ = " + displayValue);

        //and replace the text if single valued, otherwise clear b/c a SelectedItem will be added
        if (!m_isMultivalued) {
            String text = m_field.getText();
            String[] keys = text.split(m_displaySeparator.trim());
            keys[position] = option.getName();
            String join = "";
            for (String n : keys) {
                join += n.trim() + m_displaySeparator;
            }
            join = trimLastDelimiter(join, m_displaySeparator);
            m_field.setText(join);
        } else {
            m_field.setText("");
        }

        m_findExactMatchesFound++;
    }

    private void finalizeFindExactMatches()
    {
        if (m_findExactMatchesFound + m_findExactMatchesNot.size() == m_findExactMatchesTotal) {
            //when the found + not = total, we're done
            if (m_findExactMatchesNot.size() > 0) {
                String join = "";
                for (String val : m_findExactMatchesNot) {
                    join += val.trim() + m_displaySeparator;
                }
                join = trimLastDelimiter(join, m_displaySeparator);                                
                updateFormFeedback(FormFeedback.ERROR, getInvalidText(join));
            } else {
                updateFormFeedback(FormFeedback.VALID, m_validText);
            }
        }
    }


    /**
     * Returns a String without the last delimiter
     * @param s - String to trim
     * @param delim - the delimiter
     * @return the String without the last delimter
     */
    private static String trimLastDelimiter(String s, String delim)
    {
        if (s.length() > 0) {
            return s.substring(0, s.length() - delim.length());
        } else {
            return s;
        }
    }

    /**
     * Create and return a new Option with fields populated
     * @param jsonOpt - the JSONObject to populate Option
     * @return a populated Option
     */
    protected Option createOption(JSONObject jsonOpt)
    {
        Option option = new Option();
        option.setName(JSONUtil.getStringValue(jsonOpt, m_helper.getNameKey()));
        option.setValue(JSONUtil.getStringValue(jsonOpt, m_helper.getValueKey()));
        return option;
    }
    
    private void adjustTextWidth()
    {
        m_field.getTextBox().setWidth((getTextWidth("#" + m_suggestBoxContainerId + " > input") + 20) + "px");
    }

    private static native int getTextWidth(String textFieldSelector) /*-{
        return $wnd.jQuery(textFieldSelector).textWidth();
    }-*/;
    
    private static native void bindFocusHandler(String panelId, String suggestBoxContainerId) /*-{
        //$wnd.jQuery("#" + panelId).css('background', 'green');
        $wnd.jQuery("#" + panelId).click(function(evt) {
            $wnd.jQuery("#" + suggestBoxContainerId + " > input").focus();
        });
    }-*/;
    
    /**
     * Create and return a new OptionSuggestion with fields populated
     * @param o - the Option to get values to populate the OptionSuggestion
     * @param fullText - the full text in the text field of the suggest box
     * @param query - the query portion of the full text
     * @return a populated OptionSuggestion
     */
    protected OptionSuggestion createOptionSuggestion(Option o, String fullText, String query)
    {
        return new OptionSuggestion(o, fullText, query);
    }
    
    @Override
    public void onSelection(SelectionEvent<Suggestion> event)
    {
        Suggestion suggestion = event.getSelectedItem();
        if (suggestion instanceof OptionSuggestion) {
            OptionSuggestion osugg = (OptionSuggestion) suggestion;
            //if NEXT or PREVIOUS were selected, requery but bypass the timer
            String value = osugg.getOption().getValue();
            if (OptionSuggestion.NEXT_VALUE.equals(value)) {
                m_indexFrom += m_pageSize;
                m_indexTo += m_pageSize;

                RestSuggestOracle oracle = (RestSuggestOracle) m_field.getSuggestOracle();
                oracle.getSuggestions();

            } else if (OptionSuggestion.PREVIOUS_VALUE.equals(value)) {
                m_indexFrom -= m_pageSize;
                m_indexTo -= m_pageSize;

                RestSuggestOracle oracle = (RestSuggestOracle) m_field.getSuggestOracle();
                oracle.getSuggestions();

            } else {
                //made a valid selection
                updateFormFeedback(FormFeedback.VALID, m_validText);

                //add the option's value to the value map            
                putValue(osugg.getOption());

                if (m_isMultivalued) {
                    m_field.setText("");
                }
                //put the focus back into the textfield so user
                //can enter more
                m_field.setFocus(true);
            }
        }
    }

    private String getFullReplaceText(String displ, String replacePre)
    {
        String replaceText = replacePre;
        //replace the last bit after the last comma
        if (replaceText.lastIndexOf(m_displaySeparator) > 0) {
            replaceText = replaceText.substring(0, replaceText.lastIndexOf(m_displaySeparator)) + m_displaySeparator;
        } else {
            replaceText = "";
        }
        //then add a comma
        if (m_isMultivalued) {
            return replaceText + displ + m_displaySeparator;
        } else {
            return displ;
        }
    }

    /**
     * Gets the SuggestBox field
     * @return the SuggestBox component
     */
    public SuggestBox getSuggestBox()
    {
        return m_field;
    }
    
    
    /**
     * Gets the text within the text field of the suggest box
     * @return display text
     */
    public String getText()
    {
        return m_field.getText();
    }

    /**
     * Sets the text within the text field of the suggest box
     * @param text to set
     */
    public void setText(String text)
    {
        m_field.setText(text);
    }

    @Override
    public int getTabIndex()
    {
        return m_field.getTabIndex();
    }


    @Override
    public void setAccessKey(char key)
    {
        m_field.setAccessKey(key);
    }


    @Override
    public void setFocus(boolean focused)
    {
        m_field.setFocus(focused);
    }


    @Override
    public void setTabIndex(int index)
    {
        m_field.setTabIndex(index);
    }

    /**
     * Add a KeyUpHandler to the suggest box
     * @param handler to add
     * @return the HandlerRegistration
     */
    public HandlerRegistration addKeyUpHandler(KeyUpHandler handler)
    {
        return m_field.getTextBox().addKeyUpHandler(handler);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler)
    {
        return addHandler(handler, ValueChangeEvent.getType());
    }
    
    @Override
    public void onKeyUp(KeyUpEvent event)
    {
        /*
         * Because SuggestOracle.requestSuggestions does not get called when the text field is empty
         * this key up handler is necessary for handling the case when there is an empty text field...
         * Here, the FormFeedback is reset.
         */
        updateFormFeedback(FormFeedback.NONE, "");

        if (!m_isMultivalued) {
            deleteUnusedItems();
        } else {
            if (m_lastCurPos == 0 && !m_selectedItems.empty() && KeyCodes.KEY_BACKSPACE == event.getNativeKeyCode()) {
                /*
                 * If there is nothing in the text field and user continues to press Backspace, 
                 * pop off the last selected item
                 */
    
                SelectedItem item = m_selectedItems.pop();
                removeValue(item);
                
            }
            m_lastCurPos = m_field.getTextBox().getCursorPos();
            
        }
    }
    
    /**
     * If the user is editing the field they may remove one or more items that are in our map.
     * In that case we need to remove those values from our map.  This is still relevant
     * for single valued.
     */
    private void deleteUnusedItems()
    {
        ArrayList<String> displayVals = new ArrayList<String>();
        for (String displayVal : m_field.getText().split(m_displaySeparator)) {
            displayVals.add(displayVal.trim());
        }
        
        for (String key : m_valueMap.keySet()) {
            if (!displayVals.contains(key)) {
                m_valueMap.remove(key);
            }
        }
    }

    /**
     * @return Returns the selectedItemEq.
     */
    public String getSelectedItemEq()
    {
        return m_selectedItemEq;
    }
    /**
     * @param selectedItemEq The selectedItemEq to set.
     */
    public void setSelectedItemEq(String selectedItemEq)
    {
        m_selectedItemEq = selectedItemEq;
    }

    /**
     * Set the delimiting character(s) between the display texts.
     * If the display separator includes white space, it is automatically included for display,
     * but is trimmed when getting values.
     * @param displaySeparator The displaySeparator to set.
     */
    public void setDisplaySeparator(String displaySeparator)
    {
        m_displaySeparator = displaySeparator;
    }

    /**
     * Set the delimiting character(s) between values if calling getValue to get all the values
     * in the value map as a single String.
     * @param valueDelim The valueDelim to set.
     */
    public void setValueDelim(String valueDelim)
    {
        m_valueDelim = valueDelim;
    }


    /**
     * Set the maximum number of options in the selection dropdown list at a time.
     * This is used to send URL parameters of indexFrom and indexTo to the REST endpoint.
     * @param pageSize The pageSize to set.
     */
    public void setPageSize(int pageSize)
    {
        m_pageSize = pageSize;
    }


    /**
     * Set the time in milliseconds a user should stop typing before a query to the server is sent.
     * @param delay The delay to set.
     */
    public void setDelay(int delay)
    {
        m_delay = delay;
    }

    /**
     * Get the FormFeedback widget used.
     * @return Returns the feedback.
     */
    public FormFeedback getFeedback()
    {
        return m_feedback;
    }


    /**
     * Set the FormFeedback widget used.
     * @param feedback The feedback to set.
     */
    public void setFeedback(FormFeedback feedback)
    {
        m_feedback = feedback;
    }


    /**
     * Get the parameterized localized String for invalid values.
     * This is the method to override with custom localization methods.
     * @param invalids - the value or values that are invalid.
     * @return Returns the invalidText.
     */
    public String getInvalidText(String invalids)
    {
        return STRINGS.invalidColon(invalids);
    }

    /**
     * Get the parameterized localized String for invalid values.
     * This is the method to override with custom localization methods.
     * @param invalids - the value or values that are invalid.
     * @param reason - the reason or message returned about the invalid value
     * @return Returns the invalidText.
     */
    public String getInvalidReason(String invalids, String reason)
    {
        return STRINGS.invalidColonReason(invalids, reason);
    }

    /**
     * Sets the valid text String.
     * @param validText The validText to set.
     */
    public void setValidText(String validText)
    {
        m_validText = validText;
    }

    /**
     * Sets the loading text String
     * @param loadingText The loadingText to set.
     */
    public void setLoadingText(String loadingText)
    {
        m_loadingText = loadingText;
    }
    /**
     * Retrieve Options (name-value pairs) that are suggested
     * @param query - the String search term 
     * @param from - the 0-based begin index int
     * @param to - the end index inclusive int
     * @param callback - the RESTObjectCallBack to handle the response
     */
    protected abstract void queryOptions(final String query, final int from, final int to, final RESTObjectCallBack<OptionResultSet> callback);

    /**
     * Handle the query response for getting items to suggest.
     * 
     * @param callback the callback for the request
     * @param val      the value we are getting suggestions for
     */
    protected void handleQueryResponse(RESTObjectCallBack<OptionResultSet> callback, JSONValue val)
    {
        JSONObject obj = val.isObject();
        int totSize = JSONUtil.getIntValue(obj, m_helper.getTotalSizeKey());
        OptionResultSet options = new OptionResultSet(totSize);
        JSONArray optionsArray = JSONUtil.getJSONArray(obj, m_helper.getOptionsKey());

        if (options.getTotalSize() > 0 && optionsArray != null) {

            for (int i = 0; i < optionsArray.size(); i++) {
                if (optionsArray.get(i) == null) {
                    /*
                     This happens when a JSON array has an invalid trailing comma
                     */
                    continue;
                }

                JSONObject jsonOpt = optionsArray.get(i).isObject();
                Option option = createOption(jsonOpt);
                options.addOption(option);
            }
        }
        callback.success(options);
    }

/*
    * Some custom inner classes for our SuggestOracle
    */
    /**
     * A custom Suggest Oracle
     */
    private class RestSuggestOracle extends SuggestOracle
    {
        private SuggestOracle.Request m_request;
        private SuggestOracle.Callback m_callback;
        private Timer m_timer;

        RestSuggestOracle()
        {
            m_timer = new Timer() {

                @Override
                public void run()
                {
                    getSuggestionsFromTimer();
                }
            };
        }

        private void getSuggestionsFromTimer()
        {
            /*
             * The reason we check for empty string is found at
             * http://development.lombardi.com/?p=39 --
             * paraphrased, if you backspace quickly the contents of the field are
             * emptied but a query for a single character is still executed.
             * Workaround for this is to check for an empty string field here.
             */

            if (!m_field.getText().trim().isEmpty()) {
                if (m_isMultivalued) {
                    //calling this here in case a user is trying to correct the "kev" 
                    //value of Allison Andrews, Kev, Josh Nolan or pasted in multiple values
                    findExactMatches();                    
                }
                getSuggestions();                    
            }
        }

        @Override
        public void requestSuggestions(SuggestOracle.Request request, SuggestOracle.Callback callback)
        {                
            //This is the method that gets called by the SuggestBox whenever some types into the text field            
            m_request = request;
            m_callback = callback;

            //reset the indexes (b/c NEXT and PREV call getSuggestions directly)
            resetPageIndices();

            //If the user keeps triggering this event (e.g., keeps typing), cancel and restart the timer
            m_timer.cancel();        
            m_timer.schedule(m_delay);   
        }

        private void getSuggestions()
        {
            String query = m_request.getQuery();

            //find the last thing entered up to the last separator
            //and use that as the query
            if (m_isMultivalued) {
                int sep = query.lastIndexOf(m_displaySeparator);
                if (sep > 0) {
                    query = query.substring(sep + m_displaySeparator.length());                
                }
            }
            query = query.trim();

            //do not query if it's just an empty String
            //also do not get suggestions you've already got an exact match for this string in the m_valueMap
            if (query.length() > 0 && m_valueMap.get(query) == null) {
                //JSUtil.println("getting Suggestions for: " + query);
                updateFormFeedback(FormFeedback.LOADING, m_loadingText);               

                queryOptions(query, m_indexFrom, m_indexTo, new RestSuggestCallback(m_request, m_callback, query));
            }
        }


        @Override
        public boolean isDisplayStringHTML()
        {
            return true;
        }
    }

    /**
     * A custom callback that has the original SuggestOracle.Request and SuggestOracle.Callback
     */
    private class RestSuggestCallback implements RESTObjectCallBack<OptionResultSet>
    {
        private SuggestOracle.Request m_request;
        private SuggestOracle.Callback m_callback;
        private String m_query; //this may be different from m_request.getQuery when multivalued it's only the substring after the last delimiter

        RestSuggestCallback(Request request, Callback callback, String query)
        {
            m_request = request;
            m_callback = callback;
            m_query = query;
        }

        public void success(OptionResultSet optResults)
        {
            SuggestOracle.Response resp = new SuggestOracle.Response();
            List<OptionSuggestion> suggs = new ArrayList<OptionSuggestion>();
            int totSize = optResults.getTotalSize();

            if (totSize < 1) {
                //if there were no suggestions, then it's an invalid value
                updateFormFeedback(FormFeedback.ERROR, getInvalidText(m_query));

            } else if (totSize == 1) {
                //it's an exact match, so do not bother with showing suggestions, 
                Option o = optResults.getOptions()[0];
                String displ = o.getName();

                if (!m_isMultivalued) {
                    //remove the last bit up to separator
                    m_field.setText(getFullReplaceText(displ, m_request.getQuery()));
                } else {
                    m_field.setText("");
                }

                JSUtil.println("RestSuggestCallback.success! exact match found for displ = " + displ);

                //it's valid!
                updateFormFeedback(FormFeedback.VALID, m_validText);

                //set the value into the valueMap
                putValue(o);

            } else {
                //more than 1 so show the suggestions

                //if not at the first page, show PREVIOUS
                if (m_indexFrom > 0) {
                    OptionSuggestion prev = new OptionSuggestion(OptionSuggestion.PREVIOUS_VALUE, m_request.getQuery());
                    suggs.add(prev);
                }

                // show the suggestions
                for (Option o : optResults.getOptions()) {
                    OptionSuggestion sugg = createOptionSuggestion(o, m_request.getQuery(), m_query);
                    suggs.add(sugg);
                }

                //if there are more pages, show NEXT
                if (m_indexTo < totSize) {
                    OptionSuggestion next = new OptionSuggestion(OptionSuggestion.NEXT_VALUE, m_request.getQuery());
                    suggs.add(next);
                }

                //nothing has been picked yet, so let the feedback show an error (unsaveable)
                updateFormFeedback(FormFeedback.ERROR, getInvalidText(m_query));
            }

            //it's ok (and good) to pass an empty suggestion list back to the suggest box's callback method
            //the list is not shown at all if the list is empty.
            resp.setSuggestions(suggs);
            m_callback.onSuggestionsReady(m_request, resp);
        }

        @Override
        public void error(String message) 
        {
            updateFormFeedback(FormFeedback.ERROR, getInvalidReason(m_query, message));
        }

        @Override
        public void error(RESTException e) 
        {
            updateFormFeedback(FormFeedback.ERROR, getInvalidReason(m_query, e.getReason()));
        }      
    }

    /**
     * A bean to serve as a custom suggestion so that the value is available and the replace
     * will look like it is supporting multivalues
     */
    public class OptionSuggestion implements SuggestOracle.Suggestion
    {
        private Option m_option;
        private String m_display;
        private String m_replace;
        private String m_query;

        static final String NEXT_VALUE = "NEXT";
        static final String PREVIOUS_VALUE = "PREVIOUS";

        /**
         * Constructor for navigation options
         * @param nav - next or previous value
         * @param currentTextValue - the current contents of the text box
         */
        OptionSuggestion(String nav, String currentTextValue)
        {
            if (NEXT_VALUE.equals(nav)) {
                m_display = "<div class=\"autocompleterNext\" title=\"" + STRINGS.next() + "\"></div>";
                m_option = new Option(STRINGS.next(), nav);
            } else {
                m_display = "<div class=\"autocompleterPrev\" title=\"" + STRINGS.previous() + "\"></div>";
                m_option = new Option(STRINGS.previous(), nav);
            }
            m_replace = currentTextValue;
        }

        /**
         * Constructor for suggested options
         * @param option - the Option bean
         * @param replacePre - the current contents of the text box
         * @param query - the query
         */
        protected OptionSuggestion(Option option, String replacePre, String query)
        {
            String displ = option.getName(); 
            m_query = query;
            m_display = safeBoldQueryWithinString(displ);
            m_replace = getFullReplaceText(displ, replacePre);
            m_option = option;
        }

        /**
         * Escape the html so that it is safe and add bold tags 
         * around the display string where it matches the query string
         * @param display - the display string
         * @return a safe html version of the display string with bold tags around the query if found, otherwiseg
         */
        protected String safeBoldQueryWithinString(String display)
        {
            String safe = SafeHtmlUtils.htmlEscape(display);
            /*
             * Change ? to &#63; because it is a special character for RegExp 
             * in both the display as well as the query.
             * 
             * Same for * to &#42;
             */
            safe = safe.replaceAll("\\?", "&#63;");
            safe = safe.replaceAll("\\*", "&#42;");
            
            String escapedQuery = SafeHtmlUtils.htmlEscape(m_query);
            escapedQuery = escapedQuery.replaceAll("\\?", "&#63;");
            escapedQuery = escapedQuery.replaceAll("\\*", "&#42;");
            
            int begin = safe.toLowerCase().indexOf(escapedQuery.toLowerCase());
            if (begin >= 0) {
                int end = begin + escapedQuery.length();
                String match = safe.substring(begin, end);
                return safe.replaceFirst(match, "<b>" + match + "</b>");
            }
            
            //may not necessarily be a part of the query, if found in different field
            return safe;
            
        }
        /**
         * Constructor for regular options with only name and value.
         * @param displ - the name of the option
         * @param val - the value of the option
         * @param replacePre - the current contents of the text box
         * @param query - the query
         * 
         * @deprecated This method is deprecated and will be removed in future releases. Use OptionSuggestion(Option, String, String) 
         */
        @Deprecated
        protected OptionSuggestion(String displ, String val, String replacePre, String query)
        {
            this(new Option(displ, val), replacePre, query);
        }
        
        @Override
        public String getDisplayString()
        {
            return m_display;
        }

        @Override
        public String getReplacementString()
        {
            return m_replace;
        }

        /**
         * Return the Option bean
         * @return the Option bean
         */
        public Option getOption()
        {
            return m_option;
        }
        
        /**
         * Get the value of the option
         * @return value
         */
        public String getValue()
        {
            return m_option.getValue();
        }

        /**
         * Get the name of the option.
         * (when not multivalued, this will be the same as getReplacementString)
         * @return name
         */
        public String getName()
        {
            return m_option.getName();
        }
    }



    /**
     * Bean for name-value pairs
     */
    public class Option
    {

        private String m_name;
        private String m_value;

        /**
         * No argument constructor
         */
        public Option()
        {
        }
        /**
         * Constructor with default name and value
         * @param name - the name of the option
         * @param value - the value of the option
         */
        public Option(String name, String value)
        {
            m_name = name;
            m_value = value;
        }
        /**
         * @return Returns the name.
         */
        public String getName()
        {
            return m_name;
        }
        /**
         * @param name The name to set.
         */
        public void setName(String name)
        {
            m_name = name;
        }
        /**
         * @return Returns the value.
         */
        public String getValue()
        {
            return m_value;
        }
        /**
         * @param value The value to set.
         */
        public void setValue(String value)
        {
            m_value = value;
        }    


    }

    /**
     * Bean for total size and options
     */
    protected final class OptionResultSet
    {
        private final List<Option> m_options = new ArrayList<Option>();
        private int m_totalSize;


        /**
         * Constructor.  Must pass in the total size.
         * @param totalSize the total size of the template
         */
        public OptionResultSet(int totalSize)
        {
            m_totalSize = totalSize;
        }

        /**
         * Add an option
         * @param option - the Option to add
         */
        public void addOption(Option option)
        {
            m_options.add(option);
        }

        /**
         * @return an array of Options
         */
        public Option[] getOptions()
        {
            return m_options.toArray(new Option[m_options.size()]);
        }

        /**
         * @param totalSize The totalSize to set.
         */
        public void setTotalSize(int totalSize)
        {
            m_totalSize = totalSize;
        }

        /**
         * @return Returns the totalSize.
         */
        public int getTotalSize()
        {
            return m_totalSize;
        }     
    }
    
    /**
     * This class represents a UI element for a selected item.
     * It will have an anchor with an X to allow for this item to
     * be dismissed
     */
    public class SelectedItem extends HTMLPanel implements ClickHandler
    {
        private Option m_option;
        /**
         * Constructor
         * @param id - the elements unique id
         * @param option - the Option bean
         */
        public SelectedItem(String id, Option option)
        {
            this(id, option, "<span class=\"spiffy-mvsb-item\" id=\"" + id + 
                    "_main\">" + option.getName() +
                    "</span>");
        }

        /**
         * Constructor
         * @param id - the elements unique id
         * @param option - the Option bean
         * @param html - the HTML string for the SelectedItem, which must include an id + "_main"
         * to serve as the location where the close Anchor will go
         */
        public SelectedItem(String id, Option option, String html)
        {
            super("span", html);
            Anchor close = new Anchor();
            close.setHref("#");
            close.setTitle(STRINGS.close());
            close.addStyleName("spiffy-mvsb-remove");
            add(close, id + "_main");
            close.addClickHandler(this);
            
            getElement().setId(id);
            m_option = option;;
        }
        
        @Override
        public void onClick(ClickEvent event)
        {
            event.preventDefault();
            remove();
        }
        
        /**
         * Remove this selected item
         */
        public void remove()
        {
            removeValue(this);
            m_selectedItems.remove(this);
        }
        
        /**
         * Get the Option bean
         * @return the Option bean
         */
        public Option getOption()
        {
            return m_option;
        }
        
        /**
         * Get the display string,
         * which is the same as the name of the Option
         * @return the display string
         */
        public String getDisplay()
        {
            return m_option.getName();
        }
        
    }

}
