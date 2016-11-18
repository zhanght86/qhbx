package com.rh.oa.comm;

import org.apache.commons.lang.StringUtils;

import com.rh.core.base.TipException;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.ParamBean;

/**
 * 高级查询功能
 * @author yangjy
 *
 */
public class EntityQuery extends CommonServ {

    @Override
    protected void beforeQuery(ParamBean paramBean) {
        if (paramBean.isNotEmpty("IS_MOBILE")) { //如果是手机访问
            String sqlString = getSqlString(paramBean);
            if (null != sqlString) {
                paramBean.set("_extWhere", sqlString);
                paramBean.setOrder("S_ATIME desc");
            }
        } else {
            super.beforeQuery(paramBean);
            paramBean.set("_searchWhere", 
                    replaceRepeatingChars(paramBean.getStr("_searchWhere").replaceAll("\\*", "\\%"), "%"));
            paramBean.setSelectKeyWord(" /*+INDEX(SY_COMM_ENTITY IDX_SY_COMM_ENTITY_DATA )*/ ");
        }
    }
    
    /**
     * 替换指定重复字符
     * @param sqlWithXingHao 参数
     * @param x 分隔符，长度为一
     * @return 去除指定重复的字符串
     */
    private String replaceRepeatingChars(String sqlWithXingHao, String x) {
        if (StringUtils.isBlank(sqlWithXingHao) || !sqlWithXingHao.contains(x)) {
            return sqlWithXingHao;
        }
        if (x.length() != 1) {
            throw new TipException("替换符长度应为1，不可为其他");
        }
        StringBuffer sb = new StringBuffer();
        int i = 0, len = sqlWithXingHao.length();
        while (i < len) {
            char c = sqlWithXingHao.charAt(i);
            sb.append(c);
            i++;
            // 这个是如果这两个值相等，就让i+1取下一个元素并且是指定字符
            while (i < len && sqlWithXingHao.charAt(i) == c && sqlWithXingHao.charAt(i) == x.charAt(0)) {
                i++;
            }
        }
        return sb.toString();
    }
    
    /**
     * 获取查询语句
     * @param paramBean 参数
     * @return 返回结果sql语句
     */
    private String getSqlString(ParamBean paramBean) {
        if (!paramBean.isEmpty()) { //如果不为空的话在去实例化StringBuffer
            StringBuffer sqlWhereStr = new StringBuffer();
            if (paramBean.isNotEmpty("ENTITY_CODE")) { //公文编号
                sqlWhereStr.append(" and ENTITY_CODE like '%");
                sqlWhereStr.append(paramBean.getStr("ENTITY_CODE"));
                sqlWhereStr.append("%'");
            }
            if (paramBean.isNotEmpty("TITLE")) { //标题
                sqlWhereStr.append(" and TITLE like '%");
                sqlWhereStr.append(paramBean.getStr("TITLE"));
                sqlWhereStr.append("%'");
            }
            if (paramBean.isNotEmpty("SERV_NAME")) { //文件种类
                sqlWhereStr.append(" and SERV_NAME like '%");
                sqlWhereStr.append(paramBean.getStr("SERV_NAME"));
                sqlWhereStr.append("%'");
            }
            if (paramBean.isNotEmpty("S_ODEPT")) { //所属机构
                sqlWhereStr.append(" and S_ODEPT = '");
                sqlWhereStr.append(paramBean.getStr("S_ODEPT"));
                sqlWhereStr.append("'");
            }
            if (paramBean.isNotEmpty("S_WF_STATE")) { //办理状态
                sqlWhereStr.append(" and S_WF_STATE = '");
                sqlWhereStr.append(paramBean.getStr("S_WF_STATE"));
                sqlWhereStr.append("'");
            }
            if (paramBean.isNotEmpty("S_EMERGENCY")) { //紧急程度
                sqlWhereStr.append(" and S_EMERGENCY = '");
                sqlWhereStr.append(paramBean.getStr("S_EMERGENCY"));
                sqlWhereStr.append("'");
            }
            if (paramBean.isNotEmpty("S_ATIME_1")) {  //拟稿开始时间
                sqlWhereStr.append(" and S_ATIME >= '");
                sqlWhereStr.append(paramBean.getStr("S_ATIME_1"));
                sqlWhereStr.append("'");
            }
            if (paramBean.isNotEmpty("S_ATIME_2")) { //拟稿结束时间
                sqlWhereStr.append(" and S_ATIME <= '");
                sqlWhereStr.append(paramBean.getStr("S_ATIME_2"));
                sqlWhereStr.append("'");
            }
            return sqlWhereStr.toString();
        } else {
            return null;
        }
    }
}
