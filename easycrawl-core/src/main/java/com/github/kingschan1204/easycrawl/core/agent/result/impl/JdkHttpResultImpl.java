package com.github.kingschan1204.easycrawl.core.agent.result.impl;

import com.github.kingschan1204.easycrawl.core.agent.result.HttpResult;
import com.github.kingschan1204.easycrawl.helper.regex.RegexHelper;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JdkHttpResultImpl implements HttpResult {
    //请求耗时  毫秒
    private final Long timeMillis;
    private final HttpResponse response;
    private String bodyString;
    public JdkHttpResultImpl(HttpResponse response, Long timeMillis) {
        this.timeMillis = System.currentTimeMillis() - timeMillis;
        this.response = response;

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
    public String statusMessage() {
        return null;
    }

    @Override
    public String charset() {
        return null;
    }

    @Override
    public String contentType() {
        return response.headers().firstValue("Content-Type").orElse("");
    }

    @Override
    public Map<String, String> cookies() {
        Map<String, List<String>> map = response.headers().map();
        Map<String,String> heads = new LinkedHashMap<>(map.size());
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();
            if(key.equalsIgnoreCase("Set-Cookie")){
                for(String cookie : values){
                    String cookieValue = RegexHelper.findFirst(cookie,"([^;]*)(?=;)");
                    String[] array = cookieValue.split("=");
                    heads.put(array[0],array.length ==2 ? array[1] : "");
                }

            }
        }
        return heads;
    }

    @Override
    public byte[] bodyAsByes() {
        HttpResponse<byte[]> httpResponse = (HttpResponse<byte[]>) response;
        return httpResponse.body();
    }

    @Override
    public String body() {
        if(this.bodyString == null){
            HttpResponse<String> httpResponse = (HttpResponse<String>) response;
            return httpResponse.body();
        }
       return this.bodyString;
    }

    @Override
    public void setBody(String body) {
         this.bodyString = body;
    }

    @Override
    public Map<String, String> headers() {
        Map<String, List<String>> map = response.headers().map();
        Map<String,String> heads = new LinkedHashMap<>(map.size());
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();
            if(key.equalsIgnoreCase("Set-Cookie")){
                List<String> list = new ArrayList<>();
                for(String cookie : values){
                    list.add(RegexHelper.findFirst(cookie,"([^;]*)(?=;)"));
                }
                heads.put(key,list.stream().collect(Collectors.joining(";")));
            }
            heads.put(key,values.stream().collect(Collectors.joining(",")));
        }
        return heads;
    }
}
