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
package org.spiffyui.client.login;

/**
 * The Strings used by the login dialog
 *
 */
public enum LoginStrings 
{
    /**
     * This is an error string indicating the username and
     * password were valid, but that user didn't have access
     * to the application.
     */
    NO_PRIVILEGE(),

    /**
     * This string is used for the renew token dialog
     */
    RENEW(),

    /**
     * This string indicates that this is a repeat log
     * or token renew
     */
    REPEAT_LOGIN(),

    /**
     * This string is used for a token renew when the
     * username is unknown.
     */
    REPEAT_LOGIN_TWO(),

    /**
     * This is the login title
     */
    LOGIN(),
    
    /**
     * This is the title of the login panel
     */
    LOGIN_TITLE(),

    /**
     * The product name is used for the title of the login panel
     */
    PRODUCT_NAME(),

    /**
     * The label for the username field
     */
    USERNAME(),

    /**
     * The label for the password field
     */
    PASSWORD(),

    /**
     * The text of the logout button
     */
    LOGOUT(),

    /**
     * An error message indicating an invalid token server URL format
     */
    INVALID_TS_URL(),

    /**
     * An error message indicating the token server could not be found
     */
    NOT_FOUND_TS_URL(),

    /**
     * An error message indicating there was more than one user with
     * the specified username and the username resolution was ambiguous
     */
    MULTIPLE_ACCOUNTS(),

    /**
     * An error message indicating the very common error of an invalid
     * username and password combination
     */
    INVALID_USERNAME_PASSWORD()

    ;
}
