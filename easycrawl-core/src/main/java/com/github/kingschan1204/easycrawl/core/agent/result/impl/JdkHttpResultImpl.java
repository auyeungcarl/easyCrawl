package com.github.kingschan1204.easycrawl.core.agent.result.impl;

import com.github.kingschan1204.easycrawl.core.agent.dto.HttpRequestConfig;
import com.github.kingschan1204.easycrawl.core.agent.result.HttpResult;
import com.github.kingschan1204.easycrawl.core.agent.utils.HttpFileHelper;
import com.github.kingschan1204.easycrawl.helper.json.JsonHelper;
import com.github.kingschan1204.easycrawl.helper.regex.RegexHelper;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

@Slf4j
public class JdkHttpResultImpl implements HttpResult {
    //请求耗时  毫秒
    private final Long timeMillis;
    private final HttpResponse response;
    private final HttpRequestConfig requestConfig;
    private String bodyString;
    private byte[] bytes;
    //内部
    private String charset;
    private String contentType;

    public JdkHttpResultImpl(HttpResponse response, Long timeMillis, HttpRequestConfig requestConfig) {
        this.timeMillis = System.currentTimeMillis() - timeMillis;
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
        if (null == charset) {
            this.charset = RegexHelper.findFirst(contentType(), "charset\\s*=\\s*(.*)", 1);
            /*if ((null == charset || charset.isEmpty()) && contentType().matches("text/html.*")) {
                log.warn("未获取到编码信息,有可能中文乱码！尝试自动提取编码！");
                String text = RegexHelper.findFirst(body(), RegexHelper.REGEX_HTML_CHARSET);
//                charset = RegexHelper.findFirst(text, "(?i)charSet(\\s+)?=.*\"").replaceAll("(?i)charSet|=|\"|\\s", "");
                this.charset = RegexHelper.findFirst(text, "charset\\s*=\\s*(.*?)\"", 1);
                if (!charset.isEmpty()) {
                    log.debug("编码提取成功：{}", charset);
                    this.charset = charset;
                }
            }*/
        }
        return this.charset;
    }

    @Override
    public String contentType() {
        if (contentType == null) {
            this.contentType = response.headers().firstValue("Content-Type").orElse("");
        }
        return this.contentType;
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
//                String contentLength = headers().get("content-length");
//                int byteLength = null == contentLength ? httpResponse.body().length : Integer.parseInt(contentLength);
                if ("gzip".equals(contentEncoding)) {
                    this.bytes = decompressGzip(httpResponse.body());
                } else if ("deflate".equals(contentEncoding)) {
                    this.bytes = decompressDeflate(httpResponse.body());
                } else {
                    this.bytes = httpResponse.body();
                }
                log.info("{} 编码：{} 字节数:{} ", response.uri().toString(), contentEncoding, this.bytes.length);
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

              /*  Set<String> set = headers().keySet();
                //有压缩用字节 或者 文件下载 用字节
                if (set.contains("content-encoding") || set.contains("content-disposition")) {
                    this.bodyString = new String(bodyAsByes(), charset());
                } else {
                    HttpResponse<String> httpResponse = (HttpResponse<String>) response;
                    this.bodyString = httpResponse.body();
                }*/

                String charset = charset();
                byte[] bs = bodyAsByes();
                this.bodyString = new String(bs, charset.isEmpty() ? "UTF-8" : charset);


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
