/*
 * Copyright (c) 2010, 2011 Unpublished Work of Novell, Inc. All Rights Reserved.
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
package org.spiffyui.spsample.client;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import org.spiffyui.client.JSUtil;
import org.spiffyui.client.rest.AuthUtil;
import org.spiffyui.client.rest.RESTException;
import org.spiffyui.client.rest.RESTility;
import org.spiffyui.client.rest.RESTObjectCallBack;
import org.spiffyui.client.widgets.FormFeedback;
import org.spiffyui.client.widgets.button.FancyButton;
import org.spiffyui.client.widgets.button.FancySaveButton;

/**
 * This is the authentication test panel
 *
 */
public class AuthTestPanel extends HTMLPanel implements KeyUpHandler
{
    private static final SPSampleStrings STRINGS = (SPSampleStrings) GWT.create(SPSampleStrings.class);
    
    private TextBox m_url;
    private FormFeedback m_urlFeedback;

    private TextBox m_tokenType;
    private FormFeedback m_tokenTypeFeedback;
    
    private TextBox m_username;
    private FormFeedback m_usernameFeedback;
    
    private TextBox m_password;
    private FormFeedback m_passwordFeedback;
    
    private FancyButton m_test;

    private HTML m_message;

    private ArrayList<FormFeedback> m_feedbacks = new ArrayList<FormFeedback>();
    
    /**
     * Creates a new authentication test panel
     */
    public AuthTestPanel()
    {
        super("div", STRINGS.AuthTestPanel_html());
        
        getElement().setId("authTestPanel");
        
        RootPanel.get("mainContent").add(this);
        
        setVisible(false);

        /*
         authentication server URL
         */
        m_url = new TextBox();
        m_url.addKeyUpHandler(this);
        m_url.getElement().setId("authURLTxt");
        m_url.getElement().addClassName("wideTextField");
        add(m_url, "authURL");
        
        m_urlFeedback = new FormFeedback();
        m_feedbacks.add(m_urlFeedback);
        add(m_urlFeedback, "authURLRow");

        /*
         token type
         */
        m_tokenType = new TextBox();
        m_tokenType.addKeyUpHandler(this);
        m_tokenType.getElement().setId("authTokenTxt");
        m_tokenType.setText("X-OPAQUE");
        m_tokenType.getElement().addClassName("slimTextField");
        add(m_tokenType, "authToken");
        
        m_tokenTypeFeedback = new FormFeedback();
        m_feedbacks.add(m_tokenTypeFeedback);
        m_tokenTypeFeedback.setStatus(FormFeedback.VALID);
        add(m_tokenTypeFeedback, "authTokenRow");
        
        /*
         username
         */
        m_username = new TextBox();
        m_username.addKeyUpHandler(this);
        m_username.getElement().setId("authUsernameTxt");
        m_username.getElement().addClassName("wideTextField");
        add(m_username, "authUsername");
        
        m_usernameFeedback = new FormFeedback();
        m_feedbacks.add(m_usernameFeedback);
        add(m_usernameFeedback, "authUsernameRow");

        /*
         password
         */
        m_password = new PasswordTextBox();
        m_password.addKeyUpHandler(this);
        m_password.getElement().setId("authPasswordTxt");
        m_password.getElement().addClassName("wideTextField");
        add(m_password, "authPassword");
        
        m_passwordFeedback = new FormFeedback();
        m_feedbacks.add(m_passwordFeedback);
        add(m_passwordFeedback, "authPasswordRow");
        
        /*
         The big test button
         */
        m_test = new FancySaveButton(Index.getStrings().test());
        if (Index.isAppEngine()) {
            m_test.setEnabled(false);
            m_test.setText(Index.getStrings().installMessage());
        }
        
        m_test.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event)
                {
                    login();
                }
            });
        
        add(m_test, "authTestButtons");
        updateFormStatus(null);

        m_message = new HTML();
        m_message.getElement().setId("authTestLongMessageWidget");
        add(m_message, "authTestLongMessage");
    }
    
    @Override
    public void onKeyUp(KeyUpEvent event)
    {
        if (event.getNativeKeyCode() != KeyCodes.KEY_TAB) {
            updateFormStatus((Widget) event.getSource());
        } 

        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER &&
            m_test.isEnabled()) {
            login();
        }
    }
    
    private void updateFormStatus(Widget w)
    {
        if (w == m_url) {
            validateField(m_url, m_urlFeedback, Index.getStrings().invalidURL_ff_tt());
        } else if (w == m_tokenType) {
            validateField(m_tokenType, m_tokenTypeFeedback, Index.getStrings().invalidTokenType_ff_tt());
        } else if (w == m_username) {
            validateField(m_username, m_usernameFeedback, Index.getStrings().invalidUsername_ff_tt());
        } else if (w == m_password) {
            validateField(m_password, m_passwordFeedback, Index.getStrings().invalidPassword_ff_tt());
        }
        
        if (Index.isAppEngine()) {
            m_test.setEnabled(false);
            return;
        }

        /*
         * We only want to enable the save button if every field is valid
         */
        for (FormFeedback feedback : m_feedbacks) {
            if (feedback.getStatus() != FormFeedback.VALID) {
                m_test.setEnabled(false);
                return;
            }
        }

        m_test.setEnabled(true);
    }

    private void validateField(TextBox tb, FormFeedback feedback, String error)
    {
        if (tb.getText().length() > 1) {
            feedback.setStatus(FormFeedback.VALID);
            feedback.setTitle("");
        } else {
            feedback.setStatus(FormFeedback.WARNING);
            feedback.setTitle(error);
        }
    }

    private void appendMessage(String message)
    {
        m_message.setHTML(m_message.getHTML() + message);
    }
    
    private void login()
    {
        m_test.setInProgress(true);
        m_message.setHTML("");

        RESTility.setTokenType(m_tokenType.getText().trim());
        appendMessage(Index.getStrings().loggingIn(m_url.getText()));

        new AuthUtil().login(m_username.getText(), m_password.getText(), m_url.getText(), new RESTObjectCallBack<String>() {
            public void success(String info)
            {
                appendMessage("<br /><span class=\"testSuccess\">" + Index.getStrings().loggedIn(info) + "</span><br />");
                logout();
            }

            public void error(String message)
            {
                m_test.setInProgress(false);
                appendMessage("<br /><span class=\"testFail\">" + Index.getStrings().loginFail1(message) + "</span><br />");
            }

            public void error(RESTException e)
            {
                m_test.setInProgress(false);
                appendMessage("<br /><span class=\"testFail\">" + Index.getStrings().loginFail2(e.getCode(), e.getReason()) + "</span><br />");
            }
        });
    }

    private void logout()
    {
        appendMessage("<br />" + Index.getStrings().logoutAttempt());
        new AuthUtil().logout(new RESTObjectCallBack<String>() {
            public void success(String info)
            {
                appendMessage("<br /><span class=\"testSuccess\">" + Index.getStrings().logoutSuccess(info) + "</span><br />");
                JSUtil.hide("#header_actionsBlock", "fast");
                m_test.setInProgress(false);
            }

            public void error(String message)
            {
                m_test.setInProgress(false);
                appendMessage("<br /><span class=\"testFail\">" + Index.getStrings().logoutFail1(message) + "</span><br />");
            }

            public void error(RESTException e)
            {
                m_test.setInProgress(false);
                appendMessage("<br /><span class=\"testFail\">" + Index.getStrings().logoutFail2(e.getCode(), e.getReason()) + "</span><br />");
            }
        });
    }
}
