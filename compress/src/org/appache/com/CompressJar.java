package org.appache.com;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by TSA10 on 1/2/17.
 */


public class CompressJar {
    private static final Log log = LogFactory.getLog(init.class);
    private final byte[] bytes = new byte[2048];

    public void fileCompress( String source, String destination) throws FileSystemException {
        boolean resultStatus = false;
        StandardFileSystemManager manager;
        FileSystemOptions opts = init.createDefaultOptions();
        try {
            manager = init.getManager();
            FileObject fileObj = manager.resolveFile(source, opts);
            FileObject destObj = manager.resolveFile(destination, opts);
            if (fileObj.exists()) {
                if (fileObj.getType() == FileType.FOLDER) {
                    List<FileObject> fileList = new ArrayList<FileObject>();
                    getAllFiles(fileObj, fileList);
                    writeZipFiles(fileObj, destObj, fileList);
                } else {
                    JarArchiveOutputStream outputStream = null;
                    InputStream fileIn = null;
                    try {
                        outputStream = new JarArchiveOutputStream(destObj.getContent().getOutputStream());
                        fileIn = fileObj.getContent().getInputStream();
                        JarArchiveEntry JarArchiveEntry = new JarArchiveEntry(fileObj.getName().getBaseName());
                        outputStream.putArchiveEntry(JarArchiveEntry);

                        int length;
                        while ((length = fileIn.read(bytes)) != -1) {
                            outputStream.write(bytes, 0, length);
                        }
                    } catch (Exception e) {
                        log.error("Unable to compress a file." + e.getMessage());
                    } finally {
                        try {
                            if (outputStream != null) {
                                outputStream.close();
                            }
                        } catch (IOException e) {
                            log.error("Error while closing ZipOutputStream: " + e.getMessage(), e);
                        }
                        try {
                            if (fileIn != null) {
                                fileIn.close();
                            }
                        } catch (IOException e) {
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
    private void writeZipFiles(FileObject fileObj, FileObject directoryToZip,
                               List<FileObject> fileList)
            throws IOException {
        JarArchiveOutputStream zos = null;
        try {
            zos = new JarArchiveOutputStream(directoryToZip.getContent().getOutputStream());
            for (FileObject file : fileList) {
                if (file.getType() == FileType.FILE) {
                    addToZip(fileObj, file, zos);
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
    private void addToZip(FileObject fileObject, FileObject file, JarArchiveOutputStream outputStream) {
        InputStream fin = null;
        try {
            fin = file.getContent().getInputStream();
            String name = file.getName().toString();
            String entry = name.substring(fileObject.getName().toString().length() + 1,
                    name.length());
            JarArchiveEntry JarArchiveEntry = new JarArchiveEntry(entry);
            outputStream.putArchiveEntry(JarArchiveEntry);


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