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

import org.spiffyui.client.MainHeader;
import org.spiffyui.client.MessageUtil;
import org.spiffyui.client.rest.RESTException;
import org.spiffyui.client.rest.RESTObjectCallBack;
import org.spiffyui.client.rest.RESTility;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;

/**
 * This is the header for SPSample.
 *
 */
public class SPSampleHeader extends MainHeader
{
    /**
     * Creates a new SPSampleHeader panel
     */
    public SPSampleHeader()
    {
        Anchor logout = new Anchor(Index.getStrings().logout(), "#");
        logout.getElement().setId("header_logout");
        setLogout(logout);
        if (!Index.userLoggedIn()) {
            logout.setVisible(false);
            setWelcomeString("");            
        } else {
            String token = RESTility.getUserToken();
            int dashIdx = token.indexOf('-');
            if (dashIdx != -1) {
                setWelcomeString(Index.getStrings().welcome(token.substring(0, dashIdx)));
            } else {
                setWelcomeString(Index.getStrings().welcome(token));
            }
        }
        logout.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event)
                {
                    event.preventDefault();
                    doLogout();
                }
            });
    }

    /**
     * Logout of the application
     */
    public static void doLogout()
    {
        RESTility.getAuthProvider().logout(new RESTObjectCallBack<String>()
                        {
            public void success(String message)
            {
                Window.Location.reload();
            }

            public void error(String message)
            {
                Window.Location.reload();
            }

            public void error(RESTException e)
            {
                MessageUtil.showFatalError(e.getReason());
            }
        });
    }
}

