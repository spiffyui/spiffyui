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

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
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
public class ExpandingTextArea extends TextArea implements ChangeHandler, KeyUpHandler, BlurHandler
{
    private JavaScriptObject m_span;
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
        super.setValue(value);
        if (m_span != null) {
            manuallyUpdateSpanJS(m_span, value);
        }
    }

    @Override
    public void setValue(String value, boolean fireEvents)
    {
        super.setValue(value, fireEvents);
        if (m_span != null) {
            manuallyUpdateSpanJS(m_span, value);
        }
    }

    @Override
    public void setText(String text)
    {
        super.setText(text);
        if (m_span != null) {
            manuallyUpdateSpanJS(m_span, text);
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
         * let's add the the other DOM elements.
         * 
         * Doing it this way instead of extending Composite
         * or HTMLPanel allows us to inherit from TextArea.
         */
        m_span = markupTextAreaJS(getElement().getId());
        addChangeHandler(this);
        addKeyUpHandler(this);
        addBlurHandler(this);
        updateSpan();
    }
    

    private native JavaScriptObject markupTextAreaJS(String textAreaId) /*-{
        var ta = $wnd.$("#" + textAreaId);    
        var prev = ta.prev();
        var contId = textAreaId + "Cont";
        var container = $wnd.$("<div id=\"" + contId + "\" class=\"expandingArea active\"></div>");
        if (prev.length == 0) {
            //no previous siblings to come after, just wrap
            ta.wrap(container);
        } else {
            //make next sibling of prev the container
            prev.after(container);
            container.append(ta);
        }
        ta.before("<pre><span></span><br></pre>");
        //Return the span as a JQuery object
        return $wnd.$("#" + contId + " span");      
    }-*/;
        
    private native void manuallyUpdateSpanJS(JavaScriptObject spanJQueryObject, String text) /*-{
        //We are using JQuery for this because trying to wrap a span element in GWT has a history of not working in Dev Mode 
        if (spanJQueryObject.length > 0) {
            spanJQueryObject.html(text);
        }
    
    }-*/;

    private void updateSpan()
    {
        if (m_span != null) {
            manuallyUpdateSpanJS(m_span, getValue());
        }
    }
    
    @Override
    public void onChange(ChangeEvent event)
    {
        updateSpan();
    }

    @Override
    public void onBlur(BlurEvent event)
    {
        updateSpan();
    }

    @Override
    public void onKeyUp(KeyUpEvent event)
    {
        updateSpan();
    }
}
