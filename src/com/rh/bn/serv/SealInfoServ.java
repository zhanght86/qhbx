package com.rh.bn.serv;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.io.IOUtils;

import com.rh.core.base.Bean;
import com.rh.core.base.BeanUtils;
import com.rh.core.base.Context;
import com.rh.core.comm.FileMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServDefBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.ServUtils;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.serv.dict.DictMgr;
import com.rh.core.util.DateUtils;
/**
 * 印章基本信息
 * @author lidongdong
 *
 */
public class SealInfoServ extends CommonServ{
    private static final Class<String> ArrayList = null;
    private static final String SY_ORG_USER = "SY_ORG_USER";
    private static String sealDept = "";
    private static final String SY_ORG_DEPT = "SY_ORG_DEPT";
    //保管人变更记录表
    private static final String BN_SEAL_KEEPER_CHANGE = "BN_SEAL_KEEPER_CHANGE";
    /**
     * 将印章信息保存在session中
     * @param paramBean
     * @return
     */
    public OutBean saveSealList(ParamBean paramBean) {
        OutBean resultBean = new OutBean(); 
        HttpServletRequest httpServletRequest = (HttpServletRequest) Context.getRequest();
        String sealList = paramBean.getStr("sealList");
        httpServletRequest.getSession().setAttribute("sealList", sealList);
        return resultBean.setOk("操作成功！");
    }
    
    protected void beforeQuery(ParamBean paramBean) {
    	List<Bean> treeWhereBeans = paramBean.getList("_treeWhere");
    	for(Bean treeWhereBean : treeWhereBeans){
    		//导航树如果选择的是总公司，只显示总公司的印章，不显示总公司及以下
    		if("0001B210000000000BU3".equals(treeWhereBean.getStr("DICT_VALUE"))
    				&& "KEEP_ODEPT_CODE".equals(treeWhereBean.getStr("DICT_ITEM"))){
    			String servId = paramBean.getServId();
    			ServDefBean serv = ServUtils.getServDef(servId);
    			StringBuilder where0 = new StringBuilder();
    			where0.append(ServUtils.getTreeWhere(serv, (List<Bean>) paramBean.get("_treeWhere")));
    			where0 = where0.replace(where0.indexOf(" like "), where0.indexOf(" like ")+6, " = ");
    			where0 = where0.replace(where0.indexOf("^%"), where0.indexOf("^%")+2, "^");
    			paramBean.set("_treeWhere", "");
    			paramBean.set("_WHERE_",where0);
    		}
    	}
    	super.beforeQuery(paramBean);
    }
    
    protected void beforeSave(ParamBean paramBean) {
        /*
         * 初始化保管人
         * 添加模式下直接保存当前保管人为保管人
         * 非添加模式下判断数据库中该字段是否为空，如果为空，则保存保管人入库。
         */
        if(paramBean.getAddFlag()){
            Bean userBean = 
                    ServDao.find(SY_ORG_USER, paramBean.getStr("SEAL_OWNER_USER"));
            innerKeeper(userBean,paramBean);
        }else{
            Bean sealBean = 
                    ServDao.find(paramBean.getServId(), paramBean.getId());
            if(sealBean.getStr("SEAL_OWNER_USER").length()==0){
                Bean userBean = 
                        ServDao.find(SY_ORG_USER, paramBean.getStr("SEAL_OWNER_USER"));
                innerKeeper(userBean,paramBean);
            }
        }
    }
    /**
     * 初始化保管人
     * @param userBean
     * @param paramBean 
     */
    private void innerKeeper(Bean userBean, ParamBean paramBean) {
        Bean keeperBean = new ParamBean();
        keeperBean.set("SEAL_ID", paramBean.get("ID"));//印章ID
        keeperBean.set("NOW_USER_CODE", paramBean.get("SEAL_OWNER_USER"));//保管人
        keeperBean.set("NOW_USER_PHONE", userBean.get("USER_MOBILE"));//保管人电话
        keeperBean.set("KEEP_BEGIN_TIME", DateUtils.getDatetime());//开始时间
        keeperBean.set("KEEP_TDEPT_CODE", userBean.get("DEPT_CODE"));//保管部门
        ServDao.save(BN_SEAL_KEEPER_CHANGE, keeperBean);
    }


