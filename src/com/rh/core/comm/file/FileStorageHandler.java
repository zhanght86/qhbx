package com.rh.core.comm.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 
 * 文件处理接口
 * @author yangjy
 * 
 */
public interface FileStorageHandler {
    /**
     * 保存文件 保存后会close Inputstream
     * @param input file input stream
     * @param path 文件绝对路径
     * @throws IOException 路径错误时会抛出IOException
     * @return total bytes size
     */
    long save(InputStream input, String path) throws IOException;

    /**
     * 创建文件。能正常创建返回true，否则返回false
     * 
     * @param path 完整的文件路径。
     * @return 如果能创建文件，则返回true，否则返回false
     * @throws IOException - io exception
     */
    boolean createNewFile(String path) throws IOException;

    /**
     * 获取文件输入流，相对于下载文件。
     * @param absolutePath - 文件绝对路径
     * @return - output
     * @throws IOException - 文件未找到
     */
    InputStream getInputStream(String absolutePath) throws IOException;

    /**
     * 获取文件输出流
     * @param absolutePath - 文件绝对路径
     * @return - output
     * @throws IOException - 文件未找到
     */
    OutputStream getOutputStream(String absolutePath) throws IOException;

    /**
     * 列出指定目录下的文件
     * @param path 文件路径
     * @return 文件名数组
     * @throws IOException 如果path是一个文件，或者目录不能存在则抛此错误。
     */
    String[] list(String path) throws IOException;

    /**
     * 获取文件尺寸
     * @param path 文件路径
     * @return 文件字节数
     * @throws IOException IO异常
     */
    boolean exists(String path) throws IOException;

    /**
     * 删除文件
     * @param path 目标文件路径
     * @return 是否删除成功
     * @throws IOException IO异常
     */
    boolean deleteFile(String path) throws IOException;

    /**
     * 删除目录
     * @param path - 目标目录路径
     * @return 是否删除成功
     * @throws IOException IO异常
     */
    boolean deleteDirectory(String path) throws IOException;
    
    /**
     * 
     * @param path 目标目录路径
     * @return 文件大小
     * @throws IOException IO异常
     */
    long getSize(String path) throws IOException;

}
