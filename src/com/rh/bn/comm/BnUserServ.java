package com.rh.bn.comm;

import java.io.InputStream;

import jxl.Workbook;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.bn.imp.BnUserImp;
import com.rh.core.base.Bean;
import com.rh.core.comm.FileMgr;
import com.rh.core.org.serv.UserServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;

/**
 * 百年人寿项目用户管理处理类，继承UserServ
 * 主要是增加了excel导入用户列表方法
 * @author tanyh 20161107
 *
 */
public class BnUserServ extends UserServ{
	
	/**
     * 记录日志信息的函数
     */
    protected Log log = LogFactory.getLog(this.getClass());

	/**
	 * excel批量导入用户
	 * @param paramBean 参数对象
	 * @return outBean 返回结果对象
	 */
	public OutBean excelImp(ParamBean paramBean) {
		OutBean outBean = new OutBean();
		String fileId = paramBean.getStr("fileId");
		Bean fileBean = FileMgr.getFile(fileId);
		if (fileBean != null
				&& fileBean.getStr("FILE_MTYPE").equals(
						"application/vnd.ms-excel")) { // 只支持excel类型
			Workbook book = null;
			InputStream in = null;
			try {
				// 获取上传的excle文件
				in = FileMgr.download(fileBean);
				// 打开文件
				try {
					book = Workbook.getWorkbook(in);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					throw new RuntimeException(
							"Wrong file format, only suport 2003 and lower version,"
									+ "pls use export excel file as the template!");
				}
				// 处理导入的excel文件数据
				BnUserImp abImp = new BnUserImp();
				// 返回导入结果
				outBean.setOk(abImp.impData(book.getSheet(0)));
				// 关闭文件
				book.close();
				book = null;
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			} finally {
				if (book != null) {
					book.close();
				}
				IOUtils.closeQuietly(in);
			}
		} else { // 错误的文件内容或格式
			outBean.setWarn("");
		}
		FileMgr.deleteFile(fileBean); // 最后删除临时上传的文件
		return outBean;
	}
}
