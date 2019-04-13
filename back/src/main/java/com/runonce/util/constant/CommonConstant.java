package com.runonce.util.constant;

/**
 * 常量
 * @author Exrickx
 */
public interface CommonConstant {

    /**
     * 用户默认头像
     */
    String USER_DEFAULT_AVATAR = "https://s1.ax1x.com/2018/05/19/CcdVQP.png";

    /**
     * 用户正常状态
     */
    int USER_STATUS_NORMAL = 0;

    /**
     * 用户禁用状态
     */
    int USER_STATUS_LOCK = -1;

    /**
     * 普通用户
     */
    int USER_TYPE_NORMAL = 0;

    /**
     * 管理员
     */
    int USER_TYPE_ADMIN = 1;

    /**
     * 全部数据权限(超级管理员)
     */
    int DATA_TYPE_ALL = 0;

    /**
     * 自定义数据权限(部门管理员)
     */
    int DATA_TYPE_DEPT_CUSTOM = 1;

    /**
     * 自定义数据权限(梳理员)
     */
    int DATA_TYPE_CARDING = 2;
    /**
     * 自定义数据权限(审批员)
     */
    int DATA_TYPE_EXAMINE = 3;


    /**
     * 正常状态
     */
    int STATUS_NORMAL = 0;

    /**
     * 禁用状态
     */
    int STATUS_DISABLE = -1;

    /**
     * 删除标志
     */
    int DEL_FLAG = 1;

    /**
     * 限流标识
     */
    String LIMIT_ALL = "XBOOT_LIMIT_ALL";

    /**
     * 页面类型权限
     */
    int PERMISSION_PAGE = 0;

    /**
     * 操作类型权限
     */
    int PERMISSION_OPERATION = 1;

    /**
     * 1级菜单父id
     */
    String PARENT_ID = "0";

    /**
     * 1级菜单
     */
    int LEVEL_ONE = 1;

    /**
     * 2级菜单
     */
    int LEVEL_TWO = 2;

    /**
     * 3级菜单
     */
    int LEVEL_THREE = 3;

    /**
     * 消息发送范围
     */
    int MESSAGE_RANGE_ALL = 0;

    /**
     * 未读
     */
    int MESSAGE_STATUS_UNREAD = 0;

    /**
     * 已读
     */
    int MESSAGE_STATUS_READ = 1;

    /**
     * github登录
     */
    int SOCIAL_TYPE_GITHUB = 0;

    /**
     * qq登录
     */
    int SOCIAL_TYPE_QQ = 1;

    /**
     * 微博登录
     */
    int SOCIAL_TYPE_WEIBO = 2;

    /**
     * 短信验证码key前缀
     */
    String PRE_SMS = "XBOOT_PRE_SMS:";

    /**
     * 邮件验证码key前缀
     */
    String PRE_EMAIL = "XBOOT_PRE_EMAIL:";

    /**
     * 本地文件存储
     */
    Integer OSS_LOCAL = 0;

    /**
     * 七牛云OSS存储
     */
    int OSS_QINIU = 1;

    /**
     * 阿里云OSS存储
     */
    int OSS_ALI = 2;

    /**
     * 腾讯云COS存储
     */
    int OSS_TENCENT = 3;
}
