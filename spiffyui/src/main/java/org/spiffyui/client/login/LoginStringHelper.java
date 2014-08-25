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
package org.spiffyui.client.login;

import com.google.gwt.core.client.GWT;
import org.spiffyui.client.i18n.SpiffyUIStrings;


/**
 * The LoginStingHelper is a helper class to resolve the strings used
 * in the login dialog.  If the client is using default GWT localization
 * then they can just use this instance.  If not then they can extend
 * this class to provide specialized string resolution.
 */
public class LoginStringHelper 
{
    private static final SpiffyUIStrings STRINGS = (SpiffyUIStrings) GWT.create(SpiffyUIStrings.class);

    /**
     * Get the specified localized string
     * 
     * @param msgKey the message key for the string
     * 
     * @return the localized string
     */
    public String getString(LoginStrings msgKey)
    {
        return getString(msgKey, null);
    }

    /**
     * Get the specified localized string
     * 
     * @param msgKey the message key for the string
     * @param arg1   the argument for the string
     * 
     * @return the localized string
     */
    public String getString(LoginStrings msgKey, String arg1)
    {
        if (msgKey.equals(LoginStrings.NO_PRIVILEGE)) {
            return STRINGS.noPrivilege(arg1);
        } else if (msgKey.equals(LoginStrings.RENEW)) {
            return STRINGS.renew();
        } else if (msgKey.equals(LoginStrings.REPEAT_LOGIN)) {
            return STRINGS.repeatlogin();
        } else if (msgKey.equals(LoginStrings.REPEAT_LOGIN_TWO)) {
            return STRINGS.repeatloginTwo();
        } else if (msgKey.equals(LoginStrings.LOGIN) ||
                   msgKey.equals(LoginStrings.LOGIN_TITLE)) {
            return STRINGS.login();
        } else {
            return getString2(msgKey, arg1);
        }

    }
    
    
    /**
     * This method just gets back strings based on the message key.  It could be one method,
     * but we split it up to make it a little easier to read.
     * 
     * @param msgKey the message key for this string
     * @param arg1   the string argument
     * 
     * @return the localized string
     */
    private String getString2(LoginStrings msgKey, String arg1)
    {
        if (msgKey.equals(LoginStrings.PRODUCT_NAME)) {
            return STRINGS.productName();
        } else if (msgKey.equals(LoginStrings.USERNAME)) {
            return STRINGS.username();
        } else if (msgKey.equals(LoginStrings.PASSWORD)) {
            return STRINGS.password();
        } else if (msgKey.equals(LoginStrings.LOGOUT)) {
            return STRINGS.logout();
        } else if (msgKey.equals(LoginStrings.INVALID_TS_URL)) {
            return STRINGS.invalidTSURL(arg1);
        } else if (msgKey.equals(LoginStrings.NOT_FOUND_TS_URL)) {
            return STRINGS.notFoundTSURL(arg1);
        } else if (msgKey.equals(LoginStrings.MULTIPLE_ACCOUNTS)) {
            return STRINGS.multipleaccounts();
        } else if (msgKey.equals(LoginStrings.INVALID_USERNAME_PASSWORD)) {
            return STRINGS.invalidUsernamePassword();
        } else {
            /*
             * This should never happen since we are coverring all the fields
             * in the enum
             */
            return null;
        }
    }
}
