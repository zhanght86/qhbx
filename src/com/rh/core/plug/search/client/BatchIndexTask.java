package com.rh.core.plug.search.client;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.plug.search.SearchDefUtils;
import com.rh.core.serv.ServUtils;
import com.rh.core.util.DateUtils;
import com.rh.core.util.threadpool.ThreadTask;

/**
 * 批量创建索引的任务，它可以为多个索引设置启动创建索引任务，并监控创建索引任务的状态。 常用于项目初期，需要为大量文件创建索引。
 * @author yangjy
 * 
 */
public class BatchIndexTask extends ThreadTask {

    /** 检索设置ID **/
    private String searchId = "";

    private String[] searchIdArr = null;

    private int idx = 0;

    /** 任务是否在运行 **/
    private static boolean isRunning = false;

    private int maxNum = 1000;

    /**
     * 
     * @return BatchIndexTask类是否正在运行
     */
    public static synchronized boolean isRunning() {
        return isRunning;
    }

    /**
     * @param searchIds 检索设置ID，多个ID之间使用searchId分隔
     * @return 创建 BatchIndexTask 对象
     */
    public static synchronized BatchIndexTask createInstance(String searchIds) {
        if (BatchIndexTask.isRunning()) {
            return null;
        }
        BatchIndexTask task = new BatchIndexTask(searchIds);
        task.maxNum = Context.getSyConf("SY_SEARCH_BATCH_INDEX_MAX_NUM", 1000);
        return task;
    }

    /**
     * 
     * @param searchIds 索引设置ID，支持多个ID，多个ID之间使用英文逗号分隔。
     */
    private BatchIndexTask(String searchIds) {
        this.searchIdArr = searchIds.split(",");
        this.searchId = searchIdArr[idx];
        for (String sId : searchIdArr) {
            IndexTaskStatus.setRunning(sId, true);
        }
        this.idx++;
    }

    @Override
    public void execute() {
        try {
            isRunning = true;
            IndexTaskStatus.setRunning(this.searchId, true);
            int result = doIndex();
            loop(result);
        } finally {
            IndexTaskStatus.setRunning(this.searchId, false);
            isRunning = false;
        }
    }

    /**
     * 递归调用创建索引的方法
     * @param result 完成索引的数量
     */
    private void loop(int result) {
        if (result < maxNum) { // 数据不到一页，则表示已经取完所有数据
            // 结束当前的SERV_ID
            endCurrentTask(result);
            statNextTask();
            return;
        }

        String endTime = IndexTaskStatus.getEndTime(this.searchId);
        if (endTime == null) {
            endCurrentTask(result);
            statNextTask();
            return;
        }

        String currentTime = DateUtils.getDatetime();
        if (endTime.compareTo(currentTime) > 0) {
            IndexTaskStatus.addCount(this.searchId, result);
            result = doIndex();
            loop(result);
        } else {
            endCurrentTask(result);
            statNextTask();
        }
    }

    /**
     * 结束当前任务
     * @param result 索引文件数量
     */
    private void endCurrentTask(int result) {
        // 结束当前的SERV_ID
        IndexTaskStatus.setRunning(this.searchId, false);
        if (result > 0) {
            IndexTaskStatus.addCount(this.searchId, result);
        }
    }

    /**
     * 开始新任务
     */
    private void statNextTask() {
        // 如果还有SERV_ID ,则启动新的SERV_ID
        if (this.idx < this.searchIdArr.length) {
            this.searchId = this.searchIdArr[this.idx];
            this.idx++;
            this.execute();
        }
    }

    /**
     * 创建索引
     * @return 服务ID
     */
    private int doIndex() {
        try {
            Bean search = SearchDefUtils.getSearchDefBean(this.searchId);
            if (search == null) {
                log.error("Search define not found." + this.searchId);
            } else {
                return ServUtils.doIndex(search, maxNum);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return 0;
    }
}
