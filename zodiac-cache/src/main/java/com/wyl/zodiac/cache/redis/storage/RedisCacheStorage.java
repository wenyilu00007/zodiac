package com.wyl.zodiac.cache.redis.storage;

import com.wyl.zodiac.cache.exception.KeyIsNotFoundException;
import com.wyl.zodiac.cache.exception.ValueIsNullException;
import com.wyl.zodiac.cache.redis.constant.RedisConstants;
import com.wyl.zodiac.cache.redis.exception.RedisCacheStorageException;
import com.wyl.zodiac.cache.storage.IRemoteCacheStore;
import com.wyl.zodiac.core.util.string.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @param <K>
 * @param <V>
 * @author
 * @ClassName RedisCacheStorage
 * @Description 通过redis存储缓存数据
 * @date 2017年08月07日09:02:21
 */
public class RedisCacheStorage<K, V> implements IRemoteCacheStore<K, V>, InitializingBean {

    /**
     * 实际进行缓存操作的template，由spring提供
     */
    private RedisTemplate redisTemplate;

    /**
     * 日志
     */
    Log log = LogFactory.getLog(getClass());

    /**
     * 初始化Strong cache任务
     */
    private StrongCacheTask strongTask;

    /**
     * <p>存入数据，默认时效：3600 * 24</p>
     *
     * @param key
     * @param value
     * @return boolean 是否执行成功
     */
    @Override
    public boolean set(K key, V value) {
        return set(key, value, RedisConstants.TTL_DEFAULT_EXPIRED_TIME);
    }

    /**
     * <p>存入有时效的数据</p>
     *
     * @param key
     * @param value
     * @param exp
     * @return boolean 是否执行成功
     */
    @Override
    public boolean set(K key, V value, int exp) {
        if (key == null) {
            throw new RedisCacheStorageException("key does not allow for null!");
        }
        redisTemplate.opsForValue().set(key, value, exp, TimeUnit.SECONDS);
        return true;
    }

    /**
     * <p>获取key对应的数据</p>
     *
     * @param key
     * @return
     */
    @Override
    public V get(K key) {
        if (key == null) {
            throw new RedisCacheStorageException("key does not allow for null!");
        }
        //key是否存在
        boolean exist = redisTemplate.hasKey(key);
        if (!exist) {
            throw new KeyIsNotFoundException("key is not found!");
        }
        Object obj = redisTemplate.opsForValue().get(key);
        if (obj == null) {
            throw new ValueIsNullException("key exists, value is null!");
        }
        return (V) obj;
    }

    /**
     * <p>删除指定的缓存信息</p>
     *
     * @param key
     */
    @Override
    public void remove(K key) {
        redisTemplate.delete(key);
    }

    /**
     * <p>删除多个key的缓存信息</p>
     *
     * @param keys
     */
    @Override
    public void removeMulti(K... keys) {
        redisTemplate.delete(keys);
    }

    /**
     * 获取key对应的数据
     *
     * @return
     * @see
     */
    public V hget(String cacheId, K key) {
        if (key == null) {
            throw new RedisCacheStorageException("key does not allow for null!");
        }
        //key是否存在
        boolean exist = redisTemplate.opsForHash().hasKey(cacheId, key);
        if (!exist) {
            throw new KeyIsNotFoundException("key is not found!");
        }
        Object obj = redisTemplate.opsForHash().get(cacheId, key);
        if (obj == null) {
            throw new ValueIsNullException("key exists, value is null!");
        }
        return (V) obj;
    }

    /**
     * 存入数据
     *
     * @param cacheId
     * @param key
     * @param value
     * @return 执行成功与否
     */
    public boolean hset(String cacheId, K key, V value) {
        if (key == null) {
            throw new RedisCacheStorageException("key does not allow for null!");
        }
        redisTemplate.opsForHash().put(cacheId, key, value);
        return true;
    }

