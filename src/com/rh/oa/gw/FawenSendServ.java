package com.rh.oa.gw;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.comm.FileMgr;
import com.rh.core.org.DeptBean;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.OrgMgr;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.send.SendConstant;
import com.rh.core.serv.send.SendUtils;
import com.rh.core.util.DateUtils;
import com.rh.oa.gw.util.GwConstant;
import com.rh.oa.gw.util.GwFileHelper;
import com.rh.oa.gw.util.GwUtils;
import com.rh.oa.gw.util.ZW_TYPE;

/**
 * 发文分发给外机构，外机构用户打开文件时，使用此服务显示页面。
 * 
 * @author yangjy
 * 
 */
public class FawenSendServ extends GwServ {
    
        
    @Override
    protected void afterByid(ParamBean paramBean, OutBean outBean) {
        super.afterByid(paramBean, outBean);
        
        String oDeptCode = outBean.getStr("S_ODEPT");
        DeptBean odept = OrgMgr.getDept(oDeptCode);
        outBean.set("GW_SW_CNAME", odept.getName());
        outBean.set("GW_SW_TYPE", "总公司来文");
        
        String sendId = paramBean.getStr("SEND_ID");
        Bean sendDtl = SendUtils.getSendDetailById(sendId);
        boolean canOpt = SendUtils.isReciver(sendDtl, Context.getUserBean().getId())
                && SendUtils.isNotFinished(sendDtl);
        
        if (canOpt) {
            outBean.set("CAN_OPT_SEND", "true");
            outBean.set("SEND_ID", sendId);
        }
        
        if (!paramBean.getAddFlag() && paramBean.getId().length() > 0) {
            // 增加是否存在PDF盖章文件参数
            GwSealProcess gwSeal = GwUtils.createGwSeal();
            if (gwSeal.hasSealed(paramBean)) {
                outBean.set(GwConstant.EXIST_SEAL_PDF_FILE, "true");
            }
        }
    }
        
    /**
     * 
     * @param paramBean 参数Bean
     * @return 用户能访问的收文菜单列表
     */
    public OutBean findUserSwMenu(ParamBean paramBean) {
        OutBean out = new OutBean();
        
        List<Bean> tmplList = GwTmplMgr.getTmplList("OA_GW_TMPL_SW");
        
        List<Bean> dataBean = new ArrayList<Bean>();
        
        if (tmplList != null) {
            for (Bean bean : tmplList) {
                final String servId = bean.getStr("TMPL_CODE");
                if (servId.endsWith("_OLD")) {
                    continue;
                }

                if (OrgMgr.checkServAuth(servId)) {
                    Bean newBean = new Bean();
                    newBean.set("SERV_ID", servId);
                    newBean.set("SERV_NAME", bean.getStr("TMPL_NAME"));
                    dataBean.add(newBean);
                }
                
                
            }
        }
        out.setData(dataBean);
        

        //找到公文模板上设置的 收文模板编码
        if (paramBean.isNotEmpty("TMPL_CODE")) {
            Bean tmplBean = ServDao.find(GwConstant.OA_GW_TMPL, paramBean.getStr("TMPL_CODE"));
            
            out.set("SW_TMPL_CODE", tmplBean.getStr("SW_TYPE"));
        }
        
        return out;
    }
    
