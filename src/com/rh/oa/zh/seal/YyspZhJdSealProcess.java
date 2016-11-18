package com.rh.oa.zh.seal;

import java.util.List;


import org.apache.commons.lang.StringUtils;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.comm.FileMgr;
import com.rh.core.org.UserBean;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;
import com.rh.core.util.lang.Assert;
import com.rh.oa.gw.GwSealProcess;

/**
 * 
 * 中华保险项目：吉大正源电子印章与用印审批模块的接口实现类
 * @author wangchen
 * 
 */
public class YyspZhJdSealProcess extends AbstractZhJdSealProcess implements GwSealProcess {
    /** 用印材料子服务 */
    private static final String YYSPD_FILE_PRI_SERV = "OA_YY_YYSPD_FILE_PRI";

    @Override
    public String getModelName() {
        return "yy";
    }

    @Override
    public String getODeptNum(Bean yyBean) {
        String sodeptCode = yyBean.getStr("YY_ODEPT");
        return SealMgr.getODeptNum(sodeptCode);
    }

    public void checkSealFileType(Bean fileBean) {
    }

    public boolean hasSealed(ParamBean paramBean) {
        Bean yyFileBean = paramBean.getBean("fileBean");
        String fileId = yyFileBean.getId();
        if (yyFileBean.isNotEmpty("ORIG_FILE_ID")) {
            fileId = yyFileBean.getStr("ORIG_FILE_ID");
        }
        Bean sealFile = SealMgr.findSealFile(fileId);
        if (null == sealFile || sealFile.isEmpty("SEAL_FILE_ID")) {
            return false;
        }
        return true;
    }

    public void afterSeal(ParamBean paramBean) {
        // OA 中原始文件ID+审批单主键+用印模块类型代码，如：2Bjg0eXaN9H89CBe0vAJxI.doc;1vzMGU0lJbiVA4gpseTPN3;gw
        String wyh = paramBean.getStr("wyh");
        String[] wyhArr = wyh.split(";");
        // 原始文件ID
        String fileId = wyhArr[0];
        // 所属表单文件ID
        String formId = wyhArr[1];
        // 印章平台的灰章文件ID
        String fileNo = paramBean.getStr("fileNo");
        Assert.hasText(fileNo, "fileNo 参数不能为空。");
        // 印章平台的灰章文件ID的印章章数
        int Sealcount = paramBean.getInt("Sealcount");
        Assert.hasText(String.valueOf(Sealcount), "Sealcount 参数不能为空。");
        // 原文件数据Bean
        Bean srcFileBean = FileMgr.getFile(fileId);
        // 原文件对应表单文件子表记录
        Bean formRelFile = getFormRelFile(fileId, formId);
        // 判断文件是否重复盖章，避免程序错误。
         if (formRelFile.isNotEmpty("SEALED_FILE_ID") && formRelFile.getInt("SEAL_COUNT") == Sealcount) {
       // if (formRelFile.isNotEmpty("SEALED_FILE_ID")) {
           // throw new TipException("文件已盖章，重新盖章之前请先取消盖章。");
             throw new TipException("文件印章数未修改，请修改后保存。");
        }
        // 下载、保存灰章文件到OA系统
        Bean newFileBean = new ParamBean();
        newFileBean.set("FILE_NAME", srcFileBean.getStr("DIS_NAME") + ".pdf");
        newFileBean.set("SERV_ID", srcFileBean.getStr("SERV_ID"));
        newFileBean.set("DATA_ID", formId);
        newFileBean.set("FILE_CAT", srcFileBean.getStr("FILE_CAT"));


        newFileBean = downloadSealFile(newFileBean, fileNo);
        // 灰章文件上的印章数SEALCOUNT，印章操作标识CANCELSEAL
        newFileBean.set("SEALCOUNT", Sealcount);
        newFileBean.set("CANCELSEAL", paramBean.get("cancelSeal"));
        // 增加灰章文件ID到盖章前文件的SEALED_FILE_ID字段
        changeToSealed(formRelFile, newFileBean);
        // 保存红章文件、灰章文件与原始文件的关系到数据库
        saveSealFileData(newFileBean, srcFileBean.getId(), fileNo);
        // 成功后更新表单
        updateForm(formId);
        // TODO 如果是第二次盖章的话，就覆盖之前的黑头黑章文件。
        // addSealNo(true, formId, srcFileBean.getStr("SERV_ID"), paramBean);
        //增加印章系统返回数据的判断，如果是取消状态且返回的印章数据为0，则取消所有的pdf数据，还原到原始状态20150213
        if(Sealcount == 0 && paramBean.get("cancelSeal").toString().equals("2")){
            Bean yyBean = new ParamBean();
            //Bean relfile = ServDao.find(YYSPD_FILE_PRI_SERV, formId);
            // 取消盖章
            OutBean out = undoSeal(yyBean, formRelFile);
        }
        
    }

