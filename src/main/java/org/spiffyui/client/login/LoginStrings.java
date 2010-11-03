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
