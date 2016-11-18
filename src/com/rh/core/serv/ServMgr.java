/*
 * Copyright (c) 2011 Ruaho All rights reserved.
 */
package com.rh.core.serv;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.base.db.Transaction;
import com.rh.core.org.mgr.OrgMgr;
import com.rh.core.util.Constant;


/**
 * 公共的服务响应类
 * 
 * @author Jerry Li
 */
public abstract class ServMgr {
	
    /** 服务主键：通讯录申请 */
    public static final String SY_COMM_ADDRESS_APPLY = "SY_COMM_ADDRESS_APPLY";
    /** 服务主键：共享联系人 */
    public static final String SY_COMM_ADDRESS_ASSIST = "SY_COMM_ADDRESS_ASSIST";
    /** 服务主键：通讯录分组 */
    public static final String SY_COMM_ADDRESS_GROUP = "SY_COMM_ADDRESS_GROUP";
    /** 服务主键：公司通讯录 */
    public static final String SY_COMM_ADDRESS_LIST = "SY_COMM_ADDRESS_LIST";
    /** 服务主键：关注 */
    public static final String SY_COMM_ATTENTION = "SY_COMM_ATTENTION";
    /** 服务主键：版块管理 */
    public static final String SY_COMM_BBS_CHNL = "SY_COMM_BBS_CHNL";
    /** 服务主键：论坛站点 */
    public static final String SY_COMM_BBS_SITE = "SY_COMM_BBS_SITE";
    /** 服务主键：论坛模版 */
    public static final String SY_COMM_BBS_TMPL = "SY_COMM_BBS_TMPL";
    /** 服务主键：论坛发帖 */
    public static final String SY_COMM_BBS_TOPIC = "SY_COMM_BBS_TOPIC";
    /** 服务主键：隶属关系 */
    public static final String SY_COMM_BELONG = "SY_COMM_BELONG";
    /** 服务主键：日程 */
    public static final String SY_COMM_CAL = "SY_COMM_CAL";
    /** 服务主键： 投票记录 */
    public static final String SY_COMM_VOTE = "SY_COMM_VOTE";
    /** 服务主键：日程共享对象 */
    public static final String SY_COMM_CAL_OBJ = "SY_COMM_CAL_OBJ";
    /** 服务主键：日程查询 */
    public static final String SY_COMM_CAL_REC_V = "SY_COMM_CAL_REC_V";
    /** 服务主键：日程共享设置 */
    public static final String SY_COMM_CAL_SHARE = "SY_COMM_CAL_SHARE";
    /** 服务主键：日程类型 */
    public static final String SY_COMM_CAL_TYPE = "SY_COMM_CAL_TYPE";
    /** 服务主键：日程委派 */
    public static final String SY_COMM_CAL_USERS = "SY_COMM_CAL_USERS";
    /** 服务主键：CMS_栏目管理 */
    public static final String SY_COMM_CMS_CHNL = "SY_COMM_CMS_CHNL";
    /** 服务主键：CMS_站点管理 */
    public static final String SY_COMM_CMS_SITE = "SY_COMM_CMS_SITE";
    /** 服务主键：CMS_站点讨论帖管理 */
    public static final String SY_COMM_CMS_SITE_BBS = "SY_COMM_CMS_SITE_BBS";
    /** 服务主键：CMS_站点论坛栏目管理 */
    public static final String SY_COMM_CMS_SITE_BBS_CHNL = "SY_COMM_CMS_SITE_BBS_CHNL";
    /** 服务主键：CMS_站点栏目管理 */
    public static final String SY_COMM_CMS_SITE_CHNL = "SY_COMM_CMS_SITE_CHNL";
    /** 服务主键：默认站点设置 */
    public static final String SY_COMM_CMS_SITE_DEFAULT = "SY_COMM_CMS_SITE_DEFAULT";
    /** 服务主键：CMS_站点链接 */
    public static final String SY_COMM_CMS_SITE_LINKER = "SY_COMM_CMS_SITE_LINKER";
    /** 服务主键：CMS_站点新闻管理 */
    public static final String SY_COMM_CMS_SITE_NEWS = "SY_COMM_CMS_SITE_NEWS";
    /** 服务主键：CMS_站点新闻栏目管理 */
    public static final String SY_COMM_CMS_SITE_NEWS_CHNL = "SY_COMM_CMS_SITE_NEWS_CHNL";
    /** 服务主键：CMS_站点文档管理 */
    public static final String SY_COMM_CMS_SITE_WENKU = "SY_COMM_CMS_SITE_WENKU";
    /** 服务主键：CMS_站点文库栏目管理 */
    public static final String SY_COMM_CMS_SITE_WENKU_CHNL = "SY_COMM_CMS_SITE_WENKU_CHNL";
    /** 服务主键：CMS_模版设置 */
    public static final String SY_COMM_CMS_TMPL = "SY_COMM_CMS_TMPL";
    /** 服务主键：完成度查询 */
    public static final String SY_COMM_COMPLETE_DATA = "SY_COMM_COMPLETE_DATA";
    /** 服务主键：完成度配置 */
    public static final String SY_COMM_COMPLETE_SETTINGS = "SY_COMM_COMPLETE_SETTINGS";
    /** 服务主键：系统配置（私有） */
    public static final String SY_COMM_CONFIG = "SY_COMM_CONFIG";
    /** 服务主键：系统配置（公共） */
    public static final String SY_COMM_CONFIG_PUBLIC = "SY_COMM_CONFIG_PUBLIC";
    /** 服务主键：数据展示 */
    public static final String SY_COMM_DATA_VIEW = "SY_COMM_DATA_VIEW";
    /** 服务主键：数据项 */
    public static final String SY_COMM_DATA_VIEW_ITEM = "SY_COMM_DATA_VIEW_ITEM";
    /** 服务主键：工作台管理 */
    public static final String SY_COMM_DESK = "SY_COMM_DESK";
    /** 服务主键：工作台管理（公共） */
    public static final String SY_COMM_DESK_PUBLIC = "SY_COMM_DESK_PUBLIC";
    /** 服务主键：主办事务 */
    public static final String SY_COMM_ENTITY = "SY_COMM_ENTITY";
    /** 服务主键：我关注的事项 */
    public static final String SY_COMM_ENTITY_ATTENTION = "SY_COMM_ENTITY_ATTENTION";
    /** 服务主键：在办事务 */
    public static final String SY_COMM_ENTITY_DOING = "SY_COMM_ENTITY_DOING";
    /** 服务主键：已办已结审批单 */
    public static final String SY_COMM_ENTITY_DONE_FINISH = "SY_COMM_ENTITY_DONE_FINISH";
    /** 服务主键：已办未结审批单 */
    public static final String SY_COMM_ENTITY_DONE_RUN = "SY_COMM_ENTITY_DONE_RUN";
    /** 服务主键：公文管理按钮用到 */
    public static final String SY_COMM_ENTITY_GL = "SY_COMM_ENTITY_GL";
    /** 服务主键：我接收的文件 */
    public static final String SY_COMM_ENTITY_RECEIVER = "SY_COMM_ENTITY_RECEIVER";
    /** 服务主键：我分发的文件 */
    public static final String SY_COMM_ENTITY_SENDER = "SY_COMM_ENTITY_SENDER";
    /** 服务主键：单据状态父服务 */
    public static final String SY_COMM_ENTITY_STATE = "SY_COMM_ENTITY_STATE";
    /** 服务主键：我关注的事项 */
    public static final String SY_COMM_ENTITY_TAG = "SY_COMM_ENTITY_TAG";
    /** 服务主键：个人回收站 */
    public static final String SY_COMM_ENTITY_TRASH = "SY_COMM_ENTITY_TRASH";
    /** 服务主键：机构回收站 */
    public static final String SY_COMM_ENTITY_TRASH_ORG = "SY_COMM_ENTITY_TRASH_ORG";
    /** 服务主键：主办事务 */
    public static final String SY_COMM_ENTITY_ZHUBAN = "SY_COMM_ENTITY_ZHUBAN";
    /** 服务主键：表达式 */
    public static final String SY_COMM_EXPRESSION = "SY_COMM_EXPRESSION";
    /** 服务主键：系统文件 */
    public static final String SY_COMM_FILE = "SY_COMM_FILE";
    /** 服务主键：系统历史文件 */
    public static final String SY_COMM_FILE_HIS = "SY_COMM_FILE_HIS";
    /** 服务主键：系统通用信息 */
    public static final String SY_COMM_INFO = "SY_COMM_INFO";
    /** 服务主键：我报送的信息 */
    public static final String SY_COMM_INFOS = "SY_COMM_INFOS";
    /** 服务主键：信息审签单 */
    public static final String SY_COMM_INFOS_AUDIT = "SY_COMM_INFOS_AUDIT";
    /** 服务主键：需要我处理的信息 */
    public static final String SY_COMM_INFOS_DEAL = "SY_COMM_INFOS_DEAL";
    /** 服务主键：信息管理 */
    public static final String SY_COMM_INFOS_MGR = "SY_COMM_INFOS_MGR";
    /** 服务主键：信息浏览 */
    public static final String SY_COMM_INFOS_SCAN = "SY_COMM_INFOS_SCAN";
    /** 服务主键：信息栏目 */
    public static final String SY_COMM_INFOS_CHNL = "SY_COMM_INFOS_CHNL";
    
    
    /** 服务主键：用户积分 */
    public static final String SY_COMM_INTEGRAL = "SY_COMM_INTEGRAL";
    /** 服务主键：积分规则 */
    public static final String SY_COMM_INTEGRAL_RULE = "SY_COMM_INTEGRAL_RULE";
    /** 服务主键：积分记录 */
    public static final String SY_COMM_INTEGRAL_SCORE = "SY_COMM_INTEGRAL_SCORE";
    /** 服务主键：我采编的期刊 */
    public static final String SY_COMM_JOURNAL = "SY_COMM_JOURNAL";
    /** 服务主键：期刊采编信息 */
    public static final String SY_COMM_JOURNAL_NEWS = "SY_COMM_JOURNAL_NEWS";
    /** 服务主键：期刊信息关联服务 */
    public static final String SY_COMM_JOURNAL_NEWS_LINK = "SY_COMM_JOURNAL_NEWS_LINK";
    /** 服务主键：期刊模板 */
    public static final String SY_COMM_JOURNAL_TMPL = "SY_COMM_JOURNAL_TMPL";
    /** 服务主键：知识库 */
    public static final String SY_COMM_KM = "SY_COMM_KM";
    /** 服务主键：常用链接 */
    public static final String SY_COMM_LINK = "SY_COMM_LINK";
    /** 服务主键：个人备忘录 */
    public static final String SY_COMM_MEMO_PAD = "SY_COMM_MEMO_PAD";
    /** 服务主键：菜单管理 */
    public static final String SY_COMM_MENU = "SY_COMM_MENU";
    /** 服务主键：菜单（公共） */
    public static final String SY_COMM_MENU_PUBLIC = "SY_COMM_MENU_PUBLIC";
    /** 服务主键：系统信息 */
    public static final String SY_COMM_MESSAGE = "SY_COMM_MESSAGE";
    /** 服务主键：意见 */
    public static final String SY_COMM_MIND = "SY_COMM_MIND";
    /** 服务主键：意见编码 */
    public static final String SY_COMM_MIND_CODE = "SY_COMM_MIND_CODE";
    /** 服务主键：固定意见 */
    public static final String SY_COMM_MIND_REGULAR = "SY_COMM_MIND_REGULAR";
    /** 服务主键：意见类型 */
    public static final String SY_COMM_MIND_TYPE = "SY_COMM_MIND_TYPE";
    /** 服务主键：个人常用意见 */
    public static final String SY_COMM_MIND_USUAL = "SY_COMM_MIND_USUAL";
    /** 服务主键：消息监听 */
    public static final String SY_COMM_MSG_LISTENER = "SY_COMM_MSG_LISTENER";
    /** 服务主键：待办提醒方式 */
    public static final String SY_COMM_MSG_TYPE = "SY_COMM_MSG_TYPE";
    /** 服务主键：信息发布 */
    public static final String SY_COMM_NEWS = "SY_COMM_NEWS";
    /** 服务主键：信息管理基础服务 */
    public static final String SY_COMM_NEWS_BASE = "SY_COMM_NEWS_BASE";
    /** 服务主键：新闻栏目 */
    public static final String SY_COMM_NEWS_CHNL = "SY_COMM_NEWS_CHNL";
    /** 服务主键：新闻浏览 */
    public static final String SY_COMM_NEWS_CHNLNEWS = "SY_COMM_NEWS_CHNLNEWS";
    /** 服务主键：新闻栏目查看 */
    public static final String SY_COMM_NEWS_CHNLVIEW = "SY_COMM_NEWS_CHNLVIEW";
    /** 服务主键：新闻投票 */
    public static final String SY_COMM_NEWS_POLL = "SY_COMM_NEWS_POLL";
    /** 服务主键：信息查询 */
    public static final String SY_COMM_NEWS_QUERY = "SY_COMM_NEWS_QUERY";
    /** 服务主键：新闻阅读历史 */
    public static final String SY_COMM_NEWS_READ_HIS = "SY_COMM_NEWS_READ_HIS";
    /** 服务主键：新闻站点 */
    public static final String SY_COMM_NEWS_SITE = "SY_COMM_NEWS_SITE";
    /** 服务主键：新闻模板 */
    public static final String SY_COMM_NEWS_TMPL = "SY_COMM_NEWS_TMPL";
    /** 服务主键：公告 */
    public static final String SY_COMM_NOTICE = "SY_COMM_NOTICE";
    
