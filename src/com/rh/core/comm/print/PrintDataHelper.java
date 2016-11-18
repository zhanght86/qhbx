package com.rh.core.comm.print;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.comm.mind.UserMind;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.dict.DictMgr;
import com.rh.core.util.Constant;
import com.rh.core.util.RequestUtils;

/**
 * 
 * @author zhanghailong
 * 
 */
public class PrintDataHelper extends CommonServ {
    /** 服务主键：打印模板 */
    public static final String SY_COMM_PRINT_TMPL = "SY_COMM_PRINT_TMPL";
	
	
	@SuppressWarnings("unused")
	private String ftlName = "printMind.ftl";

	/**
	 * 设置信息模版
	 * @param ftl  模版
	 */
	public void setMindPrintFtl(String ftl) {
		this.ftlName = ftl;
	}

	/**
	 * 获取公共数据
	 * @param paramBean 入参
	 * @return 需要打印的数据
	 */
	@SuppressWarnings("rawtypes")
    public static HashMap getCommData(Bean paramBean) {
		/**
		 * 这四个参数必须确保都传递了，代码不做检查，默认认为都传了
		 */
		String servId = paramBean.getStr("servId"); // 服务ID
		String servSrcId = paramBean.getStr("servSrcId");
		String dataId = paramBean.getStr("dataId"); // 数据ID
		// 审签单基础数据数据
		ParamBean queryBean = new ParamBean().set(Constant.PARAM_SERV_ID, servId).set(Constant.KEY_ID, dataId);
		OutBean outBean = ServMgr.act(servId, "getPrintData", queryBean);
		FileDataHelper fdh = new FileDataHelper(servSrcId, dataId);
		outBean.put("_ZHENGWEN_", fdh.getFileNames("ZHENGWEN"));
		outBean.put("_FUJIAN_", fdh.getFileNames("FUJIAN"));
		outBean.put("_ZHUANFA_", fdh.getFileNames("ZHUANFA"));
        outBean.put("_fdh_", fdh);
		//获取相关文件名
		outBean.put("_RELATEFILE_", fdh.getRelateFileNames(dataId, servSrcId));
		/*
		 * 取得意见列表中的数据 按意见编码分类，并拼接显示样式
		 */
		UserMind userMind = new UserMind(Context.getUserBean());
		userMind.query(servId, dataId);
		outBean.put("_userMind_", userMind);
		return outBean;
	}
	
	/**
	 * 后台替换特殊字符 替换包含Html中<> <——&lt;    >——&gt;   "——&quot; 逐个替换
	 * @param ftlcontent 模板内容
	 * @return 替换后的模板内容
	 */
   public String replaceRegex(String ftlcontent) {
	    ftlcontent = ftlcontent.replaceAll("&quot;", "\"");
	    ftlcontent = ftlcontent.replaceAll("&lt;", "<");
	    ftlcontent = ftlcontent.replaceAll("&gt;", ">");
	    return ftlcontent;
   }
	/**
	 * 转换部分html成word并以返回文件流下载
	 * @param param  部分html内容
	 */
	public void writeWordFile(ParamBean param) {
		HttpServletResponse res = Context.getResponse();
		ByteArrayInputStream bais = null;
		try {
			String content = "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head>"
					+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>"
					+ "<title>通用打印</title></head><body><div style=\"text-align: center\">" + param.getStr("PT_CONTENT")
					+ "</div></body></html>";
			byte[] b = content.getBytes("UTF-8");
			bais = new ByteArrayInputStream(b);
			POIFSFileSystem poifs = new POIFSFileSystem();
			DirectoryEntry directory = poifs.getRoot();
			directory.createDocument("WordDocument", bais);
			HttpServletRequest req = Context.getRequest();
			res.resetBuffer();
			res.setContentType("application/msword; charset=UTF-8");
			RequestUtils.setDownFileName(req, res, "打印稿纸.doc");
			poifs.writeFilesystem(res.getOutputStream());
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				bais.close();
				res.flushBuffer();
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}
	
	/**
	 * 
	 * @param servId 服务ID
	 * @return 该服务是否存在打印模板
	 */
	public static boolean existPrintTmpl(String servId) {
		List<Bean> dictItems = DictMgr.getItemList(SY_COMM_PRINT_TMPL);
		for (Bean item: dictItems) {
			if (item.getStr("ID").equals(servId)) { //配置的字典上有这个服务ID
				return true;
			}
		}
		
		return false;
	}
}
