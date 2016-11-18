/*
 * Copyright (c) 2011 Ruaho All rights reserved.
 */
package com.rh.core.plug.search.word;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.Context.APP;
import com.rh.core.base.db.QueryCallback;
import com.rh.core.base.db.Transaction;
import com.rh.core.plug.search.client.RhSegClient;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServUtils;
import com.rh.core.util.Constant;
import com.rh.core.util.Lang;

/**
 * 服务全文检索词库服务类
 * 
 * @author Jerry Li
 */
public class WordServ extends CommonServ {
    /** 服务主键 */
    public static final String SERV_ID_SEARCH_WORD = "SY_PLUG_SEARCH_WORD";
    /** 搜狗scel文件路径 */
    private static final String SOUGOU_SCEL_PATH = Context.app(APP.WEBINF_DOC) + Constant.PATH_SEPARATOR
            + SERV_ID_SEARCH_WORD;
    /** log */
    private static Log log = LogFactory.getLog(WordServ.class);

    /**
     * 同步词库
     * @param paramBean 参数Bean
     * @return 服务相关设定信息
     */
    public OutBean syncWord(ParamBean paramBean) {
        int count = 0;
        final Bean param = new Bean();
        if (paramBean.getId().length() > 0) { //同步指定词
            String ids = paramBean.getId().replaceAll(",", "','");
            param.set(Constant.PARAM_WHERE, " and WORD_ID in ('" + ids + "')");
        } else { //同步所有未同步的词
            param.set("WORD_FLAG", Constant.NO_INT);
        }
        List<Bean> wordList = ServDao.finds(SERV_ID_SEARCH_WORD, param, new QueryCallback() {
            public void call(List<Bean> columns, Bean data) {
                RhSegClient.getInstance().addWord(data.getStr("WORD_CODE"));
                param.set("_COUNT_", param.getInt("_COUNT_") + 1);
            }
        });
        count = param.getInt("_COUNT_");
        if (count > 0) {
        	RhSegClient.getInstance().commit();
        	String sql = "update SY_PLUG_SEARCH_WORD set WORD_FLAG=1 where WORD_CODE=#WORD_CODE#";
            Transaction.getExecutor().executeBatchBean(sql, wordList);
        }
        OutBean outBean = new OutBean();
        outBean.setOk(Context.getSyMsg("SY_SYNC_OK", String.valueOf(count)));
        return outBean;
    }

    /**
     * 去除重复词，目前仅支持Oracle
     * @param paramBean 参数Bean
     * @return 服务相关设定信息
     */
    public OutBean removeSame(ParamBean paramBean) {
        String sql = "delete from SY_PLUG_SEARCH_WORD where rowid not in (select max(rowid) from SY_PLUG_SEARCH_WORD"
                + " group by WORD_CODE)";
        int count = Transaction.getExecutor().execute(sql);
        OutBean outBean = new OutBean();
        outBean.setOk(Context.getSyMsg("SY_DELETE_OK", String.valueOf(count)));
        return outBean;
    }

    /**
     * 重置同步状态
     * @param paramBean 参数Bean
     * @return 服务相关设定信息
     */
    public OutBean resetFlag(ParamBean paramBean) {
        String sql = "update SY_PLUG_SEARCH_WORD set WORD_FLAG=2 where 1=1 ";
        if (paramBean.getId().length() > 0) {
            sql += "and WORD_ID in('" + paramBean.getId().replaceAll(",", "','") + "')";
        } else if (!paramBean.isEmpty("_searchWhere")) {
            sql += ServUtils.replaceSysVars(paramBean.getStr("_searchWhere"));
        }
        int count = Transaction.getExecutor().execute(sql);
        OutBean outBean = new OutBean();
        outBean.setOk(Context.getSyMsg("SY_BATCHSAVE_OK", String.valueOf(count)));
        return outBean;
    }
    
