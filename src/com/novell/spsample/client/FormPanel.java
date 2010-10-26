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
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Widget;
import com.novell.spiffyui.client.JSUtil;
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
            "<h2 class=\"sectionTitle\">Create new user</h2>" +
            "<ol class=\"dialogformsection\">" +
                "<li id=\"firstNameRow\" class=\"dialogformrow\"><label class=\"dialogformlabel\" for=\"firstNameTxt\">First name: " + 
                    "</label><div id=\"firstName\" class=\"formcontrolssection\"></div></li>" +
                "<li id=\"lastNameRow\" class=\"dialogformrow\"><label class=\"dialogformlabel\" for=\"lastNameTxt\">Last name: " + 
                    "</label><div id=\"lastName\" class=\"formcontrolssection\"></div></li>" +
                "<li id=\"emailRow\" class=\"dialogformrow\"><label class=\"dialogformlabel\" for=\"emailTxt\">Email address: " + 
                    "</label><div id=\"email\" class=\"formcontrolssection\"></div></li>" +
                "<li id=\"userDescRow\" class=\"extratallformrow\"><label class=\"dialogformlabel\" for=\"userDescTxt\">Short description: " + 
                    "</label><div id=\"userDesc\" class=\"formcontrolssection\"></div></li>" +
                "<h3 class=\"subSectionTitle\">Passwords</h3>" +
                "<li id=\"passwordRow\" class=\"dialogformrow\"><label class=\"dialogformlabel\" for=\"passwordTxt\">Password: " + 
                    "</label><div id=\"password\" class=\"formcontrolssection\"></div></li>" +
                "<li id=\"passwordRepeatRow\" class=\"dialogformrow\"><label class=\"dialogformlabel\" for=\"passwordRepeatTxt\">Repeat password: " + 
                    "</label><div id=\"passwordRepeat\" class=\"formcontrolssection\"></div></li>" +
                "<li id=\"securityQuestionRow\" class=\"extratallformrow\"><label class=\"dialogformlabel\" for=\"securityQuestionTxt\">Security question: " + 
                    "</label><div id=\"securityQuestion\" class=\"formcontrolssection\"></div>" + 
                "</li>" +
                "<li id=\"securityAnswerRow\" class=\"dialogformrow\"><label class=\"dialogformlabel\" for=\"securityAnswerTxt\">Security answer: " + 
                    "</label><div id=\"securityAnswer\" class=\"formcontrolssection\"></div></li>" +
                "<li id=\"page2ButtonsRow\" class=\"dialogformrow\"><div id=\"page2Buttons\" class=\"formcontrolssection formbuttons\"></div></li>" +
            "</ol>" +
        "</fieldset>";
    
    private TextBox m_firstName;
    private FormFeedback m_firstNameFeedback;
    
    private TextBox m_lastName;
    private FormFeedback m_lastNameFeedback;
    
    private TextBox m_email;
    private FormFeedback m_emailFeedback;
    
    private TextBox m_password;
    private FormFeedback m_passwordFeedback;
    
    private TextBox m_passwordRepeat;
    private FormFeedback m_passwordRepeatFeedback;
    
    private TextArea m_userDesc;
    private FormFeedback m_userDescFeedback;
    
    private TextArea m_securityQuestion;
    private FormFeedback m_securityQuestionFeedback;
    
    private TextBox m_securityAnswer;
    private FormFeedback m_securityAnswerFeedback;
    
    private FancyButton m_save;
    
    /**
     * Creates a new import panel
     */
    public FormPanel()
    {
        super("div", "<h1>Spiffy Forms</h1>" + STRINGS.FormPanel_html() + CONTENTS);
        
        getElement().setId("formPanel");
        
        RootPanel.get("mainContent").add(this);
        
        setVisible(false);
        
        /*
         First name
         */
        m_firstName = new TextBox();
        m_firstName.addKeyUpHandler(this);
        m_firstName.getElement().setId("firstNameTxt");
        m_firstName.getElement().addClassName("wideTextField");
        add(m_firstName, "firstName");
        
        m_firstNameFeedback = new FormFeedback();
        add(m_firstNameFeedback, "firstNameRow");
        
        /*
         Last name
         */
        m_lastName = new TextBox();
        m_lastName.addKeyUpHandler(this);
        m_lastName.getElement().setId("lastNameTxt");
        m_lastName.getElement().addClassName("wideTextField");
        add(m_lastName, "lastName");
        
        m_lastNameFeedback = new FormFeedback();
        add(m_lastNameFeedback, "lastNameRow");
        
        /*
         email
         */
        m_email = new TextBox();
        m_email.addKeyUpHandler(this);
        m_email.getElement().setId("emailTxt");
        m_email.getElement().addClassName("wideTextField");
        add(m_email, "email");
        
        m_emailFeedback = new FormFeedback();
        add(m_emailFeedback, "emailRow");
        
        /*
         User description
         */
        m_userDesc = new TextArea();
        m_userDesc.addKeyUpHandler(this);
        m_userDesc.getElement().setId("userDescTxt");
        m_userDesc.getElement().addClassName("wideTextField");
        add(m_userDesc, "userDesc");
        
        m_userDescFeedback = new FormFeedback();
        add(m_userDescFeedback, "userDescRow");
        
        /*
         Password
         */
        m_password = new PasswordTextBox();
        m_password.addKeyUpHandler(this);
        m_password.getElement().setId("passwordTxt");
        m_password.getElement().addClassName("slimTextField");
        add(m_password, "password");
        
        m_passwordFeedback = new FormFeedback();
        add(m_passwordFeedback, "passwordRow");
        
        /*
         Password repeat
         */
        m_passwordRepeat = new PasswordTextBox();
        m_passwordRepeat.addKeyUpHandler(this);
        m_passwordRepeat.getElement().setId("passwordRepeatTxt");
        m_passwordRepeat.getElement().addClassName("slimTextField");
        add(m_passwordRepeat, "passwordRepeat");
        
        m_passwordRepeatFeedback = new FormFeedback();
        add(m_passwordRepeatFeedback, "passwordRepeatRow");
        
        /*
         Security Question
         */
        m_securityQuestion = new TextArea();
        m_securityQuestion.addKeyUpHandler(this);
        m_securityQuestion.getElement().setId("securityQuestionTxt");
        m_securityQuestion.getElement().addClassName("wideTextField");
        add(m_securityQuestion, "securityQuestion");
        
        m_securityQuestionFeedback = new FormFeedback();
        add(m_securityQuestionFeedback, "securityQuestionRow");
        
        /*
         Security answer
         */
        m_securityAnswer = new TextBox();
        m_securityAnswer.addKeyUpHandler(this);
        m_securityAnswer.getElement().setId("securityAnswerTxt");
        m_securityAnswer.getElement().addClassName("wideTextField");
        add(m_securityAnswer, "securityAnswer");
        
        m_securityAnswerFeedback = new FormFeedback();
        add(m_securityAnswerFeedback, "securityAnswerRow");
        
        
        
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
        if (w == m_firstName) {
            if (m_firstName.getText().length() > 2) {
                m_firstNameFeedback.setStatus(FormFeedback.VALID);
                m_firstNameFeedback.setTitle("");
            } else {
                m_firstNameFeedback.setStatus(FormFeedback.WARNING);
                m_firstNameFeedback.setTitle("Enter a name longer than 2 characters");
            }
        } else if (w == m_lastName) {
            if (m_lastName.getText().length() > 2) {
                m_lastNameFeedback.setStatus(FormFeedback.VALID);
                m_lastNameFeedback.setTitle("");
            } else {
                m_lastNameFeedback.setStatus(FormFeedback.WARNING);
                m_lastNameFeedback.setTitle("Enter a name longer than 2 characters");
            }
        } else if (w == m_email) {
            if (JSUtil.validateEmail(m_email.getText())) {
                m_emailFeedback.setStatus(FormFeedback.VALID);
                m_emailFeedback.setTitle("");
            } else {
                m_emailFeedback.setStatus(FormFeedback.ERROR);
                m_emailFeedback.setTitle("Make sure the email address is in a valid format.");
            }
        } else if (w == m_password) {
            if (m_password.getText().length() > 2) {
                m_passwordFeedback.setStatus(FormFeedback.VALID);
                m_passwordFeedback.setTitle("");
            } else {
                m_passwordFeedback.setStatus(FormFeedback.WARNING);
                m_passwordFeedback.setTitle("Enter a password longer than 2 characters");
            }
        } else if (w == m_securityQuestion) {
            if (m_securityQuestion.getText().length() > 2) {
                m_securityQuestionFeedback.setStatus(FormFeedback.VALID);
                m_securityQuestionFeedback.setTitle("");
            } else {
                m_securityQuestionFeedback.setStatus(FormFeedback.WARNING);
                m_securityQuestionFeedback.setTitle("Enter a question longer than 2 characters");
            }
        } else if (w == m_securityAnswer) {
            if (m_securityAnswer.getText().length() > 4) {
                m_securityAnswerFeedback.setStatus(FormFeedback.VALID);
                m_securityAnswerFeedback.setTitle("");
            } else {
                m_securityAnswerFeedback.setStatus(FormFeedback.WARNING);
                m_securityAnswerFeedback.setTitle("Enter an answer longer than 4 characters");
            }
        } else if (w == m_passwordRepeat) {
            if (m_passwordRepeat.getText().length() > 2) {
                if (m_passwordRepeat.getText().equals(m_password.getText())) {
                    m_passwordRepeatFeedback.setStatus(FormFeedback.VALID);
                    m_passwordRepeatFeedback.setTitle("");
                } else {
                    m_passwordRepeatFeedback.setStatus(FormFeedback.ERROR);
                    m_passwordRepeatFeedback.setTitle("Make sure your two passwords match.");
                }
            } else {
                m_passwordRepeatFeedback.setStatus(FormFeedback.WARNING);
                m_passwordRepeatFeedback.setTitle("Enter a password longer than 2 characters");
            }
        } else if (w == m_userDesc) {
            if (m_userDesc.getText().length() > 8) {
                m_userDescFeedback.setStatus(FormFeedback.VALID);
                m_userDescFeedback.setTitle("");
            } else {
                m_userDescFeedback.setStatus(FormFeedback.WARNING);
                m_userDescFeedback.setTitle("Enter a description longer than 8 characters");
            }
        } 


        
        m_save.setEnabled(m_firstNameFeedback.getStatus() == FormFeedback.VALID &&
            m_lastNameFeedback.getStatus() == FormFeedback.VALID &&
            m_emailFeedback.getStatus() == FormFeedback.VALID &&
            m_passwordFeedback.getStatus() == FormFeedback.VALID &&
            m_passwordRepeatFeedback.getStatus() == FormFeedback.VALID &&
            m_userDescFeedback.getStatus() == FormFeedback.VALID &&
            m_securityQuestionFeedback.getStatus() == FormFeedback.VALID &&
            m_securityAnswerFeedback.getStatus() == FormFeedback.VALID);
    }
    
    private void save()
    {
        m_save.setInProgress(true);
        //a little timer to simulate time it takes to set loading back to false
        Timer t = new Timer() {

            @Override
            public void run()
            {
                m_save.setInProgress(false);
            }

        };
        t.schedule(2000);
        MessageUtil.showMessage("This form doesn't save anything.");
    }
}
