package com.rh.update.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Vector;

import java.util.zip.ZipEntry; 
import java.util.zip.ZipOutputStream;  

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.util.DateUtils;

/**
 * 将需要打包的文件，打包成一个Zip包
 * @author zotn
 *
 */
public class UpdatedPackage {
    private static Object obj = "com.rh.update.utils.UpdatedPackage";
    private String systemRealPath = "";
    public static String SQL_FILE_NAME = "database.sql";
    public static String LIST_FILE_NAME = "filelist.txt";
    private static Log log = LogFactory.getLog(UpdatedPackage.class);
    public UpdatedPackage() {
    }

    /**
     * 创建zip文件
     * @param outFile
     * @param fileList
     * @return
     */
    public boolean createZipFile(OutputStream os, ArrayList fileList) throws Exception {
        
        StringBuffer successFile = new StringBuffer();
        StringBuffer failedFile = new StringBuffer();
        Vector vFileList = new Vector();
        
        // 没找到符合条件的文件
        if (fileList.size() < 0) {
            throw new Exception("FileList Size is zero!");
            //return false;
        }
        
        //UpdateUtil.checkFile(outFile);
        ZipOutputStream zipOut = null; 
        
        try {
            zipOut = new ZipOutputStream(os);

            for (int i = 0; i < fileList.size(); i++) {
                File file = (File) fileList.get(i);
                boolean tfEncoding = false;
                //取得文件的相对路径
                String relativelyPath = getRelativelyPath(file,this.systemRealPath,tfEncoding);                
                //判断文件是否重复
                if(!vFileList.contains(relativelyPath)){
                    vFileList.add(relativelyPath);
                    
                    if (!file.exists()) {
                        failedFile.append("\\" + relativelyPath + "  : file Not Found " + "\r\n");
                    }else{
                        //增加文件到Zip包中
                        addFileToZip(zipOut, file, relativelyPath);
                        successFile.append("\\" + relativelyPath + "\r\n");
                    }
                }else{
                    failedFile.append("\\" + relativelyPath + "  : Reduplicate File Name " + "\r\n");
                }
            }
            
            addFileListToZip(zipOut,successFile,failedFile);
            return true;
        } finally{
            try{
                zipOut.close(); 
            }catch(Exception e){
                log.error(e.getMessage() ,e);
            }
        }
    }
    
    /**
     * 增加文件列表
     * @param zipOut
     * @param successFile
     * @param failedFile
     * @throws IOException
     * @throws FileNotFoundException
     */
    private void addFileListToZip(ZipOutputStream zipOut,StringBuffer successFile,StringBuffer failedFile)
        throws IOException, FileNotFoundException {

        zipOut.putNextEntry(new ZipEntry(LIST_FILE_NAME));
        
        String content = "#File Created Time: " + DateUtils.getDatetime()+"\r\n";
            
        content += successFile.toString() ;
        if(failedFile.length() > 0){
            content += "\r\n" + "#Failed File List : ";
            content += failedFile.toString() ;
        }
        zipOut.write(content.getBytes());
        zipOut.closeEntry();
    }

    /**
     * 增加文件到Zip包中
     * @param zipOut
     * @param file
     * @param relativelyPath 文件在Zip包中的文件
     * @return
     * @throws IOException
     * @throws FileNotFoundException
     */
    private void addFileToZip(ZipOutputStream zipOut, File file, String relativelyPath) throws IOException, FileNotFoundException {
        FileInputStream fIn = null;
        try{
            zipOut.putNextEntry(new ZipEntry(relativelyPath));
            fIn = new FileInputStream(file);
            byte[] bf = new byte[1024];
            int len;
            while ((len = fIn.read(bf)) >= 0) {
                zipOut.write(bf, 0, len);
            }
            //取得文件
            
            zipOut.closeEntry();
        }finally{
            try{
                fIn.close();
            }catch(Exception e){
                
            }                        
        }
    }

    /**
     * 获取传入文件以tFolder为根的相对路径
     * transformED 为真时 对文件名进行转码
     * @param file
     * @return
     */
    private String getRelativelyPath(File file, String tFolder, boolean transformED) {
        
        String rpt = file.getAbsolutePath();
        String newName = rpt.substring(tFolder.length() ); 

        if(transformED){
            String encoding = System.getProperties().getProperty("file.encoding");
            newName = TranformEncoding(newName,"GBK",encoding);
        }
        return newName;
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
            log.error( "Tranform encoding "+encoding+" to " + toEncoding + " fail!!",e);
        }
        return string;
    }

    public String getSystemRealPath() {
        return systemRealPath;
    }

    public void setSystemRealPath(String systemRealPath) {
        this.systemRealPath = systemRealPath;
    }    
}
