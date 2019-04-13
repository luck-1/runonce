package com.runonce.model.eventversiontwo;
import java.io.Serializable;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
*  
*
*  Created by xuxueli on '2018-12-11 09:33:34'.
*/
@Data
@Table(name="work_situation")
public class WorkSituation implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
    * 
    */
    @Id
    @Column(name = "id")
    private String id;

    /**
    * 事项id
    */
    @Column(name = "eventId")
    private String eventId;

    /**
    * 父级id
    */
    @Column(name = "pid")
    private String pid;

    /**
    * 名称
    */
    @Column(name = "name")
    private String name;


}