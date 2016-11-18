package com.rh.core.util.threadpool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 线程任务对象。先做简单封装，避免破坏线程池中的线程，以后可以做更多的扩展，例如执行日志，执行时间。
 * @author yangjy
 * 
 */
public abstract class ThreadTask implements Runnable {
    /** 日志处理类 **/
    protected Log log = LogFactory.getLog(this.getClass());

    @Override
    public final void run() {
        try {
            execute();
        } catch (Throwable e) {
            //避免异常终止了线程池中的线程，因此catch所有的错误。
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 
     */
    public abstract void execute();

}
