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
package org.spiffyui.spsample.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.spiffyui.client.JSUtil;
import org.spiffyui.client.MessageUtil;
import org.spiffyui.client.widgets.DatePickerTextBox;
import org.spiffyui.client.widgets.FormFeedback;
import org.spiffyui.client.widgets.button.FancyButton;
import org.spiffyui.client.widgets.button.FancySaveButton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;

/**
 * This is the form sample panel
 *
 */
public class FormPanel extends HTMLPanel implements KeyUpHandler
{
    private static final SPSampleStrings STRINGS = (SPSampleStrings) GWT.create(SPSampleStrings.class);

    private static final String WIDE_TEXT_FIELD = "wideTextField";
    
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
    
    private DatePickerTextBox m_bDay;
    private FormFeedback m_bDayFeedback;
    
    private TextArea m_userDesc;
    private FormFeedback m_userDescFeedback;
    
    private TextArea m_securityQuestion;
    private FormFeedback m_securityQuestionFeedback;
    
    private TextBox m_securityAnswer;
    private FormFeedback m_securityAnswerFeedback;
    
    private FancyButton m_save;

    private List<FormFeedback> m_feedbacks = new ArrayList<FormFeedback>();
    
    /**
     * Creates a new forms panel
     */
    public FormPanel()
    {
        super("div", STRINGS.FormPanel_html() + STRINGS.FormPanelContent_html());
        
        getElement().setId("formPanel");
        
        RootPanel.get("mainContent").add(this);
        
        setVisible(false);
        
        /*
         First name
         */
        m_firstName = new TextBox();
        m_firstName.addKeyUpHandler(this);
        m_firstName.getElement().setId("firstNameTxt");
        m_firstName.getElement().addClassName(WIDE_TEXT_FIELD);
        add(m_firstName, "firstName");
        
        m_firstNameFeedback = new FormFeedback();
        m_feedbacks.add(m_firstNameFeedback);
        add(m_firstNameFeedback, "firstNameRow");
        
        /*
         Last name
         */
        m_lastName = new TextBox();
        m_lastName.addKeyUpHandler(this);
        m_lastName.getElement().setId("lastNameTxt");
        m_lastName.getElement().addClassName(WIDE_TEXT_FIELD);
        add(m_lastName, "lastName");
        
        m_lastNameFeedback = new FormFeedback();
        m_feedbacks.add(m_lastNameFeedback);
        add(m_lastNameFeedback, "lastNameRow");
        
        /*
         email
         */
        m_email = new TextBox();
        m_email.addKeyUpHandler(this);
        m_email.getElement().setId("emailTxt");
        m_email.getElement().addClassName(WIDE_TEXT_FIELD);
        add(m_email, "email");
        
        m_emailFeedback = new FormFeedback();
        m_feedbacks.add(m_emailFeedback);
        add(m_emailFeedback, "emailRow");
        
        /*
         User's birthdate
         */
        m_bDay = new DatePickerTextBox("userBdayTxt");
        m_bDay.setMaximumDate(new Date()); //user cannot be born tomorrow
        m_bDay.addKeyUpHandler(this);
        m_bDay.getElement().addClassName("slimTextField");
        add(m_bDay, "userBday");
        
        m_bDayFeedback = new FormFeedback();
        add(m_bDayFeedback, "userBdayRow");
        
        /*
         User's gender
         */
        RadioButton female = new RadioButton("userGender", "Female");
        add(female, "userGender");
        
        RadioButton male = new RadioButton("userGender", "Male");
        male.addStyleName("radioOption");
        male.setValue(true);
        male.getElement().setId("userMale");
        add(male, "userGender");
        
        /*
         User description
         */
        m_userDesc = new TextArea();
        m_userDesc.addKeyUpHandler(this);
        m_userDesc.getElement().setId("userDescTxt");
        m_userDesc.getElement().addClassName(WIDE_TEXT_FIELD);
        add(m_userDesc, "userDesc");
        
        m_userDescFeedback = new FormFeedback();
        m_feedbacks.add(m_userDescFeedback);
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
        m_feedbacks.add(m_passwordFeedback);
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
        m_feedbacks.add(m_passwordRepeatFeedback);
        add(m_passwordRepeatFeedback, "passwordRepeatRow");
        
        /*
         Security Question
         */
        m_securityQuestion = new TextArea();
        m_securityQuestion.addKeyUpHandler(this);
        m_securityQuestion.getElement().setId("securityQuestionTxt");
        m_securityQuestion.getElement().addClassName(WIDE_TEXT_FIELD);
        add(m_securityQuestion, "securityQuestion");
        
        m_securityQuestionFeedback = new FormFeedback();
        m_feedbacks.add(m_securityQuestionFeedback);
        add(m_securityQuestionFeedback, "securityQuestionRow");
        
        /*
         Security answer
         */
        m_securityAnswer = new TextBox();
        m_securityAnswer.addKeyUpHandler(this);
        m_securityAnswer.getElement().setId("securityAnswerTxt");
        m_securityAnswer.getElement().addClassName(WIDE_TEXT_FIELD);
        add(m_securityAnswer, "securityAnswer");
        
        m_securityAnswerFeedback = new FormFeedback();
        m_feedbacks.add(m_securityAnswerFeedback);
        add(m_securityAnswerFeedback, "securityAnswerRow");
        
        
        
        /*
         The big save button
         */
        m_save = new FancySaveButton(Index.getStrings().save());
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
            validateField(m_firstName, 2, m_firstNameFeedback, Index.getStrings().firstName_tt());
        } else if (w == m_lastName) {
            validateField(m_lastName, 2, m_lastNameFeedback, Index.getStrings().lastName_tt());
        } else if (w == m_email) {
            validateEmail();
        } else if (w == m_password) {
            validateField(m_password, 2, m_passwordFeedback, Index.getStrings().pwd_tt());
        } else if (w == m_bDay) {
            validateBirthday();
        } else if (w == m_securityQuestion) {
            validateField(m_securityQuestion, 2, m_securityQuestionFeedback, Index.getStrings().question_tt());
        } else if (w == m_securityAnswer) {
            validateField(m_securityAnswer, 4, m_securityAnswerFeedback, Index.getStrings().answer_tt());
        } else if (w == m_passwordRepeat) {
            validatePasswordRepeat();
        } else if (w == m_userDesc) {
            validateField(m_userDesc, 8, m_userDescFeedback, Index.getStrings().desc_tt());
        }

