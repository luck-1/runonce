package com.runonce.exception;

import com.runonce.ReturnCode;
import com.runonce.enums.IResultEnumRole;

/**
 * 
 * @author klaus
 *
 */
public class CustomException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private Integer code;
//	private String msg;

	public CustomException(ReturnCode returnCode) {
		super(returnCode.getMsg());
		this.code = returnCode.getCode();
	}

	public Integer getCode() {
		return code;
	}
	//	public String getMsg() {
//		return msg;
//	}
	public void setCode(Integer code) {
		this.code = code;
	}
//	public void setMsg(String msg) {
//		this.msg = msg;
//	}
}
