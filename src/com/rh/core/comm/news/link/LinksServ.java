package com.rh.core.comm.news.link;

import java.util.ArrayList;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;
/**
 * 常用链接serv
 * @author hai
 *
 */
public class LinksServ extends CommonServ {
    /**默认显示5条*/
	private static final String SHOWNUM = "5";
	
	
    
    /**
     * 获取启用的常用下载列表
     * 
     * @param param 参数
     * @return 返回
     */
    public OutBean getComDownload(ParamBean param) {
        String shownum = param.getStr("COUNT");
        if (shownum.equals("")) {
            shownum = SHOWNUM;
        }
        OutBean outBean = ServMgr.act("BN_MH_DOWNLOAD", "finds",
                new ParamBean().set("S_FLAG", 1).set("LINK_TYPE", "1")
                        .setShowNum(Integer.parseInt(shownum)));
        /** 将文件信息放入返回参数中 **/
        List<Bean> linkBeans = outBean.getDataList();
        List<Bean> fileBeans = new ArrayList<Bean>();
        ParamBean fileWhereBean = new ParamBean();
        try {
            for (int i = 0; i < linkBeans.size(); i++) {
                fileWhereBean.set("DATA_ID", linkBeans.get(i).getId());
                fileWhereBean.set("SERV_ID", "SY_COMM_LINK");
                OutBean fileBean = ServMgr.act("SY_COMM_FILE", "finds",
                        fileWhereBean);
                fileBeans.add(fileBean.getDataList().get(0));
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return outBean.setData(fileBeans);
    }
    
    
    
    public OutBean getComLinks2(ParamBean param) {
        return new OutBean();
    }
  /**
   * 获取启用的常用链接列表 
   * @param param 参数
   * @return 返回
   */
	public OutBean getComLinks(ParamBean param) {
		String shownum = param.getStr("COUNT");
		if (shownum.equals("")) {
			shownum = SHOWNUM;
		}
		OutBean outBean = ServMgr.act("SY_COMM_LINK", "finds", new ParamBean().set("S_FLAG", 1).set("LINK_TYPE", param.get("LINK_TYPE"))
				.setShowNum(Integer.parseInt(shownum))); 
		/*List<Bean> linkBeans = outBean.getDataList();
		try {
			for(int i=0;i<linkBeans.size();i++){
				linkBeans.get(i).set("LINK_ADDRESS",URLEncoder.encode(linkBeans.get(i).getStr("LINK_ADDRESS"),"utf-8"));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}*/
		return outBean;
	}

	/*@SuppressWarnings("deprecation")
	protected void afterQuery(ParamBean paramBean, OutBean outBean) {
		List<Bean> dataLists = outBean.getDataList();
		for (int i = 0; i < dataLists.size(); i++) {
			Bean dataList = dataLists.get(i);
			//dataList.set("LINK_ADDRESS", dataList.getStr("LINK_ADDRESS").replaceAll("\"", "\'"));
			dataList.set("LINK_ADDRESS", URLEncoder.encode(dataList.getStr("LINK_ADDRESS")));
		}
		outBean.setData(dataLists);
	}*/
	    
}
