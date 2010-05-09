package com.loadtrend.app.mobile.clientRemote;

import org.eclipse.ui.plugin.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.BundleContext;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import com.loadtrend.web.mobile.client.JMobileClientManager;

/**
 * The main plugin class to be used in the desktop.
 */
public class ClientRemotePlugin extends AbstractUIPlugin {
    
    private JMobileClientManager jMobileClientManager = null;

    //The shared instance.
    private static ClientRemotePlugin plugin;
    
    /**
     * The constructor.
     */
    public ClientRemotePlugin() {
        plugin = this;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
        BeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("clientContext.xml"));
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
    public static ClientRemotePlugin getDefault() {
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
        return AbstractUIPlugin.imageDescriptorFromPlugin("com.loadtrend.app.mobile.clientRemote", path);
    }

    public JMobileClientManager getJMobileClientManager() {
        return jMobileClientManager;
    }
}
