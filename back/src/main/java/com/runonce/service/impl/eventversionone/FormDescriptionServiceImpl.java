package com.runonce.service.impl.eventversionone;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.aliyun.oss.OSSClient;
import com.baidu.aip.ocr.AipOcr;
import com.runonce.ReturnCode;
import com.runonce.dao.eventversionone.*;
import com.runonce.exception.CustomException;
import com.runonce.model.eventversionone.DepartmentalMatters;
import com.runonce.model.eventversionone.MaterialGrouping;
import com.runonce.model.http.FormDescriptionParam;
import com.runonce.model.http.FormThatParam;
import com.runonce.model.http.FormThatStateParm;
import com.runonce.model.http.PdfToImageModel;
import com.runonce.service.eventversionone.DepartmentalMattersService;
import com.runonce.util.OssClientUtil;
import com.runonce.util.PdfToImage;
import com.runonce.util.SysUtil;
import com.runonce.util.WebTokenUtil;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import com.runonce.service.eventversionone.FormDescriptionService;
import com.runonce.service.AbstractService;
import  com.runonce.model.eventversionone.FormDescription;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 表单说明
 *
 * Created by xuxueli on '2018-12-18 13:17:37'.
 */
@Service
public class FormDescriptionServiceImpl extends AbstractService<FormDescription> implements FormDescriptionService  {

    @Resource
    private FormDescriptionDao formDescriptionDao;

    @Resource
    private DepartmentalMattersDao departmentalMattersDao;

    @Resource
    private DepartmentalMattersService departmentalMattersService;

    @Resource
    private VersionmanagementDao versionmanagementDao;

    @Resource
    private PdfToImageModelDao pdfToImageModelDao;

