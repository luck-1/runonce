package com.runonce.enums;

/**
 * @author swq
 * @date 2019/1/29 0029
 * @description
 */

public enum SheetNumEnum {


    BUSINESS_GUIDE(1, "办事指南") , MATERIAL_GROUPING(2,"材料分组") ,MATERIALS_SERVICE(2,"办事材料"),APPLICATION_TYPE_FIELD(3, "申请类型"), WORK_SITUATION(7, "办事情形");




    private Integer id;

    private String name;

    SheetNumEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }



}
