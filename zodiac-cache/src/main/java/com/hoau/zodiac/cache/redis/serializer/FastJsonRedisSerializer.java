package com.hoau.zodiac.cache.redis.serializer;

import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
* @Title: FastJsonRedisSerializer 
* @Package com.hoau.zodiac.cache.redis.serializer 
* @Description: 基于fastjson的redis数据序列化工具
* @author 陈宇霖  
* @date 2017/8/7 10:41
* @version V1.0   
*/
public class FastJsonRedisSerializer implements RedisSerializer<Object> {
    /**
     * Serialize the given object to binary data.
     *
     * @param o object to serialize
     * @return the equivalent binary data
     */
    public byte[] serialize(Object o) throws SerializationException {
        if (o == null) {
            return new byte[0];
        }
        return JSON.toJSONBytes(o);
    }

    /**
     * Deserialize an object from the given binary data.
     *
     * @param bytes object binary representation
     * @return the equivalent object instance
     */
    public Object deserialize(byte[] bytes) throws SerializationException {
        return JSON.parse(bytes);
    }
}
