package com.rh.core.comm.cms.serv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.comm.cms.CacheableServ;
import com.rh.core.comm.cms.mgr.ChannelMgr;
import com.rh.core.comm.cms.mgr.ExtendsField;
import com.rh.core.org.UserBean;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;

/**
 * cms channel service
 * @author liwei
 * 
 */
public class ChannelServ extends CacheableServ {

    /** site service id */
    private static final String SITE_SERVICE = ServMgr.SY_COMM_NEWS_SITE;
    @Override
    protected void afterSave(ParamBean paramBean, OutBean outBean) {
        //给创建栏目的用户添加管理权限
       	addUserAcl(paramBean, outBean);
       }
  
 /**
  * 给栏目创建者赋管理权限
  * @param paramBean 参数
  * @param outBean 参数
  */
    public void addUserAcl(ParamBean paramBean, OutBean outBean) {
       	if (!paramBean.getAddFlag()) {
       		return;
       	} 
       	//保存添加人的管理的权限ACL_TYPE='SY_COMM_INFOS_CHNL_MANAGE' 
       	UserBean userBean = Context.getUserBean();
       	String usercode = "U_" + userBean.getCode();
       	String dataid = paramBean.getId();
           Bean dataBean  = new Bean().set("SERV_ID", "SY_COMM_INFOS_CHNL")
       		   .set("ACL_TYPE", "SY_COMM_INFOS_CHNL_MANAGE")
       		   .set("ACL_OWNER", usercode).set("DATA_ID", dataid);
       	ServDao.save("SY_SERV_DACL_ITEM", dataBean);
       }

    /**
     * 查询栏目
     * @param paramBean 参数Bean
     * @return 查询结果
     */
    public OutBean byid(ParamBean paramBean) {
        paramBean.setServId(ServMgr.SY_COMM_CMS_CHNL);

        long startTime = System.currentTimeMillis();
        OutBean channel = super.byid(paramBean);
        if (0 == paramBean.getId().length()) {
            return channel;
        }
        Bean result = channel.copyOf();

        ChannelMgr.getInstance().setExtendValues(result);

        // 设置显示模版ID
        if (1 == result.get("CHNL_TMPL_DISMODEL", 1)) {
            result.set("TMPL_ID", result.getStr("CHNL_LIST_TMPL"));
        } else {
            result.set("TMPL_ID", result.getStr("CHNL_TMPL"));
        }

        // 如果是站点所生成的root栏目,禁止修改
        int siteCount = ServDao.count(SITE_SERVICE, new Bean().setId(paramBean.getId()));
        if (siteCount > 0) {
            result.set("IS_SITE_ROOT", 1);
        } else {
            result.set("IS_SITE_ROOT", 2);
        }
        log.debug("channel by id qtime:" + (System.currentTimeMillis() - startTime));
        return new OutBean(result);
    }

    /**
     * 获取目标栏目下的子栏目
     * @param paramBean - 参数bean
     * @return outbean
     */
    public OutBean getSubChannel(ParamBean paramBean) {
        OutBean outBean = null;
        //return from cache first
        String paramKey = getKey(paramBean);
        outBean = getFromCache(paramKey);
        if (null != outBean) {
            log.debug(" get data from cache!");
            return outBean;
        }

        String channelId = paramBean.getStr("channelId");
        String siteId = paramBean.getStr("siteId");
        int count = paramBean.getInt("count");

        List<Bean> channelList = null;
        ParamBean bean = new ParamBean();
        if (null != channelId && channelId.length() > 0) {
            bean.set("CHNL_PID", channelId);
        }
        if (null != siteId && siteId.length() > 0) {
            bean.set("SITE_ID", siteId);
        }
        if (0 < count) {
            bean.setShowNum(count);
        }
        bean.setOrder("CHNL_SORT ASC");
        channelList = ServDao.finds("SY_COMM_CMS_CHNL", bean);
        outBean = new OutBean().setData(channelList);
        
        //put in cache
        putInCache(paramKey, outBean);
        
        return outBean;
    }

    /**
     * 获取目标栏目直系结构 <br>
     * 例: <br>
     * 根据 ccc获取结果如下: <br>
     * |-c <br>
     * |---cc <br>
     * |-----ccc <br>
     * @param paramBean - 参数bean
     * @return outbean
     */
    public OutBean getChannelFamily(ParamBean paramBean) {
        OutBean outBean = null;
        //return from cache first
        String paramKey = getKey(paramBean);
        outBean = getFromCache(paramKey);
        if (null != outBean) {
            log.debug(" get data from cache!");
            return outBean;
        }
        
        String channelId = paramBean.getStr("channelId");

        List<Bean> result = new ArrayList<Bean>();
        Bean channel = getChannel(channelId);
        while (channel.isNotEmpty("CHNL_PID")) {
            channel = getChannel(channel.getStr("CHNL_PID"));
            if (!channel.isEmpty()) {
                result.add(channel);
            }
        }

        Collections.reverse(result);
        outBean = new OutBean().setData(result);
        
        //put in cache
        putInCache(paramKey, outBean);
        
        return outBean;
    }

    /**
     * get channel bean
     * @param chnlId - channel id
     * @return bean
     */
    private Bean getChannel(String chnlId) {
        ParamBean paramBean = new ParamBean().setId(chnlId);
        return byid(paramBean);
    }

    @Override
    protected void beforeSave(ParamBean paramBean) {
        for (ExtendsField exte : ChannelMgr.EXTENDS_FIELDS) {
            // 修改模版时，必须同时修改模版的ID及NAME
            if (null != exte.getNameField() && !paramBean.contains(exte.getIdField())
                    && paramBean.contains(exte.getNameField())) {
                paramBean.remove(exte.getNameField());
            }
        }

    }

    @Override
    protected String getCacheServId() {
        return ServMgr.SY_COMM_CMS_CHNL;
    }
    

}
