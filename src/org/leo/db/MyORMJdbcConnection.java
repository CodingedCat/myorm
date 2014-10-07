package org.leo.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class MyORMJdbcConnection
{
  private static Logger logger = Logger.getLogger(MyORMJdbcConnection.class);

  public static Connection getConnection()
  {
    try
    {
      Class.forName(MyORMConfiger.DRIVER);
      Connection connection = DriverManager.getConnection(MyORMConfiger.URL, MyORMConfiger.USER, MyORMConfiger.PWD);
      return connection;
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    } catch (SQLException e) {
      e.printStackTrace();
    }return null;
  }

  public static void closeConnection(Connection connection)
  {
    try
    {
      if ((connection != null) && (!connection.isClosed()))
        connection.close();
    }
    catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    Connection c = getConnection();
    logger.debug(c.getClass());
  }
}
