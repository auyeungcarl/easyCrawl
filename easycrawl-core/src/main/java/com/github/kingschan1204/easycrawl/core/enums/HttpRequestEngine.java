package com.github.kingschan1204.easycrawl.core.enums;

import java.util.Arrays;
import java.util.Optional;

public enum HttpRequestEngine {
  JDK("jdk"),
  JSOUP("jsoup"),
  HTTPCLIENT5("httpclient5");

  private final String value;

  HttpRequestEngine(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static HttpRequestEngine fromValue(String value) {
    return Arrays.stream(values())
        .filter(engine -> engine.getValue().equals(value))
        .findFirst()
        .orElse(null);
  }

  public static Optional<HttpRequestEngine> fromValueAsOptional(String value) {
    return Arrays.stream(values()).filter(engine -> engine.getValue().equals(value)).findFirst();
  }
}
