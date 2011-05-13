package org.spiffyui.spsample.client.rest;

import org.spiffyui.client.login.LoginStringHelper;
import org.spiffyui.client.login.LoginStrings;
import org.spiffyui.client.rest.AuthUtil;

/**
 * This is a sample authentication helper to provide custom strings to the login panel
 */
public class SampleAuthUtil extends AuthUtil
{
    @Override
    protected LoginStringHelper getStringHelper()
    {
        return new SPSampleStringHelper();
    }

    /**
     * This class provides a string for the title of the login dialog
     */
    private static class SPSampleStringHelper extends LoginStringHelper
    {
        @Override
        public String getString(LoginStrings msgKey)
        {
            return getString(msgKey, null);
        }

        @Override
        public String getString(LoginStrings msgKey, String arg1)
        {
            if (msgKey.equals(LoginStrings.LOGIN_TITLE)) {
                return "Login with any username and password you want";
            } else {
                return super.getString(msgKey, arg1);
            }

        }
    }
}
