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
package org.spiffyui.spsample.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.spiffyui.client.JSDateUtil;
import org.spiffyui.client.JSONUtil;
import org.spiffyui.client.JSUtil;
import org.spiffyui.client.MessageUtil;
import org.spiffyui.client.NumberFormatter;
import org.spiffyui.client.i18n.SpiffyUIStrings;
import org.spiffyui.client.widgets.DatePickerTextBox;
import org.spiffyui.client.widgets.TimePickerTextBox;
import org.spiffyui.client.widgets.multivaluesuggest.MultivalueSuggestBox;
import org.spiffyui.client.widgets.multivaluesuggest.MultivalueSuggestBoxBase.Option;
import org.spiffyui.client.widgets.multivaluesuggest.MultivalueSuggestRESTHelper;
import org.spiffyui.client.widgets.slider.RangeSlider;
import org.spiffyui.client.widgets.slider.Slider;
import org.spiffyui.client.rest.RESTCallback;
import org.spiffyui.client.rest.RESTException;
import org.spiffyui.client.rest.RESTObjectCallBack;
import org.spiffyui.client.rest.RESTility;
import org.spiffyui.spsample.client.rest.VersionInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * This is the unit test panel
 *
 */
public final class UnitTestPanel extends HTMLPanel
{
    private static final int OFFSET = getTimezoneOffset();
    private static UnitTestPanel g_panel;
    
    private static final int DOM_STRUCTURE_TESTS = 0;
    private static final int DATE_TESTS = 1;
    private static final int JSON_TESTS = 2;
    private static final int REST_EXCEPTION_TESTS = 3;
    private static final int MESSAGE_HANDLING_TESTS = 4;
    private static final int VALIDATION_TESTS = 5;
    private static final int REST_TESTS = 6;
    private static final int NUMBER_FORMAT_TESTS = 7;
    private static final int DATETIME_WIDGET_TESTS = 8;
    private static final int SLIDER_WIDGET_TESTS = 9;
    private static final int MVSB_WIDGET_TESTS = 10;
    
    private static final SpiffyUIStrings STRINGS = (SpiffyUIStrings) GWT.create(SpiffyUIStrings.class);

    private static final String WIDGETS_ID = "unitTestWidgets";
    /**
     * Creates a new panel
     */
    private UnitTestPanel()
    {
        /*
         This panel doesn't have any contents because it is depending on
         contents added to by the index-test.html file.
         */
        super("div", "<div id=\"unitTestWidgets\"></div>");
        
        getElement().setId("unitTestPanel");
        
        RootPanel.get("mainContent").add(this);
        
        setVisible(true);
    }
    
    /**
     * Run the unit tests for this framework.
     */
    public static void runUnitTests()
    {
        if (g_panel == null) {
            g_panel = new UnitTestPanel();
        }
    }
    
    @Override 
    public void onLoad()
    {
        super.onLoad();
        doTests();
        
    }
    
    private void doTests()
    {
        test("DOM structure tests", DOM_STRUCTURE_TESTS);
        test("Date handling tests", DATE_TESTS);
        test("JSON object parsing tests", JSON_TESTS);
        test("JSON REST exception handling tests", REST_EXCEPTION_TESTS);
        test("Message handling tests", MESSAGE_HANDLING_TESTS);
        test("Validation and encoding tests", VALIDATION_TESTS);
        asynctest("REST tests", REST_TESTS);
        test("Number format tests (can be run in any language -- English, French, German and any language with country of Switzerland are distinct formats)", NUMBER_FORMAT_TESTS);
        test("Date/Time Widget tests", DATETIME_WIDGET_TESTS);
        test("Slider Widget tests", SLIDER_WIDGET_TESTS);
        test("Multi-Valued Suggest Box Widget tests", MVSB_WIDGET_TESTS);
    }
    
    private static void runTest(int id)
    {
        switch (id) {
        
        case DOM_STRUCTURE_TESTS:
            domStructureTests();
            return;
            
        case DATE_TESTS:
            dateTests();
            return;
            
        case JSON_TESTS:
            jsonTests();
            return;
            
        case REST_EXCEPTION_TESTS:
            restExceptionTests();
            return;
            
        case MESSAGE_HANDLING_TESTS:
            messageHandlingTests();
            return;

        case VALIDATION_TESTS:
            validationTests();
            return;
            
        case REST_TESTS:
            restTests();
            return;
            
        case NUMBER_FORMAT_TESTS:
            numberFormatTests();
            return;

        case DATETIME_WIDGET_TESTS:
            dateTimeWidgetTests();
            return;
            
        case SLIDER_WIDGET_TESTS:
            sliderWidgetTests();
            return;
            
        case MVSB_WIDGET_TESTS:
            mvsbWidgetTests();
            return;
            
        default:
            /*
             We don't need to do anything since we don't have a default
             */
        }
        
    }

    private static void dateTimeWidgetTests()
    {
        expect(6);
        /*
         * Time Picker
         */
        TimePickerTextBox tptb = new TimePickerTextBox();
        g_panel.add(tptb, WIDGETS_ID);

        tptb.setText("5:48 AM");

        ok("5:48 AM".equals(tptb.getText()), "The time picker text box text should be 5:48 PM and it was " + tptb.getText());
        ok(5 == tptb.getHours(), "The time picker text box hours should be 5 and it was " + tptb.getHours());
        ok(48 == tptb.getMinutes(), "The time picker text box minutes should be 48 and it was " + tptb.getMinutes());
        ok(!tptb.isEmpty(), "The time picker text box shouldn't be empty.");

        /*
         * Date Picker
         */
        DatePickerTextBox dptb = new DatePickerTextBox();
        g_panel.add(dptb, WIDGETS_ID);

        dptb.setText("12/26/2010");
        ok("12/26/2010".equals(JSDateUtil.getDate(dptb.getDateValue())), 
           "The date in the date picker text box date value should be 12/26/2010 and it was " + JSDateUtil.getDate(dptb.getDateValue()));
        ok(!dptb.isEmpty(), "The date picker text box shouldn't be empty.");
    }
    
