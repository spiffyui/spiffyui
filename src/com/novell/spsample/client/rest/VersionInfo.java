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
package com.novell.spsample.client.rest;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

import com.novell.spiffyui.client.MessageUtil;
import com.novell.spiffyui.client.rest.AuthUtil;
import com.novell.spiffyui.client.rest.RESTCallback;
import com.novell.spiffyui.client.rest.RESTException;
import com.novell.spiffyui.client.rest.RESTObjectCallBack;
import com.novell.spiffyui.client.rest.RESTility;


/**
 * A bean for holding version info
 */
public final class VersionInfo
{
    private static VersionInfo g_versionInfo;

    private String m_version;

    private VersionInfo(String version)
    {
        m_version = version;
    }

    /**
     * Gets the version
     *
     * @return the version
     */
    public String getVersion()
    {
        return m_version;
    }

    /**
     * Gets the version info from the server
     *
     * @param callback the REST callback
     */
    public static void getVersionInfo(final RESTObjectCallBack<VersionInfo> callback)
    {
        if (g_versionInfo != null) {
            /*
             The version info doesn't change while the page is still running
             so we can cache it and save an HTTP call.
             */
            callback.success(g_versionInfo);
            return;
        }
        
        RESTility.setAuthProvider(new AuthUtil());        
        //RESTility.setAuthProvider(new SampleAuthUtil());
        //RESTility.callREST(RESTility.getCoreContext() + "/rpt/conf", new RESTCallback() {
        RESTility.callREST("version", new RESTCallback() {
                @Override
                public void onSuccess(JSONValue val)
                {
                    JSONObject bi = val.isObject();
                    if (bi == null) {
                        MessageUtil.showError("An error occurred trying to get version info.");
                        return;
                    }
                    g_versionInfo = new VersionInfo(bi.get("version").isString().stringValue());
                    callback.success(g_versionInfo);
                }

                @Override
                public void onError(int statusCode, String errorResponse)
                {
                    callback.error(errorResponse);
                }

                @Override
                public void onError(RESTException e)
                {
                    callback.error(e);
                }
            });
    }
}
