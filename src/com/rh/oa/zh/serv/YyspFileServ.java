package com.rh.oa.zh.serv;

import java.util.ArrayList;
import java.util.List;
import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.comm.FileMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Constant;
import com.rh.core.util.lang.ObjectCreator;
import com.rh.oa.gw.GwSealProcess;
import com.rh.oa.zh.seal.YyspZhJdSealProcess;

/**
 * 用印审批单文件材料服务
 * @author wangchen
 * 
 */
public class YyspFileServ extends CommonServ {
    /** 用印审批单盖章文件信息 */
    private static final String YYSPD_GW_SEAL_FILE = "OA_GW_SEAL_FILE";
    /** 用印审批单主单服务 */
    private static final String YYSPD_FORM_SERV = "OA_YY_YYSPD";
    /** 公共文档子服务 */
    private static final String YYSPD_FILE_PUB_SERV = "OA_YY_YYSPD_FILE_PUB";
    /** 用印材料子服务 */
    private static final String YYSPD_FILE_PRI_SERV = "OA_YY_YYSPD_FILE_PRI";
    /** 公共文档配置服务 */
    private static final String YYSPD_PUB_FILE_SERV = "OA_YY_PUB_FILE";
    /** 用印审批印章实现类 **/
    private static final String GW_SEAL_IMPL = "OA_SEAL_IMPL_CLS.yy";

    @Override
    protected void afterQuery(ParamBean paramBean, OutBean outBean) {

        /** 处理列表页自定义字段：能够追加打印标识 */
        List<Bean> dataList = outBean.getDataList();
        if (dataList.size() > 0) {
            GwSealProcess sealProcess = new YyspZhJdSealProcess();
            for (Bean data : dataList) {
                String sealFileId = data.getStr("SEALED_FILE_ID");
                boolean appendPrint = false;
                if (!sealFileId.isEmpty()) {
                    // 灰章文件数据Bean
                    Bean huiFileBean = FileMgr.getFile(sealFileId);
                    if (huiFileBean != null) {
                        appendPrint = sealProcess.canAppendPrintNum(huiFileBean);
                    }
                }
                data.set("YY_APPEND_PRINT", appendPrint);
            }
        }
    }

    /**
     * 添加公共文档
     * @param paramBean 入参
     * @return 结果
     */
    public OutBean addFileToPubList(ParamBean paramBean) {
        OutBean out = new OutBean();
        String addFileCode = paramBean.getStr("addFileCode");
        String yyOdeptCode = paramBean.getStr("yyOdeptCode");
        String mainSpdId = paramBean.getStr("mainSpdId");
        if (mainSpdId.isEmpty()) {
            return out.setOk();
        }
        // 查出对应公共文档文件ID
        SqlBean sql;
        sql = new SqlBean();
        sql.and("NAME", addFileCode);
        sql.and("S_ODEPT", yyOdeptCode);
        sql.and("S_FLAG", Constant.YES_INT);
        List<Bean> pubFiles = ServDao.finds(YYSPD_PUB_FILE_SERV, sql);
        if (pubFiles.size() == 1) {
            String addFileId = pubFiles.get(0).getStr("FILE_ID");
            // 检查是否已存在
            sql = new SqlBean();
            sql.and("YYID", mainSpdId);
            sql.and("YY_FILE_CODE", addFileCode);
            sql.and("YY_FILE_TYPE", Constant.YES_INT);
            List<Bean> relPubFiles = ServDao.finds(YYSPD_FILE_PUB_SERV, sql);
            // 添加
            if (relPubFiles.size() == 0) {
                ParamBean addBean = new ParamBean();
                addBean.setAddFlag(true);
                addBean.setServId(YYSPD_FILE_PUB_SERV);
                addBean.setAct(ServMgr.ACT_SAVE);
                addBean.set("YYID", mainSpdId);
                addBean.set("YY_FILE_CODE", addFileCode);
                addBean.set("YY_FILE_ID", addFileId);
                out = ServMgr.act(addBean);
                // 修改
            } else if (relPubFiles.size() == 1) {
                // 无变化不修改
                if (relPubFiles.get(0).getStr("YY_FILE_ID").equals(addFileId)) {
                    out.setOk();
                } else {
                    ParamBean modifyBean = new ParamBean();
                    modifyBean.setServId(YYSPD_FILE_PUB_SERV);
                    modifyBean.setAct(ServMgr.ACT_SAVE);
                    modifyBean.setId(relPubFiles.get(0).getId());
                    modifyBean.set("YY_FILE_ID", addFileId);
                    out = ServMgr.act(modifyBean);
                }
            } else {
                out.setError();
            }
        } else {
            out.setError();
        }
        return out;
    }

