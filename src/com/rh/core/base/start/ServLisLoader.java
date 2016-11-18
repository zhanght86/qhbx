package com.rh.core.base.start;

import com.rh.core.serv.ServLisMgr;

/**
 * 服务监听加载器
 * @author wanghg
 */
public class ServLisLoader {
    /**
     * 加载服务监听
     */
    public void start() {
        ServLisMgr.getInstance().init();
    }
    /**
     * 销毁
     */
    public void stop() {
    }
}
