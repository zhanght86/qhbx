package com.rh.lw.ct.mgr;

import java.rmi.RemoteException;
import java.text.ParseException;

import javax.xml.rpc.ServiceException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;
import com.rh.core.util.XmlUtils;
import com.rh.lw.ct.webservice.payment.IBizInsertLocator;
import com.rh.oa.zh.seal.SealMgr;

/**
 * 付款计划
 * @author chensheng
 *
 */
public class PaymentMgr {

    private static Log log = LogFactory.getLog(PaymentMgr.class);
    
    /**
     * 支付计划服务
     */
    public static final String LW_CT_PAYMENT = "LW_CT_PAYMENT";
    /**
     * SESSIONID key
     */
    private static final String SID = "SID";
    /**
     * 支付成功
     */
    private static final int PAY_SUCCESS = 3;
    /**
     * 支付失败
     */
    private static final int PAY_FAILURE = 4;
    
    /**
     * 开始支付
     * @param pmId 支付计划ID
     * @param address 接口地址 
     * @param msgBean 接口调用所需参数
     * @return 处理接口返回结果Bean
     */
    public static OutBean doPay(String pmId, String address, Bean msgBean) {
        IBizInsertLocator locator = new IBizInsertLocator();
        locator.setIBizInsertPortEndpointAddress(address);
        String xmlData = XmlUtils.toFullXml("paymentinfo", msgBean).toString();
        String responseStr = "";
        try {
            responseStr = locator.getIBizInsertPort().insertBiz(xmlData.toString());
            saveErrorInfo(responseStr, pmId);
        } catch (RemoteException e) {
            saveErrorInfo(e.getMessage(), pmId);
            log.error(e.getMessage(), e);
        } catch (ServiceException e) {
            saveErrorInfo(e.getMessage(), pmId);
            log.error(e.getMessage(), e);
        }
        
        return handlerResponse(responseStr);
    }
    
    /**
     * 保存接口调用的异常信息
     * @param msg 需要保存的信息
     * @param pmId 支付计划ID
     */
    private static void saveErrorInfo(String msg, String pmId) {
        ServDao.update(LW_CT_PAYMENT, new Bean(pmId).set("ERROR_INFO", msg));
    }
    
    /**
     * 处理接口返回结果
     * @param responseStr 返回结果
     * @return 处理之后的返回Bean
     */
    private static OutBean handlerResponse(String responseStr) {
        OutBean outBean = new OutBean();
        if (StringUtils.isBlank(responseStr)) {
            outBean.setError("资金系统接口没有响应！");
            log.error("资金系统接口没有响应！");
        }
        Bean responseBean = XmlUtils.toBean(responseStr);
        Bean recordBeans = responseBean.getBean("recordinfos");
        if (recordBeans.contains("recordinfo")) {
            Bean recordBean = recordBeans.getBean("recordinfo");
            int dataStatus = recordBean.getInt("DATASTATUS");
            String cId = recordBean.getStr("C_ID");
            // 更新支付计划状态，调用接口时把支付计划的主键传递给了C_ID
            ServDao.update(LW_CT_PAYMENT, new Bean(cId).set("PM_STATE", dataStatus));
            if (dataStatus == 1) {
                outBean.setOk("成功上传支付信息！");
            } else {
                outBean.setError(recordBean.getStr("ERRDESCRIPTION"));
                log.error("调用资金系统接口失败：" + recordBean.getStr("ERRDESCRIPTION"));
            }
        } 
        return outBean;
    }

    /**
     * 构造完成的报文Bean
     * @param payBean 支付要素
     * @return 返回报文Bean
     */
    public static Bean createMsgBean(Bean payBean) {
        Bean msgHeadBean = new Bean(); // 报文头
        msgHeadBean.set("TOTALNUM", 1); // 总笔数
        msgHeadBean.set("TOTALAMOUNT", payBean.getDouble("AMOUNT")); // 总金额
        msgHeadBean.set("BATCHNO", payBean.getStr("C_ID")); // 流水号和C_ID一致
        msgHeadBean.set("INPUTDATE", DateUtils.getDatetime()); // 日期
        
        Bean msgBean = new Bean();
        msgBean.set("baseinfo", msgHeadBean);
        msgBean.set("recordinfos", new Bean().set("recordinfo", payBean));
        return msgBean;
    }
    
