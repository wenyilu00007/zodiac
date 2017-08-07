package com.hoau.zodiac.cache.provider;

import java.util.Map;

/**
* @ClassName: IBatchCacheProvider
* @Description: 批量加载缓存接口
* @author 高佳
* @date 2015年4月22日 下午1:23:47
*
* @param <K>
* @param <V>
*/
public interface IBatchCacheProvider<K, V> extends ITimeRefreshableCacheProvider<K, V> {
    
	/**
	 * 批量加载数据
	 * get
	 * @return
	 * @return Map<K,V>
	 */
    Map<K, V> get();
}
