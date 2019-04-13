package com.runonce.service.impl.eventversionone;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mxgraph.canvas.mxGraphicsCanvas2D;
import com.mxgraph.canvas.mxICanvas2D;
import com.mxgraph.io.mxCodec;
import com.mxgraph.reader.mxSaxOutputHandler;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.util.png.mxPngEncodeParam;
import com.mxgraph.util.png.mxPngImageEncoder;
import com.mxgraph.view.mxGraph;
import com.runonce.ReturnCode;
import com.runonce.dao.eventversionone.DepartmentalMattersDao;
import com.runonce.dao.eventversionone.ProcessDescriptionPicDao;
import com.runonce.dao.eventversionone.VersionmanagementDao;
import com.runonce.exception.CustomException;
import com.runonce.httpbean.assets.reqbean.ProcessDescriptionPicReq;
import com.runonce.model.eventversionone.DepartmentalMatters;
import com.runonce.model.eventversionone.ProcessDescriptionPic;
import com.runonce.model.http.ProcessDataParam;
import com.runonce.service.AbstractService;
import com.runonce.service.eventversionone.ProcessDescriptionPicService;
import com.runonce.util.ImageUtil;
import com.runonce.util.OssClientUtil;
import com.runonce.util.SysUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLDecoder;
import java.util.*;
import java.util.List;

/**
 * Created by xuxueli on '2018-12-10 11:31:13'.
 */
@Service
public class ProcessDescriptionPicServiceImpl extends AbstractService<ProcessDescriptionPic> implements ProcessDescriptionPicService {

    @Resource
    private ProcessDescriptionPicDao processDescriptionPicDao;

    @Autowired
    private DepartmentalMattersServiceImpl departmentalMattersService;

    @Resource
    private VersionmanagementDao versionmanagementDao;

    @Resource
    private DepartmentalMattersDao departmentalMattersDao;

    private transient SAXParserFactory parserFactory = SAXParserFactory.newInstance();

    /**
     * Cache for all images.
     */
    protected transient Hashtable<String, Image> imageCache = new Hashtable<>();


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCode saveInfo(ProcessDescriptionPicReq processDescriptionPicReq, HttpServletRequest request) {
        // 事项存在校验
        String eventId = processDescriptionPicReq.getEventId();
        DepartmentalMatters event = departmentalMattersService.findById(eventId);
        if (event == null) {
            return new ReturnCode.Builder().failed().msg("该事项不存在").build();
        }
        //数据库中保存的版本
        Integer selectVersion = versionmanagementDao.getSheetVersion(eventId, 6);
        //前台得到版本号
        Integer version = processDescriptionPicReq.getVersion();

        List<ProcessDescriptionPic> processDescriptionPicList = processDescriptionPicReq.getProcessDescriptionPicList();
        processDescriptionPicDao.deleteByEventId(eventId);

        if (selectVersion.equals(version)) {
            processDescriptionPicList.forEach(processDescriptionPic -> {
                if (processDescriptionPic.getPicType() == 1) {
                    processDescriptionPic.setPicliu("");
                }

                String jsonString = JSON.toJSONString(processDescriptionPic.getConditionList());
                processDescriptionPic.setConditionTemp(jsonString);

                processDescriptionPic.setId(SysUtil.getUUID());

                processDescriptionPicDao.insert(processDescriptionPic);
            });
        } else {
            throw new CustomException(new ReturnCode.Builder().failed().msg("当前已有人在编辑，更改失败！").build());
        }
        //更新数据库中的版本号
        versionmanagementDao.updateSheetVersion(eventId, 6);
        // 事项相关状态修改
        event.setProcessMapState(processDescriptionPicReq.getState());
        departmentalMattersService.update(event);
        //获取事项id是否有父,更新父部门状态
        String departmentalMattersPId = departmentalMattersDao.selectPidById(eventId);
        departmentalMattersService.updateFatherState(departmentalMattersPId);

        return new ReturnCode.Builder().success().msg("保存流程图数据成功").build();
    }