    /**
     * 获取一组缓存中所有的值
     *
     * @param cacheId
     * @return
     */
    public Map<K, V> hget(String cacheId) {
        if (StringUtils.isBlank(cacheId)) {
            throw new RedisCacheStorageException("cacheId does not allow for null!");
        }
        Map<Object, Object> map = redisTemplate.opsForHash().entries(cacheId);
        Map<K, V> result = null;
        //由string转成对象
        if (map != null) {
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                if (result == null) {
                    result = new HashMap<K, V>();
                }
                result.put((K) entry.getKey(), (V) entry.getValue());
            }
            return result;
        }
        return null;
    }

    /**
     * 批量移除指定缓存中的相关key
     *
     * @param cacheId
     * @param keys
     */
    public void hremoveMulti(final String cacheId, final K... keys) {
        if (StringUtils.isBlank(cacheId)) {
            throw new RedisCacheStorageException("cacheId does not allow for null!");
        }
        if (keys == null) {
            throw new RedisCacheStorageException("keys does not allow for null!");
        }
        redisTemplate.opsForHash().delete(cacheId, keys);
    }

    /**
     * 删除指定缓存中指定的值
     *
     * @param cacheId
     * @param key
     */
    public void hremove(final String cacheId, final K key) {
        if (StringUtils.isBlank(cacheId)) {
            throw new RedisCacheStorageException("cacheId does not allow for null!");
        }
        if (key == null) {
            throw new RedisCacheStorageException("key does not allow for null!");
        }
        redisTemplate.opsForHash().delete(cacheId, key);
    }

    /**
     * 删除指定的缓存
     *
     * @param cacheId
     */
    public void hremove(final String cacheId) {
        if (StringUtils.isBlank(cacheId)) {
            throw new RedisCacheStorageException("cacheId does not allow for null!");
        }
        redisTemplate.delete(cacheId);
    }

    /**
     * 初始化只读缓存
     *
     * @param cacheId
     * @param map
     */
    public void initializationStrongCache(final String cacheId, Map<K, V> map) {
        Boolean isSuccess = (Boolean) redisTemplate.opsForValue().get(RedisConstants.STRONG_CACHE_INIT_FLAG_PREFIX + cacheId);
        if (isSuccess == null || !isSuccess) {//如果对应的cacheId 没有初始化过或者初始化失败
            Boolean lock = setNx(RedisConstants.STRONG_CACHE_INIT_LOCKING_FLAG_PREFIX + cacheId,
                    RedisConstants.STRONG_CACHE_INIT_LOCKING_CACHE_VALUE);
            if (lock) {
                initializationStrongCacheData(cacheId, map);
            } else {
                // 稍后重试
                retryStorage(cacheId, map);
                return;
            }
        }
    }

    /**
     * 初始化只读缓存实现
     *
     * @param cacheId
     * @param map
     */
    private void initializationStrongCacheData(final String cacheId, Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null) {
                log.error("storage cache initialization error: key and value does not allow for null!");
                setNx(RedisConstants.STRONG_CACHE_INIT_FLAG_PREFIX + cacheId, "false");
                redisTemplate.delete(RedisConstants.STRONG_CACHE_INIT_LOCKING_FLAG_PREFIX + cacheId);
                return;
            }
            redisTemplate.opsForHash().put(cacheId, entry.getKey(), entry.getValue());
        }
        setNx(RedisConstants.STRONG_CACHE_INIT_FLAG_PREFIX + cacheId, "true");
        redisTemplate.delete(RedisConstants.STRONG_CACHE_INIT_LOCKING_FLAG_PREFIX + cacheId);
    }

    /**
     * 设置指定值，只有当key在缓存中不存在的时候才可以进行设置，只能设置一次，用于初始化时加锁
     *
     * @param key
     * @param value
     * @return
     */
    private Boolean setNx(final String key, final String value) {
        return (Boolean) redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                StringRedisSerializer serializer = new StringRedisSerializer();
                Boolean success = connection.setNX(serializer.serialize(key), serializer.serialize(value));
                return success;
            }
        });
    }

    /**
     * 开启初始化StrongCache线程任务
     *
     * @see
     */
    private void retryStorage(String cacheId, Map<K, V> map) {
        if (strongTask == null) {
            strongTask = new StrongCacheTask("重试初始化Strong Cache任务", cacheId, map);
            strongTask.setDaemon(true);
            strongTask.start();
        } else if (strongTask.getState().name().equals(Thread.State.NEW.name())) {
            strongTask.start();
        } else if (strongTask.getState().name().equals(Thread.State.TERMINATED.name())) {
            strongTask = new StrongCacheTask("重试初始化Strong Cache任务", cacheId, map);
            strongTask.setDaemon(true);
            strongTask.start();
        }
    }

    /**
     * 此线程用于初始化只读缓存，因为同一时间只能有一个只读缓存在初始化，其他只读缓存只能轮询等待获取到锁才能初始化
     */
    class StrongCacheTask extends Thread {
        int count = 1;
        String cacheId;
        Map<K, V> map;

        public StrongCacheTask(String name, String cacheId, Map<K, V> map) {
            super(name);
            count = 1;
            this.cacheId = cacheId;
            this.map = map;
        }

        @SuppressWarnings("static-access")
        @Override
        public void run() {
            log.debug("初始化Strong Cache任务开始，延迟" + RedisConstants.STRONG_RETRY_INIT_INTERVAL + "后开始执行!");

            while (true) {
                if (map == null || map.isEmpty()) {
                    return;
                }
                try {
                    sleep(RedisConstants.STRONG_RETRY_INIT_INTERVAL);
                } catch (InterruptedException e1) {
                    log.error(e1.getMessage(), e1);
                }
                log.debug("第" + count + "次尝试初始化Strong Cache!");
                count++;
                Boolean isSuccess = (Boolean) redisTemplate.opsForValue().get(RedisConstants.STRONG_CACHE_INIT_FLAG_PREFIX + cacheId);
                if (isSuccess == null || !isSuccess) {//如果对应的cacheId 没有初始化过或者初始化失败
                    Boolean lock = setNx(RedisConstants.STRONG_CACHE_INIT_LOCKING_FLAG_PREFIX + cacheId,
                            RedisConstants.STRONG_CACHE_INIT_LOCKING_CACHE_VALUE);
                    if (lock) {
                        initializationStrongCacheData(cacheId, map);
                    } else {
                        continue;
                    }
                }
            }
        }

    }

    public void afterPropertiesSet() throws Exception {

    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

}
