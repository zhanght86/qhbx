package com.rh.core.comm.xdoc;

import org.apache.commons.lang.StringUtils;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Constant;

/**
 * xdoc配置信息
 * @author ruaho_hdy
 *
 */
public class XdocConfig extends CommonServ{
	
	/**是否页面显示，页面显示标志为1表示页面显示*/
	private static final String IS_SHOW_PAGE = "1";
	
	/**是否通过XDOC访问，标志为1表示通过XDOC SERVICE访问*/
	private static final String IS_XDOC_SERVICE = "1";
	
	@Override
	protected void beforeSave(ParamBean paramBean) { //如果存在某个key值，则不可再次写入
		SqlBean sql  = new SqlBean();
		sql.and("XDOC_KEY", paramBean.getStr("XDOC_KEY")).and("S_ODEPT", Context.getUserBean().getODeptCode());
		if (ServDao.count(paramBean.getServId(), sql) > 0) {
			throw new TipException("此key已存在，请重新设置key值");
		}
		super.beforeSave(paramBean);
	}
	
	/**
	 * xdoc执行方法
	 * @param paramBean 参数集合
	 */
	public OutBean run(ParamBean paramBean){
		init(paramBean);
		//通过xdoc sevice访问
		if (isExiteXdocSer(paramBean.getStr("IS_XDOC_SERVICE"))) {
			//显示结果页面
			if (isExitePage(paramBean.getStr("IS_SHOW_PAGE"))) {
				throw new TipException("此处没有实现，请先使用文件流形式");
				/*OutBean out = new OutBean();
				String json = JsonUtils.toJson(paramBean);
				//DesUtils.encrypt(json);
				out.set("xdocParam", json);
				out.set(OutBean.TO_DIRE, "/sy/plug/xdoc/showXdoc.jsp");
				return out;*/
			//输出文件流
			} else {
				paramBean.set("USE_XDOC_SERVICE", true);
				new OutputXdoc().outputXdoc(paramBean);
			}
		//通过文件访问
		} else {
			//显示结果页面
			if (isExitePage(paramBean.getStr("IS_SHOW_PAGE"))) {
				throw new TipException("此处没有实现，请先使用文件流形式");
				/*OutBean out = new OutBean();
				String json = JsonUtils.toJson(paramBean);
				//DesUtils.encrypt(json);
				out.set("xdocParam", json);
				out.set(OutBean.TO_DIRE, "/sy/plug/xdoc/showXdoc.jsp");
				return out;*/
			} else {
				new OutputXdoc().outputXdoc(paramBean);
			}
		}
		return null;
	}
	
	/**
	 * 初始化
	 * @param paramBean 参数集合
	 */
	public void init (ParamBean paramBean) {
		Bean thisXdocConfig = null;
		if (paramBean.isNotEmpty("XDOC_SERVID")) {
			thisXdocConfig = getXdocConfig(paramBean.getStr("XDOC_KEY"), paramBean.getStr("XDOC_SERVID"));
		} else {
			thisXdocConfig = getXdocConfig(paramBean.getStr("XDOC_KEY"), paramBean.getServId());
		}
		String fullName = "";
		String fileName = "";
		if (paramBean.isNotEmpty("FULL_NAME")) {
			fullName = paramBean.getStr("FULL_NAME");
		}
		if (paramBean.isNotEmpty("FILE_NAME")) {
			fileName = paramBean.getStr("FILE_NAME");
		}
		paramBean.putAll(thisXdocConfig);
		paramBean.set("FULL_NAME", fullName);
		paramBean.set("FILE_NAME", fileName);
		paramBean.set("sql", suffSqlStr(thisXdocConfig, paramBean));
		paramBean.remove("SQL");
		overWriteParams(paramBean);
	}
	
	/**
	 * 获取初始化数据
	 * @param paramBean 参数Bean
	 * @return 初始化数据
	 */
	public OutBean initData (ParamBean paramBean) {
		init(paramBean);
		return new OutBean().set("data", paramBean);
	}
	
	/**
	 * 是否启用页面显示
	 * @param pageFlag 页面显示标志
	 * @return 启用标志
	 */
	private boolean isExitePage(String pageFlag) {
		return pageFlag.equals(IS_SHOW_PAGE);
	}
	
	/**
	 * 是否通过xdoc service访问
	 * @param serFlag 标识位
	 * @return 启用标志
	 */
	public boolean isExiteXdocSer(String serFlag) {
		return serFlag.equals(IS_XDOC_SERVICE);
	}
	