    /** 知道公告*/
    public static final String SY_COMM_ZHIDAO_NOTICE = "SY_COMM_ZHIDAO_NOTICE";
    /** 知道分类 */
    public static final String SY_COMM_ZHIDAO_CHNL = "SY_COMM_ZHIDAO_CHNL";
    
    /** 知道关注 */
    public static final String SY_COMM_ZHIDAO_FOLLOW = "SY_COMM_ZHIDAO_FOLLOW";
    
    /** 知道我关注的用户*/
    public static final String SY_COMM_ZHIDAO_MY_PERSON_FOLLOW = "SY_COMM_ZHIDAO_MY_PERSON_FOLLOW";
    /** 知道我关注的问题*/
    public static final String SY_COMM_ZHIDAO_MY_QUESTION_FOLLOW = "SY_COMM_ZHIDAO_MY_QUESTION_FOLLOW";
    /** 知道我关注的分类*/
    public static final String SY_COMM_ZHIDAO_MY_CATEGORY_FOLLOW = "SY_COMM_ZHIDAO_MY_CATEGORY_FOLLOW";
    /** 知道邀请回答 */
    public static final String SY_COMM_ZHIDAO_INVITE = "SY_COMM_ZHIDAO_INVITE";
    /** 知道分享服务 */
    public static final String SY_COMM_ZHIDAO_SHARE = "SY_COMM_ZHIDAO_SHARE";
    /** 知道用户服务 */
    public static final String SY_COMM_ZHIDAO_USER = "SY_COMM_ZHIDAO_USER";
    /** 知道用户领域服务 */
    public static final String SY_COMM_ZHIDAO_USER_SUBJECT = "SY_COMM_ZHIDAO_USER_SUBJECT";
    /** 知道专家积分 */
    public static final String SY_COMM_ZHIDAO_SPEC_ALL_INTE = "SY_COMM_ZHIDAO_SPEC_ALL_INTE";
    /** 知道专家积分明细 */
    public static final String SY_COMM_ZHIDAO_SPEC_INTEGRAL = "SY_COMM_ZHIDAO_SPEC_INTEGRAL";
    
