package com.rh.core.util.msg;

import com.rh.core.base.Bean;

/**
 * 服务操作消息
 * @author wanghg
 */
public class ServActMsg implements Msg {
    private String act;
    private String serv;
    private Bean param;
    private Object result;
    /**
     * 服务操作消息
     * @param serv 服务
     * @param act 操作
     * @param param 参数bean
     * @param result 结果
     */
    public ServActMsg(String serv, String act, Bean param, Object result) {
        this.serv = serv;
        this.act = act;
        this.param = param;
        this.result = result;
    }
    /**
     * 获取服务
     * @return 服务
     */
    public String getServ() {
        return serv;
    }
    /**
     * 获取操作
     * @return 操作
     */
    public String getAct() {
        return act;
    }
    /**
     * 获取参数
     * @return 参数
     */
    public Bean getParam() {
        return param;
    }
    /**
     * 获取结果
     * @return 结果
     */
    public Object getResult() {
        return result;
    }
    @Override
    public String getName() {
        return this.serv + "." + this.act;
    }
    @Override
    public String getType() {
        return this.serv;
    }
    @Override
    public Bean getBody() {
        Bean bean = new Bean();
        bean.put("param", this.param);
        bean.put("result", this.result);
        return bean;
    }
}
