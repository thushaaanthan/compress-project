package org.appache.com;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder;
import org.apache.commons.vfs2.provider.ftps.FtpsFileSystemConfigBuilder;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;

/**
 * Created by vive on 1/2/17.
 */
public class init {
	private static final Log log = LogFactory.getLog(init.class);
	public static FileSystemOptions createDefaultOptions() throws FileSystemException {
		FileSystemOptions opts = new FileSystemOptions();

		// SSH Key checking
		SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");

		// Root directory set to user home
		SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, false);

		// Timeout is count by Milliseconds

		SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 100000);

		FtpFileSystemConfigBuilder.getInstance().setPassiveMode(opts, true);

		FtpFileSystemConfigBuilder.getInstance().setSoTimeout(opts, 100000);

		FtpsFileSystemConfigBuilder.getInstance().setPassiveMode(opts, true);

		return opts;

	}
	public static StandardFileSystemManager getManager() {
		StandardFileSystemManager fsm = null;
		try {
			fsm = new StandardFileSystemManager();
			fsm.init();
		} catch (FileSystemException e) {
			log.error("Unable to get FileSystemManager: " + e.getMessage(), e);
		}
		return fsm;
	}
}
