package com.github.kingschan1204.easycrawl.core.agent.impl;

import com.github.kingschan1204.easycrawl.core.agent.WebAgent;
import com.github.kingschan1204.easycrawl.core.agent.dto.HttpRequestConfig;
import com.github.kingschan1204.easycrawl.core.agent.dto.ProxyConfig;
import com.github.kingschan1204.easycrawl.core.agent.result.HttpResult;
import com.github.kingschan1204.easycrawl.core.agent.result.impl.JdkHttpResultImpl;
import com.github.kingschan1204.easycrawl.core.agent.utils.JdkHttpHelper;
import com.github.kingschan1204.easycrawl.core.variable.ScanVariable;
import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kingschan
 * 2024-12-12
 */
@Slf4j
public class JdkHttpAgent implements WebAgent {

    private HttpRequestConfig config;
    private HttpResult result;

    public JdkHttpAgent(HttpRequestConfig config) {
        this.config = config;
    }

    public JdkHttpAgent() {
        this.config = new HttpRequestConfig();
    }

    public static WebAgent of(HttpRequestConfig config) {
        return new JdkHttpAgent(config);
    }

    @Override
    public HttpRequestConfig getConfig() {
        return this.config;
    }

    @Override
    public WebAgent config(HttpRequestConfig config) {
        this.config = config;
        return this;
    }

    @Override
    public WebAgent url(String url) {
        this.config.setUrl(url);
        return this;
    }

    @Override
    public WebAgent referer(String referer) {
        this.config.setReferer(referer);
        return this;
    }

    @Override
    public WebAgent method(HttpRequestConfig.Method method) {
        this.config.setMethod(method);
        return this;
    }

    @Override
    public WebAgent head(Map<String, String> head) {
        this.config.setHead(head);
        return this;
    }

    @Override
    public WebAgent head(String key, String value) {
        if (null == this.config.getHead()) {
            this.config.head = new HashMap<>();
        }
        this.config.getHead().put(key, value);
        return this;
    }

    @Override
    public WebAgent useAgent(String useAgent) {
        this.config.setUseAgent(useAgent);
        return this;
    }

    @Override
    public WebAgent cookie(Map<String, String> cookie) {
        this.config.setCookie(cookie);
        return this;
    }

    @Override
    public WebAgent cookie(String key, String value) {
        if (null == this.config.getCookie()) {
            this.config.cookie = new HashMap<>();
        }
        this.config.getCookie().put(key, value);
        return this;
    }

    @Override
    public WebAgent timeOut(Integer timeOut) {
        this.config.setConnectTimeout(timeOut);
        return this;
    }

    @Override
    public WebAgent proxy(ProxyConfig config) {
        this.config.setProxy(config);
        return this;
    }
   /* @Override
    public WebAgent proxy(Proxy proxy) {
        this.config.setProxy(proxy);
        return this;
    }

    @Override
    public WebAgent proxy(Proxy.Type type, String host, int port) {
        this.config.setProxy(new Proxy(type, new InetSocketAddress(host, port)));
        return this;
    }*/

    @Override
    public WebAgent body(String body) {
        this.config.setRequestBody(body);
        return this;
    }

    @Override
    public WebAgent folder(String folder) {
        this.config.setFolder(folder);
        return this;
    }

    @Override
    public WebAgent fileName(String fileName) {
        this.config.setFileName(fileName);
        return this;
    }

    @Override
    public WebAgent execute(Map<String, Object> data) {
        long start = System.currentTimeMillis();
        String httpUrl = ScanVariable.parser(this.config.getUrl(), data).trim();
        String referer = null != this.config.getReferer() ? ScanVariable.parser(this.config.getReferer(), data).trim() : null;
        this.config.setUrl(httpUrl);
        this.config.setReferer(referer);
        this.config.addHead("Accept-Encoding", "gzip, deflate");
        HttpResponse response = new JdkHttpHelper(config).request();
        this.result = new JdkHttpResultImpl(response, start, this.config);
        return this;
    }

    @Override
    public HttpResult getResult() {
        return this.result;
    }

    /*@Override
    public JsonHelper getJson() {
        return JsonHelper.of(this.result.body());
    }

    @Override
    public String getText() {
        return getResult().body();
    }

    @Override
    public File getFile() {
        return HttpFileHelper.downloadFile(result, config);
    }*/
}