    private static void sliderWidgetTests()
    {
        expect(6);
        /*
         * Slider
         */
        Slider s = new Slider(HTMLPanel.createUniqueId());
        g_panel.add(s, WIDGETS_ID);
        s.setValue(10);
        ok(10 == s.getValue(), "The value of the slider should be 10 and it was " + s.getValue());

        /*
         * Range Slider
         */
        RangeSlider rs = new RangeSlider(HTMLPanel.createUniqueId(), 10, 100, 20, 90);
        g_panel.add(rs, WIDGETS_ID);
        ok(20 == rs.getValueMin(), "The minimum value for the slider should be 20 and it was " + rs.getValueMin());
        ok(90 == rs.getValueMax(), "The maximum value for the slider should be 90 and it was " + rs.getValueMax());

        rs.setMinimum(30);
        rs.setMaximum(80);

        ok(30 == rs.getValueMin(), "The new minimum value for the slider should be 30 and it was " + rs.getValueMin());
        ok(80 == rs.getValueMax(), "The new maximum value for the slider should be 80 and it was " + rs.getValueMax());

        rs.setMinimum(90);
        ok(90 == rs.getValueMin(), "The new minimum value for the slider should be 90 and it was " + rs.getValueMin());
    }
    
    private static void mvsbWidgetTests()
    {
        expect(48);
        
        /*
         * Single-Valued Suggest Box
         */
        final MultivalueSuggestBox ssb = new MultivalueSuggestBox(new MultivalueSuggestRESTHelper("TotalSize", "Options", "DisplayName", "Value") {
            
            @Override
            public String buildUrl(String q, int indexFrom, int indexTo)
            {
                return "multivaluesuggestboxexample/colors?q=" + q + "&indexFrom=" + indexFrom + "&indexTo=" + indexTo;
            }
        }, false);
        
        g_panel.add(ssb, WIDGETS_ID);
        
        ok("".equals(ssb.getText()), "The text for the single-valued suggestbox should be '' and is '" + ssb.getText() + "'");
        String newText = "non-existent color";
        ssb.setText(newText);
        ok(newText.equals(ssb.getText()), "The text for the single-valued suggestbox should be '" + newText + "' and is '" + ssb.getText() + "'");

        Map<String, String> valueMap = new HashMap<String, String>();
        valueMap.put("Electric Lime", "#CEFF1D");
        ssb.setValueMap(valueMap);
        ok("Electric Lime".equals(ssb.getText()), "The text for the single-valued suggestbox should be 'Electric Lime' and is '" + ssb.getText() + "'");
        ok("#CEFF1D".equals(ssb.getValue()), "The value for the single-valued suggestbox should be '#CEFF1D' and is '" + ssb.getValue() + "'");
        
        Map<String, String> svm = ssb.getValueMap();
        for (String key : svm.keySet()) {
            ok("Electric Lime".equals(key), "The key in the value map for the single-valued suggestbox should be 'Electric Lime' and is '" + key + "'");
            ok("#CEFF1D".equals(svm.get(key)), "The key in the value map for the single-valued suggestbox should be '#CEFF1D' and is '" + svm.get(key) + "'");
        }
        
        ok("Electric Lime~#CEFF1D".equals(ssb.getValuesAsString()), "The values as string for the single-valued suggestbox should be 'Electric Lime~#CEFF1D' and is '" + ssb.getValuesAsString() + "'");
        
        ssb.setValuesAsString("Gold~#E7C697");
        ok("Gold~#E7C697".equals(ssb.getValuesAsString()), "The values as string for the single-valued suggestbox should be 'Gold~#E7C697' and is '" + ssb.getValuesAsString() + "'");
        
        /*
         * Multi-Valued Suggest Box
         */
        final MultivalueSuggestBox msb = new MultivalueSuggestBox(new MultivalueSuggestRESTHelper("TotalSize", "Options", "DisplayName", "Value") {
            
            @Override
            public String buildUrl(String q, int indexFrom, int indexTo)
            {
                return "multivaluesuggestboxexample/colors?q=" + q + "&indexFrom=" + indexFrom + "&indexTo=" + indexTo;
            }
        }, true);
        
        ok("".equals(msb.getText()), "The text for the multi-valued suggestbox should be empty string and is '" + msb.getText() + "'");
        msb.setText(newText);
        //no value so text unchanged.
        ok(newText.equals(msb.getText()), "The text for the multi-valued suggestbox should be '" + newText + "' and is '" + msb.getText() + "'");

        /*
         * get/set ValueMap
         */
        //add Almond to this local map which already has Electric Lime in it.
        valueMap.put("Almond", "#EFDECD");
        msb.setValueMap(valueMap);
        //colors have values, so they get converted to selectedItems and will not appear in the text box
        ok("".equals(msb.getText()), "The text for the multi-valued suggestbox should be '' and is '" + msb.getText() + "'");
        ok("#CEFF1D;#EFDECD".equals(msb.getValue()), "The value for the multi-valued suggestbox should be '#CEFF1D;#EFDECD' and is '" + msb.getValue() + "'");
        
        Map<String, String> mvm = msb.getValueMap();
        int i = 0;
        for (String key : mvm.keySet()) {
            if (i == 0) {
                ok("Electric Lime".equals(key), "The key in the value map at index " + i + " for the multi-valued suggestbox should be 'Electric Lime' and is '" + key + "'");
                ok("#CEFF1D".equals(mvm.get(key)), "The key in the value map at index " + i + " for the multi-valued suggestbox should be '#CEFF1D' and is '" + mvm.get(key) + "'");                
            } else {
                ok("Almond".equals(key), "The key in the value map at index " + i + " for the multi-valued suggestbox should be 'Almond' and is '" + key + "'");
                ok("#EFDECD".equals(mvm.get(key)), "The key in the value map at index " + i + " for the multi-valued suggestbox should be '#EFDECD' and is '" + mvm.get(key) + "'");
            }
            i++;
        }
        
        /*
         * get/set ValuesAsString
         */
        ok("Electric Lime~#CEFF1D;Almond~#EFDECD".equals(msb.getValuesAsString()), 
            "The values as string for the multi-valued suggestbox should be 'Electric Lime~#CEFF1D;Almond~#EFDECD' and is '" + msb.getValuesAsString() + "'");

        //Now set the values to Gold, Red, and Sky Blue 
        msb.setValuesAsString("Gold~#E7C697;Red~#EE204D;Sky Blue~#80DAEB");
        ok("Gold~#E7C697;Red~#EE204D;Sky Blue~#80DAEB".equals(msb.getValuesAsString()), 
            "The values as string for the multi-valued suggestbox should be 'Gold~#E7C697;Red~#EE204D;Sky Blue~#80DAEB' and is '" + msb.getValuesAsString() + "'");
        
        /*
         * get/set/add SelectedOption
         */
        //Now set the values using setSelectedOptions with 3 new Options
        List<Option> options = new ArrayList<Option>();
        options.add(msb.new Option("Brick Red", "#CB4154"));
        options.add(msb.new Option("Goldenrod", "#FCD975"));
        options.add(msb.new Option("Cornflower", "#9ACEEB"));
        msb.setSelectedOptions(options);
        
        //test getValuesAsString
        ok("Brick Red~#CB4154;Goldenrod~#FCD975;Cornflower~#9ACEEB".equals(msb.getValuesAsString()), 
            "The values as string after using setSelectedOptions for the multi-valued suggestbox should be 'Brick Red~#CB4154;Goldendrod~#FCD975;Cornflower~#9ACEEB' and is '" + msb.getValuesAsString() + "'");
        
        //test getValueMap
        Map<String, String> mvmSO = msb.getValueMap();
        int j = 0;
        for (String key : mvmSO.keySet()) {
            if (j == 0) {
                ok("Brick Red".equals(key), "The key in the value map at index " + j + " for the multi-valued suggestbox should be 'Brick Red' and is '" + key + "'");
                ok("#CB4154".equals(mvmSO.get(key)), "The value in the value map at index " + j + " for the multi-valued suggestbox should be '#CB4154' and is '" + mvmSO.get(key) + "'");                
            } else if (j == 1) {
                ok("Goldenrod".equals(key), "The key in the value map at index " + j + " for the multi-valued suggestbox should be 'Goldenrod' and is '" + key + "'");
                ok("#FCD975".equals(mvmSO.get(key)), "The value in the value map at index " + j + " for the multi-valued suggestbox should be '#FCD975' and is '" + mvmSO.get(key) + "'");
            } else {
                ok("Cornflower".equals(key), "The key in the value map at index " + j + " for the multi-valued suggestbox should be 'Cornflower' and is '" + key + "'");
                ok("#9ACEEB".equals(mvmSO.get(key)), "The value in the value map at index " + j + " for the multi-valued suggestbox should be '#9ACEEB' and is '" + mvmSO.get(key) + "'");
            }
            j++;
        }
        
        //test getSelectedOptions
        List<Option> selOpts = msb.getSelectedOptions();
        for (int k = 0; k < 3; k++) {
            Option selOpt = selOpts.get(k);
            if (k == 0) {
                ok("Brick Red".equals(selOpt.getName()), "The key in the selected options list at index " + k + " for the multi-valued suggestbox should be 'Brick Red' and is '" + selOpt.getName() + "'");
                ok("#CB4154".equals(selOpt.getValue()), "The value in the selected options list at index " + k + " for the multi-valued suggestbox should be '#CB4154' and is '" + selOpt.getValue() + "'");                
            } else if (k == 1) {
                ok("Goldenrod".equals(selOpt.getName()), "The key in the selected options list at index " + k + " for the multi-valued suggestbox should be 'Goldenrod' and is '" + selOpt.getName() + "'");
                ok("#FCD975".equals(selOpt.getValue()), "The value in the selected options list at index " + k + " for the multi-valued suggestbox should be '#FCD975' and is '" + selOpt.getValue() + "'");
            } else {
                ok("Cornflower".equals(selOpt.getName()), "The key in the selected options list at index " + k + " for the multi-valued suggestbox should be 'Cornflower' and is '" + selOpt.getName() + "'");
                ok("#9ACEEB".equals(selOpt.getValue()), "The value in the selected options list at index " + k + " for the multi-valued suggestbox should be '#9ACEEB' and is '" + selOpt.getValue() + "'");
            }
        }
        
        //Now add Shamrock using addSelectedOption
        Option shamrock = msb.new Option("Shamrock", "#45CEA2");
        msb.addSelectedOption(shamrock);
        //repeat tests of getValuesAsString, getValueMap and getSelectedOptions
        //test getValuesAsString
        ok("Brick Red~#CB4154;Goldenrod~#FCD975;Cornflower~#9ACEEB;Shamrock~#45CEA2".equals(msb.getValuesAsString()), 
            "The values as string after using setSelectedOptions for the multi-valued suggestbox should be 'Brick Red~#CB4154;Goldendrod~#FCD975;Cornflower~#9ACEEB;Shamrock~#45CEA2' and is '" + msb.getValuesAsString() + "'");
        
        //test getValueMap
        Map<String, String> mvmSO2 = msb.getValueMap();
        int l = 0;
        for (String key : mvmSO2.keySet()) {
            if (l == 0) {
                ok("Brick Red".equals(key), "The key in the value map at index " + l + " for the multi-valued suggestbox should be 'Brick Red' and is '" + key + "'");
                ok("#CB4154".equals(mvmSO2.get(key)), "The value in the value map for the multi-valued suggestbox should be '#CB4154' and is '" + mvmSO2.get(key) + "'");                
            } else if (l == 1) {
                ok("Goldenrod".equals(key), "The key in the value map at index " + l + " for the multi-valued suggestbox should be 'Goldenrod' and is '" + key + "'");
                ok("#FCD975".equals(mvmSO2.get(key)), "The value in the value map for the multi-valued suggestbox should be '#FCD975' and is '" + mvmSO2.get(key) + "'");
            } else if (l == 2) {
                ok("Cornflower".equals(key), "The key in the value map at index " + l + " for the multi-valued suggestbox should be 'Cornflower' and is '" + key + "'");
                ok("#9ACEEB".equals(mvmSO2.get(key)), "The value in the value map for the multi-valued suggestbox should be '#9ACEEB' and is '" + mvmSO2.get(key) + "'");
            } else {
                ok("Shamrock".equals(key), "The key in the value map at index " + l + " for the multi-valued suggestbox should be 'Shamrock' and is '" + key + "'");
                ok("#45CEA2".equals(mvmSO2.get(key)), "The value in the value map for the multi-valued suggestbox should be '#45CEA2' and is '" + mvmSO2.get(key) + "'");
            }
            l++;
        }
        
        //test getSelectedOptions
        List<Option> selOpts2 = msb.getSelectedOptions();
        for (int m = 0; m < 4; m++) {
            Option selOpt = selOpts2.get(m);
            if (m == 0) {
                ok("Brick Red".equals(selOpt.getName()), "The key in the selected options list at index " + m + " for the multi-valued suggestbox should be 'Brick Red' and is '" + selOpt.getName() + "'");
                ok("#CB4154".equals(selOpt.getValue()), "The value in the selected options list at index " + m + " for the multi-valued suggestbox should be '#CB4154' and is '" + selOpt.getValue() + "'");                
            } else if (m == 1) {
                ok("Goldenrod".equals(selOpt.getName()), "The key in the selected options list at index " + m + " for the multi-valued suggestbox should be 'Goldenrod' and is '" + selOpt.getName() + "'");
                ok("#FCD975".equals(selOpt.getValue()), "The value in the selected options list at index " + m + " for the multi-valued suggestbox should be '#FCD975' and is '" + selOpt.getValue() + "'");
            } else if (m == 2) {
                ok("Cornflower".equals(selOpt.getName()), "The key in the selected options list at index " + m + " for the multi-valued suggestbox should be 'Cornflower' and is '" + selOpt.getName() + "'");
                ok("#9ACEEB".equals(selOpt.getValue()), "The value in the selected options list at index " + m + " for the multi-valued suggestbox should be '#9ACEEB' and is '" + selOpt.getValue() + "'");
            } else {
                ok("Shamrock".equals(selOpt.getName()), "The key in the selected options list at index " + m + " for the multi-valued suggestbox should be 'Shamrock' and is '" + selOpt.getName() + "'");
                ok("#45CEA2".equals(selOpt.getValue()), "The value in the selected options list at index " + m + " for the multi-valued suggestbox should be '#45CEA2' and is '" + selOpt.getValue() + "'");               
            }
        }
        
        g_panel.add(msb, WIDGETS_ID);
    }
    
