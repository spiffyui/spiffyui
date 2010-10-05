package com.novell.spsample.client.rest;

import com.novell.spiffyui.client.MessageUtil;
import com.novell.spiffyui.client.rest.AuthUtil;
import com.novell.spiffyui.client.rest.RESTCallback;

public class SampleAuthUtil extends AuthUtil
{
    public void showLogin(RESTCallback callback, String tokenServerUrl, String code)
    {
        MessageUtil.showMessage("Calling SampleAuthUtil's showLogin!");    
    }
}
