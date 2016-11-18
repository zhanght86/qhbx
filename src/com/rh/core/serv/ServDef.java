package com.rh.core.serv;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.base.db.Transaction;
import com.rh.core.comm.FileMgr;
import com.rh.core.comm.xdoc.OutputXdoc;
import com.rh.core.comm.xdoc.XdocUtils;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Constant;
import com.rh.core.util.JsonUtils;

/**
 * 服务定义类
 * 
 * @author Jerry Li
 */
public class ServDef extends CommonServ {
    /** log */
    private static Log log = LogFactory.getLog(ServDef.class);
    
    /** servid */
    private static final String SERV_SY_SERV = "SY_SERV";
    
    /**
     * 从数据库重新装载服务信息
     * @param paramBean    参数Bean
     * @return 服务相关设定信息
     */
    public OutBean reloadServ(ParamBean paramBean) {
        int count = 0;
        String servId = paramBean.getId();
        Bean serv = ServUtils.getServDef(servId);
        String dsName = serv.getStr("SERV_DATA_SOURCE");
        if (dsName.length() > 0) {
            Transaction.begin(dsName);
        }
        try {
            count = ServUtils.impServDef(servId, serv.getStr("TABLE_ACTION"), 
                    serv.getStr("TABLE_VIEW"), false);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            if (dsName.length() > 0) {
                Transaction.end();
            }
        }
        OutBean outBean = new OutBean();
        outBean.setOk(Context.getSyMsg("SY_IMPORT_OK", String.valueOf(count)));
        return outBean;
    }
    
    /**
     * 清除缓存信息
     * @param paramBean    参数Bean
     * @return 服务相关设定信息
     */
    public OutBean clearCache(ParamBean paramBean) {
        OutBean outBean = new OutBean();
        if (paramBean.getId().length() > 0) {
            ServUtils.clearServCache(paramBean.getId(), false);
            outBean.setOk();
        } else {
            outBean.setError();
        }
        return outBean;
    }
    
    /**
     * 将服务定义信息生成为json文本文件，缺省导出选定的服务
     * @param bean 参数信息
     * @return 生成结果
     */
    public OutBean toJson(Bean bean) {
        ParamBean paramBean = new ParamBean(bean);
        String servId = paramBean.getServId();
        ServDefBean servDef = ServUtils.getServDef(servId);
        int count = 0;
        Bean param = new Bean();
        StringBuilder where = new StringBuilder(" and S_FLAG=");
        where.append(Constant.YES);
        if (paramBean.getId().length() > 0) {
            where.append(" and SERV_ID in ('").append(paramBean.getId().replaceAll(",", "','")).append("')");
        } else {
            where.append(servDef.getServDefWhere());
        }
        param.set(Constant.PARAM_WHERE, where.toString());
        List<Bean> servList = ServDao.finds(SERV_SY_SERV, param);
        for (Bean serv : servList) {
            String sId = serv.getId();
            if (ServUtils.toJsonFile(sId) != null) {
                ServUtils.clearServCache(sId, false);
                count++;
            }
        }
        OutBean outBean = new OutBean();
        if (count > 0) {
            outBean.setOk(Context.getSyMsg("SY_BATCHSAVE_OK", String.valueOf(count)));
        } else {
            outBean.setError(Context.getSyMsg("SY_BATCHSAVE_NONE"));
        }
        return outBean;
    }
    
