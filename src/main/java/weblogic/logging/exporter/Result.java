/*
 * Copyright (c) 2018, Oracle and/or its affiliates. All rights reserved.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at http://oss.oracle.com/licenses/upl.
 */

package weblogic.logging.exporter;

/**
 * Holder of response received from REST requests invoked using methods in {@link LogExportHandler}
 * class
 */
@SuppressWarnings("unused")
class Result {

  private final String response;
  private final int status;
  final boolean successful;

  public Result(String response, int status, boolean successful) {
    this.response = response;
    this.status = status;
    this.successful = successful;
  }

  /** @return The String response received from the REST request */
  public String getResponse() {
    return response;
  }

  /** @return HTTP status code from the REST request */
  public int getStatus() {
    return status;
  }

  /**
   * @return True if the REST request returns a status code that indicates successful request, false
   *     otherwise
   */
  public boolean isSuccessful() {
    return successful;
  }

  @Override
  public String toString() {
    return "Result{"
        + "response='"
        + response
        + '\''
        + ", status="
        + status
        + ", successful="
        + successful
        + '}';
  }
}