    private static void numberFormatTests()
    {
        int likeLoc = NumberFormatter.getLikeLocale();
        boolean ch = NumberFormatter.LIKE_CH == likeLoc;
        boolean fr = NumberFormatter.LIKE_FR == likeLoc;
        boolean de = NumberFormatter.LIKE_DE == likeLoc;
        
        expect(37); 
        
        numberFormatOK(0.0, "0.0", fr || de ? "0,0" : "0.0");
        numberFormatOK(0.0, "0.#", "0");
        numberFormatOK(0.1, "0.0", fr || de ? "0,1" : "0.1");
        numberFormatOK(0.12, "0.0#", fr || de ? "0,12" : "0.12");
        numberFormatOK(0.12, "0.##", fr || de ? "0,12" : "0.12");
        numberFormatOK(0.120, "0.##", fr || de ? "0,12" : "0.12");
        numberFormatOK(0.120, "0.###", fr || de ? "0,12" : "0.12");
        numberFormatOK(0.120, "0.000", fr || de ? "0,120" : "0.120");        
        numberFormatOK(0.1200, "0.###0", fr || de ? "0,1200" : "0.1200");
        numberFormatOK(0.12003, "#.###0", fr || de ? "0,1200" : "0.1200");
        numberFormatOK(0.120067, "#.###0", fr || de ? "0,1201" : "0.1201");
        numberFormatOK(0.120067, "#.####0", fr || de ? "0,12007" : "0.12007");
        
        numberFormatOK(1, "0.0", fr || de ? "1,0" : "1.0");
        numberFormatOK(10, "0.0", fr || de ? "10,0" : "10.0");
        numberFormatOK(100, "0.0", fr || de ? "100,0" : "100.0");
        numberFormatOK(1000, "0.0", ch ? "1'000.0" : fr ? "1 000,0" : de ? "1.000,0" : "1,000.0");
        numberFormatOK(10000, "0.0", ch ? "10'000.0" : fr ? "10 000,0" : de ? "10.000,0" : "10,000.0");
        numberFormatOK(100000, "0.0", ch ? "100'000.0" : fr ? "100 000,0" : de ? "100.000,0" : "100,000.0");
        
        numberFormatOK(1, "0", "1");
        numberFormatOK(10, "0", "10");
        numberFormatOK(100, "0", "100");
        numberFormatOK(1000, "0", ch ? "1'000" : fr ? "1 000" : de ? "1.000" : "1,000");
        numberFormatOK(10000, "0", ch ? "10'000" : fr ? "10 000" : de ? "10.000" : "10,000");
        numberFormatOK(100000, "0", ch ? "100'000" : fr ? "100 000" : de ? "100.000" : "100,000");
        numberFormatOK(1000000, "0", ch ? "1'000'000" : fr ? "1 000 000" : de ? "1.000.000" : "1,000,000");
        numberFormatOK(10000000, "0", ch ? "10'000'000" : fr ? "10 000 000" : de ? "10.000.000" : "10,000,000");
        numberFormatOK(100000000, "0", ch ? "100'000'000" : fr ? "100 000 000" : de ? "100.000.000" : "100,000,000");
        numberFormatOK(1000000000, "0", ch ? "1'000'000'000" : fr ? "1 000 000 000" : de ? "1.000.000.000" : "1,000,000,000");
     
        numberFormatWithAbbrevOK(1234567890, fr || de ? STRINGS.gigaAbbrev("1,235") : STRINGS.gigaAbbrev("1.235"));
        numberFormatWithAbbrevOK(123456789, fr || de ? STRINGS.megaAbbrev("123,46") : STRINGS.megaAbbrev("123.46"));
        numberFormatWithAbbrevOK(12345678, fr || de ? STRINGS.megaAbbrev("12,35") : STRINGS.megaAbbrev("12.35"));
        numberFormatWithAbbrevOK(1234567, fr || de ? STRINGS.megaAbbrev("1,23") : STRINGS.megaAbbrev("1.23"));
        numberFormatWithAbbrevOK(123456, fr || de ? STRINGS.kiloAbbrev("123,5") : STRINGS.kiloAbbrev("123.5"));
        numberFormatWithAbbrevOK(12345, fr || de ? STRINGS.kiloAbbrev("12,3") : STRINGS.kiloAbbrev("12.3"));
        numberFormatWithAbbrevOK(1234, fr || de ? STRINGS.kiloAbbrev("1,2") : STRINGS.kiloAbbrev("1.2"));
        numberFormatWithAbbrevOK(123, "123");
        numberFormatWithAbbrevOK(1071161, fr || de ? STRINGS.megaAbbrev("1,07") : STRINGS.megaAbbrev("1.07"));
    }
    
