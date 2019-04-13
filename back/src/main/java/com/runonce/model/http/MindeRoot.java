package com.runonce.model.http;

import lombok.Data;

import java.io.*;

/**
 * @Author: zhaolei
 * @Descriptions:
 * @Date: create at 2018/12/13 0013 上午 9:53
 */
@Data
public class MindeRoot implements Serializable{

    private MinderNode root;

    private String template;

    private String theme;
    private String version;

}
