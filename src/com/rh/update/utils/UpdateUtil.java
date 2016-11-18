package com.rh.update.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.TipException;

/**
 * 处理程序更新常用的类
 * @author Tanyh 20151105
 *
 */
public class UpdateUtil {

    public UpdateUtil() {
        super();
    }

    private static Object obj = UpdateUtil.class;

    private static Log log = LogFactory.getLog(UpdateUtil.class);


    /**
     * 复制单个文件到目标文件夹
     * @param filePath 原文件路径
     * @param toFolder 目的文件路径
     */
    public static void copyFile(String filePath,String toFolder)throws FileNotFoundException,IOException {
        
        File file = new File(filePath);
        File destFile = new File(toFolder);
        createDir(destFile.getParentFile().getAbsolutePath());
        
        if(file.exists()){
            copyFile1(filePath,toFolder);  
        }else{
            throw new FileNotFoundException("Not found file ["+filePath+"]");
        }
    }
    
    /**
     * 拷贝文件
     *
     * @param srcFile 源文件
     * @param dstFile 目标文件
     * @throws IOException
     */
    private static void copyFile1(String srcFile, String dstFile) throws IOException {
        FileInputStream inp = null;
        FileOutputStream out = null;
        try {
            inp = new FileInputStream(srcFile) ;
            out = new FileOutputStream(dstFile) ;
            byte[] buff = new byte[8192];
            int count;
            while ((count = inp.read(buff)) != -1) {
                out.write(buff, 0, count);
            }
        } finally {
            if (out != null) {
                out.close();
            }
            if (inp != null) {
                inp.close();
            }
        }
    }    



    /**
     * 检测目录，如果不存在则创建
     * 
     * @param file file对象，可以是目录，也可以是文件。
     * @throws IOException
     */
    public static String checkFile(File file) throws IOException {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        return file.getAbsolutePath();
    }
    
    /**
     * 转码
     * @param string
     * @param encoding
     * @param toEncoding
     * @return
     */
    public static String TranformEncoding(String string,String encoding,String toEncoding) {
        if(!encoding.equalsIgnoreCase(toEncoding))
        try {
            string = new String(string.getBytes(encoding), toEncoding);
        } catch (IOException e) {
            log.error("Tranform encoding "+encoding+" to " + toEncoding + " fail!!",e);
        }
        return string;
    }
    
    public static void createDir(String strPath){
        File file = new File(strPath);
        if(!file.exists()){
            boolean rtn = file.mkdirs();
        }
    }
    
    /**
     * 读文件内容
     * @param filename 文件名
     * @return 文件内容
     * @throws ApplicationException
     */
    public static String readFromFile( String filename )
        throws TipException {

        String tmp, infile = new String();
        BufferedReader filehandle =null;
        try {
            File file = new File( filename );
            if( file.exists() ) {
                filehandle = new BufferedReader( new FileReader(file) );
                while( ( tmp = filehandle.readLine() ) != null ) {
                    infile += tmp + "\n" ;
                }
            } else infile = "";
        } catch( FileNotFoundException e ) {
            throw new TipException("找不到文件路径");
        } catch(IOException e) {
        } finally {
            if (filehandle != null) {
                try {
                    filehandle.close();
                } catch (IOException ioe) {
                    throw new TipException("file closed fail");
                }
            }
        }

        return infile;

    }    
}