    private static void numberFormatOK(double number, String pattern, String expected)
    {
        ok(NumberFormatter.format(number, pattern).equals(expected), number + " formatted as " + pattern + " should be " + expected);
    }
    
    private static void numberFormatWithAbbrevOK(double number, String expected)
    {
        ok(NumberFormatter.formatWithAbbreviation(number).equals(expected), number + " formatted with abbreviation should be " + expected);
    }
    
    private static void restTests()
    {
        expect(4);
        
        VersionInfo.getVersionInfo(new RESTObjectCallBack<VersionInfo>() {
            public void success(VersionInfo info)
            {
                ok(true, "The REST call completed successfully and returned the revision " + info.getRevision());
                notFoundUrlTests();
            }

            public void error(String message)
            {
                ok(false, "The REST call failed " + message);
                notFoundUrlTests();
            }

            public void error(RESTException e)
            {
                ok(false, "The REST call failed " + e.getReason());
                notFoundUrlTests();
            }
        });
        
    }
    
    private static void notFoundUrlTests()
    {
        RESTility.callREST("invalidurl", new RESTCallback() {
                @Override
                public void onSuccess(JSONValue val)
                {
                    ok(false, "The REST call to the invalid url should have returned a 404");
                    invalidHostTests();
                }

                @Override
                public void onError(int statusCode, String errorResponse)
                {
                    ok(false, "The REST call correctly returned a 404");
                    invalidHostTests();
                }

                @Override
                public void onError(RESTException e)
                {
                    ok(true, "The REST call to the invalid url returned a 404");
                    invalidHostTests();
                }
            });
    }
    
