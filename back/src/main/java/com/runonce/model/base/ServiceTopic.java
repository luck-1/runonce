package com.runonce.model.base;
import java.io.Serializable;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
*  
*
*  Created by xuxueli on '2018-12-11 09:37:24'.
*/
@Data
@Table(name="service_topic")
public class ServiceTopic implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
    * 
    */
    @Id
    @Column(name = "id")
    private String id;

    /**
    * 服务对象ID
    */
    @Column(name = "pid")
    private String pid;

    /**
    * 编码
    */
    @Column(name = "code")
    private String code;

    /**
    * 名称
    */
    @Column(name = "name")
    private String name;

    /**
    * 计数
    */
    @Column(name = "count")
    private int count;

    /**
    * 备注
    */
    @Column(name = "remarks")
    private String remarks;


}