    /** 服务主键：导出XODC格式化文件流 */
    public static final String SY_COMM_OUTPUT_XDOC = "SY_COMM_OUTPUT_XDOC";
    /** 服务主键：个人通讯录 */
    public static final String SY_COMM_PERSONAL_ADDRESS = "SY_COMM_PERSONAL_ADDRESS";
    /** 服务主键: 分享*/
    public static final String SY_COMM_SHARE = "SY_COMM_SHARE";
    /** 服务主键: 分享内容*/
    public static final String SY_COMM_SHARE_ITEM = "SY_COMM_SHARE_ITEM";
    /** 服务主键：投票 */
    public static final String SY_COMM_POLL = "SY_COMM_POLL";
    /** 服务主键：投票选项 */
    public static final String SY_COMM_POLL_OPTION = "SY_COMM_POLL_OPTION";
    /** 服务主键：投票结果 */
    public static final String SY_COMM_POLL_VOTE = "SY_COMM_POLL_VOTE";
    /** 服务主键：提醒消息 */
    public static final String SY_COMM_REMIND = "SY_COMM_REMIND";
    /** 服务主键：提醒消息类型分组 */
    public static final String SY_COMM_REMIND_GROUP = "SY_COMM_REMIND_GROUP";
    /** 服务主键：提醒消息历史 */
    public static final String SY_COMM_REMIND_HIS = "SY_COMM_REMIND_HIS";
    /** 服务主键：提醒我的 */
    public static final String SY_COMM_REMIND_ME = "SY_COMM_REMIND_ME";
    /** 服务主键：被提醒人 */
    public static final String SY_COMM_REMIND_USERS = "SY_COMM_REMIND_USERS";
    /** 服务主键：用户请求历史 */
    public static final String SY_COMM_REQ_HIS = "SY_COMM_REQ_HIS";
    /** 服务主键: 事件 */
    public static final String SY_COMM_EVENT = "SY_COMM_EVENT";
    /** 服务主键: 知道事件(动态) */
    public static final String SY_COMM_ZHIDAO_EVENT = "SY_COMM_ZHIDAO_EVENT";
    /** 服务主键：任务计划 */
    public static final String SY_COMM_SCHED = "SY_COMM_SCHED";
    
    /** 服务主键：本地任务 */
    public static final String SY_COMM_LOCAL_SCHED = "SY_COMM_LOCAL_SCHED";
    
    /** 服务主键：本地任务执行日志 */
    public static final String SY_COMM_LOCAL_SCHED_HIS = "SY_COMM_LOCAL_SCHED_HIS";
    
