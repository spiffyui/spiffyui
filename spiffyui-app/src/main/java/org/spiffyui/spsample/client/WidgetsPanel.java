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
package org.spiffyui.spsample.client;

import org.spiffyui.client.MessageUtil;
import org.spiffyui.client.widgets.DatePickerTextBox;
import org.spiffyui.client.widgets.ExpandingTextArea;
import org.spiffyui.client.widgets.LongMessage;
import org.spiffyui.client.widgets.ProgressBar;
import org.spiffyui.client.widgets.SlideDownPrefsPanel;
import org.spiffyui.client.widgets.SlidingGridPanel;
import org.spiffyui.client.widgets.SmallLoadingIndicator;
import org.spiffyui.client.widgets.StatusIndicator;
import org.spiffyui.client.widgets.TimePickerTextBox;
import org.spiffyui.client.widgets.Tooltip;
import org.spiffyui.client.widgets.button.FancySaveButton;
import org.spiffyui.client.widgets.button.RefreshAnchor;
import org.spiffyui.client.widgets.button.SimpleButton;
import org.spiffyui.client.widgets.dialog.ConfirmDialog;
import org.spiffyui.client.widgets.dialog.Dialog;
import org.spiffyui.client.widgets.multivaluesuggest.MultivalueSuggestRESTHelper;
import org.spiffyui.client.widgets.slider.RangeSlider;
import org.spiffyui.spsample.client.widgets.FancyAutocompleter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This is the widgets sample panel
 *
 */
public class WidgetsPanel extends HTMLPanel implements CloseHandler<PopupPanel>
{
    private static final SPSampleStrings STRINGS = (SPSampleStrings) GWT.create(SPSampleStrings.class);
    
    private ConfirmDialog m_dlg;
    private RefreshAnchor m_refresh;
    private SlidingGridPanel m_slideGridPanel;
    
    private static final int TALL = 1;
    private static final int WIDE = 2;
    private static final int BIG = 3;
    
    /**
     * Creates a new panel
     */
    public WidgetsPanel()
    {
        super("div", 
             "<div id=\"WidgetsPrefsPanel\"></div>" + 
             STRINGS.WidgetsPanel_html() + 
             "<div id=\"WidgetsLongMessage\"></div><br /><br />" + 
             "<div id=\"WidgetsSlidingGrid\"></div>" +           
             "</div>");
        
        getElement().setId("WidgetsPanel");
        
        //Create the sliding grid and set it up for the rest of the controls
        m_slideGridPanel = new SlidingGridPanel();
        m_slideGridPanel.setCellHeight(264);
        m_slideGridPanel.setCellWidth(302);
        m_slideGridPanel.setPadding(36);
        setVisible(false);
        
        RootPanel.get("mainContent").add(this);
        
        addLongMessage();
        
        addNavPanelInfo();
        
        /*
         * Add widgets to the sliding grid in alphabetical order
         */

        addDatePicker();
        
        addExpandingTextArea();
        
        addFancyButton();

        addMessageButton();
        
        addLoginPanel();
        
        addMultiValueAutoComplete();
        
        addOptionsPanel();
        
        addProgressBar();  
        
        addConfirmDialog();
        
        addRefreshAnchor();
        
        addSimpleButton();
        
        addSlider();
        
        addSlidingGrid();
        
        addSmallLoadingIndicator();
        
        addStatusIndicators();
        
        addTimePicker();
        
        addTooltip();
        
        /*
         * Add the sliding grid here.  This call must go last so that the onAttach of the SlidingGridPanel can do its thing.
         */
        add(m_slideGridPanel, "WidgetsSlidingGrid");

        Index.addToc(this, "h3");
                
    }

    /**
     * Create a slider and add it to the sliding grid
     */
    private void addSlider()
    {
        /*
         * Add a range slider
         */
        addToSlidingGrid(new RangeSlider("rangeslider", 0, 500, 200, 300), "WidgetsRangeSlider", Index.getStrings().slider(),
                         STRINGS.Slider_html(), WIDE);
    }

