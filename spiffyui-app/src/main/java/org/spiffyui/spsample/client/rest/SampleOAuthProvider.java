/*******************************************************************************
 *
 * Copyright 2012 Spiffy UI Team   
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

import com.google.gwt.http.client.Response;

import org.spiffyui.client.rest.RESTCallback;
import org.spiffyui.client.rest.RESTException;

import org.spiffyui.client.rest.v2.RESTOAuthProvider;

/**
 * This is a sample OAuth information provider
 */
public class SampleOAuthProvider implements RESTOAuthProvider
{
    @Override
    public String getAuthServerUrl(RESTCallback callback, String tokenServerUrl, Response response, RESTException exception)
    {
        return "/oauthserver";
    }

    @Override
    public String getClientId()
    {
        return null;
    }

    @Override
    public String getScope()
    {
        return null;
    }
}
