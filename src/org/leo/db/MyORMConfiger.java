package org.leo.db;

import java.util.Map;

public class MyORMConfiger
{
  public static final String DECLARE;
  public static final String DRIVER;
  public static final String URL;
  public static final String USER;
  public static final String PWD;

  static
  {
    Map settingMap = ParseConfig.getSettingConfig();
    DECLARE = (String)settingMap.get("declare");
    DRIVER = (String)settingMap.get("driver");
    URL = (String)settingMap.get("url");
    USER = (String)settingMap.get("user");
    PWD = (String)settingMap.get("pwd");
  }
}
