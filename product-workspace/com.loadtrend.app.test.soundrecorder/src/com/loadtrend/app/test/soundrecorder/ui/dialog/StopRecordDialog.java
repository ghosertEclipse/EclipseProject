package com.loadtrend.app.test.soundrecorder.ui.dialog;

import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.loadtrend.app.test.soundrecorder.info.RecordingScheduleInfo;
import com.loadtrend.app.test.soundrecorder.util.FormatUtil;

public class StopRecordDialog {
	
	private Shell shell = new Shell(SWT.CLOSE | SWT.SYSTEM_MODAL);
	
	private int status = SWT.NO;

    private long enterTime = System.currentTimeMillis();
    
    private RecordingScheduleInfo info = null;
    
	public StopRecordDialog(Shell parentShell) {
		shell.setSize(300, 135);
		shell.setText("Recording Schedule Prompt");
		int x = parentShell.getLocation().x + parentShell.getSize().x / 2
				- shell.getSize().x / 2;
		int y = parentShell.getLocation().y + parentShell.getSize().y / 2
				- shell.getSize().y / 2;
		shell.setLocation(x, y);
	}

	/**
	 * Return recording duration <=0 means not to record.
	 * @return
	 */
	public int create(RecordingScheduleInfo info) {
		
		this.info = info;
		
		FillLayout fillLayout = new FillLayout(SWT.VERTICAL);
		fillLayout.marginHeight = 15;
		fillLayout.marginWidth = 20;
		fillLayout.spacing = 5;
		shell.setLayout(fillLayout);

		final Label label = new Label(shell, SWT.CENTER);
		
		final Timer timer = new Timer(true);
		timer.schedule(new TimerTask() {
			public void run() {
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						if (!label.isDisposed()) {
							int overdueTime = getOverdueTime();
							if (overdueTime > 0) {
				                label.setText("Recording schedule: " + StopRecordDialog.this.info +
				            		          " is required to apply, whether to stop current recording? Overdue Time: " + FormatUtil.getFormatedTime(overdueTime));
							} else {
				                label.setText("Recording schedule: " + StopRecordDialog.this.info +
				            		          " is required to apply, whether to stop current recording? Overdue schedule.");
							}
						}
					}
				});
			}
		}, 0, 1000);

		// Ok, Cancel button
		Composite composite = new Composite(shell, SWT.NONE);
		fillLayout = new FillLayout();
		fillLayout.marginWidth = 20;
		fillLayout.marginHeight = 5;
		fillLayout.spacing = 10;
		composite.setLayout(fillLayout);
		Button btOk = new Button(composite, SWT.PUSH);
		btOk.setText("Yes");
		btOk.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				status = SWT.YES;
		        timer.cancel();
				shell.dispose();
			}
		});

		Button btCancel = new Button(composite, SWT.PUSH);
		btCancel.setText("No");
		btCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
		        timer.cancel();
				shell.dispose();
			}
		});

		shell.open();

		Display display = shell.getDisplay();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		
		if (status == SWT.NO) return 0;
		
		return getOverdueTime();
	}
	
	private int getOverdueTime() {
		return (info.getEndTime() - info.getStartTime()) - (int) (System.currentTimeMillis() - this.enterTime) / 1000;
	}
}