    /**
     * Create the time picker control and add it to the sliding grid
     */
    private void addTimePicker()
    {
        /*
         * Add the time picker
         */
        addToSlidingGrid(new TimePickerTextBox("timepicker"), "WidgetsTimePicker", Index.getStrings().timePicker(),
                         STRINGS.TimePicker_html());
    }
    
    /**
     * Create the tooltip and add it to the sliding grid
     */
    private void addTooltip()
    {
        /*
         * Create a tooltip with a simple body.
         * Add an anchor that will show the tooltip.
         */
        final Tooltip tooltip = new Tooltip();
        tooltip.setBody(new HTML(Index.getStrings().tooltipBody()));
        
        final Anchor anchor = new Anchor(Index.getStrings().showTooltip());        
        final Timer showTooltip = new Timer() {
            
            @Override
            public void run()
            {
                tooltip.showRelativeTo(anchor);
            }
        };
        anchor.addMouseOverHandler(new MouseOverHandler() {
            
            @Override
            public void onMouseOver(MouseOverEvent event)
            {
                /*
                 * Show the tooltip after a delay
                 */
                showTooltip.schedule(tooltip.getAutoCloseTime());
            }
        });
        anchor.addMouseOutHandler(new MouseOutHandler() {

            @Override
            public void onMouseOut(MouseOutEvent event)
            {
                if (tooltip.isShowing()) {
                    /*
                     * Autoclose the tooltip after a delay
                     */
                    tooltip.startAutoCloseTimer();
                } else {
                    /*
                     * Cancel the delay to show the tooltip
                     */
                    showTooltip.cancel();
                }
            }
        });
        addToSlidingGrid(anchor, "WidgetsTooltip", Index.getStrings().tooltip(),
                         STRINGS.Tooltip_html());
    }
    
    /**
     * Create the status indicators and add them to the sliding grid
     */
    private void addStatusIndicators()
    {
        /*
         * Add 3 status indicators 
         */
        StatusIndicator status1 = new StatusIndicator(StatusIndicator.IN_PROGRESS);
        StatusIndicator status2 = new StatusIndicator(StatusIndicator.SUCCEEDED);
        StatusIndicator status3 = new StatusIndicator(StatusIndicator.FAILED);
        HTMLPanel statusPanel = addToSlidingGrid(status1, "WidgetsStatus", Index.getStrings().statusIndicator(), 
                                                 STRINGS.Status_html());
        statusPanel.add(status2, "WidgetsStatus");
        statusPanel.add(status3, "WidgetsStatus");        
    }
    
    /**
     *  Create the small loading indicator and add it to the sliding grid
     */
    private void addSmallLoadingIndicator()
    {
        /*
         * Add a small loading indicator to our page
         */
        SmallLoadingIndicator loading = new SmallLoadingIndicator();
        addToSlidingGrid(loading, "WidgetsSmallLoading", Index.getStrings().smallLoadingIndicator(), 
                         STRINGS.SmallLoading_html());  
    }
    