    /**
     * 构造支付要素
     * 支付要素如下：
     * C_ID                 主键ID    付款计划主键
     * BILLCODE             业务单据编号  与C_ID一致
     * DATASOURCE           数据来源    固定值：3合同系统
     * TRANSFERDATE         业务单据日期  YYYY-MM-DD 用户点击付款按钮的日期
     * ORGCODE              业务申请单位编号 (机构代码) 占用预算的单位编号，NC系统中的单位编号 需要做对应
     * PAYWAY               资金交割方式   1：直联 2：非直联 确定为1
     * TRANSFERCODE         业务类型编号  固定值：9801：费用报销 资金系统要去确认下
     * PAYTYPE              付款用途编号  暂定和业务类型保持一致
     * BUDGETPROJECTCODE    预算项目编号 预算项目不能为空
     * RECACCOUNTNO         收款账户编号  收款人对应账号
     * RECACCOUNTNAME       收款账户名称    收款人名称
     * RECBANKNAME          收款开户行名称  收款人开户行地址
     * RECPROVINCE          收款行地址（省）
     * RECAREANAMEOFCITY    收款行地址（市）
     * BANKPAYTYPE          付款对象类型（银行指令类型）   1：公对公 2：公对私 确定为公对公
     * CURRENCYCODE         结算币种    固定值：CNY 人民币
     * AMOUNT               付款金额  必须大于零
     * TRANSFERTYPE         业务类别    1：业务结算 2：员工结算 确定为业务结算
     * DATASTATUS           数据状态  传递0，接口会回写该字段 0：待处理 1：引入成功 2：引入失败 3：业务撤销 4：业务成功 5：退票
     * @param paramBean 参数
     * @return 返回结果
     */
    public static Bean createPayBean(ParamBean paramBean) {
        
        /**
         * 查出合同和付款计划
         */
        String ctId = paramBean.getStr("CT_ID");
        Bean ctBean = ServDao.find(ContractMgr.LW_CT_CONTRACT, ctId);
        Bean pmBean = ServDao.find(LW_CT_PAYMENT, paramBean.getId());
        
        /**
         * 构造支付要素
         */
        Bean payBean = new Bean();
        payBean.set("C_ID", pmBean.getId());    // 以支付计划的ID作为支付的唯一标识码
        payBean.set("BILLCODE", pmBean.getId());  // 和C_ID一致
        payBean.set("DATASOURCE", 3); // 3为合同系统在资金系统里的代码
        String aTime = pmBean.getStr("S_ATIME");
        try {
            aTime = DateUtils.formatDate(DateUtils.parseDate(aTime));
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
            // 不做任何处理，S_ATIME为系统生成
        }
        payBean.set("TRANSFERDATE", aTime); // 付款计划创建日期作为业务单据日期
        payBean.set("ORGCODE", SealMgr.getODeptNum(ctBean.getStr("S_ODEPT"))); // 起草机构作为业务申请单位编号
        payBean.set("PAYTWAY", 1); // 资金交割方式，需要确认，1：直联 2：非直联
        payBean.set("TRANSFERCODE", "HT01"); // 业务类型编号
        payBean.set("PAYTYPE", "HT01"); // 付款用途编号
        payBean.set("BUDGETPROJECTCODE", 1); // 预算项目编号，需要确认
        payBean.set("RECACCOUNTNO", ctBean.getStr("CT_ST_ACCOUNT")); // 签约对方开户行账号作为收款账户编号
        payBean.set("RECACCOUNTNAME", ctBean.getStr("CT_ST_NAME")); // 签约对方姓名作为收款账户名称，签约方可能有多个账户
        payBean.set("RECBANKNAME", ctBean.getStr("CT_ST_BLANK")); // 收款人开户行地址不能提供
        payBean.set("RECPROVINCE", "北京"); // 收款行地址（省）不能提供
        payBean.set("RECAREANAMEOFCITY", "北京"); // 收款行地址（市）不能提供
        payBean.set("BANKPAYTYPE", 1); // 付款对象类型（银行指令类型）   1：公对公 2：公对私
        payBean.set("CURRENCYCODE", getCurrency(ctBean.getInt("CT_CTYPE"))); // 结算币种，国际标准代码
        payBean.set("AMOUNT", pmBean.getDouble("PM_AMONEY")); // 付款计划的预付款金额作为付款金额
        payBean.set("TRANSFERTYPE", 1); // 业务类别    1：业务结算 2：员工结算
        payBean.set("DATASTATUS", 0); // 数据状态，开始传递0，资金系统会回写该状态
        
        return payBean;
        
    }
    
