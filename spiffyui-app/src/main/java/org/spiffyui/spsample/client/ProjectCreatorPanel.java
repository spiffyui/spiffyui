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
import java.util.Arrays;
import java.util.List;

import org.spiffyui.client.JSUtil;
import org.spiffyui.client.widgets.FormFeedback;
import org.spiffyui.client.widgets.button.SimpleButton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;


/**
 * This is the landing panel
 *
 */
public class ProjectCreatorPanel extends HTMLPanel implements KeyUpHandler, KeyPressHandler
{
    private static final SPSampleStrings STRINGS = (SPSampleStrings) GWT.create(SPSampleStrings.class);
    
    private static final String WIDE_TEXT_FIELD = "wideTextField";

    private static final String[] JAVA_KEYWORDS =
            {"abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
             "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "float",
             "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native",
             "new", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super",
             "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while",
             "false", "null", "true" };
    
    private TextBox m_projectName;
    private FormFeedback m_projectNameFeedback;
    
    private TextBox m_packageName;
    private FormFeedback m_packageNameFeedback;

    private List<FormFeedback> m_feedbacks = new ArrayList<FormFeedback>();

    private SimpleButton m_submit;
    
    private InlineLabel m_buildType;
    private RadioButton m_maven;
    private RadioButton m_ant;
    
    private static final String TYPE_MAVEN = "maven";
    private static final String TYPE_ANT = "ant";
    
