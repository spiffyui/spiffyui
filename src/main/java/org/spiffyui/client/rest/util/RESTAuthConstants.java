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
package org.spiffyui.client.rest.util;

/**
 * This interface defines the necessary constants to perform a login for a REST service
 * that follows the Novell architecture council standard for tokenized identity
 */
public interface RESTAuthConstants
{
       /**
         * The username JSON token.
         */
        public static final String USERNAME_TOKEN = "user";
        /**
         * The password JSON token.
         */
        public static final String PASSWORD_TOKEN = "pwd";
        /**
         * The authorization URL JSON token.
         */
        public static final String AUTH_URL_TOKEN = "auth-url";
        /**
         * The authorization logout URL JSON token.
         */
        public static final String AUTH_LOGOUT_URL_TOKEN = "auth-logout-url";
        /**
         * The user token JSON token.
         */
        public static final String USER_TOKEN = "token";

        /**
         * An error code indicating the login request was invalid.
         */
        public static final String INVALID_LOGIN_REQUEST = "InvalidLoginRequest";

        /**
         * An error code indicating the current token is no longer valid.
         */
        public static final String INVALID_AUTH_HEADER = "InvalidAuthHeader";

        /**
         * An error code indicating the logout request was invalid.
         */
        public static final String INVALID_LOGOUT_REQUEST = "InvalidLogoutRequest";

        /**
         * An error code indicating the token server URL was invalid.
         */
        public static final String INVALID_TS_URL = "InvalidTokenServerURL";

        /**
         * An error code indicating there were multiple accounts with the same username.
         */
        public static final String MULTIPLE_ACCOUNTS = "MultipleAccounts";

        /**
         * An error code indicating the JSON sent to the AuthServlet was invalid.
         */
        public static final String INVALID_JSON = "InvalidJSONRequest";

        /**
         * An error code indicating the username or password for login was incorrect.
         */
        public static final String INVALID_INPUT = "InvalidInput";

        /**
         * An error code indicating the user is valid, but doesn't have permission to access the application.
         */
        public static final String NO_PRIVILEGE = "NoPrivilege";

        /**
         * An error code indicating the token server URL can't be found.
         */
        public static final String NOTFOUND_TS_URL = "UnFoundTokenServerURL";

        /**
         * An error code indicating the token is gone from the authentication.
         * server.  Normally because it expired.
         */
        public static final String GONE = "Gone";    
}
