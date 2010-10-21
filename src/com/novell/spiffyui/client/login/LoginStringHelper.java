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
package com.novell.spiffyui.client.login;

import com.google.gwt.core.client.GWT;
import com.novell.spiffyui.client.SpiffyUIStrings;


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
        } else if (msgKey.equals(LoginStrings.PRODUCT_NAME)) {
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
