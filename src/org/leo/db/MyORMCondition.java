package org.leo.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyORMCondition
{
  private Class<?> clazz;
  private List<String> fetchs = new ArrayList();

  private List<String> eqs = new ArrayList();

  private Map<String, Object> eqvs = new HashMap();

  private List<String> neqs = new ArrayList();

  private Map<String, Object> neqvs = new HashMap();

  private List<String> gts = new ArrayList();

  private Map<String, Object> gtvs = new HashMap();

  private List<String> ges = new ArrayList();

  private Map<String, Object> gevs = new HashMap();

  private List<String> lts = new ArrayList();

  private Map<String, Object> ltvs = new HashMap();

  private List<String> les = new ArrayList();

  private Map<String, Object> levs = new HashMap();

  private List<String> betweens = new ArrayList();

  private Map<String, Object> betweenVs = new HashMap();

  private List<String> catchs = new ArrayList();
  private int currentPage;
  private int aidPage;
  private int pageSize;

  public static MyORMCondition getInstance()
  {
    return new MyORMCondition();
  }

  public void eq(String field, Object value)
  {
    this.eqs.add(field);
    this.eqvs.put(field, value);
  }

  public void ne(String field, Object value)
  {
    this.neqs.add(field);
    this.neqvs.put(field, value);
  }

  public void lt(String field, Object value)
  {
    this.lts.add(field);
    this.ltvs.put(field, value);
  }

  public void le(String field, Object value)
  {
    this.les.add(field);
    this.levs.put(field, value);
  }

  public void gt(String field, Object value)
  {
    this.gts.add(field);
    this.gtvs.put(field, value);
  }

  public void ge(String field, Object value)
  {
    this.ges.add(field);
    this.gevs.put(field, value);
  }

  public void between(String field, Object fc, Object sc)
  {
    this.betweens.add(field);
    StringBuffer sb = new StringBuffer();
    this.betweenVs.put(field + "_btweenFrt", fc);
    sb.delete(0, sb.length());
    this.betweenVs.put(field + "_btweenScd", sc);
  }

  public void fetch(String field)
  {
    this.fetchs.add(field);
  }

  public void catchs(String field)
  {
    this.catchs.add(field);
  }

  public Class<?> getClazz()
  {
    return this.clazz;
  }

  public void setClazz(Class<?> clazz)
  {
    this.clazz = clazz;
  }

  public List<String> getFetchs() {
    return this.fetchs;
  }

  public void setFetchs(List<String> fetchs) {
    this.fetchs = fetchs;
  }

  public List<String> getEqs() {
    return this.eqs;
  }

  public void setEqs(List<String> eqs) {
    this.eqs = eqs;
  }

  public Map<String, Object> getEqvs() {
    return this.eqvs;
  }

  public void setEqvs(Map<String, Object> eqvs) {
    this.eqvs = eqvs;
  }

  public List<String> getNeqs() {
    return this.neqs;
  }

  public void setNeqs(List<String> neqs) {
    this.neqs = neqs;
  }

  public Map<String, Object> getNeqvs() {
    return this.neqvs;
  }

  public void setNeqvs(Map<String, Object> neqvs) {
    this.neqvs = neqvs;
  }

  public List<String> getGts() {
    return this.gts;
  }

  public void setGts(List<String> gts) {
    this.gts = gts;
  }

  public Map<String, Object> getGtvs() {
    return this.gtvs;
  }

  public void setGtvs(Map<String, Object> gtvs) {
    this.gtvs = gtvs;
  }

  public List<String> getGes() {
    return this.ges;
  }

  public void setGes(List<String> ges) {
    this.ges = ges;
  }

  public Map<String, Object> getGevs() {
    return this.gevs;
  }

  public void setGevs(Map<String, Object> gevs) {
    this.gevs = gevs;
  }

  public List<String> getLts() {
    return this.lts;
  }

  public void setLts(List<String> lts) {
    this.lts = lts;
  }

  public Map<String, Object> getLtvs() {
    return this.ltvs;
  }

  public void setLtvs(Map<String, Object> ltvs) {
    this.ltvs = ltvs;
  }

  public List<String> getLes() {
    return this.les;
  }

  public void setLes(List<String> les) {
    this.les = les;
  }

  public Map<String, Object> getLevs() {
    return this.levs;
  }

  public void setLevs(Map<String, Object> levs) {
    this.levs = levs;
  }

  public List<String> getBetweens() {
    return this.betweens;
  }

  public void setBetweens(List<String> betweens) {
    this.betweens = betweens;
  }

  public Map<String, Object> getBetweenVs() {
    return this.betweenVs;
  }

  public void setBetweenVs(Map<String, Object> betweenVs) {
    this.betweenVs = betweenVs;
  }

  public List<String> getCatchs() {
    return this.catchs;
  }

  public void setCatchs(List<String> catchs) {
    this.catchs = catchs;
  }

  public int getCurrentPage() {
    return this.currentPage;
  }

  public void setCurrentPage(int currentPage)
  {
    this.currentPage = (currentPage - 1);
    if (this.currentPage < 0)
      this.currentPage = 0;
  }

  public int getAidPage()
  {
    return this.aidPage;
  }

  public void setAidPage(int aidPage)
  {
    this.aidPage = (aidPage - 1);
    if (this.aidPage < 0)
      this.aidPage = 0;
  }

  public int getPageSize()
  {
    return this.pageSize;
  }

  public void setPageSize(int pageSize)
  {
    this.pageSize = pageSize;
  }
}
