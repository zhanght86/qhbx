package com.rh.core.util.msg;


/**
 * 类别定时定量消息监听
 */
public abstract class TypeTimeCountMsgListener extends TimeCountMsgListener {
    private String[] types;
    /**
     * 初始化
     * @param type 类型
     * @param time 时间（单位：秒）
     * @param count 数量
     */
    public void init(String type, int time, int count) {
        this.init(new String[] {type}, time, count);
    }
    /**
     * 初始化
     * @param types 类型
     * @param time 时间（单位：秒）
     * @param count 数量
     */
    public void init(String[] types, int time, int count) {
        this.types = types;
        super.init(time, count);
    }
    /**
     * 初始化
     * @param conf 配置
     *      格式：type\n[时间[,数量]]
     *          如:type1
     *             30,100
     */
    @Override
    public void init(String conf) {
        String[] strs = conf.split("\n");
        if (strs.length > 0) {
            this.types = strs[0].split(",");
        }
        if (strs.length > 1) {
            super.init(strs[1]);
        }
    }
    @Override
    public void onMsg(Msg msg) {
        if (types != null) {
            for (String type : this.types) {
                if (msg.getType().equals(type)) {
                    super.onMsg(msg);
                    break;
                }
            }
        }
    }
}
