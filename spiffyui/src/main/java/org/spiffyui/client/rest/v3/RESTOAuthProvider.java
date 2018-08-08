/*******************************************************************************
 * 
 * Copyright 2018 Spiffy UI Team   
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
package org.spiffyui.client.rest.v3;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * <p>
 * This interface allows clients to provide extra information about OAuth authentications.
 * </p>
 * 
 */
public interface RESTOAuthProvider extends org.spiffyui.client.rest.v2.RESTOAuthProvider
{
    
    /**
     * Get any additional parameters (e.g., scope) that must be added to the OAuth
     * authn request.
     * <p>
     * The return is an array of JS objects, each of which must have a "name" member and
     * a "value" member.
     * 
     * @return The parameters, or {@code null}.
     */
    JsArray<JavaScriptObject> getAddlAuthParameters();
}
