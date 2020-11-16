package core;

import com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.Arrays;

public class XMLUtils {
	public static JAXBContext getJAXBContext(Object obj) throws JAXBException {
		return JAXBContext.newInstance(obj.getClass());
	}

	/**
	 * 使用jaxb将对象转换为xml字符串
	 * @param obj
	 * @return
	 */
	public static String objToXML(Object obj) throws JAXBException {
		JAXBContext jaxbContext = getJAXBContext(obj);
		StringWriter writer = new StringWriter();
		Marshaller marshaller = jaxbContext.createMarshaller();
		//设置编码格式
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		//设置否是格式化xml
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		//是否省略头信息
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);
		marshaller.marshal(obj, writer);
		return writer.toString();
	}



	/**
	 * 使用jaxb将字符串转换为对象
	 * @param xmlStr
	 * @param
	 * @return
	 */
	public static <T> T xmlToObj(String xmlStr,Class<T> clazz) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader reader = new StringReader(xmlStr);
        T message = (T) unmarshaller.unmarshal(reader);
        return message;
	}

	/**
	 * 读取xml文档
	 *
	 * @author weiyang6
	 * @date   2019/12/4  21:09
	 * @return
	 * @exception /throws
	 */
	public static Document readDocument(String xmlPath) {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(xmlPath);
			SAXReader reader = new SAXReader();
			return reader.read(inputStream);
		} catch (Exception e) {
			throw new RuntimeException("读取xml文件异常", e);
		} finally {

		}
	}

	public static String attrValue(Node node, String key){
		Document document= DocumentHelper.createDocument();
		document.setContent(Arrays.asList(node));
		String value=document.getRootElement().attributeValue(key);
		if(value != null) {
			return value;
		}
		throw new RuntimeException("节点"+document.getName()+"的属性"+key+"不能为空");
	}

    @SuppressWarnings("unchecked")
    public static <T> T readString(Class<T> clazz, String path) {
        try {
            JAXBContext jc = JAXBContext.newInstance(clazz);
            Unmarshaller u = jc.createUnmarshaller();
            return (T) u.unmarshal(new FileInputStream(new File(path)));
        } catch (JAXBException | FileNotFoundException e) {
            throw new RuntimeException("读取xml文件异常, 文件路径: " + path);
        }
    }

	public static void main(String[] args) {
	}

}
