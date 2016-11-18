<%
	Map<String, ArrayList<BiAppTreeView>> map = new HashMap<String, ArrayList<BiAppTreeView>>();
	long rootId = Const.DEFAULT_APP_TREE_VIEW_ROOT_ID;
	try {
		rootId = Bi.getTreeViewMap(map, userBean.getLoginName());
	} catch (Exception e) {
		e.printStackTrace();
		map = null;
		rootId = Const.DEFAULT_APP_TREE_VIEW_ROOT_ID;
	}
%>

<script type="text/javascript">
jQuery(document).ready(function() {
	if (<%= map == null %>) {
		alert('<%= Util.getMessage("error_db_get_data_failed") %>');
		return;
	}
	if (<%= map.size() == 0 %>) {
		alert('<%= Util.getMessage("error_no_useful_data") %>');
		return;
	}
});
</script>

<div class="lfr-tree" id="biTreeView">
			<% if(map != null && map.containsKey(String.valueOf(rootId))){ %>
					<ul class="has-children node-open" style="white-space:nowrap;margin-left:-42px;margin-top:0">
					<%
						request.setAttribute("parentId", String.valueOf(rootId));
						request.setAttribute("map", map);
						pageContext.include("treeNode.jsp");
					%>
					</ul>
			<%} %>
</div>
<br/>

<script type="text/javascript">
	var bilayoutIcons = {
		root: '/bi/images/root.png',
		spacer: '/bi/images/spacer.png',
		plus: '/bi/images/plus.png',
		minus: '/bi/images/minus.png',
		page: '/bi/images/page.png'
	};
	
	jQuery(
		function() {
			BiTree.initialize(
				{
					outputId: '#biTreeView',
					icons: bilayoutIcons
				}
			);
		}
	);
</script>