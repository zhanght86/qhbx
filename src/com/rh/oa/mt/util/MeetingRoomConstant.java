package com.rh.oa.mt.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.rh.core.base.Context;
import com.rh.core.util.DateUtils;

/**
 * 会议室占用常量设置，及方法
 * 
 * @author hdy
 * 
 */
public class MeetingRoomConstant {

    /**
     * 上午时间段
     */
    private static final String OA_MT_MORNING_KEK = "OA_MT_MORNING";
    
    /**
     * 下午时间段
     */
    private static final String OA_MT_AFTERNOON_KEY = "OA_MT_AFTERNOON";
    
    
    /**
     * Map上午数值标示
     */
    private static final String MORNING_DATA = "MORNING";
    
    /**
     * Map下午数值标示
     */
    private static final String AFTERNOON_DATA = "AFTERNOON";
    
    /**
     * Map晚上数值标示
     */
    private static final String EVENING_DATA = "EVENING";
    
    /**
     * Map上午和下午数值标示
     */
    private static final String MORNING_AND_AFTERNOON_DATA = "MORNING_AND_AFTERNOON";
    
    /**
     * Map下午和晚上数值标示
     */
    private static final String AFTERNOON_AND_EVENING_DATA = "AFTERNOON_AND_EVENING";
    
    /**
     * Map全天数值标示
     */
    private static final String ALL_DAY_DATA = "ALL_DAY";
    
    /**
     * 上午时间区域开始时间
     */
    private static final String MORNING_START = "MORNING_START";
    
    /**
     * 下午时间区域开始时间
     */
    private static final String AFTERNOON_START = "AFTERNOON_START";
    
    /**
     * 下午时间区域结束时间
     */
    private static final String AFTERNOON_END = "AFTERNOON_END";
    
    /**
     * 晚上时间区域开始时间
     */
    private static final String EVENING_START = "EVENING_START";
    
    /**
     * 晚上时间区域结束时间
     */
    private static final String EVENING_END = "EVENING_END";

    /**
     * 上午时间区域结束时间
     */
    private static final String MORNING_END = "MORNING_END";
    
    /**
     * 会议室类型数据字典
     */
    public static final String OA_MT_MEETINGROOM_MR_TYPE_DIC_CODE = "OA_MT_MEETINGROOM_MR_TYPE_DIC";

    /**
     * 会议室服务ID
     */
    public static final String OA_MT_MEETINGROOM_CODE = "OA_MT_MEETINGROOM";

    /**
     * 会议室预订信息
     */
    public static final String OA_MT_BOOKING_CODE = "OA_MT_BOOKING";


    /**
     * 查询列表上午查字段，用FLOOR_ID代替
     */
    public static final String MORNING = "FLOOR_ID";

    /**
     * 查询列表下午字段，用IMAGE代替
     */
    public static final String AFTERNOON = "IMAGE";

    /**
     * 查询列表晚上字段，用BUILDING_AREA代替
     */
    public static final String EVENING = "BUILDING_AREA";
    
    /**
     * 每个区域开始时间
     */
    public static final String START_TIME = "START_TIME";
    
    /**
     * 上午被占用标示位
     */
    public static final String MORNING_OCCUPY = "1";
    
    /**
     * 下午被占用标示位
     */
    public static final String AFTERNOON_OCCUPY = "2";
    
    /**
     * 晚上被占用标示位
     */
   public static final String EVENING_OCCUPY = "3";
    
    /**
     * 每个区域结束时间
     */
    public static final String END_TIME = "END_TIME";
    
    /**
     * 会议室不可用标示位 1为可用，2为不可用
     */
    public static final String MEETINGROOM_UNUSED = "2";

    /**
     * 会议室状态:12，为【取消】状态
     */
    public static final int BOOKING_STATUS_CANCEL = 12;
    /**
     * 会议室状态:11，为不同意
     */
    public static final int  BOOKING_STATUS_NOT_AGREE = 11;
    
    /**
     * 早于系统时间错误，在系统信息配置中
     */
    public static final String AFTERSYDATE_ERROR = "OA_MT_BOOKING_AFTERSYDATE_ERROR";
    
    /**
     * 此时间段已预订错误，在系统信息配置中
     */
    public static final String BOOKING_DATE_ERROR = "OA_MT_BOOKING_DATE_ERROR";
    
    /**
     * 预订信息已加锁错误提示，在系统信息配置中
     */
    public static final String BOOKING_LOCKED = "OA_MT_BOOKING_LOCKED";
    
    /**
     * 预订信息还未预定错误提示，在系统信息配置中
     */
    public static final String BOOKING_UNBOOKED = "OA_MT_BOOKING_UNBOOKED";
    
