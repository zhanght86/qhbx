package com.aeon.bi.bean;

import java.net.URLEncoder;

import com.aeon.bi.util.Const;
import com.aeon.bi.util.Util;

public class BiAppTreeView {
	
	private long id = -1;
	
	private long parentId = -1;
	
	private String name = null;
	
	private String url = null;
	
	private int seqId = 0;
	
	private int type = 0;
	
	private User user = null;
	
	private CognosUser cognosUser = null;
	
	public long getId() {
		return id;
	}
	public void setId(long value) {
		id = value;
	}
	
	public long getParentId() {
		return parentId;
	}
	public void setParentId(long value) {
		parentId = value;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String value) {
		name = value;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String value) {
		url = value;
	}
	
	public int getSeqId() {
		return seqId;
	}
	public void setSeqId(int value) {
		seqId = value;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int value) {
		type = value;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User value) {
		user = value;
	}
	
	public CognosUser getCognosUser() {
		return cognosUser;
	}
	public void setCognosUser(CognosUser value) {
		cognosUser = value;
	}
	
	public String getAppUrl() {
		if (Util.isNull(url)) {
			return null;
		}
		if (type == Const.TYPE_APP_SETTING) {
			if (user != null &&
				!Util.isNull(user.getUserId()) && !Util.isNull(user.getPassword())) {
				try {
					return String.format(Const.FORMAT_SETTING_EXT_URL,
							user.getUserId(), user.getPassword(),
							URLEncoder.encode(url, "UTF-8"));
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		} else if (type == Const.TYPE_APP_NORMAL) {
			return url;
		} else if (type == Const.TYPE_APP_COGNOS) {
			if (cognosUser != null &&
				!Util.isNull(cognosUser.getCUserId()) && !Util.isNull(cognosUser.getCPassword())) {
				String yesterdayStr = Util.getYesterdayStr("yyyy-MM-dd");
				return url + String.format(Const.FORMAT_COGNOS_EXT_URL,
						cognosUser.getCUserId(), cognosUser.getCPassword(),
						yesterdayStr, yesterdayStr, yesterdayStr,
						Util.getYesterdayStr("yyyyMM"), Util.getYesterdayStr("yyyyMM"), Util.getYesterdayStr("yyyyMM"));
			}
		}
		return null;
	}
}