    /** 服务主键：任务计划_任务执行日志 */
    public static final String SY_COMM_SCHED_HIS = "SY_COMM_SCHED_HIS";
    /** 服务主键：任务计划_触发器 */
    public static final String SY_COMM_SCHED_TRIGGER = "SY_COMM_SCHED_TRIGGER";
    /** 服务主键：打分服务 */
    public static final String SY_COMM_SCORE = "SY_COMM_SCORE";
    /** 服务主键：打分结果 */
    public static final String SY_COMM_SCORE_VOTE = "SY_COMM_SCORE_VOTE";
    /** 服务主键：分发方案表 */
    public static final String SY_COMM_SEND = "SY_COMM_SEND";
    /** 服务主键：分发明细 */
    public static final String SY_COMM_SEND_DETAIL = "SY_COMM_SEND_DETAIL";
    /** 服务主键：分发方案明细 */
    public static final String SY_COMM_SEND_ITEM = "SY_COMM_SEND_ITEM";
    /** 服务主键：分发方案明细部门 */
    public static final String SY_COMM_SEND_ITEM_DEPT = "SY_COMM_SEND_ITEM_DEPT";
    /** 服务主键：分发方案明细角色 */
    public static final String SY_COMM_SEND_ITEM_ROLE = "SY_COMM_SEND_ITEM_ROLE";
    /** 服务主键：分发方案明细用户 */
    public static final String SY_COMM_SEND_ITEM_USER = "SY_COMM_SEND_ITEM_USER";
    /** 服务主键：我发出的提醒 */
    public static final String SY_COMM_SEND_REMIND = "SY_COMM_SEND_REMIND";
    /** 服务主键：分发方案选择 */
    public static final String SY_COMM_SEND_SELECT = "SY_COMM_SEND_SELECT";
    /** 服务主键：分发卡片页面 */
    public static final String SY_COMM_SEND_SHOW_CARD = "SY_COMM_SEND_SHOW_CARD";
    /** 服务主键：分发人员列表 */
    public static final String SY_COMM_SEND_SHOW_USERS = "SY_COMM_SEND_SHOW_USERS";
    /** 服务主键：动态搜索 */
    public static final String SY_COMM_SUGGEST = "SY_COMM_SUGGEST";
    /** 服务主键：标签管理 */
    public static final String SY_COMM_TAG = "SY_COMM_TAG";
    /** 服务主键：任务管理 */
    public static final String SY_COMM_TASK = "SY_COMM_TASK";
    /** 服务主键：我分配的任务 */
    public static final String SY_COMM_TASK_ASSIGN = "SY_COMM_TASK_ASSIGN";
    /** 服务主键：下属的任务 */
    public static final String SY_COMM_TASK_BEL = "SY_COMM_TASK_BEL";
    /** 服务主键：我已完成任务 */
    public static final String SY_COMM_TASK_DONE = "SY_COMM_TASK_DONE";
    /** 服务主键：我接收的任务 */
    public static final String SY_COMM_TASK_REC = "SY_COMM_TASK_REC";
    /** 服务主键：今日待处理任务 */
    public static final String SY_COMM_TASK_TODAY = "SY_COMM_TASK_TODAY";
    /** 服务主键：我未完成任务 */
    public static final String SY_COMM_TASK_UNDONE = "SY_COMM_TASK_UNDONE";
    /** 服务主键：门户模版 */
    public static final String SY_COMM_TEMPL = "SY_COMM_TEMPL";
    /** 服务主键：模板组件 */
    public static final String SY_COMM_TEMPL_COMS = "SY_COMM_TEMPL_COMS";
    /** 服务主键：测试服务 */
    public static final String SY_COMM_TEST = "SY_COMM_TEST";
    /** 服务主键：待办事务 */
    public static final String SY_COMM_TODO = "SY_COMM_TODO";
    /** 服务主键：委办事务 */
    public static final String SY_COMM_TODO_AGENT = "SY_COMM_TODO_AGENT";
    /** 服务主键：已办事务 */
    public static final String SY_COMM_TODO_HIS = "SY_COMM_TODO_HIS";
    /** 服务主键：待阅事务 */
    public static final String SY_COMM_TODO_READ = "SY_COMM_TODO_READ";
    /** 服务主键：人员查阅 */
    public static final String SY_COMM_USER_WORK = "SY_COMM_USER_WORK";
    /** 服务主键：常用批语 */
    public static final String SY_COMM_USUAL = "SY_COMM_USUAL";
    /** 服务主键：文库分类 */
    public static final String SY_COMM_WENKU_CHNL = "SY_COMM_WENKU_CHNL";
    /** 服务主键：文辑管理 */
    public static final String SY_COMM_WENKU_DOCLIST = "SY_COMM_WENKU_DOCLIST";
    /** 服务主键：文辑项 */
    public static final String SY_COMM_WENKU_DOCLIST_ITEM = "SY_COMM_WENKU_DOCLIST_ITEM";
    /** 服务主键：文档管理 */
    public static final String SY_COMM_WENKU_DOCUMENT = "SY_COMM_WENKU_DOCUMENT";
    /** 服务主键：文档收藏夹 */
    public static final String SY_COMM_WENKU_FAVORITES = "SY_COMM_WENKU_FAVORITES";
    /** 服务主键：我的文辑 */
    public static final String SY_COMM_WENKU_MYDOCLIST = "SY_COMM_WENKU_MYDOCLIST";
    /** 服务主键：我的文档 */
    public static final String SY_COMM_WENKU_MYDOCUMENT = "SY_COMM_WENKU_MYDOCUMENT";
    /** 服务主键：我的下载 */
    public static final String SY_COMM_WENKU_MYDOWNLOAD = "SY_COMM_WENKU_MYDOWNLOAD";
    /** 服务主键：我的阅读 */
    public static final String SY_COMM_WENKU_MYREAD = "SY_COMM_WENKU_MYREAD";
    /** 服务主键：我的文库积分明细 */
    public static final String SY_COMM_WENKU_MY_INTEGRAL = "SY_COMM_WENKU_MY_INTEGRAL";
    /** 服务主键：文库公告 */
    public static final String SY_COMM_WENKU_NOTICE = "SY_COMM_WENKU_NOTICE";
    /** 服务主键：文档阅读下载历史 */
    public static final String SY_COMM_WENKU_REQHIS = "SY_COMM_WENKU_REQHIS";
    /** 服务主键：文档评分 */
    public static final String SY_COMM_WENKU_SCORE = "SY_COMM_WENKU_SCORE";
    /** 服务主键：文库站点 */
    public static final String SY_COMM_WENKU_SITE = "SY_COMM_WENKU_SITE";
    /** 服务主键：文库模版 */
    public static final String SY_COMM_WENKU_TMPL = "SY_COMM_WENKU_TMPL";
    /** 服务主键：工位 */
    public static final String SY_COMM_WORKLOC = "SY_COMM_WORKLOC";
    /** 服务主键：工位楼层 */
    public static final String SY_COMM_WORKLOC_FLOOR = "SY_COMM_WORKLOC_FLOOR";
    /** 服务主键：工位历史 */
    public static final String SY_COMM_WORKLOC_HIS = "SY_COMM_WORKLOC_HIS";
    /** 服务主键：工作日期设置 */
    public static final String SY_COMM_WORK_DAY = "SY_COMM_WORK_DAY";
    /** 服务主键：工作查阅 */
    public static final String SY_COMM_WORK_READ = "SY_COMM_WORK_READ";
    /** 服务主键：回答 */
    public static final String SY_COMM_ZHIDAO_ANSWER = "SY_COMM_ZHIDAO_ANSWER";
    /** 服务主键：知道提问 */
    public static final String SY_COMM_ZHIDAO_QUESTION = "SY_COMM_ZHIDAO_QUESTION";
    /** 服务主键：知道专家 */
    public static final String SY_COMM_ZHIDAO_SPECIALIST = "SY_COMM_ZHIDAO_SPECIALIST";
    /** 服务主键：知道专家领域 */
    public static final String SY_COMM_ZHIDAO_SPEC_SUBJECT = "SY_COMM_ZHIDAO_SPEC_SUBJECT";
    /** 服务主键：知道回答投票*/
    public static final String SY_COMM_ZHIDAO_ANSWER_VOTE = "SY_COMM_ZHIDAO_ANSWER_VOTE";
    /** 服务主键：我的关注*/
    public static final String SY_COMM_ZHIDAO_MYFOLLOW = "SY_COMM_ZHIDAO_MYFOLLOW";
    /** 服务主键：关注 */
    public static final String SY_COMM_FOLLOW = "SY_COMM_FOLLOW";
    /** 服务主键：知道人员关注 */
    public static final String SY_COMM_ZHIDAO_PERSON_FOLLOW = "SY_COMM_ZHIDAO_PERSON_FOLLOW";
    /** 服务主键：知道分类关注 */
    public static final String SY_COMM_ZHIDAO_CATEGORY_FOLLOW = "SY_COMM_ZHIDAO_CATEGORY_FOLLOW";
    /** 服务主键：知道问题关注 */
    public static final String SY_COMM_ZHIDAO_QUESTION_FOLLOW = "SY_COMM_ZHIDAO_QUESTION_FOLLOW";
    /**知道专题 */
    public static final String SY_COMM_ZHIDAO_TOPIC = "SY_COMM_ZHIDAO_TOPIC";
    /**知道专题项 */
    public static final String SY_COMM_ZHIDAO_TOPIC_ITEM = "SY_COMM_ZHIDAO_TOPIC_ITEM";
    /**通用收藏夹 */
    public static final String SY_COMM_FAVORITES = "SY_COMM_FAVORITES";
    /**标签 */
    public static final String SY_COMM_MARK = "SY_COMM_MARK";
    /**收藏夹-标签 */
    public static final String SY_COMM_FAVORITES_MARK = "SY_COMM_FAVORITES_MARK";
    /** 服务主键：系统文件 */
    public static final String SY_MODIFY_FILE = "SY_MODIFY_FILE";
    /** 服务主键：权限控制列表 */
    public static final String SY_ORG_ACL = "SY_ORG_ACL";
    /** 服务主键：公司管理 */
    public static final String SY_ORG_CMPY = "SY_ORG_CMPY";
    /** 服务主键：机构管理(机构内) */
    public static final String SY_ORG_DEPT = "SY_ORG_DEPT";
    /** 服务主键：机构管理(全部) */
    public static final String SY_ORG_DEPT_ALL = "SY_ORG_DEPT_ALL";
    /** 服务主键：部门主管表 */
    public static final String SY_ORG_DEPT_DIRECTOR = "SY_ORG_DEPT_DIRECTOR";
    /** 服务主键：机构管理(含下级) */
    public static final String SY_ORG_DEPT_SUB = "SY_ORG_DEPT_SUB";
    /** 服务主键：群组管理 */
    public static final String SY_ORG_GROUP = "SY_ORG_GROUP";
    /** 服务主键：群公告 */
    public static final String SY_ORG_GROUP_NOTICE = "SY_ORG_GROUP_NOTICE";
    /** 服务主键：群组用户 */
    public static final String SY_ORG_GROUP_USER = "SY_ORG_GROUP_USER";
    /** 服务主键：用户登录 */
    public static final String SY_ORG_LOGIN = "SY_ORG_LOGIN";
    /** 服务主键：角色管理 */
    public static final String SY_ORG_ROLE = "SY_ORG_ROLE";
    /** 服务主键：角色管理（全部） */
    public static final String SY_ORG_ROLE_ALL = "SY_ORG_ROLE_ALL";
    /** 服务主键：角色管理（公共） */
    public static final String SY_ORG_ROLE_PUBLIC = "SY_ORG_ROLE_PUBLIC";
    /** 服务主键：角色管理（含下级） */
    public static final String SY_ORG_ROLE_SUB = "SY_ORG_ROLE_SUB";
    /** 服务主键：角色用户表 */
    public static final String SY_ORG_ROLE_USER = "SY_ORG_ROLE_USER";
    /** 服务主键：人员管理(机构内) */
    public static final String SY_ORG_USER = "SY_ORG_USER";
    /** 服务主键：用户委托管理 */
    public static final String SY_ORG_USER_AGENT = "SY_ORG_USER_AGENT";
    /** 服务主键：用户委托业务类型设置 */
    public static final String SY_ORG_USER_AGT_TYPE = "SY_ORG_USER_AGT_TYPE";
    /** 服务主键：人员管理(全部) */
    public static final String SY_ORG_USER_ALL = "SY_ORG_USER_ALL";
    /** 服务主键：用户中心 */
    public static final String SY_ORG_USER_CENTER = "SY_ORG_USER_CENTER";
    /** 服务主键：联系方式 */
    public static final String SY_ORG_USER_CONTACT = "SY_ORG_USER_CONTACT";
    /** 服务主键：工作台设置 */
    public static final String SY_ORG_USER_DESK = "SY_ORG_USER_DESK";
    /** 服务主键：图标化首页 */
    public static final String SY_ORG_USER_DESK_ICON = "SY_ORG_USER_DESK_ICON";
    /** 服务主键：手机桌面设置 */
    public static final String SY_ORG_USER_DESK_MB = "SY_ORG_USER_DESK_MB";
    /** 服务主键：用户头像 */
    public static final String SY_ORG_USER_IMG = "SY_ORG_USER_IMG";
    /** 服务主键：个人信息 */
    public static final String SY_ORG_USER_INFO_SELF = "SY_ORG_USER_INFO_SELF";
    /** 服务主键：在线用户管理 */
    public static final String SY_ORG_USER_ONLINE = "SY_ORG_USER_ONLINE";
    /** 服务主键：用户密码 */
    public static final String SY_ORG_USER_PASSWD = "SY_ORG_USER_PASSWD";
    /** 服务主键：用户密码管理 */
    public static final String SY_ORG_USER_PASSWORD = "SY_ORG_USER_PASSWORD";
    /** 服务主键：用户关系表 */
    public static final String SY_ORG_USER_RELATION = "SY_ORG_USER_RELATION";
    /** 服务主键：教育经历 */
    public static final String SY_ORG_USER_RESUME = "SY_ORG_USER_RESUME";
    /** 服务主键：工作经历 */
    public static final String SY_ORG_USER_RESUME_WORK = "SY_ORG_USER_RESUME_WORK";
    /** 服务主键：奖惩情况 */
    public static final String SY_ORG_USER_REWARD = "SY_ORG_USER_REWARD";
    /** 服务主键：用户状态信息 */
    public static final String SY_ORG_USER_STATE = "SY_ORG_USER_STATE";
    /** 服务主键：风格设定 */
    public static final String SY_ORG_USER_STYLE = "SY_ORG_USER_STYLE";
    /** 服务主键：人员管理(含下级) */
    public static final String SY_ORG_USER_SUB = "SY_ORG_USER_SUB";
    /** 服务主键：用户业务委托管理 */
    public static final String SY_ORG_USER_TYPE_AGENT = "SY_ORG_USER_TYPE_AGENT";
    /** 服务主键：委托列表 */
    public static final String SY_ORG_USER_TYPE_AGENT_FROM = "SY_ORG_USER_TYPE_AGENT_FROM";
    /** 服务主键：委托我的 */
    public static final String SY_ORG_USER_TYPE_AGENT_TO = "SY_ORG_USER_TYPE_AGENT_TO";
    /** 服务主键：报表服务 */
    public static final String SY_PLUG_REPORT = "SY_PLUG_REPORT";
    /** 服务主键：报表服务字段项 */
    public static final String SY_PLUG_REPORT_ITEM = "SY_PLUG_REPORT_ITEM";
    /** 服务主键：搜索 */
    public static final String SY_PLUG_SEARCH = "SY_PLUG_SEARCH";
    /** 服务主键：搜索-个性化数据 */
    public static final String SY_PLUG_SEARCH_CUSTOM = "SY_PLUG_SEARCH_CUSTOM";
    /** 服务主键：检索关联服务 */
    public static final String SY_PLUG_SEARCH_LINK = "SY_PLUG_SEARCH_LINK";
    /** 服务主键：互联网 */
    public static final String SY_PLUG_SEARCH_WEB = "SY_PLUG_SEARCH_WEB";
    /** 服务主键：互联网抓取服务 */
    public static final String SY_PLUG_SEARCH_WEBCRAW = "SY_PLUG_SEARCH_WEBCRAW";
    /** 服务主键：全文检索词库 */
    public static final String SY_PLUG_SEARCH_WORD = "SY_PLUG_SEARCH_WORD";
    /** 服务主键：XDOC服务 */
    public static final String SY_PLUG_XDOC = "SY_PLUG_XDOC";
    /** 服务主键：服务定义 */
    public static final String SY_SERV = "SY_SERV";
    /** 服务主键：权限控制列表 */
    public static final String SY_SERV_ACL = "SY_SERV_ACL";
    /** 服务主键：服务按钮 */
    public static final String SY_SERV_ACT = "SY_SERV_ACT";
    /** 服务主键：按钮图标 */
    public static final String SY_SERV_ACT_ICONS = "SY_SERV_ACT_ICONS";
    /** 服务主键：参数定义 */
    public static final String SY_SERV_ACT_PARAM = "SY_SERV_ACT_PARAM";
    /** 服务主键：评论表 */
    public static final String SY_SERV_COMMENT = "SY_SERV_COMMENT";
    /** 服务主键：评论支持反对 */
    public static final String SY_SERV_COMMENT_VOTE = "SY_SERV_COMMENT_VOTE";
    /** 服务主键：数据权限定义 */
    public static final String SY_SERV_DACL = "SY_SERV_DACL";
    /** 服务主键：数据权限 */
    public static final String SY_SERV_DACL_ITEM = "SY_SERV_DACL_ITEM";
    /** 服务主键：数据字典 */
    public static final String SY_SERV_DICT = "SY_SERV_DICT";
    /** 服务主键：字典数据信息表 */
    public static final String SY_SERV_DICT_ITEM = "SY_SERV_DICT_ITEM";
    /** 服务主键：字典项（公司内） */
    public static final String SY_SERV_DICT_ITEM_CMPY = "SY_SERV_DICT_ITEM_CMPY";
    /** 服务主键：流经表 */
    public static final String SY_SERV_FLOW = "SY_SERV_FLOW";
    /** 服务主键：服务项 */
    public static final String SY_SERV_ITEM = "SY_SERV_ITEM";
    /** 服务主键：服务关联定义 */
    public static final String SY_SERV_LINK = "SY_SERV_LINK";
    /** 服务主键：关联明细设定 */
    public static final String SY_SERV_LINK_ITEM = "SY_SERV_LINK_ITEM";
    /** 服务主键：服务监听 */
    public static final String SY_SERV_LISTENER = "SY_SERV_LISTENER";
    /** 服务主键：操作留痕 */
    public static final String SY_SERV_LOG_ACT = "SY_SERV_LOG_ACT";
    /** 服务主键：变更监控 */
    public static final String SY_SERV_LOG_ITEM = "SY_SERV_LOG_ITEM";
    /** 服务主键：变更监控 */
    public static final String SY_SERV_LOG_ITEM_SINGLE = "SY_SERV_LOG_ITEM_SINGLE";
    /** 服务主键：服务定义（全部） */
    public static final String SY_SERV_PUBLIC = "SY_SERV_PUBLIC";
    /** 服务主键：常用查询 */
    public static final String SY_SERV_QUERY = "SY_SERV_QUERY";
    /** 服务主键：相关文件 */
    public static final String SY_SERV_RELATE = "SY_SERV_RELATE";
    /** 服务主键：全文检索设置 */
    public static final String SY_SERV_SEARCH = "SY_SERV_SEARCH";
    /** 服务主键：数据库表 */
    public static final String SY_SERV_TABLE = "SY_SERV_TABLE";
    /** 服务主键：数据表字段 */
    public static final String SY_SERV_TABLE_COL = "SY_SERV_TABLE_COL";
    /** 服务主键：服务过滤规则 */
    public static final String SY_SERV_WHERE = "SY_SERV_WHERE";
    /** 服务主键：测试分发 */
    public static final String SY_TEST_SEND = "SY_TEST_SEND";
    /** 服务主键：自定义变量 */
    public static final String SY_WFE_CUSTOM_VAR = "SY_WFE_CUSTOM_VAR";
    /** 服务主键：流经表 */
    public static final String SY_WFE_FLOW = "SY_WFE_FLOW";
    /** 服务主键：节点连线定义表 */
    public static final String SY_WFE_LINE_DEF = "SY_WFE_LINE_DEF";
    /** 服务主键：流程节点和ACT关联表 */
    public static final String SY_WFE_NODE_ACT = "SY_WFE_NODE_ACT";
    /** 服务主键：节点定义表 */
    public static final String SY_WFE_NODE_DEF = "SY_WFE_NODE_DEF";
    
