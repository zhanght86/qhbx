
<div id="menu">
	<ul>
		<li class="first">
			<a log="action:menuclick,index:0" href="<@tmplUrl index_tmpl_id />">
				知道首页
			</a>
		</li>
		<li id="category">
			<a log="action:menuclick,index:1" name="zhidaoNavigation" href="<@chnlUrl root_channel_id 1 />">
				问题分类
			</a>
		</li>
		<li id="specialist">
			<a log="action:menuclick,index:1" name="zhidaoNavigation" href="<@tmplUrl specialist_tmpl_id /> ">
				 知道专家
			</a>
		</li>
		<li id="recommend">
			<a log="action:menuclick,index:1" name="zhidaoNavigation" href="<@tmplUrl zhidao_recommend_tmpl_id />">
				 知道阅读
			</a>
		</li>
		<li id="myfollow">
			<a log="action:menuclick,index:1" name="zhidaoNavigation" href="<@tmplUrl myfollow_tmpl_id /> ">
				 我的关注
			</a>
		</li>
		<!--
		<li id="hotzhidao">
			<a log="action:menuclick,index:1" rel="nofollow" value="">
				<label> 精彩知识</label>
			</a>
		</li>
		-->
	</ul>
	<a rel="nofollow" href="javascript:myZhiDaoCenter('${userZhidao_center_tmpl_id}');" target="_self"
	class="ucenter">
	</a>
</div>