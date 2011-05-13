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
package org.spiffyui.spsample.client.rest;

import org.spiffyui.client.JSONUtil;
import org.spiffyui.client.MessageUtil;
import org.spiffyui.client.rest.RESTCallback;
import org.spiffyui.client.rest.RESTException;
import org.spiffyui.client.rest.RESTObjectCallBack;
import org.spiffyui.client.rest.RESTility;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;


/**
 * A bean for holding version info
 */
public final class VersionInfo
{
    private static VersionInfo g_versionInfo;

    private String m_version;
    private String m_user;
    private String m_rev;
    private String m_date;
    private String m_revDate;

    private VersionInfo()
    {
        
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
    
    public String getUser()
    {
        return m_user;
    }
    
    public String getRevision()
    {
        return m_rev;
    }
    
    public String getDate()
    {
        return m_date;
    }

    public String getRevDate()
    {
        return m_revDate;
    }

    private static void createVersionInfo(JSONValue val, RESTObjectCallBack<VersionInfo> callback)
    {
        JSONObject bi = val.isObject();
        if (bi == null) {
            MessageUtil.showError("An error occurred trying to get version info.");
            return;
        }
        g_versionInfo = new VersionInfo();
        
        g_versionInfo.m_version = JSONUtil.getStringValue(bi, "version");
        g_versionInfo.m_user = JSONUtil.getStringValue(bi, "user");
        g_versionInfo.m_rev = JSONUtil.getStringValue(bi, "rev");
        g_versionInfo.m_date = JSONUtil.getStringValue(bi, "date");
        g_versionInfo.m_revDate = JSONUtil.getStringValue(bi, "revdate");
        
        callback.success(g_versionInfo);
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

        RESTility.callREST("version", new RESTCallback() {
                @Override
                public void onSuccess(JSONValue val)
                {
                    createVersionInfo(val, callback);
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