    private String m_type = TYPE_MAVEN;
    /**
     * Creates a new panel
     * @param id - the ID of the parent panel or some other way to uniquely prefix IDs on within this HTML fragment
     */
    public ProjectCreatorPanel(String id)
    {
        super("div", getHTML(id));
        
        /*
         * Add project and package fields and button
         */
        m_projectName = new TextBox();
        m_projectName.setTitle(Index.getStrings().projectName_tt());
        m_projectName.addKeyUpHandler(this);
        m_projectName.addKeyPressHandler(this);
        m_projectName.getElement().setId(id + "projectNameTxt");
        m_projectName.getElement().addClassName(WIDE_TEXT_FIELD);
        add(m_projectName, id + "projectName");
           
        m_projectNameFeedback = new FormFeedback();
        m_feedbacks.add(m_projectNameFeedback);
        add(m_projectNameFeedback, id + "projectNameRow");
           
        m_packageName = new TextBox();
        m_packageName.setTitle(Index.getStrings().packageName_tt());
        m_packageName.addKeyUpHandler(this);
        m_packageName.addKeyPressHandler(this);
        m_packageName.getElement().setId(id + "packageNameTxt");
        m_packageName.getElement().addClassName(WIDE_TEXT_FIELD);
        add(m_packageName, id + "packageName");
           
        m_packageNameFeedback = new FormFeedback();
        m_feedbacks.add(m_packageNameFeedback);
        add(m_packageNameFeedback, id + "packageNameRow");
          
        addBuildTypes(id);
        
        m_submit = new SimpleButton(Index.getStrings().projectCreatorSubmit());
        m_submit.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                event.preventDefault();
                createProject();
            }
        });
  
        add(m_submit, id + "projectCreatorButtons");        
        
        Anchor backToCreate = new Anchor(Index.getStrings().backToCreate(), "#");
        backToCreate.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event)
            {
                event.preventDefault();
                JSUtil.hide(getElement().getId() + "createFormInst");
                JSUtil.show(getElement().getId() + "createForm");
            }
        });
        add(backToCreate, id + "backToCreateAnchor");
        
        getElement().setId(id);

        updateFormStatus(null);
    }
    
    private void addBuildTypes(String id)
    {
        String radioGroup = HTMLPanel.createUniqueId();
        m_ant = new RadioButton(radioGroup, Index.getStrings().buildWithAnt());
        m_ant.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event)
            {
                m_type = TYPE_ANT;
            }
        });
        add(m_ant, id + "buildTypes");

        m_maven = new RadioButton(radioGroup, Index.getStrings().buildWithMaven());
        m_maven.addStyleName("radioOption");
        m_maven.setValue(true);
        m_maven.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event)
            {
                m_type = TYPE_MAVEN;
            }
        });
        
        add(m_maven, id + "buildTypes");
        m_maven.setChecked(true);
    }
    
    @Override
    public void onKeyPress(KeyPressEvent event)
    {
        /*
         We want to submit the request if the user pressed enter
         */
        if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER &&
            m_submit.isEnabled()) {
            createProject();
            
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event)
    {        
        if (event.getNativeKeyCode() != KeyCodes.KEY_TAB) {
            updateFormStatus((Widget) event.getSource());
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                if (enableCreateButton()) {
                    JSUtil.hide(getElement().getId() + "createForm");
                    JSUtil.show(getElement().getId() + "createFormInst");
                    createProject();
                }
            }
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
               m_projectNameFeedback.setTitle(Index.getStrings().projNameInValidChar_tt());
            }
        } else {
            m_projectNameFeedback.setStatus(FormFeedback.WARNING);
            m_projectNameFeedback.setTitle(Index.getStrings().projNameTooShort_tt());
        }
    }

    private boolean isAlphaNumericUnderscore(String str)
    {
        for (int i = 0; i < str.length(); i++) {
            //If we find a non-alphanumeric or non-underscore character we return false.
            if ((!Character.isLetterOrDigit(str.charAt(i))) && (!(Character.toString(str.charAt(i)).equals("_")))) {
                return false;
            }
        }
        return true;
    }

    private boolean isDigit(String str)
    {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    /**
     * Validate that the package name field is filled in with a valid package name.
     * Package name cannot start or end with dot
     * Each package component part can not start with or contain only number, or be empty (2 consecutive dots)
     * Each package component part can only contain alpha numeric and underscore characters
     * Each package component part can not be the same name as reserved java keywords
     * Package name can not be org.spiffyui
     * Package string will be converted to all lowercase
     */
    private void validatePackageName()
    {
        if (m_packageName.getText() != null && m_packageName.getText().length() > 0) {
            if (m_packageName.getText().startsWith(".")  || (m_packageName.getText().endsWith("."))) {
                m_packageNameFeedback.setStatus(FormFeedback.ERROR);
                m_packageNameFeedback.setTitle(Index.getStrings().packageNameDot_tt());
            } else if (m_packageName.getText().trim().toLowerCase().startsWith("org.spiffyui")) {
                m_packageNameFeedback.setStatus(FormFeedback.ERROR);
                m_packageNameFeedback.setTitle(Index.getStrings().packageNameSpiffy_tt());
            } else {
                String[] result = m_packageName.getText().split("\\.");
                for (int i = 0; i < result.length; i++) {
                    String packageToken = result[i];
                    if (packageToken.length() <= 0) {
                        m_packageNameFeedback.setStatus(FormFeedback.ERROR);
                        m_packageNameFeedback.setTitle(Index.getStrings().packageNameEmpty_tt());
                        break; 
                    } else if (isDigit(packageToken) || isDigit(Character.toString(packageToken.charAt(0)))) {
                        m_packageNameFeedback.setStatus(FormFeedback.ERROR);
                        m_packageNameFeedback.setTitle(Index.getStrings().packageNameDigit_tt());
                        break;
                    } else if (!isAlphaNumericUnderscore(packageToken)) {
                        m_packageNameFeedback.setStatus(FormFeedback.ERROR);
                        m_packageNameFeedback.setTitle(Index.getStrings().packageNameInvalidChar_tt());
                        break;
                    } else if (Arrays.asList(JAVA_KEYWORDS).contains(packageToken)) {
                        m_packageNameFeedback.setStatus(FormFeedback.ERROR);
                        m_packageNameFeedback.setTitle(Index.getStrings().packageNameKeyword_tt());
                        break;
                    } else {
                        m_packageNameFeedback.setStatus(FormFeedback.VALID);
                        m_packageNameFeedback.setTitle("");
                        m_packageName.setText((m_packageName.getText().toLowerCase()));
                    }
                }
            }
        } else { //no package name, which is fine
            m_packageNameFeedback.setStatus(FormFeedback.VALID);
            m_packageNameFeedback.setTitle("");
        }
    }

    /**
     * Enable or disable the create project button based on the state of the fields.
     * and return a flag indicating if the create button is enabled or not
     */
    private boolean enableCreateButton()
    {
        /*
         * We only want to enable the create button if every field is valid
         */
        for (FormFeedback feedback : m_feedbacks) {
            if (feedback.getStatus() != FormFeedback.VALID) {
                m_submit.setEnabled(false);
                return false;
            }
        }
        m_submit.setEnabled(true);
        return true;
    }

    private void createProject()
    {
        setProjectName(m_projectName.getText());
        JSUtil.hide(getElement().getId() + "createForm");
        JSUtil.show(getElement().getId() + "createFormInst");

        logToGoogleAnalytics();
        Window.Location.replace("/createProject?type=" + m_type + "&projectName=" + m_projectName.getText() + 
            "&packagePath=" + m_packageName.getText());
    }
    
    @Override
    public void onLoad()
    {
        super.onLoad();
        /*
         Let's set focus into the text field when the page first loads
         */
        m_projectName.setFocus(true);
    }
    
    private static native void logToGoogleAnalytics() /*-{
        $wnd._gaq.push(['_trackPageview', '/createProject']);
    }-*/;
    
    /**
     * Set the name of the project in the instructions.
     * 
     * @param name   the project name
     */
    private void setProjectName(String name)
    {
        getElementById(getElement().getId() + "createFormInst1").setInnerHTML(
            Index.getStrings().downloadProjInstr1(name, name));
                
        getElementById(getElement().getId() + "createFormInst2").setInnerHTML(
            TYPE_ANT.equals(m_type) ? Index.getStrings().downloadProjInstr2a(name) : Index.getStrings().downloadProjInstr2(name));
    }
    
    private static final String getHTML(String id) 
    {
        return
    "<div class=\"createForm\" id=\"" + id + "createForm\">" +
        "<h2 id=\"" + id + "createFormH2\" title=\"" + Index.getStrings().projectCreatorTitle_tt() + "\">" + Index.getStrings().projectCreatorTitle() + "</h2>" +
        "<fieldset class=\"projectCreatorFields\">" +
            "<ol class=\"dialogformsection\">" +
                "<li id=\"" + id + "projectNameRow\" class=\"dialogformrow\">" +
                    "<label class=\"dialogformlabel\" for=\"" + id + "projectNameTxt\">" + Index.getStrings().projectName() + "</label>" +
                    "<div id=\"" + id + "projectName\" class=\"formcontrolssection\"></div>" +
                "</li>" +                
                "<li id=\"" + id + "packageNameRow\" class=\"dialogformrow\">" +
                    "<label class=\"dialogformlabel\" for=\"" + id + "packageNameTxt\">" + Index.getStrings().packageName() + "</label>" +
                    "<div id=\"" + id + "packageName\" class=\"formcontrolssection\"></div>" +
                "</li>" +
                "<li id=\"" + id + "buildTypeRow\" class=\"dialogformrow\">" +
                    "<label class=\"dialogformlabel\" for=\"" + id + "buildTypes\">" + Index.getStrings().buildTypes() + "</label>" +
                    "<div id=\"" + id + "buildTypes\" class=\"formcontrolssection\"></div>" +
                "</li>" +
                "<li id=\"" + id + "projectCreatorButtonsRow\" class=\"dialogformrow\">" +
                    "<div id=\"" + id + "projectCreatorButtons\" class=\"formcontrolssection formbuttons\"></div>" +
                "</li>" +
            "</ol>" +
        "</fieldset>" +
    "</div>" +
    "<div class=\"createFormInst createForm\" id=\"" + id + "createFormInst\">" +
       "<h2>" + Index.getStrings().downloadProjInstr() + "</h2>" +
        "<ol>" +
            "<li id=\"" + id + "createFormInst1\">" + Index.getStrings().downloadProjInstr1("", "") + "</li>" +
            "<li id=\"" + id + "createFormInst2\">" + Index.getStrings().downloadProjInstr2("") + "</li>" +
            "<li>" + Index.getStrings().downloadProjInstr3() + "</li>" +
        "</ol>" +
        "<p>" + Index.getStrings().downloadProjInstr4() + "</p>" +
        "<h3  class=\"backToCreateAnchor\" id=\"" + id + "backToCreateAnchor\"></h3>" +
    "</div>";
    }
}
