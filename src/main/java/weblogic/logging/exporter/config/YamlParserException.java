// Copyright (c) 2018, 2021, Oracle and/or its affiliates.
// Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.

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
