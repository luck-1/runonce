package com.runonce.model.eventversionone;
import java.io.Serializable;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
*  
*
*  Created by xuxueli on '2018-12-10 12:17:30'.
*/
@Data
@Table(name="process_description")
public class ProcessDescription implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
    * id
    */
    @Id
    @Column(name = "id")
    private String id;

    /**
    * 流程环节描述
    */
    @Column(name = "describetion")
    private String describetion;

    /**
    * 备注
    */
    @Column(name = "remarks")
    private String remarks;

   /**
    * 事项id
    */
   @NotBlank(message = "事项id不能为空")
    @Column(name = "eventId")
    private String eventId;
    /**
     * 序号
     */
    @Column(name = "itemNumber")
    private String itemNumber;
    /**
    * 
    */
//    @Column(name = "primary")
//    private Object primary;
}