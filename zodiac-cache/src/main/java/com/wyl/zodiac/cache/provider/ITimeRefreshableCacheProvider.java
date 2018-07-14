package com.wyl.zodiac.cache.provider;

import java.util.Date;

/**
 * @author
 * @version V1.0
 * @Title: ITimeRefreshableCacheProvider
 * @Package com.wyl.zodiac.cache.provider
 * @Description: 基于时间刷新的缓存提供者
 * @date 2017/8/7 08:25
 */
public interface ITimeRefreshableCacheProvider<K, V> {
    /**
     * 获取最后修改时间
     *
     * @return
     * @author
     * @date 2017年08月07日08:26:00
     */
    Date getLastModifyTime();
}
