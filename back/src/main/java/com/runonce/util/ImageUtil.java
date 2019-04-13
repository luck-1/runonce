package com.runonce.util;

import com.mxgraph.canvas.mxGraphicsCanvas2D;
import com.mxgraph.canvas.mxICanvas2D;
import com.mxgraph.reader.mxSaxOutputHandler;
import com.mxgraph.util.mxUtils;
import com.runonce.model.http.ProcessDataParam;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * @Author: zhaolei
 * @Descriptions: 图片工具类
 * @Date: create at 2019/1/2 0002 下午 1:46
 */
public class ImageUtil {
    private static transient SAXParserFactory parserFactory = SAXParserFactory.newInstance();

    /**
     * Cache for all images.
     */
    protected static transient Hashtable<String, Image> imageCache = new Hashtable<String, Image>();

    /**
     * 根据xml生成图片
     *
     * @param param
     * @return
     * @throws IOException
     */
    public static String generatePng(ProcessDataParam param, HttpServletRequest request) throws IOException, ParserConfigurationException, SAXException {
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
        image = writeImage(url, "png", param.getFileName(), param.getWidth(), param.getHeight(), Color.white, xml);
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

    protected static BufferedImage writeImage(String url, String format, String fname, int w, int h, Color bg, String xml)
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
    protected static void renderXml(String xml, mxICanvas2D canvas) throws SAXException, ParserConfigurationException, IOException {
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
    protected static mxGraphicsCanvas2D createCanvas(String url, Graphics2D g2) {
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
    protected static String getRequestXml(String xmlData) throws IOException, UnsupportedEncodingException {
        // Decoding is optional (no plain text values allowed)
        if (xmlData != null && xmlData.startsWith("%3C")) {
            xmlData = URLDecoder.decode(xmlData, "UTF-8");
        }

        return xmlData;
    }


}
