/*
 * Copyright (c) 2012 Ruaho All rights reserved.
 */
package com.rh.core.plug.search;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;

/**
 * ruaho index message bean
 * 
 * @author liwei
 * 
 */
public class RhIndex extends Bean {
	/** log */
	private static Log log = LogFactory.getLog(RhIndex.class);

	/**
	 * message action
	 */
	public enum ACT {
		/** add index */
		ADD,
		/** delete index */
		DELETE,
		/** update index */
		UPDATE,
	}

	/**
	 * relative type
	 */
	public enum RELATIVE_TYPE {
		/** data from search server */
		SY_PLUG_SEARCH_SERV,
		/** data from service */
		SERVICE,
	}

	/**
	 * version id
	 */
	private static final long serialVersionUID = 1339187838437028058L;

	/** index id key */
	public static final String INDEX_ID = "id";

	/** display title key */
	public static final String DISPLAY_TITLE = "title";

	/** content key */
	public static final String CONTENT = "content";

	/** keywords */
	public static final String KEYWORDS = "keywords";

	/** abstract key */
	public static final String DISPLAY_ABSTRACT = "abstract";

	/** preview key */
	public static final String DISPLAY_PREVIEW = "preview";

	/** owner key */
	public static final String OWNER = "owner";

	/** company key */
	public static final String COMPANY = "company";

	/** department key */
	public static final String DEPARTMENT = "department";

	/** service key */
	public static final String SERVICE = "service";

	/** create time key */
	public static final String CREATE_TIME = "create_time";

	/** last modified time key */
	public static final String LAST_MODIFIED = "last_modified";

	/** grantee key */
	public static final String GRANTEE = "grantee";

	/** attachment key */
	public static final String ATTACHMENT = "attachment";

	/** relative data */
	public static final String RELATIVE = "relative";

	/** attachment title key */
	public static final String ATTACHMENT_TITLE = "attachment_title";

	/** attachment id key */
	public static final String ATTACHMENT_ID = "attachment_id";

	/** attachment path key */
	public static final String ATTACHMENT_PATH = "attachment_path";

	/** attachment mime type key */
	public static final String ATTACHMENT_MTYPE = "attachment_mtype";

	/** dynamic string field */
	public static final String DYNAMIC_STR_FIELD = "_strfield";

	/** dynamic number field */
	public static final String DYNAMIC_NUM_FIELD = "_numfield";

	/** dynamic date field */
	public static final String DYNAMIC_DATE_FIELD = "_datefield";
	
	/** 索引库名称  **/
	private static final String CORE_NAME = "_corename";
	
	/** 默认索引库的名称 **/
	private static final String DEFAULT_CORE_NAME = "rhsearch";

	/** url key */
	public static final String URL = "url";

	private static final String BOOST = "boost";
	
    /** 扩展索引字段 **/
    public static final String EXTEND_INDEX = "extendIndex";

    /** 授权范围 **/
    public static final String GRANTEE_SCOPE = "granteeScope";
    
    /** 默认的授权范围  **/
    private static final String DEFAULT_GRANTEE_SCOPE = "ALL";
	

	/** action */
	private ACT act = null;
	/** dynamic fields */
	private Map<String, Object> fields = null;

	private List<IndexAttachment> attList = null;

	private List<String> granteeList = null;

	private List<String> relativeList = null;

	private List<String> keywordList = null;

	/** index transformer */
	private IndexTransformer transformer = null;

	/** update model */
	public static final String OVERWRITE_UPDATE_INDEX_MODEL = "update_model";

	/**
	 * can not new instance
	 */
	@SuppressWarnings("unused")
	private RhIndex() {

	}

	/**
	 * build add Index Message
	 * 
	 * @param title - display title
	 * @param content - display content
	 * @param service - serv id
	 * @param owner - owner id
	 * @param dept - department id
	 * @param company - company id
	 * @param lastModified - last modified time
	 */
	public RhIndex(String title, String content, String service, String owner, String dept, String company,
			Date lastModified) {
		set(DISPLAY_TITLE, title);
		set(CONTENT, content);
		set(DEPARTMENT, dept);
		set(OWNER, owner);
		set(SERVICE, service);
		set(COMPANY, company);
		set(LAST_MODIFIED, lastModified);
		setAct(ACT.ADD);
		fields = new HashMap<String, Object>();
		attList = new LinkedList<IndexAttachment>();
		granteeList = new LinkedList<String>();
		relativeList = new LinkedList<String>();
		keywordList = new LinkedList<String>();
	}

