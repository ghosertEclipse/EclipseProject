package com.loadtrend.app.test.soundrecorder.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.Section;

import com.loadtrend.app.test.soundrecorder.info.RecordingResultInfo;
import com.loadtrend.app.test.soundrecorder.ui.dialog.NoSoundDialog;
import com.loadtrend.app.test.soundrecorder.util.FormatUtil;
import com.loadtrend.app.test.soundrecorder.util.IOOperation;
import com.loadtrend.app.test.soundrecorder.util.PreferenceUtil;

public class RecordingResultListItem {

	private RecordingSection section = null;
	
	private Table listTable = null;
	
	private CLabel lbPlay = null;
	
	private CLabel lbCopyTo = null;
	
	private CLabel lbDelete = null;
	
	private CLabel lbEdit = null;
	
	private int[] selectedIndex = null;
	
	private Mediator mediator = new Mediator();
	
	public RecordingResultListItem(RecordingSection section) {
		this.section = section;
	}
	
	public CTabItem createItem() {
        // recording result list tab item
		final CTabItem resultList = new CTabItem(section.tabFolder, SWT.NONE);
		
		Form resulstListForm = section.kit.createForm(section.tabFolder);
		resultList.setControl(resulstListForm);
		FormLayout layout = new FormLayout();
		layout.marginHeight = 10;
		layout.marginWidth = 10;
		layout.spacing = 10;
		resulstListForm.getBody().setLayout(layout);
		
		// list section
		Section listSection = section.kit.createSection(resulstListForm.getBody(), Section.TITLE_BAR | Section.TREE_NODE);
		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(70, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		listSection.setLayoutData(formData);
		listSection.setText("Recording History List:");
		listSection.setExpanded(true);
		listSection.setTitleBarBackground(section.orangeColor);
		listSection.setTitleBarGradientBackground(section.orangeColor);
		
		// list table
//        TableLayout tableLayout = new TableLayout();
//        tableLayout.addColumnData(new ColumnWeightData(25, 75, true));
//        tableLayout.addColumnData(new ColumnWeightData(20, 75, true));
//        tableLayout.addColumnData(new ColumnWeightData(55, 75, true));
        this.listTable = section.kit.createTable(listSection, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
        listSection.setClient(listTable);
        // listTable.setLayout(tableLayout);
        listTable.setHeaderVisible(true);
        listTable.setLinesVisible(true);
        TableColumn startTimeColumn = new TableColumn(listTable, SWT.CENTER);
        TableColumn durationColumn = new TableColumn(listTable, SWT.CENTER);
        TableColumn locationColumn = new TableColumn(listTable, SWT.CENTER);
        startTimeColumn.setText("Start Time");
        startTimeColumn.setWidth(160);
        durationColumn.setText("Duration");
        durationColumn.setWidth(120);
        locationColumn.setText("Location");
        locationColumn.setWidth(260);
        
        this.listTable.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(MouseEvent e) {
				mediator.mouseUp(lbPlay);
			}
        });
        
        // Create context menu for list table.
        Menu menu = this.createListTableContextMenu();
        this.listTable.setMenu(menu);
        
        // Load saved recording result file from local when clicked to listTable.
        section.tabFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (e.item == resultList) {
					refreshTable();
				}
			}
        });
		
		// Operation section
		Section operationSection = section.kit.createSection(resulstListForm.getBody(), Section.TITLE_BAR | Section.TREE_NODE);
		formData = new FormData();
		formData.top = new FormAttachment(listSection);
		formData.bottom = new FormAttachment(100, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		operationSection.setLayoutData(formData);
		operationSection.setText("Recording Result Operation:");
		operationSection.setExpanded(true);
		operationSection.setTitleBarBackground(section.orangeColor);
		operationSection.setTitleBarGradientBackground(section.orangeColor);
		
		Composite operationComposite = section.kit.createComposite(operationSection);
		operationSection.setClient(operationComposite);
		FormLayout formLayout = new FormLayout();
		formLayout.marginHeight = 5;
		formLayout.marginWidth = 20;
		formLayout.spacing = 20;
		operationComposite.setLayout(formLayout);
		
		// play button
		this.lbPlay = section.createOperationButton(operationComposite, null, "Play", "images/btPlay.gif");
		this.addMouseListener(this.lbPlay);
		
		// copy to button
		this.lbCopyTo = section.createOperationButton(operationComposite, this.lbPlay, "Copy To", null);
		this.addMouseListener(this.lbCopyTo);
		
		// delete button
		this.lbDelete = section.createOperationButton(operationComposite, this.lbCopyTo, "Delete", null);
		this.addMouseListener(this.lbDelete);
		
		// edit button
		this.lbEdit = section.createOperationButton(operationComposite, this.lbDelete, "Edit", null);
		this.addMouseListener(this.lbEdit);
		
		listTable.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				selectedIndex = listTable.getSelectionIndices();
		        section.changeCLabelStatus(lbPlay, AbstractSharedSection.ENABLE_STATUS);
		        section.changeCLabelStatus(lbCopyTo, AbstractSharedSection.ENABLE_STATUS);
		        section.changeCLabelStatus(lbDelete, AbstractSharedSection.ENABLE_STATUS);
		        section.changeCLabelStatus(lbEdit, AbstractSharedSection.ENABLE_STATUS);
			}
		});
		
		return resultList;
	}
	
	private void refreshTable() {
        section.changeCLabelStatus(lbPlay, AbstractSharedSection.DISABLE_STATUS);
        section.changeCLabelStatus(lbCopyTo, AbstractSharedSection.DISABLE_STATUS);
        section.changeCLabelStatus(lbDelete, AbstractSharedSection.DISABLE_STATUS);
        section.changeCLabelStatus(lbEdit, AbstractSharedSection.DISABLE_STATUS);
		listTable.removeAll();
		Iterator it = section.resultRecordingList.iterator();
		while (it.hasNext()) {
			RecordingResultInfo info = (RecordingResultInfo) it.next();
			TableItem item = new TableItem(listTable, SWT.NONE);
			String[] texts = new String[] {info.getStartTime().toLocaleString(),
					                       FormatUtil.getFormatedTime(info.getDuration()),
					                       info.getLocation()};
			item.setText(texts);
		}
	}
	
	private Menu createListTableContextMenu() {
        Menu menu = new Menu(this.listTable);
        MenuItem item = new MenuItem(menu, SWT.PUSH);
        item.setText("Play");
        item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				mediator.mouseUp(lbPlay);
			}
        });
        item = new MenuItem(menu, SWT.PUSH);
        item.setText("Copy To");
        item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				mediator.mouseUp(lbCopyTo);
			}
        });
        item = new MenuItem(menu, SWT.PUSH);
        item.setText("Delete");
        item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				mediator.mouseUp(lbDelete);
			}
        });
        item = new MenuItem(menu, SWT.PUSH);
        item.setText("Edit");
        item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				mediator.mouseUp(lbEdit);
			}
        });
        return menu;
	}
	
	private void addMouseListener(final CLabel label) {
		label.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				mediator.mouseUp(label);
			}
		});
	}
	
	private class Mediator {
		public void mouseUp(CLabel label) {
			
			// disable status
			if (label.getData(AbstractSharedSection.STATUS_KEY) == AbstractSharedSection.DISABLE_STATUS) return;
			
			// enable status
			if (label.getData(AbstractSharedSection.STATUS_KEY) == AbstractSharedSection.ENABLE_STATUS) {
				if (label == lbPlay) {
			    	RecordingResultInfo info = (RecordingResultInfo) section.resultRecordingList.get(selectedIndex[0]);
			    	section.notifyToPlay(info.getLocation(), info.getDuration());
				    return;
				}
				if (label == lbDelete) {
			    	NoSoundDialog box = new NoSoundDialog(Display.getDefault().getActiveShell());
			    	int status = box.create("Prompt", "Whether to delete the selected files?", 2);
			    	if (SWT.OK != status) return;
			    	
					List tempList = new ArrayList();
					boolean isUsingSign = false;
				    for (int i = 0; i < selectedIndex.length; i++) {
				    	RecordingResultInfo info = (RecordingResultInfo) section.resultRecordingList.get(selectedIndex[i]);
				    	File file = new File(info.getLocation());
				    	if (file.exists()) {
				    		if (!file.delete()) {
				    			isUsingSign = true;
				    			continue;
				    		}
				    	}
				    	tempList.add(info);
				    }
			        section.resultRecordingList.removeAll(tempList);
				    RecordingResultListItem.this.refreshTable();
				    IOOperation.writeObject(PreferenceUtil.RESULT_DATA, section.resultRecordingList);
				    if (isUsingSign) {
				    	box = new NoSoundDialog(Display.getDefault().getActiveShell());
				    	box.create("Prompt", "Some files are using, delete them next time start-up.", 1);
				    }
				    return;
				}
				if (label == lbCopyTo) {
	                DirectoryDialog dialog = new DirectoryDialog(Display.getDefault().getActiveShell());
	                dialog.setText("Select Path");
	                dialog.setMessage("Select path to save recording result files");
	                String path = dialog.open();
	                if (path != null) {
					    for (int i = 0; i < selectedIndex.length; i++) {
					    	RecordingResultInfo info = (RecordingResultInfo) section.resultRecordingList.get(selectedIndex[i]);
					    	File file = new File(info.getLocation());
					    	if (file.exists()) {
					    		IOOperation.copy(file.getAbsolutePath(), path + File.separator + file.getName());
					    	}
					    }
	                }
				    return;
				}
				if (label == lbEdit) {
			    	RecordingResultInfo info = (RecordingResultInfo) section.resultRecordingList.get(selectedIndex[0]);
			    	section.notifyToEdit(info.getLocation());
				    return;
				}
			}
		}
	}
}
