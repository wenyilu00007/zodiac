package com.wyl.zodiac.core.util;

import java.util.*;

/**
 * @author
 * @version V1.0
 * @Title: MapUtils
 * @Package com.wyl.zodiac.core.util
 * @Description: 操作map工具类
 * @date 2017/8/22 08:13
 */
public class MapUtils {


    /**
     * 去除map中值为空的记录
     *
     * @return 去掉空值后的新参数组
     * @author
     * @date 2017年08月22日08:19:53
     */
    public static Map<String, Object> removeEmpty(Map<String, Object> sArray) {

        Map<String, Object> result = new HashMap<String, Object>();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }

        for (Map.Entry<String, Object> entity : sArray.entrySet()) {
            Object value = entity.getValue();
            if (value == null) {
                continue;
            }
            //空字符串
            if (value instanceof String && ((String)value).length() == 0) {
                continue;
            }
            //空数组
            if (value instanceof Object[] && ((Object[])value).length == 0) {
                continue;
            }
            //空集合
            if (value instanceof Collection && ((Collection)value).size() == 0) {
                continue;
            }
            result.put(entity.getKey(), value);
        }

        return result;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     * @author
     * @date 2017年08月22日08:20:10
     */
    public static String createLinkString(Map<String, Object> params) {
        if (params == null || params.size() == 0) {
            return "";
        }

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        StringBuffer result = new StringBuffer();

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            Object value = params.get(key);

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                result.append(key).append("=").append(value instanceof Object[] ? ((Object[])value).length == 1 ? ((Object[])value)[0] : Arrays.toString((Object[])value) : value);
            } else {
                result.append(key).append("=").append(value instanceof Object[] ? ((Object[])value).length == 1 ? ((Object[])value)[0] : Arrays.toString((Object[])value) : value).append("&");
            }
        }

        return result.toString();
    }

}
