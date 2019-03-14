/*
 * Copyright (c) 2018, Oracle and/or its affiliates. All rights reserved.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at http://oss.oracle.com/licenses/upl.
 */

package weblogic.logging.exporter.config;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationException extends RuntimeException {
  public static final String BAD_YAML_FORMAT = "Configuration YAML format has errors";
  public static final String NOT_YAML_FORMAT = "Configuration is not in YAML format";
  public static final String CONFIG_FILE_NOT_FOUND = "Configuration file cannot be found";

  private List<String> context = new ArrayList<>();

  ConfigurationException(String description) {
    super(description);
  }

  void addContext(String parentContext) {
    context.add(0, parentContext);
  }

  @Override
  public String getMessage() {
    StringBuilder sb = new StringBuilder(super.getMessage());
    if (!context.isEmpty()) sb.append(" at ").append(String.join(".", context));
    return sb.toString();
  }
}
