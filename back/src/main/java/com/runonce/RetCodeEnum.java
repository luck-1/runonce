package com.runonce;

/**
 * @author swq
 * @date 2018/10/24 0024
 * @description
 */
public enum RetCodeEnum {
    SUCCESS(0,"成功"),
    FAILED(-1,"失败"),
    PARAM_ERROR(1,"参数异常"),
    ERROR(2,"方法异常");
    private int code;
    private String msg;
    RetCodeEnum(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode(){
        return this.code;
    }

    public String getMsg(){
        return this.msg;
    }


}
