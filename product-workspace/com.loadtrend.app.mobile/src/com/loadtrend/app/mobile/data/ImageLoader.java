package com.loadtrend.app.mobile.data;

import java.util.Enumeration;
import java.util.ResourceBundle;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

public class ImageLoader
{	
	private static ImageLoader imageLoader = null;
	
	private ImageRegistry imageRegistry = new ImageRegistry();
	
	private ImageLoader()
	{
	}
	
	public static ImageLoader getInstance()
	{
		if ( imageLoader == null )
		{
			imageLoader = new ImageLoader();
			imageLoader.loadImage();
		}
		return imageLoader;
	}
	
	public Image getImage( String imageKey )
	{
		return imageRegistry.get( imageKey );
	}
	
	public ImageDescriptor getImageDescriptor( String imageKey )
	{
		return imageRegistry.getDescriptor( imageKey );
	}
	
	private void loadImage()
	{
		String key = null;
		String pathname = null;
		
		ResourceBundle bundle = ResourceBundle.getBundle( MobileAppConstant.IMAGE_PROPERTIES );
		Enumeration enumeration = bundle.getKeys();
		while ( enumeration.hasMoreElements() )
		{
			key = (String) enumeration.nextElement();
			pathname = bundle.getString( key );
			storeToImageRegistry( key, pathname );
		}
	}
	
	private void storeToImageRegistry( String imageKey, String imagePathname )
	{
		imageRegistry.put( imageKey, createImageDescriptor( imagePathname ) );
	}
	
	private ImageDescriptor createImageDescriptor( String imagePathname )
	{
		ImageData data = new ImageData( this.getClass().getClassLoader().getResourceAsStream( imagePathname ) );
		ImageDescriptor id = ImageDescriptor.createFromImageData( data );
		return id;
	}
}
