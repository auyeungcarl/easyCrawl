package com.github.kingschan1204.easycrawl.core.agent.result;

import java.util.Map;

public interface HttpResult {
    Long timeMillis();

    Integer statusCode();

    String statusMessage();

    String charset();

    String contentType();

    Map<String, String> cookies();

    byte[] bodyAsByes();

    String body();

    void setBody(String body);

    Map<String, String> headers();
}
