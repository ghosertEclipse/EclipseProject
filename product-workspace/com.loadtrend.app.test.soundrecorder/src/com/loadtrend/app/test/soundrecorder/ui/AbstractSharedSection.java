package com.loadtrend.app.test.soundrecorder.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.loadtrend.app.test.soundrecorder.util.ColorUtil;
import com.loadtrend.app.test.soundrecorder.util.FontUtil;
import com.loadtrend.app.test.soundrecorder.util.ImageUtil;
import com.loadtrend.app.test.soundrecorder.util.PreferenceUtil;

public abstract class AbstractSharedSection {
	
    protected FormToolkit kit = null;
    
    protected Color whiteColor = ColorUtil.getColor(ColorUtil.whiteColor);
    
    protected Color blackColor = ColorUtil.getColor(ColorUtil.blackColor);
    
    protected Color orangeColor = ColorUtil.getColor(ColorUtil.orangeColor);
    
    protected Color darkOrangeColor = ColorUtil.getColor(ColorUtil.darkOrangeColor);
    
    protected Color darkBlueColor = ColorUtil.getColor(ColorUtil.darkBlueColor);
    
    protected Color smallBlackColor = ColorUtil.getColor(ColorUtil.smallBlackColor);
    
    protected Color smallBlueColor = ColorUtil.getColor(ColorUtil.smallBlueColor);
    
    protected Color smallRedColor = ColorUtil.getColor(ColorUtil.smallRedColor);
    
    protected Composite parentComposite = null;
    
    protected Display display = null;
    
    protected CTabFolder tabFolder = null;
    
    protected static List subSectionList = new ArrayList();
    
	protected static final int CLABEL_WIDTH = 80;
    
	protected static final int CLABEL_HEIGHT = 30;
	
	protected static final int enable = 0;
	
	protected static final int disable = 1;
	
	protected static final int running = 2;
	
    protected static final Integer ENABLE_STATUS = new Integer(enable);
    
	protected static final Integer DISABLE_STATUS = new Integer(disable);
	
	protected static final Integer RUNNING_STATUS = new Integer(running);
	
	protected static final String STATUS_KEY = "status";
	
	protected static final int BELOW_LEVER = 5;

	public AbstractSharedSection(Composite parentComposite) {
		this.parentComposite = parentComposite;
    	this.display = parentComposite.getDisplay();
        this.kit = new FormToolkit(display);
        this.createTabFolder();
        // add this to preferenceutil for further adjusting the main color.
        subSectionList.add(this);
	}
	
	protected void createTabFolder() {
		this.tabFolder = new CTabFolder(this.parentComposite, SWT.TOP | SWT.BORDER);
		tabFolder.setBackground(this.smallBlackColor);
        // set color from preference
		tabFolder.setSelectionBackground(new Color[]{this.whiteColor, ColorUtil.getColor(PreferenceUtil.getValue(PreferenceUtil.MAIN_COLOR))},
                                                new int[] {50},
                                                true);
		tabFolder.setSelectionForeground(this.whiteColor);
		tabFolder.setForeground(this.whiteColor);
		tabFolder.setFont(FontUtil.getBoldSystemFont());
		
		tabFolder.setTabHeight(25);
	}
    
    protected void changeInterfaceColor() {
        Iterator it = subSectionList.iterator();
        while (it.hasNext()) {
            AbstractSharedSection section = (AbstractSharedSection) it.next();
            section.tabFolder.setSelectionBackground(new Color[]{this.whiteColor, ColorUtil.getColor(PreferenceUtil.getValue(PreferenceUtil.MAIN_COLOR))},
                                                     new int[] {50},
                                                     true);
        }
    }
    
	protected CLabel createOperationButton(Composite parentComposite, CLabel leftLabel, String name, String image) {
		final CLabel label = new CLabel(parentComposite, SWT.NONE);
		label.setText(name);
		label.setForeground(whiteColor);
		label.setFont(FontUtil.getBoldSystemFont());
		if (image != null) label.setImage(ImageUtil.getImage(image));
		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		if (leftLabel != null) formData.left = new FormAttachment(leftLabel);
		formData.width = CLABEL_WIDTH;
		formData.height = CLABEL_HEIGHT;
		label.setLayoutData(formData);
		label.addMouseTrackListener(new MouseTrackAdapter(){
			public void mouseEnter(MouseEvent e) {
				if (label.getData(STATUS_KEY) == ENABLE_STATUS) {
			        label.setBackground(new Color[]{whiteColor, darkOrangeColor, whiteColor},
        		            new int[] {50, 100},
                            true);
				}
			}
			
			public void mouseExit(MouseEvent e) {
				if (label.getData(STATUS_KEY) == ENABLE_STATUS) {
			        label.setBackground(new Color[]{whiteColor, darkBlueColor, whiteColor},
        		            new int[] {50, 100},
                            true);
				}
			}
			
		});
		return label;
	}
	
	protected void changeCLabelStatus(CLabel label, Integer status) {
        FormData formData = (FormData) label.getLayoutData();
		switch (status.intValue()) {
		    case enable : 
		        label.setBackground(new Color[]{whiteColor, darkBlueColor, whiteColor},
		        		            new int[] {50, 100},
                                    true);
		        label.setData(STATUS_KEY, ENABLE_STATUS);
		        if (formData.top.offset == BELOW_LEVER) {
		            formData.top.offset = 0;
				    label.getParent().layout(true);
		        }
		        break;
		    case disable :
		        label.setBackground(new Color[]{whiteColor, smallBlackColor, whiteColor},
		        		            new int[] {50, 100},
                                    true);
		        label.setData(STATUS_KEY, DISABLE_STATUS);
		        if (formData.top.offset == BELOW_LEVER) {
		            formData.top.offset = 0;
				    label.getParent().layout(true);
		        }
		        break;
		    case running :
		        label.setBackground(new Color[]{whiteColor, darkOrangeColor, whiteColor},
		        		            new int[] {50, 100},
                                    true);
		        label.setData(STATUS_KEY, RUNNING_STATUS);
		        if (formData.top.offset == 0) {
		            formData.top.offset = BELOW_LEVER;
				    label.getParent().layout(true);
		        }
		        break;
		}
	}
}
