package com.rh.oa.zh.seal;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.base.db.Transaction;
import com.rh.core.org.DeptBean;
import com.rh.core.org.mgr.OrgMgr;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.lang.Assert;
import com.rh.core.util.lang.ObjectCreator;
import com.rh.oa.gw.GwSealProcess;
import com.rh.oa.zh.seal.axis1.WSServerServiceLocator;

/**
 * 接收吉大正源印章系统发送的请求。 在签章成功以后会通知OA端签章已经完成。向OA端发送一个链接并带有在签章服务器中生成的文档编号和签章时发送过来的唯一编号。
 * 链接："http://ip+port/xxxx.jsp?fileNo="+fileNo+"wyh="+weiyihao;
 * @author yangjy
 * 
 */
public class SealMgr {
    /** 服务编码：盖章文件对应表 **/
    public static final String OA_GW_SEAL_FILE = "OA_GW_SEAL_FILE";
    private static final Log LOG = LogFactory.getLog(SealMgr.class);
    /**
     * 印章系统地址
     */
    public static final String CONF_SEAL_SYS_HOST = "SEAL_SYS_HOST";
    /**
     * 公文默认打印份数
     */
    public static final String CONF_GW_DEFAULT_PRINT_NUM = "GW_DEFAULT_PRINT_NUM";
    
    /** 公文印章实现类 **/
    private static final String GW_SEAL_IMPL = "OA_SEAL_IMPL_CLS";

    /**
     * 接收印章系统盖完章的文件信息，并做一下操作： 1，记录红头红章文件信息。 2，下载黑头黑章文件。
     * @param paramBean 参数Bean
     */
    public static void receiveSealFile(ParamBean paramBean) {
        try {
            Transaction.begin();
            // OA 中原始文件ID，盖章前的Word文件
            String wyh = paramBean.getStr("wyh");
            Assert.hasText(wyh, "wyh 参数不能为空。");
            
            String[] fileIds = wyh.split(";");
            if (fileIds.length != 3) {
                throw new RuntimeException("参数格式不正确。");
            }
            
            GwSealProcess sealProcess = createGwSeal(fileIds[2]);
            sealProcess.afterSeal(paramBean);

            Transaction.commit();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new TipException(e.getMessage());
        } finally {
            Transaction.end();
        }
    }
    
    /**
     * 根据模块名称，取得印章实现类。
     * @param modelName 模块名
     * @return 印章系统实现类
     */
    public static GwSealProcess createGwSeal(String modelName) {
        String key = GW_SEAL_IMPL + "." + modelName;
        String gwSealCls = Context.getSyConf(key, "");
        Assert.hasText(gwSealCls, "系统配置值不能为空。" + key);
        return ObjectCreator.create(GwSealProcess.class, gwSealCls);
    }
    
    /**
     * 清除文件已盖章的记录
     * @param zhengwenId 正文ID
     */
//    public static void removeOldSealFileData(String zhengwenId) {
//        SqlBean sql = new SqlBean();
//        sql.and("BEFORE_SEAL_ID", zhengwenId);
//
//        List<Bean> list = ServDao.finds(OA_GW_SEAL_FILE, sql);
//        for (Bean bean : list) {
//            removeSealFileData(bean.getId());
//        }
//    }

    /**
     * 删除印章文件关联数据。
     * @param zwBean 黑头黑张文件ID
     */
    public static void removeSealFileData(Bean zwBean) {
        Bean fileBean = getSealFileBean(zwBean);
        try {
            removeSealSystemFileData(fileBean.getStr("SEAL_FILE_ID"));
        } catch (Exception e) {
            LOG.error(" Failed to delete seal system file ", e);
        }

        ParamBean bean = new ParamBean();
        bean.setId(zwBean.getId());

        ServMgr.act(OA_GW_SEAL_FILE, ServMgr.ACT_DELETE, bean);
    }
    
    /**
     * 
     * @param fileNum 机构编号
     * @exception Exception Webservice错误
     * @return 指定结构对应的印章列表
     */
    private static String removeSealSystemFileData(String fileNum) throws Exception {
        String wsdlUrl = getWsdlUrl();
        try {
            QName qname = new QName("http://www.jd.com", "WSServerService");

            WSServerServiceLocator ws = new WSServerServiceLocator(wsdlUrl
                    + "?wsdl", qname);
            ws.setWSServerEndpointAddress(wsdlUrl);
            return ws.getWSServer().delSealedFile(fileNum);

        } catch (Exception e) {
            LOG.error("Access web service error:" + wsdlUrl, e);
        }

        return "";
    }    

    /**
     * 
     * @param fileID 原始文件ID
     * @return 盖章文件信息Bean
     */
    public static Bean findSealFile(String fileID) {
        Bean bean = ServDao.find(OA_GW_SEAL_FILE, fileID);

        return bean;
    }

