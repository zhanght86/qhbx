<div class="mod rg">
    <b class="top">
        <b class="tl"></b>
        <b class="tr"></b>
    </b>
    <div class="inner">
        <div class="bd">
            <div class="box top-search-box ">
                <div class="content">
                    <form action="/SY_PLUG_SEARCH.query.do" name="ftop" id="searchForm" method="get">
                        <span class="s_ipt_wr">
                            <input type="text" id="kw" name="word" class="s_ipt" maxlength="256" tabindex="1" autocomplete="off">
                        </span>
                        <span class="s_btn_wr">
                            <input type="submit" id="bsb" value="搜索文档" onclick="javascript:search(this);" class="s_btn"
	                            onmouseover="this.className='s_btn s_btn_h'" 
	                            onmousedown="this.className='s_btn s_btn_d'"
	                            onmouseout="this.className='s_btn'" />
                        </span>
                        <div class="g-sl">
                            <input type="radio" name="lm" value="0" id="bot_all" checked="checked">
                            <label for="bot_all"> 全部</label>
                           
                            <input type="radio" name="lm" value="1" id="bot_doc">
                            <label for="bot_doc">DOC</label>
                            
                            <input type="radio" name="lm" value="3" id="bot_ppt">
                            <label for="bot_ppt">PPT</label>
                            
                            <input type="radio" name="lm" value="5" id="bot_txt">
                            <label for="bot_txt">TXT</label>
                            
                            <input type="radio" name="lm" value="2" id="bot_pdf">
                            <label for="bot_pdf">PDF</label>
                            
                            <input type="radio" name="lm" value="4" id="bot_xls">
                            <label for="bot_xls">XLS</label>
                           
                            <div style="clear: both"></div>
                        </div>
                        <input type="hidden" name="od" value="0">
                        <input type="hidden" name="fr" value="top_home">
                        <input type="hidden" name="data" id="data" value="" >
                    </form>
                </div>
            </div>
        </div>
    </div>
    <b class="bottom">
        <b class="bl"></b>
        <b class="br"></b>
    </b>
</div>