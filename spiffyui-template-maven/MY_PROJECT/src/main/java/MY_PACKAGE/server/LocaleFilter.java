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
package MY_PACKAGE.server;

import org.spiffyui.server.filter.GWTLocaleBundleFilter;

/**
 * <p>This is a sample locale filter using the Spiffy UI locale filter. </p> 
 *  
 * <p>This filter finds the supported locales in the application using the 
 * list of properties files and adds the GWT locale property to the source 
 * of the HTML page to make sure that the client and server agree on the same 
 * locale.</p> 
 *  
 *  
 */
public class LocaleFilter extends GWTLocaleBundleFilter
{
    @Override
    protected String getResourcePath()
    {
        return "/WEB-INF/classes/MY_FILE_PATH/client";
    }
}