    /**
     * 
     * @param deptNum 机构编号
     * @return 指定机构的所有印章列表
     */
    public static List<Bean> findSealList(String deptNum) {
        List<Bean> list = new ArrayList<Bean>();

        try {
            String result = getSealsByDeptId(deptNum);
            if (result.length() == 0) {
                return list;
            }
            String[] seals = result.split(";");

            for (String strSeal : seals) {
                String[] arr = strSeal.split(",");
                Bean bean = new Bean();
                bean.set("SEAL_CODE", StringUtils.trim(arr[0]));
                bean.set("SEAL_NAME", StringUtils.trim(arr[1]));
                list.add(bean);
            }
        } catch (Exception e) {
            LOG.error("获取印章信息失败", e);
        }

        return list;
    }
    
    /**
     * 
     * @param deptNum 原始的部门编号
     * @return 去掉结尾的0
     */
    public static String formatDeptNum(String deptNum) {
        if (StringUtils.isEmpty(deptNum)) {
            return "";
        }
        char[] chars = deptNum.toCharArray();
        int len = chars.length;
        int i = len - 1;
        for (; i >= 0; i--) {
            char ch = chars[i];
            if (ch == '0') {
                continue;
            } else {
                i++;
                break;
            }
        }
        
        if (i <= 0) {
            return "";
        }
        
        return deptNum.substring(0, i);
    }

    /**
     * 
     * @param odeptCode 机构编号
     * @exception Exception Webservice错误
     * @return 指定结构对应的印章列表
     */
    private static String getSealsByDeptId(String odeptCode) throws Exception {
        String wsdlUrl = getWsdlUrl();
        try {
            // WSServerService serv = WSServerService.createWSServerService(wsdlUrl);
            // return serv.getWSServer().getSealsByDeptId(odeptCode);
            QName qname = new QName("http://www.jd.com", "WSServerService");

            WSServerServiceLocator ws = new WSServerServiceLocator(wsdlUrl
                    + "?wsdl", qname);
            ws.setWSServerEndpointAddress(wsdlUrl);
            return ws.getWSServer().getSealsByDeptId(odeptCode);

        } catch (Exception e) {
            LOG.error("Access web service error:" + wsdlUrl, e);
        }

        return "";
    }

    /**
     * @param sealFileId 印章系统的已盖章文件ID
     * @param odeptNum 机构编码
     * @return 追加打印分数
     */
    public static String lssuedDocPrint(String sealFileId, String odeptNum) {
        String result = null;
        try {
            //WSServerServiceLocator serv = WSServerServiceLocator.createWSServerService(getWsdlUrl());
            QName qname = new QName("http://www.jd.com", "WSServerService");
            String wsdlUrl = getWsdlUrl();
            WSServerServiceLocator ws = new WSServerServiceLocator(wsdlUrl
                    + "?wsdl", qname);
            ws.setWSServerEndpointAddress(wsdlUrl);
            int printNum = getDefaultPrintNum();
            StringBuffer sb = new StringBuffer();
            sb.append(odeptNum);
            sb.append(",").append(sealFileId);
            sb.append(",").append(printNum);
            //sb.append(";");
            result = ws.getWSServer().lssuedDocPrint(sb.toString());
        } catch (Exception e) {
            LOG.error("追加打印份数失败！", e);
        }

        if (!"3".equals(result)) {
            throw new RuntimeException("追加打印份数失败，返回值为：" + result);
        }

        return result;
    }

    /**
     * 
     * @return 取得印章系统Webservice地址
     */
    private static String getWsdlUrl() {
        StringBuffer sealHost = new StringBuffer(Context.getSyConf(CONF_SEAL_SYS_HOST, ""));
        sealHost.append("/services/WSServer.jws");

        return sealHost.toString();
    }

    /**
     * 
     * @return 系统配置的默认打印份数
     */
    public static int getDefaultPrintNum() {
        int result = Context.getSyConf(CONF_GW_DEFAULT_PRINT_NUM, 6);

        return result;
    }
    
    /**
     * 
     * @param gwBean 公文Bean
     * @return 取得指定公文所属机构编码
     */
    public static String getODeptNum(Bean gwBean) {
        String sodeptCode = gwBean.getStr("S_ODEPT");
        return getODeptNum(sodeptCode);
    }
    
    /**
     * 
     * @param sodeptCode OA系统机构编码
     * @return 人力资源系统的机构编码
     */
    public static String getODeptNum(String sodeptCode) {
        DeptBean odeptBean = OrgMgr.getDept(sodeptCode);
        if (odeptBean != null) {
            if (odeptBean.isNotEmpty("DEPT_SRC_TYPE4")) {
                return odeptBean.getStr("DEPT_SRC_TYPE4");
            } else {
                String rtn = odeptBean.getStr("DEPT_SRC_TYPE3");
                rtn = formatDeptNum(rtn);
                if (rtn.length() == 0) {
                    throw new TipException("机构编号不能为空。");
                }
                return rtn;
            }
        }

        return "";
    }
       
    /**
     * 
     * @param fileBean OA系统文件Bean
     * @return 取得签章文件对象
     */
    public static Bean getSealFileBean(Bean fileBean) {
        String fileId = fileBean.getId();
        if (fileBean.isNotEmpty("ORIG_FILE_ID")) {
            fileId = fileBean.getStr("ORIG_FILE_ID");
        }

        Bean sealFile = SealMgr.findSealFile(fileId);
        if (null == sealFile || sealFile.isEmpty("SEAL_FILE_ID")) {
            return null;
        }
        return sealFile;
    }

}
