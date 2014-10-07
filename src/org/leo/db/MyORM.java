package org.leo.db;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

public class MyORM {
	private static Logger logger = Logger.getLogger(MyORM.class);
	private static final String ORMSUFFIX = "_myorm_";

	public static <E> List<E> queryByConditions(MyORMCondition condition) throws SQLException, SecurityException,
			NoSuchMethodException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, InstantiationException, DocumentException, ClassNotFoundException {
		int entityCounter = 1;

		Class<?> clazz = condition.getClazz();

		Map<String, String> mappingMap = ParseConfig.getMapperConfig();

		Map<Integer, Map<String, String>> mapper = ParseEntity.parseXML((String) mappingMap.get(clazz.getName()));

		Map<String, String> columnMap = mapper.get(ParseEntity.COLUMNMAPPER);

		Map<String, String> keyMap = mapper.get(ParseEntity.KEYMAPPER);

		Map<String, String> typeMap = mapper.get(ParseEntity.TYPEMAPPER);

		Map<String, String> catchMap = mapper.get(ParseEntity.CATCHMAPPER);

		List<String> fields = new ArrayList<String>();
		for (String column : columnMap.keySet()) {
			if ((column.equals(clazz.getName())) || (keyMap.get(column) != null) || (catchMap.get(column) != null))
				continue;
			logger.debug(column);

			fields.add(column);
		}

		List<String> catchs = condition.getCatchs();

		List<String> fetchs = condition.getFetchs();

		StringBuffer entitySB = new StringBuffer();

		String tableAlias = clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1) + "_myorm_";

		StringBuffer fetchEntitySB = new StringBuffer();

		Map<String, List<String>> fetchTableMap = new HashMap<String, List<String>>();

		Map<String, Map<String, String>> fetchTableColumnMap = new HashMap<String, Map<String, String>>();

		List<String> fetchTableColumns = new ArrayList<String>();

		for (String fetchField : fetchs) {
			String fetchEntityName = (String) typeMap.get(fetchField);
			Map<Integer, Map<String, String>> fetchMapper = ParseEntity.parseXML((String) mappingMap
					.get(fetchEntityName));
			Map<String, String> fetchColumnMapper = fetchMapper.get(ParseEntity.COLUMNMAPPER);
			for (String fs : fetchColumnMapper.keySet()) {
				if (fs.equals(fetchEntityName))
					continue;
				logger.debug(fs);

				fetchTableColumns.add(fs);
			}

			fetchTableMap.put(fetchField, fetchTableColumns);
			fetchTableColumnMap.put(fetchField, fetchColumnMapper);
		}

		List<String> eqs = condition.getEqs();
		Map<String, Object> eqvs = condition.getEqvs();
		List<String> neqs = condition.getNeqs();
		Map<String, Object> neqvs = condition.getNeqvs();
		List<String> ges = condition.getGes();
		Map<String, Object> gevs = condition.getGevs();
		List<String> gts = condition.getGts();
		Map<String, Object> gtvs = condition.getGtvs();
		List<String> les = condition.getLes();
		Map<String, Object> levs = condition.getLevs();
		List<String> lts = condition.getLts();
		Map<String, Object> ltvs = condition.getLtvs();
		List<String> betweens = condition.getBetweens();
		Map<String, Object> betweenVs = condition.getBetweenVs();

		StringBuffer sql = new StringBuffer();
		sql.append("select ");

		for (int i = 0; i < fields.size(); i++) {
			if (i > 0) {
				sql.append(",");
			}
			sql.append(tableAlias).append(".").append((String) columnMap.get(fields.get(i))).append(" as ")
					.append(tableAlias).append((String) fields.get(i));
		}

		for (String fetchField : fetchs) {
			fetchEntitySB.delete(0, fetchEntitySB.length());
			String fetchEntityName = (String) typeMap.get(fetchField);
			fetchTableColumns = fetchTableMap.get(fetchField);
			String fetchTableAlias = fetchEntityName.substring(fetchEntityName.lastIndexOf(".") + 1) + "_myorm_"
					+ entityCounter + "_";
			Map<String, String> fetchColumnMapper = fetchTableColumnMap.get(fetchField);
			for (String fetchColumn : fetchColumnMapper.keySet()) {
				if (fetchColumn.equals(fetchEntityName))
					continue;
				sql.append(",").append(fetchTableAlias).append(".").append((String) fetchColumnMapper.get(fetchColumn))
						.append(" as ").append(fetchTableAlias).append(fetchColumn);
			}

			entityCounter++;
		}

