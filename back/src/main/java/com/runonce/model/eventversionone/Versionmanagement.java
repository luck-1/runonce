package com.runonce.model.eventversionone;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
*  事项sheet页版本管理
*
*  Created by xuxueli on '2019-01-28 15:06:51'.
*/
@Data
@Table(name="versionmanagement")
public class Versionmanagement implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
    * 
    */
    @Id
    @Column(name = "id")
    private String id;

    /**
    * 
    */
    @Column(name = "dmId")
    private String dmId;

    /**
    * 
    */
    @Column(name = "sheetNumber")
    private String sheetNumber;

    /**
    * 
    */
    @Column(name = "version")
    private int version;

    /**
    * 
    */
    @Column(name = "who")
    private String who;

    /**
    * 
    */
    @Column(name = "updateTime")
    private Date updateTime;


}