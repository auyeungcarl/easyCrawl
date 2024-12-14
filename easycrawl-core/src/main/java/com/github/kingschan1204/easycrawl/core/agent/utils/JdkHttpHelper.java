package com.github.kingschan1204.easycrawl.core.agent.utils;

import com.github.kingschan1204.easycrawl.core.agent.dto.HttpRequestConfig;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

public class JdkHttpHelper {

    final HttpRequestConfig config;

    public JdkHttpHelper(HttpRequestConfig config) {
        this.config = config;
    }

    public HttpResponse request() {
        HttpResponse response = null;
        try {
            response = httpResponse();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }


    private HttpClient httpClient() {
        HttpClient.Builder builder = HttpClient.newBuilder();
        /*if (config.getProxy() != null) {
            builder.proxy(ProxySelector.of(config.getProxy()));
        }*/
        if (config.getConnectTimeout() != null) {
            builder.connectTimeout(Duration.ofMillis(config.getConnectTimeout()));
        }
//        builder.version(HttpClient.Version.HTTP_2);
        return builder.build();
    }

    private HttpRequest httpRequest() {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        if (config.getUrl() != null) {
            builder.uri(URI.create(config.getUrl()));
        }
        if (config.getHead() != null) {
            for (Map.Entry<String, String> entry : config.getHead().entrySet()) {
                if (entry.getKey().matches("(?i)connection")) {
                    continue;
                }
                builder.header(entry.getKey(), entry.getValue());
            }

        }
//        if (config.getRequestBody() != null) {
//            builder.POST(HttpRequest.BodyPublishers.ofString(config.getRequestBody()));
//        }
        if (null != config.getReferer()) {
            builder.header("Referer", config.getReferer());
        }
        switch (config.getMethod()) {
            case GET -> builder.GET();
            case POST -> builder.POST(HttpRequest.BodyPublishers.ofString(config.getRequestBody()));
            case PUT -> builder.PUT(HttpRequest.BodyPublishers.ofString(config.getRequestBody()));
            default -> throw new RuntimeException("目前只支持：get,post,put 方法！");
        }
        if (null != config.getCookie()) {
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, String> entry : config.getCookie().entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append(";");
            }
            builder.header("Cookie", sb.toString());
        }
        return builder.build();
    }

    private HttpResponse httpResponse() throws Exception {
       /* if (null != config.getFileName() || null != config.getFolder() || (null != config.getHead() && config.getHead().containsKey("Accept-Encoding"))) {
            //下载文件
            HttpResponse<byte[]> response = httpClient().send(httpRequest(), HttpResponse.BodyHandlers.ofByteArray());
            return response;
        } else {
//            HttpResponse<String> response = httpClient().send(httpRequest(), HttpResponse.BodyHandlers.ofString());
            HttpResponse<byte[]> response = httpClient().send(httpRequest(), HttpResponse.BodyHandlers.ofByteArray());
            return response;
        }*/

        HttpResponse<byte[]> response = httpClient().send(httpRequest(), HttpResponse.BodyHandlers.ofByteArray());
        return response;

    }
}
