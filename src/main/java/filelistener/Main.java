package filelistener;

//2、common-io实现的文件夹变化的监听

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.log4j.Logger;

import init.InitConfig;

public class Main {

	private Logger logger = Logger.getLogger(Main.class);
	
	public static void main(String[] args) throws Exception {
		InitConfig.init();
		Main ma = new Main();
		ma.test();
	}

	public void test() throws Exception {
		String filePath = InitConfig.ADDRESS;
		FileFilter filter = FileFilterUtils.and(new MyFileFilter());
		FileAlterationObserver filealtertionObserver = new FileAlterationObserver(filePath, filter);
		filealtertionObserver.addListener(new FileAlterationListenerAdaptor() {

			@Override
			public void onDirectoryCreate(File directory) {
				logger.info("DirectoryCreate");
			}

			@Override
			public void onDirectoryDelete(File directory) {
				logger.info("DirectoryDelete");
			}

			@Override
			public void onFileChange(File file) {
				logger.info("FileChange");
			}

			@Override
			public void onFileCreate(File file) {
				logger.info("onFileCreate" + file.getAbsoluteFile());
				if (file.getName().endsWith("-1.wav")) {
					String substring = file.getPath().substring(0, file.getPath().indexOf("-")) + ".wav";
					logger.info(substring);
					new File(substring).delete();
					logger.info("file " + substring + " delete!");
					file.renameTo(new File(substring));
					logger.info("file " + file.getAbsolutePath() + " rename to" + substring);
				}
			}

			@Override
			public void onFileDelete(File file) {
				logger.info("onFileDelete" + file.getAbsoluteFile());
			}

			@Override
			public void onStart(FileAlterationObserver observer) {
				logger.info("onStart");
			}

		});

		FileAlterationMonitor filealterationMonitor = new FileAlterationMonitor(6000);
		filealterationMonitor.addObserver(filealtertionObserver);
		filealterationMonitor.start();
	}

}

class MyFileFilter implements IOFileFilter {

	@Override
	public boolean accept(File file) {
		/*
		 * String extension=FilenameUtils.getExtension(file.getAbsolutePath());
		 * if(extension!=null&&extension.equals("txt")){ return true; } return false;
		 */
		return true;
	}

	@Override
	public boolean accept(File dir, String name) {
		return true;
	}

}