    /**
     * 导入搜狗词库，文件的存放路径位于WEB-iNF/doc/SY_PLUG_SEARCH_WORD/下
     * @param paramBean 参数Bean
     * @return 服务相关设定信息
     */
    @SuppressWarnings("unchecked")
    public OutBean impSougou(ParamBean paramBean) {
        int count = 0;
        try {
            String fileName = SOUGOU_SCEL_PATH;
            File file = new File(fileName);
            if (file.exists()) {
                Collection<File> files = FileUtils.listFiles(file, new String[] { "scel" }, false);
                for (File f : files) {
                    SougouReader rd = new SougouReader();
                    Bean reader = rd.read(f);
                    String sql = "insert into SY_PLUG_SEARCH_WORD(WORD_ID,WORD_CODE,WORD_LENGTH,WORD_ORDER,"
                            + "WORD_TYPE,WORD_FLAG) values(#WORD_ID#,#WORD_CODE#,#WORD_LENGTH#,#WORD_ORDER#,'"
                            + reader.getStr("TYPE") + "',#WORD_FLAG#)";
                    count += Context.getExecutor().executeBatchBean(sql, (List<Bean>) reader.get("WORD"));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        OutBean outBean = new OutBean();
        outBean.setOk(Context.getSyMsg("SY_IMPORT_OK", String.valueOf(count)));
        return outBean;
    }

    /**
     * 导入系统常用词
     * @param paramBean 参数Bean
     * @return 服务相关设定信息
     */
    public OutBean impOrg(ParamBean paramBean) {
        int count = 0;
        // 导入公司词
        final List<Bean> dataList = new ArrayList<Bean>();
        String sql = "select CMPY_NAME,CMPY_SORT from SY_ORG_CMPY";
        Transaction.getExecutor().query(sql, new QueryCallback() {
            public void call(List<Bean> columns, Bean data) {
                Bean word = new Bean();
                word.set("WORD_ID", Lang.getUUID());
                word.set("WORD_CODE", data.getStr("CMPY_NAME").trim());
                word.set("WORD_LENGTH", word.getStr("WORD_CODE").length());
                word.set("WORD_ORDER", word.get("CMPY_SORT"));
                word.set("WORD_TYPE", "SY_ORG_CMPY");
                dataList.add(word);
            }
        });
        String insertSql = "insert into SY_PLUG_SEARCH_WORD(WORD_ID,WORD_CODE,WORD_LENGTH,WORD_ORDER,"
                + "WORD_TYPE,WORD_FLAG) values(#WORD_ID#,#WORD_CODE#,#WORD_LENGTH#,#WORD_ORDER#,"
                + "#WORD_TYPE#,2)";
        count += Context.getExecutor().executeBatchBean(insertSql, dataList);

        // 导入部门词
        dataList.clear();
        sql = "select DEPT_NAME,DEPT_SORT from SY_ORG_DEPT";
        Transaction.getExecutor().query(sql, new QueryCallback() {
            public void call(List<Bean> columns, Bean data) {
                Bean word = new Bean();
                word.set("WORD_ID", Lang.getUUID());
                word.set("WORD_CODE", data.getStr("DEPT_NAME").trim());
                word.set("WORD_LENGTH", word.getStr("WORD_CODE").length());
                word.set("WORD_ORDER", word.get("DEPT_SORT"));
                word.set("WORD_TYPE", "SY_ORG_DEPT");
                dataList.add(word);
            }
        });
        insertSql = "insert into SY_PLUG_SEARCH_WORD(WORD_ID,WORD_CODE,WORD_LENGTH,WORD_ORDER,"
                + "WORD_TYPE,WORD_FLAG) values(#WORD_ID#,#WORD_CODE#,#WORD_LENGTH#,#WORD_ORDER#,"
                + "#WORD_TYPE#,2)";
        count += Context.getExecutor().executeBatchBean(insertSql, dataList);

        // 导入用户词
        dataList.clear();
        sql = "select USER_NAME, min(USER_SORT) USER_SORT from SY_ORG_USER group by USER_NAME";
        Transaction.getExecutor().query(sql, new QueryCallback() {
            public void call(List<Bean> columns, Bean data) {
                String name = data.getStr("USER_NAME").trim();
                int len = name.length();
                if (len > 0) {
                    Bean word = new Bean();
                    word.set("WORD_ID", Lang.getUUID());
                    word.set("WORD_CODE", name);
                    word.set("WORD_LENGTH", len);
                    word.set("WORD_ORDER", word.get("USER_SORT"));
                    word.set("WORD_TYPE", "SY_ORG_USER");
                    dataList.add(word);
                    String[] names = splitName(name);
                    if (names[0].length() > 0) { //拆出姓和名
                        Bean xing = new Bean();
                        xing.set("WORD_ID", Lang.getUUID());
                        xing.set("WORD_CODE", names[0].trim());
                        xing.set("WORD_LENGTH", xing.getStr("WORD_CODE").length());
                        xing.set("WORD_TYPE", "姓");
                        dataList.add(xing);
                        Bean ming = word.copyOf(new String[]{"WORD_ORDER", "WORD_TYPE"});
                        ming.set("WORD_ID", Lang.getUUID());
                        ming.set("WORD_CODE", names[1].trim());
                        ming.set("WORD_LENGTH", ming.getStr("WORD_CODE").length());
                        dataList.add(ming);
                    }
                }
            }
        });
        insertSql = "insert into SY_PLUG_SEARCH_WORD(WORD_ID,WORD_CODE,WORD_LENGTH,WORD_ORDER,"
                + "WORD_TYPE,WORD_FLAG) values(#WORD_ID#,#WORD_CODE#,#WORD_LENGTH#,#WORD_ORDER#,"
                + "#WORD_TYPE#,2)";
        count += Context.getExecutor().executeBatchBean(insertSql, dataList);
        OutBean outBean = new OutBean();
        outBean.setOk(Context.getSyMsg("SY_IMPORT_OK", String.valueOf(count)));
        return outBean;
    }

    /**
     * 导入任意服务的指定字段值为搜索用词
     * @param paramBean 参数Bean
     * @return 服务相关设定信息
     */
    public OutBean impServ(ParamBean paramBean) {
        int count = 0;
        String servItem = paramBean.getStr("SERV_ITEM");
        int pos = servItem.indexOf(".");
        final String serv = servItem.substring(0, pos);
        final String item = servItem.substring(pos + 1);
        Bean param = new Bean();
        param.set(Constant.PARAM_SELECT, item);
        Bean servDef = ServUtils.getServDef(serv);
        final List<Bean> dataList = new ArrayList<Bean>();
        String sql = "select " + item + " from " + servDef.getStr("TABLE_VIEW");
        String dsName = servDef.getStr("SERV_DATA_SOURCE");
        Connection conn = Context.getConn(dsName);
        try {
            Context.getExecutor(dsName).query(conn, sql, null, new QueryCallback() {
                public void call(List<Bean> columns, Bean data) {
                    Bean word = new Bean();
                    word.set("WORD_ID", Lang.getUUID());
                    word.set("WORD_CODE", data.getStr(item).trim());
                    word.set("WORD_LENGTH", word.getStr("WORD_CODE").length());
                    word.set("WORD_TYPE", serv);
                    dataList.add(word);
                }
            });
        } finally {
            Context.endConn(conn);
        }
        String insertSql = "insert into SY_PLUG_SEARCH_WORD(WORD_ID,WORD_CODE,WORD_LENGTH,WORD_ORDER,"
                + "WORD_TYPE,WORD_FLAG) values(#WORD_ID#,#WORD_CODE#,#WORD_LENGTH#,#WORD_ORDER#,"
                + "#WORD_TYPE#,2)";
        count += Context.getExecutor().executeBatchBean(insertSql, dataList);
        OutBean outBean = new OutBean();
        outBean.setOk(Context.getSyMsg("SY_IMPORT_OK", String.valueOf(count)));
        return outBean;
    }
    
    /** 复姓数组 */
    public static final String[] NAME_FUXING = new String[] { "安陵", "安平", "安期", "安阳白马", "百里", "柏侯",
        "鲍俎", "北宫", "北郭", "北门", "北山", "北唐", "奔水", "逼阳", "宾牟", "薄奚", "薄野", "曹牟", "曹丘", 
        "常涛", "长鱼", "车非", "成功", "成阳", "乘马", "叱卢", "丑门", "樗里", "穿封", "淳子", "答禄", "达勃", 
        "达步", "达奚", "淡台", "邓陵", "第五", "地连", "地伦", "东方", "东里", "东南", "东宫", "东门", "东乡",
        "东丹", "东郭", "东陵", "东关", "东闾", "东阳", "东野", "东莱", "豆卢", "斗于", "都尉", "独孤", "端木", 
        "段干", "多子", "尔朱", "方雷", "丰将", "封人", "封父", "夫蒙", "夫馀", "浮丘", "傅余", "干已", "高车", 
        "高陵", "高堂", "高阳", "高辛", "皋落", "哥舒", "盖楼", "庚桑", "梗阳", "宫孙", "公羊", "公良", "公孙", 
        "公罔", "公西", "公冶", "公敛", "公梁", "公输", "公上", "公山", "公户", "公玉", "公仪", "公仲", "公坚",
        "公伯", "公祖", "公乘", "公晰", "公族", "姑布", "古口", "古龙", "古孙", "谷梁", "谷浑", "瓜田", "关龙", 
        "鲑阳", "归海", "函治", "韩馀", "罕井", "浩生", "浩星", "纥骨", "纥奚", "纥于", "贺拨", "贺兰", "贺楼", 
        "赫连", "黑齿", "黑肱", "侯冈", "呼延", "壶丘", "呼衍", "斛律", "胡非", "胡母", "胡毋", "皇甫", "皇父", 
        "兀官", "吉白", "即墨", "季瓜", "季连", "季孙", "茄众", "蒋丘", "金齿", "晋楚", "京城", "泾阳", "九百", 
        "九方", "睢鸠", "沮渠", "巨母", "勘阻", "渴侯", "渴单", "可汗", "空桐", "空相", "昆吾", "老阳", "乐羊", 
        "荔菲", "栎阳", "梁丘", "梁由", "梁馀", "梁垣", "陵阳", "伶舟", "冷沦", "令狐", "刘王", "柳下", "龙丘", 
        "卢妃", "卢蒲", "鲁步", "陆费", "角里", "闾丘", "马矢", "麦丘", "茅夷", "弥牟", "密革", "密茅", "墨夷", 
        "墨台", "万俊", "昌顿", "慕容", "木门", "木易", "南宫", "南郭", "南门", "南荣", "欧侯", "欧阳", "逄门", 
        "盆成", "彭祖", "平陵", "平宁", "破丑", "仆固", "濮阳", "漆雕", "奇介", "綦母", "綦毋", "綦连", "祁连", 
        "乞伏", "绮里", "千代", "千乘", "勤宿", "青阳", "丘丽", "丘陵", "屈侯", "屈突", "屈男", "屈卢", "屈同", 
        "屈门", "屈引", "壤四", "扰龙", "容成", "汝嫣", "萨孤", "三饭", "三闾", "三州", "桑丘", "商瞿", "上官", 
        "尚方", "少师", "少施", "少室", "少叔", "少正", "社南", "社北", "申屠", "申徒", "沈犹", "胜屠", "石作", 
        "石牛", "侍其", "士季", "士弱", "士孙", "士贞", "叔孙", "叔先", "叔促", "水丘", "司城", "司空", "司寇", 
        "司鸿", "司马", "司徒", "司士", "似和", "素和", "夙沙", "孙阳", "索阳", "索卢", "沓卢", "太史", "太叔",
        "太阳", "澹台", "唐山", "堂溪", "陶丘", "同蹄", "统奚", "秃发", "涂钦", "吐火", "吐贺", "吐万", "吐罗", 
        "吐门", "吐难", "吐缶", "吐浑", "吐奚", "吐和", "屯浑", "脱脱", "拓拨", "完颜", "王孙", "王官", "王人", 
        "微生", "尾勺", "温孤", "温稽", "闻人", "屋户", "巫马", "吾丘", "无庸", "无钩", "五鹿", "息夫", "西陵", 
        "西乞", "西钥", "西乡", "西门", "西周", "西郭", "西方", "西野", "西宫", "戏阳", "瑕吕", "霞露", "夏侯",
        "鲜虞", "鲜于", "鲜阳", "咸丘", "相里", "解枇", "谢丘", "新垣", "辛垣", "信都", "信平", "修鱼", "徐吾", 
        "宣于", "轩辕", "轩丘", "阏氏", "延陵", "罔法", "铅陵", "羊角", "耶律", "叶阳", "伊祁", "伊耆", "猗卢", 
        "义渠", "邑由", "因孙", "银齿", "尹文", "雍门", "游水", "由吾", "右师", "宥连", "於陵", "虞丘", "盂丘", 
        "宇文", "尉迟", "乐羊", "乐正", "运奄", "运期", "宰父", "辗迟", "湛卢", "章仇", "仉督", "长孙", "长儿", 
        "真鄂", "正令", "执头", "中央", "中长", "中行", "中野", "中英", "中梁", "中垒", "钟离", "钟吾", "终黎", 
        "终葵", "仲孙", "仲长", "周阳", "周氏", "周生", "朱阳", "诸葛", "主父", "颛孙", "颛顼", "訾辱", "淄丘", 
        "子言", "子人", "子服", "子家", "子桑", "子叔", "子车", "子阳", "宗伯", "宗正", "宗政", "尊卢", "昨和", 
        "左人", "左丘", "左师", "左行", "刘文", "额尔", "达力", "蔡斯", "浩赏", "斛斯", "夹谷", "揭阳" };

    /**
     * 将人名拆为姓和名的字符串数组
     * @param name 姓名
     * @return 姓和名的数组
     */
    private String[] splitName(String name) {
        String[] result = new String[2];
        int len = name.length();
        if (len == 2) {
            result[0] = name.substring(0, 1);
            result[1] = name.substring(1);
        } else if (name.length() == 3 || name.length() == 4) {
            for (int i = 0; i < NAME_FUXING.length; i++) {
                if (name.startsWith(NAME_FUXING[i])) {
                    result[0] = NAME_FUXING[i];
                    result[1] = name.substring(2);
                    break;
                }
            }
            if (result[0] == null) { //没有匹配上复姓，则采用单姓
                result[0] = name.substring(0, 1);
                result[1] = name.substring(1);
            }
        } else {
            result = new String[] {"", ""};
        }
        return result;
    }
}
