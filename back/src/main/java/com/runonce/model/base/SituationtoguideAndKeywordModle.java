package com.runonce.model.base;
import java.io.Serializable;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
*  情形引导关键字中间表
*
*  Created by xuxueli on '2019-02-27 14:10:47'.
*/
@Data
@Table(name="situationtoguide_and_keyword_modle")
public class SituationtoguideAndKeywordModle implements Serializable {
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
    @Column(name = "situationToGuideId")
    private String situationToGuideId;

    /**
    * 
    */
    @Column(name = "keywordId")
    private String keywordId;


}