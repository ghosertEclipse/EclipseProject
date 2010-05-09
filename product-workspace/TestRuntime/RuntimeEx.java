import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RuntimeEx {
	public static void main(String args[]) {
		
		// String command = "cmd /c dir";
		// String command = "javac";
		// String command = "cmd /c start notepad.exe";
		String command = "cmd /c tree c:";
		
		String resultString = executeCommand(command);
		System.out.println(resultString);
	}
	
	/**
	 * Execute the command given and return the result string, null if exception happens.
	 * @param command eg. "cmd /c dir"; "javac"; "cmd /c start http://www.google.com"
	 * @return result string.
	 */
	public static String executeCommand(String command) {
		try {
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(command);
			
			StringBuffer outputContent = new StringBuffer();
			// any error message?
			InputStream errorInputStream = proc.getErrorStream();
			// any output?
			InputStream inputStream = proc.getInputStream();
			
			new RuntimeEx().startGettingContent(errorInputStream, outputContent);
			
			new RuntimeEx().startGettingContent(inputStream, outputContent);

			// any error?
			proc.waitFor();
			
			return outputContent.toString();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}
	
	private void startGettingContent(final InputStream is, final StringBuffer outputContent) {
		new Thread() {
			public void run() {
				try {
					InputStreamReader isr = new InputStreamReader(is);
					BufferedReader br = new BufferedReader(isr);
					String line = null;
					while ((line = br.readLine()) != null) {
						outputContent.append(line + "\n");
					}
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}.start();
	}
}
