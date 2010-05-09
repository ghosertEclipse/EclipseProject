package com.loadtrend.app.mobile.views.smsArrival;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import loadtrend.mobile.Message;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;

import com.loadtrend.app.mobile.data.Messages;
import com.loadtrend.app.mobile.data.MessagesConstant;
import com.loadtrend.app.mobile.dialog.NewMessageArrivalPopupWindowShell;
import com.loadtrend.app.mobile.dialog.NewSMSDialog;
import com.loadtrend.app.mobile.util.SpecialEntity;

public class SMSArrivalViewLog extends ViewPart {

    public static final String VIEW_ID = "com.loadtrend.app.mobile.views.smsArrival.SMSArrivalViewLog";
    
    private static final String RECEIVESMS = "receiveSMS";

    private static final String REPLYSMS = "replySMS";
    
    private static final String DELETESMS = "deleteSMS";
    
    private static final String SYSTEM = "system";

    private Color red = null;
    
    private Color green = null;
    
    private Color blue = null;
    
    SMSArrivalViewListener listener = null;
    
    public void createPartControl(Composite parent) {
        
        FormColors formColors = new FormColors(this.getSite().getShell().getDisplay());
        blue = formColors.getColor(FormColors.TITLE);
        red = parent.getDisplay().getSystemColor(SWT.COLOR_DARK_RED);
        green = parent.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN);
        
        this.setPartName(Messages.getString(MessagesConstant.SMSArrivalView_BOTTOMSECTION_TEXT));
        FormToolkit kit = new FormToolkit( parent.getDisplay() );
        final ScrolledForm bottomScrolledForm = kit.createScrolledForm( parent );
        
        final Shell shell = this.getSite().getShell();
        bottomScrolledForm.getBody().setLayout(new TableWrapLayout());
        final FormText monitorText = kit.createFormText(bottomScrolledForm.getBody(), true);
        monitorText.setLayoutData(new TableWrapData());
        monitorText.setWhitespaceNormalized(true);
        monitorText.setColor(SYSTEM, blue);
        monitorText.setColor(RECEIVESMS, green);
        monitorText.setColor(REPLYSMS, red);
        monitorText.setColor(DELETESMS, red);
        monitorText.setText("<form><p>" + Messages.getString(MessagesConstant.SMSArrivalView_MONITORTEXT_DESC) + "</p></form>", true, true);
        monitorText.addHyperlinkListener(new HyperlinkAdapter() {
            public void linkActivated(HyperlinkEvent e) {
                NewSMSDialog dialog = new NewSMSDialog(shell);
                dialog.create();
                dialog.getTxtReceiver().setText(e.getHref().toString());
                dialog.getTxtContent().setText("");
                dialog.getTxtContent().setFocus();
                dialog.open();
            }
        });
        
        final StringBuffer monitorTextString = new StringBuffer();
        
        // define the SMSArrivalViewListener, SHOULD BE SYNCHRONIZED SHARED RESOURCE.
        listener = new SMSArrivalViewListener() {
            public void popupWindow(Message message) {
                new NewMessageArrivalPopupWindowShell(shell, message).create();
            }
            
            // synchronized the shared resource monitor text for several work thread will reached this method.
            public synchronized void refreshMonitorText(Message message, boolean receiveSMS) throws ParseException {
                final StringBuffer string = new StringBuffer();
                if (receiveSMS) {
                    SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
                    Date date = format.parse(message.getReceiveTime().substring(0, 12));
                    format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateString = format.format(date);
                    string.append(Messages.getString(MessagesConstant.SMSArrivalView_MONITORTEXT_RECEIVESMS,
                                                     SpecialEntity.encode(dateString),
                                                     SpecialEntity.encode(message.getTeleNum()),
                                                     SpecialEntity.encode(message.getContent())));
                } else {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateString = format.format(new Date());
                    string.append(Messages.getString(MessagesConstant.SMSArrivalView_MONITORTEXT_SENDSMS,
                                                     SpecialEntity.encode(dateString),
                                                     SpecialEntity.encode(message.getTeleNum()),
                                                     SpecialEntity.encode(message.getContent())));
                }
                monitorTextString.insert(0, string);
                shell.getDisplay().syncExec(new Runnable() {
                    public void run() {
                        monitorText.setText("<form><p>" + monitorTextString + "</p></form>", true, true);
                        bottomScrolledForm.reflow(true);
                    }
                });
                
            }

            // define the SMSArrivalViewListener, SHOULD BE SYNCHRONIZED SHARED RESOURCE.
            public synchronized void startOrStopMonitor(boolean isStarting, boolean isStartOrStop) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = format.format(new Date());
                StringBuffer string = new StringBuffer();
                if (isStarting) {
                    string.append(Messages.getString(MessagesConstant.SMSArrivalView_MONITORTEXT_STARTING, dateString));
                } else {
	                if (isStartOrStop) {
	                    string.append(Messages.getString(MessagesConstant.SMSArrivalView_MONITORTEXT_STARTED, dateString));
	                } else {
	                    string.append(Messages.getString(MessagesConstant.SMSArrivalView_MONITORTEXT_STOPPED, dateString));
	                }
                }
                monitorTextString.insert(0, string);
                shell.getDisplay().syncExec(new Runnable() {
                    public void run() {
                        monitorText.setText("<form><p>" + monitorTextString + "</p></form>", true, true);
                        bottomScrolledForm.reflow(true);
                    }
                });
            }

            public void deleteMessageLog(Message message) {
                StringBuffer string = new StringBuffer(Messages.getString(MessagesConstant.SMSArrivalView_MONITORTEXT_DELETESMS));
                monitorTextString.insert(0, string);
                shell.getDisplay().syncExec(new Runnable() {
                    public void run() {
                        monitorText.setText("<form><p>" + monitorTextString + "</p></form>", true, true);
                        bottomScrolledForm.reflow(true);
                    }
                });
            }
        };
    }

    public void setFocus() {
        // TODO Auto-generated method stub
    }
    
    public void dispose()
    {
        super.dispose();
        green.dispose();
        red.dispose();
        blue.dispose();
    }

    public SMSArrivalViewListener getListener() {
        return listener;
    }
}