    /**
     * 从其他服务复制服务信息
     * @param paramBean    参数Bean
     * @return 服务相关设定信息
     */
    @SuppressWarnings("unchecked")
    public OutBean copyOf(ParamBean paramBean) {
        String servId = paramBean.getStr("SERV_ID");
        String fromServId = paramBean.getStr("FROM_SERV_ID");
        Bean fromServ = ServUtils.getServData(fromServId);
        int count = 0;
        //导入item
        List<Bean> dataList = (List<Bean>) fromServ.get(ServUtils.TABLE_SERV_ITEM);
        for (Bean data : dataList) {
            data.setId("");
            data.set("ITEM_ID", "");
            data.set("SERV_ID", servId);
            try {
                if (ServDao.create(ServUtils.TABLE_SERV_ITEM, data) != null) {
                    count++;
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        //导入act
        dataList = (List<Bean>) fromServ.get(ServUtils.TABLE_SERV_ACT);
        for (Bean data : dataList) {
            data.setId("");
            data.set("ACT_ID", "");
            data.set("SERV_ID", servId);
            try {
                if (ServDao.create(ServUtils.TABLE_SERV_ACT, data) != null) {
                    count++;
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        //导入where规则
        dataList = (List<Bean>) fromServ.get(ServUtils.TABLE_SERV_WHERE);
        for (Bean data : dataList) {
            data.setId("");
            data.set("WHERE_ID", "");
            data.set("SERV_ID", servId);
            try {
                if (ServDao.create(ServUtils.TABLE_SERV_WHERE, data) != null) {
                    count++;
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        //导入link
        dataList = fromServ.getList(ServUtils.TABLE_SERV_LINK);
        for (Bean data : dataList) {
            data.setId("");
            data.set("LINK_ID", "");
            data.set("SERV_ID", servId);
            Bean newData = null;
            try {
                newData = ServDao.create(ServUtils.TABLE_SERV_LINK, data); 
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            if (newData != null) {
                count++;
                if (data.contains(ServUtils.TABLE_SERV_LINK_ITEM)) {
                    List<Bean> items = (List<Bean>) data.get(ServUtils.TABLE_SERV_LINK_ITEM);
                    for (Bean item : items) {
                        item.setId("");
                        item.set("LITEM_ID", "");
                        item.set("LINK_ID", newData.getStr("LINK_ID"));
                        if (ServDao.create(ServUtils.TABLE_SERV_LINK_ITEM, item) != null) {
                            count++;
                        }
                    }
                }
            } //end if
        } //end for
        fromServ.setId(servId);
        fromServ.set("SERV_ID", servId);
        ServDao.update(SERV_SY_SERV, fromServ);
        
        OutBean outBean = new OutBean();
        outBean.setOk(Context.getSyMsg("SY_IMPORT_OK", String.valueOf(count)));
        return outBean;
    }
   
    /**
     * 从指定数据表导入服务定义信息，支持指定数据源
     * @param paramBean 参数信息
     * @return 导入结果
     */
    public OutBean fromTable(ParamBean paramBean) {
        int count = 0;
        String table = paramBean.getStr("TABLE_VIEW");
        String dsName = "";
        int pos = table.indexOf(".");
        if (pos > 0) {
            dsName = table.substring(0, pos);
            table = table.substring(pos + 1);
        } else if (pos == 0) {
            table = table.substring(1);
        }
        
        if (dsName.length() > 0) {
            Transaction.begin(dsName);
        }
        try {
            if (ServUtils.getServDataByDB(table) == null) {
                ServUtils.impServDef(table);
                if (dsName.length() > 0) { //提交本事务
                    Transaction.commit();
                }
                //导入按钮信息
                ParamBean param = new ParamBean(ServMgr.SY_SERV_ACT, "impActs");
                param.set("SERV_ID", table);
                ServMgr.act(param);
                count++;
            } else {
                throw new TipException(Context.getSyMsg("SY_IMPORT_EXISTS"));
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            if (dsName.length() > 0) {
                Transaction.end();
            }
        }
        OutBean outBean = new OutBean();
        outBean.setOk(Context.getSyMsg("SY_IMPORT_OK", String.valueOf(count)));
        return outBean;
    }
    
    /**
     * 将服务定义从文件导入数据库，导入选定的服务，如果没有选定则导入数据库不存在的服务。
     * @param paramBean 参数信息
     * @return 导入结果
     */
    public OutBean fromJson(ParamBean paramBean) {
        int count = 0;
        if (paramBean.getId().length() > 0) {
            String[] servIds = paramBean.getId().split(Constant.SEPARATOR);
            for (String servId : servIds) {
                Bean serv = ServUtils.getServDataByFile(servId);
                if (serv != null) {
                    impJsonBean(serv);
                }
                count++;
            }
        } else { //全部导入不存在的服务
            List<Bean> servList = ServUtils.getFileServDataList();
            for (Bean serv : servList) {
                Bean countBean = new Bean();
                countBean.set("SERV_ID", serv.getStr("SERV_ID"));
                if (ServDao.count(SERV_SY_SERV, countBean) <= 0) { //不存在的再添加
                    ParamBean param = new ParamBean(serv);
                    param.setServId(SERV_SY_SERV).set("$JSON_FLAG", false);
                    try {
                        if (add(param).isOk()) {
                            count++;
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }
        OutBean outBean = new OutBean();
        outBean.setOk(Context.getSyMsg("SY_IMPORT_OK", String.valueOf(count)));
        return outBean;
    }

    /**
     * 导入服务定义 如果系统中已经有此流程定义，则进行覆盖
     * @param paramBean 要导入的文件的fileId
     * @return Bean
     */
    public OutBean uploadJson(ParamBean paramBean) {
        OutBean resultBean = new OutBean();
        int count = 0; // 导入的流程数量
        String fileId = paramBean.getStr("fileId");
        Bean fileBean = FileMgr.getFile(fileId);
        if (fileBean != null) {
            ZipInputStream zipIn = null;
            InputStream in = null;
            try {
                if (fileBean.getStr("FILE_MTYPE").equals("application/zip")) {
                    zipIn = new ZipInputStream(FileMgr.download(fileBean));
                    in = new BufferedInputStream(zipIn);
                    while (zipIn.getNextEntry() != null) {
                        if (impJsonBean(JsonUtils.toBean(IOUtils.toString(in, Constant.ENCODING)))) {
                            count++;
                        }
                        zipIn.closeEntry();
                    }
                } else {
                    in = FileMgr.download(fileBean);
                    if (impJsonBean(JsonUtils.toBean(IOUtils.toString(in, Constant.ENCODING)))) {
                        count++;
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                }
                if (zipIn != null) {
                    try {
                        zipIn.close();
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }
        if (count > 0) {
            FileMgr.deleteFile(fileBean);
            resultBean.setOk(count + "个服务导入成功！");
        } else {
            resultBean.setError();
        }
        return resultBean;
    }
    
    /**
     * 修改服务定义之前继承主键定义
     * 
     * @param paramBean 参数Bean
     */
    protected void beforeSave(ParamBean paramBean) {
        //强制编码大写
        if (!paramBean.isEmpty("SERV_ID")) {
            paramBean.set("SERV_ID", paramBean.getStr("SERV_ID").toUpperCase());
        }
    }
    
    /**
     * 修改服务定义后清除cache
     * 
     * @param paramBean 参数Bean
     * @param outBean 输出信息
     */
    protected void afterSave(ParamBean paramBean, OutBean outBean) {
        if (outBean.isOk()) {
            Bean serv = paramBean.getSaveFullData();
            ServUtils.clearServCache(serv.getStr("SERV_ID"), paramBean.get("$JSON_FLAG", true));
            //变更了服务编码，则删除历史服务编码
            if (paramBean.getId().length() > 0 && !paramBean.isEmpty("SERV_ID")) {
                Bean oldBean = paramBean.getSaveOldData();
                if (oldBean.getStr("SERV_ID").length() > 0 
                        && !oldBean.getStr("SERV_ID").equals(paramBean.getStr("SERV_ID"))) {
                    ServUtils.deleteJsonFile(oldBean.getStr("SERV_ID")); //删除服务定义文件
                }
            }
        }
    }

    /**
     * 删除服务定义后清除cache
     * 
     * @param paramBean 参数Bean
     * @param outBean 输出信息
     */
    protected void afterDelete(ParamBean paramBean, OutBean outBean) {
        if (outBean.isOk()) {
            String[] ids = outBean.getDeleteIds().split(Constant.SEPARATOR);
            for (String id : ids) {
                //先删除对应的搜索设置
                ServDao dataBean  = new ServDao(ServUtils.TABLE_SERV_SEARCH, id);
                dataBean.destroy();
                //再清除缓存
                ServUtils.clearServCache(id, false);
                //删除服务定义文件（没有设定不处理json文件）
                if (paramBean.get("$JSON_FLAG", true)) { 
                    ServUtils.deleteJsonFile(id);
                }
            }
        }
    }
    
    /**
     * 获取Xdoc格式化之后的文件流
     * @param paramBean 参数集合
     */
    public void getOutputXdocFile(ParamBean paramBean) {
        SqlBean query = new SqlBean();
        String ids = paramBean.getStr("ids");
        if (!ids.isEmpty()) {
            query.andIn("SERV_ID", ids.split(Constant.SEPARATOR));
        }
        query.set(Constant.PARAM_LINK_FLAG, true);
        List<Bean> beanList = ServDao.finds(paramBean.getServId(), query);   
        String sb = XdocUtils.toXml("xdoc", new Bean().set("data", beanList));
        paramBean.set("_xdata", sb);
        OutputXdoc xdoc = new OutputXdoc();
        xdoc.outputXdoc(paramBean);
    }
    
    /**
     * 导入JsonBean格式的服务定义，如果已经存在，则直接覆盖当前服务定义（删除后导入）
     * @param jsonBean JsonBean格式的服务定义
     * @return 是否成功导入
     */
    private boolean impJsonBean(Bean jsonBean) {
        ParamBean param = new ParamBean();
        String servId = jsonBean.getStr("SERV_ID");
        param.setId(servId).setServId(SERV_SY_SERV).setLinkFlag(true).set("$JSON_FLAG", false); //不生成文件、级联删除
        delete(param);
        param = new ParamBean(jsonBean).setServId(SERV_SY_SERV).setAddFlag(true);
        return save(param).isOk();
    }
}
