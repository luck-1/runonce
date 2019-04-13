package com.runonce.model.base;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name="service_businessguidei_modle")
public class ServiceBusinessguideiModle {
    /**
     *
     */
    @Id
    @Column(name = "id")
    private String id;
    /**
     *  辦事指南ID
     */
    @Column(name = "businessGuideId")
    private String businessGuideId;
    /**
     *  服務主題ID
     */
    @Column(name = "serviceObjectId")
    private String serviceObjectId;

    public ServiceBusinessguideiModle(String businessGuideId,String serviceObjectId){
        this.businessGuideId = businessGuideId;
        this.serviceObjectId = serviceObjectId;
    }
    public ServiceBusinessguideiModle(String Id,String businessGuideId,String serviceObjectId){
        this(businessGuideId,serviceObjectId);
        this.setId(Id);
    }
}
