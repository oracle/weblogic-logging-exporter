// Copyright (c) 2019, 2021, Oracle and/or its affiliates.
// Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.

package weblogic.logging.exporter.config;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test the Config class")
public class ConfigTest {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private final PrintStream originalErr = System.err;

  private static final String EXPECTED_STRING = "Config{host='host1', port=1234, indexName='index1', bulkSize=2, enabled=false, severity='Warning', filterConfigs=[FilterConfig{expression='MSGID != 'BEA-000449'', servers=[]}], domainUID='domain1', fileLoggingEnabled=false, outputFile='null', getMaxRollbackFiles=null, maxFileSize=null, appendToFile=false, fileLoggingLogLevel='INFO'}";

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

    // now check that the config contains the expected values
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

  @DisplayName("Config file does not exist")
  @Test
  public void configFileDoesNotExist() {
    Config config = Config.loadConfig(new File("src/test/resources/no-such-file.yaml"));
    assertTrue(outContent.toString().contains(("Not Found")));
    assertTrue(outContent.toString().contains("Using default for all parameters"));
  }

  @DisplayName("Config file cannot be parsed")
  @Test
  public void configFileCannotBeParsed() {
    Config config = Config.loadConfig(new File("src/test/resources/bad.yaml"));
    assertTrue(outContent.toString().contains(("Error parsing configuration file")));
    assertTrue(outContent.toString().contains("Using default for all parameters"));
  }

  @DisplayName("Should convert index name to lower case")
  @Test
  public void shouldConvertIndexNameToLowerCase() {
    // create config by loading an empty file
    Config config = Config.loadConfig(new File("src/test/resources/config2.yaml"));

    // now check that the config contains the expected values
    assertEquals("index2", config.getIndexName());
  }

  @DisplayName("Check the toString() method works as expected")
  @Test
  public void checkToStringWorksAsExpected() {
    Config config = Config.loadConfig(new File("src/test/resources/config1.yaml"));
    System.out.println(config.toString());
    assertEquals(EXPECTED_STRING, config.toString());
  }
}
