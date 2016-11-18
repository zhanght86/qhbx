package com.rh.oa.zh.count;

import java.util.ArrayList;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.comm.xdoc.XdocConfig;
import com.rh.core.comm.xdoc.XdocUtils;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;

/**
 * 流程节点办理效率
 * @author ruaho_hdy
 *
 */
public class WfNodesEffCount extends CommonServ {
    
    /**
     * 执行方法
     * @param paramBean 参数集合
     */
    /*public void run(ParamBean paramBean) {
        XdocConfig xdoc = new XdocConfig(); //实例化xdoc配置对象
        //获取xdoc配置信息
        xdoc.init(paramBean);
        //当前是否通过xdoc serv获取的返回数据
        if (xdoc.isExiteXdocSer(paramBean.getStr("IS_XDOC_SERVICE"))) {
            paramBean.set("USE_XDOC_SERVICE", true);
        }
        //获取配置中的sql语句
        String sql = paramBean.getStr("sql");
        //获取sql数据
        List<Bean> outDataList = Context.getExecutor().query(sql);
        //获取xdoc文件输出实例
        OutputXdoc outXdoc = new OutputXdoc();
        //获取xml数据
        paramBean.set("_xdata", getXmlStr(outDataList));
        //输出流文件
        outXdoc.outputXdoc(paramBean);
    }*/
    
    
    /**
     * 执行方法
     * @param paramBean 参数集合
     * @return 结果集
     */
    public OutBean run(ParamBean paramBean) {
        XdocConfig xdoc = new XdocConfig(); //实例化xdoc配置对象
        //获取xdoc配置信息
        xdoc.init(paramBean);
        //获取配置中的sql语句
        String sql = paramBean.getStr("sql");
        //获取sql数据
        int count = Context.getExecutor().count(sql);
        if (count <= 0) {
            return new OutBean().setError("统计数据为空，请重新选择统计条件！");
        }
        List<Bean> outDataList = Context.getExecutor().query(sql);
        return new OutBean().set("_xdata", getXmlStr(outDataList)).set("format", paramBean.getStr("format"))
                            .set("fullName", paramBean.getStr("fullName"))
                            .set("filePath", paramBean.getStr("filePath")).set("URL", paramBean.getStr("URL"));
    }

    /**
     * 获取指定格式xml数据
     * @param outDataList 结果数据集合
     * @return 格式化之后的xml数据字符串
     */
    private String getXmlStr(List<Bean> outDataList) {
        //按照流程类型分组，例如：省总，中支等
        List<String> group = new ArrayList<String>();
        for (Bean b : outDataList) {
            isExitGroup(group, b);
        }
        //将分组数据写入到xml中
        List<Bean> beanList = new ArrayList<Bean>();
        for (String s : group) {
            addXmlNodes(outDataList, beanList, s);
        }
        return XdocUtils.toXml("xdoc", new Bean().set("docs", beanList));
    }

    /**
     * 增加xml数据节点
     * @param outDataList 结果集
     * @param beanList 当前节点数据
     * @param s 流程类型
     */
    private void addXmlNodes(List<Bean> outDataList, List<Bean> beanList, String s) {
        //同组数据
        List<Bean> thisGroupList = new ArrayList<Bean>();
        for (Bean b : outDataList) {
            //找到同组数据
            if (s.equals(b.getStr("PROC_NAME"))) {
                thisGroupList.add(b);
            }
        }
        Bean thisNode = new Bean();
        thisNode.set("doc", thisGroupList);
        thisNode.set("PROC_NAME", s);
        beanList.add(thisNode);
    }

    /**
     * 当前流程类型是否存在
     * @param group 流程类型分组
     * @param b 当前数据对象
     */
    private void isExitGroup(List<String> group, Bean b) {
        //流程类型名称
        String procName = b.getStr("PROC_NAME");
        if (group.size() == 0) {
            group.add(procName);
        } else {
            boolean isExt = false; //是否存在于流程类型分组中
            for (String s : group) {
                if (s.equals(procName)) {
                    isExt = true;
                }
            }
            if (!isExt) {
                group.add(procName);
            }
        }
    }
}
