package org.appache.com;

import org.apache.commons.vfs2.FileSystemException;

/**
 * Created by vive on 1/2/17.
 */
public class demo {
	public static void main(String args[]) throws FileSystemException {
		compress com = new compress();
		Decompress decom = new Decompress();
		com.fileCompress("/home/thushaan/kkk","/home/thushaan/Desktop/payload.zip");
		decom.unzip("/home/thushaan/Desktop/payload.zip","/home/thushaan/Desktop/payload001");

	}
}

