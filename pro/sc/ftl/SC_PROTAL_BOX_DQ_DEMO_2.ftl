<style>
.clearfix:before,.clearfix:after {content:"";display:table;}
.clearfix:after {clear:both;overflow:hidden;}
.clearfix {zoom:1; /* IE < 8 */}
.dq-header{background-color:#fff;color:#ca1600;font:12px/33px '微软雅黑'}
.dq-tab-wrapper,.dq-tab-item,.dq-tab-item a{background-image:url('/sc/img/dq_bg.png');background-repeat:no-repeat}
.dq-tab-wrapper{background-position:0 -326px}
.dq-tab-wrapper .tabControl{height:33px;padding-left:40px}
.dq-tab-item{list-style:none;float:left;margin-top:5px;margin-right:5px;padding-right:10px;background-position:right -795px;line-height:28px}
.dq-tab-item a{display:block;margin-left:-3px;padding-left:10px;background-position:0 -755px;color:#724000;text-decoration:none}
.dq-tab-item.current{background-position:right -904px}
.dq-tab-item.current a{background-position:0 -864px;color:#f00;text-decoration:none}
a.dq-more{display: block;position:absolute;right:10px;bottom:10px;margin-right: 10px;color: #F00;line-height: 28px;text-decoration: none;}
a.dq-more:hover,a.dq-more:focus{text-decoration: none;}
</style>

<div class='portal-box portal-box-dq' style='height:276px'>
	<div class='dq-tab-wrapper'>
		<ul class='tabControl' id='tabControl'>
            <li class='dq-tab-item current'>
                <a href='#js-tab1' title='学习教育'> 学习教育</a>
            </li>
            <li class='dq-tab-item'>
                <a href='#js-tab2' title='我身边的先锋'> 我身边的先锋 </a>
            </li>
            <li class='dq-tab-item'>
                <a href='#js-tab3' title='党员承诺'> 党员承诺</a>
            </li>
        </ul>
	</div>	    
	<div id="js-tab1" class='portal-box-con-dq dq-tab-content'>
		<#if _DATA_?size !=0 >
			<ul>
			<#list _DATA_ as obj>
					<li>
						<div class="news-title">
	                    	<span class="icon"></span>
							<a href='/cms/SY_COMM_INFOS/${obj.NEWS_ID}.html' data-id="${obj.NEWS_ID}" target="_blank">${obj.NEWS_SUBJECT}</a>
	                    </div>
	                    <div class="news-date">${obj.S_ATIME}</div>
					</li>
			</#list>
			</ul>
			<a href="" class="dq-more">更多&gt;&gt;</a> 
		<#else>
			暂无内容!
		</#if>	
	</div>
	<div id="js-tab2" class='portal-box-con-dq dq-tab-content' style="display:none;">
		<ul>
			<li>
				<div class="news-title">
	                <span class="icon"></span>
					<a href='#'>选项卡2选项卡2选项卡2内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容</a>
				 </div>
	             <div class="news-date">2013-06-06</div>
	        </li>
	    </ul>
	</div>
	<div id="js-tab3" class='portal-box-con-dq dq-tab-content' style="display:none;">
		<ul>
			<li>
				<div class="news-title">
	                <span class="icon"></span>
					<a href='#'>选项卡2选项卡2选项卡2内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容</a>
				 </div>
	             <div class="news-date">2013-06-06</div>
	        </li>
	    </ul>
	</div>
</div>
<script>
$(document).ready(function(){
	$('.dq-tab-item').on('click',function(event){
			event.preventDefault();
			if($(this).hasClass('current')){
				return false;
			}
			var id=$(this).find('a').attr('href'),
			    hid=$(this).siblings('.current').find('a').attr('href');
			$(this).siblings('.current').removeClass('current');
			$(this).addClass('current');
			$(hid).hide();
			$(id).show();
	});
});
</script>