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
package org.spiffyui.client;

/**
 * <p> 
 * The JSEffectCallback is used in conjunction with long running JavaScript effect functions
 * from JSUtil. 
 * </p> 
 *  
 * <p> 
 * Many JavaScript effects are long running and program control will return to the calling 
 * code before the effect is complete.  This callback gives the calling code the option to 
 * respond to the completion of the effect. 
 * </p> 
 */
public interface JSEffectCallback
{
    /**
     * Called when the effect is completed.
     * 
     * @param id     the id of the element the effect was called on
     */
    void effectComplete(String id);
}
