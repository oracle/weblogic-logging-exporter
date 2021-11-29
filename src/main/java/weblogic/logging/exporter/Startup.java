// Copyright (c) 2018, 2021, Oracle and/or its affiliates.
// Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.

package weblogic.logging.exporter;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import weblogic.logging.LoggingHelper;
import weblogic.logging.ServerLoggingHandler;
import weblogic.logging.exporter.config.Config;

public class Startup {

  private static final String DEFAULT_CONFIG_FILE = "config/WebLogicLoggingExporter.yaml";

  public static void main(String[] argv) {
    System.out.println("======================= WebLogic Logging Exporter Startup class called");
    try {
      Logger logger = LoggingHelper.getServerLogger();

      /*
        We will read from the system variable for the location and name of the configuration file.
        If the environment variable is not set, will use the default file under Domain Home Config directory.
        If the file doesn't exist, give WARNING, and use default.
        It is assumed that when this is integrated to Operator, the system variable will be set.
      */

      String fileName =
              System.getProperty(
                      "WEBLOGIC_LOGGING_EXPORTER_CONFIG_FILE",
                      System.getenv("WEBLOGIC_LOGGING_EXPORTER_CONFIG_FILE"));
      System.out.println(
              "JavaProperty/EnvVariable WEBLOGIC_LOGGING_EXPORTER_CONFIG_FILE:" + fileName);
      if (fileName == null || fileName.isEmpty()) {
        System.out.println(
                "Env variable WEBLOGIC_LOGGING_EXPORTER_CONFIG_FILE is not set. Defaulting to:"
                        + DEFAULT_CONFIG_FILE);
        fileName = DEFAULT_CONFIG_FILE;
      }
      File file = new File(fileName);
      System.out.println("Reading configuration from file name: " + file.getAbsolutePath());
      Config config = Config.loadConfig(file);
      System.out.println(config);

      //  Elastic log handler is enabled or the file log handler
      if (config.getEnabled()) {
        logger.addHandler(new LogExportHandler(config));
      } else if (config.isFileLoggingEnabled()) {
        // Because of this bridge log messages in the applications themselves are being forwarded to the server logger.
        // so that logging in ear/war artifacts are also visible to the server logger and appear in the JSON log file.
        Logger.getLogger("").addHandler(new ServerLoggingHandler());

        // Register a file handler using the provided config
        FileHandler fh = new FileHandler(config.getOutputFile(), config.getMaxFileSize(), config.getGetMaxRollbackFiles(), config.getAppendToFile());
        fh.setLevel(Level.parse(config.getFileLoggingLogLevel()));
        fh.setFormatter(new WebLogicLogFormatter(config.getDomainUID()));
        logger.addHandler(fh);
      } else {
        System.out.println("WebLogic Elasticsearch Logging Exporter is disabled");
      }
      // also catch errors so that WebLogic does not crash when a required library was not placed in the classpath correctly.
    } catch (Error | Exception e) {
      System.out.println("======================= Something went wrong, the WebLogic Logging Exporter is not activated");
      e.printStackTrace();
    }
  }
}