    /** 流程公共按钮服务 **/
    public static final String SY_WFE_NODE_PACTS = "SY_WFE_NODE_PACTS";
    
    /** 服务主键：子流程节点定义表 */
    public static final String SY_WFE_PROC_NODE_DEF = "SY_WFE_PROC_NODE_DEF";
    /** 服务主键：节点实例表 */
    public static final String SY_WFE_NODE_INST = "SY_WFE_NODE_INST";
    /** 服务主键：节点实例历史表 */
    public static final String SY_WFE_NODE_INST_HIS = "SY_WFE_NODE_INST_HIS";
    /** 服务主键：节点实例用户表 */
    public static final String SY_WFE_NODE_USERS = "SY_WFE_NODE_USERS";
    /** 服务主键：节点实例用户历史表 */
    public static final String SY_WFE_NODE_USERS_HIS = "SY_WFE_NODE_USERS_HIS";
    /** 服务主键：流程管理 */
    public static final String SY_WFE_PROC_DEF = "SY_WFE_PROC_DEF";
    /** 服务主键：流程公用按钮 */
    public static final String SY_WFE_PROC_DEF_ACT = "SY_WFE_PROC_DEF_ACT";
    /** 服务主键：流程版本 */
    public static final String SY_WFE_PROC_DEF_HIS = "SY_WFE_PROC_DEF_HIS";
    /** 服务主键：流程管理 */
    public static final String SY_WFE_PROC_DEF_PUBLIC = "SY_WFE_PROC_DEF_PUBLIC";
    /** 服务主键：流程实例表 */
    public static final String SY_WFE_PROC_INST = "SY_WFE_PROC_INST";
    /** 服务主键：流程实例历史表 */
    public static final String SY_WFE_PROC_INST_HIS = "SY_WFE_PROC_INST_HIS";
    /** 服务主键：催办单 */
    public static final String SY_WFE_REMIND = "SY_WFE_REMIND";
    /** 服务主键：催办单进展情况 */
    public static final String SY_WFE_REMIND_PROC = "SY_WFE_REMIND_PROC";
    /** 服务主键：催办单查询 */
    public static final String SY_WFE_REMIND_SEARCH = "SY_WFE_REMIND_SEARCH";
    /** 服务主键：测试 */
    public static final String SY_WFE_TEST = "SY_WFE_TEST";
    /** 服务主键：流程跟踪 */
    public static final String SY_WFE_TRACK = "SY_WFE_TRACK";
    /** 服务主键：图形化流程跟踪 */
    public static final String SY_WFE_TRACK_FIGURE = "SY_WFE_TRACK_FIGURE";
   /** 服务主键：群组方案明细用户 */
    public static final String BN_TM_TASK_ITEM_USER = "BN_TM_TASK_ITEM_USER";
	/** 服务主键：群组方案明细角色 */
    public static final String BN_TM_TASK_ITEM_ROLE = "BN_TM_TASK_ITEM_ROLE";
	/** 服务主键：群组方案明细部门 */
    public static final String BN_TM_TASK_ITEM_DEPT = "BN_TM_TASK_ITEM_DEPT";
	/** 服务主键：群组方案明细 */
    public static final String TASK_COMMON_GROUPS_ITEM = "TASK_COMMON_GROUPS_ITEM";
	/** 服务主键：任务常用群组方案选择*/
    public static final String TASK_COMMON_GROUPS_SELECT = "TASK_COMMON_GROUPS_SELECT";
	/** 服务主键：任务常用群组 */
    public static final String BN_TM_TASK_COMMON_GROUPS = "BN_TM_TASK_COMMON_GROUPS";
    
