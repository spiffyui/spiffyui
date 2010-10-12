/*
 * Copyright (c) 2010 Unpublished Work of Novell, Inc. All Rights Reserved.
 *
 * THIS WORK IS AN UNPUBLISHED WORK AND CONTAINS CONFIDENTIAL,
 * PROPRIETARY AND TRADE SECRET INFORMATION OF NOVELL, INC. ACCESS TO
 * THIS WORK IS RESTRICTED TO (I) NOVELL, INC. EMPLOYEES WHO HAVE A NEED
 * TO KNOW HOW TO PERFORM TASKS WITHIN THE SCOPE OF THEIR ASSIGNMENTS AND
 * (II) ENTITIES OTHER THAN NOVELL, INC. WHO HAVE ENTERED INTO
 * APPROPRIATE LICENSE AGREEMENTS. NO PART OF THIS WORK MAY BE USED,
 * PRACTICED, PERFORMED, COPIED, DISTRIBUTED, REVISED, MODIFIED,
 * TRANSLATED, ABRIDGED, CONDENSED, EXPANDED, COLLECTED, COMPILED,
 * LINKED, RECAST, TRANSFORMED OR ADAPTED WITHOUT THE PRIOR WRITTEN
 * CONSENT OF NOVELL, INC. ANY USE OR EXPLOITATION OF THIS WORK WITHOUT
 * AUTHORIZATION COULD SUBJECT THE PERPETRATOR TO CRIMINAL AND CIVIL
 * LIABILITY.
 *
 * ========================================================================
 */
package com.novell.spsample.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;

import com.novell.spiffyui.client.MessageUtil;
import com.novell.spiffyui.client.widgets.DatePickerTextBox;
import com.novell.spiffyui.client.widgets.LongMessage;
import com.novell.spiffyui.client.widgets.ProgressBar;
import com.novell.spiffyui.client.widgets.SmallLoadingIndicator;
import com.novell.spiffyui.client.widgets.StatusIndicator;
import com.novell.spiffyui.client.widgets.TimePickerTextBox;
import com.novell.spiffyui.client.widgets.button.FancySaveButton;
import com.novell.spiffyui.client.widgets.button.RefreshAnchor;
import com.novell.spiffyui.client.widgets.dialog.ConfirmDialog;
import com.novell.spiffyui.client.widgets.dialog.Dialog;
import com.novell.spiffyui.client.widgets.multivaluesuggest.MultivalueSuggestBox;
import com.novell.spiffyui.client.widgets.multivaluesuggest.MultivalueSuggestRESTHelper;

/**
 * This is the page 1 panel
 *
 */
