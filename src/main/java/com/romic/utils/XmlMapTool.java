package com.romic.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlMapTool {
    public static void main(String[] args) {
        String input = "<xml><SuiteId><![CDATA[tjb81a01aa758f05e4]]></SuiteId><AuthCorpId><![CDATA[wxde0c64cd89919990]]></AuthCorpId><InfoType><![CDATA[change_contact]]></InfoType><TimeStamp>1500543991</TimeStamp><ChangeType><![CDATA[update_user]]></ChangeType><UserID><![CDATA[shenrm]]></UserID><Name><![CDATA[沈荣明2]]></Name></xml>";
        Map<String, Object> map = xml2map(input);
        System.out.println("最终生成的map如下:\n" + map);
    }

    public static Map<String, Object> xml2map(String xml) {
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        if (doc == null)
            return map;
        Element rootElement = doc.getRootElement();
        element2map(rootElement, map);
        return map;
    }

    private static Map element2map(Element outele, Map<String, Object> outmap) {
        List<Element> list = outele.elements();//必定返回0,1,2,3,....... 不会异常
        int size = list.size();
        if (size == 0) {//当前节点是叶子节点
            outmap.put(outele.getName(), outele.getTextTrim());
        } else if (size == 1) {
            Map<String, Object> innermap = new HashMap<String, Object>();
            Element ele1 = list.get(0);
            element2map(ele1, innermap);
            outmap.put(outele.getName(), innermap);
        } else {
            Map<String, Object> innermap = new HashMap<String, Object>();
            for (Element ele1 : list) {
                String eleName = ele1.getName();
                Object obj = innermap.get(eleName);//获取MASTER
                if (obj == null) {//如果该MASTER不存在,现在有一个MASTER过来
                    element2map(ele1, innermap);
                } else {
                    if (obj instanceof Map) {//如果没有生成过list,把原来的单个map合并到新的list
                        innermap.remove(eleName);
                        List<Map> list1 = new ArrayList<Map>();
                        list1.add((Map) obj);
                        Map<String, Object> map1 = new HashMap<String, Object>();
                        element2map(ele1, map1);
                        list1.add((Map) map1.get(eleName));
                        innermap.put(eleName, list1);
                    } else if (obj instanceof List) {//如果已经生成过list
                        element2map(ele1, innermap);
                        ((List) obj).add(innermap);
                    }
                }
            }
            outmap.put(outele.getName(), innermap);
        }
        return outmap;
    }
}