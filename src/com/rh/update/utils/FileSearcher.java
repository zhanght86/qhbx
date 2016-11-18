package com.rh.update.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

/**
 * 搜索指定目录下，指定时间段内，带有某个扩展名的文件
 * @author zotn
 *
 */
public class FileSearcher {
    
    private Vector m_excludeFolder = new Vector();
    
    public FileSearcher() {
        super();
    }

    
    /**
     * 文件检索 根据日期型修改时间检索
     * @param file 搜索开始路径
     * @param lastModifyTime 修改日期，开始时间
     * @param stopModifyTime 修改日期，结束时间
     * @param fileExt 文件扩展名
     * @return
     */
    public ArrayList SearchFile(File file, Date lastModifyTime,Date stopModifyTime, String[] fileExt) {
        return SearchFile(file, lastModifyTime.getTime(), stopModifyTime.getTime(), fileExt);
    }

    /**
     * 文件检索 根据long型修日期检索
     * @param file 搜索开始路径
     * @param modifyTimeStart 修改日期，开始时间
     * @param modifyTimeStop 修改日期，结束时间
     * @param fileExt 文件扩展名
     * @return
     */
    public ArrayList SearchFile(File file, long modifyTimeStart,long modifyTimeStop, String[] fileExt) {
        
        ArrayList resultList = new ArrayList();
        startSearch(file, modifyTimeStart, modifyTimeStop, fileExt,resultList);
        return resultList;

    }
    
    /**
     * 增加不需要查询的目录，在遍历所有文件时，参数中指定的目录不遍历。
     * @param strFolder
     */
    public void addExcludeFolder(String strFolder){
        this.m_excludeFolder.add(strFolder);
    }
    
    /**
     * 清除对象中存在的不包含的目录
     *
     */
    public void clearExcludeFolder(){
        this.m_excludeFolder.clear() ;
    }
        
    /**
     * 主检索程序
     * @param file
     * @param modifyTimeStart
     * @param modifyTimeStop
     * @param fileExt
     * @param resultList 存放查询结果的ArrayList对象
     */
    private void startSearch(File file, long modifyTimeStart,
            long modifyTimeStop, String[] fileExt,ArrayList resultList) {
        if (modifyTimeStop == 0) {
            modifyTimeStop = System.currentTimeMillis();
        }
        if (file.canRead()) {
            if (file.isDirectory()) {
                if(!isExcludeFolder( file.getAbsolutePath() )){
                    File[] files = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        startSearch(files[i], modifyTimeStart, modifyTimeStop, fileExt,resultList);
                    }
                }
            } else {
                if (file.lastModified() > modifyTimeStart&& file.lastModified() <= modifyTimeStop) {
                    for (int i = 0; i < fileExt.length; i++) {
                        if (file.getName().endsWith(fileExt[i])) {
                            //Manager.log.debug(obj,"FoundFile >>"+ file.getAbsolutePath());
                            resultList.add(file);
                        } else if (fileExt[i].equals(".*")) {
                            //Manager.log.debug(obj,"FoundFile >>"+ file.getAbsolutePath());
                            resultList.add(file);
                        }
                    }
                }
            }
        }
    }
    /**
     * 是否是不包含的目录
     * @param strFolder
     * @return
     */
    private boolean isExcludeFolder(String strFolder){
        for(int i=0;i<this.m_excludeFolder.size();i++){
            String excludeFolder = (String)this.m_excludeFolder.get(i);
            if(strFolder.startsWith(excludeFolder)){
                return true;
            }
        }
        
        return false;
    }    

}
