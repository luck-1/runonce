package com.runonce.model.eventpublic;
import java.io.Serializable;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
*  
*
*  Created by xuxueli on '2018-12-11 09:36:41'.
*/
@Data
@Table(name="business_file_relation")
public class BusinessFileRelation implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
    * id
    */
    @Id
    @Column(name = "id")
    private String id ;

    /**
     * 业务类型
     */
    @Column(name = "businessType")
    private int businessType;

    /**
    * 业务id
    */
    @Column(name = "businessId")
    private String businessId;

    /**
    * 文件名称
    */
    @Column(name = "fileName")
    private String fileName;

    /**
    * 文件路径
    */
    @Column(name = "filePath")
    private String filePath;

}