    /**
     * Create the refresh anchor and add it to the sliding grid
     */
    private void addRefreshAnchor()
    {
        /*
         * Add a refresh anchor to our page
         */
        m_refresh = new RefreshAnchor("Widgets_refreshAnchor");
        HTMLPanel p = addToSlidingGrid(m_refresh, "WidgetsRefreshAnchor", Index.getStrings().refreshAnchor(), STRINGS.RefreshAnchor_html(), TALL);
        Element h3 = p.getElementById("WID_WidgetsRefreshAnchor");
        //Set the title so that the TOC's list item for this isn't too long
        h3.setTitle(Index.getStrings().refreshAnchorConfirmDialog_toc());
        
        m_refresh.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) 
            {
                m_refresh.setLoading(true);
                m_dlg.center();
                m_dlg.show();
                event.preventDefault();
            }
            
        });
    }
    
    /**
     * Create the confirm dialog and add it to the sliding grid
     */
    private void addConfirmDialog()
    {
        /*
         * Add the ConfirmDialog which will show up when refresh is clicked
         */
        m_dlg = new ConfirmDialog("WidgetsConfirmDlg", Index.getStrings().sampleConfirm());
        m_dlg.hide();
        m_dlg.addCloseHandler(this);
        m_dlg.setText(Index.getStrings().refreshSure());
        m_dlg.addButton("btn1", Index.getStrings().proceed(), "OK");
        m_dlg.addButton("btn2", Index.getStrings().cancel(), "CANCEL");
    }
    
    /**
     * Create the progress bar and add it to the sliding grid
     */
    private void addProgressBar()
    {
        /*
         * Add a progress bar to our page
         */
        ProgressBar bar = new ProgressBar("WidgetsPanelProgressBar");
        bar.setValue(65);
        addToSlidingGrid(bar, "WidgetsProgressSpan", Index.getStrings().progressBar(), STRINGS.ProgressBar_html());
    }
    
    /**
     * Create the options panel and add it to the sliding grid
     */
    private void addOptionsPanel()
    {
        /*
         * Add the options slide down panel
         */
        SlideDownPrefsPanel prefsPanel = new SlideDownPrefsPanel("WidgetsPrefs", Index.getStrings().slideDown());
        add(prefsPanel, "WidgetsPrefsPanel");
        FlowPanel prefContents = new FlowPanel();
        prefContents.add(new Label(Index.getStrings().addDisplay()));
        prefsPanel.setPanel(prefContents);
        addToSlidingGrid(null, "WidgetsDisplayOptions", Index.getStrings().osdp(), STRINGS.SlideDown_html());
    }
    
    /**
     * Create the mutli-value auto-complete field and add it to the sliding grid
     */
    private void addMultiValueAutoComplete()
    {
        /*
         * Add the multivalue suggest box
         */
        final FancyAutocompleter msb = new FancyAutocompleter(new MultivalueSuggestRESTHelper("TotalSize", "Options", "DisplayName", "Value") {
            
            @Override
            public String buildUrl(String q, int indexFrom, int indexTo)
            {
                return "multivaluesuggestboxexample/colors?q=" + URL.encodeQueryString(q) + "&indexFrom=" + indexFrom + "&indexTo=" + indexTo;
            }
        }, true);
        msb.getFeedback().addStyleName("msg-feedback");
        msb.setPageSize(8); //since each value takes up more space, let's cut the size.
        
        String spanId = "WidgetsSuggestBox";
        HTMLPanel panel = addToSlidingGrid(msb, spanId, Index.getStrings().mvsp(), STRINGS.MultiVal_html(), BIG);
        /*
         * so that spellcheck is not done on a selected crayon color of "Screamin' Green"
         */
        Element span = panel.getElementById(spanId);
        span.setAttribute("spellcheck", "false");
        
//        /*
//         * temp to show how to get values and how to
//         * have a default value of Electric Lime
//         */
//        Map<String, String> valueMap = new HashMap<String, String>();
//        valueMap.put("Electric Lime", "#CEFF1D");
//        msb.setValueMap(valueMap);
//        
//        final Button b = new Button("get values");
//        panel.add(b);
//        b.addClickHandler(new ClickHandler() {
//            
//            @Override
//            public void onClick(ClickEvent event)
//            {
//                Map<String, String> values = msb.getValueMap();
//                ConfirmDialog c = new ConfirmDialog("mvsb-results-dialog", "Results");
//                StringBuffer sb = new StringBuffer();
//                for (String key : values.keySet()) {
//                    sb.append("<div style=\"background-color:" + values.get(key) + "\">" + key + "</div>");
//                }
//                c.replaceDialogBodyContents(new HTML(sb.toString()));
//                c.addButton("mvsb-dialog-ok", "Close", "OK");
//                c.showRelativeTo(b);
//            }
//        });
//        /*
//         * end temp
//         */
        
    }
    
    /**
     * Create the date picker control and add it to the sliding grid
     */
    private void addDatePicker()
    {
        /*
         * Add the date picker
         */
        addToSlidingGrid(new DatePickerTextBox("datepicker"), "WidgetsDatePicker", Index.getStrings().datePicker(), 
                         STRINGS.DatePicker_html(), TALL);
    }
    
    private void addNavPanelInfo()
    {
        Anchor css = new Anchor(Index.getStrings().cssPage(), "CSSPanel");
        css.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event)
            {
                event.preventDefault();
                Index.selectItem(Index.CSS_NAV_ITEM_ID);
            }
        });
        
        HTMLPanel panel = addToSlidingGrid(null, "NavPanelGridCell", Index.getStrings().navBar(), STRINGS.NavBar_html(), TALL);

        panel.add(css, "cssPageWidgetsLink");
    }
    
    /**
     * Create the sliding grid
     */
    private void addSlidingGrid()
    {
        /*
         * Create the sliding grid and add its big cell
         */
        addToSlidingGrid(null, "WidgetsSlidingGridCell", Index.getStrings().sgp(), STRINGS.SlideGrid_html());
    }
    
    /**
     * Create the long message control to the top of the page
     */
    private void addLongMessage()
    {
        /*
         * Add a long message to our page
         */
        LongMessage message = new LongMessage("WidgetsLongMessageWidget");
        add(message, "WidgetsLongMessage");
        message.setHTML(STRINGS.LongMessage_html());
    }
    
    /**
     * Create the simple button control and add it to the sliding grid
     */
    private void addSimpleButton()
    {
        /*
         * Add the simple button
         */
        final SimpleButton simple = new SimpleButton(Index.getStrings().simpleButton());
        simple.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event)
            {
                simple.setInProgress(true);
                //a little timer to simulate time it takes to set loading back to false
                Timer t = new Timer() {

                    @Override
                    public void run()
                    {
                        simple.setInProgress(false);
                    }
                    
                };
                t.schedule(2000);
            }
        });
        addToSlidingGrid(simple, "WidgetsButton", Index.getStrings().simpleButton(), STRINGS.SimpleButton_html());
    }
    
    /**
     * Create the fancy button control and add it to the sliding grid
     */
    private void addFancyButton()
    {
        /*
         * Add the fancy button
         */
        final FancySaveButton fancy = new FancySaveButton(Index.getStrings().save());
        fancy.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event)
            {
                fancy.setInProgress(true);
                //a little timer to simulate time it takes to set loading back to false
                Timer t = new Timer() {

                    @Override
                    public void run()
                    {
                        fancy.setInProgress(false);
                    }

                };
                t.schedule(2000);
            }
        });
        addToSlidingGrid(fancy, "WidgetsFancyButton", Index.getStrings().fsb(), STRINGS.FancyButton_html());
    }

    /**
     * Create login panel and add it to the sliding grid
     */
    private void addLoginPanel()
    {
       String buttonText = "";
        if (Index.userLoggedIn()) {
            buttonText = Index.getStrings().secData();
        } else {
            buttonText = Index.getStrings().getSecData();
        }
        final SimpleButton doLoginButton = new SimpleButton(buttonText);
        
        if (Index.isAppEngine()) {
            doLoginButton.setText(Index.getStrings().installMessage());
            doLoginButton.setEnabled(false);
        }

        doLoginButton.getElement().setId("doLoginBtn");

        doLoginButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event)
            {
                doLoginButton.setInProgress(true);
                //a little timer to simulate time it takes to set in progress back to false
                Timer t = new Timer() {
                    @Override
                    public void run()
                    {
                        doLoginButton.setInProgress(false);
                        AuthPanel.getData(true);
                    }

                };
                t.schedule(1000);
            }
        });

        HTMLPanel loginButtonPanel = addToSlidingGrid(doLoginButton, "WidgetsLoginPanel", Index.getStrings().loginPanel(),
                                                      STRINGS.LoginWidget_html(), TALL);
        loginButtonPanel.add((new HTML("<p><div id=\"loginResult\"></div>")), "WidgetsLoginPanel");

        Anchor auth = new Anchor(Index.getStrings().authPage(), "AuthPanel");
        auth.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event)
            {
                event.preventDefault();
                Index.selectItem(Index.AUTH_NAV_ITEM_ID);
            }
        });
        loginButtonPanel.add(auth, "authPanelSpan");
    }

    
    /**
     * A helper method that adds a widget and some HTML description to the sliding
     * grid panel
     * 
     * @param widget   the widget to add
     * @param id       the ID of the new cell
     * @param title    the title of the new cell
     * @param htmlText the HTML description of the widget
     * 
     * @return the HTMLPanel used to add the contents to the new cell
     */
    private HTMLPanel addToSlidingGrid(Widget widget, String id, String title, String htmlText)
    {
        return addToSlidingGrid(widget, id, title, htmlText, 0);
    }
    
    /**
     * A helper method that adds a widget and some HTML description to the sliding
     * grid panel
     * 
     * @param widget   the widget to add
     * @param id       the ID of the new cell
     * @param title    the title of the new cell
     * @param htmlText the HTML description of the widget
     * @param type     the type of cell to add:  TALL, BIG, or WIDE
     * 
     * @return the HTMLPanel used to add the contents to the new cell
     */
    private HTMLPanel addToSlidingGrid(Widget widget, String id, String title, String htmlText, int type)
    {       
        HTMLPanel p = new HTMLPanel("div", 
            "<h3 id=\"WID_" + id + "\">" + title + "</h3>" + 
            htmlText + 
            "<span id=\"" + id + "\"></span>");
     
        if (widget != null) {
            p.add(widget, id);
        }
        
        switch (type) {
            case WIDE:
                m_slideGridPanel.addWide(p);
                break;
            case TALL:
                m_slideGridPanel.addTall(p);
                break;
            case BIG:
                m_slideGridPanel.addBig(p);
                break;
            default:
                m_slideGridPanel.add(p);
                break;
        }
        return p;
    }

    /**
     * Add the message buttons to the sliding grid
     */
    private void addMessageButton()
    {        
        /*
         * Add the message buttons
         */
        Button b = new Button(Index.getStrings().showInfoMessage());
        HTMLPanel p = addToSlidingGrid(b, "WidgetsMessages", Index.getStrings().humanMsg(), STRINGS.HumanMsg_html(), TALL);
        
        b.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) 
            {
                MessageUtil.showMessage(Index.getStrings().infoMsg());
            }
            
        });
        
        b = new Button(Index.getStrings().showWrnMsg());
        p.add(b, "WidgetsMessages");
        b.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) 
            {
                MessageUtil.showWarning(Index.getStrings().wrnMsg(), false);
            }
            
        });
        
        b = new Button(Index.getStrings().showErrMsg());
        p.add(b, "WidgetsMessages");
        b.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) 
            {
                MessageUtil.showError(Index.getStrings().errMsg());
            }
            
        });
        
        b = new Button(Index.getStrings().showFatalErrMsg());
        p.add(b, "WidgetsMessages");
        b.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) 
            {
                MessageUtil.showFatalError(Index.getStrings().fatalErrMsg());
            }
            
        });

        b = new Button(Index.getStrings().showPageWarningMsg());
        p.add(b, "WidgetsMessages");
        b.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) 
            {
                MessageUtil.showPageLevelWarning(Index.getStrings().pageWarningMsg());
            }
            
        });
        
    }

    @Override
    public void onClose(CloseEvent<PopupPanel> event)
    {
        Dialog dlg = (Dialog) event.getSource();
        String btn = dlg.getButtonClicked();
        if (dlg == m_dlg && "OK".equals(btn)) {
            MessageUtil.showMessage(Index.getStrings().refreshing());
            //a little timer to simulate time it takes to set loading back to false
            Timer t = new Timer() {

                @Override
                public void run()
                {
                    m_refresh.setLoading(false);
                }
                
            };
            t.schedule(2000);
        } else {
            m_refresh.setLoading(false);
        }
    }

    /**
     * @return Returns the slideGridPanel.
     */
    public SlidingGridPanel getSlideGridPanel()
    {
        return m_slideGridPanel;
    }    
    
    private void addExpandingTextArea()
    {
        ExpandingTextArea eta = new ExpandingTextArea("expandingTextArea");
        eta.setText(Index.getStrings().expandingTextAreaDefault());
        addToSlidingGrid(eta, "WidgetsExpandingTextArea", Index.getStrings().expandingTextArea(), 
                         STRINGS.ExpandingTextArea_html(), TALL);
    }
}
