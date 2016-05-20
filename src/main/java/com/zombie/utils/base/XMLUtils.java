package com.zombie.utils.base;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

/**
 * 解析XML
 */

public class XMLUtils {
    public static void main(String[] args) {
        File classPathRoot = null;
        try {
            classPathRoot = new File(XMLUtils.class.getClassLoader().getResource("ApplicationMessageTest.xml").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        // System.out.println(classPathRoot.getPath());
        Document document = null;
        SAXReader reader = new SAXReader();
        try {
            document = reader.read(classPathRoot);
            //Node rootNode = document.selectSingleNode("/");
            Element root = document.getRootElement();
            List<Element> testcases = root.elements();
            Iterator<Element> iterator = testcases.iterator();
            while (iterator.hasNext()) {
                List<Attribute> attributes = iterator.next().attributes();
                Iterator<Attribute> it = attributes.iterator();
                while (it.hasNext()) {
                    Attribute attribute = it.next();
                    System.out.println(attribute.getName() + "=" + attribute.getValue());
                }
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
