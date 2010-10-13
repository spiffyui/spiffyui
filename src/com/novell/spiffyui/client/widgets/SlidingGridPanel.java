package com.novell.spiffyui.client.widgets;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * A Panel that uses the slidegrid.js for layout
 */
public class SlidingGridPanel extends ComplexPanel
{

    /**
     * 
     * @param id
     */
    public SlidingGridPanel(String id)
    {
        super();
        Element divEle = DOM.createDiv();
        setElement(divEle);
        setStyleName("slidegrid");
        getElement().setId(id);
    }

    @Override
    public void add(Widget w)
    {
        addWidget(w, null);
    }
    
    /**
     * 
     * @param w
     */
    public void addTall(Widget w)
    {
        addWidget(w, "tallcell");
    }
    
    /**
     * 
     * @param w
     */
    public void addWide(Widget w)
    {
        addWidget(w, "widecell");
    }
    
    /**
     * 
     * @param w
     * @param style
     */
    private void addWidget(Widget w, String style)
    {
        SimplePanel div = new SimplePanel();
        div.addStyleName("cell");
        if (style != null) {
            div.addStyleName(style);
        }
        div.add(w);
        add(div, getElement());
    }
    
    @Override
    public void onAttach()
    {
        alignGrid();
        super.onAttach();
    }

    private static native void alignGrid() /*-{
        $wnd.alignGrid(250, 150, 30);
    }-*/;
}
