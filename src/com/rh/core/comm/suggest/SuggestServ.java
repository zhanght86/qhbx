package com.rh.core.comm.suggest;

import java.util.ArrayList;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.comm.InfoServ;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;

/***
 * 搜索定义类
 * @author wangjianli
 *
 */
public class SuggestServ {
    /**
     * 获取字典或服务的项值,并动态呈现供用户选择的表单
     * @param paramBean 参数 例如:{sdata=分, surl=PJ_PROJ_INFO_MGTYPE.show.do, 
     * column=ITEM_NAME, serv=SY_COMM_SUGGEST, _TRANS_=true, act=filterSuggest}
     * @return 返回 例如: {datas=[]}
     */
    public Bean filterSuggest(Bean paramBean) {
        List<String> datas = new ArrayList<String>();
        String url = paramBean.getStr("surl");
        String column = paramBean.getStr("column");
        String data = paramBean.getStr("sdata");
        String extwhere = paramBean.getStr("_extWhere");
        List<Bean> list = new ArrayList<Bean>();
        ParamBean b = new ParamBean();
        String servId = url.substring(0, url.indexOf("."));
        if (column.equals("ITEM_NAME") && !servId.equals("SY_SERV_ITEM")) { //调用字典数据
            b.set("DICT_ID", url.substring(0, url.indexOf(".")));
            list = new InfoServ().dict(b).getList("CHILD");
        } else { //调用系统链接
            ParamBean extBean = new ParamBean(url.substring(0, url.indexOf(".")), 
                    url.substring(url.indexOf(".") + 1, url.lastIndexOf(".")));
            extBean.set("_extWhere", extwhere);
            list = ServMgr.act(extBean).getDataList();
        }
        for (int i = 0; i < list.size(); i++) {
            Bean bean = list.get(i);
            if (bean.getStr(column).startsWith(data) && !data.equals(bean.getStr(column))) { //以输入值开头的内容
                datas.add(bean.getStr(column));
                if (datas.size() == 5) { //显示5项
                    break;
                }
            }
        }   
        Bean outBean = new Bean();
        outBean.put("datas", datas);
        return outBean;
    } 
}