	/**
	 * build delete Index Message
	 * 
	 * @param rrn ruaho resource namespace
	 */
	public RhIndex(String rrn) {
		set(INDEX_ID, rrn);
		setAct(ACT.DELETE);
	}

	/**
	 * build update Index Message
	 * 
	 * @param rrn - ruaho resource namespace
	 * @param overwrite - is overwrite model?
	 */
	public RhIndex(String rrn, boolean overwrite) {
		set(INDEX_ID, rrn);
		setAct(ACT.UPDATE);
		set(OVERWRITE_UPDATE_INDEX_MODEL, overwrite);
		fields = new HashMap<String, Object>();
		attList = new LinkedList<IndexAttachment>();
		granteeList = new LinkedList<String>();
		relativeList = new LinkedList<String>();
		keywordList = new LinkedList<String>();
	}

	/**
	 * get index transformer
	 * @return this index's transformer
	 */
	public IndexTransformer getTransformer() {
		return transformer;
	}

	/**
	 * set this index transformer This method will be a callback
	 * @param tf transformer
	 */
	public void setTransformer(IndexTransformer tf) {
		transformer = tf;
	}

	/**
	 * add keyword
	 * @param keyword - keyword string
	 */
	public void addKeyword(String keyword) {
		// if (keywordList.contains(keyword)){
		// return;
		// }
		keywordList.add(keyword);
	}

	/**
	 * get keywords list
	 * @return keywords
	 */
	public List<String> getKeywords() {
		return keywordList;
	}

	/**
	 * set boost value(default is 1.0)
	 * @param f boost value
	 */
	public void setBoost(float f) {
		set(BOOST, f);
	}

	/**
	 * get boost value
	 * @return boost value
	 */
	public float getBoost() {
		return (float) get(BOOST, (double) 1.00);
	}

	/**
	 * set result abstract <br>
	 * @param disAbstract abstract text support html script <br>
	 *            internal expresstion: ${dynamic-content} base conent
	 */
	public void setAbstract(String disAbstract) {
		set(DISPLAY_ABSTRACT, disAbstract);
	}

	/**
	 * is over write model for update ?
	 * @return boolean value
	 */
	public boolean isOverwriteModel() {
		return get(OVERWRITE_UPDATE_INDEX_MODEL, false);
	}

	/**
	 * set search relative data
	 * @param relative - relative query
	 */
	public void addSearchRelative(String relative) {
		addRelative(RELATIVE_TYPE.SY_PLUG_SEARCH_SERV.toString(), relative);
	}

	/**
	 * set relative data
	 * @param dataType - relative type service id
	 * @param relative - relative query
	 */
	public void addRelative(String dataType, String relative) {
		relativeList.add(dataType + "://" + relative);
	}

	/**
	 * get relative
	 * @return relative string
	 */
	public List<String> getRelatives() {
		return relativeList;
	}

	/**
	 * get abstract
	 * @return abstract string
	 **/
	public String getAbstract() {
		return getStr(DISPLAY_ABSTRACT);
	}

	/**
	 * set preview text
	 * @param preview support html script <br>
	 */
	public void setPreview(String preview) {
		set(DISPLAY_PREVIEW, preview);
	}

	/**
	 * get preview text
	 * @return preview text
	 */
	public String getPreview() {
		return getStr(DISPLAY_PREVIEW);
	}

	/**
	 * replace preview
	 * @param target - The sequence of char values to be replaced
	 * @param replacement - The replacement sequence of char values
	 * 
	 */
	public void replacePreview(String target, String replacement) {
		String preview = getPreview();
		preview = preview.replace(target, replacement);
		setPreview(preview);
	}

	/**
	 * replace preview
	 * @param regex - the regular expression to which this string is to be matched
	 * @param replacement - The replacement sequence of char values
	 * 
	 */
	public void replaceAllPreview(String regex, String replacement) {
		String preview = getPreview();
		preview = preview.replaceAll(regex, replacement);
		setPreview(preview);
	}

	/**
	 * replace abstract
	 * @param target - The sequence of char values to be replaced
	 * @param replacement - The replacement sequence of char values
	 * 
	 */
	public void replaceAbstract(String target, String replacement) {
		String abstractText = getAbstract();
		abstractText = abstractText.replace(target, replacement);
		setAbstract(abstractText);
	}

