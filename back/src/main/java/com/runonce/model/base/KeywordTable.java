package com.runonce.model.base;
import java.io.Serializable;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
*  关键字
*
*  Created by xuxueli on '2019-02-27 14:10:47'.
*/
@Data
@Table(name="keyword_table")
public class KeywordTable implements Serializable {
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
    @Column(name = "name")
    private String name;

    /**
    * 
    */
    @Column(name = "count")
    private int count;


}