    /** 操作：服务定义 */
    public static final String ACT_SERV = "serv";
    /** 操作：查询 */
    public static final String ACT_QUERY = "query";
    /** 操作：基于主键获取数据 */
    public static final String ACT_BYID = "byid";
    /** 操作：删除 */
    public static final String ACT_DELETE = "delete";
    /** 操作：添加 */
    public static final String ACT_ADD = "add";
    /** 操作：保存（更新保存或者插入保存） */
    public static final String ACT_SAVE = "save";
    /** 操作：批量保存（同时支持删除、插入和更新） */
    public static final String ACT_BATCHSAVE = "batchSave";
    /** 操作：根据参数查询数据，不分页 */
    public static final String ACT_FINDS = "finds";
    /** 操作：根据参数查询数量 */
    public static final String ACT_COUNT = "count";
    
    
    
    
    
    /** log */
    private static Log log = LogFactory.getLog(ServMgr.class);
   
    /**
     * 服务执行入口方法，内部提供事务以及记录操作历史的支持。
     * @param paramBean 请求参数封装实体
     * @return 执行结果
     */
    public static OutBean act(ParamBean paramBean) {
        return act(paramBean.getServId(), paramBean.getAct(), paramBean);
    }

    /**
     * 服务执行入口方法，内部提供事务以及记录操作历史的支持。
     * @deprecated 建议使用act(ParamBean param)做参数的方法
     * @param servId 服务服务名
     * @param act 执行方法名
     * @param param 请求参数封装实体
     * @return 执行结果
     */
    public static Bean act(String servId, String act, Bean param) {
        return act(servId, act, new ParamBean(param));
    }
    
