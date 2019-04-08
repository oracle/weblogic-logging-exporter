/*
 * Copyright (c) 2019, Oracle and/or its affiliates. All rights reserved.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at http://oss.oracle.com/licenses/upl.
 */

package weblogic.logging.exporter.config;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test FilterConfig class")
public class FilterConfigTest {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private final PrintStream originalErr = System.err;

  @BeforeEach
  public void setUpStreams() {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @AfterEach
  public void restoreStreams() {
    System.setOut(originalOut);
    System.setErr(originalErr);
  }

  private static final String EXPECTED_FILTERS_STRING =
      "FilterConfig{expression='MSGID != 'BEA-000449'', servers=[]}";

  private static final String EXPECTED_SERVERS_STRING =
      "FilterConfig{expression='null', servers=[managed-server-1]}";

  @DisplayName("Check toString() works as expected for filter expressions")
  @Test
  public void checkToStringWorksAsExpectedForFilterExpressions() {
    Map<String, Object> map = new HashMap<>();
    map.put("FilterExpression", "MSGID != 'BEA-000449'");
    FilterConfig filterConfig = FilterConfig.create(map);

    assertEquals(EXPECTED_FILTERS_STRING, filterConfig.toString());
  }

  @DisplayName("Check toString() works as expected for servers")
  @Test
  public void checkToStringWorksAsExpectedForServers() {
    Map<String, Object> map = new HashMap<>();
    map.put("FilterServers", "managed-server-1");
    FilterConfig filterConfig = FilterConfig.create(map);

    assertEquals(EXPECTED_SERVERS_STRING, filterConfig.toString());
  }

  @DisplayName("Check the query can be retrieved")
  @Test
  public void checkTheQuery() {
    Map<String, Object> map = new HashMap<>();
    map.put("FilterExpression", "MSGID != 'BEA-000449'");
    FilterConfig filterConfig = FilterConfig.create(map);

    assertTrue(
        filterConfig.getQuery().toString().contains("weblogic.diagnostics.query.CompiledQuery"));
  }

  @DisplayName("Bad filter expression")
  @Test
  public void badFilterExpression() {
    Map<String, Object> map = new HashMap<>();
    map.put("FilterExpression", "nonsense-text");
    FilterConfig filterConfig = FilterConfig.create(map);

    assertTrue(outContent.toString().contains(("Error Parsing expression:")));
  }

  @DisplayName("Check duplicate values are rejected")
  @Test
  public void checkDuplicateValuesAreRejected() {
    Map<String, Object> map = new HashMap<>();
    map.put(
        "FilterServers", new String[] {"managed-server-1", "managed-server-2", "managed-server-1"});

    assertThrows(ConfigurationException.class, () -> FilterConfig.create(map));
  }

  @DisplayName("Check getServers() returns the right data")
  @Test
  public void checkGetServersWorks() {
    Map<String, Object> map = new HashMap<>();
    map.put("FilterServers", new String[] {"managed-server-1", "managed-server-2"});
    FilterConfig filterConfig = FilterConfig.create(map);

    assertTrue(filterConfig.getServers() instanceof List);
    assertEquals(2, filterConfig.getServers().size());
    assertTrue(filterConfig.getServers().get(0) instanceof String);
    assertEquals("managed-server-1", filterConfig.getServers().get(0));
    assertTrue(filterConfig.getServers().get(1) instanceof String);
    assertEquals("managed-server-2", filterConfig.getServers().get(1));
  }
}
