package org.leo.db;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ParseEntity {
	private static Logger logger = Logger.getLogger(ParseEntity.class);
	private static final String ID = "id";
	private static final String PROPERTY = "property";
	private static final String ONETOMANY = "onetomany";
	private static final String MANYTOONE = "manytoone";
	private static final String NAME = "name";
	private static final String COLUMN = "column";
	public static final String KEY = "key";
	public static final String TYPE = "type";
	public static final String TABLE = "table";
	public static final String MAPPER = "mapper";
	public static final Integer KEYMAPPER = Integer.valueOf(1);
	public static final Integer COLUMNMAPPER = Integer.valueOf(0);
	public static final Integer TYPEMAPPER = Integer.valueOf(2);
	public static final Integer CATCHMAPPER = Integer.valueOf(3);

	public static Map<Integer, Map<String, String>> parseXML(String path) throws DocumentException {
		SAXReader saxReader = new SAXReader();

		Document document = saxReader.read(new File(path));

		Element root = document.getRootElement();

		Map<Integer, Map<String, String>> mapper = new HashMap<Integer, Map<String, String>>();
		Map<String, String> columnMapper = new HashMap<String, String>();
		Map<String, String> keyMapper = new HashMap<String, String>();
		Map<String, String> typeMapper = new HashMap<String, String>();
		Map<String, String> catchMapper = new HashMap<String, String>();

		if (root.getName().equals("mapper")) {
			String type = root.attributeValue("type");
			String table = root.attributeValue("table");
			columnMapper.put(type, table);
			Iterator<Element> rootIterator = root.elementIterator();
			String name = null;
			String column = null;
			String key = null;
			String constraint = null;

			while (rootIterator.hasNext()) {
				Element element = rootIterator.next();
				String elementName = element.getName();

				name = element.attributeValue("name");
				column = element.attributeValue("column");
				if ((!elementName.equals("id")) && (!elementName.equals("property"))) {
					if (elementName.equals("manytoone")) {
						key = element.attributeValue("key");
						type = element.attributeValue("type");
					} else if (elementName.equals("onetomany")) {
						key = element.attributeValue("key");
						type = element.attributeValue("type");
						constraint = element.attributeValue("column");
					}
				}
				logger.debug(name + ":" + column);

				if ((name != null) && (column != null)) {
					columnMapper.put(name, column);
				}
				if ((name != null) && (column != null) && (key != null)) {
					keyMapper.put(name, key);
					typeMapper.put(name, type);
				}
				if ((name != null) && (constraint != null) && (key != null)) {
					keyMapper.put(name, key);
					typeMapper.put(name, type);
					catchMapper.put(name, constraint);
				}
				Iterator sonFCIterator = element.elementIterator();

				while (sonFCIterator.hasNext())
					;
			}
		} else {
			return null;
		}
		mapper.put(COLUMNMAPPER, columnMapper);
		mapper.put(KEYMAPPER, keyMapper);
		mapper.put(TYPEMAPPER, typeMapper);
		mapper.put(CATCHMAPPER, catchMapper);
		return mapper;
	}
}
