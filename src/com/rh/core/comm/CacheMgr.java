package com.rh.core.comm;

import java.util.ArrayList;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import com.rh.core.base.Context;
import com.rh.core.base.Context.APP;
import com.rh.core.base.Context.THREAD;

/**
 * 缓存管理器
 * 
 * @author cuihf
 * 
 */
public class CacheMgr {

    /** 缓存管理器静态实例。 */
    private static CacheMgr cacheManager = null;
    /** ehcache缓存管理器 . */
    private static CacheManager ehCacheManager = null;

    /**
     * 获取CacheMgr的实例.
     * 
     * @return CacheMgr的实例
     */
    public static CacheMgr getInstance() {
        if (cacheManager == null) {
            String ehcacheConf = Context.app(APP.WEBINF) + "ehcache.xml";
            ehCacheManager = CacheManager.create(ehcacheConf);
            cacheManager = new CacheMgr();
        }
        return cacheManager;
    }

    /**
     * 关闭缓存.
     */
    public static void destroyCache() {
        if (ehCacheManager != null) {
            ehCacheManager.shutdown();
        }
    }

    /**
     * 根据缓存类型清除缓存数据。
     * 
     * @param cacheType 缓存类型/缓存名称
     */
    public void clearCache(String cacheType) {
        if (ehCacheManager != null && ehCacheManager.cacheExists(cacheType)) {
            ehCacheManager.removeCache(cacheType);
        }
    }

    /**
     * 根据指定的缓存类型和key获取缓存对象
     * 
     * @param key 缓存的key
     * @param cacheType 缓存类型/缓存名称
     * @return 缓存的对象
     */
    public Object get(Object key, String cacheType) {
        if (!ehCacheManager.cacheExists(cacheType)) {
            return null;
        }
        Cache cache = ehCacheManager.getCache(cacheType);
        Element element = cache.get(key);
        if (element == null) {
            element = cache.get(key + "_" + Context.getThread(THREAD.CMPYCODE));
        }
        if (element != null) {
            return element.getObjectValue();
        }

        return element;
    }

    /**
     * 根据指定的缓存类型获取对应key的列表
     * 
     * @param cacheType 缓存类型/缓存名称
     * @return 缓存对象KEY列表
     */
    @SuppressWarnings("unchecked")
    public List<Object> getKeyList(String cacheType) {
        List<Object> keyList = null;
        if (ehCacheManager.cacheExists(cacheType)) {
            Cache cache = ehCacheManager.getCache(cacheType);
            keyList = cache.getKeys();
        }
        if (keyList == null) {
            keyList = new ArrayList<Object>();
        }
        return keyList;
    }

    /**
     * 将数据写入缓存。
     * 
     * @param key 缓存的key
     * @param object 缓存的对象
     * @param cacheType 缓存类型/缓存名称
     */
    public void set(Object key, Object object, String cacheType) {
        set(key, object, cacheType, null);
    }

    /**
     * 将数据写入缓存。
     * 
     * @param key 缓存的key
     * @param object 缓存的对象
     * @param cacheType 缓存类型/缓存名称
     * @param cmpyCode 公司编码
     */
    public void set(Object key, Object object, String cacheType, String cmpyCode) {
        set(key, object, cacheType, cmpyCode, -1);
    }

    /**
     * 将数据写入缓存。
     * 
     * @param key 缓存的key
     * @param object 缓存的对象
     * @param cacheType 缓存类型/缓存名称
     * @param cmpyCode 公司编码
     * @param timeToLiveSeconds 缓存存活时间(秒)
     */
    public void set(Object key, Object object, String cacheType, String cmpyCode, int timeToLiveSeconds) {
        Cache cache = getCache(cacheType);
        Element elem = null;
        if (cmpyCode == null) {
            elem = new Element(key, object);
        } else {
            elem = new Element(key + "_" + cmpyCode, object);
        }

        if (0 < timeToLiveSeconds) {
            elem.setTimeToLive(timeToLiveSeconds);
        }
        cache.put(elem);
    }

    /**
     * 取得指定名称的缓存对象
     * @param cacheType 缓存名称
     * @return 指定名称的缓存对象
     */
    private Cache getCache(String cacheType) {
        synchronized (cacheManager) {
            if (!ehCacheManager.cacheExists(cacheType)) {
                ehCacheManager.addCache(cacheType);
            }
        }

        return ehCacheManager.getCache(cacheType);
    }

    /**
     * 在指定的缓存中清除数据.
     * 
     * @param key 缓存的key
     * @param cacheType 缓存类型/缓存名称
     */
    public void remove(Object key, String cacheType) {
        remove(key, cacheType, null);
    }

    /**
     * 在指定的缓存中清除数据.
     * 
     * @param key 缓存的key
     * @param cacheType 缓存类型/缓存名称
     * @param cmpyCode 公司编码
     */
    public void remove(Object key, String cacheType, String cmpyCode) {
        if (ehCacheManager.cacheExists(cacheType)) {
            if (cmpyCode == null) {
                ehCacheManager.getCache(cacheType).remove(key);
            } else {
                ehCacheManager.getCache(cacheType).remove(key + "_" + cmpyCode);
            }
        }
    }

    /**
     * 获取EhCacheManager的实例。
     * @return EhCacheManager实例
     */
    public CacheManager getEhCacheManager() {
        return ehCacheManager;
    }

    /**
     * 
     * @param cacheType 缓存对象名称
     * @return 取得指定缓存对象的数据量
     */
    public int getSize(String cacheType) {
        if (!ehCacheManager.cacheExists(cacheType)) {
            return 0;
        }

        return ehCacheManager.getCache(cacheType).getSize();
    }

    /**
     * 
     * @param cacheType 缓存对象名称
     * @param offset 从那个位置开始取，从0开始
     * @param size 取多少条记录
     * @return 从指定缓存中取得数据列表
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List getDataList(String cacheType, int offset, int size) {
        if (!ehCacheManager.cacheExists(cacheType)) {
            return new ArrayList<Object>();
        }

        Cache cache = ehCacheManager.getCache(cacheType);

        List<String> list = ehCacheManager.getCache(cacheType).getKeys();

        if (offset >= list.size()) {
            return new ArrayList();
        }

        int len = size + offset;
        if (size <= 0) {
            len = list.size();
        } else if (len >= list.size()) {
            len = list.size();
        }

        List<Object> rtnList = new ArrayList();
        for (int i = offset; i < len; i++) {
            String key = list.get(i);
            Element element = cache.get(key);
            rtnList.add(element.getObjectValue());
        }
        return rtnList;
    }
}
