package com.runonce.model.eventversionone;
import java.io.Serializable;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
*  
*
*  Created by xuxueli on '2019-01-04 10:40:30'.
*/
@Data
@Table(name="default_system_docking_pic")
public class DefaultSystemDockingPic implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
    * 
    */
    @Id
    @Column(name = "id")
    private String id;

    /**
    * 排序序号
    */
    @Column(name = "index")
    private Integer index;

    /**
    *  对接图名称
    */
    @Column(name = "dockingName")
    private String dockingName;

    /**
    *  对接图路径
    */
    @Column(name = "dockingPicName")
    private String dockingPicName;

    /**
    *  描述
    */
    @Column(name = "remarks")
    private String remarks;


}