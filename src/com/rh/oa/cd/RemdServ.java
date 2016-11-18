package com.rh.oa.cd;

import com.rh.core.base.Bean;
import com.rh.core.comm.todo.TodoBean;
import com.rh.core.comm.todo.TodoUtils;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.oa.cd.util.CdUtils;

/**
 * 处理催督办服务的监听类
 * 
 * @author cuihf
 * 
 */
public class RemdServ extends CommonServ {

    /**
     * 服务ID
     */
    public static final String SERV_ID = "OA_CD_REMIND";

    /**
     * 状态：未送出
     */
    private static final int STATE_NOSEND = 0;

    /**
     * 状态：未反馈
     */
    private static final int STATE_SENT = 1;

    /**
     * 状态：已反馈
     */
    public static final int STATE_FEEDBACK = 2;

    /**
     * 状态：已办结
     */
    private static final int STATE_FINISH = 3;

    /**
     * 字段：催办标题
     */
    protected static final String COLUMN_REMD_TITLE = "REMD_TITLE";
    /**
     * 字段：被催办用户
     */
    protected static final String COLUMN_ACPT_USER = "ACPT_USER";
    /**
     * 字段：催办状态
     */
    protected static final String COLUMN_REMD_STATUS = "REMD_STATUS";
    /**
     * 字段：办理结果
     */
    protected static final String COLUMN_DO_MIND = "DO_MIND";
    /**
     * 字段：催办ID
     */
    protected static final String COLUMN_REMD_ID = "REMD_ID";

    /**
     * 字段：催办人
     */
    protected static final String COLUMN_S_USER = "S_USER";

    /**
     * 字段：流水号
     */
    private static final String REMD_NUM = "REMD_NUM";

    /**
     * 字段：年度
     */
    private static final String REMD_YEAR = "REMD_YEAR";

    /**
     * 字段：催办代字
     */
    private static final String REMD_CODE = "REMD_CODE";

    /**
     * 根据代字、年度取得最大流水号
     * @param paramBean 参数信息
     * @return 流水号
     */
    public OutBean getMaxCode(ParamBean paramBean) {
        String servId = paramBean.getServId();
        Bean cdBean = new Bean();
        cdBean.set(REMD_CODE, paramBean.getStr(REMD_CODE));
        cdBean.set(REMD_YEAR, paramBean.getStr(REMD_YEAR));
        return new OutBean(CdUtils.getMaxCode(servId, cdBean, REMD_NUM));
    }

    /**
     * 发送催办单
     * @param paramBean 参数信息
     * @return 送交信息
     */
    public OutBean sendTodo(ParamBean paramBean) {
        String servId = paramBean.getServId();
        OutBean cdBean = this.byid(paramBean);
        TodoBean todoBean = new TodoBean();
        todoBean.setSender(cdBean.getStr(COLUMN_S_USER));
        todoBean.setTitle(cdBean.getStr(COLUMN_REMD_TITLE));
        todoBean.setOwner(cdBean.getStr(COLUMN_ACPT_USER));
        todoBean.setCode(servId);
        todoBean.setObjectId1(paramBean.getStr(COLUMN_REMD_ID));
        todoBean.setUrl(servId + ".byid.do?data={_PK_:" + paramBean.getStr(COLUMN_REMD_ID) + "}");
        TodoUtils.insert(todoBean);
        cdBean.set(COLUMN_REMD_STATUS, new Integer(STATE_SENT));
        ServDao.update(servId, cdBean);
        // this.modify(cdBean);
        cdBean.setOk();
        return cdBean;
    }

    /**
     * 办结催办单
     * @param paramBean 参数信息
     * @return 办结信息
     */
    public OutBean finish(ParamBean paramBean) {
        String servId = paramBean.getServId();
        OutBean cdBean = this.byid(paramBean);
        cdBean.set(COLUMN_REMD_STATUS, new Integer(STATE_FINISH));
        ServDao.update(servId, cdBean);
        Bean todoBean = new Bean();
        todoBean.set("TODO_OBJECT_ID1", paramBean.getStr(COLUMN_REMD_ID));
        todoBean.set("TODO_CODE", servId);
        TodoUtils.ends(todoBean);
        cdBean.setOk();
        return cdBean;
    }

    /**
     * 取消办结催办单
     * @param paramBean 参数信息
     * @return 取消办结信息
     */
    public OutBean unfinish(ParamBean paramBean) {
        OutBean cdBean = this.byid(paramBean);
        cdBean.set(COLUMN_REMD_STATUS, new Integer(STATE_NOSEND));
        ServDao.update(paramBean.getServId(), cdBean);
        cdBean.setOk();
        return cdBean;
    }
}
