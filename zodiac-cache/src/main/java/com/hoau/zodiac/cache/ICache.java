package com.hoau.zodiac.cache;

import java.util.Map;


/**
 * @param <K> 缓存Key类型
 * @param <V> 缓存Value类型
 * @author 陈宇霖
 * @ClassName ICache
 * @Description 缓存接口
 * @date 2017年08月07日07:54:09
 */
public interface ICache<K, V> {

    /**
     * 标记当前Cache的UUID
     *
     * @return
     */
    String getUUID();

    /**
     * 获取缓存
     *
     * @param key 缓存Key
     * @return 缓存Value
     */
    V get(K key);

    /**
     * 一次性取出所有内容
     *
     * @return
     */
    Map<K, V> get();

    /**
     * 失效一组缓存
     * <p>
     * 使旧的一组缓存全部失效
     * 如果是LRU的在下一次使用会自动加载最新的
     * 如果是Strong的会立即重新加载一次新的数据到缓存中
     */
    void invalid();

    /**
     * 失效key对应的缓存
     * <p>
     * 如果是LRU的会在下一次使用这个Key时自动加载最新的
     * 如果是Strong的会Throws RuntimeException异常，不允许失效部分数据
     */
    void invalid(K key);

    /**
     * 失效传入的多个key对应的缓存
     *
     * @param keys
     */
    void invalidMulti(K... keys);

}