    /**
     * 取消公共文档
     * @param paramBean 入参
     * @return 结果
     */
    public OutBean delFileFromPubList(ParamBean paramBean) {
        OutBean out = new OutBean();
        String delFileCode = paramBean.getStr("delFileCode");
        String mainSpdId = paramBean.getStr("mainSpdId");
        // 删除
        ParamBean destroyBean = new ParamBean();
        destroyBean.setWhere(" and YY_FILE_TYPE = 1 and YYID = '" + mainSpdId + "' and YY_FILE_CODE = '" + delFileCode
                + "'");
        int delCounts = ServDao.deletes(YYSPD_FILE_PUB_SERV, destroyBean);
        if (delCounts >= 0) {
            out.setOk();
        } else {
            out.setError();
        }
        return out;
    }

    /**
     * 添加私有材料
     * @param paramBean 入参
     * @return 结果
     */
    public OutBean addFileToPriList(ParamBean paramBean) {
        OutBean out = new OutBean();
        String addFileCodes = paramBean.getStr("addFileCodes");
        String addFileIds = paramBean.getStr("addFileIds");
        String mainSpdId = paramBean.getStr("mainSpdId");
        if (!addFileCodes.isEmpty() && !addFileIds.isEmpty()) {
            String[] fileCodeArr = addFileCodes.split(Constant.SEPARATOR);
            String[] fileIdArr = addFileIds.split(Constant.SEPARATOR);
            if (fileCodeArr.length != fileIdArr.length) {
                return out.setError();
            }
            ParamBean batchAddBean = new ParamBean();
            List<Bean> batchDatas = new ArrayList<Bean>();
            int i = 0;
            for (String fileCode : fileCodeArr) {
                ParamBean addBean = new ParamBean();
                addBean.setAddFlag(true);
                addBean.set("YYID", mainSpdId);
                addBean.set("YY_FILE_CODE", fileCode);
                addBean.set("YY_FILE_ID", fileIdArr[i]);
                batchDatas.add(addBean);
                i++;
            }
            batchAddBean.set("BATCHDATAS", batchDatas);
            batchAddBean.setServId(YYSPD_FILE_PRI_SERV);
            batchAddBean.setAct(ServMgr.ACT_BATCHSAVE);
            ServMgr.act(batchAddBean);
            out.setOk();
            return out;
        }
        out.setError();
        return out;
    }

    @Override
    /**
     * 删除私有文件关系表关系后再删除sy_comm_file表中的对应文件
     */
    protected void afterDelete(ParamBean paramBean, OutBean outBean) {
        if (paramBean.getServId().equals(YYSPD_FILE_PRI_SERV)) {
            List<Bean> delDataList = outBean.getDataList();
            if (delDataList.size() > 0) {
                for (Bean delData : delDataList) {
                    String fileId = delData.getStr("YY_FILE_ID");
                    if (fileId.isEmpty()) {
                        continue;
                    }
                    FileMgr.deleteFile(fileId);
                }
            }
        }
    }

