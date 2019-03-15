/*
 * Copyright (c) 2018, 2019, Oracle and/or its affiliates. All rights reserved.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at http://oss.oracle.com/licenses/upl.
 */

package weblogic.logging.exporter;

import java.io.File;
import java.util.logging.Logger;
import weblogic.logging.LoggingHelper;
import weblogic.logging.exporter.config.Config;

public class Startup {

  private static final String DEFAULT_CONFIG_FILE = "config/WebLogicLoggingExporter.yaml";

  public static void main(String[] argv) {
    System.out.println("======================= Weblogic Logging Exporter Startup class called");
    try {
      // Logger logger = LoggingHelper.getDomainLogger();
      Logger logger = LoggingHelper.getServerLogger();

      /*
        We will read from the system variable for the location and name of the configuration file.
        If the environment variable is not set, will use the default file under Domain Home Config directory.
        If the file doesn't exist, give WARNING, and use default.
        It is assumed that when this is integrated to Operator, the system variable will be set.
      */
      String fileName =
          System.getProperty("WEBLOGIC_LOGGING_EXPORTER_CONFIG_FILE", DEFAULT_CONFIG_FILE);
      File file = new File(fileName);
      System.out.println("Reading configuration from file name: " + file.getAbsolutePath());
      Config config = Config.loadConfig(file);
      System.out.println(config);
      if (config.getEnabled()) {
        logger.addHandler(new LogExportHandler(config));
      } else {
        System.out.println("WebLogic Logging Exporter is disabled");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
