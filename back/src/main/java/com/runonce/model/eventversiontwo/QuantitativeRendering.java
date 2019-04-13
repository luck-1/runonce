package com.runonce.model.eventversiontwo;
import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

/**
*  
*
*  Created by xuxueli on '2018-12-10 15:04:12'.
*/
@Data
@Table(name="quantitative_rendering")
public class QuantitativeRendering implements Serializable {
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
    @NotBlank(message = "事项id为空")
    @Column(name = "eventId")
    private String eventId;

    /**
    * 类型
    */
    @Column(name = "type")
    private String type;

    /**
    * 备注
    */
    @Column(name = "remarks")
    private String remarks;

    /**
     *  减材料说明
     */
//    @Length(max = 50,message = "减材料说明过长")
    @Column(name = "reductionMaterialDetail")
    private String reductionMaterialDetail;

    /**
     *  减材料问题
     */
    @Column(name = "reductionMaterialProblem")
    private String reductionMaterialProblem;

    /**
     * 减材料前材料数
     */
    @Column(name = "beforeMateriaReduction")
    private Integer beforeMateriaReduction;

    /**
     * 减材料后材料数
     */
    @Column(name = "afterReducingMaterial")
    private Integer afterReducingMaterial;

    /**
     *  减环节说明
     */
    @Column(name = "reductionLinkDetail")
    private String reductionLinkDetail;

    /**
     *  减环节问题
     */
    @Column(name = "reductionLinkProblem")
    private String reductionLinkProblem;

    /**
     *  精简前环节数
     */
    @Column(name = "beforeLinkReduction")
    private Integer beforeLinkReduction;

    /**
     *  精简后环节数
     */
    @Column(name = "afterLinkReduction")
    private Integer afterLinkReduction;

    /**
     * 减时效说明
     */
    @Column(name = "reductionTimeDetail")
    private String reductionTimeDetail;

    /**
     *  减时效问题
     */
    @Column(name = "reductionTimeProblem")
    private String reductionTimeProblem;

    /**
     *  减时效前业务平均办理时长
     */
    @Column(name = "beforeTimeReduction")
    private Double beforeTimeReduction;

    /**
     *  减时效后业务平均办理时长
     */
    @Column(name = "afterTimeReduction")
    private Double afterTimeReduction;

    /**
     *  总体效果说明
     */
    @Column(name = "overallEffectDetail")
    private String overallEffectDetail;

    /**
     * 总体问题
     */
    @Column(name = "overallProblem")
    private String overallProblem;

    /**
     *  总体效果(减材料、减环节、减时效的平均权重)
     */
    @Column(name = "overallEffect")
    private Double overallEffect;

    /**
     * 量化呈现图
     */
    @Column(name = "renderPicture")
    private String renderPicture;
    /**
     * 对接方式图
     */
    @Column(name = "dockingModePicture")
    private String dockingModePicture;
    /**
     * 量化呈现图类型(1.用户上传 2.工具绘制)
     */
    @Column(name = "quantitativePicType")
    private String quantitativePicType;
    /**
     * 量化呈现图xml数据
     */
    @Column(name = "quantitativePicData")
    private String quantitativePicData;
    /**
     * 系统对接图类型(1.用户上传 2.工具绘制)
     */
    @Column(name = "systemDockingPicType")
    private String systemDockingPicType;
    /**
     * 系统对接图xml数据
     */
    @Column(name = "systemDockingPicData")
    private String systemDockingPicData;
    /**
     * echarts图路径
     */
    @Column(name = "eChartsPicPath")
    private String eChartsPicPath;
    /**
     * 时间单位(工作日\小时\分钟)
     */
    @Column(name = "timeUnit")
    private String timeUnit;


    // 材料分组状态(1. 未完善 2.已完善)
    @Transient
    private Integer state;




    public String geteChartsPicPath() {
        return eChartsPicPath;
    }

    public void seteChartsPicPath(String eChartsPicPath) {
        this.eChartsPicPath = eChartsPicPath;
    }
}