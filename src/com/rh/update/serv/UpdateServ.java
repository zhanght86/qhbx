package com.rh.update.serv;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.comm.FileMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.util.DateUtils;
import com.rh.core.util.RequestUtils;
import com.rh.update.utils.FileSearcher;
import com.rh.update.utils.FileUpdate;
import com.rh.update.utils.UpdateResultList;
import com.rh.update.utils.UpdatedPackage;
/**
 * 系统程序更新、打包服务类
 * @author Tanyh20151030
 *
 */
public class UpdateServ extends CommonServ{

    /**
     * 根据时间，搜索指定时间段内更新的文件
     * @param paramBean 查询对象
     * @return outBean 返回结果
     */
    public OutBean searchFile(ParamBean paramBean) {
        OutBean outBean = new OutBean();
        ArrayList fileList = new ArrayList();
        //项目根路径
        String realPath = Context.appStr(Context.APP.SYSPATH);
        //获取手动输入的文件列表
        String fileListStr = paramBean.getStr("UPDATE_FILE_LIST");
        if (fileListStr.length() <= 0) {//根据时间搜索文件
            //开始时间
            String startTime = paramBean.getStr("UPDATE_BEGIN_DATE");
            if (startTime.length() <= 0) {
                //开始时间为空，默认当前时间
                startTime = DateUtils.getDate();
            }
            //结束时间
            String endTime = paramBean.getStr("UPDATE_END_DATE");
            if (endTime.length() <= 0) {
                //结束时间为空，默认当前时间
                endTime = DateUtils.getDate();
            }
            //加上时间点
            startTime += " 00:00:00";
            endTime += " 23:59:59";
            Date startDate = new Date(System.currentTimeMillis());
            Date endDate = new Date(System.currentTimeMillis());
            startDate = DateUtils.getDateFromString(startTime);
            endDate = DateUtils.getDateFromString(endTime);
            //前端页面文件
            String[] pageFileExt = {".jsp",".js",".ftl"};
            String[] javaFileExt = {".java"};
            //初始化搜索对象
            FileSearcher fileSearcher = new FileSearcher();
            fileSearcher.addExcludeFolder(realPath + "WEB-INF");
            //获取更新了的前端页面文件列表
            ArrayList pageFileList = fileSearcher.SearchFile(new File(realPath), startDate, endDate, pageFileExt);
            // 获取Java文件列表
            ArrayList javaFileList = fileSearcher.SearchFile(new File(realPath + ".." + File.separator + "src"),
                    startDate, endDate, javaFileExt);
            ArrayList classFileList = new ArrayList();
            //根据Java文件获取对应的class文件
            if (javaFileList != null && javaFileList.size() > 0) {
                for (int i = 0; i < javaFileList.size(); i ++) {
                    File javaFile = (File) javaFileList.get(i);
                    String fileName = javaFile.getPath();
                    String indexStr = File.separator + "src";
                    if (fileName.indexOf(indexStr) >= 0) {
                        String classFileName = realPath + "WEB-INF" + File.separator + "classes" + fileName.substring(fileName.indexOf(indexStr) + indexStr.length());
                        classFileName = classFileName.substring(0, classFileName.length()-4) + "class";
                        File classFile = new File(classFileName);
                        if (classFile.exists()) {
                            classFileList.add(classFile);
                        }
                    }
                }
            }
            fileList.addAll(pageFileList);
            fileList.addAll(javaFileList);
            fileList.addAll(classFileList);
        } else {
            //根据手动输入的文件列表，搜索文件
            String[] fileLists = fileListStr.split("\n");
            for(int i=0;i<fileLists.length;i++){
                String strTemp = fileLists[i].trim() ;
                strTemp = realPath + strTemp; 
                if(strTemp.length() > 0){
                    File file = new File(strTemp);
                    fileList.add(file);
                }
            }
        }
        outBean.set("FILE_LIST", fileList);
        outBean.setToDispatcher("/update/showSearchResult.jsp");
        return outBean;
    }
    
