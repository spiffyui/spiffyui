/*
 * Copyright (c) 2010 Unpublished Work of Novell, Inc. All Rights Reserved.
 *
 * THIS WORK IS AN UNPUBLISHED WORK AND CONTAINS CONFIDENTIAL,
 * PROPRIETARY AND TRADE SECRET INFORMATION OF NOVELL, INC. ACCESS TO
 * THIS WORK IS RESTRICTED TO (I) NOVELL, INC. EMPLOYEES WHO HAVE A NEED
 * TO KNOW HOW TO PERFORM TASKS WITHIN THE SCOPE OF THEIR ASSIGNMENTS AND
 * (II) ENTITIES OTHER THAN NOVELL, INC. WHO HAVE ENTERED INTO
 * APPROPRIATE LICENSE AGREEMENTS. NO PART OF THIS WORK MAY BE USED,
 * PRACTICED, PERFORMED, COPIED, DISTRIBUTED, REVISED, MODIFIED,
 * TRANSLATED, ABRIDGED, CONDENSED, EXPANDED, COLLECTED, COMPILED,
 * LINKED, RECAST, TRANSFORMED OR ADAPTED WITHOUT THE PRIOR WRITTEN
 * CONSENT OF NOVELL, INC. ANY USE OR EXPLOITATION OF THIS WORK WITHOUT
 * AUTHORIZATION COULD SUBJECT THE PERPETRATOR TO CRIMINAL AND CIVIL
 * LIABILITY.
 *
 * ========================================================================
 */
package com.novell.spsample.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.novell.spiffyui.client.MessageUtil;
import com.novell.spiffyui.client.widgets.FormFeedback;
import com.novell.spiffyui.client.widgets.button.FancyButton;
import com.novell.spiffyui.client.widgets.button.FancySaveButton;

/**
 * This is the form sample panel
 *
 */
public class FormPanel extends HTMLPanel implements KeyUpHandler
{
    private static final SPSampleStrings STRINGS = (SPSampleStrings) GWT.create(SPSampleStrings.class);
    
    private static final String CONTENTS =
        "<fieldset id=\"Page2Fields\">" +
            "<h2 class=\"sectionTitle\">Sample form field set</h2>" +
            "<ol class=\"dialogformsection\">" +
                "<li id=\"firstValueRow\" class=\"dialogformrow\"><label class=\"dialogformlabel\" for=\"firstValueTxt\">Value One: " + 
                    "</label><div id=\"firstValue\" class=\"formcontrolssection\"></div></li>" +
                "<li id=\"secondValueRow\" class=\"dialogformrow\"><label class=\"dialogformlabel\" for=\"secondValueTxt\">Value Two: " + 
                    "</label><div id=\"secondValue\" class=\"formcontrolssection\"></div></li>" +
                "<li id=\"thirdValueRow\" class=\"dialogformrow\"><label class=\"dialogformlabel\" for=\"thirdValueTxt\">Value Three: " + 
                    "</label><div id=\"thirdValue\" class=\"formcontrolssection\"></div></li>" +
                "<li id=\"page2ButtonsRow\" class=\"dialogformrow\"><div id=\"page2Buttons\" class=\"formcontrolssection\"></div></li>" +
            "</ol>" +
        "</fieldset>";
    
    private TextBox m_field1;
    private FormFeedback m_field1Feedback;
    
    private TextBox m_field2;
    private FormFeedback m_field2Feedback;
    
    private TextBox m_field3;
    private FormFeedback m_field3Feedback;
    
    private FancyButton m_save;
    
    /**
     * Creates a new import panel
     */
    public FormPanel()
    {
        super("div", "<h1>Sample Form</h1>" + STRINGS.FormPanel_html() + CONTENTS);
        
        getElement().setId("formPanel");
        
        RootPanel.get("mainContent").add(this);
        
        setVisible(false);
        
        /*
         Field 1
         */
        m_field1 = new TextBox();
        m_field1.addKeyUpHandler(this);
        m_field1.getElement().setId("firstValueTxt");
        m_field1.getElement().addClassName("wideTextField");
        add(m_field1, "firstValue");
        
        m_field1Feedback = new FormFeedback();
        add(m_field1Feedback, "firstValueRow");
        
        /*
         Field 2
         */
        m_field2 = new TextBox();
        m_field2.addKeyUpHandler(this);
        m_field2.getElement().setId("secondValueTxt");
        m_field2.getElement().addClassName("wideTextField");
        add(m_field2, "secondValue");
        
        m_field2Feedback = new FormFeedback();
        add(m_field2Feedback, "secondValueRow");
        
        /*
         Field 3
         */
        m_field3 = new TextBox();
        m_field3.addKeyUpHandler(this);
        m_field3.getElement().setId("thirdValueTxt");
        m_field3.getElement().addClassName("wideTextField");
        add(m_field3, "thirdValue");
        
        m_field3Feedback = new FormFeedback();
        add(m_field3Feedback, "thirdValueRow");
        
        /*
         The big save button
         */
        m_save = new FancySaveButton("Save");
        m_save.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event)
                {
                    save();
                }
            });
        
        add(m_save, "page2Buttons");
        updateFormStatus(null);
    }
    
    @Override
    public void onKeyUp(KeyUpEvent event)
    {
        if (event.getNativeKeyCode() != KeyCodes.KEY_TAB) {
            updateFormStatus((Widget) event.getSource());
        }
    }
    
    private void updateFormStatus(Widget w)
    {
        if (w == m_field1) {
            if (m_field1.getText().length() > 2) {
                m_field1Feedback.setStatus(FormFeedback.VALID);
            } else {
                m_field1Feedback.setStatus(FormFeedback.WARNING);
            }
        } else if (w == m_field2) {
            if (m_field2.getText().length() > 2) {
                m_field2Feedback.setStatus(FormFeedback.VALID);
            } else {
                m_field2Feedback.setStatus(FormFeedback.WARNING);
            }
        } else if (w == m_field3) {
            if (m_field3.getText().length() > 2) {
                m_field3Feedback.setStatus(FormFeedback.VALID);
            } else {
                m_field3Feedback.setStatus(FormFeedback.WARNING);
            }
        }

        
        m_save.setEnabled(m_field1.getText().length() > 2 &&
                          m_field2.getText().length() > 2 &&
                          m_field3.getText().length() > 2);
    }
    
    private void save()
    {
        MessageUtil.showMessage("This form doesn't save anything.");
    }
}