    private static void invalidHostTests()
    {
        RESTility.callREST("http://www.google.com", new RESTCallback() {
                @Override
                public void onSuccess(JSONValue val)
                {
                    ok(false, "The REST call to the invalid url should not have returned success");
                    invalidFileFormatTest();
                }

                @Override
                public void onError(int statusCode, String errorResponse)
                {
                    ok(false, "The REST call to an invalid host should not have returned an error code");
                    invalidFileFormatTest();
                }

                @Override
                public void onError(RESTException e)
                {
                    ok(true, "The REST call to an invalid host should have returned a blank error");
                    invalidFileFormatTest();
                }
            });
    }
    
    private static void invalidFileFormatTest()
    {
        RESTility.callREST("spsample.css", new RESTCallback() {
                @Override
                public void onSuccess(JSONValue val)
                {
                    ok(false, "The REST call to an invalid protocol should have failed");
                    restart();
                }

                @Override
                public void onError(int statusCode, String errorResponse)
                {
                    ok(false, "The REST call to an invalid protocol should have failed");
                    restart();
                }

                @Override
                public void onError(RESTException e)
                {
                    ok(RESTException.UNPARSABLE_RESPONSE.equals(e.getCode()), 
                       "The REST call to an invalid protocol should cause error " + RESTException.UNPARSABLE_RESPONSE);
                    restart();
                }
            });
    }

