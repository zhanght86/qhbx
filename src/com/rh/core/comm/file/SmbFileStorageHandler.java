package com.rh.core.comm.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author yangjy
 *
 */
public class SmbFileStorageHandler implements FileStorageHandler {
    private static Log log = LogFactory.getLog(SmbFileStorageHandler.class);
    
    @Override
    public long save(InputStream input, String path) throws IOException {
        long size = -1;
        // save to windows share file system
        SmbFile target = new SmbFile(path);
        SmbFile parent = new SmbFile(target.getParent());
        if (!parent.exists()) {
            parent.mkdirs();
        }
        OutputStream os = target.getOutputStream();
        OutputStream bos = new BufferedOutputStream(os);
        try {
            size = IOUtils.copyLarge(input, bos);
        } catch (Exception e) {
            log.error("copy file error.", e);
            throw new IOException(e);
        } finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(bos);
            IOUtils.closeQuietly(os);
        }
        
        return size;
    }

    @Override
    public boolean createNewFile(String path) throws IOException {
        // save to windows share file system
        SmbFile target = new SmbFile(path);
        SmbFile parent = new SmbFile(target.getParent());
        if (!parent.exists()) {
            parent.mkdirs();
        }
        // 文件存在，则返回false
        if (target.exists()) {
            return false;
        }

        try {
            // 创建文件成功，则返回true
            target.createNewFile();
            return true;
        } catch (Exception e) {
            // 创建文件失败，返回false
            return false;
        }
    }

    @Override
    public InputStream getInputStream(String absolutePath) throws IOException {
        // get from windows share file system
        InputStream in = null;
        try {
            in = new BufferedInputStream(new SmbFileInputStream(absolutePath));
        } catch (Exception e) {
            throw new FileNotFoundException(absolutePath);
        }
        return in;
    }

    @Override
    public OutputStream getOutputStream(String absolutePath) throws IOException {
        OutputStream bos = null;
        try {
            SmbFile target = new SmbFile(absolutePath);
            SmbFile parent = new SmbFile(target.getParent());
            if (!parent.exists()) {
                parent.mkdirs();
            }
            bos = new BufferedOutputStream(new SmbFileOutputStream(absolutePath));
        } catch (Exception e) {
            throw new FileNotFoundException(absolutePath);
        }
        return bos;
    }

    @Override
    public String[] list(String path) throws IOException {
        // get from windows share file system
        SmbFile target = new SmbFile(path);
        if (!target.exists()) {
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
        // get file from windows share file system
        SmbFile target = new SmbFile(path);
        return target.exists();
    }

    @Override
    public boolean deleteFile(String path) throws IOException {
        // delete from windows share file system
        SmbFile target = new SmbFile(path);
        if (!target.exists()) {
            log.warn(" the path can not be found:" + path);
            return false;
        }
        if (!target.isFile()) {
            log.warn(" the path is not a file:" + path);
            return false;
        }
        target.delete();
        return true;
    }

    @Override
    public boolean deleteDirectory(String path) throws IOException {
        // delete from windows share file system
        SmbFile target = new SmbFile(path);
        if (!target.exists()) {
            log.warn(" the path can not be found:" + path);
            return false;
        }
        if (!target.isDirectory()) {
            log.warn(" the path is not a file:" + path);
            return false;
        }
        target.delete();
        return true;
    }
    
    @Override
    public long getSize(String path) throws IOException {
        SmbFile target = new SmbFile(path);
        return target.length();
    }    

}
