package com.loadtrend.app.util.springClientRemote;

import org.eclipse.ui.plugin.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.BundleContext;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import com.loadtrend.web.mobile.client.JMobileClientManager;
import com.loadtrend.web.mobile.service.JMobileManager;

/**
 * The main plugin class to be used in the desktop.
 */
public class SpringClientRemotePlugin extends AbstractUIPlugin {
    
    private JMobileManager jMobileManager = null;
    
    private JMobileClientManager jMobileClientManager = null;

	//The shared instance.
	private static SpringClientRemotePlugin plugin;
	
	/**
	 * The constructor.
	 */
	public SpringClientRemotePlugin() {
		plugin = this;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
        BeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("clientContext.xml"));
        this.jMobileManager = (JMobileManager) beanFactory.getBean("httpInvokerProxy");
        this.jMobileClientManager = (JMobileClientManager) beanFactory.getBean("clientHttpInvokerProxy");
        Thread.currentThread().setContextClassLoader(classLoader);
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static SpringClientRemotePlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin("com.loadtrend.app.util.springClientRemote", path);
	}

    public JMobileManager getJMobileManager() {
        return jMobileManager;
    }

    public JMobileClientManager getJMobileClientManager() {
        return jMobileClientManager;
    }
}
