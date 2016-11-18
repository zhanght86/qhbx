package com.rh.core.plug.search.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.math.NumberUtils;

/**
 * 为批量创建索引的类BatchIndexTask中管理的索引设置，提供状态记录支持。
 * @author yangjy
 * 
 */
public class IndexTaskStatus {
    /** 已完成数量 **/
    private static final String COUNT_VAL = "COUNT_VAL";
    /** 强制结束时间 **/
    private static final String END_TIME = "END_TIME";
    /** 任务是否在运行 **/
    private static final String IS_RUNNING = "IS_RUNNING";
    /** 实际结束时间 **/
    private static final String FINISH_TIME = "FINISH_TIME";

    private static Map<String, Map<String, String>> tasks = new ConcurrentHashMap<String, Map<String, String>>();

    /**
     * 
     * @return 任务名称
     */
    public static Object[] getTaskNames() {
        Object[] values = tasks.keySet().toArray();

        return values;
    }

    /**
     * 增加任务
     * @param taskName 任务名
     * @return 任务状态
     */
    public static Map<String, String> addStatusMap(String taskName) {
        Map<String, String> map = new ConcurrentHashMap<String, String>();

        tasks.put(taskName, map);

        return map;
    }

    /**
     * 
     * @param taskName 任务名称
     * @return 保存任务状态的Map
     */
    public static Map<String, String> getStatusMap(String taskName) {
        if (tasks.containsKey(taskName)) {
            return tasks.get(taskName);
        }

        return addStatusMap(taskName);
    }

    /**
     * @param taskName SearchId名称
     * @return 已创建索引数量
     */
    public static int getCountVal(String taskName) {
        Map<String, String> map = getStatusMap(taskName);
        String count = map.get(COUNT_VAL);
        if (count != null && NumberUtils.isNumber(count)) {
            return Integer.parseInt(count);
        }
        return 0;
    }

    /**
     * @param taskName SearchId名称
     * @param count 新增数量
     */
    public static void addCount(String taskName, int count) {
        Map<String, String> map = getStatusMap(taskName);
        int old = getCountVal(taskName);
        old += count;

        map.put(COUNT_VAL, String.valueOf(old));
    }

    /**
     * @param taskName SearchId名称
     * @return 是否正在运行
     */
    public static boolean isRunning(String taskName) {
        Map<String, String> map = getStatusMap(taskName);
        if (map.get(IS_RUNNING) != null) {
            return true;
        }

        return false;
    }

    /**
     * @param taskName SearchId名称
     * @param ifRunning 是否正在运行
     */
    public static void setRunning(String taskName, boolean ifRunning) {
        Map<String, String> map = getStatusMap(taskName);
        if (ifRunning) {
            map.put(IS_RUNNING, "true");
        } else {
            map.remove(IS_RUNNING);
        }
    }

    /**
     * @param taskName SearchId名称
     * @return 取得指定的结束时间
     */
    public static String getEndTime(String taskName) {
        Map<String, String> map = getStatusMap(taskName);
        return map.get(END_TIME);
    }

    /**
     * 设置结束时间
     * @param taskName SearchId名称
     * @param dateTime 结束时间
     */
    public static void setEndTime(String taskName, String dateTime) {
        Map<String, String> map = getStatusMap(taskName);
        map.put(END_TIME, dateTime);
    }

    /**
     * 
     * @param taskName 任务名称
     * @return 实际结束时间
     */
    public static String getFinishTime(String taskName) {
        Map<String, String> map = getStatusMap(taskName);
        return map.get(FINISH_TIME);
    }

    /**
     * 
     * @param taskName 任务名称
     * @param finishTime 完成时间
     */
    public static void setFinishTime(String taskName, String finishTime) {
        Map<String, String> map = getStatusMap(taskName);
        map.put(FINISH_TIME, finishTime);
    }
}