    /**
     * 从session中获取缓存的印章列表
     * @param paramBean
     * @return
     */
    public  OutBean getSaveSealList(ParamBean paramBean) {
        OutBean resultBean = new OutBean(); 
        HttpServletRequest httpServletRequest = (HttpServletRequest) Context.getRequest();
        Object sealList= httpServletRequest.getSession().getAttribute("sealList");
        return resultBean.set("sealList", sealList);
    }
    /**
     * @author lidongdong
     * 启用
     */
    public OutBean sealStart(ParamBean paramBean){
        String status = paramBean.getStr("SEAL_STATE");
        if("0".equals(status)||"2".equals(status)){
            paramBean.set("SEAL_STATE", "1");
            paramBean.set("SEAL_START_TIME", DateUtils.getDatetime());
            ServDao.update(paramBean.getServId(), paramBean);
            return new OutBean().setOk("启用成功");
        }
        return new OutBean().setError("状态错误,请联系管理员");
    }
    /**
     * @author lidongdong
     * 启用(列表按钮)
     */
    public OutBean sealStartList(ParamBean paramBean){
        boolean flag = false;
        String[] ids = paramBean.getStr("IDS").split(",");
        for (String id : ids) {
            Bean sealBean = ServDao.find(paramBean.getServId(), id);
            String status = sealBean.getStr("SEAL_STATE");
            if("0".equals(status)||"2".equals(status)){
                paramBean.set("SEAL_STATE", "1");
                paramBean.set("SEAL_START_TIME", DateUtils.getDatetime());
                paramBean.setId(id);
                ServDao.update(paramBean.getServId(), paramBean);
                flag=true;
            }else{
                flag=false;
            }
        }
        if(flag){
            return new OutBean().setOk("启用成功");
        }else{
            return new OutBean().setError("启用失败");
        }
    }
    /**
     * @author lidongdong
     * 停用
     */
    public OutBean sealEnd(ParamBean paramBean){
        boolean flag = false;
        String[] ids = paramBean.getStr("IDS").split(",");
        for (String id : ids) {
            Bean sealBean = ServDao.find(paramBean.getServId(), id);
            String status = sealBean.getStr("SEAL_STATE");
            if("1".equals(status)){
                paramBean.set("SEAL_STATE", "2");
                paramBean.set("SEAL_STOP_TIME", DateUtils.getDatetime());
                paramBean.setId(id);
                ServDao.update(paramBean.getServId(), paramBean);
                flag=true;
            }else{
                flag=false;
            }
        }
        if(flag){
            return new OutBean().setOk("停用成功");
        }else{
            return new OutBean().setOk("停用失败");
        }
    }
    /**
     * 导入Excel
     * @param paramBean 参数信息
     * @return 执行结果
     */
    public OutBean imp(ParamBean paramBean) {
        //@todo 调用父类的imp导入方法

        OutBean outBean = new OutBean();
        beforeImp(paramBean); //执行监听方法
        String servId = paramBean.getServId();
        ServDefBean servDef = ServUtils.getServDef(servId);
        LinkedHashMap<String, Bean> titleMap = BeanUtils.toLinkedMap(servDef.getTableItems(), "ITEM_NAME");
        String fileId = paramBean.getStr("fileId");
        Bean fileBean = FileMgr.getFile(fileId);
        if (fileBean != null && fileBean.getStr("FILE_MTYPE").equals("application/vnd.ms-excel")) { //只支持excel类型
            Workbook book = null;
            InputStream in = null;
            try {
                in = FileMgr.download(fileBean);
                //打开文件 
                try {
                    book = Workbook.getWorkbook(in) ;
                } catch(Exception e) {
                    log.error(e.getMessage(), e);
                    throw new RuntimeException("Wrong file format, only suport 2003 and lower version," 
                            + "pls use export excel file as the template!");
                }
                //取得第一个sheet  
                Sheet sheet = book.getSheet(0);  
                //取得行数  
                int rows = sheet.getRows();
                List<Bean> dataList = new ArrayList<Bean>(rows);
                Cell[] titleCell = sheet.getRow(0);
                int cols = titleCell.length;
                Bean[] itemMaps = new Bean[cols];
                for (int j = 0; j < cols; j++) { //第一行标题列，进行标题与字段的自动匹配，优先匹配中文名称，其次配置编码
                    String title = sheet.getCell(j, 0).getContents();
                    Bean itemMap = null;
                    if (titleMap.containsKey(title)) {
                        itemMap = titleMap.get(title);
                    } else {
                        itemMap = servDef.getItem(title);
                    }
                    if (itemMap != null) {
                        itemMaps[j] = itemMap;
                    }
                }
                for(int i = 1; i < rows; i++) {
                    Cell [] cell = sheet.getRow(i);
                    Bean data = new Bean();
                    for(int j = 0; j < cell.length; j++) {
                        if (itemMaps[j] != null) {
                            String value = sheet.getCell(j, i).getContents();
                            if (itemMaps[j].isNotEmpty("DICT_ID")) { //字典处理名称和值的转换
                                if(itemMaps[j].getStr("ITEM_CODE").equals("KEEP_ODEPT_CODE")){
                                    sealDept = value;
                                }
                                String dictVal = getItemCodeByName(itemMaps[j].getStr("DICT_ID"), value);
                                if (dictVal != null) {
                                    value = dictVal;
                                }
                            }
                            data.set(itemMaps[j].getStr("ITEM_CODE"), value);
                        }
                    }
                    dataList.add(data);
                }  
                //关闭文件  
                book.close();
                book = null;
                if (dataList.size() > 0) {
                    ParamBean param = new ParamBean(servId, ServMgr.ACT_BATCHSAVE);
                    param.setBatchSaveDatas(dataList);
                    outBean = ServMgr.act(param);
                } else {
                    outBean.setWarn("");
                }
            } catch (Exception e) {  
                throw new RuntimeException(e.getMessage(), e);  
            } finally {
                if (book != null) {
                    book.close();
                }
                IOUtils.closeQuietly(in);
            }
        } else { //错误的文件内容或格式
            outBean.setWarn("");
        }
        FileMgr.deleteFile(fileBean); //最后删除临时上传的文件
        
    
        //@todo 获取导入后的数据列表，更新数据列表
        String[] saveIds = outBean.getSaveIds().split(",");
        for (String saveId:saveIds) {
            Bean saveBean = ServDao.find(paramBean.getServId(), saveId);
            //根据保存的机构过滤部门，然后根据公司及部门确定保管人及责任人
            Bean odeptCodeBean = ServDao.find(SY_ORG_DEPT, saveBean.getStr("KEEP_ODEPT_CODE"));
            String odeptCode = saveBean.getStr("KEEP_ODEPT_CODE");
            //导入的机构可能是地市或者县支公司，通过机构层级进行递归查询，直到查询到县级公司
            if(odeptCodeBean!=null){
                if(odeptCodeBean.getInt("DEPT_LEVEL")<4){
                    odeptCode = odeptCodeBean.getStr("ODEPT_CODE");
                }else{
                    for (int i = 0; i < odeptCodeBean.getInt("DEPT_LEVEL")-3; i++) {
                        odeptCode = ServDao.find(SY_ORG_DEPT, odeptCode).getStr("DEPT_PCODE");
                    }
                }
            }
            //获取唯一部门
            try {
            List<Bean> deptBeans = ServDao.finds(SY_ORG_DEPT, " and ODEPT_CODE = '"+odeptCode+"' and DEPT_NAME='"+DictMgr.getName(SY_ORG_DEPT, saveBean.getStr("KEEP_TDEPT_CODE"))+"'");
            String deptCode = deptBeans.get(0).getId();
            //获取部门下保管人、责任人编码
            List<Bean> userInfos = ServDao.finds("SY_ORG_USER", " and DEPT_CODE='"+deptCode+"' and USER_NAME='"+DictMgr.getName(SY_ORG_USER, saveBean.getStr("SEAL_OWNER_USER"))+"'");
            //保管人
            String keepUser = userInfos.get(0).getId();
            //责任人
            List<Bean> userRespons = ServDao.finds("SY_ORG_USER", " and ODEPT_CODE='"+odeptCode+"' and USER_NAME='"+DictMgr.getName(SY_ORG_USER, saveBean.getStr("SEAL_RESPONS_USER"))+"'");
            String responsUser = userRespons.get(0).getId();
            saveBean.set("SEAL_OWNER_USER", keepUser);
            saveBean.set("SEAL_RESPONS_USER", responsUser);
            saveBean.set("KEEP_TDEPT_CODE", deptCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //获取印章规格信息，更新印章信息表
            List<Bean> styleBeans = 
                    ServDao.finds("BN_SEAL_STYLE",
                            new SqlBean().set("SEAL_TYPE1", 
                                    saveBean.getStr("SEAL_TYPE1")));
            if (styleBeans.size() == 1) {
                Bean styleBean = styleBeans.get(0);
                saveBean.set("SEAL_FONT", styleBean.get("SEAL_FONT"));//字体排列
                saveBean.set("SEAL_QUALITY", styleBean.get("SEAL_QUALITY"));//印章材质
                saveBean.set("SEAL_WIDTH", styleBean.get("SEAL_WIDTH"));//印章宽度(mm)
                saveBean.set("SEAL_HEIGHT", styleBean.get("SEAL_HEIGHT"));//印章高度(mm)
                saveBean.set("SEAL_COLOR", styleBean.get("SEAL_COLOR"));//字体颜色
                saveBean.set("SEAL_FORM", styleBean.get("SEAL_FORM"));//印章外形
            }
            ServDao.save(paramBean.getServId(), saveBean);
        }
        
        return outBean;
    }
    
    public static String getItemCodeByName(String dictId, String value) {
        Bean dict = DictMgr.getDict(dictId);
        //内部字典
        if(dict.getInt("DICT_IS_INNER")==1){
            List<Bean> ItemBeans = dict.getList(("SY_SERV_DICT_ITEM"));
            for (int j = 0; j < ItemBeans.size(); j++) {
                if(value.equals(ItemBeans.get(j).getStr("ITEM_NAME"))){
                    return ItemBeans.get(j).getStr("ITEM_CODE");
                }
            }
        //外部字典
        }else if(dict.getInt("DICT_IS_INNER")==2){
            List<Bean> ItemBeans = ServDao.finds(dict.getStr("TABLE_ID"), " and "+dict.getStr("DICT_F_NAME")+"='"+value+"'");
            if(dict.getStr("DICT_ID").equals("BN_SEAL_LIBARY_TREE")){
                ItemBeans = ServDao.finds("BN_SEAL_INFO_LEAF_V", "and SEAL_NAME = '"+value + "' and DEPT_NAME = '" + sealDept+"'");
                for (int j = 0; j < ItemBeans.size(); j++) {
                    if(sealDept.equals(ItemBeans.get(j).getStr(dict.getStr("DICT_F_NAME")))){
                        return ItemBeans.get(j).getStr("SEAL_ID");
                    }
                }
            }else{
                for (int j = 0; j < ItemBeans.size(); j++) {
                    if(value.equals(ItemBeans.get(j).getStr(dict.getStr("DICT_F_NAME")))){
                        return ItemBeans.get(j).getStr(dict.getStr("DICT_F_ID"));
                    }
                }
            }
        }
        return null;
    }


    /**
     * 
     * @param paramBean 参数信息
     */
    public void saveInSeal(ParamBean paramBean) {
        Bean seanBean = new ParamBean();
        String filePath = paramBean.getStr("FilePath");
        seanBean = ServDao.find(paramBean.getServId(), paramBean.getId());
        seanBean.set("EKEY_ADDRESS", paramBean.get("index"));
        seanBean.set("EKEYSN", paramBean.get("EkeySn"));
        seanBean.set("SEAL_HAS_ESEAL", "1");
        ServDao.save(paramBean.getServId(), seanBean);
        Bean fileBean = new ParamBean();
        fileBean.set("FILE_PATH", fileBean);
        fileBean.set("SERV_ID", paramBean.getServId());
        fileBean.set("", "");
    }
}