    /**
     * 预订信息已被他人取消错误提示，在系统信息配置中
     */
    public static final String BOOKING_CANCELLED = "OA_MT_BOOKING_CANCELLED";

    
    /**
     * 格式化日期
     * @param date 传递日期
     * @param formit 格式
     * @return 格式化之后的日期
     */
    public static String getFormitDate(String date, String formit) {
        String formitDate = "";
        try {
            formitDate = new SimpleDateFormat(formit).format(new SimpleDateFormat(formit).parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formitDate;
    }

    
    /**
     * 获取两个日期之间是相差天数
     * 
     * @param firstDate 第一个日期 yyyy-MM-dd
     * @param secondDate 第二个日期 yyyy-MM-dd
     * @return 相差天数
     */
    public static int getDateDiffDays(String firstDate, String secondDate) {
        int first = new Integer(firstDate.replace("-", "")).intValue();
        int second = new Integer(secondDate.replace("-", "")).intValue();
        int last = second - first;
        return Math.abs(last);
    }

    
    /**
     * 获得时间整数值
     * @param date 日期格式为 yyyy-MM-dd HH:mm:ss
     * @return 时间整数
     * @throws ParseException 异常
     */
    public static int getTimeNum(String date) throws ParseException {
        int timeNum = new Integer(new SimpleDateFormat("HH:mm").format(
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date)).replace(":", "")).intValue();
        return timeNum;
    }
    
    
    /**
     * 获得日期时间毫秒值
     * @param date 日期格式为 yyyy-MM-dd HH:mm:ss
     * @return 时间整数
     */
    public static long getNewTimeNum(String date) {
        long timeNum = 0;
        try {
            timeNum = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeNum;
    }

    
    /**
     * 拆分上午和下午时间区间，得到起始位置时间
     * 
     * @param morningArea 系统配置上午时间区域
     * @param afterNoonArea 时间配置下午时间区域
     * @return 时间区域起止时间
     */
    public static Map<String, String> getAreaTime(String morningArea, String afterNoonArea) {
        String[] morningStr = morningArea.split("-");
        String[] afterNoonStr = afterNoonArea.split("-");
        Map<String, String> m = new HashMap<String, String>();
        m.put(MORNING_START, morningStr[0] + ":00");
        m.put(MORNING_END, morningStr[1] + ":00");
        m.put(AFTERNOON_START, afterNoonStr[0] + ":00");
        m.put(AFTERNOON_END, afterNoonStr[1] + ":00");
        m.put(EVENING_START, afterNoonStr[1] + ":59");
        m.put(EVENING_END, "23:59:59");
        return m;
    }
    
    /**
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param areaStatrTime 上午、下午开始时间
     * @param areaEndTime 上下午结束时间
     * @return 是否已占用
     */
    private static boolean isCross(String startTime, String endTime, String areaStatrTime, String areaEndTime) {
        String strDate = DateUtils.getDate();
        // 记录中某一条记录的开始时间整数值，精确到分钟
        long startMs = DateUtils.getDateFromString(strDate + " " + startTime, DateUtils.FORMAT_DATETIME).getTime();

        // 记录中某一条记录的开结束时间整数值，精确到分钟
        long endMs = DateUtils.getDateFromString(strDate + " " + endTime, DateUtils.FORMAT_DATETIME).getTime();

        // 这条记录的圆心位置，即中间值
        long timeCenterDate = (endMs + startMs) / 2;

        // 这条记录的半径
        double timeRadiusData = Math.abs((endMs - timeCenterDate));

        // 记录中某一条记录的开始时间整数值，精确到分钟
        long areaStartMs = DateUtils.getDateFromString(strDate + " " + areaStatrTime, DateUtils.FORMAT_DATETIME)
                .getTime();

        // 记录中某一条记录的开结束时间整数值，精确到分钟
        long areaEndMs = DateUtils.getDateFromString(strDate + " " + areaEndTime, DateUtils.FORMAT_DATETIME)
                .getTime();

        // 这条记录的圆心位置，即中间值
        long areaCenter = (areaEndMs + areaStartMs) / 2;

        // 这条记录的半径
        double areaRadius = Math.abs((areaEndMs - areaCenter));
        
        //圆心之间差
        double centerData = Math.abs((areaCenter - timeCenterDate));
        //半径之和
        double radiusData = timeRadiusData + areaRadius;
        
        //圆心差 大于半径和，则表示两段时间不相交
        if (centerData >= radiusData) {
            return false;
        } 
        return true;        
        
    }
    
    /**
     * 判断两个时间段是否有交集
     * @param startTime1 开始时间1
     * @param endTime1 结束时间1
     * @param startTime2 开始时间2
     * @param endTime2 结束时间2
     * @return true 有交集；false 无交集
     */
    public static boolean isCross4TwoDateArea(String startTime1, String endTime1, String startTime2, String endTime2) {
        // 记录中某一条记录的开始时间1整数值，精确到分钟
        long startMs1 = DateUtils.getDateFromString(startTime1, DateUtils.FORMAT_DATETIME).getTime();
        // 记录中某一条记录的开结束时间1整数值，精确到分钟
        long endMs1 = DateUtils.getDateFromString(endTime1, DateUtils.FORMAT_DATETIME).getTime();
        // 记录中某一条记录的开始时间整2数值，精确到分钟
        long startMs2 = DateUtils.getDateFromString(startTime2, DateUtils.FORMAT_DATETIME).getTime();
        // 记录中某一条记录的开结束时间2整数值，精确到分钟
        long endMs2 = DateUtils.getDateFromString(endTime2, DateUtils.FORMAT_DATETIME).getTime();
        // 这条记录的圆心位置1，即中间值
        long timeCenterDate1 = (startMs1 + endMs1) / 2;
        // 这条记录的半径1
        double timeRadiusData1 = Math.abs((endMs1 - timeCenterDate1));
        // 这条记录的圆心位置2，即中间值
        long timeCenterDate2 = (startMs2 + endMs2) / 2;
        // 这条记录的半径2
        double timeRadiusData2 = Math.abs((endMs2 - timeCenterDate2));
        //圆心之间差
        double centerData = Math.abs((timeCenterDate1 - timeCenterDate2));
        //半径之和
        double radiusData = timeRadiusData1 + timeRadiusData2;
        //圆心差 大于半径和，则表示两段时间不相交
        if (centerData >= radiusData) {
            return false;
        }
        return true;        
    }
    
    /**
     * 判断事时间属于哪个时间区域
     * 
     * @param newDate 查询日期
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 返回占用其余标示位
     * @throws ParseException 
     */
    public static String isWhichArea(String newDate, String startDate, String endDate) throws ParseException {
        String morningTime = Context.getSyConf(OA_MT_MORNING_KEK, "9:00-12:00"); // 上午时间段
        String afterNoonTime = Context.getSyConf(OA_MT_AFTERNOON_KEY, "13:00-18:00"); // 下午时间段

        // 获得上、下、晚上的时间区域起止时间
        Map<String, String> m = getAreaTime(morningTime, afterNoonTime);

        String morningStartTime = m.get(MORNING_START); // 上午区域开始时间整数
        String morningEndTime = m.get(MORNING_END); // 上午区域结束时间整数
        String afterNoonStartTime = m.get(AFTERNOON_START); // 下午区域开始整数
        String afterNoonEndTime = m.get(AFTERNOON_END); // 下午区域结束时间整数
        String eveningStartTime = m.get(EVENING_START); // 晚上区域开始时间整数
        String eventingEndTime = m.get(EVENING_END); // 晚上区域结束时间整数
        
        
        String startTime = "";
        if ((newDate + " 00:00:00").compareTo(startDate) > 0) {
            startTime = "00:00:00";
        } else {
            startTime = startDate.substring(11);
        }
        
        String endTime = "";
        if ((newDate + " 23:59:59").compareTo(endDate) <= 0) {
            endTime = "23:59:59";
        } else {
            endTime = endDate.substring(11);
        }
        
        String rtnVal = "";
        if (isCross(startTime, endTime, morningStartTime, morningEndTime)) {
            rtnVal += "1";
        }

        if (isCross(startTime, endTime, afterNoonStartTime, afterNoonEndTime)) {
            rtnVal += "2";
        }

        if (isCross(startTime, endTime, eveningStartTime, eventingEndTime)) {
            rtnVal += "3";
        }
        
        return rtnVal;
    }

    
    /**
     * 解析Map拿到bookingID集合字符串，以，分割
     * 
     * @param m bookingId集合
     * @return 返回格式过之后的字符串
     */
    public static String parseBookingIdsMap(Map<String, String> m) {
        StringBuffer sb = new StringBuffer();
        String retStr = "";
        String[] dataStr = { MORNING_DATA, AFTERNOON_DATA, EVENING_DATA, MORNING_AND_AFTERNOON_DATA,
                AFTERNOON_AND_EVENING_DATA, ALL_DAY_DATA };
        for (int i = 0; i < dataStr.length; i++) {
            if (null != m.get(dataStr[i])) {
                sb.append(m.get(dataStr[i]));
            } else {
                sb.append("");
            }
            sb.append(",");
        }
        retStr = sb.toString();
        retStr = retStr.substring(0, retStr.lastIndexOf(","));
        return retStr;
    }

    
    /**
     * 拆分时间区域时间，拿到开始时间和结束时间字符串
     * @param timeArea 时间区域字符串，格式为 HH:MM-HH:MM
     * @return 时间区域开始时间和结束时间 格式为 HH:MM,HH:MM
     */
    public static String[] splitTimeArea(String timeArea) {
        return timeArea.split("-");
    }

    
    /**
     * 拆分时间区域，根据上、下午时间区域分出晚上时间区域
     * @return 上、下、晚上时间区域Map集合
     */
    public static Map<String, String> sliptTimeAreasMap() {
        String morningTime = Context.getSyConf(OA_MT_MORNING_KEK, "9:00-12:00"); // 上午时间段
        String afterNoonTime = Context.getSyConf(OA_MT_AFTERNOON_KEY, "13:00-18:00"); // 下午时间段
        String[] morningStr = morningTime.split("-");
        String[] afterNoonStr = afterNoonTime.split("-");
        Map<String, String> m = new HashMap<String, String>();
        m.put(MORNING_START, morningStr[0]);
        m.put(MORNING_END, morningStr[1]);
        m.put(AFTERNOON_START, afterNoonStr[0]);
        m.put(AFTERNOON_END, afterNoonStr[1]);
        m.put(EVENING_START, afterNoonStr[1]);
        m.put(EVENING_END, "23:59");
        return m;
    }
}
