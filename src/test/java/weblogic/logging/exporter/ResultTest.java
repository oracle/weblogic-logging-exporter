/*
 * Copyright (c) 2019, Oracle and/or its affiliates. All rights reserved.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at http://oss.oracle.com/licenses/upl.
 */

package weblogic.logging.exporter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test the Result object")
public class ResultTest {

  private static final String EXPECTED_STRING =
      "Result{response='good', status=1, successful=true}";

  @DisplayName("Check constructor and getters")
  @Test
  public void testConstructorAndGetters() {
    Result result = new Result("good", 1, true);

    assertEquals("good", result.getResponse());
    assertEquals(1, result.getStatus());
    assertEquals(true, result.isSuccessful());
  }

  @DisplayName("Check toString() works")
  @Test
  public void checkToString() {
    Result result = new Result("good", 1, true);

    assertEquals(EXPECTED_STRING, result.toString());
  }
}
