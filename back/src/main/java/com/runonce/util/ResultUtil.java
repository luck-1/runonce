package com.runonce.util;

import com.runonce.RetCodeEnum;
import com.runonce.ReturnCode;

/**
 * 
 * @author klaus
 *
 */
public class ResultUtil {

    public static ReturnCode success(Object object) {
        return new ReturnCode.Builder().code(1).msg("成功").object(object).build();
    }

    public static ReturnCode success() {
        return success(null);
    }

    public static ReturnCode failed(Integer code, String msg) {
        return new ReturnCode.Builder().code(code).msg(msg).build();
    }
}
