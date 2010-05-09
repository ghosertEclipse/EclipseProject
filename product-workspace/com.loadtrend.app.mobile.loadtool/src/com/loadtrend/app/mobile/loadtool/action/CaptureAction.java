package com.loadtrend.app.mobile.loadtool.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;

import com.loadtrend.app.mobile.loadtool.dialog.CaptureDialog;
import com.loadtrend.app.mobile.loadtool.util.HttpClientPost;

public class CaptureAction extends NetAction {
	
	private static final String prefix = "http://www.caishow.com";
	
	public CaptureAction(IWorkbenchWindow window) {
		super("捕捉图铃");
		super.window = window;
	}

	protected void netExecute() {
		CaptureDialog captureDialog = new CaptureDialog(window.getShell());
		if (captureDialog.open() != IDialogConstants.OK_ID) return;
		String pathname = captureDialog.getFile();
		String url = captureDialog.getUrl();
		boolean isMusic = captureDialog.isMusic();
		boolean isPicture = captureDialog.isPicture();
		
		
		List resultList = new ArrayList();
		
		// load url list from file.
		List urlList = this.loadUrls(url);
		List result = null;
		Iterator it = urlList.iterator();
		while (it.hasNext()) {
			String line = (String) it.next();
		    if (isMusic) result = this.getResultList(line, false);
		    if (isPicture) result = this.getResultList(line, true);
			if (result == null) {
				MessageBox box = new MessageBox(window.getShell());
				box.setMessage("失败，请检查参数网络连接后重试。");
				box.open();
				return;
			}
			resultList.addAll(0, result);
		}
		
		// Save result to file.
		boolean success = this.saveFile(resultList, pathname);
		if (!success) {
			MessageBox box = new MessageBox(window.getShell());
			box.setMessage("保存文件失败。");
			box.open();
			return;
		}
		
		MessageBox box = new MessageBox(window.getShell());
		box.setMessage("捕获图铃成功，结果已存入文件" + pathname);
		box.open();
		return;
	}
	