		entityCounter = 1;

		sql.append(" from ").append((String) columnMap.get(clazz.getName())).append(" as ").append(tableAlias);

		for (String fetchField : fetchs) {
			fetchEntitySB.delete(0, fetchEntitySB.length());
			String fetchEntityName = (String) typeMap.get(fetchField);
			fetchTableColumns = fetchTableMap.get(fetchField);
			String fetchTableAlias = fetchEntityName.substring(fetchEntityName.lastIndexOf(".") + 1) + "_myorm_"
					+ entityCounter + "_";
			entityCounter++;
			Map<String, String> fetchColumnMapper = fetchTableColumnMap.get(fetchField);
			sql.append(" left outer join ").append((String) fetchColumnMapper.get(fetchEntityName)).append(" as ")
					.append(fetchTableAlias).append(" on ").append(tableAlias).append(".")
					.append((String) columnMap.get(fetchField)).append("=").append(fetchTableAlias).append(".")
					.append((String) keyMap.get(fetchField));
		}

		sql.append(" where 1=1");
		for (String s : eqs) {
			sql.append(" and ").append(tableAlias).append(".").append((String) columnMap.get(s)).append("=?");
		}
		for (String s : neqs) {
			sql.append(" and ").append(tableAlias).append(".").append((String) columnMap.get(s)).append("!=?");
		}
		for (String s : ges) {
			sql.append(" and ").append(tableAlias).append(".").append((String) columnMap.get(s)).append(">=?");
		}
		for (String s : gts) {
			sql.append(" and ").append(tableAlias).append(".").append((String) columnMap.get(s)).append(">?");
		}
		for (String s : les) {
			sql.append(" and ").append(tableAlias).append(".").append((String) columnMap.get(s)).append("<=?");
		}
		for (String s : lts) {
			sql.append(" and ").append(tableAlias).append(".").append((String) columnMap.get(s)).append("<?");
		}
		for (String s : betweens) {
			sql.append(" and ").append(tableAlias).append(".").append((String) columnMap.get(s)).append(" between ")
					.append("?").append(" and ").append("?");
		}

		if (MyORMConfiger.DECLARE.equals("OracleDeclare"))
			sql.append(" and rownum between ? and ?");
		else if (MyORMConfiger.DECLARE.equals("MySqlDeclare")) {
			sql.append(" limit ?,?");
		}

		logger.debug(sql.toString());

		Connection connection = MyORMJdbcConnection.getConnection();
		List<E> dataList = null;

		PreparedStatement ps = connection.prepareStatement(sql.toString().toUpperCase());

		int paramCounter = 1;
		for (String s : eqs) {
			ps.setObject(paramCounter, eqvs.get(s));
			paramCounter++;
		}
		for (String s : neqs) {
			ps.setObject(paramCounter, neqvs.get(s));
			paramCounter++;
		}
		for (String s : ges) {
			ps.setObject(paramCounter, gevs.get(s));
			paramCounter++;
		}
		for (String s : gts) {
			ps.setObject(paramCounter, gtvs.get(s));
			paramCounter++;
		}
		for (String s : les) {
			ps.setObject(paramCounter, levs.get(s));
			paramCounter++;
		}
		for (String s : lts) {
			ps.setObject(paramCounter, ltvs.get(s));
			paramCounter++;
		}
		for (String s : betweens) {
			StringBuffer sb = new StringBuffer();
			ps.setObject(paramCounter, betweenVs.get(s + "_btweenFrt"));
			sb.delete(0, sb.length());
			ps.setObject(paramCounter + 1, betweenVs.get(s + "_btweenScd"));
			paramCounter += 2;
		}

		if (MyORMConfiger.DECLARE.equals("OracleDeclare")) {
			ps.setInt(paramCounter, condition.getCurrentPage() * condition.getPageSize());
			ps.setInt(paramCounter + 1, condition.getAidPage() * condition.getPageSize());
		} else if (MyORMConfiger.DECLARE.equals("MySqlDeclare")) {
			ps.setInt(paramCounter, condition.getCurrentPage() * condition.getPageSize());
			ps.setInt(paramCounter + 1, condition.getPageSize());
		}

