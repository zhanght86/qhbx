package com.rh.core.comm;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.comm.file.FileStorageHandler;
import com.rh.core.comm.file.HttpFileStorageHandler;
import com.rh.core.comm.file.LocalFileStorageHandler;
import com.rh.core.comm.file.SmbFileStorageHandler;

/**
 * @author liwei
 * 
 *         文件存储(storage)
 * 
 */
public class FileStorage {

    /** log */
    private static Log log = LogFactory.getLog(FileStorage.class);

    /** Server Message Block 用于共享例如文件、打印机、串口或者是命名管道等用于通讯的抽象对象； */
    private static final String SMB_PREFIX = "smb://";

    // //////////////////file crud////////////////////////////////

    /**
     * 保存文件 保存后会close Inputstream
     * @param input file input stream
     * @param path 文件绝对路径
     * @throws IOException 路径错误时会抛出IOException
     * @return total bytes size
     */
    public static long saveFile(InputStream input, String path) throws IOException {
        return createStorageHandler(path).save(input, path);
    }

    /**
     * 创建文件。能正常创建返回true，否则返回false
     * 
     * @param path 完整的文件路径。
     * @return 如果能创建文件，则返回true，否则返回false
     * @throws IOException - io exception
     */
    public static boolean createFile(String path) throws IOException {
        return createStorageHandler(path).createNewFile(path);
    }

    /**
     * 下载文件
     * @param absolutePath 文件绝对路径
     * @return InputStream file inputstream
     * @throws IOException file not found
     * @deprecated - 请使用getInputStream()
     */
    public static InputStream downloadFromPath(String absolutePath) throws IOException {
        return getInputStream(absolutePath);
    }

    /**
     * 获取文件输入流
     * @param absolutePath - 文件绝对路径
     * @return - output
     * @throws IOException - 文件未找到
     */
    public static InputStream getInputStream(String absolutePath) throws IOException {
        try {
            absolutePath = URLDecoder.decode(absolutePath, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            log.warn("url decode error" + e1);
        }

        return createStorageHandler(absolutePath).getInputStream(absolutePath);
    }

    /**
     * 获取文件输出流
     * @param absolutePath - 文件绝对路径
     * @return - output
     * @throws IOException - 文件未找到
     */
    public static OutputStream getOutputStream(String absolutePath) throws IOException {
        try {
            absolutePath = URLDecoder.decode(absolutePath, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            log.warn("url decode error" + e1);
        }
        return createStorageHandler(absolutePath).getOutputStream(absolutePath);
    }

    /**
     * list
     * @param path - file path
     * @return file name array
     * @throws IOException - IOException throws this exception,if the file not found or the path is a folder
     */
    public static String[] list(String path) throws IOException {
        return createStorageHandler(path).list(path);
    }

    /**
     * get File size
     * @param path - file path
     * @return size In Bytes
     * @throws IOException - IO exception
     */
    public static boolean exists(String path) throws IOException {
        return createStorageHandler(path).exists(path);
    }

    /**
     * get File size
     * @param path - file path
     * @return size In Bytes
     * @throws IOException - IO exception
     */
    // public static long getFileSize(String path) throws IOException {
    // if (path.startsWith(SMB_PREFIX)) {
    // // delete from windows share file system
    // SmbFile target = new SmbFile(path);
    // if (!target.exists()) {
    // log.warn(" the path is not a file:" + path);
    // }
    // if (!target.isFile()) {
    // log.warn(" the path is not a file:" + path);
    // // throw new IOException(" the path is not a file:" + path);
    // }
    // return target.length();
    // } else {
    // // delete from local file system
    // File target = new File(path);
    // if (!target.exists()) {
    // // throw new IOException(" the file not found:" + path);
    // log.warn(" the path is not a file:" + path);
    // }
    // if (!target.isFile()) {
    // log.warn(" the path is not a file:" + path);
    // // throw new IOException(" the path is not a file:" + path);
    // }
    // return target.length();
    // }
    //
    // }

    /**
     * delete file
     * @param path target file path
     * @return deleted ?
     * @throws IOException throws this exception
     */
    public static boolean deleteFile(String path) throws IOException {
        return createStorageHandler(path).deleteFile(path);
    }

    /**
     * delete Directory
     * @param path - target Directory path
     * @return deleted ?
     * @throws IOException throws this exception
     */
    public static boolean deleteDirectory(String path) throws IOException {
        return createStorageHandler(path).deleteDirectory(path);
    }
    
    /**
     * 
     * @param path 文件路径
     * @return 文件大大小
     * @throws IOException IO异常
     */
    public static long getSize(String path) throws IOException {
        return createStorageHandler(path).getSize(path);
    }

    /**
     * 将文件复制到本地路径，便于对文件进行其它处理。如解压、替换文件内容等
     * @param fileId 文件ID
     * @param localPath 本地路径
     * @throws IOException - io exception
     */
    public static void copyToLocal(String fileId, String localPath) throws IOException {
        Bean fileBean = FileMgr.getFile(fileId);
        String filePath = FileMgr.getAbsolutePath(fileBean.getStr("FILE_PATH"));
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = FileStorage.getInputStream(filePath);
            fos = new FileOutputStream(localPath);
            IOUtils.copyLarge(is, fos);
        } finally {
            if (is != null) {
                IOUtils.closeQuietly(is);
            }
            if (fos != null) {
                IOUtils.closeQuietly(fos);
            }
        }
    }

    /**
     * 
     * @param path 文件存储路径
     * @return FileStorageHandler 接口实现类
     */
    private static FileStorageHandler createStorageHandler(String path) {
        FileStorageHandler handler = null;
        if (path.startsWith(SMB_PREFIX)) {
            handler = new SmbFileStorageHandler();
        } else if (path.startsWith("http://")) {
            handler = new HttpFileStorageHandler();
        } else {
            handler = new LocalFileStorageHandler();
        }

        return handler;
    }

}
