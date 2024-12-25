package com.github.kingschan1204.easycrawl.core.agent.result.impl;

import com.github.kingschan1204.easycrawl.core.agent.dto.HttpRequestConfig;
import com.github.kingschan1204.easycrawl.core.agent.result.HttpResult;
import com.github.kingschan1204.easycrawl.core.agent.utils.HttpFileHelper;
import com.github.kingschan1204.easycrawl.helper.json.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;

import java.io.File;
import java.util.Map;

@Slf4j
public class JsoupHttpResultImpl implements HttpResult {
    //请求耗时  毫秒
    private final Long timeMillis;
    private final Connection.Response response;

    private final HttpRequestConfig requestConfig;
    private String bodyString;
    private byte[] bytes;

    public JsoupHttpResultImpl(Long millis, Connection.Response response, HttpRequestConfig requestConfig) {
        this.timeMillis = System.currentTimeMillis() - millis;
        this.response = response;
        this.requestConfig = requestConfig;
    }

    @Override
    public HttpRequestConfig getConfig() {
        return this.requestConfig;
    }

    @Override
    public Long timeMillis() {
        return timeMillis;
    }

    @Override
    public Integer statusCode() {
        return response.statusCode();
    }


    @Override
    public String charset() {
        return response.charset();
    }

    @Override
    public String contentType() {
        return response.contentType();
    }

    @Override
    public Map<String, String> cookies() {
        return response.cookies();
    }

    @Override
    public byte[] bodyAsByes() {
        if (null == bytes) {
            bytes = response.bodyAsBytes();
        }
        log.info("{} 编码：{} 字节数:{}", response.url().toString(), contentType(), bytes.length);
        return this.bytes;
    }

    @Override
    public String body() {
        if (bodyString == null) {
            bodyString = response.body();
        }
        return this.bodyString;
    }

    @Override
    public void setBody(String body) {
        this.bodyString = body;
    }

    @Override
    public Map<String, String> headers() {
        return response.headers();
    }

    @Override
    public JsonHelper getJson() {
        return JsonHelper.of(body());
    }

    @Override
    public String getText() {
        return body();
    }

    @Override
    public File getFile() {
        return HttpFileHelper.downloadFile(this, getConfig());
    }
}
