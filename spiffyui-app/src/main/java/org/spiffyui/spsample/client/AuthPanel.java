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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;

import org.spiffyui.client.JSUtil;
import org.spiffyui.client.MessageUtil;
import org.spiffyui.client.rest.RESTException;
import org.spiffyui.client.rest.RESTObjectCallBack;
import org.spiffyui.client.widgets.button.SimpleButton;
import org.spiffyui.spsample.client.rest.SampleAuthBean;

/**
 * This is the authentication documentation panel
 *
 */
public class AuthPanel extends HTMLPanel
{
    private static final SPSampleStrings STRINGS = (SPSampleStrings) GWT.create(SPSampleStrings.class);
    
    /**
     * Creates a new panel
     */
    public AuthPanel()
    {
        super("div", STRINGS.AuthPanel_html());
        
        getElement().setId("authPanel");
        
        RootPanel.get("mainContent").add(this);

        setVisible(false);
        String buttonText = "";
        if (Index.userLoggedIn()) {
            buttonText = Index.getStrings().secData();
        } else {
            buttonText = Index.getStrings().getSecData();
        }
        final SimpleButton authTestButton = new SimpleButton(buttonText);
        
        if (Index.isAppEngine()) {
            authTestButton.setText(Index.getStrings().installMessage());
            authTestButton.setEnabled(false);
        }

        authTestButton.getElement().setId("authTestBtn");
        this.add(authTestButton, "testAuth");

        authTestButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event)
            {
                authTestButton.setInProgress(true);
                //a little timer to simulate time it takes to set in progress back to false
                Timer t = new Timer() {
                    @Override
                    public void run()
                    {
                        authTestButton.setInProgress(false);
                        getData();
                    }

                };
                t.schedule(1000);
            }
        });
        
        /*
         * Add TOC anchors
         */
        Index.addTocAnchor(this, "liAS_Try");
        Index.addTocAnchor(this, "liAS_Token");
        Index.addTocAnchor(this, "liAS_Cloud");
        Index.addTocAnchor(this, "liAS_Custom");
        Index.addTocAnchor(this, "liAS_Proxy");
        Index.addTocAnchor(this, "liAS_Login");
        Index.addTocAnchor(this, "liAS_Logout");
    }

    /**
     * get sample authenticated data
     */
    public static void getData()
    {
        getData(false);
    }

    private static void showAuthenticationData(SampleAuthBean info, boolean inWidgetPanel)
    {
        String data = Index.getStrings().authDataMsg(DateTimeFormat.getFormat("h:mm:ss a").format(info.getDate()),
                                                     info.getToken(), info.getMessage());
        if (!inWidgetPanel) {
            JSUtil.setText("#testAuthResult", data);
        } else {
            JSUtil.setText("#loginResult", data);
        }                
        
        /*
         Add a yellow highlight to show that you've logged in
         */
        DOM.getElementById("loginSection").addClassName("yellowHighlightSection");
    }

    /**
     * get sample authenticated data
     * @param inWidgetPanel  specifies whether the login request is issued from the button in 
     *        widget panel or not (the other place is auth panel)
     *
     */
    public static void getData(final boolean inWidgetPanel)
    {
        SampleAuthBean.getSampleAuthData(new RESTObjectCallBack<SampleAuthBean>() {
            public void success(SampleAuthBean info)
            {
                showAuthenticationData(info, inWidgetPanel);
            }

            public void error(String message)
            {
                MessageUtil.showFatalError(message);
            }

            public void error(RESTException e)
            {
                MessageUtil.showFatalError(e.getReason());
            }
        });
   }
}
