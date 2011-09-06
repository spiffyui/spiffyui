/*******************************************************************************
 *
 * Copyright 2011 Spiffy UI Team   
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
package org.spiffyui.spsample.server;

import java.net.MalformedURLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;

import org.spiffyui.server.AuthServlet;
import org.spiffyui.server.AuthURLValidator;


/**
 * This is a sample init listener to add our URL validator which allows access to remote
 * servers.
 */
public class SampleInitListener implements ServletContextListener, AuthURLValidator
{
    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
        AuthServlet.setUrlValidator(this);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {
        /*
         Nothing to do here
         */
    }
    
    @Override
    public boolean validateURI(HttpServletRequest request, String uri)
        throws MalformedURLException
    {
        /*
         This is not very secure, but it helps remote authentication
         server testing.
         */
        return true;
    }
}
