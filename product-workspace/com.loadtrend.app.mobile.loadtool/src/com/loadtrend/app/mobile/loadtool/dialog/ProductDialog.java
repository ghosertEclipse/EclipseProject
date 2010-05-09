package com.loadtrend.app.mobile.loadtool.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ProductDialog extends Dialog {
    private Text txtName = null;
    
    private String name = null;
    
    private Button okButton = null;
    
    public ProductDialog(Shell shell) {
        super(shell);
    }

    protected Control createDialogArea(Composite parent) {
        GridLayout gridLayout = new GridLayout(2, true);
        parent.setLayout(gridLayout);
        
        Label label = new Label(parent, SWT.NORMAL);
        label.setText("·ÖÀàÃû×Ö: ");
        
        txtName = new Text(parent, SWT.BORDER);
        txtName.setSize(10, 10);
        txtName.setTextLimit(10);
        
        txtName.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                okButton.setEnabled(txtName.getText().length() > 0);
            }
        });
        
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
        super.okPressed();
    }

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.txtName.setText(name);
    }
    
}
