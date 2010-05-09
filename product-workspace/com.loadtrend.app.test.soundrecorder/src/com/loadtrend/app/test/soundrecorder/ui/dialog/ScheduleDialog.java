package com.loadtrend.app.test.soundrecorder.ui.dialog;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.loadtrend.app.test.soundrecorder.info.RecordingScheduleInfo;
import com.loadtrend.app.test.soundrecorder.util.FormatUtil;
import com.loadtrend.app.test.soundrecorder.util.IOOperation;
import com.loadtrend.app.test.soundrecorder.util.PreferenceUtil;

public class ScheduleDialog {
    
    private Shell shell = new Shell(SWT.CLOSE | SWT.SYSTEM_MODAL);
    
    private DateTime startTime = null;
    
    private DateTime endTime = null;
    
    private Button btOnce = null;
    
    private DateTime onceDate = null;
    
    private Button btDaily = null;
    
    private Button btWeekly = null;
    
    private Combo weeks = null;
    
    private Text radioUrl = null;
    
    private Button enable = null;
    
    private List scheduleList = null;
    
    private int status = SWT.CANCEL;
    
    public ScheduleDialog(Shell parentShell, List scheduleList) {
        shell.setText("Recording Schedule");
        shell.setSize(300, 350);
        int x = parentShell.getLocation().x + parentShell.getSize().x / 2 - shell.getSize().x / 2;
        int y = parentShell.getLocation().y + parentShell.getSize().y / 2 - shell.getSize().y / 2;
        shell.setLocation(x, y);
        this.scheduleList = scheduleList;
    }
    
    public int create(final RecordingScheduleInfo info) {
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginHeight = 10;
        gridLayout.marginWidth = 20;
        gridLayout.horizontalSpacing = 10;
        gridLayout.verticalSpacing = 10;
        shell.setLayout(gridLayout);
        
        // Time group
        Group timeGroup = new Group(shell, SWT.SHADOW_IN);
        timeGroup.setText("Time");
        timeGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        timeGroup.setLayout(new GridLayout(2, true));
        Label lbStartTime = new Label(timeGroup, SWT.SHADOW_IN);
        lbStartTime.setText("Start Time:");
        this.startTime = new DateTime(timeGroup, SWT.TIME);
        Label lbEndTime = new Label(timeGroup, SWT.SHADOW_IN);
        lbEndTime.setText("End Time:");
        this.endTime = new DateTime(timeGroup, SWT.TIME);
        
        // Date group
        Group dateGroup = new Group(shell, SWT.SHADOW_IN);
        dateGroup.setText("Date");
        dateGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        dateGroup.setLayout(new GridLayout(2, true));
        this.btOnce = new Button(dateGroup, SWT.RADIO);
        this.btOnce.setText("Once");
        this.onceDate = new DateTime(dateGroup, SWT.DATE);
        this.btDaily = new Button(dateGroup, SWT.RADIO);
        this.btDaily.setText("Daily");
        GridData gridData = new GridData();
        gridData.horizontalSpan = 2;
        this.btDaily.setLayoutData(gridData);
        this.btWeekly = new Button(dateGroup, SWT.RADIO);
        this.btWeekly.setText("Weekly");
        this.weeks = new Combo(dateGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
        this.weeks.add("Sunday");
        this.weeks.add("Monday");
        this.weeks.add("Tuesday");
        this.weeks.add("Wednesday");
        this.weeks.add("Thursday");
        this.weeks.add("Friday");
        this.weeks.add("Saturday");
        
        // Others group
        Group otherGroup = new Group(shell, SWT.SHADOW_IN);
        otherGroup.setText("Optional");
        otherGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        gridLayout = new GridLayout(2, false);
        gridLayout.marginWidth = 5;
        gridLayout.verticalSpacing = 10;
        otherGroup.setLayout(gridLayout);
        Label lbUrl = new Label(otherGroup, SWT.SHADOW_IN);
        lbUrl.setText("Radio Url:");
        this.radioUrl = new Text(otherGroup, SWT.BORDER);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        this.radioUrl.setLayoutData(gridData);
        this.enable = new Button(otherGroup, SWT.CHECK);
        gridData = new GridData();
        gridData.horizontalSpan = 2;
        this.enable.setLayoutData(gridData);
        this.enable.setText("Enable Schedule Immediately");
        this.enable.setSelection(true);
        
        // Initial value setting.
        if (info != null) {
            // Load data from RecordingScheduleInfo to edit.
            int[] hms = FormatUtil.getHoursMinutesSeconds(info.getStartTime());
            startTime.setHours(hms[0]);
            startTime.setMinutes(hms[1]);
            startTime.setSeconds(hms[2]);
            
            hms = FormatUtil.getHoursMinutesSeconds(info.getEndTime());
            endTime.setHours(hms[0]);
            endTime.setMinutes(hms[1]);
            endTime.setSeconds(hms[2]);
            
            if (info.getDate() != null) {
                btOnce.setSelection(true);
                int[] ymd = FormatUtil.getYearMonthDay(info.getDate());
                onceDate.setYear(ymd[0]);
                onceDate.setMonth(ymd[1]);
                onceDate.setDay(ymd[2]);
            } else if (info.isDaily()) {
                btDaily.setSelection(true);
            } else {
                btWeekly.setSelection(true);
            }
            weeks.select(info.getDayOfWeek());
            radioUrl.setText(info.getUrl());
            enable.setSelection(info.isEnable());
        } else {
            // add new RecordingScheduleInfo
            this.btOnce.setSelection(true);
            this.weeks.select(0);
        }
        
        // Ok, Cancel button
        Composite composite = new Composite(shell, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        FillLayout fillLayout = new FillLayout();
        fillLayout.spacing = 10;
        composite.setLayout(fillLayout);
        Button btOk = new Button(composite, SWT.PUSH);
        btOk.setText("Confirm");
        Button btCancel = new Button(composite, SWT.PUSH);
        btCancel.setText("Cancel");
        
        btCancel.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                shell.dispose();
            }
        });
        
        btOk.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                int startSeconds = startTime.getSeconds() + startTime.getMinutes() * 60 + startTime.getHours() * 3600;
                int endSeconds = endTime.getSeconds() + endTime.getMinutes() * 60 + endTime.getHours() * 3600;
                String date = btOnce.getSelection() == false ? null : FormatUtil.getFormatedDate(onceDate.getYear(), onceDate.getMonth(), onceDate.getDay());
                // add schedule or edit schedule
                if (info == null) {
	                RecordingScheduleInfo info = new RecordingScheduleInfo(startSeconds, endSeconds, date,
	                                                                       btDaily.getSelection(), weeks.getSelectionIndex(),
	                                                                       radioUrl.getText(), enable.getSelection());
	                scheduleList.add(0, info);
                } else {
                    info.setStartTime(startSeconds);
                    info.setEndTime(endSeconds);
                    info.setDate(date);
                    info.setDaily(btDaily.getSelection());
                    info.setDayOfWeek(weeks.getSelectionIndex());
                    info.setUrl(radioUrl.getText());
                    info.setEnable(enable.getSelection());
                }
                IOOperation.writeObject(PreferenceUtil.SCHEDULE_DATA, scheduleList);
                status = SWT.OK;
                shell.dispose();
            }
        });
        
        shell.open();
        
        Display display = shell.getDisplay();
        
        while ( !shell.isDisposed() )
        {
            if ( !display.readAndDispatch() )
            {
                display.sleep();
            }
        }
        return status;
    }
}