	private List getResultList(final String url, final boolean isPictureOrRingtone) {
		final List resultList = new ArrayList();
		
		final Set set = isPictureOrRingtone ? this.parseHtml(url, "<A HREF='/Pic/", "<A HREF='".length(),".Html", 0) :
		                                      this.parseHtml(url, "<A HREF='/Ring/", "<A HREF='".length(),".Html", 0);
		if (set == null) return null;
		
        // Create Progress Dialog
        ProgressMonitorDialog monitorDialog = new ProgressMonitorDialog(window.getShell());
		monitorDialog.create();
		IRunnableWithProgress runnableWithProgress = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				monitor.beginTask(MessageFormat.format("正在从页面 {0} 捕获图铃...", new String[] {url}), set.size() );
				Iterator it = set.iterator();
				while ( it.hasNext() ) {
					monitor.worked( 1 );
				    String targetUrl = prefix + (String) it.next();
					monitor.subTask(MessageFormat.format("正在从链接 {0} 捕获图铃...", new String[] {targetUrl}));
				    String result = isPictureOrRingtone ? getPictrueResult(targetUrl) : getRingResult(targetUrl);
				    if (result == null) continue;
				    resultList.add(result);
					if (monitor.isCanceled()) {
						throw new InterruptedException();
					}
				}
				// finish the long time performing
				monitor.done();
				}
		};
		
		try {
			monitorDialog.run( true, true, runnableWithProgress );
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return null;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
		
		return resultList;
	}
	
	private String getPictrueResult(String targetUrl) {
		Set[] sets = this.parseHtml(targetUrl,
				                    new String[] {"background:url(", "<p>"},
				                    new int[] {"background:url(".length(), "<p>".length()},
				                    new String[] {")", "</p>"},
				                    new int[] {")".length(), "</p>".length()});
		Set resultUrl = sets[0];
		Set resultName = sets[1];
		if (resultUrl == null || resultName == null) return null;
		if (resultUrl.size() != resultName.size()) return null;
		String[] resultUrls = (String[]) resultUrl.toArray(new String[0]);
		String[] resultNames = (String[]) resultName.toArray(new String[0]);
		return resultUrls.length > 0 ? resultNames[0] + " " + resultUrls[0] : null;
	}
	
	private String getRingResult(String targetUrl) {
		Set[] sets = this.parseHtml(targetUrl,
                new String[] {"<EMBED src=\"", "<li>歌曲:", "<li>歌手:"},
                new int[] {"<EMBED src=\"".length(), "<li>歌曲:".length(), "<li>歌手:".length()},
                new String[] {"\"", "</li>", "</li>"},
                new int[] {"\"".length(), "</li>".length(), "</li>".length()});
		Set resultUrl = sets[0];
		Set resultName = sets[1];
		Set resultAuthor = sets[2];
		if (resultUrl == null || resultName == null || resultAuthor == null) return null;
		if (resultUrl.size() != resultName.size() || resultUrl.size() != resultAuthor.size()) return null;
		String[] resultUrls = (String[]) resultUrl.toArray(new String[0]);
		String[] resultNames = (String[]) resultName.toArray(new String[0]);
		String[] resultAuthors = (String[]) resultAuthor.toArray(new String[0]);
		return resultUrls.length > 0 ? resultNames[0] + " " + resultAuthors[0] + " " + resultUrls[0] : null;
	}
	
	private Set parseHtml(String url, String startString, int startStringNotIncludeNumber, String endString, int endStringNotIncdlueNumber) {
		return this.parseHtml(url, new String[] {startString}, new int[] {startStringNotIncludeNumber}, new String[] {endString}, new int[] {endStringNotIncdlueNumber})[0];
	}
	
	private Set[] parseHtml(String url, String[] startString, int[] startStringNotIncludeNumber, String[] endString, int[] endStringNotIncdlueNumber) {
		StringBuffer buffer = new StringBuffer();
		HttpClientPost httpClientPost = new HttpClientPost();
        boolean success = httpClientPost.get(url, buffer, "gb2312");
		if (!success) return null;
		
		Set[] set = new Set[startString.length];
		for (int i = 0; i < startString.length; i++) {
			// lower case
			StringBuffer lowerBuffer = new StringBuffer(buffer.toString().toLowerCase());
			startString[i] = startString[i].toLowerCase();
			endString[i] = endString[i].toLowerCase();
			
			set[i] = new LinkedHashSet();
			int fromIndex = 0;
			while (true) {
			    fromIndex = lowerBuffer.indexOf(startString[i], fromIndex);
			    if (fromIndex == -1) break;
			    int toIndex = lowerBuffer.indexOf(endString[i], fromIndex + startString[i].length());
			    if (toIndex == -1) break;
			    String targetUrl = buffer.substring(fromIndex + startStringNotIncludeNumber[i],
			    		                            toIndex + endString[i].length() - endStringNotIncdlueNumber[i]);
			    set[i].add(targetUrl);
			    fromIndex = toIndex + endString[i].length();
			}
		}
		return set;
	}
	
	private boolean saveFile(List resultList, String pathname) {
		String[] results = (String[]) resultList.toArray(new String[0]);
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		try {
			fileWriter = new FileWriter(pathname);
			bufferedWriter = new BufferedWriter(fileWriter);
			for (int i = results.length - 1; i >= 0; i-- ) {
				String lineString = results[i] + "\r\n";
				bufferedWriter.write(lineString);
			}
		    return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
			    if (bufferedWriter != null) bufferedWriter.close();
			    if (fileWriter != null) fileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
	
	private List loadUrls(String url) {
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			List list = new ArrayList();
			fileReader = new FileReader(url);
			bufferedReader = new BufferedReader(fileReader);
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				if (!line.equals("")) list.add(line);
			}
			return list;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
			    if (fileReader != null) fileReader.close();
			    if (bufferedReader != null) bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
