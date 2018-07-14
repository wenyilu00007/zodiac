package com.wyl.zodiac.web.request;

import org.apache.commons.codec.binary.Base64;

/**
 * @Title TokenMarshal
 * @Package com.wyl.zodiac.web.request
 * @Description 令牌序列化与反序列化
 * @author
 * @date 2017/11/3 15:31
 * @version V1.0
 */
final public class TokenMarshal {
	private TokenMarshal(){
	}

	/**
	 * 序列化(Base64编码)
	 * @param token
	 * @return
	 * @author
	 * @date 2017/11/3 15:31
	 */
	public static String marshal(CookieToken token) {
		return new String(Base64.encodeBase64String(token.toBytes()));
	}

	/**
	 * 反序列化（Base64解码）
	 * @param tokenStr
	 * @return
	 * @author
	 * @date 2017/11/3 15:31
	 */
	public static CookieToken unMarshal(String tokenStr) {
		return new CookieToken(Base64.decodeBase64(tokenStr));
	}
}
