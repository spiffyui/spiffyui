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
package org.spiffyui.client.rest;

/**
 * RESTility and the AuthUtil try to make login and authentication 
 * transparent to the calling code.  Most of the time this works well. 
 * However, when we upload large files (like importing reports) we 
 * can't make that call through RESTility and we need to know that a 
 * login happened.  This listener provides a mechanism to let us know 
 * that happened. 
 */
public interface RESTLoginCallBack
{
    /**
     * Indicates that a login prompt happened.  This is 
     * separate from submitting the login or getting authenticated. 
     */
    void loginPrompt();

    /**
     * Called when a login is successful
     *
     */
    void onLoginSuccess();
}
