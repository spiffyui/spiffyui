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
    private int m_cellWidth = 250;
    private int m_cellHeight = 150;
    private int m_padding = 30;
    private int m_offset = 0;

    /**
     * Constructor
     */
    public SlidingGridPanel()
    {
        super();
        Element divEle = DOM.createDiv();
        setElement(divEle);
        setStyleName("slidegrid");
    }

    @Override
    public void add(Widget w)
    {
        addWidget(w, null);
    }
    
    /**
     * Add a widget to a tall cell
     * @param w Widget
     */
    public void addTall(Widget w)
    {
        addWidget(w, "tallcell");
    }
    
    /**
     * Add a widget to a wide cell
     * @param w Widget
     */
    public void addWide(Widget w)
    {
        addWidget(w, "widecell");
    }
    
    /**
     * Add a widget to a big cell (tall and wide)
     * @param w Widget
     */
    public void addBig(Widget w)
    {
        addWidget(w, "bigcell");
    }
    
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
    
    /**
     * the width of each cell in px
     * @return the cellWidth
     */
    public int getCellWidth()
    {
        return m_cellWidth;
    }

    /**
     * the width of each cell in px
     * @param cellWidth the cellWidth to set
     */
    public void setCellWidth(int cellWidth)
    {
        m_cellWidth = cellWidth;
    }

    /**
     * the height of each cell in px
     * @return the cellHeight
     */
    public int getCellHeight()
    {
        return m_cellHeight;
    }

    /**
     * the height of each cell in px
     * @param cellHeight the cellHeight to set
     */
    public void setCellHeight(int cellHeight)
    {
        m_cellHeight = cellHeight;
    }

    /**
     * the padding between each cell in px
     * @return the padding
     */
    public int getPadding()
    {
        return m_padding;
    }

    /**
     * the padding between each cell in px
     * @param padding the padding to set
     */
    public void setPadding(int padding)
    {
        m_padding = padding;
    }

    /**
     * the offset width of the grid in px
     * @return the offset
     */
    public int getGridOffset()
    {
        return m_offset;
    }

    /**
     * the offset width of the grid in px
     * @param offset the offset to set
     */
    public void setGridOffset(int offset)
    {
        m_offset = offset;
    }

    @Override
    public void onAttach()
    {
        alignGrid(m_cellWidth, m_cellHeight, m_padding, m_offset);
        super.onAttach();
    }

    private static native void alignGrid(int cellWidth, int cellHeight, int padding, int gridOffset) /*-{
        $wnd.slidegrid.alignGrid(cellWidth, cellHeight, padding, gridOffset);
    }-*/;
}
