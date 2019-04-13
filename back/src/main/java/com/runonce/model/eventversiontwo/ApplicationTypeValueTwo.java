package com.runonce.model.eventversiontwo;
import java.io.Serializable;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
*  
*
*  Created by xuxueli on '2018-12-10 15:04:12'.
*/
@Data
@Table(name="application_type_value_two")
public class ApplicationTypeValueTwo implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
    * id
    */
    @Id
    @Column(name = "id")
    private String id;

    /**
    * 材料id
    */
    @Column(name = "materiaId")
    private String materiaId;

    /**
    * 字段id
    */
    @Column(name = "fieldId")
    private String fieldId;

    /**
    * 字段值
    */
    @Column(name = "value")
    private Object value;


}