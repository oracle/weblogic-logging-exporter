// Copyright (c) 2018, 2021, Oracle and/or its affiliates.
// Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.

package weblogic.logging.exporter.config;

import java.util.ArrayList;
import java.util.List;

class ConfigurationException extends RuntimeException {
  static final String BAD_YAML_FORMAT = "Configuration YAML format has errors";
  public static final String NOT_YAML_FORMAT = "Configuration is not in YAML format";

  private final List<String> context = new ArrayList<>();

  ConfigurationException(String description) {
    super(description);
  }

  @SuppressWarnings("unused")
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
