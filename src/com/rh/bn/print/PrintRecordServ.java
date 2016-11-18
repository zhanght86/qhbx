package com.rh.bn.print;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;

/**
 * 文件打印记录服务类
 * @author tanyh 20150709
 *
 */
public class PrintRecordServ extends CommonServ{
    private static final String PRINT_SERV_ID = "BN_FILE_PRINT_RECORD";
    /**
     * 根据前段传递的文件ID、数据ID获取该文件打印次数
     * @param paramBean
     * @return OutBean
     */
    public OutBean queryPrintCopies(ParamBean paramBean) {
        OutBean outBean = new OutBean();
        List<Bean> resultBean = ServDao.finds(PRINT_SERV_ID, (new ParamBean()).
                setSelect(" SUM(PRINT_NUM) AS COPIES ").
                setWhere(" and FILE_ID='" + paramBean.getStr("FILE_ID") + "'" + 
                " and DATA_ID='" + paramBean.getStr("DATA_ID") + "'"));
        int printCopies = 0;
        if (resultBean != null && resultBean.size() > 0) {
            printCopies = (resultBean.get(0)).getInt("COPIES");
        }
        outBean.set("PRINT_COPIES", printCopies);
        return outBean;
    }
    /**
     * 保存打印记录
     * @param paramBean
     * @return OutBean
     */
    public OutBean savePrintRecord(ParamBean paramBean) {
        OutBean outBean = new OutBean();
        Bean dataBean = ServDao.create(PRINT_SERV_ID, paramBean);
        if (dataBean != null && dataBean.getId().length() > 0) {
            outBean.set("SUCCESS", "true");
        } else {
            outBean.set("SUCCESS", "false");
        }
        return outBean;
    }
}
