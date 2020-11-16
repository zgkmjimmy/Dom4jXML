package core;

import java.net.URL;
import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XmlParseUtil {

    @SuppressWarnings("rawtypes")
    public static void parseXml(String xmlPath){
        SAXReader saxReader=new SAXReader();
        Document document=null;
        try {
            document=saxReader.read(xmlPath);
            //获取根节点
            Element elementRoot=document.getRootElement();
            System.out.println(document);
            System.out.println("根节点名称："+elementRoot.getName());
            //遍历根节点下面的节点
            for(Iterator elementIterator=elementRoot.elementIterator();elementIterator.hasNext();){
                Element element=(Element)elementIterator.next();
                //获取节点的信息，，包括节点的名称以及节点的属性信息
                System.out.println("节点："+element.getName());
                for (Iterator attrIterator = element.attributeIterator(); attrIterator.hasNext();) {
                    Attribute attr=(Attribute)attrIterator.next();
                    //节点的属性信息
                    System.out.println("节点的属性名称为："+attr.getName()+",值为："+attr.getValue());

                }
                //获取节点下的子节点
                for (Iterator subElementiterator = element.elementIterator(); subElementiterator.hasNext();) {
                    Element subElement=(Element)subElementiterator.next();
                    //获取子节点的信息，包括子节点的名称以及子节点的属性信息
                    System.out.println("子节点："+subElement.getName());
                    for (Iterator subElementAttrIterator = subElement.attributeIterator(); subElementAttrIterator.hasNext();) {
                        Attribute subElementAttr=(Attribute)subElementAttrIterator.next();
                        //子节点的属性信息
                        System.out.println("子节点的属性名称为："+subElementAttr.getName()+",值为："+subElementAttr.getValue());

                    }

                }
            }
        } catch (Exception e) {
        }
    }
}