    /**
     * 保存
     * @param formDescriptionParam
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCode add(FormDescriptionParam formDescriptionParam, HttpServletRequest request){
        //获取对象信息
        List<FormDescription> list1 = formDescriptionParam.getList();
        String materialId = formDescriptionParam.getMaterialId();
        String eventId = formDescriptionParam.getEventId();

        //事项取消分配情况
        DepartmentalMatters departmentalMatters = departmentalMattersDao.findState(eventId);
        if(departmentalMatters == null){
            new ReturnCode.Builder().failed().msg("该事项不存在").build();
        }

        //数据库中存储的版本号
        Integer version1 = versionmanagementDao.getSheetVersion(eventId,8);
        //事项复制时初始化
        if(version1 == null){
            version1 = 1;
        }
        //前端得到的版本号（点击配置按钮时的版本）
        Integer version2 = formDescriptionParam.getVersion();
        if(version2 == null){
            version2 = version1;
        }
        formDescriptionParam.setVersion(version2);

        if(version1.equals(version2)){

        //删除表中记录
        formDescriptionDao.deleteByMaterialId(materialId);
        if(list1 != null && list1.size() > 0) {

            //不为空，单个保存表单说明
            list1.forEach(iterator -> {
                iterator.setId(SysUtil.getUUID());
                formDescriptionDao.insert(iterator);
            });
        }
        }else{
            throw new CustomException(new ReturnCode.Builder().failed().msg("当前已有人在编辑，更改失败！").build());
        }
        //更新状态
        departmentalMatters.setFormThatState(formDescriptionParam.getState());
        //更新修改用户
        departmentalMatters.setOperator(WebTokenUtil.getUserByWebToken(request));
        departmentalMattersDao.updateByPrimaryKey(departmentalMatters);
        //更新数据库中的版本号
        versionmanagementDao.updateSheetVersion(eventId,8);
        //获取事项id 是否有父
        String departmentalMattersPId = departmentalMattersDao.selectPidById(formDescriptionParam.getEventId());
        //更新父部门状态
        departmentalMattersService.updateFatherState(departmentalMattersPId);
        return new ReturnCode.Builder().success().msg("表单说明保存成功").object(formDescriptionParam).build();
    }

    /**
     * 显示所有申请表图片
     * @param eventId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public FormThatStateParm findPhoto1(String eventId) {

        DepartmentalMatters departmentalMatters = departmentalMattersDao.findState(eventId);
        //取消分配
        if(departmentalMatters == null){
            throw new CustomException(new ReturnCode.Builder().failed().msg("当前事项不存在！").build());
        }
        FormThatStateParm formThatStateParm = new FormThatStateParm();

        //返回表单说明版本
        Integer version = versionmanagementDao.getSheetVersion(eventId,8);
        if(version == null ){
            throw new CustomException(new ReturnCode.Builder().failed().msg("未知错误：未查到当前版本！").build());
        }
        formThatStateParm.setVersion(version);

        List<MaterialGrouping> list = formDescriptionDao.findApplicationForm(eventId);
        //pdf转图片
        formThatStateParm.setPdfList(getImageForPdf(list));

        Integer state = departmentalMatters.getFormThatState();
        formThatStateParm.setList(list);
        formThatStateParm.setState(state);
        return formThatStateParm;
    }

    /**
     * pdf文件转image
     * @param list
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<PdfToImageModel> getImageForPdf(List<MaterialGrouping> list){
        OSSClient ossClient = OssClientUtil.getOssClient();
        List<PdfToImageModel> pdfList = new ArrayList<>();

        for (int j = 0; j < list.size(); j++) {
            //imgs  String  JSON
            String sampleTablesSource = list.get(j).getSampleTable();
            //申请表样表
            List<String> sampleTableList = OssClientUtil.getFileName(sampleTablesSource);
            if(sampleTableList == null || sampleTableList.size() == 0){
                continue;
            }
            for (int i = 0; i <sampleTableList.size() ; i++) {
                //样表名
                String sampleTableName = sampleTableList.get(i);
                if("pdf".equals(PdfToImage.getSuffix(sampleTableName))){
                    //从原文件名查询，存在就不转化
                    PdfToImageModel pdfToImageModel = pdfToImageModelDao.findBySourceName(sampleTableList.get(i));
                    //不存在则转化，存在直接取
                    if(pdfToImageModel == null){
                        //OSS得到pdf文件的流
                        InputStream inputStream = OssClientUtil.getObjectForInputStream(ossClient,sampleTableName);
                        if(inputStream != null){
                            //单个PDF转化得到多页图片
                            List<byte[]> imageByteList = PdfToImage.pdf2png(inputStream);

                            List<String> imageNames = new ArrayList<>();
                            if( !(imageByteList == null || imageByteList.size() == 0) ){
                                imageByteList.forEach(imageByte -> {
                                    //OSS文件上传，返回上传后的文件名
                                    String fileName = OssClientUtil.uploadFileByInputStream(new ByteArrayInputStream(imageByte),"",0);
                                    imageNames.add(fileName);
                                });
                                //设置属性
                                pdfToImageModel = new PdfToImageModel();
                                pdfToImageModel.setMaterialId(list.get(j).getId());
                                pdfToImageModel.setId(SysUtil.getUUID());
                                pdfToImageModel.setSourceName(sampleTableList.get(i));

                                Map<String,List<String>> map = new HashMap<>();
                                map.put("imgs",imageNames);
                                JSONObject jsonObject = new JSONObject(map);
                                pdfToImageModel.setImageName(jsonObject.toString());
                                pdfList.add(pdfToImageModel);
                                //保存到数据库
                                pdfToImageModelDao.insert(pdfToImageModel);
                            }
                        }
                    }else{
                        pdfList.add(pdfToImageModel);
                    }
                }
            }
        }
        ossClient.shutdown();
        return pdfList;
    }

    /**
     * 查询表单说明
     *
     * @return
     */
    @Override
    public List<FormDescription> findMaterial(FormThatParam formThatParam){
        String eventId = formThatParam.getEventId();
        String materialId = formThatParam.getMaterialId();
        List<FormDescription> list;

        //材料ID不存在，查询所有材料
        if(materialId == null || "".equals(materialId)){
            list = formDescriptionDao.findAllMaterialByEventId(eventId);
        }else{
            //材料ID存在，查询单个材料
            list = formDescriptionDao.findMaterial(materialId);
        }
        return list;
    }

    /**
     * 显示图片文字
     * @param objectName
     * @return
     */
    @Override
    public JSONObject showPictureText(String objectName){
        //登录客户端对象
        OSSClient ossClient =  OssClientUtil.getOssClient();
        //得到文件字符数组
        byte [] bytes = new byte[0];

        try {
            bytes = OssClientUtil.getObjectForByte(ossClient,objectName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (bytes == null || bytes.length == 0){
            return null;
        }
        AipOcr ocr = OssClientUtil.getAipOcr();
        //获取图片文字
        JSONObject res = ocr.basicGeneral(bytes,new HashMap<>());
        ossClient.shutdown();
        return res;
    }
}