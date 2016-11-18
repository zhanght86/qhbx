package com.rh.core.base.start;

import java.util.Timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 触发更新移动系统字典的Timer
 * @author wanghg
 */
public class TimerLoader {
    
    /** log. */
    private static Log log = LogFactory.getLog(TimerLoader.class);
    
    /**
     * 加载
     */
    public void start() {
        log.debug("TimerLoader start ... ...");
        //启动自定义Timer
        String jvmTimer = System.getProperty("com.rh.timerClearflag", "");
        if (jvmTimer.equalsIgnoreCase("true")) {
            Timer timer = new Timer();
            timer.schedule(new TimerClearDict(), 180*1000, 60*60*1000);
            log.debug("Timer started");
        }
    }
    /**
     * 销毁
     */
    public void stop() {
    }
}