public class WidgetsPanel extends HTMLPanel implements CloseHandler<PopupPanel>
{
	private ConfirmDialog m_dlg;
	private RefreshAnchor m_refresh;
    /**
     * Creates a new panel
     */
    public WidgetsPanel()
    {
        super("div", 
             "<h1>Some example Widgets</h1><br /><br />" + 

             "<div id=\"WidgetsLongMessage\"></div><br /><br />" + 

             "<div class=\"slidegrid\">" + 
                
                 "<div class=\"cell tallcell weak\">" +
                     "<h3>Date Picker</h3>" + 
                     "Date Picker shows a calendar for easy date selection.  It also allows you to type directly into the text field. " + 
                     "Other useful features include being able to specify the minimum and maximum dates allowed and getting the " +
                     "selected date as a java.util.Date." +
                     "It is localized.  Try changing your browser locale and refreshing your browser.<br /><br />" + 
                     "<span id=\"WidgetsDatePicker\"></span>" +
                 "</div>" + 
              
                 "<div class=\"cell weak\">" +
                    "<h3>Fancy Button</h3>" + 
                    "Fancy buttons show an image and text with a disabled image and hover style.  It also supports an in progress " + 
                    "state.<br /><br />" + 
                    "<span id=\"WidgetsFancyButton\"></span>" +
                 "</div>" + 
              
                 "<div class=\"cell tallcell weak\">" +
                    "<h3>Humanized Messages</h3>" + 
                    "The Spiffy UI framework support has an integrated message framework following the pattern of humanized " + 
                    "messages.  These messages are non-modal and fade away without requiring further interaction.  They " + 
                    "include info messages, warnings, errors, and fatal errors.  Errors and some warnings are sent to an error " + 
                    "log at the bottom of the screen.<br /><br />" + 
                    "<span id=\"WidgetsMessages\"></span>" +
                 "</div>" +
              
                 "<div class=\"cell widecell weak\">" +
                    "<h3>Multivalue Suggest Box</h3>" + 
                    "The Multivalue suggest box is an autocompleter that allows for multiple values and browsing. It uses REST to " + 
                    "retrieve suggestions from the server.  The full process is documented " + 
                    "<a href=\"http://www.zackgrossbart.com/hackito/gwt-rest-auto\">here</a>. <br /><br />" + 
                    "Type blue, mac, or *.<br /><br />" + 
                    "<span id=\"WidgetsSuggestBox\"></span>" +
                 "</div>" + 

                 "<div class=\"cell weak\">" + 
                    "<h3>Progress bar</h3>" + 
                    "This progress bar GWT control wraps the progress bar control from JQuery UI.<br /><br />" + 
                    "<span id=\"WidgetsProgressSpan\"></span>" + 
                 "</div>" +
              
                 "<div class=\"cell weak\">" +
                    "<h3>Refresh anchor</h3>" + 
                    "The refresh anchor handles an in progress status for refreshing items with an AJAX request.<br /><br />" + 
                    "This one will show an example of a confirm dialog<br /><br />" + 
                    "<span id=\"WidgetsRefreshAnchor\"></span>" +
                 "</div>" +
              
                 "<div class=\"cell weak\">" +
                    "<h3>Simple Button</h3>" + 
                    "All buttons get special styling from the Spiffy UI framework.<br /><br />" + 
                    "<span id=\"WidgetsButton\"></span>" +
                 "</div>" + 

                 "<div class=\"cell weak\">" +
                    "<h3>Small loading indicator</h3>" + 
                    "The small loading indicator shows a loading status.<br /><br />" + 
                    "<span id=\"WidgetsSmallLoading\"></span>" + 
                 "</div>" +

                 "<div class=\"cell weak\">" +
                    "<h3>Status Indicator</h3>" + 
                    "The status indicator shows valid, failed, and in progress status.  It can be extended for others.<br /><br />" + 
                    "<span id=\"WidgetsStatus\"></span>" + 
                 "</div>" + 
                 
                 "<div class=\"cell weak\">" +
                      "<h3>Time Picker</h3>" + 
                      "Time Picker shows a time dropdown for easy selection.  It also allows you to type directly into the text field. " + 
                      "The time step is set to 30 min but can be configured.  It is localized. " + 
                      "Try changing your browser locale and refreshing your browser.<br /><br />" + 
                      "<span id=\"WidgetsTimePicker\"></span>" +
                   "</div>" + 
           
             "</div>");
        
        getElement().setId("WidgetsPanel");
        
        RootPanel.get("mainContent").add(this);

        setVisible(false);

        /*
         Add a progress bar to our page
         */
        ProgressBar bar = new ProgressBar("WidgetsPanelProgressBar");
        bar.setValue(65);
        add(bar, "WidgetsProgressSpan");
        
        /*
         Add a long message to our page
         */
        LongMessage message = new LongMessage("WidgetsLongMessageWidget");
        add(message, "WidgetsLongMessage");
        message.setHTML("<b>Long Message</b><br />" + 
                             "Long messages are useful for showing information messages " +
                             "with more content than the standard messages but they are still " +
                             "transient messages.");
        
        /*
         * Add a small loading indicator to our page
         */
        SmallLoadingIndicator loading = new SmallLoadingIndicator();
        add(loading, "WidgetsSmallLoading");
        
        /*
         * Add 3 status indicators 
         */
        StatusIndicator status1 = new StatusIndicator(StatusIndicator.IN_PROGRESS);
        StatusIndicator status2 = new StatusIndicator(StatusIndicator.SUCCEEDED);
        StatusIndicator status3 = new StatusIndicator(StatusIndicator.FAILED);
        add(status1, "WidgetsStatus");
        add(status2, "WidgetsStatus");
        add(status3, "WidgetsStatus");
        
        /*
         * Add the ConfirmDialog which will show up when refresh is clicked
         */
        m_dlg = new ConfirmDialog("WidgetsConfirmDlg", "Sample Confirm");
        m_dlg.hide();
        m_dlg.addCloseHandler(this);
        m_dlg.setText("Are you sure you want to refresh? (Doesn't make much sense as a confirm, but this is just a sample.)");
        m_dlg.addButton("btn1", "Proceed", "OK");
        m_dlg.addButton("btn2", "Cancel", "CANCEL");

        /*
         * Add a refresh anchor to our page
         */
        m_refresh = new RefreshAnchor("Widgets_refreshAnchor");
        add(m_refresh, "WidgetsRefreshAnchor");
        m_refresh.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) 
            {
            	m_refresh.setLoading(true);
                m_dlg.center();
                m_dlg.show();
            }
            
        });
        
        /*
         Add the message buttons
         */
        addMessageButton();
        

        /*
         * Add the simple button
         */
        add(new Button("Simple Button"), "WidgetsButton");

        /*
         * Add the fancy button
         */
        add(new FancySaveButton("Save"), "WidgetsFancyButton");
        
        /*
         * Add the multivalue suggest box
         */
        MultivalueSuggestBox msb = new MultivalueSuggestBox(new MultivalueSuggestRESTHelper("TotalSize", "Options", "DisplayName", "Value") {
            
            @Override
            public String buildUrl(String q, int indexFrom, int indexTo)
            {
                return "multivaluesuggestboxexample/colors?q=" + q + "&indexFrom=" + indexFrom + "&indexTo=" + indexTo;
            }
        }, true);
        msb.getFeedback().addStyleName("msg-feedback");
        add(msb, "WidgetsSuggestBox");
        
        /*
         * Add the date picker
         */
        add(new DatePickerTextBox("datepicker"), "WidgetsDatePicker");
        /*
         * Add the time picker
         */
        add(new TimePickerTextBox("timepicker"), "WidgetsTimePicker");
    }
    
    private void addMessageButton()
    {
        Button b = new Button("Show Info Message");
        add(b, "WidgetsMessages");
        b.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) 
            {
            	MessageUtil.showMessage("This is an information message");
            }
            
        });
        
        b = new Button("Show Warning Message");
        add(b, "WidgetsMessages");
        b.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) 
            {
            	MessageUtil.showWarning("This is a warning message", false);
            }
            
        });
        
        b = new Button("Show Error Message");
        add(b, "WidgetsMessages");
        b.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) 
            {
            	MessageUtil.showError("This is a error message");
            }
            
        });
        
        b = new Button("Show Fatal Error Message");
        add(b, "WidgetsMessages");
        b.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) 
            {
            	MessageUtil.showFatalError("This is a fatal error message");
            }
            
        });
    }

	@Override
	public void onClose(CloseEvent<PopupPanel> event) {
		Dialog dlg = (Dialog) event.getSource();
        String btn = dlg.getButtonClicked();
        if (dlg == m_dlg && "OK".equals(btn)) {
            MessageUtil.showMessage("Refreshing!");
            //a little timer to simulate time it takes to set loading back to false
            Timer t = new Timer() {

				@Override
				public void run() {
					m_refresh.setLoading(false);
				}
            	
            };
            t.schedule(2000);
        } else {
        	m_refresh.setLoading(false);
        }
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
