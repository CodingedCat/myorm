package org.leo.db;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ParseConfig
{
  private static Logger logger = Logger.getLogger(ParseConfig.class);
  private static Map<String, String> mapperConfig;
  private static Map<String, String> settingConfig;
  private static final String MAPPING = "mapping";
  private static final String SETTING = "setting";
  private static final String NAME = "name";
  private static final String VALUE = "value";
  private static final String TYPE = "type";
  private static final String LOCAL = "local";
  public static final Integer MAPPINGMAPPER = Integer.valueOf(1);
  public static final Integer SETTINGMAPPER = Integer.valueOf(0);
  public static final String ORACLE = "OracleDeclare";
  public static final String MYSQL = "MySqlDeclare";

  public static void execute(String path)
    throws DocumentException
  {
    String beforePath = Thread.currentThread().getContextClassLoader().getResource("").toString().substring(6);

    logger.debug(beforePath);

    Document document = new SAXReader().read(new File(path));
    Element root = document.getRootElement();
    Iterator iterator = root.elementIterator();

    mapperConfig = new HashMap();
    settingConfig = new HashMap();
    StringBuffer sb = new StringBuffer();

    while (iterator.hasNext()) {
      sb.delete(0, sb.length());
      Element element = (Element)iterator.next();
      String elementName = element.getName();
      if ("setting".equals(elementName)) {
        String name = element.attributeValue("name");
        String value = element.attributeValue("value");
        settingConfig.put(name, value);
      } else if ("mapping".equals(elementName)) {
        String type = element.attributeValue("type");
        String local = beforePath + element.attributeValue("local");
        logger.debug(type + ":" + local);
        mapperConfig.put(type, local);
      }
    }
  }

  public static Map<String, String> getMapperConfig()
  {
    return mapperConfig;
  }

  public static Map<String, String> getSettingConfig()
  {
    return settingConfig;
  }

  public static void main(String[] args) {
    try {
      execute("D:/workspace/test/bin/mapper.xml");
    } catch (DocumentException e) {
      e.printStackTrace();
    }
  }
}
