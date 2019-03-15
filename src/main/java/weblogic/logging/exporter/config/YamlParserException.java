/*
 * Copyright (c) 2018, Oracle and/or its affiliates. All rights reserved.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at http://oss.oracle.com/licenses/upl.
 */

package weblogic.logging.exporter.config;

import org.yaml.snakeyaml.scanner.ScannerException;

/** An exception thrown when there is an error parsing the YAML. */
public class YamlParserException extends ConfigurationException {
  private final ScannerException scannerException;

  YamlParserException(ScannerException scannerException) {
    super(BAD_YAML_FORMAT);
    this.scannerException = scannerException;
  }

  @Override
  public String getMessage() {
    return super.getMessage() + '\n' + scannerException.getMessage();
  }
}
