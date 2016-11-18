package com.rh.core.comm.poll;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.org.UserBean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.util.RequestUtils;

/**
 * @author liwei
 * 
 */
public class PollServ extends CommonServ {

    /** poll service id */
    private static final String POLL_SERV = "SY_COMM_POLL";

    /** option service id */
    private static final String OPTION_SERV = "SY_COMM_POLL_OPTION";

    /** vote service id */
    private static final String VOTE_SERV = "SY_COMM_POLL_VOTE";

    /**
     * 获取投票信息
     * @param paramBean - param bean
     * @return out bean
     */
    public OutBean show(ParamBean paramBean) {
        OutBean poll = new OutBean(ServDao.find(POLL_SERV, paramBean));
        boolean voted = isVoted(poll);
        Bean optParam = new Bean().set("POLL_ID", poll.getId());
        List<Bean> opts = ServDao.finds(OPTION_SERV, optParam);
        poll.set("OPTIONS", opts);

        // 是否显示结果
        int showResult = 2;
        int resultStatus = poll.get("POLL_SHOW_RESULTS", 1);
        if (1 == resultStatus) {
            // 投票后显示
            if (voted) {
                showResult = 1;
            }
        } else if (2 == resultStatus) {
            // 始终显示
            showResult = 1;

        } else if (3 == resultStatus) {
            // 始终不显示
            showResult = 2;
        }
        
        poll.set("SHOW_RESULT", showResult);
        // 是否已投票
        if (voted) {
            poll.set("VOTED", 1);
        } else {
            poll.set("VOTED", 2);
        }
        
        return poll;
    }

    /**
     * 投票
     * @param paramBean - param bean
     * @return out bean
     */
    public OutBean vote(ParamBean paramBean) {
        String pollId = paramBean.getStr("POLL_ID");
        String optionsId = paramBean.getStr("OPTION_ID");
        String[] optArray = optionsId.split(",");
        for (String opt : optArray) {
            if (0 == opt.trim().length()) {
                continue;
            }
            // TODO update OPTION_COUNTER=OPTION_COUNTER+1
            Bean optBean = new Bean().setId(opt);
            Bean oldBean = ServDao.find(OPTION_SERV, optBean);
            optBean.set("OPTION_COUNTER", oldBean.get("OPTION_COUNTER", 0) + 1);
            ServDao.update(OPTION_SERV, optBean);
        }

        Bean vote = new Bean();
        vote.set("POLL_ID", pollId);
        vote.set("VOTE_IP", RequestUtils.getIpAddr(Context.getRequest()));
        if (null != Context.getUserBean() && null != Context.getUserBean().getId()) {
            vote.set("VOTE_USER", Context.getUserBean().getCode());
        }
        ServDao.save(VOTE_SERV, vote);

        OutBean outBean = new OutBean();
        outBean.setOk("投票成功");
        return outBean;
    }

    /**
     * 当前访问者是否已进行投票
     * @param poll - poll bean
     * @return - this user voted?
     */
    private boolean isVoted(Bean poll) {
        String ip = RequestUtils.getIpAddr(Context.getRequest());
        UserBean user = Context.getUserBean();
        boolean result = false;

        int anonymous = poll.get("POLL_ANONYMOUS", 1);
        if (1 == anonymous) {
            // 验证IP
            result = ipVoted(poll, ip);
            if (!result) {
                return result;
            }

            // 验证用户
            result = userVoted(poll, user);
            return result;
        } else {
            // 验证用户
            result = userVoted(poll, user);
            return result;
        }
    }

    /**
     * vate ip check, if this ip voted we return true
     * @param poll - poll bean
     * @param user - user bean
     * @return this user voted?
     */
    private boolean userVoted(Bean poll, UserBean user) {
        Bean vote = new Bean();
        vote.set("POLL_ID", poll.getId());
        vote.set("VOTE_USER", user.getCode());
        List<Bean> list = ServDao.finds(VOTE_SERV, vote);
        if (0 == list.size()) {
            return false;
        }
        return true;
    }

    /**
     * vate ip check, if this ip voted we return true
     * @param poll - poll bean
     * @param ip - ip addr
     * @return this ip voted?
     */
    private boolean ipVoted(Bean poll, String ip) {
        Bean vote = new Bean();
        vote.set("POLL_ID", poll.getId());
        vote.set("VOTE_IP", ip);
        List<Bean> list = ServDao.finds(VOTE_SERV, vote);
        if (0 == list.size()) {
            return false;
        }
        return true;
    }

}