    /**
     * 服务执行入口方法，内部提供事务以及记录操作历史的支持。
     * @param servId 服务服务名
     * @param act 执行方法名
     * @param paramBean 请求参数封装实体
     * @return 执行结果
     */
    public static OutBean act(String servId, String act, ParamBean paramBean) {
    	OutBean result = null;
    	ServDefBean servDef = ServUtils.getServDef(servId);
        if (paramBean.contains("_CLIENT_REQ_")) { //如果是客户端请求才需要判断权限
            paramBean.remove("_CLIENT_REQ_"); //移除客户端访问标记，避免影响其他操作
            if (!OrgMgr.checkServAuth(servDef)) {
                throw new TipException(Context.getSyMsg("SY_SERV_AUTH_ERROR", servDef.getName(), servId)
                		+ "，登录用户：" + (Context.getUserBean() == null ? null : Context.getUserBean().getLoginName())
                		+ "，访问节点：" + System.getProperty("servName"));
            }
        }
    	long beginTime = System.currentTimeMillis();
    	boolean bTrans = paramBean.getTransFlag(); //是否启用事务处理
    	String ds = servDef.getDataSource();
    	if (!bTrans && ds.length() > 0) { //如果没启用事务且使用自定义数据源，则强制启用事务
    		bTrans = true;
    	}
        // 事务处理
        if (bTrans) {
        	Transaction.begin(ds);
        	paramBean.setTransFlag(false); //清除参数的事务标志，确保不被误传递
        }
        Object servClass = servDef.getServClass();
        try {
            paramBean.set(Constant.PARAM_SERV_ID, servId);
            paramBean.set(Constant.PARAM_ACT_CODE, act);
            //服务监听before
            ServLisMgr.getInstance().before(servId, act, paramBean);
            Object rtn = doMethod(servClass, act, paramBean);
            if (rtn == null) {
                result = new OutBean().setError(Context.getSyMsg("SY_RETURN_NULL"));
            } else if (rtn instanceof OutBean) {
                result = (OutBean) rtn;
            } else {
                result = new OutBean((Bean) rtn);
            }
            //服务监听after
            ServLisMgr.getInstance().after(servId, act, paramBean, result);

            if (bTrans) {
            	Transaction.commit();
            }
            long time = System.currentTimeMillis() - beginTime;
            result.set(Constant.RTN_TIME, ((float) (time + 1) / 1000)); //增加1毫秒的servlet处理
            ServUtils.actLog(servDef, act, paramBean.getId(), time); //记录操作历史及时效
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("SERV act error:" + servId + "." + act);
            }
            if (e.getCause() instanceof TipException) {
                throw (TipException) (e.getCause());
            } else if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException) e.getCause();
            } else {
                throw new RuntimeException(Context.getSyMsg("SY_RUN_ERROR", 
                        servClass.getClass().getName() + "." + act), e);
            }
        } finally {
            if (bTrans) {
            	Transaction.end();
            }
        }
        return result;
    }
    
    /**
     * 提供给JSP或者前端获取服务定义信息
     * @param servId 服务编码
     * @return 提供给前端的服务定义信息
     */
    public static OutBean servDef(String servId) {
        try {
            ServDefBean servDef = ServUtils.getServDef(servId);
            Object servClass = servDef.getServClass();
            return (OutBean) doMethod(servClass, "serv", new ParamBean(servId));
        } catch (Exception e) {
            String msg = e.getMessage();
            log.error(msg, e);
            return new OutBean().setError(msg);
        }
    }
    
    /**
     * 监听类执行入口
     * @param obj 监听类实例
     * @param methodName 方法名
     * @param paramBean 请求参数封装实体
     * @return 执行结果
     * @throws IllegalAccessException IllegalAccessException
     * @throws InvocationTargetException InvocationTargetException
     */
    private static Object doMethod(Object obj, String methodName, ParamBean paramBean) 
            throws IllegalAccessException, InvocationTargetException {
    	Object result = null;
        Method method = null;
        Object[] params = null;
        Class<?> cls = obj.getClass();
        try {
            method = cls.getMethod(methodName, Bean.class);
            params = new Object[] {paramBean};
        } catch (Exception e) {
            try {
                method = cls.getMethod(methodName, ParamBean.class);
                params = new Object[] {paramBean};
            } catch (Exception e2) {
                try {
                    method = cls.getMethod(methodName);
                    params = new Object[] { };
                } catch (Exception e3) {
                    method = null;
                }
            }
        }
        if (method != null) {
            result = method.invoke(obj, params);
        } else {
            final String msg = Context.getSyMsg("SY_RUN_ERROR", obj.getClass().getName() + "." + methodName
                    + paramBean.toString() + ";指定方法不存在:" + methodName);
            OutBean out = new OutBean();
            out.setError(msg);
            if (log.isDebugEnabled()) {
                log.debug(msg);
            }
            return out;
        }
        return result;
    }
}