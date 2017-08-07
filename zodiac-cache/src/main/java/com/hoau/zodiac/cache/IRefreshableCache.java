package com.hoau.zodiac.cache;


/**
 * @param <K>
 * @param <V>
 * @author 陈宇霖
 * @ClassName RefreshableCache
 * @Description 允许刷新缓存接口
 * @date 2017年08月07日08:10:54
 */
public interface IRefreshableCache<K, V> extends ICache<K, V> {
    /**
     * 刷新缓存
     * 根据provider提供的最后修改时间去刷新这段时间之内修改的数据
     * 如果是LRU的根据最后修改时间刷新时间段的数据
     * 如果是Strong根据最后修改时间刷新所有数据
     *
     * @return
     */
    boolean refresh();

}