    private static void validationTests()
    {
        expect(6);
        
        ok(JSUtil.validateEmail("you@them.com"), "you@them.com should be a valid email address.");
        ok(!JSUtil.validateEmail("you@them"), "you@them should not be a valid email address.");
        ok(!JSUtil.validateEmail("you"), "you should not be a valid email address.");

        ok(!JSUtil.trimLastDelimiter("a,b,c,", ",").endsWith(","), 
           JSUtil.trimLastDelimiter("a,b,c,", ",") + " should not end with a comma.");

        ok(JSUtil.base64Decode("dGVzdGluZw==").equals("testing"), 
           JSUtil.base64Decode("dGVzdGluZw==") + " should equal testing.");
        ok(JSUtil.base64Encode("testing").equals("dGVzdGluZw=="), 
           JSUtil.base64Encode("testing") + " should equal dGVzdGluZw==.");
        
    }
    
    private static void messageHandlingTests()
    {
        expect(1);
        
        MessageUtil.showWarning("Test warning message with log");
        
        ok(RootPanel.get("humanMsgLog").isVisible(), "Message log should now be visible since a message has been logged.");
    }
    
    private static void jsonTests()
    {
        expect(16);
        
        String testString = "Test String";
        
        JSONObject obj = new JSONObject();
        obj.put("stringVal", new JSONString(testString));
        obj.put("intVal", new JSONNumber(99));
        obj.put("intStringVal", new JSONString("99"));
        obj.put("booleanVal", JSONBoolean.getInstance(true));
        obj.put("booleanStringVal", new JSONString("true"));
        obj.put("dateStringVal", new JSONString("1293373801745"));
        
        /*
         String tests
         */
        ok(testString.equals(JSONUtil.getStringValue(obj, "stringVal")), 
           "Getting string value should be Test String.  It was " + JSONUtil.getStringValue(obj, "stringVal"));
        
        ok(testString.equals(JSONUtil.getStringValueIgnoreCase(obj, "stringval")), 
           "Getting string value ignore case should be Test String.  It was " + JSONUtil.getStringValueIgnoreCase(obj, "stringval"));
        
        ok(testString.equals(JSONUtil.getStringValue(obj, "stringValBogus", testString)), 
           "Getting string value with default should be Test String.  It was " + JSONUtil.getStringValue(obj, "stringValBogus", testString));
        
        ok(null == JSONUtil.getStringValue(obj, "stringValBogus"), 
           "Getting string value with no default should be Test String.  It was " + JSONUtil.getStringValue(obj, "stringValBogus"));
        
        /*
         int tests
         */
        ok(99 == JSONUtil.getIntValue(obj, "intVal"), 
           "Getting int value should be 99.  It was " + JSONUtil.getIntValue(obj, "intVal"));
        
        ok(99 == JSONUtil.getIntValue(obj, "intValBogus", 99), 
           "Getting int value with default should be 99.  It was " + JSONUtil.getIntValue(obj, "intValBogus", 99));
        
        ok(-1 == JSONUtil.getIntValue(obj, "intValBogus"), 
           "Getting int value with no default should be -1.  It was " + JSONUtil.getIntValue(obj, "intValBogus"));
        
        ok(99 == JSONUtil.getIntValue(obj, "intStringVal"), 
           "Getting int value with string parsing should be 99.  It was " + JSONUtil.getIntValue(obj, "intStringVal"));
        
        /*
         boolean tests
         */
        ok(JSONUtil.getBooleanValue(obj, "booleanVal"), 
           "Getting boolean value should be true.  It was " + JSONUtil.getBooleanValue(obj, "booleanVal"));
        
        ok(!JSONUtil.getBooleanValue(obj, "booleanBogusVal"), 
           "Getting boolean value with no default should be true.  It was " + JSONUtil.getBooleanValue(obj, "booleanBogusVal"));
        
        ok(JSONUtil.getBooleanValue(obj, "booleanStringVal"), 
           "Getting boolean value with string parsing should be true.  It was " + JSONUtil.getBooleanValue(obj, "booleanStringVal"));
        
        /*
         Date tests
         */
        ok(null != JSONUtil.getDateValue(obj, "dateStringVal"), 
           "Getting Date value should be a valid object.  It was " + JSONUtil.getDateValue(obj, "dateStringVal"));
        
        ok("12/26/2010 9:30 AM".equals(JSDateUtil.getShortDateTime(getOffsetDate(JSONUtil.getDateValue(obj, "dateStringVal")))), 
           "Getting Date value short date and time should be 12/26/2010 9:30 AM.  It was " + 
           JSDateUtil.getShortDateTime(getOffsetDate(JSONUtil.getDateValue(obj, "dateStringVal"))));
        
        ok(null == JSONUtil.getDateValue(obj, "dateStringBogusVal"), 
           "Getting Date value of invalid field should be null.  It was " + JSONUtil.getDateValue(obj, "dateStringBogusVal"));

        /*
         Case ignoring tests
         */
        ok(JSONUtil.containsKeyIgnoreCase(obj, "StRiNgVaL"), 
           "The object should contain the key StRiNgVaL when ignoring case and contains key was " + 
           JSONUtil.containsKeyIgnoreCase(obj, "StRiNgVaL"));

        ok(null != JSONUtil.getIgnoreCase(obj, "StRiNgVaL"), 
           "The object should return a value for the key StRiNgVaL when ignoring case.  The value was " + 
           JSONUtil.getIgnoreCase(obj, "StRiNgVaL"));
    }
    
