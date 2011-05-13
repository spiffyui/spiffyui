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
