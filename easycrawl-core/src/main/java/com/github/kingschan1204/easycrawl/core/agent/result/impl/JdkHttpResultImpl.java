package com.github.kingschan1204.easycrawl.core.agent.result.impl;

import com.github.kingschan1204.easycrawl.core.agent.result.HttpResult;
import com.github.kingschan1204.easycrawl.helper.regex.RegexHelper;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

@Slf4j
public class JdkHttpResultImpl implements HttpResult {
    //请求耗时  毫秒
    private final Long timeMillis;
    private final HttpResponse response;
    private String bodyString;
    private byte[] bytes;

    private String defaultCharset = "UTF-8";

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
        Map<String, String> heads = new LinkedHashMap<>(map.size());
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();
            if (key.equalsIgnoreCase("Set-Cookie")) {
                for (String cookie : values) {
                    String cookieValue = RegexHelper.findFirst(cookie, "([^;]*)(?=;)");
                    String[] array = cookieValue.split("=");
                    heads.put(array[0], array.length == 2 ? array[1] : "");
                }

            }
        }
        return heads;
    }

    @Override
    public byte[] bodyAsByes() {
        if (null == bytes) {
            try {
                HttpResponse<byte[]> httpResponse = (HttpResponse<byte[]>) response;
                byte[] bytes = httpResponse.body();
                //获取Content-Encoding响应头，判断实际使用的压缩格式
                String contentEncoding = headers().get("content-encoding");
                log.info("{} 编码：{} 字节数:{}", response.uri().toString(), contentEncoding, bytes.length);
                if ("gzip".equals(contentEncoding)) {
                    this.bytes = decompressGzip(bytes);
                } else if ("deflate".equals(contentEncoding)) {
                    this.bytes = decompressDeflate(bytes);
                } else {
                    this.bytes = bytes;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return this.bytes;
    }

    @Override
    public String body() {
        if (this.bodyString == null) {
            try {

                Set<String> set = headers().keySet();
                //有压缩用字节 或者 文件下载 用字节
                if (set.contains("content-encoding") || set.contains("content-disposition")) {
                    this.bodyString = new String(bodyAsByes(), defaultCharset);
                } else {
                    HttpResponse<String> httpResponse = (HttpResponse<String>) response;
                    this.bodyString = httpResponse.body();
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

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
        Map<String, String> heads = new LinkedHashMap<>(map.size());
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            //统一小写
            String key = entry.getKey().toLowerCase();
            List<String> values = entry.getValue();
            if (key.equalsIgnoreCase("Set-Cookie")) {
                List<String> list = new ArrayList<>();
                for (String cookie : values) {
                    list.add(RegexHelper.findFirst(cookie, "([^;]*)(?=;)"));
                }
                heads.put(key, list.stream().collect(Collectors.joining(";")));
            }
            heads.put(key, values.stream().collect(Collectors.joining(",")));
        }
        return heads;
    }

//--------------------------------------content-encoding 解码------------------------------------------------------------

    private static byte[] decompressGzip(byte[] compressedData) throws IOException {
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(compressedData));
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gzipInputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
            return outputStream.toByteArray();
        }
    }

    private static byte[] decompressDeflate(byte[] compressedData) throws IOException {
        try (InflaterInputStream inflaterInputStream = new InflaterInputStream(new ByteArrayInputStream(compressedData));
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inflaterInputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
            return outputStream.toByteArray();
        }
    }
}
