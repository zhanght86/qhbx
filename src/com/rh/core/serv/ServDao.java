/*
 * Copyright (c) 2011 Ruaho All rights reserved.
 */
package com.rh.core.serv;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.base.db.QueryCallback;
import com.rh.core.base.db.RowHandler;
import com.rh.core.base.db.Transaction;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;
import com.rh.core.util.Lang;

/**
 * 面向数据表对象的数据载体，可以存放各种数据库的内容
 * @author Jerry Li
 * @version $Id$
 * */

public class ServDao extends Bean {

    /**
     * UID
     */
    private static final long serialVersionUID = -1630711884604888358L;

    /**
     * 私有化空构建体方法，避免没有SERV_ID创建servBean
     */
    @SuppressWarnings("unused")
    private ServDao() {
    }

    /**
     * 通过服务ID构建服务数据对象
     * @param servId 服务主键
     */
    public ServDao(String servId) {
        // 通过服务Id获取服务Bean
        set("_SERV_ID", servId);
    }
    
    /**
     * 通过服务ID和数据ID构建服务数据对象
     * @param servId 服务主键
     * @param dataId 数据主键
     */
    public ServDao(String servId, String dataId) {
        // 通过服务Id获取服务Bean
        set("_SERV_ID", servId);
        setId(dataId);
    }

    /**
     * 通过服务Id和数据信息构建服务对象
     * @param servId 服务Id
     * @param dataBean 数据信息Bean
     */
    public ServDao(String servId, Bean dataBean) {
        super(dataBean);
        set("_SERV_ID", servId);
    }

    /**
     * 获取服务编码
     * @return 服务编码
     */
    public String getServId() {
        return getStr("_SERV_ID");
    }
    
    /**
     * 修改当前数据
     * @return 是否成功修改
     */
    public boolean update() {
        String servId = getServId();
        ServDefBean servDef = ServUtils.getServDef(servId);
        set("S_MTIME", DateUtils.getDatetimeTS());
        String psql = Transaction.getBuilder().updateByid(servDef, this);
        boolean result = Transaction.getExecutor()
                .execute(psql, this.getList(Constant.PARAM_PRE_VALUES)) > 0 ? true : false;
        if (result) {
            String key = servDef.getPKey();
            if (contains(key)) { //设置主键字段
                setId(getStr(key));
            }
            servDef.clearDataCache(getId()); //清除缓存
        }
        return result;
    }

    /**
     * 根据是否启用假删除设置，如果启用，假删除当前数据，没有启用则真删除。
     * @return 是否删除成功
     */
    public boolean delete() {
        String servId = getServId();
        ServDefBean servDef = ServUtils.getServDef(servId);
        boolean trueDel = true;
        if (servDef.hasFalseDelete()) {
            trueDel = false; //启用假删除
        }
        String psql = Transaction.getBuilder().deleteByid(servDef, this, trueDel);
        boolean result = Transaction.getExecutor().execute(psql, this.getList(Constant.PARAM_PRE_VALUES)) 
                > 0 ? true : false;
        if (result) {
            servDef.clearDataCache(getId()); //清除缓存
        }
        return result;
    }

    /**
     * 真删除当前数据
     * @return 是否删除成功
     */
    public boolean destroy() {
        String servId = getServId();
        ServDefBean servDef = ServUtils.getServDef(servId);
        String psql = Transaction.getBuilder().deleteByid(servDef, this, true);
        boolean result = Transaction.getExecutor().execute(psql, this.getList(Constant.PARAM_PRE_VALUES)) 
                > 0 ? true : false;
        if (result) {
            servDef.clearDataCache(getId()); //清除缓存
        }
        return result;
    }
    
    /**
     * 插入当前数据
     * @return 是否插入成功
     */
    public boolean insert() {
        String servId = getServId();
        ServDefBean servDef = ServUtils.getServDef(servId);
        String psql = Transaction.getBuilder().insert(servDef, this);
        //进行唯一组约束的判断
        String uniqueStr = ServUtils.checkUniqueExists(servDef, this, true);
        if (uniqueStr != null) {
            throw new TipException(Context.getSyMsg("SY_SAVE_UNIQUE_EXISTS", uniqueStr));
        }
        boolean result = Transaction.getExecutor()
                .execute(psql, this.getList(Constant.PARAM_PRE_VALUES)) > 0 ? true : false;
        if (result) {
            setId(getStr(servDef.getPKey())); //设置主键字段
        }
        return result;
    }

