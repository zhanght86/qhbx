package com.rh.core.comm.cms;

import java.security.MessageDigest;

import com.rh.core.comm.CacheMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;

/**
 * 可对查询数据进行cache
 * 
 * @author liwei
 * 
 */
public abstract class CacheableServ extends CommonServ {

    /** 最后修改时间 */
    private static long lastModified = System.currentTimeMillis();

    /** 缓存前缀 */
    private static final String CACHE_TYPE_PREFIX = "SY_SERV_DATA_CACHE_";
    
    /** 是否开启数据缓存 */
    private static boolean enable = true;

    /**
     * 获取服务ID
     * @return 缓存服务ID
     */
    protected abstract String getCacheServId();

    /**
     * get cache type str
     * @return cache type
     */
    protected String getCacheType() {
        return CACHE_TYPE_PREFIX + getCacheServId();
    }

    /**
     * 修改最后修改时间
     */
    protected void setLastModified() {
        lastModified = System.currentTimeMillis();
    }

    /**
     * put outbean in cache
     * @param key - query param key
     * @param outBean - out bean
     */
    protected void putInCache(String key, OutBean outBean) {
        if (!enable) {
            return;
        }
       // String key = getKey(paramBean);
        String cacheKey = key + "_" + lastModified;
        int timeToLiveSeconds = 60 * 60 * 24;
        CacheMgr.getInstance().set(cacheKey, outBean, getCacheType(), null, timeToLiveSeconds);

    }

    /**
     * get data from cache
     * @param key - query param key
     * @return - outbean
     */
    protected OutBean getFromCache(String key) {
        if (!enable) {
            return null;
        }
      //  String key = getKey(paramBean);
        String cacheKey = key + "_" + lastModified;
        Object obj = CacheMgr.getInstance().get(cacheKey, getCacheType());
        if (null == obj) {
            return null;
        } else {
            return (OutBean) obj;
        }
    }

    /**
     * clear channel cache
     * @param paramBean - param bean
     * @return - Result of Operation
     */
    protected OutBean clearCache(ParamBean paramBean) {
        OutBean outBean = new OutBean();
        CacheMgr.getInstance().remove(paramBean, getCacheType());
        outBean.setOk("清除成功");
        return outBean;
    }

    @Override
    public OutBean query(ParamBean paramBean) {
        String paramKey = getKey(paramBean);
        OutBean outBean = null;
        // get from cache first
        outBean = getFromCache(paramKey);
        if (null != outBean) {
            log.debug(" get data from cache!");
            return outBean;
        }

        outBean = super.query(paramBean);

        // put in cache
        putInCache(paramKey, outBean);

        return outBean;
    }

    @Override
    public OutBean finds(ParamBean paramBean) {
        String paramKey = getKey(paramBean);
        OutBean outBean = null;
        // get from cache first
        outBean = getFromCache(paramKey);
        if (null != outBean) {
            log.debug(" get data from cache!");
            return outBean;
        }

        outBean = super.finds(paramBean);

        // put in cache
        putInCache(paramKey, outBean);

        return outBean;
    }
    
    @Override
    public OutBean count(ParamBean paramBean) {
        String paramKey = getKey(paramBean);
        OutBean outBean = null;
        // get from cache first
        outBean = getFromCache(paramKey);
        if (null != outBean) {
            log.debug(" get data from cache!");
            return outBean;
        }

        outBean = super.count(paramBean);

        // put in cache
        putInCache(paramKey, outBean);

        return outBean;
    }

    @Override
    public OutBean save(ParamBean paramBean) {
        OutBean outBean = null;
        outBean = super.save(paramBean);
        setLastModified();
        return outBean;
    }

    @Override
    public OutBean delete(ParamBean paramBean) {
        OutBean outBean = null;
        outBean = super.delete(paramBean);
        setLastModified();
        return outBean;
    }

    @Override
    public OutBean batchSave(ParamBean paramBean) {
        OutBean outBean = null;
        outBean = super.batchSave(paramBean);
        setLastModified();
        return outBean;
    }

    /**
     * get parambean key
     * @param paramBean - param bean
     * @return - key
     */
    protected String getKey(ParamBean paramBean) {
        StringBuilder sb = new StringBuilder(32);

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(paramBean.toString().getBytes("utf-8"));

            for (int i = 0; i < array.length; i++) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).toUpperCase().substring(1, 3));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

}
