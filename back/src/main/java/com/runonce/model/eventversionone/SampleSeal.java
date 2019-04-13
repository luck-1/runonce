package com.runonce.model.eventversionone;
import java.io.Serializable;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
*  
*
*  Created by xuxueli on '2018-12-11 09:31:18'.
*/
@Data
@Table(name="sample_seal")
public class SampleSeal implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
    * id
    */
    @Id
    @Column(name = "id")
    private String id;

    /**
    * 图例名称
    */
    @Column(name = "legendName")
    private String legendName;

    /**
    * 事项id
    */
    @Column(name = "eventId")
    private String eventId;


}