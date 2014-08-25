/*******************************************************************************
 *
 * Copyright 2011-2014 Spiffy UI Team   
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

import java.util.Date;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

import org.spiffyui.client.MessageUtil;
import org.spiffyui.client.rest.RESTCallback;
import org.spiffyui.client.rest.RESTException;
import org.spiffyui.client.rest.RESTObjectCallBack;
import org.spiffyui.client.rest.RESTility;

/**
 * A bean holding sample auth widget data
 */
public final class SampleAuthBean
{
    private static SampleAuthBean g_sampleAuthBean;

    private String m_message;
    private String m_token;
    private Date m_date;

    private SampleAuthBean(String message, String token, Date date)
    {
        m_message = message;
        m_token = token;
        m_date = date;
    }

    /**
     * Gets the message
     *
     * @return the message
     */
    public String getMessage()
    {
        return m_message;
    }

    /**
     * Gets the user token
     *
     * @return the user token
     */
    public String getToken()
    {
        return m_token;
    }

    /**
     * Gets the date
     *
     * @return the date
     */
    public Date getDate()
    {
        return (Date) m_date.clone();
    }

    private static void createSampleAuthBean(JSONValue val, RESTObjectCallBack<SampleAuthBean> callback)
    {
        JSONObject info = val.isObject();
        if (info == null) {
            MessageUtil.showError("An error occurred trying to get sample data.");
            return;
        }

        Date date = null;
        try {
            String dateString = info.get("date").isString().stringValue();
            date = new Date(Long.parseLong(dateString));
        } catch (Exception e) {
            MessageUtil.showError("Invalid date: " + info.get("date").isString().stringValue());
        }
        g_sampleAuthBean = new SampleAuthBean(info.get("message").isString().stringValue(),
                                              info.get("token").isString().stringValue(),
                                              date);
        callback.success(g_sampleAuthBean);
    }

    /**
     * Gets the sample auth data from the server
     *
     * @param callback the REST callback
     */
    public static void getSampleAuthData(final RESTObjectCallBack<SampleAuthBean> callback)
    {
        RESTility.callREST("authdata", new RESTCallback() {
                @Override
                public void onSuccess(JSONValue val)
                {
                    createSampleAuthBean(val, callback);
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
