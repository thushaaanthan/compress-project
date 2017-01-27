package org.appache.com;

import org.apache.commons.vfs2.FileSystemException;

/**
 * Created by vive on 1/2/17.
 */
public class demo {
	public static void main(String args[]) throws FileSystemException {
		compress_zip com = new compress_zip();
		com.fileCompress("C:/Users/TSA10/Desktop/test/new","C:/Users/TSA10/Desktop/test/New folder1.zip");

		CompressTar com1 = new CompressTar();
		com1.fileCompress("C:/Users/TSA10/Desktop/test/new","C:/Users/TSA10/Desktop/test/New folder2.tar");

		CompressJar com2 = new CompressJar();
		com2.fileCompress("C:/Users/TSA10/Desktop/test/new","C:/Users/TSA10/Desktop/test/New folder3.jar");

		CompressCpio com3 = new CompressCpio();
		com3.fileCompress("C:/Users/TSA10/Desktop/test/new","C:/Users/TSA10/Desktop/test/New folder4.cpio");



		DecompressZip decom = new DecompressZip();
		decom.unzip("C:/Users/TSA10/Desktop/test/New folder1.zip","C:/Users/TSA10/Desktop/test/New folder1");

		DecompressTar decom1 = new DecompressTar();
		decom1.unzip("C:/Users/TSA10/Desktop/test/New folder2.tar","C:/Users/TSA10/Desktop/test/New folder2");

		DecompressJar decom2 = new DecompressJar();
		decom2.unzip("C:/Users/TSA10/Desktop/test/New folder3.jar","C:/Users/TSA10/Desktop/test/New folder3");

		DecompressCpio decom3 = new DecompressCpio();
		decom3.unzip("C:/Users/TSA10/Desktop/test/New folder4.cpno","C:/Users/TSA10/Desktop/test/New folder4");


	}
}

