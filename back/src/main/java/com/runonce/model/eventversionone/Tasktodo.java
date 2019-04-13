package com.runonce.model.eventversionone;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
*  taskToDo
*
*  Created by xuxueli on '2019-02-21 11:20:11'.
*/
@Data
@Table(name="tasktodo")
public class Tasktodo implements Serializable {
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
    @Column(name = "type")
    private Integer type;

    /**
    * 
    */
    @Column(name = "content")
    private String content;

    /**
    * 
    */
    @Column(name = "createTime")
    private Date createTime;

    /**
    * 
    */
    @Column(name = "updateTime")
    private Date updateTime;

    /**
    * 
    */
    @Column(name = "who")
    private String who;

    @Column(name = "state")
    private Integer state;


}