	/**
	 * replace abstract
	 * @param regex - the regular expression to which this string is to be matched
	 * @param replacement - The replacement sequence of char values
	 * 
	 */
	public void replaceAllAbstract(String regex, String replacement) {
		String abstractText = getAbstract();
		abstractText = abstractText.replaceAll(regex, replacement);
		setAbstract(abstractText);
	}

	/**
	 * Authorized all users (public grantee) for this data
	 * 
	 */
	public void grantAllUsers() {
		granteeList.add(SearchHelper.ALL_USERS_GRANTEE);
	}

	/**
	 * Authorized users for this data
	 * @param cmpy company id
	 * @param dept deparment id
	 * @param role role id
	 */
	public void grant(String cmpy, String dept, String role) {
		String expresstion = SearchHelper.getMulitGranteeExpr(cmpy, dept, role);
		log.debug(expresstion);
		granteeList.add(expresstion);
	}

	/**
	 * Authorized users for this data
	 * @param user user id string
	 */
	public void grantUser(String user) {
		granteeList.add(SearchHelper.getUserGranteeExpr(user));
	}
	
	   /**
     * Authorized groups for this data
     * @param group - group id string
     */
    public void grantGroup(String group) {
        granteeList.add(SearchHelper.getGroupGranteeExpr(group));
    }

	/**
	 * Authorized deparment for this data
	 * @param deparment deparment id sting
	 */
	public void grantDept(String deparment) {
		granteeList.add(SearchHelper.getDeptGranteeExpr(deparment));
	}

	/**
	 * Authorized role for this data
	 * @param role role id string
	 */
	public void grantRole(String role) {
		granteeList.add(SearchHelper.getRoleGranteeExpr(role));
	}

	/**
	 * Authorized company for this data
	 * @param cmpy company id string
	 */
	public void grantCmpy(String cmpy) {
		granteeList.add(SearchHelper.getCmpyGranteeExpr(cmpy));
	}

	/**
	 * get trantee list <br>
	 * example: u_david (user: david) <br>
	 * d_product (deparment: product) <br>
	 * r_manager (role: manager)
	 * @return grantee list
	 */
	public List<String> getGranteeList() {
		return granteeList;
	}

	/**
	 * set index id
	 * 
	 * @param rrn ruaho resource name
	 */
	public void setIndexId(String rrn) {
		set(INDEX_ID, rrn);
	}

	/**
	 * get index id
	 * 
	 * @return index id
	 */
	public String getIndexId() {
		return getStr(INDEX_ID);
	}

	/**
	 * put user defined key-value field, string value(options)
	 * 
	 * @param key field key
	 * @param value field value
	 */
	public void putStrField(String key, String value) {
		key += DYNAMIC_STR_FIELD;
		fields.put(key, value);
	}

	/**
	 * get user defined key-value field
	 * 
	 * @param key field key
	 * @return string field
	 */
	public String getStrField(String key) {
		key += DYNAMIC_STR_FIELD;
		return (String) fields.get(key);
	}

	/**
	 * put user defined key-value field, Numeric Value (options)
	 * 
	 * @param key key
	 * @param numValue numeric value
	 */
	public void putNumField(String key, double numValue) {
		key += DYNAMIC_NUM_FIELD;
		fields.put(key, numValue);
	}

	/**
	 * put user defined key-value field, Datetime Value (options)
	 * 
	 * @param key key
	 * @param dateValue date value<CODE>Date</CODE>
	 */
	public void putDateField(String key, Date dateValue) {
		key += DYNAMIC_DATE_FIELD;
		fields.put(key, dateValue);
	}

	/**
	 * add attachment
	 * 
	 * @param attId attachment id
	 * @param attPath attachment expression format: protocol//path <BR>
	 *            example: internal://dff-2323-dfdf2-dfdfdf.doc <BR>
	 *            http://localhost:8080/file.doc <BR>
	 *            ftp://localhost:21/ftpfile <BR>
	 * @param attTitle display title
	 */
	public void addAtt(String attId, String attPath, String attTitle) {
		IndexAttachment attachment = new IndexAttachment();
		attachment.setId(attId);
		attachment.setIndexPath(attPath);
		attachment.setTitle(attTitle);
		attList.add(attachment);
	}
	
