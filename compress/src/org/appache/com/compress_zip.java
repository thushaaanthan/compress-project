package org.appache.com;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by TSA10 on 1/2/17.
 */


public class compress_zip {
	private static final Log log = LogFactory.getLog(init.class);
	private final byte[] bytes = new byte[2048];

	public void fileCompress( String source, String destination) throws FileSystemException {
		boolean resultStatus = false;
		StandardFileSystemManager manager;/* create a manager for manage the files*/
		FileSystemOptions opts = init.createDefaultOptions(); /* configure the commons vfs is working */
		try {
			manager = init.getManager();
			FileObject fileObj = manager.resolveFile(source, opts);/* create remote object for source */
			FileObject destObj = manager.resolveFile(destination, opts);/* create remote object destination*/
			if (fileObj.exists()) {/* if it is a file then do it */
				if (fileObj.getType() == FileType.FOLDER) {/* check wether the file type is folder or not */
					List<FileObject> fileList = new ArrayList<FileObject>();/*initalise array list */
					getAllFiles(fileObj, fileList); /*get the all files from the array list */
					writeZipFiles(fileObj, destObj, fileList); /* call the write zip file */
				} else { /*if the file type is not a folder and it is a normal file*/
					ZipArchiveOutputStream outputStream = null;
					InputStream fileIn = null;

						/*adding the file into the zip folder */
					try {
						outputStream = new ZipArchiveOutputStream(destObj.getContent().getOutputStream());
						fileIn = fileObj.getContent().getInputStream();
						ZipArchiveEntry zipEntry = new ZipArchiveEntry(fileObj.getName().getBaseName());
						outputStream.putArchiveEntry(zipEntry);
						//NextEntry(zipEntry);
						int length;
						while ((length = fileIn.read(bytes)) != -1) {
							outputStream.write(bytes, 0, length);
						}
					} catch (Exception e) { /*any problem will occour while compression */
						log.error("Unable to compress a file." + e.getMessage());
					} finally {
						try {
							if (outputStream != null) {
								outputStream.close();
							}
						} catch (IOException e) {/*any problem occour in ZipOutputStream */
							log.error("Error while closing ZipOutputStream: " + e.getMessage(), e);
						}
						try {
							if (fileIn != null) {
								fileIn.close();
							}
						} catch (IOException e) {/*any problem occour in ZipOInputStream */
							log.error("Error while closing InputStream: " + e.getMessage(), e);
						}
						manager.close();
					}
				}
				resultStatus = true;

				if (log.isDebugEnabled()) {
					log.debug("File archiving completed." + destination);
				}
			} else {
				log.error("The File location does not exist.");
				resultStatus = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void getAllFiles(FileObject dir, List<FileObject> fileList) {
		try {
			/* if there any sub folder with in the main folder then get the folder */
			FileObject[] children = dir.getChildren();
			for (FileObject child : children) {
				fileList.add(child);
				if (child.getType() == FileType.FOLDER) {
					getAllFiles(child, fileList);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*write the folder and files into the copressed file */
	private void writeZipFiles(FileObject fileObj, FileObject directoryToZip, List<FileObject> fileList) throws IOException {
		ZipArchiveOutputStream zos = null;
		try {
			zos = new ZipArchiveOutputStream(directoryToZip.getContent().getOutputStream());
			for (FileObject file : fileList) {
				if (file.getType() == FileType.FILE) {
					addToZip(fileObj, file, zos); /*call the addtozip method for add the file into zip */
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (zos != null) {
				zos.close();
			}
		}
	}
	/*adding the each and every file into the zip file */
	private void addToZip(FileObject fileObject, FileObject file, ZipArchiveOutputStream outputStream) {
		InputStream fin = null;
		try {
			fin = file.getContent().getInputStream();
			String name = file.getName().toString(); /*create the files within the zip file like in the main folder */
			String entry = name.substring(fileObject.getName().toString().length() + 1,
					name.length());
			ZipArchiveEntry zipEntry = new ZipArchiveEntry(entry);
			outputStream.putArchiveEntry(zipEntry);


			int length;
			while ((length = fin.read(bytes)) != -1) {
				outputStream.write(bytes, 0, length);
			}
		} catch (IOException e) {
			log.error("Unable to add a file into the zip file directory." + e.getMessage());
		} finally {
			try {
				outputStream.closeArchiveEntry();
				if (fin != null) {
					fin.close();
				}
			} catch (IOException e) {
				log.error("Error while closing InputStream: " + e.getMessage(), e);
			}
		}
	}
}