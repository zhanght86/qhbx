package com.rh.oa.mt;

import com.rh.core.base.Bean;
import com.rh.core.serv.OutBean;

/**
 * 会议添加到个人日程接口
 * @author ruaho_hdy
 *
 */
public interface MeetingCalendar {
    
    /**
     * 增加日程
     * @param meetingBean 会议Bean
     * @param doneUsers 参会人员code,以英文逗号分隔。例如：123,456,789
     * @return 结果集
     */
    OutBean add(Bean meetingBean, String doneUsers);
    
    /**
     * 删除日程
     * @param meetingBean 会议Bean
     * @return 结果集
     */
    OutBean delete(Bean meetingBean);
}