		ResultSet rs = ps.executeQuery();

		dataList = new ArrayList<E>();

		StringBuffer columnSB = new StringBuffer();

		while (rs.next()) {
			logger.debug("####################");

			Object e = clazz.newInstance();

			for (String field : fields) {
				columnSB.delete(0, columnSB.length());
				logger.debug(rs.getObject(tableAlias + field));

				columnSB.delete(0, columnSB.length());
				Object value = rs.getObject(tableAlias + field);
				if (value != null) {
					makeObject(e, field, value);
				}
			}
			entityCounter = 1;

			for (String fetchField : fetchs) {
				entitySB.delete(0, entitySB.length());
				String fetchEntityName = (String) typeMap.get(fetchField);
				Class<?> fetchClss = Class.forName(fetchEntityName);
				Object fetchObject = fetchClss.newInstance();
				fetchTableColumns = fetchTableMap.get(fetchField);
				String fetchTableAlias = fetchEntityName.substring(fetchEntityName.lastIndexOf(".") + 1) + "_myorm_"
						+ entityCounter + "_";
				Object value;
				for (String fetchTableColumn : fetchTableColumns) {
					columnSB.delete(0, columnSB.length());
					logger.debug("fetch###"
							+ rs.getObject(columnSB.append(fetchTableAlias).append(fetchTableColumn).toString()));

					columnSB.delete(0, columnSB.length());

					value = rs.getObject(fetchTableAlias + fetchTableColumn);
					if (value != null) {
						makeObject(fetchObject, fetchTableColumn, value);
					}
				}

				makeObject(e, fetchField, fetchObject);
				entityCounter++;

				for (String catchField : catchs) {
					logger.debug(catchField);
					logger.debug(typeMap.get(catchField));

					String catchSonEntityName = (String) typeMap.get(catchField);

					Map<Integer, Map<String, String>> catchSonMap = ParseEntity.parseXML((String) mappingMap
							.get(catchSonEntityName));

					Map<String, String> catchSonColumnMap = catchSonMap.get(ParseEntity.COLUMNMAPPER);

					StringBuffer catchSonSql = new StringBuffer();
					catchSonSql.append("select ");

					List<String> catchSonFields = new ArrayList<String>();

					for (String catchSonColumn : catchSonColumnMap.keySet()) {
						if (!catchSonColumn.equals(catchSonEntityName)) {
							catchSonFields.add(catchSonColumn);
						}
					}

					for (int i = 0; i < catchSonFields.size(); i++) {
						if (i > 0) {
							catchSonSql.append(",");
						}
						catchSonSql.append((String) catchSonColumnMap.get(catchSonFields.get(i)));
					}
					columnSB.delete(0, columnSB.length());

					catchSonSql.append(" from ").append((String) catchSonColumnMap.get(catchSonEntityName))
							.append(" where ").append((String) keyMap.get(catchField)).append("=?");

					logger.debug(catchSonSql.toString());

					Object keyValue = rs.getObject(tableAlias + (String) catchMap.get(catchField));

					PreparedStatement catchPs = connection.prepareStatement(catchSonSql.toString().toUpperCase());

					catchPs.setObject(1, keyValue);

					ResultSet catchRs = catchPs.executeQuery();

					Class<?> catchClass = Class.forName(catchSonEntityName);

					List<Object> catchSons = new ArrayList<Object>();

					while (catchRs.next()) {
						Object catchObject = catchClass.newInstance();

						for (String catchSonField : catchSonFields) {
							value = (Object) catchRs.getObject(catchSonColumnMap.get(catchSonField));
							makeObject(catchObject, catchSonField, value);
						}
						catchSons.add(catchObject);
					}

					makeObject(e, catchField, catchSons);
				}
			}
			dataList.add((E) e);
		}
		return dataList;
	}

	private static void makeObject(Object o, String field, Object value) throws SecurityException,
			NoSuchMethodException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {

		String setMethod = "set" + field.substring(0, 1).toUpperCase() + field.substring(1);

		Method method = o.getClass().getMethod(setMethod,
				new Class[] { o.getClass().getDeclaredField(field).getType() });

		method.invoke(o, new Object[] { value });
	}
}
