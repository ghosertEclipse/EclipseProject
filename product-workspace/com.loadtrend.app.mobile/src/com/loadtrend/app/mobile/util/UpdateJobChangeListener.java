package com.loadtrend.app.mobile.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.PluginVersionIdentifier;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;
import org.eclipse.update.configurator.ConfiguratorUtils;
import org.eclipse.update.configurator.IPlatformConfiguration;
import org.eclipse.update.core.IFeature;
import org.eclipse.update.core.VersionedIdentifier;
import org.eclipse.update.internal.ui.UpdateUI;
import org.eclipse.update.operations.IInstallFeatureOperation;
import org.eclipse.update.standalone.InstallCommand;
import org.eclipse.update.ui.UpdateJob;

public class UpdateJobChangeListener extends JobChangeAdapter {
	
	private IWorkbenchWindow window = null;
	
	private UpdateJob job = new UpdateJob("正在搜索可升级组件...", true, true);
	
	private boolean isStartup = false;
	
	private static UpdateJobChangeListener instance = null;
	
	private UpdateJobChangeListener(IWorkbenchWindow window) {
        this.window = window;
	}
	
	public static UpdateJobChangeListener getInstance(IWorkbenchWindow window) {
		if (instance == null) {
			instance = new UpdateJobChangeListener(window);
		}
		return instance;
	}
	
	public void start(boolean isStartup) {
		
		this.isStartup = isStartup;
		
        // cancel any existing jobs and remove listeners
        Platform.getJobManager().removeJobChangeListener(this);
        Platform.getJobManager().cancel(job);
                
        // then setup the new job and listener and schedule the job
        Platform.getJobManager().addJobChangeListener(this);
        job.schedule();
        
		// UpdateManagerUI.openInstaller(window.getShell(), job);
		if (!this.isStartup) PlatformUI.getWorkbench().getProgressService().showInDialog(window.getShell(), job);
	}

	public void done(final IJobChangeEvent event) {
        // the job listener is triggered when the download job is done, and it proceeds
        // with the actual install
        if (event.getJob() == this.job && this.job.getStatus() == Status.OK_STATUS) {
            Platform.getJobManager().removeJobChangeListener(this);
            Platform.getJobManager().cancel(this.job);
            
            final IProgressService progressService= PlatformUI.getWorkbench().getProgressService();
            UpdateUI.getStandardDisplay().asyncExec(new Runnable() {
                public void run() {
                	
                	// Check updates: no available remote update server or the net connection is down.
                	if (job.getUpdates() == null || job.getUpdates().length == 0) {
                		if (!isStartup) UpdateJobChangeListener.this.popupNoUpdates("您当前的软件版本是最新版本,不需要更新。");
                		return;
                	}
                	
                	// Check feature updates
                    final IInstallFeatureOperation[] operations = job.getUpdates();
                    List list = UpdateJobChangeListener.this.getUpdatedFeatures(operations);
                    if (list.size() == 0) {
                		if (!isStartup) UpdateJobChangeListener.this.popupNoUpdates("您当前的软件版本是最新版本,不需要更新。");
                        return;
                    }
                    
                    // Whether install now ?
                    MessageBox box = new MessageBox(window.getShell(), SWT.OK | SWT.CANCEL | SWT.ICON_INFORMATION);
                    box.setText("检查更新");
                    box.setMessage("系统已检测，下载到一个更新的版本，是否现在安装更新以替换当前版本？");
                    int buttonId = box.open();
                    if (buttonId == SWT.CANCEL) return;
                    
                    // Install all the updated features now.
                    Iterator it = list.iterator();
                    while (it.hasNext()) {
                    	final IFeature feature = (IFeature) it.next();
                        try {
                            progressService.busyCursorWhile( new IRunnableWithProgress() {
                                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                                    UpdateJobChangeListener.this.install(feature, monitor);
                                }
                            });
                        } catch (InvocationTargetException e) {
                            UpdateUI.logException(e);
                        } catch (InterruptedException e) {
                            UpdateUI.logException(e, false);
                        }
                    }
                    
                    // Whether restart.
                    box = new MessageBox(window.getShell(), SWT.OK | SWT.CANCEL | SWT.ICON_INFORMATION);
                    box.setText("更新版本");
                    box.setMessage("新版本安装完毕，您必须重新启动软件加载更新，是否现在就重新启动？");
                    buttonId = box.open();
                    if (buttonId == SWT.OK) window.getWorkbench().restart();
                }
            }); 
        } 
        if (event.getJob() == this.job && this.job.getStatus() == Status.CANCEL_STATUS) {
            Platform.getJobManager().removeJobChangeListener(this);
            Platform.getJobManager().cancel(this.job);
        	return;
        }
        if (event.getJob() == this.job && this.job.getStatus() != Status.OK_STATUS) {
            Platform.getJobManager().removeJobChangeListener(this);
            Platform.getJobManager().cancel(this.job);
            UpdateUI.getStandardDisplay().syncExec(new Runnable() {
                public void run() {
                	if (!isStartup) UpdateJobChangeListener.this.popupNoUpdates("无法访问升级服务器, 请检查网络连接或升级服务器不可用, 请直接在官网下载更新。");
                    UpdateUI.log(event.getResult(), true);
               		return;
                }
            });
        }
    }
    
    private void popupNoUpdates(String message) {
        MessageBox box = new MessageBox(window.getShell(), SWT.OK | SWT.ICON_INFORMATION);
        box.setText("检查更新");
        box.setMessage(message);
        box.open();
    }
    
    private List getUpdatedFeatures(IInstallFeatureOperation[] operations) {
    	List list = new ArrayList();
        for (int i = 0; i < operations.length; i++) {
            IFeature feature = operations[i].getFeature();
            VersionedIdentifier identifier = feature.getVersionedIdentifier();
            boolean isOlder = UpdateJobChangeListener.this.checkFeature(identifier.getIdentifier(), identifier.getVersion().toString());
            if (!isOlder) list.add(feature);
        }
        return list;
    }
    
    private boolean checkFeature(String feature, String version) {
        IPlatformConfiguration config = ConfiguratorUtils.getCurrentPlatformConfiguration();
        IPlatformConfiguration.IFeatureEntry[] features = config.getConfiguredFeatureEntries();
        PluginVersionIdentifier targetVersion = new PluginVersionIdentifier(version);
        for (int i = 0; i < features.length; i++) {
          String id = features[i].getFeatureIdentifier();
          if (feature.equals(id)) {
            PluginVersionIdentifier v = new PluginVersionIdentifier(
                features[i].getFeatureVersion());
            if (v.isCompatibleWith(targetVersion))
              return true;
          }
        }
        return false;
      }

    private void install(IFeature feature, IProgressMonitor monitor) {
    	VersionedIdentifier versionedIdentifier = feature.getVersionedIdentifier();
    	String identifier = versionedIdentifier.getIdentifier();
    	String version = versionedIdentifier.getVersion().toString();
    	String updateSite = feature.getUpdateSiteEntry().getURL().toString();
        try {
            InstallCommand install = new InstallCommand(identifier, version, updateSite, null, "false");
            install.run(monitor);
        } catch (Exception e) {
            UpdateUI.logException(e);
        }
    }
}
