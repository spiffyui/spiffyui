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
