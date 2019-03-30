/*
 * Copyright (c) 2019, Oracle and/or its affiliates. All rights reserved.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at http://oss.oracle.com/licenses/upl.
 */

package weblogic.logging.exporter.config;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test the Config class")
public class ConfigTest {

  @DisplayName("Create the default config from an empty file")
  @Test
  public void createDefaultConfigFromEmptyFile() {
    // create config by loading an empty file
    Config config = Config.loadConfig(new File("src/test/resources/emptyConfig.yaml"));

    // now check that the config contains the expected default values
    assertAll(
        "config",
        () -> assertEquals("localhost", config.getHost()),
        () -> assertEquals(9200, config.getPort()),
        () -> assertEquals(true, config.getEnabled()),
        () -> assertEquals("wls", config.getIndexName()),
        () -> assertEquals(null, config.getSeverity()),
        () -> assertTrue(config.getFilterConfigs() instanceof ArrayList),
        () -> assertEquals(0, config.getFilterConfigs().size()),
        () -> assertEquals(1, config.getBulkSize()),
        () -> assertEquals("unknown", config.getDomainUID()));
  }

  @DisplayName("Create config from file")
  @Test
  public void createConfigFromFile() {
    // create config by loading an empty file
    Config config = Config.loadConfig(new File("src/test/resources/config1.yaml"));

    // now check that the config contains the expected default values
    assertAll(
        "config",
        () -> assertEquals("host1", config.getHost()),
        () -> assertEquals(1234, config.getPort()),
        () -> assertEquals(false, config.getEnabled()),
        () -> assertEquals("index1", config.getIndexName()),
        () -> assertEquals("Warning", config.getSeverity()),
        () -> assertTrue(config.getFilterConfigs() instanceof ArrayList),
        () -> assertEquals(1, config.getFilterConfigs().size()),
        () ->
            assertEquals(
                "FilterConfig{expression='MSGID != 'BEA-000449'', servers=[]}",
                config.getFilterConfigs().get(0).toString()),
        () -> assertEquals(2, config.getBulkSize()),
        () -> assertEquals("domain1", config.getDomainUID()));
  }
}
