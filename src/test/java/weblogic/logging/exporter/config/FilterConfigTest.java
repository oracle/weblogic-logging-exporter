/*
 * Copyright (c) 2019, Oracle and/or its affiliates. All rights reserved.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at http://oss.oracle.com/licenses/upl.
 */

package weblogic.logging.exporter.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test FilterConfig class")
public class FilterConfigTest {

  private static final String EXPECTED_STRING =
      "FilterConfig{expression='MSGID != 'BEA-000449'', servers=[]}";

  @DisplayName("Check toString() works as expected")
  @Test
  public void checkToStringWorksAsExpected() {
    Map<String, Object> map = new HashMap<>();
    map.put("FilterExpression", "MSGID != 'BEA-000449'");
    FilterConfig filterConfig = FilterConfig.create(map);

    assertEquals(EXPECTED_STRING, filterConfig.toString());
  }
}
