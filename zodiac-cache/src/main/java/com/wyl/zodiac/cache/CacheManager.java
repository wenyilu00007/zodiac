package com.wyl.zodiac.cache;


import com.wyl.zodiac.cache.exception.CacheConfigException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @param <K>
 * @param <V>
 * @author
 * @ClassName CacheManager
 * @Description 缓存统一管理类，可以通过各个缓存的uuid进行获取
 * @date 2017年08月07日08:00:09
 */
public final class CacheManager<K, V> {

    private static final CacheManager INSTANCE = new CacheManager();

    /**
     * 保存所有缓存实例
     */
    private final Map<String, ICache<K, V>> uuidCaches = new ConcurrentHashMap<String, ICache<K, V>>();

    /**
     * 禁止从外部拿到实例
     * 创建一个新的实例 CacheManager.
     *
     * @since 0.6
     */
    private CacheManager() {
    }

    public static CacheManager getInstance() {
        return INSTANCE;
    }

    /**
     * 将缓存注册到CacheManager中，在各个缓存中调用此方法实现自主注册
     *
     * @param cache
     * @author
     * @date 2017年08月07日08:06:54
     */
    public void registerCache(ICache<K, V> cache) {
        // 不允许UUID重复，应用必须在实现的Cache接口确保命名不重复
        String uuid = cache.getUUID();
        if (uuidCaches.containsKey(uuid)) {
            throw new CacheConfigException("Dumplicate uuid " + uuid
                    + " to cache provider " + cache.getClass().getName()
                    + " and " + uuidCaches.get(uuid).getClass().getName());
        }
        uuidCaches.put(uuid, cache);
    }

    /**
     * 从缓存管理类中移除已注册的缓存
     *
     * @param uuid
     * @return
     * @author
     * @date 2017年08月07日08:06:47
     */
    public ICache<K, V> unRegisterCache(String uuid) {
        if (uuidCaches.containsKey(uuid)) {
            return uuidCaches.remove(uuid);
        }
        return null;
    }

    /**
     * 根据uuid获取缓存实例
     * getCache
     *
     * @param uuid
     * @return ICache<K,V>
     */
    public ICache<K, V> getCache(String uuid) {
        ICache<K, V> cache = uuidCaches.get(uuid);
        if (cache == null) {
            throw new CacheConfigException(
                    "No register cache provider for cache UUID " + uuid);
        }
        return cache;
    }

    /**
     * 清除所有管理的缓存
     *
     * @author
     * @date 2017年08月07日08:02:30
     */
    public void shutdown() {
        uuidCaches.clear();
    }
}