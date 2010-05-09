package com.loadtrend.app.test.soundrecorder.ui.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class NoSoundDialog {
	private Shell shell = new Shell(SWT.CLOSE | SWT.SYSTEM_MODAL);

	private int status = SWT.CANCEL;

	public NoSoundDialog(Shell parentShell) {
		shell.setSize(300, 120);
		int x = parentShell.getLocation().x + parentShell.getSize().x / 2
				- shell.getSize().x / 2;
		int y = parentShell.getLocation().y + parentShell.getSize().y / 2
				- shell.getSize().y / 2;
		shell.setLocation(x, y);
	}

	/**
	 * Return SWT.OK or SWT.CANCEL
	 * @param title
	 * @param message
	 * @return
	 */
	public int create(String title, String message, int numOfButton) {
		shell.setText(title);

		FillLayout fillLayout = new FillLayout(SWT.VERTICAL);
		fillLayout.marginHeight = 15;
		fillLayout.marginWidth = 20;
		shell.setLayout(fillLayout);

		Label label = new Label(shell, SWT.CENTER);
		label.setText(message);

		// Ok, Cancel button
		Composite composite = new Composite(shell, SWT.NONE);
		fillLayout = new FillLayout();
		fillLayout.marginWidth = 70;
		fillLayout.marginHeight = 5;
		fillLayout.spacing = 10;
		composite.setLayout(fillLayout);
		Button btOk = new Button(composite, SWT.PUSH);
		btOk.setText("Confirm");
		btOk.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				status = SWT.OK;
				shell.dispose();
			}
		});

		if (numOfButton == 2) {
			fillLayout.marginWidth = 20;

			Button btCancel = new Button(composite, SWT.PUSH);
			btCancel.setText("Cancel");
			btCancel.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					shell.dispose();
				}
			});
		}

		shell.open();

		Display display = shell.getDisplay();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return status;
	}
}
