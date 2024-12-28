package com.github.kingschan1204.easycrawl.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author kingschan
 */
@Getter
@AllArgsConstructor
public enum HttpHeadKey {
  UserAgent("User-Agent"),
  Referer("Referer"),
  ContentType("Content-Type"),
  Cookie("Cookie"),
  SetCookie("Set-Cookie");

  private final String value;
}