    private static void restExceptionTests()
    {
        expect(7);
        /*
         * {
         *     "Fault": {
         *         "Code": {
         *             "Value": "Sender",
         *             "Subcode": {
         *                 "Value": "MessageTimeout" 
         *             } 
         *         },
         *         "Reason": {
         *             "Text": "Sender Timeout" 
         *         },
         *         "Detail": {
         *             "MaxTime": "P5M" 
         *         } 
         *     }
         * }
         */
        
        JSONObject obj = new JSONObject();
        
        JSONObject fault = new JSONObject();
        obj.put("Fault", fault);
        
        JSONObject code = new JSONObject();
        fault.put("Code", code);
        code.put("Value", new JSONString("Sender"));
        
        JSONObject subcode = new JSONObject();
        code.put("Subcode", subcode);
        subcode.put("Value", new JSONString("MessageSubcodeTest"));
        
        JSONObject reason = new JSONObject();
        fault.put("Reason", reason);
        reason.put("Text", new JSONString("Test Reason"));
        
        JSONObject detail = new JSONObject();
        fault.put("Detail", detail);
        detail.put("Value1", new JSONString("First Value"));
        detail.put("Value2", new JSONString("Second Value"));
        
        RESTException exception = JSONUtil.getRESTException(obj, -1, "url");
        
        ok(null != exception, "Getting RESTException should be a value object.  It was " + exception);

        ok("Sender".equals(exception.getCode()), "Getting RESTException code should be Sender.  It was " + exception.getCode());

        ok("MessageSubcodeTest".equals(exception.getSubcode()), 
           "Getting RESTException subcode should be MessageSubcodeTest.  It was " + exception.getSubcode());

        ok("Test Reason".equals(exception.getReason()), 
           "Getting RESTException reason should be Test Reason.  It was " + exception.getReason());
        
        ok("First Value".equals(exception.getDetails().get("Value1")), 
           "Getting RESTException details first value should be First Value.  It was " + exception.getDetails().get("Value1"));
        
        ok("Second Value".equals(exception.getDetails().get("Value2")), 
           "Getting RESTException details first value should be Second Value.  It was " + exception.getDetails().get("Value2"));
        
        /*
         Now we'll test a value that isn't an NCAC fault
         */
        obj = new JSONObject();
        obj.put("foo", new JSONString("foo"));
        obj.put("bar", new JSONString("bar"));
        
        ok(null == JSONUtil.getRESTException(obj, -1, "url"), 
           "Getting RESTException for JSON data that doesn't represent an NCAC exception should be null.  It was " + 
           JSONUtil.getRESTException(obj, -1, "url"));
    }
    
    private static void dateTests()
    {
        if (!"en-US".equalsIgnoreCase(JSDateUtil.getLocale()) &&
            !"en".equalsIgnoreCase(JSDateUtil.getLocale())) {
            expect(1);
            ok(false, "The date tests may only be run in United State English and your locale is " + JSDateUtil.getLocale());
            return;
        }
        
        expect(18);
        
        
        /*
         We are starting with a test date of Sun Dec 26 2010 09:30:01 GMT-0500 (Eastern Standard Time)
         */
        Date date = new Date(Long.parseLong("1293373801745"));
        
        ok("12/26/2010".equals(JSDateUtil.getDate(getOffsetDate(date))), "The date should be 12/26/2010 and was " + 
           JSDateUtil.getDate(getOffsetDate(date)));

        ok("12/26/2010".equals(JSDateUtil.getDate("" + getOffsetDate(date).getTime())), 
           "The date should be 12/26/2010 and was " + JSDateUtil.getDate("" + getOffsetDate(date).getTime()));

        ok("Sunday, December 26, 2010".equals(JSDateUtil.getLongDate("" + getOffsetDate(date).getTime())), 
           "The long date should be Sunday, December 26, 2010 and was " + JSDateUtil.getLongDate("" + getOffsetDate(date).getTime()));

        ok("9:30 AM".equals(JSDateUtil.getShortTime(getOffsetDate(date))), 
           "The short time should be 9:30 AM and was " + JSDateUtil.getShortTime(getOffsetDate(date)));

        ok("Sunday, December 26, 2010 9:30:01 AM".equals(JSDateUtil.getFullDateTime(getOffsetDate(date))), 
           "The full time should be Sunday, December 26, 2010 9:30:01 AM and was " + JSDateUtil.getFullDateTime(getOffsetDate(date)));

        ok("Sunday, December 26, 2010 9:30:01 AM".equals(JSDateUtil.getFullDateTime("" + getOffsetDate(date).getTime())), 
           "The full parsed time should be Sunday, December 26, 2010 9:30:01 AM and was " + 
           JSDateUtil.getFullDateTime("" + getOffsetDate(date).getTime()));

        ok("12/26/2010 9:30 AM".equals(JSDateUtil.getShortDateTime(getOffsetDate(date))), 
           "The short date and time should be 12/26/2010 9:30 AM and was " + 
           JSDateUtil.getShortDateTime(getOffsetDate(date)));

        ok("12/26/2010".equals(JSDateUtil.getShortDate("1293373801745")), 
           "The short date and time should be 12/26/2010 and was " + 
           JSDateUtil.getShortDate("1293373801745"));

        ok("Dec 26 9:30 AM".equals(JSDateUtil.getShortMonthDayTime("1293373801745")), 
           "The short date and time should be Dec 26 9:30 AM and was " + 
           JSDateUtil.getShortMonthDayTime("1293373801745"));
        
        ok("1293978601745".equals(JSDateUtil.dateAdd(date, 1, "WEEK").getTime() + ""), 
           "The time plus one week should be 1293978601745 and was " + JSDateUtil.dateAdd(date, 1, "WEEK").getTime());

        ok("2010-12-26 09:30".equals(JSDateUtil.getDate(getOffsetDate(date), "yyyy-MM-dd HH:mm")), 
           "The time with custom formatting should be 2010-12-26 09:30 and was " + 
           JSDateUtil.getDate(getOffsetDate(date), "yyyy-MM-dd HH:mm"));
        
        ok(JSDateUtil.getUTCOffset() < 20, 
           "Getting the UTC offset shouldn't throw an exception and it returned: " + JSDateUtil.getUTCOffset());

        ok("10:00 AM".equals(JSDateUtil.getShortTimeRounded("1356533280000")), 
           "The short time rounded should be 10:00 AM and was " + 
           JSDateUtil.getShortTimeRounded("1356533280000"));

        
        
        ok(JSDateUtil.getOrdinalNumber(date) == 359, "12/26/2010 is the 359th day of the year so the ordinal number is: " + JSDateUtil.getOrdinalNumber(date));        
        
        ok(JSDateUtil.format(date, "%w").equals("0"), "12/26/2010 is a Sunday so the day of the week is: " + JSDateUtil.format(date, "%w"));

        ok(JSDateUtil.getHours("9:48 AM") == 9, "The hours of 9:48 AM should be 9 and it was: " + JSDateUtil.getHours("9:48 AM"));

        ok(JSDateUtil.getMinutes("9:48 AM") == 48, "The minutes of 9:48 AM should be 48 and it was: " + JSDateUtil.getMinutes("9:48 AM"));
        
        ok("Thursday, September 25, 2014 12:00:00 AM".equals(JSDateUtil.getFullDateTime("1411617600000")), 
            "The full date time of epoch date 1411617600000 should be Thursday, September 25, 2014 12:00:00 AM and it was: " + JSDateUtil.getFullDateTime("1411617600000"));

        
    }
    
