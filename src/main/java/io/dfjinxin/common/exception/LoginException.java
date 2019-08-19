/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjinxin.common.exception;

/**
 * 自定义异常
 *
 * @author Mark sunlightcs@gmail.com
 */
public class LoginException extends RuntimeException {
	private static final long serialVersionUID = 1L;

    private String msg;
    private int code = 401;

    public LoginException(String msg) {
		super(msg);
		this.msg = msg;
	}

	public LoginException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}

	public LoginException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}

	public LoginException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	
}