    /**
     * 打包程序文件
     * @param paramBean 参数对象
     * @return OutBean返回对象
     */
    public OutBean doPackage(ParamBean paramBean){
        OutBean outBean = new OutBean();
        HttpServletRequest req = Context.getRequest();
        //获取待打包文件列表
        String fileList[] = req.getParameterValues("fileList");
        ArrayList list = new ArrayList();
        for(int i=0;i<fileList.length;i++){
            File file = new File(fileList[i]);
            list.add(file);
        }
        //项目根路径
        String realPath = Context.appStr(Context.APP.SYSPATH);
        UpdatedPackage zipFile = new UpdatedPackage();
        zipFile.setSystemRealPath(realPath);
        String zipName = "package_" + DateUtils.getDate().replaceAll("-", "") + ".zip";
        HttpServletResponse res = Context.getResponse();
        res.resetBuffer();
        res.setContentType("application/x-download");
        RequestUtils.setDownFileName(req, res, zipName);
        try {
            zipFile.createZipFile(res.getOutputStream(),list);
        } catch (IOException e) {
            throw new TipException(e.getMessage());
        } catch (Exception e) {
            throw new TipException(e.getMessage());
        } finally {
            try {
                res.flushBuffer();
            } catch (Exception e) {
                throw new TipException(e.getMessage());
            }
        }
        return outBean;
    }
    
    /**
     * 显示待更新程序文件
     * @param paramBean 参数对象
     * @return 返回结果对象
     */
    public OutBean showUpdateFile(ParamBean paramBean) {
        OutBean outBean = new OutBean();
        //获取程序包文件ID
        String fileId = paramBean.getStr("FILE_ID");
        if (fileId.length() <= 0) {
            throw new TipException("未传递文件ID");
        }
        Bean zipBean = FileMgr.getFile(fileId);
        //程序包真实路径
        String zipPath = zipBean.getStr("FILE_PATH");
        if (zipPath.length() > 0) {
            zipPath = zipPath.replace("@SYS_FILE_PATH@", FileMgr.getRootPath());
        } else {
            throw new TipException("未找到更新包");
        }
        ArrayList fileList = new ArrayList();
        File file = new File(zipPath);
        ZipFile zf;
        try {
            zf = new ZipFile(file);
            Enumeration em = zf.entries();
            while (em.hasMoreElements()) {
                //依次从zip包中读取出文件名
                ZipEntry zipEntry = (ZipEntry) em.nextElement();
                fileList.add(zipEntry.getName());
            }
        } catch (ZipException e) {
            log.error(e.getMessage());
            throw new TipException(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new TipException(e.getMessage());
        }
        outBean.set("FILE_LIST", fileList);
        outBean.set("FILE_ID", fileId);
        outBean.setToDispatcher("/update/showUpdateFileList.jsp");
        return outBean;
    }
    
    /**
     * 更新程序文件
     * @param paramBean 参数对象
     * @return OutBean 返回结果
     */
    public OutBean doUpdate(ParamBean paramBean) {
        OutBean outBean = new OutBean();
        HttpServletRequest req = Context.getRequest();
        //获取待更新文件列表
        String fileList[] = req.getParameterValues("fileList");
        //获取程序包文件ID
        String fileId = req.getParameter("fileId");
        if (fileId.length() <= 0) {
            throw new TipException("未传递文件ID");
        }
        Bean zipBean = FileMgr.getFile(fileId);
        //程序包真实路径
        String zipPath = zipBean.getStr("FILE_PATH");
        if (zipPath.length() > 0) {
            zipPath = zipPath.replace("@SYS_FILE_PATH@", FileMgr.getRootPath());
        } else {
            throw new TipException("未找到更新包");
        }
        HashMap fileMap = new HashMap();
        for(int i=0;i<fileList.length;i++){
            fileMap.put(fileList[i],new Long(i));
        }
        try{
            FileUpdate fileUpdate = new FileUpdate();
            UpdateResultList resultList = fileUpdate.update(zipPath, fileList, fileMap);
            outBean.set("RESULT_LIST",resultList);
        }catch(Exception e){
            throw new TipException(e.getMessage());
        }
        outBean.setToDispatcher("/update/showUpdateResult.jsp");
        return outBean;
    }
}
