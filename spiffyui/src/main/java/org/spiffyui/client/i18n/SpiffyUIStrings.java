/*******************************************************************************
 * 
 * Copyright 2011 Spiffy UI Team   
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
package org.spiffyui.client.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * <p> 
 * Localized strings for the library classes 
 * </p> 
 *  
 * <p> 
 * <b>This class is not part of the public API and should not be accessed</b> 
 * </p> 
 *
 */
public interface SpiffyUIStrings extends Messages
{
    public String close();
    public String refresh();
    public String statusInProgress();
    public String statusFailed();
    public String statusSucceeded();
    public String displayOptions();
    public String noAuthHeader();
    public String invalidAuthHeader(String authHeader);
    public String noServerContact();
    public String jsonErrorShort();
    public String jsonError();
    public String jsonError2(String url, String respText);
    public String unabledAuthServer();
    public String renew();
    public String loginTitle();
    public String loginDataError(String data);
    public String login();
    public String username();
    public String password();
    public String logout();
    public String repeatlogin();
    public String repeatloginTwo();
    public String invalidTSURL(String reason);
    public String notFoundTSURL(String url);
    public String invalidUsernamePassword();
    public String noPrivilege(String user);
    public String productName();
    public String multipleaccounts();
    public String invalidColon(String value);
    public String invalidColonReason(String value, String reason);
    public String valid();
    public String loading();
    public String percentCompleted(String s);
    public String kiloAbbrev(String number);
    public String megaAbbrev(String number);
    public String gigaAbbrev(String number);

}
