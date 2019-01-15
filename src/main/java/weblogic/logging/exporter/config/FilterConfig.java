/*
 * Copyright (c) 2018 Oracle and/or its affiliates
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at http://oss.oracle.com/licenses/upl.
 */

package weblogic.logging.exporter.config;


import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import weblogic.diagnostics.logging.LogVariablesImpl;
import weblogic.diagnostics.query.Query;
import weblogic.diagnostics.query.QueryException;
import weblogic.diagnostics.query.QueryFactory;
import weblogic.diagnostics.query.QueryException;

public class FilterConfig {
  static final String EXPRESSION = "FilterExpression";
  static final String SERVERS = "FilterServers";


  private static final String[] NO_VALUES = {};
  private String expression;
  private String[] servers = new String[0];
  private Query query = null;
  LogVariablesImpl lv = LogVariablesImpl.getInstance();

  private FilterConfig(Map<String, Object> map) {
    for (String key : map.keySet()) {
      switch (key) {
        case EXPRESSION:
          expression = MapUtils.getStringValue(map, EXPRESSION);
          try {
            query = QueryFactory.createQuery(lv, lv, expression);
          }catch(QueryException ex){
            System.out.println("Error Parsing expression: " + expression);
          }
          break;
        case SERVERS:
          setServers(MapUtils.getStringArray(map, SERVERS));
          break;
        default:
          break;
      }
    }
  }

  static FilterConfig create(Map<String, Object> map) {
    return new FilterConfig(map);
  }

  private void setServers(String[] values) {
    if (values.length == 0) throw new ConfigurationException("Values specified as empty array");

    Set<String> uniqueValues = new HashSet<>(Arrays.asList(values));
    if (values.length != uniqueValues.size())
      reportDuplicateValues(values, uniqueValues);
    this.servers = values;

  }


  private void reportDuplicateValues(String[] values, Set<String> uniqueValues) {
    ArrayList<String> duplicate = new ArrayList<>(Arrays.asList(values));
    for (String unique : uniqueValues)
      duplicate.remove(duplicate.indexOf(unique));

    throw new ConfigurationException("Duplicate values for " + duplicate);
  }

  public Query getQuery(){
    return query;
  }

  public List<String> getServers(){
    return Arrays.asList(servers);
  }

  @Override
  public String toString() {
    return "FilterConfig{" +
        "expression='" + expression + '\'' +
        ", servers=" + Arrays.toString(servers) +
        '}';
  }
}
