package com.rh.core.util.msg;

/**
 * 指定类别的消息监听器
 * @author wanghg
 */
public abstract class TypeMsgListener implements MsgListener {
    private String[] types;
    @Override
    public void init(String conf) {
        this.types = conf.split(",");
    }
    @Override
    public void onMsg(Msg msg) {
        if (types != null) {
            for (String type : this.types) {
                if (msg.getType().equals(type)) {
                    onTypeMsg(msg);
                    break;
                }
            }
        }
    }
    /**
     * 处理预定类别的消息
     * @param msg 消息
     */
    protected abstract void onTypeMsg(Msg msg);
}
