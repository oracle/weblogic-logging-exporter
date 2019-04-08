// Copyright (c) 2018, Oracle and/or its affiliates. All rights reserved.
// Licensed under the Universal Permissive License v 1.0 as shown at
// http://oss.oracle.com/licenses/upl.

package weblogic.logging.exporter.config;

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("EmptyMethod")
public class MapUtilsTest {

  @BeforeAll
  public static void setUp() {}

  @Test
  public void whenStringArrayValueIsStringArray_returnAsIs() {
    final String[] STRING_ARRAY = {"1", "2", "3"};
    Map<String, Object> map = createMapWithValue(STRING_ARRAY);

    assertThat(MapUtils.getStringArray(map, "values"), arrayContaining(STRING_ARRAY));
  }

  @Test
  public void whenStringArrayValueIsSingleObject_returnAsLengthOneArray() {
    Map<String, Object> map = createMapWithValue(33);

    assertThat(MapUtils.getStringArray(map, "values"), arrayContaining("33"));
  }

  @Test
  public void whenStringArrayValueIsList_returnAsArray() {
    Map<String, Object> map = createMapWithValue(Arrays.asList(7, 8, true));

    assertThat(MapUtils.getStringArray(map, "values"), arrayContaining("7", "8", "true"));
  }

  @DisplayName("After creating a map, check that 'good' true values are correctly interpreted")
  @Test
  public void afterCreateMap_checkGoodTrueValues() {
    Map<String, Object> map = createMapOfTrueValues();

    assertEquals(true, MapUtils.getBooleanValue(map, "1"));
    assertEquals(true, MapUtils.getBooleanValue(map, "2"));
    assertEquals(true, MapUtils.getBooleanValue(map, "3"));
    assertEquals(true, MapUtils.getBooleanValue(map, "4"));
    assertEquals(true, MapUtils.getBooleanValue(map, "5"));
  }

  @DisplayName("After creating a map, check that 'bad' true values are correctly interpreted")
  @Test
  public void afterCreateMap_checkBadTrueValues() {
    Map<String, Object> map = createMapOfTrueValues();

    assertThrows(ConfigurationException.class, () -> MapUtils.getBooleanValue(map, "6"));
    assertThrows(ConfigurationException.class, () -> MapUtils.getBooleanValue(map, "7"));
    assertThrows(ConfigurationException.class, () -> MapUtils.getBooleanValue(map, "8"));
    assertThrows(ConfigurationException.class, () -> MapUtils.getBooleanValue(map, "9"));
    assertThrows(ConfigurationException.class, () -> MapUtils.getBooleanValue(map, "10"));
  }

  @DisplayName("After creating a map, check that 'good' false values are correctly interpreted")
  @Test
  public void afterCreateMap_checkGoodFalseValues() {
    Map<String, Object> map = createMapOfFalseValues();

    assertEquals(false, MapUtils.getBooleanValue(map, "1"));
    assertEquals(false, MapUtils.getBooleanValue(map, "2"));
    assertEquals(false, MapUtils.getBooleanValue(map, "3"));
    assertEquals(false, MapUtils.getBooleanValue(map, "4"));
    assertEquals(false, MapUtils.getBooleanValue(map, "5"));
  }

  @DisplayName("After creating a map, check that 'bad' false values are correctly interpreted")
  @Test
  public void afterCreateMap_checkBadFalseValues() {
    Map<String, Object> map = createMapOfFalseValues();

    assertThrows(ConfigurationException.class, () -> MapUtils.getBooleanValue(map, "6"));
    assertThrows(ConfigurationException.class, () -> MapUtils.getBooleanValue(map, "7"));
    assertThrows(ConfigurationException.class, () -> MapUtils.getBooleanValue(map, "8"));
    assertThrows(ConfigurationException.class, () -> MapUtils.getBooleanValue(map, "9"));
    assertThrows(ConfigurationException.class, () -> MapUtils.getBooleanValue(map, "10"));
  }

  private Map<String, Object> createMapWithValue(Object value) {
    Map<String, Object> map = new HashMap<>();
    map.put("values", value);
    return map;
  }

  private Map<String, Object> createMapOfTrueValues() {
    Map<String, Object> map = new HashMap<>();
    map.put("1", "true");
    map.put("2", "t");
    map.put("3", "yes");
    map.put("4", "on");
    map.put("5", "y");
    map.put("6", "truenot");
    map.put("7", "nottrue");
    map.put("8", "yesss");
    map.put("9", "y ");
    map.put("10", " y ");
    return map;
  }

  private Map<String, Object> createMapOfFalseValues() {
    Map<String, Object> map = new HashMap<>();
    map.put("1", "false");
    map.put("2", "f");
    map.put("3", "no");
    map.put("4", "off");
    map.put("5", "n");
    map.put("6", "falsedata");
    map.put("7", "sofalse");
    map.put("8", "ono");
    map.put("9", "n ");
    map.put("10", " n ");
    return map;
  }
}