    /**
     * 把合同系统中的币种字典转换成实际的币种代码
     * 1：CNY人民币
     * 2：USD美元
     * 3：EUR欧元
     * 4：HKD港币
     * 5：TWD新台币
     * 6：PHP菲律宾
     * 7：GBP英镑
     * 8：JPY日元
     * 9：KRW韩元
     * 10：CAD加元
     * 11：AUD澳元
     * 12：CHF瑞郎
     * 13：SGD新加坡元
     * 14：MYR马来西亚币
     * 15：IDR印尼
     * 16：NZD新西兰
     * 17：VND越南
     * 18：THB泰铢
     * @param ctype 币种字典值
     * @return 返回币种
     */
    public static String getCurrency(int ctype) {
        String currency = "CNY";
        switch(ctype) {
            case 1 : currency = "CNY"; break;
            case 2 : currency = "USD"; break;
            case 3 : currency = "EUR"; break;
            case 4 : currency = "HKD"; break;
            case 5 : currency = "TWD"; break;
            case 6 : currency = "PHP"; break;
            case 7 : currency = "GBP"; break;
            case 8 : currency = "JPY"; break;
            case 9 : currency = "KRW"; break;
            case 10 : currency = "CAD"; break;
            case 11 : currency = "AUD"; break;
            case 12 : currency = "CHF"; break;
            case 13 : currency = "SGD"; break;
            case 14 : currency = "MYR"; break;
            case 15 : currency = "IDR"; break;
            case 16 : currency = "NZD"; break;
            case 17 : currency = "VND"; break;
            case 18 : currency = "THB"; break;
            default : currency = "CNY";
        }
        return currency;
    }
    
    /**
     * 开始处理回传支付状态
     * @param paramBean 参数
     * @return 返回
     */
    public static OutBean doCheckPay(ParamBean paramBean) {
        OutBean outBean = null;
        String sId = paramBean.getStr("SID");
        String xmlData = paramBean.getStr("xmlData");
        Bean checkPayBean = null;
        if (isLogin(sId)) {
            checkPayBean = XmlUtils.toBean(xmlData);
            Bean baseBean = checkPayBean.getBean("baseinfo");
            // 通过流水号取出支付计划，流水号就是支付计划的主键
            Bean payBean = ServDao.find(LW_CT_PAYMENT, new Bean(baseBean.getStr("BATCHNO")));
            if (payBean == null) {
                throw new RuntimeException("支付计划不存在");
            }
            Bean recordBeans = checkPayBean.getBean("recordinfos");
            Bean recordBean = recordBeans.getBean("recordinfo");
            int status = recordBean.getInt("DATASTATUS");
            if (status == 1) { // 支付成功
                ServDao.update(LW_CT_PAYMENT, 
                        new Bean(payBean.getId()).set("PM_STATE", PAY_SUCCESS));
                outBean = createOutBean(baseBean, recordBean);
            } else { // 支付失败
                ServDao.update(LW_CT_PAYMENT, 
                        new Bean(payBean.getId()).set("PM_STATE", PAY_FAILURE))    
                            .set("PM_MEMO", recordBeans.getStr("ERRDESCRIPTION"));
                outBean = createOutBean(baseBean, recordBean);
            }
        } else {
            outBean = new OutBean().setError("尚未登录");
        }
        return outBean;
    }
    
    /**
     * 构造返回值
     * @param baseBean 报文头
     * @param recordBean 报文体
     * @return 返回
     */
    private static OutBean createOutBean(Bean baseBean, Bean recordBean) {
        OutBean outBean = new OutBean();
        
        // 返回的报文头
        Bean retBaseBean = new Bean();
        retBaseBean.set("BATCHNO", baseBean.getStr("BATCHNO"));
        retBaseBean.set("CHECKRESULT", "E0");
        retBaseBean.set("RESPONSE", DateUtils.getDatetime());
        
        // 返回的报文体
        Bean retRecordBean = new Bean();
        retRecordBean.set("C_ID", recordBean.getStr("C_ID"));
        retRecordBean.set("DATESTATUS", 1);
        retRecordBean.set("ERRDESCRIPTION", "ERRDESCRIPTION");
        
        // 设置返回格式
        Bean rootBean = new Bean();
        rootBean.set("baseinfo", baseBean);
        rootBean.set("recordinfos", new Bean().set("recordinfo", retRecordBean));
        outBean.set(Constant.XML_ROOT, "platforminfo");
        outBean.set(Constant.XML_ROOT_BEAN, rootBean);
        
        return outBean;
    }
    
    /**
     * 通过SESSIONID来判断当前同步用户是否登录
     * @param sId SESSIONID
     * @return 返回是否登录
     */
    private static boolean isLogin(String sId) {
        ParamBean bean = new ParamBean();
        bean.setServId(ServMgr.SY_ORG_LOGIN);
        bean.setAct("isLogin");
        bean.set(SID, sId);
        OutBean outBean = ServMgr.act(bean);
        if (outBean.getStr(Constant.RTN_MSG).indexOf(Constant.RTN_MSG_OK) >= 0) {
            return true;
        }
        return false;
    }
    
}
