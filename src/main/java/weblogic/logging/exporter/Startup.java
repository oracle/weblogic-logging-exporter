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
      Logger domainLogger = LoggingHelper.getDomainLogger();
      Logger serverLogger = LoggingHelper.getServerLogger();

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
      if (!config.getEnabled()) {
        System.out.println("WebLogic Logging Exporter is disabled");
      } else {
        boolean atLeastOneEnabled = false;
        if (config.getServerLogEnabled()) {
          serverLogger.addHandler(new LogExportHandler(config));
          atLeastOneEnabled = true;
        }
        if (config.getDomainLogEnabled()) {
          domainLogger.addHandler(new LogExportHandler(config));
          atLeastOneEnabled = true;
        }
        // if no other log was enabled, then turn on server log - to provide
        // backwards compatibility to 1.0.0 where you did not have to say
        // which logs you wanted becuase only server log was supported
        if (!atLeastOneEnabled) {
          serverLogger.addHandler(new LogExportHandler(config));
        }

      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