	/**
	 * 增加附件
	 * @param attachment 附件
	 */
    public void addAtt(IndexAttachment attachment) {
        attList.add(attachment);
    }
	
	/**
	 * get attachment expression list
	 * @return attachment expression list
	 */
	public List<IndexAttachment> getAttList() {
		return attList;
	}

	/**
	 * get dynamic field
	 * 
	 * @return fields map
	 */
	public Map<String, Object> getFields() {
		return fields;
	}

	/**
	 * get index message action
	 * 
	 * @return index act
	 */
	public ACT getAct() {
		return act;
	}

	/**
	 * get title
	 * 
	 * @return title string
	 */
	public String getTitle() {
		return getStr(DISPLAY_TITLE);
	}

	/**
	 * get content
	 * 
	 * @return content string
	 */
	public String getContent() {
		return getStr(CONTENT);
	}

	/**
	 * get owner id
	 * 
	 * @return user id
	 */
	public String getOwner() {
		return getStr(OWNER);
	}

	/**
	 * get department id
	 * 
	 * @return dept id
	 */
	public String getDept() {
		return getStr(DEPARTMENT);
	}

	/**
	 * get company
	 * 
	 * @return cmpy
	 */
	public String getCmpy() {
		return getStr(COMPANY);
	}

	/**
	 * get create time
	 * 
	 * @return create time
	 */
	public Date getCreateTime() {
		return (Date) get(CREATE_TIME);
	}

	/**
	 * get last modified time
	 * 
	 * @return modified time
	 */
	public Date getLastModified() {
		return (Date) get(LAST_MODIFIED);
	}

	/**
	 * get serv
	 * 
	 * @return service str
	 */
	public String getService() {
		return getStr(SERVICE);
	}

	/**
	 * set data url
	 * 
	 * @param url url string example: http://new.sina.com/abcd.html <BR>
	 *            internal://SY_TEST.pk001 <BR>
	 *            ftp://172.16.0.4:21/abc/eft.doc <BR>
	 */
	public void setUrl(String url) {
		set(URL, url);
	}

	/**
	 * get data url
	 * 
	 * @return url string
	 */
	public String getUrl() {
		return getStr(URL);
	}
	
    /**
     * 
     * @return 取得扩展索引字段的值
     */
    public List<String> getExtendIndex() {
        return getList(EXTEND_INDEX);
    }

    /**
     * 
     * @param val 增加索引数据
     */
    public void addExtendIndex(String val) {
        List<String> list = getList(EXTEND_INDEX);
        list.add(val);
    }
    
    /**
     * 
     * @return 授权范围
     */
    public List<String> getGranteeScope() {
        List<String> list = getList(GRANTEE_SCOPE);
        if (list.size() == 0) {
            list.add(DEFAULT_GRANTEE_SCOPE);
        }
        return list;
    }
    
    /**
     * 增加授权范围
     * @param val 授权范围值
     */
    public void addGranteeScope(String val) {
        List<String> list = getList(GRANTEE_SCOPE);
        list.add(val);
    }

	/**
	 * set index action
	 * 
	 * @param targetAct target act
	 */
	private void setAct(ACT targetAct) {
		act = targetAct;
	}

	// /////////////////////////////////////////////////////////
	// GongWenIndex TODO code reconsitution
	// ///////////////////////////////////////////////////////

	/** content key */
	public static final String FILE_PATH = "file_path";

	/**
	 * set file expresstion
	 * @param path - file expression format: protocol//path <BR>
	 *            example: internal://dff-2323-dfdf2-dfdfdf.doc <BR>
	 *            ${rh.file.server}/file.png http://localhost:8080/file.doc <BR>
	 *            ftp://localhost:21/ftpfile <BR>
	 */
	public void setFilePath(String path) {
		putStrField(FILE_PATH, path);
	}

	/**
	 * get file path
	 * @return text content
	 */
	public String getFilePath() {
		return getStrField(FILE_PATH);
	}
	
	/**
	 * 
	 * @return 取得索引库的名称。如果未设置索引库名称，则返回默认索引库名称。
	 */
    public String getCoreName() {
        if (this.isNotEmpty(CORE_NAME)) {
            return getStr(CORE_NAME);
        }
        return DEFAULT_CORE_NAME;
    }
	
    /**
     * 设置索引库名称
     * @param coreName 索引库名称
     */
    public void setCoreName(String coreName) {
        this.set(CORE_NAME, coreName);
    }
}
