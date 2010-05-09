package com.loadtrend.app.mobile.loadtool.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ItemDialog extends Dialog {
	
	    private Text txtProductId = null;
	    
	    private String productId = null;
	    
	    private Text txtName = null;
	    
	    private String name = null;
	    
	    private Text txtAuthor = null;
	    
	    private String author = null;
	    
	    private Text txtUrl = null;
	    
	    private String url = null;
	    
	    private Text txtUploader = null;
	    
	    private String uploader = null;
	    
	    private Text txtWeekpaytimes = null;
	    
	    private String weekpaytimes = null;
	    
	    private Text txtPaytimes = null;
	    
	    private String paytimes = null;
	    
	    private Button btIsValid = null;
	    
	    private boolean isValid = false;
	    
	    private Text txtUploadTime = null;
	    
	    private String uploadTime = null;
	    
	    private Button okButton = null;
	    
	    public ItemDialog(Shell shell) {
	        super(shell);
	    }

	    protected Control createDialogArea(Composite parent) {
	    	
	        GridLayout gridLayout = new GridLayout(2, true);
	        parent.setLayout(gridLayout);
	        
	        Label label = new Label(parent, SWT.NORMAL);
	        label.setText("分类编号(填数字): ");
	        txtProductId = new Text(parent, SWT.BORDER);
	        txtProductId.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	        txtProductId.setTextLimit(10);
	        
	        label = new Label(parent, SWT.NORMAL);
	        label.setText("项目名称: ");
	        txtName = new Text(parent, SWT.BORDER);
	        txtName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	        txtName.setTextLimit(50);
	        
	        label = new Label(parent, SWT.NORMAL);
	        label.setText("作者(音乐项目填): ");
	        txtAuthor = new Text(parent, SWT.BORDER);
	        txtAuthor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	        txtAuthor.setTextLimit(20);
	        
	        label = new Label(parent, SWT.NORMAL);
	        label.setText("URL链接: ");
	        txtUrl = new Text(parent, SWT.BORDER);
	        txtUrl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	        txtUrl.setTextLimit(512);
	        
	        label = new Label(parent, SWT.NORMAL);
	        label.setText("上传者: ");
	        txtUploader = new Text(parent, SWT.BORDER);
	        txtUploader.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	        txtUploader.setTextLimit(20);
	        
	        label = new Label(parent, SWT.NORMAL);
	        label.setText("周下载(填数字): ");
	        txtWeekpaytimes = new Text(parent, SWT.BORDER);
	        txtWeekpaytimes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	        txtWeekpaytimes.setTextLimit(10);
	        
	        label = new Label(parent, SWT.NORMAL);
	        label.setText("总下载(填数字): ");
	        txtPaytimes = new Text(parent, SWT.BORDER);
	        txtPaytimes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	        txtPaytimes.setTextLimit(10);
	        
	        btIsValid = new Button(parent, SWT.CHECK);
	        btIsValid.setText("是否有效(选中即有效)");
	        
	        txtUploadTime = new Text(parent, SWT.NORMAL);
	        txtUploadTime.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	        txtUploadTime.setEditable(false);
	        
	        ModifyListener modifyListener = new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					boolean enable = txtProductId.getText().length() > 0 && 
					                 txtName.getText().length() > 0 && 
					                 txtPaytimes.getText().length() > 0 &&
					                 txtWeekpaytimes.getText().length() > 0 &&
					                 txtUrl.getText().length() > 0;
					enable = enable && txtProductId.getText().matches("[0-9]+");
					enable = enable && txtPaytimes.getText().matches("[0-9]+");
					enable = enable && txtWeekpaytimes.getText().matches("[0-9]+");
	                okButton.setEnabled(enable);
				}
	        };
	        
	        txtProductId.addModifyListener(modifyListener);
	        txtAuthor.addModifyListener(modifyListener);
	        txtName.addModifyListener(modifyListener);
	        txtPaytimes.addModifyListener(modifyListener);
	        txtUploader.addModifyListener(modifyListener);
	        txtUrl.addModifyListener(modifyListener);
	        txtWeekpaytimes.addModifyListener(modifyListener);
	        
	        return parent;
	    }

	    protected void createButtonsForButtonBar(Composite parent) {
	        // TODO Auto-generated method stub
	        super.createButtonsForButtonBar(parent);
	        okButton = super.getButton(ProductDialog.OK);
	        okButton.setEnabled(false);
	    }
	    
	    protected void okPressed() {
	        this.name = this.txtName.getText();
	        this.productId = this.txtProductId.getText();
	        this.author = this.txtAuthor.getText();
	        this.paytimes = this.txtPaytimes.getText();
	        this.weekpaytimes = this.txtWeekpaytimes.getText();
	        this.uploader = this.txtUploader.getText();
	        this.url = this.txtUrl.getText();
	        this.isValid = this.btIsValid.getSelection();
	        super.okPressed();
	    }

	    public String getName() {
	        return this.name;
	    }
	    
	    public void setName(String name) {
	        this.txtName.setText(name);
	    }

		/**
		 * @return Returns the author.
		 */
		public String getAuthor() {
			return author;
		}

		/**
		 * @param author The author to set.
		 */
		public void setAuthor(String author) {
			this.txtAuthor.setText(author);
		}

		/**
		 * @return Returns the isValid.
		 */
		public boolean isValid() {
			return isValid;
		}

		/**
		 * @param isValid The isValid to set.
		 */
		public void setValid(boolean isValid) {
			this.btIsValid.setSelection(isValid);
		}

		/**
		 * @return Returns the paytimes.
		 */
		public String getPaytimes() {
			return paytimes;
		}

		/**
		 * @param paytimes The paytimes to set.
		 */
		public void setPaytimes(String paytimes) {
			this.txtPaytimes.setText(paytimes);
		}

		/**
		 * @return Returns the productId.
		 */
		public String getProductId() {
			return productId;
		}

		/**
		 * @param productId The productId to set.
		 */
		public void setProductId(String productId) {
			this.txtProductId.setText(productId);
		}

		/**
		 * @return Returns the uploader.
		 */
		public String getUploader() {
			return uploader;
		}

		/**
		 * @param uploader The uploader to set.
		 */
		public void setUploader(String uploader) {
			this.txtUploader.setText(uploader);
		}

		/**
		 * @return Returns the url.
		 */
		public String getUrl() {
			return url;
		}

		/**
		 * @param url The url to set.
		 */
		public void setUrl(String url) {
			this.txtUrl.setText(url);
		}

		/**
		 * @return Returns the weekpaytimes.
		 */
		public String getWeekpaytimes() {
			return weekpaytimes;
		}

		/**
		 * @param weekpaytimes The weekpaytimes to set.
		 */
		public void setWeekpaytimes(String weekpaytimes) {
			this.txtWeekpaytimes.setText(weekpaytimes);
		}

		/**
		 * @return Returns the uploadTime.
		 */
		public String getUploadTime() {
			return uploadTime;
		}

		/**
		 * @param uploadTime The uploadTime to set.
		 */
		public void setUploadTime(String uploadTime) {
			this.txtUploadTime.setText(uploadTime);
		}
}