    private static Date getOffsetDate(Date date)
    {
        return JSDateUtil.dateAdd(date, OFFSET, "HOUR");
    }
    
    /**
     * All of our sample dates are in EST.  We have to adjust our parsed dates for the timezone
     * in case the user is in a different timezone.
     * 
     * @return the timezone offset in hours
     */
    private static int getTimezoneOffset()
    {
        int offset = 0;
        try {
            String offsetString = getUTCOffest();
            
            /*
             This offset works fine on Firefox and Chrome and you get a string like
             -0700.  However, IE doesn't handle this properly and 
             
             */
            if (offsetString.startsWith("-")) {
                offsetString = offsetString.substring(1);
            } else if (offsetString.startsWith("+")) {
                offsetString = "-" + offsetString.substring(1);
            } else if (offsetString.startsWith("undefined")) {
                offsetString = offsetString.substring(9);
            }
            
            offset = Integer.parseInt(offsetString) / 100;
            offset = offset - 5;
        } catch (Exception e) {
            /*
             IE won't let us get the UTC offset.
             */
            offset = 0;
        }
        
        JSONObject obj = new JSONObject();
        obj.put("dateStringVal", new JSONString("1293373801745"));
        Date d = JSONUtil.getDateValue(obj, "dateStringVal");
        d = JSDateUtil.dateAdd(d, offset, "HOUR");
        
        if (!"12/26/2010 9:30 AM".equals(JSDateUtil.getShortDateTime(d))) {
            /*
             Then this must be daylight savings time
             */
            offset++;
        }
        
        return offset;
    }
    
    private static native String getUTCOffest() /*-{ 
        var date = new $wnd.Date();
        return date.getUTCOffset();
    }-*/;
    
    private static void domStructureTests()
    {
        expect(8);
        ok(RootPanel.get("loginPanel") != null, "The #loginPanel should be added to the page.");
        ok(RootPanel.get("mainWrap") != null, "The #mainWrap should be added to the page.");
        ok(RootPanel.get("main") != null, "The #main should be added to the page.");
        ok(RootPanel.get("mainHeader") != null, "The #mainHeader should be added to the page.");
        ok(RootPanel.get("mainBody") != null, "The #mainBody should be added to the page.");
        ok(RootPanel.get("mainNavigation") != null, "The #mainNavigation should be added to the page.");
        ok(RootPanel.get("mainContent") != null, "The #mainContent should be added to the page.");
        ok(RootPanel.get("mainFooter") != null, "The #mainFooter should be added to the page.");
        
    }
    
    private static native void test(String title, int id) /*-{ 
        $wnd.test(title, function() {
            @org.spiffyui.spsample.client.UnitTestPanel::runTest(I)(id);
        });
    }-*/;
    
    private static native void asynctest(String title, int id) /*-{ 
        $wnd.asyncTest(title, function() {
            @org.spiffyui.spsample.client.UnitTestPanel::runTest(I)(id);
        });
    }-*/;
    
    private static native void ok(boolean state, String message) /*-{ 
        $wnd.ok(state, message);
    }-*/;
    
    private static native void restart() /*-{ 
        $wnd.start();
    }-*/;
    
    private static native void expect(int numberOfTests) /*-{ 
        $wnd.expect(numberOfTests);
    }-*/;
}
