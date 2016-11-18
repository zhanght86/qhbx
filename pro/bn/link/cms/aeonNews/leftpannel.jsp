<%@ page language="java" contentType="text/html; charset=GBK"%>
<div id="lefttarea">  
	<div id="newscenter">
		<div id="newscentertop"></div>
		<div id="newscenterbody">
			<div   onmouseover="doOpenFloder(this);" id="side1" class="side" style="height:35px;">
				<span class="sidemouseover">新闻中心</span>
				<ul>
					<li style="font-size:12px;line-height:24px;padding-left:25px;"><a href="/bn/link/cms/aeonNews/news/aeonnews/">百年新闻</a></li>
					<li style="font-size:12px;line-height:24px;padding-left:25px;"><a href="/bn/link/cms/aeonNews/news/report/">百年公告</a></li>
					<li style="font-size:12px;line-height:24px;padding-left:25px;"><a href="/bn/link/cms/aeonNews/news/mediaFocus/">媒体关注</a></li>
					<li style="font-size:12px;line-height:24px;padding-left:25px;"><a href="/bn/link/cms/aeonNews/news/activity/">活动报道</a></li>
					<li style="font-size:12px;line-height:24px;padding-left:25px;"><a href="/bn/link/cms/aeonNews/news/subCompany/">分公司动态</a></li>
				</ul>
			</div>
			<%--<div  onmouseover="" id="side2" class="side" style="height:35px;">
				<span class="sidemouseout"><a href="/bn/link/cms/aeonNews/intertek/">百年内刊</a></span>
			</div>--%>
			<div onmouseover="doOpenFloder(this);" id="side3" class="sideBottom" style="height:35px;">
				<span class="sidemouseover" >反洗钱宣传专栏</span>
				<ul>
					<li style="font-size:12px;line-height:24px;padding-left:25px;"><a href="/bn/link/cms/aeonNews/flack/law/">反洗钱法律法规</a></li>
					<li style="font-size:12px;line-height:24px;padding-left:25px;"><a href="/bn/link/cms/aeonNews/flack/lejitinate/">反洗钱宣传月活动</a></li>
				</ul>
			</div>
<%--			<div  onmouseover="doOpenFloder(this);" id="side4" class="sideBottom" style="height:35px;">--%>
<%--				 <span class="sidemouseover">保险知识库</span>--%>
<%--				<ul>--%>
<%--					<li style="font-size:12px;line-height:24px;padding-left:25px;"><a href="/bn/link/cms/aeonNews/insurance/insuranceNews/">寿险资讯</a></li>--%>
<%--					<li style="font-size:12px;line-height:24px;padding-left:25px;"><a  href="/bn/link/cms/aeonNews/insurance/investmentNews/">保险资讯</a></li>--%>
<%--					<li style="font-size:12px;line-height:24px;padding-left:25px;"><a  href="/bn/link/cms/aeonNews/insurance/lifeinsuranceNews/">投资资讯</a></li>--%>
<%--				</ul>--%>
<%--			</div>--%>
			<div id="sidehidden" style="display:none;"></div>
		</div>
		<div id="newscenterbottom"></div>
	</div>
</div>
<script type="text/javascript" src="/bn/link/global/js/leftPannel.js"></script>