package core;

import com.google.common.io.Resources;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.*;

public  class main {
    public static void main(String[] args) throws DocumentException, IOException {
        File dir = new File("D:/item");

        File[] files = dir.listFiles();
        //遍历item文件夹下所有文件夹
        LinkedList<File> dirList = new LinkedList<File>();

        for(File file : files){
            if(file.isDirectory()){
                dirList.add(file);
            }
        }


        for(File directory : dirList){
            //创建五个文件夹
            List<String> directoryList=new ArrayList();
            String directory1=directory+"/LSRA";
            String directory2=directory+"/LSRB";
            String directory3=directory+"/LSRC";
            String directory4=directory+"/LSRD";
            String directory5=directory+"/LSRE";
            directoryList.add(directory1);
            directoryList.add(directory2);
            directoryList.add(directory3);
            directoryList.add(directory4);
            directoryList.add(directory5);
            for (String name : directoryList) {
                File directoryName = new File(name);
                if (!directoryName.exists()) {
                    directoryName.mkdirs();
                }
            }

            String directroyBA=directory+"/LSRBA/item.xml";

            List<LinkedHashMap<String, String>> list = xmltojson(directroyBA);

            for (int i=0;i<list.size();i++){
                //System.out.println(list.get(i).get("modelAudio"));
                String modelAudio=list.get(i).get("modelAudio");
                String oldPath=directory+"/LSRBA/"+modelAudio;
                String newPath="";
                String fileName="";
                if(i==0){
                     newPath=directory+"/LSRA/"+modelAudio;
                     fileName=directory+"/LSRA/item.xml";
                }else if(i==1){
                     newPath=directory+"/LSRB/"+modelAudio;
                     fileName=directory+"/LSRB/item.xml";
                }else if(i==2){
                     newPath=directory+"/LSRC/"+modelAudio;
                     fileName=directory+"/LSRC/item.xml";
                }else if(i==3){
                     newPath=directory+"/LSRD/"+modelAudio;
                     fileName=directory+"/LSRD/item.xml";
                }else if(i==4){
                     newPath=directory+"/LSRE/"+modelAudio;
                     fileName=directory+"/LSRE/item.xml";
                }

                copyFile(oldPath,newPath);

                File newFile = new File(fileName);
//                if(!newFile.exists()){
//                    newFile.createNewFile();
//                }
                OutputFormat format = OutputFormat.createPrettyPrint();
                // 设置编码格式
                format.setEncoding("UTF-8");

                XMLWriter writer = new XMLWriter(new FileOutputStream(newFile),format);;

                writer.setEscapeText(false);

                Document newxml=setXml("D:/item.xml",list.get(i));

                writer.write(newxml);

                writer.close();


            }

            String deleteDir=directory+"/LSRBA";
            deleteDir(deleteDir);

        }



    }


    public static Document setXml(String path,LinkedHashMap<String, String> map) throws IOException {

        Document document = XMLUtils.readDocument(path);
        Element rootElement = document.getRootElement();

        List<Element> dataLists = rootElement.elements("data");
        for(Element datas:dataLists){
            String dataName = datas.attributeValue("name");
            //System.out.println("dataName:"+dataName);
            if(dataName.equals("modelAudio")) {
                setCdata(datas, map.get("modelAudio"));
            }
            else if(dataName.equals("listenContent")) {
                setCdata(datas, map.get("listenContent"));
            }
        }

        Element group = rootElement.element("group");
        List<Element> dataTypeList = group.elements("question");

        for (Element dataElement : dataTypeList) {


            List<Element> dataList = dataElement.elements("data");
            for (Element data : dataList) {
                String dataName = data.attributeValue("name");
                //System.out.println("dataName:"+dataName);
                if(dataName.equals("questionAudio")) {
                    //setCdata(data, map.get("questionAudio"));
                    data.clearContent();
                }
                else if(dataName.equals("questionText")) {
                    setCdata(data, map.get("questionText"));
                }

            }

            Element choiceList = dataElement.element("choice");
            List<Element> choices = choiceList.elements("choice");
            int i=1;
            for (Element choice : choices) {

                setCdata(choice, map.get("choice"+i));

                i++;
            }

            Element answer = dataElement.element("answer");
            setCdata(answer, map.get("answer"));


            Element knowledge = dataElement.element("knowledge");
            setCdata(knowledge, map.get("knowledge"));

        }
        return document;
    }


     public static List<LinkedHashMap<String,String>> xmltojson(String xmlPath){
         Document document = XMLUtils.readDocument(xmlPath);
         Element rootElement = document.getRootElement();
         Element group = rootElement.element("group");
         List<Element> dataTypeList = group.elements("question");
         List<LinkedHashMap<String,String>> xmlList=new ArrayList();
         for (Element dataElement : dataTypeList) {
             LinkedHashMap<String,String> map=new LinkedHashMap();

             List<Element> dataList = dataElement.elements("data");
             for (Element data : dataList) {
                 String dataName = data.attributeValue("name");
//                 System.out.println("dataName:"+dataName);
//                 System.out.println("data:"+data.getText());
                 map.put(dataName,data.getText());
             }

             Element choiceList = dataElement.element("choice");
             List<Element> choices = choiceList.elements("choice");
             int i=1;
             for (Element choice : choices) {
                 String dataName = choice.attributeValue("choice");
//                 System.out.println("choice"+i+":"+dataName);
//                 System.out.println("data:"+choice.getText());
                 map.put("choice"+i,choice.getText());
                 i++;
             }

             Element answer = dataElement.element("answer");
             map.put("answer",answer.getText());


             Element knowledge = dataElement.element("knowledge");
             map.put("knowledge",knowledge.getText());

             xmlList.add(map);
         }
         return xmlList;
     }



    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    //System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        }
        catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }

    protected static void setCdata(Element element, String text) {
        element.clearContent();
        element.addCDATA(text);
    }

    /**
     * 迭代删除文件夹
     * @param dirPath 文件夹路径
     */
    public static void deleteDir(String dirPath)
    {
        File file = new File(dirPath);
        if(file.isFile())
        {
            file.delete();
        }else
        {
            File[] files = file.listFiles();
            if(files == null)
            {
                file.delete();
            }else
            {
                for (int i = 0; i < files.length; i++)
                {
                    deleteDir(files[i].getAbsolutePath());
                }
                file.delete();
            }
        }
    }
}
