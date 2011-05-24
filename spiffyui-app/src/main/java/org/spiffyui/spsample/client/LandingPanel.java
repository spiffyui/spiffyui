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

import org.spiffyui.client.JSUtil;
import org.spiffyui.client.widgets.FormFeedback;
import org.spiffyui.client.widgets.button.SimpleButton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel; 
import com.google.gwt.user.client.ui.TextBox;
 

/**
 * This is the landing panel
 *
 */
public class LandingPanel extends HTMLPanel
{
    private static final SPSampleStrings STRINGS = (SPSampleStrings) GWT.create(SPSampleStrings.class);
    
    private static final String WIDE_TEXT_FIELD = "wideTextField";
    
    private TextBox m_projectName;
    private FormFeedback m_projectNameFeedback;
    
    private TextBox m_packageName;
    private FormFeedback m_packageNameFeedback;

    private SimpleButton m_submit;

    /**
     * Creates a new panel
     */
    public LandingPanel()
    {
        super("div", STRINGS.LandingPanel_html());

        getElement().setId("landingPanel");

        RootPanel.get("mainContent").add(this);
        
        /*
         * Add the documentation anchor
         */
        Anchor doc = new Anchor(Index.getStrings().documentation(), "#");
        doc.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event)
            {
                event.preventDefault();
                JSUtil.println("Selecting the overview item...");
                Index.selectItem(Index.OVERVIEW_NAV_ITEM_ID);
            }
        });
        add(doc, "landingDoc");
        
        /*
         * Add project and package fields and button
         */
        m_projectName = new TextBox();
        //       m_projectName.addKeyUpHandler(this);
        m_projectName.getElement().setId("projectNameTxt");
        m_projectName.getElement().addClassName(WIDE_TEXT_FIELD);
        add(m_projectName, "projectName");
           
        m_projectNameFeedback = new FormFeedback();
        //       m_feedbacks.add(m_projectNameFeedback);
        add(m_projectNameFeedback, "projectNameRow");
           
        m_packageName = new TextBox();
        //       m_packageName.addKeyUpHandler(this);
        m_packageName.getElement().setId("packageNameTxt");
        m_packageName.getElement().addClassName(WIDE_TEXT_FIELD);
        add(m_packageName, "packageName");
           
        m_packageNameFeedback = new FormFeedback();
        //       m_feedbacks.add(m_packageNameFeedback);
        add(m_packageNameFeedback, "packageNameRow");
          
        m_submit = new SimpleButton(Index.getStrings().projectCreatorSubmit());
        m_submit.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event)
            {
                createProject();
            }
        });
  
        add(m_submit, "projectBuilderButtons");
    }
    
    private void createProject()
    {
        Window.open("/createProject?type=ant&projectName=" + m_projectName.getText() + 
            "&packagePath=" + m_packageName.getText(), "CreateProj", "");
    }
}
