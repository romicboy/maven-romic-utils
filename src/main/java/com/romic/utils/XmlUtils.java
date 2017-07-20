package com.romic.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.*;


public class XmlUtils {
    private static final String HEAD = "head";
    private static final String BODY = "body";


    public static void main(String[] args) throws DocumentException {
        String xml = "<ap>   <head>      <tr_code>330003</tr_code>      <corp_no>8000500436</corp_no>      <user_no>22222</user_no>      <req_no>123456789001</req_no>      <tr_acdt>20160510</tr_acdt>      <tr_time>151238</tr_time>      <atom_tr_count>1</atom_tr_count>      <channel>0</channel>      <reserved />   </head>   <body>      <cert_no>TEST01234</cert_no>      <pay_acno>310066865018010123060</pay_acno>      <type>S</type>      <sum>1</sum>      <sum_amt>0.01</sum_amt>      <pay_month>201605</pay_month>      <summary>测试0.01</summary>      <busi_no>3100012379</busi_no>      <selsec_flg>0</selsec_flg><tran><rcd><card_no>00000001</card_no><acname>测试账号</acname><card_flag>0</card_flag><amt>21.01</amt><busino>111111111</busino></rcd><rcd><card_no>00000001</card_no><acname>测试账号</acname><card_flag>0</card_flag><amt>21.01</amt><busino>111111111</busino></rcd></tran>   </body></ap>";
        System.out.println(parseXmlStr(xml).toString());
    }


    /**
     * 解析XML字符串
     *
     * @param xml
     * @return
     * @throws DocumentException
     */
    private static Map<String, Object> parseXmlStr(String xml)
            throws DocumentException {
        Document document = DocumentHelper.parseText(xml);
        Element root = document.getRootElement();
        Map<String, Object> stringObjectMap = parseElement(root);
        return stringObjectMap;
    }


    /**
     * 解析Element
     *
     * @param root
     * @return
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> parseElement(Element root) {
        String rootName = root.getName();
        Iterator<Element> rootItor = root.elementIterator();
        Map<String, Object> rMap = new HashMap<String, Object>();
        List<Map<String, Object>> rList = new ArrayList<Map<String, Object>>();
        Map<String, Object> rsltMap = null;
        while (rootItor.hasNext()) {
            Element tmpElement = rootItor.next();
            String name = tmpElement.getName();
            if (rsltMap == null || (!HEAD.equals(name) && !BODY.equals(name)
                    && !tmpElement.isTextOnly())) {
                if (!HEAD.equals(name) && !BODY.equals(name)
                        && !tmpElement.isTextOnly() && rsltMap != null) {
                    rList.add(rsltMap);
                }
                rsltMap = new HashMap<String, Object>();
            }
            if (!tmpElement.isTextOnly()) {
                Iterator<Element> headItor = tmpElement.elementIterator();
                while (headItor.hasNext()) {
                    Element hElement = headItor.next();
                    if (hElement.isTextOnly()) {
                        rsltMap.put(hElement.getName(), hElement.getTextTrim());
                    } else {
                        rsltMap.putAll(parseElement(hElement));
                    }
                }
            }
        }
        rList.add(rsltMap);
        rMap.put(rootName, rList);
        return rMap;
    }


}