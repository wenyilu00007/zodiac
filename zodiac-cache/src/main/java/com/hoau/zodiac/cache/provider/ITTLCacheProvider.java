package com.hoau.zodiac.cache.provider;

/**
 * @param <V>
 * @author 陈宇霖
 * @ClassName ITTLCacheProvider
 * @Description 定时失效缓存数据提供者
 * @date 2017年08月07日08:20:37
 */
public interface ITTLCacheProvider<V> {

    /**
     * 加载单个元素
     * get
     *
     * @param key
     * @return V
     * @since:
     */
    V get(String key);

}