        enableSaveButton();
    }

    /**
     * Enable or disable the save button based on the state of the fields.
     */
    private void enableSaveButton()
    {
        /*
         * We only want to enable the save button if every field is valid
         */
        for (FormFeedback feedback : m_feedbacks) {
            if (feedback.getStatus() != FormFeedback.VALID) {
                m_save.setEnabled(false);
                return;
            }
        }

        m_save.setEnabled(true);
    }

    /**
     * Validate the second password field.
     */
    private void validatePasswordRepeat()
    {
        validateField(m_passwordRepeat, 2, m_passwordRepeatFeedback, Index.getStrings().pwdMatch());
        if (m_passwordRepeat.getText().equals(m_password.getText())) {
            m_passwordRepeatFeedback.setStatus(FormFeedback.VALID);
            m_passwordRepeatFeedback.setTitle("");
        } else {
            m_passwordRepeatFeedback.setStatus(FormFeedback.ERROR);
            m_passwordRepeatFeedback.setTitle(Index.getStrings().pwdMatch());
        }
    }

    /**
     * Validate that the email field is filled in with a valid email address.
     */
    private void validateEmail()
    {
        if (JSUtil.validateEmail(m_email.getText())) {
            m_emailFeedback.setStatus(FormFeedback.VALID);
            m_emailFeedback.setTitle("");
        } else {
            m_emailFeedback.setStatus(FormFeedback.ERROR);
            m_emailFeedback.setTitle(Index.getStrings().email_tt());
        }
    }

    /**
     * Validate that the birthday is filled in, a valid date, and occurs in the past.
     */
    private void validateBirthday()
    {
        if (m_bDay.getText().length() > 2) {
            if (m_bDay.getDateValue() != null) {
                if (m_bDay.getDateValue().after(new Date())) {
                    // user cannot be born tomorrow
                    m_bDayFeedback.setStatus(FormFeedback.ERROR);
                    m_bDayFeedback.setTitle(Index.getStrings().dateFuture_tt());
                } else {
                    m_bDayFeedback.setStatus(FormFeedback.VALID);
                    m_bDayFeedback.setTitle("");
                }
            } else {
                m_bDayFeedback.setStatus(FormFeedback.ERROR);
                m_bDayFeedback.setTitle(Index.getStrings().validDate_tt(m_bDay.getText()));
            }
        } else {
            m_bDayFeedback.setStatus(FormFeedback.WARNING);
            m_bDayFeedback.setTitle(Index.getStrings().birthday_tt());
        }
    }

    /**
     * Validate that the specified field is filled in and valid.
     * 
     * @param tb        the field to validate
     * @param minLength the minimum character length of the field
     * @param feedback  the feedback control for this field
     * @param error     the error to show in the feedback if the field isn't valid
     */
    private void validateField(TextBoxBase tb, int minLength, FormFeedback feedback, String error)
    {
        if (tb.getText().length() > minLength) {
            feedback.setStatus(FormFeedback.VALID);
            feedback.setTitle("");
        } else {
            feedback.setStatus(FormFeedback.WARNING);
            feedback.setTitle(error);
        }
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
        MessageUtil.showMessage(Index.getStrings().formSaveMessage());
    }
}
