package com.runonce.model.common;

/**
 * @author swq
 * @date 2018/12/20 0020
 * @description
 */
public class SystemBean {
    /**
     * 模板文件位置
     */
    public static final String EXPORT_LOCAL = "/excel";


    /**
     * 文件位置
     */
    public static final String DOWNLOAD_LOCAL = "/static/untangle/";


    /**
     * 导出格式
     */
    public static final String EXPORT_FORMAT_XLS = ".xls";
    /**
     * 导出格式
     */
    public static final String EXPORT_FORMAT_XLSX = ".xlsx";


    /**
     * 审核
     */
    public static final int DEPT_APPROVAL = 1;
    public static final int EXAMINE_APPROVAL = 2;

    /**
     * 审核状态
     * 1.待领导确认
     * 2.部门领导拒绝
     * 3.部门领导同意
     * 4.待发起评审
     * 5.部门领导发起评审
     * 6.待评审
     * 7.评审通过
     * 8.评审拒绝
     * 9.最终通过
     */
    public static final Integer WAIT_DEPT_CONFIRM = 1;
    public static final Integer DEPT_LEADER_REFUSE = 2;
    public static final Integer DEPT_LEADER_AGREE = 3;
    public static final Integer WAIT_REVIEW = 6;
    public static final Integer REVIEW_AGREE = 7;
    public static final Integer REVIEW_REFUSE = 8;


    //问题（评论状态）
    /**
     * 待梳理员回复
     */
    public static final Integer QUESTION_WAIT = 0;
    /**
     * 梳理员拒绝
     */
    public static final Integer QUESTION_REFUSE = 1;

    /**
     * 沟通中
     */
    public static final Integer QUESTION_COMMUNICATEING = 2;

    /**
     * 已确认
     */
    public static final Integer QUESTION_AGREE = 3;


    //审批中状态
    /**
     * 确认
     */
    public static final Integer MATTER_PROPOSAL_AGREE = 1;

    /**
     * 拒绝
     */
        public static final Integer MATTER_PROPOSAL_REFUSE = 2;
    /**
     * 待审批
     */
    public static final Integer MATTER_PROPOSAL_WAIT = 3;



    //省市区

    /**
     * 省
     */
    public static final String REGION_PROVINCE = "1";

    /**
     * 市
     */
    public static final String REGION_CITY = "2";
    /**
     * 区
     */
    public static final String REGION_AREA = "3";

}
