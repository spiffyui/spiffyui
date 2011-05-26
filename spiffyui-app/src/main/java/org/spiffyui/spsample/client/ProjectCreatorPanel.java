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
import java.util.List;

import org.spiffyui.client.widgets.FormFeedback;
import org.spiffyui.client.widgets.button.SimpleButton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;


/**
 * This is the landing panel
 *
 */
public class ProjectCreatorPanel extends HTMLPanel implements KeyUpHandler
{
    private static final SPSampleStrings STRINGS = (SPSampleStrings) GWT.create(SPSampleStrings.class);
    
    private static final String WIDE_TEXT_FIELD = "wideTextField";
    
    private TextBox m_projectName;
    private FormFeedback m_projectNameFeedback;
    
    private TextBox m_packageName;
    private FormFeedback m_packageNameFeedback;

    private List<FormFeedback> m_feedbacks = new ArrayList<FormFeedback>();

    private SimpleButton m_submit;

    /**
     * Creates a new panel
     */
    public ProjectCreatorPanel()
    {
        super("div", STRINGS.ProjectCreatorPanel_html());

        /*
         * Add project and package fields and button
         */
        m_projectName = new TextBox();
        m_projectName.addKeyUpHandler(this);
        m_projectName.getElement().setId("projectNameTxt");
        m_projectName.getElement().addClassName(WIDE_TEXT_FIELD);
        add(m_projectName, "projectName");
           
        m_projectNameFeedback = new FormFeedback();
        m_feedbacks.add(m_projectNameFeedback);
        add(m_projectNameFeedback, "projectNameRow");
           
        m_packageName = new TextBox();
        m_packageName.addKeyUpHandler(this);
        m_packageName.getElement().setId("packageNameTxt");
        m_packageName.getElement().addClassName(WIDE_TEXT_FIELD);
        add(m_packageName, "packageName");
           
        m_packageNameFeedback = new FormFeedback();
        m_feedbacks.add(m_packageNameFeedback);
        add(m_packageNameFeedback, "packageNameRow");
          
        m_submit = new SimpleButton(Index.getStrings().projectCreatorSubmit());
        m_submit.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                createProject();
            }
        });
  
        add(m_submit, "projectBuilderButtons");
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
        if (w == m_projectName) {
            validateProjectName();
        } else if (w == m_packageName) {
            validatePackageName();
        }
        enableCreateButton();
    }

    /**
     * Validate that the project name field is filled in and valid
     * It must contain only alpha numeric characters, and length must be at least 2
     */
    private void validateProjectName()
    {
        if (m_projectName.getText() != null && m_projectName.getText().length() > 1) {
            if (isAlphaNumericUnderscore(m_projectName.getText())) {
                m_projectNameFeedback.setStatus(FormFeedback.VALID);
                m_projectNameFeedback.setTitle("");
            } else {
               m_projectNameFeedback.setStatus(FormFeedback.ERROR);
               m_projectNameFeedback.setTitle(Index.getStrings().projNameValidChar_tt());
            }
        } else {
            m_projectNameFeedback.setStatus(FormFeedback.WARNING);
            m_projectNameFeedback.setTitle(Index.getStrings().projNameTooShort_tt());
        }
    }

    private boolean isAlphaNumericUnderscore(String str)
    {
        for (int i = 0; i < str.length(); i++) {
            //If we find a non-alphanumeric character we return false.
            if ((!Character.isLetterOrDigit(str.charAt(i))) && (!(Character.toString(str.charAt(i)).equals("_")))) {
                return false;
            }
        }
        return true;
    }


    /**
     * Validate that the package name field is filled in with a valid package name.
     * Package name can only contain alpha numeric, underscore and dot characters
     * It cannot start with dot or number, can not end with dot
     * It can not contain reserved java keywords (add an underscore)
     * package name will be converted to lowercase
     */
    private void validatePackageName()
    {
        m_packageNameFeedback.setStatus(FormFeedback.VALID);
        m_packageNameFeedback.setTitle("");
    }

    /**
     * Enable or disable the create project button based on the state of the fields.
     */
    private void enableCreateButton()
    {
        /*
         * We only want to enable the create button if every field is valid
         */
        for (FormFeedback feedback : m_feedbacks) {
            if (feedback.getStatus() != FormFeedback.VALID) {
                m_submit.setEnabled(false);
                return;
            }
        }
        m_submit.setEnabled(true);
    }

    private void createProject()
    {
        logToGoogleAnalytics();
        Window.Location.replace("/createProject?type=ant&projectName=" + m_projectName.getText() + 
            "&packagePath=" + m_packageName.getText());
    }
    
    private static native void logToGoogleAnalytics() /*-{
        $wnd._gaq.push(['_trackPageview', '/createProject']);
    }-*/;
}
