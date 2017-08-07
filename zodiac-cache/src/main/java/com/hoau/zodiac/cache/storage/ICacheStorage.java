package com.hoau.zodiac.cache.storage;

import java.util.Map;


/**
 * @param <K>
 * @param <V>
 * @author 陈宇霖
 * @ClassName ICacheStorage
 * @Description 抽象的数据缓存仓库
 * @date 2017年08月07日08:19:02
 */
public interface ICacheStorage<K, V> {

    /**
     * 存放数据
     *
     * @param key
     * @param value
     */
    void set(K key, V value);

    /**
     * 批量设置数据
     *
     * @param values
     */
    void set(Map<K, V> values);

    /**
     * 获取数据
     *
     * @param key
     */
    V get(K key);

    /**
     * 移除指定的数据
     *
     * @param key
     */
    void remove(K key);

    /**
     * 批量获取数据
     *
     * @return
     */
    Map<K, V> get();

    /**
     * 是否存在
     *
     * @param key
     * @return
     * @see
     */
    Boolean exists(K key);

    /**
     * 移除所有的数据
     */
    void clear();
}
