/*
 * Copyright (c) 2018, 2019, Oracle and/or its affiliates. All rights reserved.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at http://oss.oracle.com/licenses/upl.
 */

package weblogic.logging.exporter.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.scanner.ScannerException;

public class Config {

  public static final String DEFAULT_ES_HOST = "localhost";
  public static final int DEFAULT_ES_PORT = 9200;
  public static final String DEFAULT_INDEX_NAME = "wls";
  public static final int DEFAULT_BULK_SIZE = 1;
  public static final String DEFAULT_DOMAIN_UID = "unknown";

  private static final String HOST = "ElasticSearchHost";
  private static final String PORT = "ElasticSearchPort";
  private static final String FILTERS = "weblogicLoggingExporterFilters";
  private static final String ENABLED = "weblogicLoggingExporterEnabled";
  private static final String SEVERITY = "weblogicLoggingExporterSeverity";
  private static final String BULK_SIZE = "weblogicLoggingExporterBulkSize";
  private static final String INDEX_NAME = "weblogicLoggingIndexName";
  private static final String DOMAIN_UID = "domainUID";

  private String host = DEFAULT_ES_HOST;
  private int port = DEFAULT_ES_PORT;
  private String indexName = DEFAULT_INDEX_NAME;
  private int bulkSize = DEFAULT_BULK_SIZE;
  private boolean enabled = true;
  private String severity = null;
  private List<FilterConfig> filterConfigs = new ArrayList<>();
  private String domainUID = DEFAULT_DOMAIN_UID;

  private Config() {}

  private Config(Map<String, Object> yaml) {
    if (yaml.containsKey(HOST)) host = MapUtils.getStringValue(yaml, HOST);
    if (yaml.containsKey(PORT)) port = MapUtils.getIntegerValue(yaml, PORT);
    if (yaml.containsKey(ENABLED)) enabled = MapUtils.getBooleanValue(yaml, ENABLED);
    if (yaml.containsKey(SEVERITY)) severity = MapUtils.getStringValue(yaml, SEVERITY);
    if (yaml.containsKey(BULK_SIZE)) bulkSize = MapUtils.getIntegerValue(yaml, BULK_SIZE);
    if (yaml.containsKey(INDEX_NAME)) indexName = MapUtils.getStringValue(yaml, INDEX_NAME);
    if (yaml.containsKey(DOMAIN_UID)) domainUID = MapUtils.getStringValue(yaml, DOMAIN_UID);
    if (bulkSize <= 1) {
      bulkSize = 1;
    }
    // index name needs to be all lowercase.
    if (yaml.containsKey(INDEX_NAME)) indexName = MapUtils.getStringValue(yaml, INDEX_NAME);
    if (!(indexName.toLowerCase().equals(indexName))) {
      indexName = indexName.toLowerCase();
      System.out.println("Index name is converted to all lower case : " + indexName);
    }
    if (yaml.containsKey(FILTERS)) appendFilters(yaml.get(FILTERS));
  }

  public static Config loadConfig(File file) {
    try {
      return loadConfig(new FileInputStream(file));
    } catch (FileNotFoundException e) {
      System.out.println(file.toString() + "Not Found");
    } catch (YamlParserException ex) {
      System.out.println("Error parsing configuration file : " + file.toString());
    } catch (Exception ex) {
      System.out.println("Error detected in configuration file.");
    }
    System.out.println("Using default for all paramters");
    return new Config();
  }

  private void appendFilters(Object filtersYaml) {
    for (Map<String, Object> filterYaml : getAsListOfMaps(filtersYaml)) {
      filterConfigs.add(FilterConfig.create(filterYaml));
    }
  }

  /**
   * Loads a YAML configuration to create a new configuration object.
   *
   * @param inputStream a reader of a YAML configuration.
   * @return an ExporterConfig object that matches the parsed YAML
   */
  private static Config loadConfig(InputStream inputStream) {
    try {
      return loadConfig(asMap(new Yaml().load(inputStream)));
    } catch (ScannerException e) {
      throw new YamlParserException(e);
    }
  }

  @SuppressWarnings("unchecked")
  private static Map<String, Object> asMap(Object yaml) {
    try {
      return (Map<String, Object>) yaml;
    } catch (ClassCastException e) {
      throw new ConfigurationException(ConfigurationException.NOT_YAML_FORMAT);
    }
  }

  @SuppressWarnings("unchecked")
  private List<Map<String, Object>> getAsListOfMaps(Object queriesYaml) {
    if (!isArrayOfMaps(queriesYaml))
      throw MapUtils.createBadTypeException(FILTERS, queriesYaml, "a list of structures");

    return (List<Map<String, Object>>) queriesYaml;
  }

  private boolean isArrayOfMaps(Object object) {
    return List.class.isAssignableFrom(object.getClass()) && emptyOrContainsMaps((List) object);
  }

  private boolean emptyOrContainsMaps(List list) {
    return list.isEmpty() || Map.class.isInstance(list.get(0));
  }

  private static Config loadConfig(Map<String, Object> yamlConfig) {
    if (yamlConfig == null) yamlConfig = new HashMap<>();

    return new Config(yamlConfig);
  }

  @Override
  public String toString() {
    return "Config{"
        + "weblogicLoggingIndexName='"
        + indexName
        + '\''
        + ", ElasticSearchHost='"
        + host
        + '\''
        + ", ElasticSearchPort="
        + port
        + ", weblogicLoggingExporterSeverity='"
        + severity
        + '\''
        + ", weblogicLoggingExporterBulkSize='"
        + bulkSize
        + '\''
        + ", enabled="
        + enabled
        + ", weblogicLoggingExporterFilters="
        + filterConfigs
        + ", domainUID='"
        + domainUID
        + '\''
        + '}';
  }

  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }

  public boolean getEnabled() {
    return enabled;
  }

  public String getIndexName() {
    return indexName;
  }

  public String getSeverity() {
    return severity;
  }

  public List<FilterConfig> getFilterConfigs() {
    return filterConfigs;
  }

  public int getBulkSize() {
    return bulkSize;
  }

  public String getDomainUID() {
    return domainUID;
  }
}
