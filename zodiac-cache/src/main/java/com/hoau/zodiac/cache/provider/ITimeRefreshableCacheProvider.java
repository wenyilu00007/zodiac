package com.hoau.zodiac.cache.provider;

import java.util.Date;

/**
 * @author 陈宇霖
 * @version V1.0
 * @Title: ITimeRefreshableCacheProvider
 * @Package com.hoau.zodiac.cache.provider
 * @Description: 基于时间刷新的缓存提供者
 * @date 2017/8/7 08:25
 */
public interface ITimeRefreshableCacheProvider<K, V> {
    /**
     * 获取最后修改时间
     *
     * @return
     * @author 陈宇霖
     * @date 2017年08月07日08:26:00
     */
    Date getLastModifyTime();
}
