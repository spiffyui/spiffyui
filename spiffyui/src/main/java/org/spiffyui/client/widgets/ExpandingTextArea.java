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
package org.spiffyui.client.widgets;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;

/**
 * This is a self-expanding text area.  It's height grows
 * dynamically depending on the text within it.
 * 
 * See http://www.alistapart.com/articles/expanding-text-areas-made-elegant
 * 
 * 
 */
public class ExpandingTextArea extends TextArea
{
    private String m_areaId;
    /**
     * Constructor
     * @param id - the text area's element ID
     */
    public ExpandingTextArea(String id)
    {
        super();        
        getElement().setId(id);
    }

    /**
     * Constructor
     * A unique ID will be generated.
     */
    public ExpandingTextArea()
    {
        this(HTMLPanel.createUniqueId());
    }

    @Override
    public void setValue(String value)
    {
        setText(value);
    }

    @Override
    public void setText(String text)
    {
        super.setText(text);
        if (m_areaId != null) {
            manuallyUpdatePreJS(m_areaId);
        }
    }
    
    @Override
    protected void onLoad()
    {
        super.onLoad();
        /*
         * This is the markup we want
         * <div class="expandingArea">
         *     <pre><span></span><br></pre>
         *     <textarea></textarea>
         * </div>
         *
         * So when this TextArea is attached
         * let's add the the other DOM elements
         */
        m_areaId = markupTextAreaJS(getElement().getId());
        makeExpandingAreaJS(m_areaId);
    }
    
    private native String markupTextAreaJS(String textAreaId) /*-{
        var areaId = textAreaId + "Cont";
        var ta = $wnd.$("#" + textAreaId);    
        var prev = ta.prev();
        var container = $wnd.$("<div id=\"" + areaId + "\" class=\"expandingArea\"></div>");
        if (prev.length == 0) {
            //no previous siblings to come after, just wrap
            ta.wrap(container);
        } else {
            //make next sibling of prev the container
            prev.after(container);
            container.append(ta);
        }
        ta.before("<pre><span></span><br></pre>");
        return areaId;      
    }-*/;
    
    private native void makeExpandingAreaJS(String contId) /*-{
        var areas = $wnd.document.querySelectorAll("#" + contId);
        var container = areas[0];
    
        if (container) {
            var area = container.querySelector('textarea');
            var span = container.querySelector('span');
            if (area.addEventListener) {
                area.addEventListener('input', function() {
                    span.textContent = area.value;
                }, false);
                span.textContent = area.value;
            } else if (area.attachEvent) {
                // IE8 compatibility
                area.attachEvent('onpropertychange', function() {
                    span.innerText = area.value;
                });
                span.innerText = area.value;
            }
            // Enable extra CSS
            container.className += ' active';    
        }
    }-*/;
    
    private native void manuallyUpdatePreJS(String contId) /*-{
        var areas = $wnd.document.querySelectorAll("#" + contId);
        var container = areas[0];
    
        if (container) {
            var area = container.querySelector('textarea');
            var span = container.querySelector('span');
            if (area.addEventListener) {
                span.textContent = area.value;
            } else if (area.attachEvent) {
                // IE8 compatibility
                span.innerText = area.value;
            }
        }
    }-*/;
}
