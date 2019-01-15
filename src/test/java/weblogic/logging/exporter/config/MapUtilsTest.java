package weblogic.logging.exporter.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.junit.MatcherAssert.assertThat;

/**
 * Copyright (c) 2018, Oracle and/or its affiliates. All rights reserved.
 */
public class MapUtilsTest {

  @Before
  public void setUp() throws Exception {}

  @Test
  public void whenStringArrayValueIsStringArray_returnAsIs()  {
    final String[] STRING_ARRAY = {"1", "2", "3"};
    Map<String,Object> map = createMapWithValue(STRING_ARRAY);

    assertThat(MapUtils.getStringArray(map, "values"), arrayContaining(STRING_ARRAY));
  }

  @Test
  public void whenStringArrayValueIsSingleObject_returnAsLengthOneArray() {
    Map<String,Object> map = createMapWithValue(33);

    assertThat(MapUtils.getStringArray(map, "values"), arrayContaining("33"));
  }

  @Test
  public void whenStringArrayValueIsList_returnAsArray() {
    Map<String,Object> map = createMapWithValue(Arrays.asList(7, 8, true));

    assertThat(MapUtils.getStringArray(map, "values"), arrayContaining("7", "8", "true"));
  }

  Map<String, Object> createMapWithValue(Object value) {
    Map<String,Object> map = new HashMap<>();
    map.put("values", value);
    return map;
  }
}