    public OutBean undoSeal(Bean yyBean, Bean formRelFile) {
        OutBean rtnBean = new OutBean();
        // 灰章文件ID
        String heiFileId = formRelFile.getStr("SEALED_FILE_ID");
        if (heiFileId.isEmpty()) {
            throw new TipException("文件未盖章。");
        }
        // 灰章文件
        Bean heiFile = FileMgr.getFile(heiFileId);
        // 检查被盖章文件是否存在
        if (heiFile == null) {
            throw new TipException("盖章文件不存在。");
        }
        // 清除表单文件子表灰章文件标识
        changeToUnSealed(formRelFile);
        // 删除红章文件、灰章文件与原始文件的关系
        deleteSealFile(heiFile);
        // 清除印章平台盖章记录
        if (heiFile != null) {
            SealMgr.removeSealFileData(heiFile);
        }
        rtnBean.set("rtnMsg", "succ");
        return rtnBean;
    }

    /**
     * 是否存在盖章机构
     * @param paramBean 参数集合
     * @return 状态结果集
     */
    public OutBean isExitSealOpdets(ParamBean paramBean) {
        String servId = paramBean.getServId();
        // TODO
        return null;
    }

    /**
     * 保存盖章次数
     * @param isSeal 是否是盖章操作
     * @param formId 文件主键
     * @param servId 服务主键
     * @param paramBean 参数集合
     */
    private void addSealNo(boolean isSeal, String formId, String servId, ParamBean paramBean) {
        // 实现盖章次数，在用印审批单中添加盖章次数字段，用于记录盖章次数是否为最后一次（盖章次数-取消印章次数）
        Bean bean = ServDao.find(servId, formId);
        if (null == bean || bean.isEmpty()) {
            return;
        }
        int sealCount = bean.getInt("SEAL_COUNT");
        if (!isSeal) { // 如果为取消盖章操作
            if (sealCount > 0) {
                sealCount = sealCount - 1;
            }
        } else {
            sealCount = sealCount + 1;
        }
        String thisOdeptCode = Context.getUserBean().getODeptCode();
        // 主表中则增加一个odept_id字段，用于记录当前可以追加分数的机构的id；
        String sealOdepts = bean.getStr("SEAL_ODEPTS");
        if (StringUtils.isBlank(sealOdepts)) {
            sealOdepts = thisOdeptCode;
        } else if (!sealOdepts.contains(thisOdeptCode)) {
            sealOdepts = sealOdepts + Constant.SEPARATOR + thisOdeptCode;
        }
        ParamBean updateBean = new ParamBean();
        updateBean.setAddFlag(false).setAct(ServMgr.ACT_SAVE).setServId(servId).setId(formId)
                .set("SEAL_COUNT", sealCount).set("SEAL_ODEPTS", sealOdepts);
        ServMgr.act(updateBean);
        // 那么追加此用户odept_id；在追加打印份数按钮上判断，当前此段中层级最高的odept_id可以追加打印份数的。在取消盖章的时候去除当前人的odept_id
        // 添加一个盖章和取消印章的log表，记录操作的记录
        addSealLogs(isSeal, formId, servId, paramBean.getStr("SEAL_CODE"), paramBean.getStr("SEAL_NAME"));
    }

    /**
     * 添加印章操作log表
     * @param isSeal 是否是盖章操作
     * @param formId 主单id
     * @param servId 服务id
     * @param sealCode 印章编码
     * @param sealName 印章名称
     */
    private void addSealLogs(boolean isSeal, String formId, String servId, String sealCode, String sealName) {
        String sealLogServId = "OA_SEAL_LOGS";
        ParamBean addLogBean = new ParamBean();
        addLogBean.setAddFlag(true);
        addLogBean.setServId(sealLogServId);
        addLogBean.setAct(ServMgr.ACT_SAVE);
        addLogBean.set("SEAL_CODE", sealCode).set("SEAL_NAME", sealName).set("DATA_ID", formId).set("SERV_ID", servId);
        addLogBean.set("SEAL_STATE", isSeal ? "盖章" : "取消盖章");
        ServMgr.act(addLogBean);
    }

