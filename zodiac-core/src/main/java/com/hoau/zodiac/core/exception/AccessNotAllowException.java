package com.hoau.zodiac.core.exception;

/**
* @ClassName: AccessNotAllowException
* @Description: 访问拒绝异常
* @author HOAU-271755
* @date 2015年4月21日 下午6:52:35
*
*/
public class AccessNotAllowException extends RuntimeException {

	/**
	 * 异常信息国际化编码
	 */
	private String errCode;

	/**
	 * 默认国际化编码
	 */
	public static final String ERROR_CODE = "error.user.no.right.to.access";

	/**
	 * 默认异常提示内容
	 */
	public static final String MESSAGE = "Method not allow access";
	
	public AccessNotAllowException() {
		this(MESSAGE);
		this.errCode = ERROR_CODE;
	}

	public AccessNotAllowException(String msg) {
		super(msg);
		this.errCode = ERROR_CODE;
	}

	public String getErrorCode() {
		return this.errCode;
	}

}
