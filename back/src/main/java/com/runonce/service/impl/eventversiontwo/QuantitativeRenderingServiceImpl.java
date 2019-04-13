package com.runonce.service.impl.eventversiontwo;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;

import com.mxgraph.io.mxCodec;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;
import com.runonce.ReturnCode;
import com.runonce.dao.eventversionone.DepartmentalMattersDao;
import com.runonce.dao.eventversionone.VersionmanagementDao;
import com.runonce.exception.CustomException;
import com.runonce.httpbean.assets.reqbean.QuantitativeRenderingReq;
import com.runonce.model.eventversionone.DepartmentalMatters;
import com.runonce.model.eventversionone.Versionmanagement;
import com.runonce.model.http.ProcessDataParam;
import com.runonce.service.impl.eventversionone.DepartmentalMattersServiceImpl;
import com.runonce.util.ImageUtil;
import com.runonce.util.OssClientUtil;
import com.runonce.util.SysUtil;
import com.runonce.util.WebTokenUtil;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.runonce.service.eventversiontwo.QuantitativeRenderingService;
import com.runonce.dao.eventversiontwo.QuantitativeRenderingDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.eventversiontwo.QuantitativeRendering;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
*
*
* Created by xuxueli on '2018-12-10 15:04:12'.
*/
@Service
public class QuantitativeRenderingServiceImpl extends AbstractService<QuantitativeRendering> implements QuantitativeRenderingService  {

	@Resource
	private QuantitativeRenderingDao quantitativeRenderingDao;

	@Autowired
	private DepartmentalMattersServiceImpl departmentalMattersService;

	@Resource
	private VersionmanagementDao versionmanagementDao;

	@Autowired
	private QuantitativeRenderingService quantitativeRenderingService;

	@Resource
	private DepartmentalMattersDao departmentalMattersDao;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ReturnCode saveInfo(QuantitativeRendering model, Integer version, HttpServletRequest request){
		// 事项存在校验
		DepartmentalMatters event = departmentalMattersService.findById(model.getEventId());
		if(event == null){
			return new ReturnCode.Builder().failed().msg("该事项不存在").build();
		}

//		QuantitativeRendering quantitativeRendering = quantitativeRenderingService.findBydmId(model.getEventId());
		//数据库中保存的版本
		Integer selectVersion = versionmanagementDao.getSheetVersion(model.getEventId(),9);
		if(selectVersion == null ){
			throw new CustomException(new ReturnCode.Builder().failed().msg("未知错误：未查到当前版本！").build());
		}
		if (model.getId() == null || model.getId().equals("")) {
			//新增时 是否为默认版本号
			//if(version == 1){
//              //删除原有的
                Condition condition = new Condition(QuantitativeRendering.class);
                Example.Criteria criteria = condition.createCriteria();
                criteria.andEqualTo("eventId",model.getEventId());
                mapper.deleteByCondition(condition);
                // 新增
                model.setId(SysUtil.getUUID());
                mapper.insert(model);
			//}else{
			//	throw new CustomException(new ReturnCode.Builder().failed().msg("当前已有人在编辑，更改失败！").build());
			//}
		}else {
			//版本号相同则修改，不相同则回滚
			if (version.equals(selectVersion)) {
				quantitativeRenderingDao.updateByPrimaryKey(model);
			}else {
                throw new CustomException(new ReturnCode.Builder().failed().msg("当前已有人在编辑，更改失败！").build());
			}
		}
        //更新数据库中的版本号
        versionmanagementDao.updateSheetVersion(model.getEventId(),9);

		//更新修改用户
		event.setOperator(WebTokenUtil.getUserByWebToken(request));
		// 更新事项相关状态
		event.setPresentQuantitativeState(model.getState());
		departmentalMattersService.update(event);
		//获取事项id 是否有父
		String departmentalMattersPId = departmentalMattersDao.selectPidById(event.getId());
		//更新父部门状态
		departmentalMattersService.updateFatherState(departmentalMattersPId);
		return new ReturnCode.Builder().success().msg("量化呈现保存成功").object(model).build();
	}




	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update (QuantitativeRendering model){
		mapper.updateByPrimaryKey(model);
		// 更新事项相关状态
		DepartmentalMatters event = departmentalMattersService.findById(model.getEventId());
		event.setPresentQuantitativeState(model.getState());
		departmentalMattersService.update(event);
	}

	@Override
	public String generatePngByXml(ProcessDataParam param, HttpServletRequest request) throws IOException, ParserConfigurationException, SAXException {
//		System.setProperty("java.awt.headless", "false");
//		Document doc = mxXmlUtils.parseXml(param.getCodeedXmlString());
//		mxGraph graph = new mxGraph();
//		mxCodec codec = new mxCodec(doc);
//		codec.decode(doc.getDocumentElement(), graph.getModel());
//
//		mxGraphComponent graphComponent = new mxGraphComponent(graph);
//		BufferedImage image = mxCellRenderer.createBufferedImage(graphComponent.getGraph(), null, 1, Color.WHITE, graphComponent.isAntiAlias(), null, graphComponent.getCanvas());
//
//		// 将图片转为inputStream
//		ByteArrayOutputStream os = new ByteArrayOutputStream();
//		ImageIO.write(image, "png", os);
//		InputStream is = new ByteArrayInputStream(os.toByteArray());
//		// 上传到oss
//		String filepath = OssClientUtil.uploadFileByInputStream(is,param.getFileName(),2);
//		os.close();
//		is.close();
		String filepath = ImageUtil.generatePng(param,request);
		return filepath;
	}

	@Override
	public QuantitativeRenderingReq findBydmId(String dmId) {

		//查询版本
		Integer version = versionmanagementDao.getSheetVersion(dmId,9);
		if(version == null ){
			throw new CustomException(new ReturnCode.Builder().failed().msg("未知错误：未查到当前版本！").build());
		}
		QuantitativeRenderingReq quantitativeRenderingReq = new QuantitativeRenderingReq();
		QuantitativeRendering quantitativeRendering = quantitativeRenderingDao.findBydmId(dmId);

		quantitativeRenderingReq.setQuantitativeRendering(quantitativeRendering);
		quantitativeRenderingReq.setVersion(version);

		return quantitativeRenderingReq;
	}
}