    /**
     * 根据表单ID和文件ID获取唯一的表单子文件
     * @param fileId 文件主键
     * @param formId 表单主键
     * @return Bean 表单子文件
     */
    private Bean getFormRelFile(String fileId, String formId) {
        SqlBean sql;
        sql = new SqlBean();
        sql.and("YYID", formId);
        sql.and("YY_FILE_ID", fileId);
        List<Bean> formRelFiles = ServDao.finds("OA_YY_YYSPD_FILE_PRI", sql);
        return formRelFiles.get(0);
    }

    /**
     * 将盖章前文件的是否盖章标识改成已盖章
     * @param formRelFile 表单子文件
     * @param newFileBean 灰章文件
     */
    private void changeToSealed(Bean formRelFile, Bean newFileBean) {
        ParamBean modifyBean = new ParamBean();
        modifyBean.setAddFlag(false);
        modifyBean.setServId("OA_YY_YYSPD_FILE_PRI");
        modifyBean.setAct(ServMgr.ACT_SAVE);
        modifyBean.setId(formRelFile.getId());
        modifyBean.set("SEALED_FILE_ID", newFileBean.getId());
        // 相关操作信息记录        
        String sealodeptlist = sealDepartment(formRelFile.getStr("SEAL_ODEPTS").toString(), Context.getUserBean(),""+newFileBean.get("CANCELSEAL",1));
        modifyBean.set("SEAL_COUNT", newFileBean.get("SEALCOUNT",0));
        modifyBean.set("SEAL_ODEPTS", sealodeptlist);
        modifyBean.set("CANCELSEAL", newFileBean.get("CANCELSEAL",1));
        ServMgr.act(modifyBean);
    }

    /**
     * 将盖章前文件的是否盖章标识改成已盖章
     * @param formRelFile 表单子文件
     */
    private void changeToUnSealed(Bean formRelFile) {
        ParamBean modifyBean = new ParamBean();
        modifyBean.setAddFlag(false);
        modifyBean.setServId("OA_YY_YYSPD_FILE_PRI");
        modifyBean.setAct(ServMgr.ACT_SAVE);
        modifyBean.setId(formRelFile.getId());
        modifyBean.set("SEALED_FILE_ID", "");
        // 清理盖章记录
        modifyBean.set("SEAL_ODEPTS", "");
        modifyBean.set("SEAL_COUNT", 0);
        modifyBean.set("CANCELSEAL", "");

        ServMgr.act(modifyBean);
    }

    /**
     * 删除盖章文件
     * @param heiFile 灰章文件
     */
    private void deleteSealFile(Bean heiFile) {
        FileMgr.deleteFile(heiFile);
    }

    /**
     * 成功后更新表单
     * @param formId 表单主键
     */
    private void updateForm(String formId) {
        ParamBean param = new ParamBean();
        param.setServId("OA_YY_YYSPD");
        param.setAct(ServMgr.ACT_SAVE);
        param.setId(formId);
        param.set("YY_DATE", DateUtils.getDatetimeTS()); // 更新用印日期字段
        param.set("YY_SIGN_USER", Context.getUserBean().getCode()); // 更新签章人字段
        ServMgr.act(param);
    }

    /*
     * 用印机构 判断当前机构是否存在盖章记录，有则修改盖章标识，无则添加机构编码和盖章标识
     * 
     * @param seal_Odepts 用印机构操作记录
     * 
     * @param userBean 当前用户
     * 
     * @param cancelSeal 当前用印机构操作，1，盖章。2，取消盖章
     */

    private String sealDepartment(String seallist, UserBean userBean, String cancelSeal) {
        String seal_OdeptsL = seallist;
        String seal_OdeptL = userBean.getDeptCode();
        String dept_level = userBean.getStr("DEPT_LEVEL");
        String cancelSealL = cancelSeal.trim();
        String seal_Str = null;
        if(cancelSealL.isEmpty()){
            cancelSealL="1";
        }
        if (seal_OdeptsL.indexOf(seal_OdeptL) != -1) {
            if (seal_OdeptsL.indexOf(seal_OdeptL + ":" + 1) != -1) {
                seal_Str = seal_OdeptsL.replace(seal_OdeptL + ":" + 1, seal_OdeptL + ":" + cancelSealL);
            } else if (seal_OdeptsL.indexOf(seal_OdeptL + ":" + 2) != -1) {
                seal_Str = seal_OdeptsL.replace(seal_OdeptL + ":" + 2, seal_OdeptL + ":" + cancelSealL);
            }
        } else {
            if (seal_OdeptsL.length() == 0) {
                seal_Str = seal_OdeptL + ":" + cancelSealL + ":" + dept_level;
            } else {
                seal_Str = seal_OdeptsL + "," + seal_OdeptL + ":" + cancelSealL + ":" + dept_level;
            }

        }
        return seal_Str;
    }
}
