package com.loadtrend.app.mobile.loadtool.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class CaptureDialog extends Dialog {
	
    private Button okButton = null;
    
    private Text txtUrl = null;
    
    private Text txtFile = null;
    
    private Button rdPicture = null;
    
    private Button rdMusic = null;
    
    private String url = null;
    
    private String file = null;
    
    private boolean isPicture = false;
    
    private boolean isMusic = false;
    
	public CaptureDialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}

	public CaptureDialog(IShellProvider parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}

    protected Control createDialogArea(Composite parent) {
        GridLayout gridLayout = new GridLayout(2, true);
        parent.setLayout(gridLayout);
        
        Label label = new Label(parent, SWT.NORMAL);
        label.setText("数据源地址文件(右键点文本框):");
        label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        txtUrl = new Text(parent, SWT.BORDER);
        txtUrl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        label = new Label(parent, SWT.NORMAL);
        label.setText("文件保存(右键点文本框):");
        label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        txtFile = new Text(parent, SWT.BORDER);
        txtFile.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        txtUrl.addMouseListener(new MouseAdpater(txtUrl));
        txtFile.addMouseListener(new MouseAdpater(txtFile));
        
        ModifyListener modifyListener = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				boolean enable = txtUrl.getText().length() > 0 && txtFile.getText().length() > 0;
			    okButton.setEnabled(enable);
			}
        };
        
        txtUrl.addModifyListener(modifyListener);
        txtFile.addModifyListener(modifyListener);
        
        rdPicture = new Button(parent, SWT.RADIO);
        rdPicture.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        rdPicture.setText("数据源是图片资源");
        
        rdMusic = new Button(parent, SWT.RADIO);
        rdMusic.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        rdMusic.setText("数据源是音乐资源");
        
        return parent;
    }
    
    private class MouseAdpater extends MouseAdapter {
    	private Text text = null;
    	public MouseAdpater(Text text) {
    		this.text = text;
    	}
		public void mouseDown(MouseEvent e) {
			if (e.button == 3) {
                FileDialog dialog = new FileDialog(text.getShell());
                dialog.setFilterExtensions(new String[] {"*.txt"});
                dialog.setFilterNames(new String[] {"文本格式(*.txt)"});
                String pathname = dialog.open();
                if (pathname != null) {
                	text.setText(pathname);
                }
			}
		}
    }

    protected void createButtonsForButtonBar(Composite parent) {
        // TODO Auto-generated method stub
        super.createButtonsForButtonBar(parent);
        okButton = super.getButton(ProductDialog.OK);
        okButton.setEnabled(false);
    }
    
	protected void okPressed() {
		isMusic = rdMusic.getSelection();
		isPicture = rdPicture.getSelection();
		file = txtFile.getText();
		url = txtUrl.getText();
		super.okPressed();
	}

	/**
	 * @return Returns the isMusic.
	 */
	public boolean isMusic() {
		return isMusic;
	}

	/**
	 * @return Returns the isPicture.
	 */
	public boolean isPicture() {
		return isPicture;
	}

	/**
	 * @return Returns the file.
	 */
	public String getFile() {
		return file;
	}

	/**
	 * @return Returns the url.
	 */
	public String getUrl() {
		return url;
	}
}
