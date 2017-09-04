package com.sky.demo.utils.pending;

import com.sky.demo.ui.tree.FileBean;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LiBin
 * @Description: TODO
 * @date 2015/9/30 10:41
 */
public class XmlParser {
    public XmlParser() {
    }

    //    public static final <T> T parseObject(String text, Class<T> clazz) {
    //    public <T> List<T> xmlPullParser(InputStream stream,Class<T> clazz) {
    public List<FileBean> xmlPullParser(InputStream stream) {
        List<FileBean> datas = new ArrayList<>();

        FileBean data = null;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xml = factory.newPullParser();
            xml.setInput(stream, "UTF-8");
            int type = xml.getEventType();
            while (type != XmlPullParser.END_DOCUMENT) {
                switch (type) {
//                    <Province ID="1" ProvinceName="北京市">北京市</Province>
//                    <City ID="1" CityName="北京市" PID="1" ZipCode="100000">北京市</City>
//                    <District ID="1" DistrictName="东城区" CID="1">东城区</District>
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:
                        if (xml.getName().equals("Province")) {
                            data = new FileBean();
//                            LogUtils.i("getAttributeName=" + xml.getAttributeValue(null, "ID"));
//                            LogUtils.i("getAttributeName=" + xml.getAttributeValue(null, "ProvinceName"));
                            data.setId(xml.getAttributeValue(0));
                            data.setName(xml.getAttributeValue(1));
                            data.setpId("0");
                        } else if (xml.getName().equals("City")) {
                            data = new FileBean();
                            data.setId(Integer.parseInt(xml.getAttributeValue(0)) + 34 + "");
                            data.setName(xml.getAttributeValue(1));
                            data.setpId(xml.getAttributeValue(2));
                        } else if (xml.getName().equals("District")) {
                            data = new FileBean();
                            data.setId(Integer.parseInt(xml.getAttributeValue(0)) + 34 + 345 + "");
                            data.setName(xml.getAttributeValue(1));
                            data.setpId(Integer.parseInt(xml.getAttributeValue(2)) + 34 + "");
                        }

//                        if (xml.getName().equals("RECORD")) {
//                            type = xml.next();
//                            LogUtils.i("RECORD=" + xml.getText());
//                        } else if (xml.getName().equals("id")) {
//                            type = xml.next();
//                            LogUtils.i("ID=" + xml.getText());
//                        } else if (xml.getName().equals("name")) {
//                            type = xml.next();
//                            LogUtils.i("anem=" + xml.getText());
//                        } else if (xml.getName().equals("prov_id")) {
//                            type = xml.next();
//                            LogUtils.i("prov_id=" + xml.getText());
//                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (data != null) {
                            datas.add(data);
                            data = null;
                        }
                        break;
                }
                type = xml.next();
//                LogUtils.i("type=" + type);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return datas;
    }
}