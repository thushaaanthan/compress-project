package org.appache.com;

import org.apache.commons.vfs2.FileSystemException;

/**
 * Created by TSA10 on 1/2/17.
 */
public class demo {



	public void compress(String source, String destination, String file_type) throws FileSystemException {
		if (file_type=="zip")
		{
			compress_zip com = new compress_zip();
			com.fileCompress(source,destination);

		}

		else if(file_type=="tar")
		{
			CompressTar com1 = new CompressTar();
			com1.fileCompress(source,destination);
		}

		else if(file_type=="jar")
		{
			CompressJar com2 = new CompressJar();
			com2.fileCompress(source,destination);
		}
		else if(file_type=="cpio")
		{
			CompressCpio com3 = new CompressCpio();
			com3.fileCompress(source,destination);
		}


	}


	public void decompress(String source, String destination, String file_type) throws FileSystemException {
		if (file_type=="zip")
		{
			DecompressZip decom = new DecompressZip();
			decom.unzip(source,destination);

		}

		else if(file_type=="jar")
		{
			DecompressJar decom = new DecompressJar();
			decom.unJar(source,destination);
		}

		else if(file_type=="tar")
		{
			DecompressTar decom1 = new DecompressTar();
			decom1.unTar(source,destination);
		}
		else if(file_type=="cpio")
		{
			DecompressCpio decom2 = new DecompressCpio();
			decom2.unCpio(source,destination);
		}


	}








	public static void main(String args[]) throws FileSystemException {

		demo comp=new demo();
			comp.compress("C:/Users/TSA10/Desktop/test/compress","C:/Users/TSA10/Desktop/test/compressfi.zip","zip");

		demo decomp=new demo();
		decomp.decompress("C:/Users/TSA10/Desktop/test/compressfi.zip","C:/Users/TSA10/Desktop/test/compressfi","zip");


	}
}

