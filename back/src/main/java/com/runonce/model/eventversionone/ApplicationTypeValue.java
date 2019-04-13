package com.runonce.model.eventversionone;
import java.io.Serializable;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
*  
*
*  Created by xuxueli on '2018-12-10 12:17:29'.
*/
@Data
@Table(name="application_type_value")
public class ApplicationTypeValue implements Serializable {
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

    /**
    * 
    */
    @Transient
    @Column(name = "primary")
    private Object primary;


}