package com.aeon.bi.business;

import java.util.ArrayList;
import java.util.Map;

import com.aeon.bi.bean.BiAppTreeView;
import com.aeon.bi.bean.CognosUser;
import com.aeon.bi.db.BiDB;
import com.aeon.bi.util.Const;
import com.aeon.bi.util.Util;

public class Bi {
	public static long getTreeViewMap(Map<String, ArrayList<BiAppTreeView>> map, String userId) throws Exception {
		long rootId = Const.DEFAULT_APP_TREE_VIEW_ROOT_ID;
		
		BiDB biDB = new BiDB();
		ArrayList<BiAppTreeView> list = biDB.getBiAppTreeViewList(userId);
		biDB.close();
		
		if (list == null || list.size() <= 0) {
			return rootId;
		}
		
		for (int i = 0; i < list.size(); i++) {
			BiAppTreeView node = list.get(i);
			if (node.getId() == node.getParentId()) {
				rootId = node.getId();
			}
			ArrayList<BiAppTreeView> array = map.get(String.valueOf(node.getParentId()));
			if (array == null) {
				array = new ArrayList<BiAppTreeView>();
				array.add(node);
				map.put(String.valueOf(node.getParentId()), array);
			} else {
				array.add(node);
			}
		}
		return rootId;
	}
	
	public static BiAppTreeView getTreeView(String userId, String appId) throws Exception {
		BiDB biDB = new BiDB();
		BiAppTreeView treeView = biDB.getBiAppTreeView(userId, appId);
		biDB.close();
		
		if (treeView == null) {
			return null;
		}
		
		return treeView;
	}
	
	public static String getAppUrl(String userId, String appId) throws Exception {
		BiDB biDB = new BiDB();
		BiAppTreeView treeView = biDB.getBiAppTreeView(userId, appId);
		biDB.close();
		
		if (treeView == null) {
			return null;
		}
		
		return treeView.getAppUrl();
	}
	
	public static String getCognosExtUrl(String userId) throws Exception {
		BiDB biDB = new BiDB();
		CognosUser cognosUser = biDB.getCognosUser(userId);
		biDB.close();
		
		if (cognosUser == null ||
				Util.isNull(cognosUser.getCUserId()) || Util.isNull(cognosUser.getCPassword())) {
			return null;
		}
		
		return String.format(Const.FORMAT_COGNOS_EXT_URL,
				cognosUser.getCUserId(), cognosUser.getCPassword(), Util.getYesterdayStr("yyyy-MM-dd"));
	}
	
	public static void savePortalUserTracker(BiAppTreeView treeView) throws Exception {
		BiDB biDB = new BiDB();
		biDB.saveBiPortalUserTracker(treeView);
		biDB.close();
	}
}