package com.runonce.handle;

import com.runonce.ReturnCode;
import com.runonce.exception.CustomException;
import com.runonce.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author  joker
 * @date 2019/7/16 22:29
 * @deescription 全局异常处理类，捕获所有来自Controller的异常，打印异常信息到日志文件中，并返回简单错误码给前端。
*/
@Slf4j
@ControllerAdvice
public class ExceptionHandle {

	@ResponseBody
	@ExceptionHandler(value = Exception.class)
	public ReturnCode handle(Exception e) {
		if (e instanceof CustomException) {
		    CustomException userException = (CustomException) e;
			log.error(userException.getMessage());
			return ResultUtil.failed(userException.getCode(), userException.getMessage());
		} else {
			log.error(String.format("【系统异常】{%s}", e));
			e.printStackTrace();
			return ResultUtil.failed(-1, "未知错误");
		}
	}
}
