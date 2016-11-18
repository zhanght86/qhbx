package com.rh.core.base.start;

import com.rh.core.base.Context;
import com.rh.core.comm.wenku.msg.DocumentSnapshotListener;
import com.rh.core.util.msg.MsgCenter;
import com.rh.core.util.msg.actlog.LogBatchSavelistener;
import com.rh.core.util.msg.index.IndexListener;

/**
 * 消息监听加载器
 * @author wanghg
 */
public class MsgLisLoader {
    /** 索引消息 */
    public static final String INDEX_MSG_TYPE = "INDEX_MSG";
    /** 操作日志消息 */
    public static final String ACTLOG_MSG_TYPE = "ACTLOG_MSG";
    /** 关注消息 */
    public static final String ATTENTION_MSG_TYPE = "ATTENTION_MSG";
    
    /**TODO:文库消息代码应属于comm包 文库快照消息 */
    public static final String DOCSNAPSHOT_MSG_TYPE = "DOCSNAPSHOT_MSG_TYPE";
    
    //默认批量提交 5分钟
    private static final int DEFAULT_BATCH_SAVE_INTERVAL = 60 * 5; 
    //默认批量提交最大数据量 500
    private static final int DEFAULT_BATCH_SAVE_MAX_SIZE = 500;
    
    /**
     * 加载消息监听
     */
    public void start() {
        MsgCenter.getInstance().init();
        
        int interval =  Context.getSyConf("SY_COMM_BATCH_SAVE_LOG_INTERVAL", DEFAULT_BATCH_SAVE_INTERVAL);
        int maxSize =  Context.getSyConf("SY_COMM_BATCH_SAVE_LOG_MAX_SIZE", DEFAULT_BATCH_SAVE_MAX_SIZE);
        
        //操作日志监听
        LogBatchSavelistener logListener = new LogBatchSavelistener();
        logListener.init(ACTLOG_MSG_TYPE, interval, maxSize);
        MsgCenter.getInstance().addListener(logListener);
        
        //索引监听
        IndexListener indexListener = new IndexListener();
        indexListener.init(INDEX_MSG_TYPE);
        MsgCenter.getInstance().addListener(indexListener);
        
        //文库快照
        //TODO:文档消息 代码重构
        DocumentSnapshotListener docListener = new DocumentSnapshotListener();
        docListener.init(DOCSNAPSHOT_MSG_TYPE);
        MsgCenter.getInstance().addListener(docListener);
    }
    /**
     * 销毁
     */
    public void stop() {
    }
}
