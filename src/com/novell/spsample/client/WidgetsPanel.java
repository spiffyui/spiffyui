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
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.novell.spiffyui.client.MessageUtil;
import com.novell.spiffyui.client.widgets.DatePickerTextBox;
import com.novell.spiffyui.client.widgets.LongMessage;
import com.novell.spiffyui.client.widgets.ProgressBar;
import com.novell.spiffyui.client.widgets.SlideDownPrefsPanel;
import com.novell.spiffyui.client.widgets.SlidingGridPanel;
import com.novell.spiffyui.client.widgets.SmallLoadingIndicator;
import com.novell.spiffyui.client.widgets.StatusIndicator;
import com.novell.spiffyui.client.widgets.TimePickerTextBox;
import com.novell.spiffyui.client.widgets.button.FancySaveButton;
import com.novell.spiffyui.client.widgets.button.RefreshAnchor;
import com.novell.spiffyui.client.widgets.button.SimpleButton;
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
             "<div id=\"WidgetsPrefsPanel\"></div><h1>Some Sample Widgets</h1><br /><br />" + 
             "<div id=\"WidgetsLongMessage\"></div><br /><br />" + 
             "<div id=\"WidgetsSlidingGrid\"></div>" +           
             "</div>");
        
        getElement().setId("WidgetsPanel");
        
        RootPanel.get("mainContent").add(this);

        setVisible(false);

        /*
         * Add a long message to our page
         */
        LongMessage message = new LongMessage("WidgetsLongMessageWidget");
        add(message, "WidgetsLongMessage");
        message.setHTML("<b>Long Message</b><br />" + 
                             "Long messages are useful for showing information messages " +
                             "with more content than the standard messages but they are still " +
                             "transient messages.");
        
        /*
         * Create the sliding grid and add its big cell
         */
        m_slideGridPanel = new SlidingGridPanel();
        m_slideGridPanel.setGridOffset(175);
        addToSlidingGrid(null, "WidgetsSlidingGridCell", "Sliding Grid Panel",
            "<p>" +  
                "All the cells here are layed out using the sliding grid panel. This panel is a wrapper for slidegrid.js, " + 
                "which automatically moves cells to fit nicely on the screen for any browser window size." + 
            "</p>" + 
            "<p>" + 
                "Resize your browser window to see it in action." +
            "</p>" + 
            "<p>" + 
                "More information on the sliding grid can be found in <a href=\"http://www.zackgrossbart.com/hackito/slidegrid/\">" + 
                "Create Your Own Sliding Resizable Grid</a>." + 
            "</p>", TALL);
        /*
         * Add widgets to the sliding grid in alphabetical order
         */

        /*
         * Add the date picker
         */
        addToSlidingGrid(new DatePickerTextBox("datepicker"), "WidgetsDatePicker", "Date Picker",
            "<p>" + 
                "Spiffy UI's date picker shows a calendar for easy date selection.  It wraps the <a href=\"http://jqueryui.com/demos/datepicker\">" + 
                "JQuery UI Date Picker</a> which is better tested, has more features, and is easier to style than the GWT date " + 
                "picker control.  The JQuery Date Picker includes many features including the ablility to specify the minimum " + 
                "and maximum dates and changing the way to pick months and years." +
            "</p>" + 
            "<p>" +
                "The Spiffy UI Framework is localized into 53 languages.  Try changing your browser locale and refreshing " + 
                "this page. In addition, since it is a GWT widget, you may get the selected date value as a java.util.Date." + 
            "</p>", TALL);
                
        /*
         * Add the fancy button
         */
        final FancySaveButton fancy = new FancySaveButton("Save");
        fancy.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event)
            {
                fancy.setInProgress(true);
                //a little timer to simulate time it takes to set loading back to false
                Timer t = new Timer() {

                    @Override
                    public void run() {
                        fancy.setInProgress(false);
                    }
                    
                };
                t.schedule(2000);
            }
        });
        addToSlidingGrid(fancy, "WidgetsFancyButton", "Fancy Save Button", 
            "<p>" +            
                "Fancy buttons show an image and text with a disabled image and hover style.  Click to demonstrate its in progress status." +
            "</p>");
        
        /*
         *Add the message buttons
         */
        addMessageButton();  
       
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
        addToSlidingGrid(msb, "WidgetsSuggestBox", "Multivalue Suggest Box",
            "<p>" +                        
                "The Multivalue suggest box is an autocompleter that allows for multiple values and browsing. It uses REST to " + 
                "retrieve suggestions from the server.  The full process is documented in " + 
                "<a href=\"http://www.zackgrossbart.com/hackito/gwt-rest-auto\">" +
                "Creating a Multi-Valued Auto-Complete Field Using GWT SuggestBox and REST</a>." +
            "</p>" +        
            "<p>" +              
                "Type blue, mac, or * to search for crayon colors." +
            "</p>",
            WIDE);
        
        /*
         * Add the options slide down panel
         */
        SlideDownPrefsPanel prefsPanel = new SlideDownPrefsPanel("WidgetsPrefs", "Slide Down Prefs Panel");
        add(prefsPanel, "WidgetsPrefsPanel");
        FlowPanel prefContents = new FlowPanel();
        prefContents.add(new Label("Add display option labels and fields and an 'Apply' or 'Save' button."));
        prefsPanel.setPanel(prefContents);
        addToSlidingGrid(null, "WidgetsDisplayOptions", "Options Slide Down Panel",
            "<p>" +              
                "Click the 'Slide Down Prefs Panel' slide-down tab at the top of the page to view an example of this widget." +
            "</p>");

        /*
         * Add a progress bar to our page
         */
        ProgressBar bar = new ProgressBar("WidgetsPanelProgressBar");
        bar.setValue(65);
        addToSlidingGrid(bar, "WidgetsProgressSpan", "Progress Bar", 
            "<p>" +  
                "This progress bar GWT control wraps the " +
                "<a href=\"http://jqueryui.com/demos/progressbar/\">JQuery UI Progressbar</a>." +
            "</p>");        
        
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
        addToSlidingGrid(m_refresh, "WidgetsRefreshAnchor", "Refresh Anchor<br/><br/>Confirm Dialog", 
            "<p>" +             
                "The refresh anchor handles an in progress status for refreshing items with an AJAX request. Click to show its in progress status and " +
                "open an example of a confirm dialog." +
            "</p>" +
            "<p>" +              
                "Dialogs are keyboard accessible and support autohide and modal properties." +
            "</p>",
            TALL);
        
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
         * Add the simple button
         */
        final SimpleButton simple = new SimpleButton("Simple Button");
        simple.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event)
            {
                simple.setInProgress(true);
                //a little timer to simulate time it takes to set loading back to false
                Timer t = new Timer() {

                    @Override
                    public void run() {
                        simple.setInProgress(false);
                    }
                    
                };
                t.schedule(2000);
            }
        });
        addToSlidingGrid(simple, "WidgetsButton", "Simple Button", 
            "<p>" +              
                "All buttons get special styling from the Spiffy UI framework. Click to demonstrate its in progress status." +
            "</p>");

        /*
         * Add a small loading indicator to our page
         */
        SmallLoadingIndicator loading = new SmallLoadingIndicator();
        addToSlidingGrid(loading, "WidgetsSmallLoading", "Small Loading Indicator", "<p>This indicator shows a loading status.</p>");  
        
        /*
         * Add 3 status indicators 
         */
        StatusIndicator status1 = new StatusIndicator(StatusIndicator.IN_PROGRESS);
        StatusIndicator status2 = new StatusIndicator(StatusIndicator.SUCCEEDED);
        StatusIndicator status3 = new StatusIndicator(StatusIndicator.FAILED);
        HTMLPanel statusPanel = addToSlidingGrid(status1, "WidgetsStatus", "Status Indicator", 
            "<p>The status indicator shows valid, failed, and in progress status.  It can be extended for others.</p>");
        statusPanel.add(status2, "WidgetsStatus");
        statusPanel.add(status3, "WidgetsStatus");        
        
        /*
         * Add the time picker
         */
        addToSlidingGrid(new TimePickerTextBox("timepicker"), "WidgetsTimePicker", "Time Picker",
            "<p>" +
                "Time Picker shows a time dropdown for easy selection. It is a wrapper for a " +
                "<a href=\"http://code.google.com/p/jquery-timepicker/\">JQuery time picker</a>. " + 
                "The time step is set to 30 min but can be configured.  It is localized. " + 
                "Try changing your browser locale and refreshing your browser." +
            "</p>");
        
        /*
         * Add the sliding grid here.  This call must go last so that the onAttach of the SlidingGridPanel can do its thing.
         */
        add(m_slideGridPanel, "WidgetsSlidingGrid");
    }
    
    private HTMLPanel addToSlidingGrid(Widget widget, String id, String title, String htmlText)
    {
        return addToSlidingGrid(widget, id, title, htmlText, 0);
    }
    
    private HTMLPanel addToSlidingGrid(Widget widget, String id, String title, String htmlText, int type)
    {
        HTMLPanel p = new HTMLPanel("div", 
            "<h3>" + title + "</h3>" + 
            htmlText + 
            "<span id=\"" + id + "\"></span>");
        
        if (widget != null) {
            p.add(widget, id);
        }
        p.addStyleName("weak");
        
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

    private void addMessageButton()
    {        
        Button b = new Button("Show Info Message");
        HTMLPanel p = addToSlidingGrid(b, "WidgetsMessages", "Humanized Messages", 
            "<p>" +            
                "The Spiffy UI framework support has an integrated message framework following the pattern of " +
                "<a href=\"http://code.google.com/p/humanmsg/\">Humanized Messages</a>.  " + 
                "These messages are non-modal and fade away without requiring further interaction.  They " + 
                "include info messages, warnings, errors, and fatal errors.  Errors and some warnings are sent to an error " + 
                "log at the bottom of the screen." +
            "</p>",
            TALL);
        
        b.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) 
            {
            	MessageUtil.showMessage("This is an information message");
            }
            
        });
        
        b = new Button("Show Warning Message");
        p.add(b, "WidgetsMessages");
        b.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) 
            {
            	MessageUtil.showWarning("This is a warning message", false);
            }
            
        });
        
        b = new Button("Show Error Message");
        p.add(b, "WidgetsMessages");
        b.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) 
            {
            	MessageUtil.showError("This is a error message");
            }
            
        });
        
        b = new Button("Show Fatal Error Message");
        p.add(b, "WidgetsMessages");
        b.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) 
            {
            	MessageUtil.showFatalError("This is a fatal error message");
            }
            
        });
        
        p.addStyleName("weak");
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

}