    /**
     * 批量签收外单位的分发明细。如果A公司发送10条给B公司，那么一次性签收10条
     * @param sendId 分发ID
     * @param doUser 办理用户
     */
    private void qianshou(String sendId, UserBean doUser) {
        Bean sendDtlBean = SendUtils.getSendDetailById(sendId);
        final String odeptCode = doUser.getODeptCode();
        // 取得所有为签收的分发明细
        List<Bean> sendList = SendUtils.getUnrecvedSendDetailList(sendDtlBean.getStr("DATA_ID"));
        for (Bean bean : sendList) {
            boolean received = false;
            //是否分发给外单位
            if (bean.getStr("RECV_TYPE").equals(SendConstant.OUTSIDE)) {
                //当前用户与分发的接受单位为同一个单位，且不等于sendId（避免多次签收）
                if (bean.getStr("RECV_ODEPT").equals(odeptCode)) {
                    SendUtils.qianShouSingle(bean, false);
                    received = true;
                }
            }
            
            if (!received && sendId.equals(bean.getId())) {
                SendUtils.qianShouSingle(bean);
            }
        }
    }
    
    
    /**
     * 发文转收文
     * @param paramBean 参数Bean
     * @return 处理结果
     */
    public OutBean toShouwen(ParamBean paramBean) {
        // 修改分发的接收时间
        String sendId = paramBean.getStr("SEND_ID");
        if (StringUtils.isEmpty(sendId)) {
            throw new TipException("没取到分发ID,不能签收");
        }
        
        UserBean doUser = paramBean.getDoUserBean();
        
        qianshou(sendId, doUser);

        // 取得分发明细的对应记录
        Bean sendBean = ServDao.find("SY_COMM_SEND_DETAIL", sendId);

        // 根据sendBean的dataId取得对应的分发的公文数据
        Bean gwBean = ServDao.find(sendBean.getStr("SERV_ID"), sendBean.getStr("DATA_ID"));
        
        OutBean rtnBean = ServMgr.act(paramBean.getStr("nextServId"), "save", cloneGwBean(gwBean));

        // 拷贝文件
        Bean queryBean = new Bean();
        queryBean.set("DATA_ID", gwBean.getId());

        List<Bean> fileList = ServDao.finds(ServMgr.SY_COMM_FILE, queryBean);

        if (fileList != null && fileList.size() > 0) {
            for (Bean fileBean : fileList) {
                //对于正文类型的文件，只复制正文
                if (fileBean.getStr("FILE_CAT").equals(ZW_TYPE.ZHENG_WEN.getCode())) {
                    if (!fileBean.getStr("ITEM_CODE").equals(ZW_TYPE.ZHENG_WEN.getCode())) {
                        continue;
                    }
                }
                
                FileMgr.createLinkFile(fileBean, new Bean().set("SERV_ID", rtnBean.getStr("TMPL_TYPE_CODE"))
                        .set("DATA_ID", rtnBean.getId()).set("S_USER", rtnBean.getStr("S_USER"))
                        .set("S_UNAME", rtnBean.getStr("S_UNAME")).set("S_DEPT", rtnBean.getStr("S_DEPT"))
                        .set("S_DNAME", rtnBean.getStr("S_DNAME")).set("S_CMPY", rtnBean.getStr("S_CMPY"))
                        .set("FILE_HIST_COUNT", 0));
            }
        }
               
        GwFileHelper fileHelper = new GwFileHelper(gwBean.getId(), gwBean.getStr("TMPL_TYPE_CODE"));
        Bean zwFileBean = fileHelper.getZhengwen();
        if (zwFileBean != null) {
            GwSealProcess gwSeal = GwUtils.createGwSeal();
            gwSeal.setPrintNum(rtnBean, zwFileBean);
        }

        return rtnBean;
    }
    
    /**
     * 克隆公文bean
     * 
     * @param gwBean gwBean
     * @return bean
     */
    private ParamBean cloneGwBean(Bean gwBean) {
        UserBean currBean = Context.getUserBean();
        ParamBean bean = new ParamBean();
        bean.set("GW_TITLE", gwBean.getStr("GW_TITLE"));
        bean.set("GW_TOPIC", gwBean.getStr("GW_TOPIC"));
        bean.set("GW_PAGE", gwBean.getStr("GW_PAGE"));
        bean.set("GW_COPIES", gwBean.getStr("GW_COPIES"));
        bean.set("S_EMERGENCY", gwBean.getStr("S_EMERGENCY"));
        bean.set("GW_SECRET", gwBean.getStr("GW_SECRET"));
        bean.set("GW_YEAR_CODE", gwBean.getStr("GW_YEAR_CODE"));
        bean.set("GW_YEAR", gwBean.getStr("GW_YEAR"));
        bean.set("GW_YEAR_NUMBER", gwBean.getStr("GW_YEAR_NUMBER"));
        bean.set("GW_FILE_TYPE", gwBean.getStr("GW_FILE_TYPE"));
        bean.set("GW_MAIN_TO", gwBean.getStr("GW_MAIN_TO"));
        bean.set("GW_COPY_TO", gwBean.getStr("GW_COPY_TO"));
        bean.set("GW_COPYUP_TO", gwBean.getStr("GW_COPYUP_TO"));
        bean.set("GW_COSIGN_TO", gwBean.getStr("GW_COSIGN_TO"));
        bean.set("GW_SEND_TO", gwBean.getStr("GW_SEND_TO"));
        bean.set("GW_BEGIN_TIME", DateUtils.getDatetime());
        bean.set("GW_EXPIRE_TIME", gwBean.getStr("GW_EXPIRE_TIME"));
        bean.set("GW_ZB_TDEPT", currBean.getTDeptCode());
        bean.set("GW_ZB_TNAME", currBean.getTDeptName());
        bean.set("GW_CONTACT", gwBean.getStr("GW_CONTACT"));
        bean.set("GW_CONTACT_PHONE", gwBean.getStr("GW_CONTACT_PHONE"));
        bean.set("GW_SW_TYPE", gwBean.getStr("GW_SW_TYPE"));
        bean.set("GW_SW_DO", gwBean.getStr("GW_SW_DO"));
        bean.set("GW_MEMO", gwBean.getStr("GW_MEMO"));
        bean.set("GW_CODE", gwBean.getStr("GW_CODE"));
        //bean.set("GW_DRAFTDO_MIND", gwBean.getStr("GW_DRAFTDO_MIND"));
        bean.set("GW_ORIG_PLACE", gwBean.getStr("GW_ORIG_PLACE"));
        bean.set("GW_PRINTED_NUM", gwBean.getStr("GW_PRINTED_NUM"));
        
        //来文单位
        String oDeptCode = gwBean.getStr("S_ODEPT");
        DeptBean odept = OrgMgr.getDept(oDeptCode);
        bean.set("GW_SW_CNAME", odept.getName());

        return bean;
    }
}
