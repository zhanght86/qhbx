package com.rh.core.util.msg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Lang;

/**
 * 消息中心
 * @author wanghg
 */
public class MsgCenter {
    private Log log = LogFactory.getLog(MsgCenter.class);
    /**
     * 实例
     */
    private static MsgCenter inst = new MsgCenter();
    /**
     * 获取实例
     * @return 实例
     */
    public static MsgCenter getInstance() {
        return inst;
    }
    /**
     * 初始化，加载配置的监听类
     */
    public void init() {
        SqlBean sql = new SqlBean().selects("LIS_CLASS, LIS_CONF").and("S_FLAG", "1").asc("LIS_SORT");
        List<Bean> list = ServDao.finds(ServMgr.SY_COMM_MSG_LISTENER, sql);
        synchronized (this.initListeners) {
            this.initListeners.clear();
            for (int i = 0; i < list.size(); i++) {
                Object obj;
                try {
                    obj = Class.forName(list.get(i).getStr("LIS_CLASS")).newInstance();
                    if (obj instanceof MsgListener) {
                        ((MsgListener) obj).init(list.get(i).getStr("LIS_CONF"));
                        this.initListeners.add((MsgListener) obj);
                    }
                } catch (Exception e) {
                    log.error("初始化消息监听错误", e);
                }
            }
        }
    }
    private Map<String, MsgListener> listeners = new HashMap<String, MsgListener>();
    private List<MsgListener> initListeners = new ArrayList<MsgListener>();
    /**
     * 添加监听
     * @param listener 监听
     * @return 监听ID
     */
    public String addListener(MsgListener listener) {
        synchronized (this.listeners) {
            String id = Lang.getUUID();
            this.listeners.put(id, listener);
            return id;
        }
    }
    /**
     * 移除监听
     * @param id 监听id
     */
    public void removeListener(String id) {
        synchronized (this.listeners) {
            this.listeners.remove(id);
        }
    }
    /**
     * 线程池
     */
    private ExecutorService pool = new ThreadPoolExecutor(0, 10, 60, 
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    /**
     * 添加消息
     * @param msg 消息
     */
    public void addMsg(Msg msg) {
        pool.execute(new MsgProcessor(msg));
    }
    /**
     * 获取消息监听
     * @return 消息监听
     */
    public Map<String, MsgListener> getListeners() {
        return this.listeners;
    }
    /**
     * 获取初始化消息监听
     * @return 消息监听
     */
    public List<MsgListener> getInitListeners() {
        return this.initListeners;
    }
}
/**
 * 消息处理器
 */
class MsgProcessor implements Runnable {
    private Msg msg;
    /**
     * 消息处理器
     * @param msg 消息
     */
    public MsgProcessor(Msg msg) {
        this.msg = msg;
    }
    @Override
    public void run() {
        Map<String, MsgListener> listeners = MsgCenter.getInstance().getListeners();
        synchronized (listeners) {
            Iterator<String> it = listeners.keySet().iterator();
            while (it.hasNext()) {
                listeners.get(it.next()).onMsg(this.msg);
            }
        }
        List<MsgListener> initListeners = MsgCenter.getInstance().getInitListeners();
        synchronized (initListeners) {
            for (MsgListener listener : initListeners) {
                listener.onMsg(this.msg);
            }
        }
    }
}

