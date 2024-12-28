package com.github.kingschan1204.easycrawl.core.agent.utils;

import com.github.kingschan1204.easycrawl.app.Application;
import com.github.kingschan1204.easycrawl.core.agent.dto.HttpRequestConfig;
import com.github.kingschan1204.easycrawl.core.agent.result.HttpResult;
import com.github.kingschan1204.easycrawl.core.agent.result.impl.ApacheHttpResultImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.DecompressingEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.io.CloseMode;
import org.apache.hc.core5.util.Timeout;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ApacheHttpClientHelper {
    final HttpRequestConfig config;

    public ApacheHttpClientHelper(HttpRequestConfig config) {
        this.config = config;
    }

    public HttpResult request() {
        long start = System.currentTimeMillis();
        HttpHost proxy = null;
        if (null != config.getProxy()) {
            // 创建代理配置
            proxy = new HttpHost(config.getProxy().getHost(), config.getProxy().getPort());
        }
        boolean compress = Application.getInstance().getDefaultConfig().getHttpCompress();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.of(config.getConnectTimeout(), TimeUnit.MILLISECONDS))
                .setConnectTimeout(Timeout.of(config.getConnectTimeout(), TimeUnit.MILLISECONDS))
                .setContentCompressionEnabled(compress)
                .setProxy(proxy)
                .build();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
        HttpUriRequestBase request = null;
        switch (config.getMethod()) {
            case POST -> request = new HttpPost(config.getUrl());
            case PUT -> request = new HttpPut(config.getUrl());
            case GET -> request = new HttpGet(config.getUrl());
            default -> {
                throw new RuntimeException("暂不支持该请求方式:" + config.getMethod());
            }
        }
        request.setHeader("User-Agent", config.getUseAgent());
        if (null != config.getHead()) {
            // 将Map中的头信息设置到请求中
            for (Map.Entry<String, String> entry : config.getHead().entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue());
            }
        }
        if (null != config.getCookie()) {
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, String> entry : config.getCookie().entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append(";");
            }
            request.addHeader(HttpHeaders.COOKIE, sb.toString());
        }
        // 设置请求体（仅适用于 POST 和 PUT 请求）
        if (config.getMethod().toString().matches("(?i)post|put") && config.getRequestBody() != null) {
            HttpEntity entity;
            if (config.getRequestBody() instanceof String) { // JSON 或文本
                entity = new StringEntity(config.getRequestBody(), StandardCharsets.UTF_8);
                request.setHeader("Content-Type", "application/json");
            } /*else if (config.getRequestBody() instanceof Map) { // 表单参数
                List<NameValuePair> formParams = new ArrayList<>();
                ((Map<?, ?>) body).forEach((key, value) -> formParams.add(new BasicNameValuePair(key.toString(), value.toString())));
                entity = new UrlEncodedFormEntity(formParams, StandardCharsets.UTF_8);
                request.setHeader("Content-Type", "application/x-www-form-urlencoded");
            }*/ else {
                throw new IllegalArgumentException("Unsupported body type");
            }
            request.setEntity(entity);
        }
        try (CloseableHttpResponse response = httpClient.execute((ClassicHttpRequest) request)) {
//            DecompressingEntity decompressingEntity = (DecompressingEntity) response.getEntity();
            // 开启压缩： org.apache.hc.client5.http.entity.DecompressingEntity
            // 不开启压缩： org.apache.hc.client5.http.impl.classic.ResponseEntityProxy
            byte[] bytes = EntityUtils.toByteArray(response.getEntity());
//            String contentEncoding = response.getHeader("content-encoding").getValue();
            return new ApacheHttpResultImpl(System.currentTimeMillis() - start, response, config, bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close(CloseMode.GRACEFUL);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