	/**
	 * 填充sql语句
	 * @param bean 配置数据对象
	 * @param paramBean 参数集合
	 * @return 填充之后的sql语句
	 */
	private String suffSqlStr(Bean bean, ParamBean paramBean) {
		//如果sql不为空，则返回填充之后的sql语句，否则返回空
		if (bean.isNotEmpty("SQL")) {
			String oldSql = bean.getStr("SQL");
			StringBuffer sql = new StringBuffer();
			String[] sqlParams = oldSql.split("#");
			//存在变量替换，则替换之后返回sql字符串
			int sqlLength = sqlParams.length;
			if (sqlLength > 0) {
				for (int i = 0; i < sqlLength; i++) {
					if (i % 2 == 0) {
						sql.append(sqlParams[i]);
					} else {
						String thisParamVal = paramBean.getStr(sqlParams[i].toUpperCase()).trim().replaceAll("'", "");
						if (null == thisParamVal || thisParamVal.equals("")) {
							throw new TipException(sqlParams[i].toUpperCase() + "此key不存在，请核对");
						}
						sql.append(isExiteINBranch(thisParamVal));
					}
				}
				return sql.toString();
			}
			//不存在则直接返回sql
			return oldSql;
		}
		return null;
	}
	
	/**
	 * 获取对应xdoc key值得数据对象
	 * @param xdocKey key值
	 * @param thisServId 服务id
	 * @return 数据集合
	 */
	private Bean getXdocConfig(String xdocKey, String thisServId){
		SqlBean sql = new SqlBean();
		sql.and("XDOC_KEY", xdocKey).and("S_CMPY", Context.getUserBean().getCmpyCode());
		return ServDao.find(thisServId, sql);
	}
	
	/**
	 * 重写参数。比如，文件名修改了，则将文件名写成参数传递过来的
	 * @param paramBean 参数集合
	 */
	public void overWriteParams(ParamBean paramBean){
		SqlBean sql = new SqlBean();
		sql.and("XDOC_KEY", paramBean.getStr("XDOC_KEY")).and("S_CMPY", Context.getUserBean().getCmpyCode());
		Bean thisObj = null;
		if (paramBean.isNotEmpty("XDOC_SERVID")) {
			thisObj = ServDao.find(paramBean.getStr("XDOC_SERVID"), sql);
		} else {
			thisObj = ServDao.find(paramBean.getServId(), sql);
		}
		resetParamKey(paramBean, thisObj);
		//paramBean.putAll(getJdbcBean());
		paramBean.set("jdbc_name", Context.getSyConf("SY_XDOC_JDBC_NAME", ""));
	}

	/**
	 * 重置参数key值
	 * @param paramBean 参数
	 * @param thisObj xdoc配置
	 */
	public void resetParamKey(ParamBean paramBean, Bean thisObj) {
		//显示名称
		if (paramBean.isNotEmpty("FULL_NAME")) {
			paramBean.set("fullName", paramBean.getStr("FULL_NAME"));
			paramBean.remove("FULL_NAME");
		} else {
			paramBean.set("fullName", thisObj.getStr("FULL_NAME"));
		}
		//文件名
		if (paramBean.isNotEmpty("FILE_NAME")) {
			paramBean.set("fileName", paramBean.getStr("FILE_NAME"));
			paramBean.remove("FILE_NAME");
		} else {
			paramBean.set("fileName", thisObj.getStr("FILE_NAME"));
		}
		//xdoc文件路径
		if (paramBean.isNotEmpty("FILE_PATH")) {
			paramBean.set("filePath", paramBean.getStr("FILE_PATH"));
			paramBean.remove("FILE_PATH");
		} else {
			paramBean.set("filePath", thisObj.getStr("FILE_PATH"));
		}
		//文件格式
		if (paramBean.isNotEmpty("FOMAT")) {
			paramBean.set("format", paramBean.getStr("FOMAT"));
			paramBean.remove("FOMAT");
		} else {
			paramBean.set("format", thisObj.getStr("FORMAT"));
			paramBean.remove("FORMAT");
		}
	}
	
	/**
	 * 获取jdbc配置
	 * @return jdbc配置值
	 */
	/*private Bean getJdbcBean(){
		return JsonUtils.toBean(Context.getSyConf("SY_XDOC_JDBC_CONF", ""));
	}*/
	
	/**
	 * 是否存是in后面的匹配字符，比如：'123','456','789';
	 * @param str 字符
	 * @return 去除原始字符单引号的字符串
	 */
	private String isExiteINBranch(String str){
		String[] newINBranchArr = str.split(Constant.SEPARATOR);
		if (newINBranchArr.length > 1) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < newINBranchArr.length; i++) {
				sb.append(",").append("'").append(newINBranchArr[i]).append("'");
			}
			return sb.toString().substring(1);
		}
		return str;
	}
	
	/**
	 * 是否存在数据
	 * @param sql 要查询的sql语句
	 * @return 存在返回true，不存在返回false
	 */
	public OutBean isExiteData(ParamBean paramBean){
		init(paramBean);
		String sql = paramBean.getStr("sql");
		OutBean out = new OutBean();
		if (StringUtils.isBlank(sql)) {
			return out.setError("查询语句不存在！");
		}
		int count = Context.getExecutor().count(sql);
		if (count <= 0) {
			return out.setError("统计数据为空，请重新选择统计条件！");
		}
		return out.setOk();
	}
}
