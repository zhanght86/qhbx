package com.rh.update.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;

import java.util.zip.ZipEntry; 
import java.util.zip.ZipFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Context;
import com.rh.core.util.DateUtils;


/**
 * 打开更新的程序包，并将包中的新程序更新到指定的目录。
 * 
 * @author zotn
 * 
 */
public class FileUpdate {

    // 更新文件的临时目录
    private static String updateTmplPath = Context.appStr(Context.APP.WEBINF)
            + File.separator + "update" + File.separator;

    private static Object obj = new FileUpdate();

    private static Log log = LogFactory.getLog(FileUpdate.class);


    private String getTimpStamp() {
        String nowTime = DateUtils.getDatetime();
        nowTime = nowTime.replaceAll("-", "");
        nowTime = nowTime.replaceAll(":", "");
        nowTime = nowTime.replaceAll(" ", "");

        return nowTime;
    }
    
    /**
     * 更新文件，步骤: 
     * 1.解压包中的所有文件放到"/public-html/WEB-INF/update/nowtime/*"目录下
     * 2.备份原有的程序文件，将原程序的文件改成"旧文件名+.日期" ，如"test.jsp.20060522"。
     * 3.复制解压包中的新程序文件到对应旧文件所在目录。
     * 
     * @param encoding
     * @param filelist
     *            需要更新的文件名称
     * @param fileMap
     *            需要更新的文件列表
     */
    public UpdateResultList update(String zipPath, String[] filelist,
            HashMap fileMap) throws IOException, FileNotFoundException {
        
        UpdateResultList resultList = new UpdateResultList();
        String nowTime = this.getTimpStamp();

        File zipFile = new File(zipPath);

        // 存放更新源文件的目录
        String tempPath = updateTmplPath + nowTime + File.separator;

        // 存放备份的旧文件目录
        String backupFolder = tempPath + "backup" + File.separator;
        
        // 正在运行的程序存放的目录。
        String workFolder = Context.appStr(Context.APP.SYSPATH);

        // 解压文件
        unZipFile(zipFile, fileMap, tempPath);

        for (int i = 0; i < filelist.length; i++) {
                // 被更新文件在系统中的相对路径
                String fileRelativePath = filelist[i];
                //将文件路径改成Unix下的路径
                fileRelativePath = convertToSysFilePath(fileRelativePath);
                
                //被更新文件的完整路径
                String srcFile = workFolder + fileRelativePath;
                //备份文件完整路径
                String backupFileName = backupFolder + fileRelativePath;
                
                //备份原文件
                resultList.addElement(backupOldFile(srcFile, backupFileName));
                
                UpdateResultElement element = new UpdateResultElement();
                resultList.addElement(element);
                element.setItemName(srcFile);
                element.setOperationName(" update file ");
    
                //复制解包后的新程序文件，到老程序文件对应的目录中
                String strUpdateFile = tempPath + fileRelativePath;
                UpdateUtil.copyFile(strUpdateFile, srcFile);
                element.setItemResult(" success ");
            
        }
        resultList.writeToFile( new File(tempPath + "update.log"));
        return resultList;
    }

    
    /**
     * 备份系统原有程序文件
     * @param resultList
     * @param srcFile
     * @param backupFileName
     * @throws IOException
     */
    private UpdateResultElement backupOldFile( String srcFile, String backupFileName) 
        throws IOException {
        UpdateResultElement element = new UpdateResultElement();
        element.setItemName(srcFile);
        element.setOperationName(" backup org file ");
        try {
            // 备份原来的文件到指定目录中
            UpdateUtil.copyFile(srcFile, backupFileName);
            
            // 重命名需要被更新的文件
            renameFile(srcFile);
            element.setItemResult(" success ");
        } catch (FileNotFoundException e) {
            element.setItemResult("File not Found");
        }
        
        return element;
    }

    /**
     *  重命名文件
     * 
     * @param srcFile
     */
    private void renameFile(String srcFile) {
        String timeStamp = this.getTimpStamp();
        timeStamp = timeStamp.substring(0, 8);
        File aFile = new File(srcFile);
        String fileName = aFile.getName() + "." + timeStamp;
        File destFile = new File(aFile.getParent() + File.separator + fileName);
        aFile.renameTo(destFile);
    }

    /**
     * 从file里解包fileMap中包含的文件到target地址
     * 
     * @param file
     * @param fileMap
     * @param target
     * @return
     */
    private static void unZipFile(File file, HashMap fileMap, String target)
            throws FileNotFoundException, IOException {
        upZipFileByList(file, fileMap, target);
    }

    /**
     * 解压压缩包中指定的程序文件
     * 
     * @param file
     *            zip文件的路径
     * @param fileMap
     *            需要解包的文件
     * @param target
     *            目的路径，相对路径
     */
    private static void upZipFileByList(File file, HashMap fileMap,
            String target) throws FileNotFoundException, IOException {

        if (!target.endsWith(File.separator)) {
            target = target + File.separator;
        }

        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(file);
            // 取出Zip包中包含的所有文件项
            Enumeration em = zipFile.entries();
            while (em.hasMoreElements()) {
                ZipEntry ze = (ZipEntry) em.nextElement();
                String zeName = ze.getName();
                // 将文件路径改成Unix下的路径
                zeName = convertToSysFilePath(zeName);
                if (ze.isDirectory()) {
                    UpdateUtil.checkFile(new File(zeName));
                } else {
                    if (fileMap == null || fileMap.containsKey(ze.getName())) {
                        UpdateUtil.checkFile(new File(target + zeName));
                        InputStream in = null;
                        FileOutputStream out = null;
                        try {
                            in = zipFile.getInputStream(ze);
                            out = new FileOutputStream(target + zeName);
                            byte[] bf = new byte[8192];
                            int len;
                            while ((len = in.read(bf)) >= 0) {
                                out.write(bf, 0, len);
                            }
                        } finally {
                            try {
                                if (in != null) {
                                    in.close();
                                }
                            } catch (Exception e) {
                                log.error(e.getMessage(), e);
                            }
                            try {
                                if (out != null) {
                                    out.close();
                                }
                            } catch (Exception e) {
                                log.error(e.getMessage(), e);
                            }
                        }
                    }
                }
            }

        } finally {
            if (zipFile != null) {
                zipFile.close();
            }
        }
    }

    /**
     * 将不符合Unix系统的路径改成符合Unix操作系统的路径
     * 
     * @param strSrc
     * @return
     */
    private static String convertToSysFilePath(String strSrc) {
        char a = 92;
        char b = File.separatorChar;
        return strSrc.replace(a, b);
    }
}
