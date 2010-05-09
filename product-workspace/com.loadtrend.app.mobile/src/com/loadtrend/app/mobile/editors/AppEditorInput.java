package com.loadtrend.app.mobile.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

public class AppEditorInput implements IEditorInput
{
	private String name = null;
	private ImageDescriptor imageDescriptor = null;
	private String toolTipText = null;
	
	public AppEditorInput( String name, ImageDescriptor imageDescriptor, String toolTipText )
	{
		this.name = name;
		this.imageDescriptor = imageDescriptor;
		this.toolTipText = toolTipText;
	}
	
	/**
	* ����true����򿪸ñ༭�������������Eclipse���˵����ļ���
	* ���²�������򿪵��ĵ����С�����false�򲻳���������
	*/
	public boolean exists()
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	public void setImageDescriptor( ImageDescriptor imageDescriptor )
	{
		this.imageDescriptor = imageDescriptor;
	}
	
	/**
	* �༭����������ͼ�꣬����������Ҫ��ChinaEditor����
	* setTitleImage�������ã����ܳ����ڱ�������
	*/
	public ImageDescriptor getImageDescriptor()
	{
		// TODO Auto-generated method stub
		return imageDescriptor;
	}
	
	public void setName( String name )
	{
		this.name = name;
	}
	
	/**
	* �༭������������ʾ���ƣ��������getImageDescriptor
	* һ��ҲҪ��ChinaEditor����setPartName�������ã����ܳ����ڱ�������
	*/
	public String getName()
	{
		// TODO Auto-generated method stub
		return this.name;
	}

	/**
	* ����һ�������������汾�༭��������״̬�Ķ��󣬱���������ʵ��
	*/
	public IPersistableElement getPersistable()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setToolTipText( String toolTipText )
	{
		this.toolTipText = toolTipText;
	}
	
	/**
	* �༭����������С������ʾ���֣�������getName������ChinaEditor��������
	*/
	public String getToolTipText()
	{
		// TODO Auto-generated method stub
		return toolTipText;
	}

	/**
	* �õ�һ���༭����������������������ʵ��
	* IAdaptable a = new ChinaEditorInput();
	* IFoo x = (IFoo)a.getAdapter(IFoo.class);
    * if (x != null)
    * [��x����IFoo������]
    */
	public Object getAdapter( Class adapter )
	{
		// TODO Auto-generated method stub
		return null;
	}
	
}