    @Override
    public ProcessDescriptionPicReq findByEventId(String eventId) {
        List<ProcessDescriptionPic> processDescriptionPicList = processDescriptionPicDao.findByEventId(eventId);

        List<ProcessDescriptionPic> returnList = new ArrayList<>();
        ProcessDescriptionPicReq processDescriptionPicReq = new ProcessDescriptionPicReq();

        processDescriptionPicList.forEach(processDescriptionPic -> {

            List<String> conditionList = JSON.parseArray(processDescriptionPic.getConditionTemp(), String.class);
            processDescriptionPic.setConditionList(conditionList);
            returnList.add(processDescriptionPic);
        });

        processDescriptionPicReq.setProcessDescriptionPicList(returnList);
        processDescriptionPicReq.setVersion(versionmanagementDao.getSheetVersion(eventId, 6));
        processDescriptionPicReq.setState(departmentalMattersDao.selectByPrimaryKey(eventId).getProcessDescriptionState());

        return processDescriptionPicReq;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateByEventId(ProcessDescriptionPic ProcessDescriptionPic) {
        return processDescriptionPicDao.updateEvent(ProcessDescriptionPic);
    }

    /**
     * 根据xml生成png图片
     *
     * @param param
     * @return
     * @throws IOException
     */
    @Override
    public String generatePngByXml(ProcessDataParam param, HttpServletRequest request) throws IOException {
        System.setProperty("java.awt.headless", "false");
//		Document doc = mxXmlUtils.parseXml(param.getXmlData());
//		mxGraph graph = new mxGraph();
//		mxCodec codec = new mxCodec(doc);
//		codec.decode(doc.getDocumentElement(), graph.getModel());
//
//		mxGraphComponent graphComponent = new mxGraphComponent(graph);
//		BufferedImage image = mxCellRenderer.createBufferedImage(graphComponent.getGraph(), null, 1, Color.WHITE, graphComponent.isAntiAlias(), null, graphComponent.getCanvas());
        String url = request.getRequestURL().toString();
        BufferedImage image = null;
        try {
            image = writeImage(url, param.getFormat(), param.getFileName(), param.getWidth(), param.getHeight(), Color.white, param.getXmlData());
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        // 将图片转为inputStream
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "png", os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        // 上传到oss
        String filepath = OssClientUtil.uploadFileByInputStream(is, "", 1);
        os.close();
        is.close();
        return filepath;
    }

    /**
     * 根据xml生成图片
     *
     * @param param
     * @return
     * @throws IOException
     */
    @Override
    public String generatePng(ProcessDataParam param, HttpServletRequest request) throws IOException, ParserConfigurationException, SAXException {
        System.setProperty("java.awt.headless", "false");
//		Document doc = mxXmlUtils.parseXml(param.getXmlData());
//		mxGraph graph = new mxGraph();
//		mxCodec codec = new mxCodec(doc);
//		codec.decode(doc.getDocumentElement(), graph.getModel());
//
//		mxGraphComponent graphComponent = new mxGraphComponent(graph);
//		BufferedImage image = mxCellRenderer.createBufferedImage(graphComponent.getGraph(), null, 1, Color.WHITE, graphComponent.isAntiAlias(), null, graphComponent.getCanvas());
        String url = request.getRequestURL().toString();
        String xml = getRequestXml(param.getCodeedXmlString());
        BufferedImage image = null;
        image = writeImage(url, param.getFormat(), param.getFileName(), param.getWidth(), param.getHeight(), Color.white, xml);
        // 将图片转为inputStream
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "png", os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        // 上传到oss
        String filepath = OssClientUtil.uploadFileByInputStream(is, "", 1);
        os.close();
        is.close();
        return filepath;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByEventId(String EventId) {


        return processDescriptionPicDao.deleteByEventId(EventId);
    }

//	@Override
//	public ProcessDescriptionPic findBydmId(String dmId) {
//		ProcessDescriptionPic processDescriptionPic = processDescriptionPicDao.findByEventId(dmId);
//
////		Integer version = versionmanagementDao.getSheetVersion(dmId,6);
//		if (processDescriptionPic == null) {
//		    ProcessDescriptionPic processDescriptionPic1 = new ProcessDescriptionPic();
////		    processDescriptionPic1.setVersion(version);
//        }
////        processDescriptionPic.setVersion(version);
//		return null;
//	}

    protected BufferedImage writeImage(String url, String format, String fname, int w, int h, Color bg, String xml)
            throws IOException, SAXException, ParserConfigurationException {
        BufferedImage image = mxUtils.createBufferedImage(w, h, bg);

        if (image != null) {
            Graphics2D g2 = image.createGraphics();
            mxUtils.setAntiAlias(g2, true, true);
            renderXml(xml, createCanvas(url, g2));

//			if (fname != null)
//			{
//				response.setContentType("application/x-unknown");
//				response.setHeader("Content-Disposition", "attachment; filename=\"" + fname + "\"; filename*=UTF-8''" + fname);
//			}
//			else if (format != null)
//			{
//				response.setContentType("image/" + format.toLowerCase());
//			}
//
//			ImageIO.write(image, format, response.getOutputStream());
        }
        return image;
    }

    /**
     * Renders the XML to the given canvas.
     */
    protected void renderXml(String xml, mxICanvas2D canvas) throws SAXException, ParserConfigurationException, IOException {
        XMLReader reader = parserFactory.newSAXParser().getXMLReader();
        reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        reader.setFeature("http://xml.org/sax/features/external-general-entities", false);
        reader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        reader.setContentHandler(new mxSaxOutputHandler(canvas));
        reader.parse(new InputSource(new StringReader(xml)));
    }

    /**
     * Creates a graphics canvas with an image cache.
     */
    protected mxGraphicsCanvas2D createCanvas(String url, Graphics2D g2) {
        // Caches custom images for the time of the request
        final Hashtable<String, Image> shortCache = new Hashtable<String, Image>();
        final String domain = url.substring(0, url.lastIndexOf("/"));

        mxGraphicsCanvas2D g2c = new mxGraphicsCanvas2D(g2) {
            public Image loadImage(String src) {
                // Uses local image cache by default
                Hashtable<String, Image> cache = shortCache;

                // Uses global image cache for local images
                if (src.startsWith(domain)) {
                    cache = imageCache;
                }

                Image image = cache.get(src);

                if (image == null) {
                    image = super.loadImage(src);

                    if (image != null) {
                        cache.put(src, image);
                    } else {
                        cache.put(src, new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
                    }
                } else if (image == new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)) {
                    image = null;
                }

                return image;
            }
        };

        return g2c;
    }

    /**
     * Gets the XML request parameter.
     */
    protected String getRequestXml(String xmlData) throws IOException, UnsupportedEncodingException {
        // Decoding is optional (no plain text values allowed)
        if (xmlData != null && xmlData.startsWith("%3C")) {
            xmlData = URLDecoder.decode(xmlData, "UTF-8");
        }

        return xmlData;
    }

}