    /**
     * 获取盖章信息
     * @param paramBean 参数Bean
     * @return OutBean 盖章文件及其印章信息
     */
    public OutBean getSealFileInfo(ParamBean paramBean) {
        final String yyFormId = paramBean.getStr("PK_FORM");
        final String yyFileId = paramBean.getStr("PK_FILE");

        // 获取中间文件bean
        Bean relfile = ServDao.find(YYSPD_FILE_PRI_SERV, yyFileId);

        /*
         * 修改验证条件，不再进行文件是否已盖章进行验证20150114
         */
        // if (relfile.isNotEmpty("SEALED_FILE_ID")) {
        // throw new TipException("该文件已盖章");
        // }

        // 检查待盖章文件是否存在
        Bean file = null;
        try {
            file = FileMgr.getFile(relfile.getStr("YY_FILE_ID"));
        } catch (TipException e) {
            log.warn("文件获取失败" + e);
            throw new TipException("被盖章文件获取失败。");
        }
        if (file == null) {
            throw new TipException("被盖章文件不存在。");
        }
        // 获取表单Bean
        Bean yyBean = ServDao.find(YYSPD_FORM_SERV, yyFormId);
        // 创建印章处理对象
        GwSealProcess yySeal = createYySeal();
        // 获取打印份数
        int printNum = relfile.get("YY_FILE_NUM", 1);
        // 获取盖章信息
        OutBean out = yySeal.getSealFileInfo(yyBean, file, printNum);
        // 添加印章信息
        out.put("SEAL_CODE", yyBean.getStr("YY_SEALS"));
        out.put("SEAL_NAME", yyBean.getStr("YY_SEALS_NAME"));

        // 添加印章用印机构编码-SEAL_ODEPTS ，印章操作次数-SEAL_COUNT20150104
        out.put("SEAL_ODEPTS", relfile.getStr("SEAL_ODEPTS"));
        out.put("SEAL_COUNT", relfile.get("SEAL_COUNT", 0));

        // 获取印章系统的印章文件id
        Bean gwSealbean = null;
        if (!relfile.getStr("SEALED_FILE_ID").isEmpty()) {
            gwSealbean = ServDao.find(YYSPD_GW_SEAL_FILE, relfile.getStr("SEALED_FILE_ID"));
        }
        //if (!gwSealbean.isEmpty()) {
        if (gwSealbean != null) {
            out.put("SEAL_FILE_ID", gwSealbean.getStr("SEAL_FILE_ID"));
        }
        out.put("PK_FILE", yyFileId);
        return out;
    }

    /**
     * 取消印章
     * @param paramBean bean
     * @return bean
     */
    public OutBean undoSeal(ParamBean paramBean) {
        final String yyFormId = paramBean.getStr("PK_FORM");
        final String yyFileId = paramBean.getStr("PK_FILE");
        // 获取表单Bean
        Bean yyBean = ServDao.find(YYSPD_FORM_SERV, yyFormId);
        // 获取中间文件Bean
        Bean relfile = ServDao.find(YYSPD_FILE_PRI_SERV, yyFileId);
        // 创建印章处理对象
        GwSealProcess yySeal = createYySeal();
        // 取消盖章
        OutBean out = yySeal.undoSeal(yyBean, relfile);
        return out;
    }

    /**
     * 获取打印文件的信息
     * @param paramBean 参数Bean
     * @return 打印红头红章正文的信息
     */
    public OutBean getPrintFileInfo(ParamBean paramBean) {
        final String yyFormId = paramBean.getStr("PK_FORM");
        final String yyFileId = paramBean.getStr("PK_FILE");

        // 获取中间文件bean
        Bean relfile = ServDao.find(YYSPD_FILE_PRI_SERV, yyFileId);
        if (relfile.isEmpty("SEALED_FILE_ID")) {
            throw new TipException("盖章文件不存在");
        }
        // 灰章文件
        Bean heiFile = FileMgr.getFile(relfile.getStr("SEALED_FILE_ID"));
        // 获取表单Bean
        Bean yyBean = ServDao.find(YYSPD_FORM_SERV, yyFormId);
        // 创建印章处理对象
        GwSealProcess yySeal = createYySeal();
        // 获取盖章信息
        OutBean out = yySeal.getPrintFileInfo(yyBean, heiFile);
        out.setOk();
        return out;
    }

    /**
     * 创建印章处理对象
     * @return 印章处理对象
     */
    private GwSealProcess createYySeal() {
        String yySealCls = Context.getSyConf(GW_SEAL_IMPL, "com.rh.oa.zh.seal.YyspZhJdSealProcess");
        return ObjectCreator.create(GwSealProcess.class, yySealCls);
    }
}
