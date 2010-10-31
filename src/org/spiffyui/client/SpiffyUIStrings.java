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
package org.spiffyui.client;

import com.google.gwt.i18n.client.Messages;

/**
 * Localized strings for the library classes
 *
 */
public interface SpiffyUIStrings extends Messages {

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

}
