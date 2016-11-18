package com.rh.core.comm.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author yangjy
 * 
 */
public class LocalFileStorageHandler implements FileStorageHandler {
    private static Log log = LogFactory.getLog(LocalFileStorageHandler.class);

    @Override
    public long save(InputStream input, String path) throws IOException {
        long size = 0;
        File target = new File(path);
        if (!target.getParentFile().exists()) {
            target.getParentFile().mkdirs();
        }
        // move file to upload_file_path
        OutputStream newOut = new FileOutputStream(target);
        BufferedOutputStream buffOut = new BufferedOutputStream(newOut);
        try {
            size = IOUtils.copyLarge(input, buffOut);
        } catch (Exception e) {
            log.error("move file error.", e);
            throw new IOException(e);
        } finally {
            IOUtils.closeQuietly(buffOut);
            IOUtils.closeQuietly(newOut);
            IOUtils.closeQuietly(input);
        }
        return size;
    }

    @Override
    public boolean createNewFile(String path) throws IOException {
        File target = new File(path);
        if (!target.getParentFile().exists()) {
            target.getParentFile().mkdirs();
        }
        // 文件存在则返回false
        if (target.exists()) {
            return false;
        }

        return target.createNewFile();
    }

    @Override
    public InputStream getInputStream(String absolutePath) throws IOException {
        File file = new File(absolutePath);
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(file));
        return is;
    }

    @Override
    public OutputStream getOutputStream(String absolutePath) throws IOException {
        File target = new File(absolutePath);
        if (!target.getParentFile().exists()) {
            target.getParentFile().mkdirs();
        }
        // get from local file system
        File file = new File(absolutePath);
        OutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        return bos;
    }

    @Override
    public String[] list(String path) throws IOException {
        // get from local file system
        File target = new File(path);
        if (!target.exists()) {
            // throw new IOException(" the file not found:" + path);
            log.warn(" the path is not a file:" + path);
        }
        if (!target.isFile()) {
            log.warn(" the path is not a file:" + path);
            // throw new IOException(" the path is not a file:" + path);
        }
        return target.list();
    }

    @Override
    public boolean exists(String path) throws IOException {
        // get file from local file system
        File target = new File(path);
        return target.exists();
    }

    @Override
    public boolean deleteFile(String path) throws IOException {
        // delete from local file system
        File target = new File(path);
        if (!target.exists()) {
            // throw new IOException(" the file not found:" + path);
            log.warn(" the path can not be found:" + path);
            return false;
        }
        if (!target.isFile()) {
            log.warn(" the path is not a file:" + path);
            return false;
            // throw new IOException(" the path is not a file:" + path);
        }
        return target.delete();
    }

    @Override
    public boolean deleteDirectory(String path) throws IOException {
        // delete from local file system
        File target = new File(path);
        if (!target.exists()) {
            // throw new IOException(" the file not found:" + path);
            log.warn(" the path can not be found:" + path);
            return false;
        }
        if (!target.isDirectory()) {
            log.warn(" the path is not a file:" + path);
            return false;
            // throw new IOException(" the path is not a file:" + path);
        }
        return target.delete();
    }

    @Override
    public long getSize(String path) throws IOException {
        File target = new File(path);
        return target.length();
    }

}
