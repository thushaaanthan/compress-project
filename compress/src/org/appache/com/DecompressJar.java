package org.appache.com;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;




/**
 * Created by thushaan on 1/2/17.
 */
public class DecompressJar {
    private static final Log log = LogFactory.getLog(init.class);
    StandardFileSystemManager manager;
    public boolean unzip(String source, String destDirectory) throws FileSystemException {

        manager = init.getManager();
        boolean resultStatus = false;
        FileSystemOptions opts = init.createDefaultOptions();
        try {

            // Create remote object
            FileObject remoteFile = manager.resolveFile(source, opts);
            FileObject remoteDesFile = manager.resolveFile(destDirectory, opts);
            // File destDir = new File(destDirectory);
            if (remoteFile.exists()) {
                if (!remoteDesFile.exists()) {
                    //create a folder
                    remoteDesFile.createFolder();
                }
                //open the zip file
                JarArchiveInputStream jarIn = new JarArchiveInputStream(remoteFile.getContent().getInputStream());
                JarArchiveEntry entry = jarIn.getNextJarEntry();

                try {
                    // iterates over entries in the zip file
                    while (entry != null) {
                        // boolean testResult;
                        String filePath = destDirectory + File.separator + entry.getName();
                        // Create remote object
                        FileObject remoteFilePath = manager.resolveFile(filePath, opts);
                        if (log.isDebugEnabled()) {
                            log.debug("The created path is " + remoteFilePath.toString());
                        }
                        try {
                            if (!entry.isDirectory()) {
                                // if the entry is a file, extracts it
                                extractFile(jarIn, filePath, opts);
                            } else {
                                // if the entry is a directory, make the directory
                                remoteFilePath.createFolder();
                            }
                        } catch (IOException e) {
                            log.error("Unable to process the zip file. ", e);
                        } finally {
                            jarIn.close();
                            entry = jarIn.getNextJarEntry();

                        }
                    }
                    resultStatus = true;
                } finally {
                    //we must always close the zip file
                   jarIn.close();
                }
            } else {
                log.error("File does not exist.");
            }
        } catch (IOException e) {
            log.error("Unable to process the zip file." + e.getMessage(), e);
        } finally {
            manager.close();
        }
        return resultStatus;
    }


    private void extractFile(JarArchiveInputStream jarIn, String filePath, FileSystemOptions opts) {
        BufferedOutputStream bos = null;
        try {
            // Create remote object
            FileObject remoteFilePath = manager.resolveFile(filePath, opts);
            //open the zip file
            OutputStream fOut = remoteFilePath.getContent().getOutputStream();
            bos = new BufferedOutputStream(fOut);
            byte[] bytesIn = new byte[2048];
            int read;
            while ((read = jarIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
        } catch (IOException e) {
            log.error("Unable to read an entry: " + e.getMessage(), e);
        } finally {
            //we must always close the tar files
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    log.error("Error while closing the BufferedOutputStream: " + e.getMessage(), e);
                }
            }
        }
    }

}