    /**
     * 从数据库装载当前对象，要求提前设定了servId和dataId
     * @return 是否存在数据，不存在则返回false
     */
    public boolean load() {
        ServDefBean servDef = ServUtils.getServDef(getServId());
        String psql = Transaction.getBuilder().selectByid(servDef);
        Bean result = Transaction.getExecutor().queryOne(psql, Lang.asList(this.getId()));
        if (result != null) {
            putAll(result);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 保存数据，自动根据是否设定了主键来判断是添加还是修改保存
     * @param servId 服务主键
     * @param dataBean 数据信息
     * @return 保存后的数据
     */
    public static ServDao save(String servId, Bean dataBean) {
        if (dataBean.getId().length() > 0) {
            return update(servId, dataBean);
        } else {
            return create(servId, dataBean);
        }
    }
    
    /**
     * 直接创建一个servBean并插入到数据库
     * @param servId 服务主键
     * @param dataBean 数据信息
     * @return 已经插入的servBean
     */
    public static ServDao create(String servId, Bean dataBean) {
        ServDao servBean = new ServDao(servId, dataBean);
        if (servBean.insert()) {
            servBean.remove(Constant.PARAM_PRE_VALUES);  //清除Pre value
            return servBean;
        } else {
            return null;
        }
    }
    
    /**
     * 根据Bean的信息执行更新操作
     * @param servId 服务主键
     * @param dataBean 数据信息
     * @return 已经插入的servBean
     */
    public static ServDao update(String servId, Bean dataBean) {
        ServDao servBean = new ServDao(servId, dataBean);
        if (servBean.update()) {
            return servBean;
        } else {
            return null;
        }
    }

    /**
     * 根据是否启用假删除设置，如果启用，假删除当前数据，没有启用则真删除。
     * @param servId 服务主键
     * @param dataBean 数据信息
     * @return 删除结果
     */
    public static boolean delete(String servId, Bean dataBean) {
        ServDao servBean = new ServDao(servId, dataBean);
        return servBean.delete();
    }
    
    /**
     * 真删除当前数据
     * @param servId 服务主键
     * @param dataBean 数据信息
     * @return 删除结果
     */
    public static boolean destroy(String servId, Bean dataBean) {
        ServDao servBean = new ServDao(servId, dataBean);
        return servBean.destroy();
    }    

    /**
     * 批量插入数据库
     * @param servId 服务主键
     * @param beans 数据信息
     * @return 成功插入的数量
     */
    public static int creates(String servId, List<Bean> beans) {
        ServDefBean servDef = ServUtils.getServDef(servId);
        int count = 0;
        String psql = null;
        List<List<Object>> params = new ArrayList<List<Object>>();
        for (Bean dataBean : beans) {
            psql = Transaction.getBuilder().insert(servDef, dataBean);
            params.add(dataBean.getList(Constant.PARAM_PRE_VALUES));
        }
        if (psql != null) { // 多个对象执行批处理方法
            count = Transaction.getExecutor().executeBatch(psql, params);
        }
        return count;
    }
    
    /**
     * 根据主键获取数据记录，不包含关联子数据
     * @param servId 服务Id
     * @param dataId 数据主键，多主键采用数字方式提供参数
     * @return 数据记录,如果不存在则返回null
     */
    public static Bean find(String servId, String dataId) {
        return find(servId, dataId, false);
    }
    
    /**
     * 根据主键获取数据记录，多主键按照顺序提供数组
     * @param servId 服务Id
     * @param dataId 数据主键，多主键采用数字方式提供参数
     * @param withLinks 是否包含关联数据，true:包含关联定义所有子；false：不包含关联数据
     * @return 数据记录,如果不存在则返回null
     */
    public static Bean find(String servId, String dataId, boolean withLinks) {
        Bean result;
        ServDefBean servDef = ServUtils.getServDef(servId);
        if (!withLinks && servDef.hasCache()) {
            result = (Bean) servDef.getDataCache(dataId);
        } else {
            result = null;
        }
        if (result == null) {
            String psql = Transaction.getBuilder().selectByid(servDef);
            result = Transaction.getExecutor().queryOne(psql, Lang.asList(dataId));
            if (result != null) {
                if (withLinks) {
                    //获取关联数据，并设置到结果对象中
                    LinkedHashMap<String, Bean> links = servDef.getAllLinks();
                    for (String key : links.keySet()) {
                        Bean link = links.get(key);
                        if (link.getInt("LINK_SHOW_TYPE") == Constant.LINK_SHOW_TYPE_URL) { //url关联不获取数据
                            continue;
                        }
                        if (!link.isEmpty("LINK_EXPRESSION")) { //处理表达式，禁止不符合规则的数据获取
                            if (!Lang.isTrueScript(ServUtils.replaceSysAndData(link.getStr("LINK_EXPRESSION"), 
                                    result))) { //表达式不为true，则不允许获取数据
                                continue;
                            }
                        }
                        List<Bean> dataList = ServUtils.getLinkDataList(servId, link, result, 1);
                        result.set(link.getStr("LINK_SERV_ID"), dataList);
                    }
                } else if (servDef.hasCache()) {
                    servDef.setDataCache(dataId, result); //设置缓存
                }
            }
        }
        return result;
    }

    /**
     * 根据参数获取数据记录
     * @param servId 服务Id
     * @param paramBean 参数信息
     * @return 数据记录,如果不存在则返回null
     */
    public static Bean find(String servId, Bean paramBean) {
        Bean result = null;
        Object key = null;
        ServDefBean servDef = ServUtils.getServDef(servId);
        boolean cacheFlag = servDef.hasCache();
        if (cacheFlag) {
            key = paramBean.getId().length() > 0 ? paramBean.getId() : paramBean.get(servDef.getPKey());
            if (key != null) {
                result = (Bean) servDef.getDataCache(key);
            }
        }
        if (result == null) {
            String psql = Transaction.getBuilder().select(servDef, paramBean);
            result = Transaction.getExecutor().queryOne(psql, paramBean.getList(Constant.PARAM_PRE_VALUES));
            if ((result != null) && cacheFlag && key != null) {
                servDef.setDataCache(key, result); //设置缓存
            }
        }
        return result;
    }
    
    /**
     * 根据where条件获取数据记录集
     * @param servId 服务Id
     * @param where and起始的where条件语句
     * @return 数据记录集
     */
    public static List<Bean> finds(String servId, String where) {
        Bean paramBean = new Bean();
        paramBean.set(Constant.PARAM_WHERE, where);
        return ServDao.finds(servId, paramBean);
    }
    
    /**
     * 根据where条件获取数据记录集，支持3级以内的级联查询，需要指定PARAM_LINK_FLAG为true
     * @param servId 服务Id
     * @param paramBean 参数信息
     * @return 数据记录集
     */
    public static List<Bean> finds(final String servId, Bean paramBean) {
        QueryCallback qc = null;
        final ServDefBean serv = ServUtils.getServDef(servId);
        final int linkLevel = paramBean.getInt(Constant.PARAM_LINK_LEVEL);
        if (paramBean.getBoolean(Constant.PARAM_LINK_FLAG) && linkLevel < 3) { //设定了级联查找，向下2层级内获取关联数据
            qc = new QueryCallback() {
                public void call(List<Bean> columns, Bean data) {
                    //获取关联数据，并设置到结果对象中
                    LinkedHashMap<String, Bean> links = serv.getAllLinks();
                    for (String key : links.keySet()) {
                        Bean link = links.get(key);
                        if (link.getInt("LINK_SHOW_TYPE") == Constant.LINK_SHOW_TYPE_URL) { //url关联不获取数据
                            continue;
                        }
                        if (!link.isEmpty("LINK_EXPRESSION")) { //处理表达式，禁止不符合规则的数据获取
                            if (!Lang.isTrueScript(ServUtils.replaceSysAndData(link.getStr("LINK_EXPRESSION"), 
                                    data))) { //表达式不为true，则不允许获取数据
                                continue;
                            }
                        }
                        List<Bean> dataList = ServUtils.getLinkDataList(servId, link, data, linkLevel);
                        data.set(link.getStr("LINK_SERV_ID"), dataList);
                    }
                } //end call
            };
        }
        return finds(servId, paramBean, qc);
    }
    
    /**
     * 根据where条件获处理数据记录集
     * @param servId 服务Id
     * @param paramBean 参数信息
     * @param rowHandler 行处理器
     */
    public static void findsCall(String servId, Bean paramBean, RowHandler rowHandler) {
        ServDefBean servDef = ServUtils.getServDef(servId);
        String psql = Transaction.getBuilder().select(servDef, paramBean);
        int rowNum = paramBean.get(Constant.PARAM_ROWNUM, -1);  //兼容以前方法，不建议使用此参数
        rowNum = paramBean.get(Constant.SHOWNUM, rowNum);
        int page = paramBean.get(Constant.NOWPAGE, 1);
        int start = (page - 1) * rowNum + 1;
        Transaction.getExecutor().queryCall(psql, start, rowNum,
                paramBean.getList(Constant.PARAM_PRE_VALUES), rowHandler);
    }
    
    /**
     * 根据where条件获取数据记录集
     * @param servId 服务Id
     * @param paramBean 参数信息
     * @param qc 回调方法
     * @return 数据记录集
     */
    public static List<Bean> finds(String servId, Bean paramBean, QueryCallback qc) {
        ServDefBean servDef = ServUtils.getServDef(servId);
        List<Bean> result;
        String psql = Transaction.getBuilder().select(servDef, paramBean);
        int rowNum = paramBean.get(Constant.PARAM_ROWNUM, -1);  //兼容以前方法，不建议使用此参数
        rowNum = paramBean.get(Constant.SHOWNUM, rowNum);
        int page = paramBean.get(Constant.NOWPAGE, 1);
        int start = (page - 1) * rowNum + 1;
        result = Transaction.getExecutor().query(psql, start, rowNum,
                paramBean.getList(Constant.PARAM_PRE_VALUES), qc);
        return result;
    }
    
    /**
     * 根据paramBean参数组装的where条件获取数据的数量
     * @param servId 服务Id
     * @param paramBean 参数信息
     * @return 符合条件的数据数量
     */
    public static int count(String servId, Bean paramBean) {
        ServDefBean servDef = ServUtils.getServDef(servId);
        paramBean.set(Constant.PARAM_SELECT, " count(*) COUNT_ ");
        String psql = Transaction.getBuilder().select(servDef, paramBean);
        return Transaction.getExecutor().count(psql, paramBean.getList(Constant.PARAM_PRE_VALUES));
    }
    
    /**
     * 根据参数进行删除处理
     * @param servId 服务Id
     * @param paramBean 参数信息
     * @return 数据记录集
     */
    public static int deletes(String servId, Bean paramBean) {
        ServDefBean servDef = ServUtils.getServDef(servId);
        boolean trueDel = true;
        if (servDef.hasFalseDelete()) {
            trueDel = false; //启用假删除
        }
        paramBean.remove(Constant.PARAM_PRE_VALUES);
        String psql = Transaction.getBuilder().delete(servDef, paramBean, trueDel);
        int  count = Transaction.getExecutor().execute(psql, paramBean.getList(Constant.PARAM_PRE_VALUES));
        if (count > 0) { //清除缓存
            servDef.clearDataCache();
        }
        return count;
    }
    
    /**
     * 根据参数进行真删除处理
     * @param servId 服务Id
     * @param paramBean 参数信息
     * @return 数据记录集
     */
    public static int destroys(String servId, Bean paramBean) {
        ServDefBean servDef = ServUtils.getServDef(servId);
        paramBean.remove(Constant.PARAM_PRE_VALUES);
        String psql = Transaction.getBuilder().delete(servDef, paramBean, true);
        int count = Transaction.getExecutor().execute(psql, paramBean.getList(Constant.PARAM_PRE_VALUES));
        if (count > 0) { //清除缓存
            servDef.clearDataCache();
        }
        return count;
    }
    
    /**
     * 根据参数进行真删除处理
     * @param servId 服务Id
     * @param setBean 更新数据项的参数信息
     * @param whereBean 过滤条件数据项参数信息
     * @return 数据记录集
     */
    public static int updates(String servId, Bean setBean, Bean whereBean) {
        ServDefBean servDef = ServUtils.getServDef(servId);
        setBean.remove(Constant.PARAM_PRE_VALUES);
        String psql = Transaction.getBuilder().update(servDef, setBean, whereBean);
        int count = Transaction.getExecutor().execute(psql, setBean.getList(Constant.PARAM_PRE_VALUES));
        if (count > 0) { //清除缓存
            servDef.clearDataCache();
        }
        return count;
    }
}
