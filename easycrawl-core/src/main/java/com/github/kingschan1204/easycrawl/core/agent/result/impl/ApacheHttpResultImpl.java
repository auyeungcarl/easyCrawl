package com.github.kingschan1204.easycrawl.core.agent.result.impl;

import com.github.kingschan1204.easycrawl.core.agent.dto.HttpRequestConfig;
import com.github.kingschan1204.easycrawl.core.agent.result.HttpResult;
import com.github.kingschan1204.easycrawl.core.agent.utils.HttpFileHelper;
import com.github.kingschan1204.easycrawl.core.enums.HttpHeadKey;
import com.github.kingschan1204.easycrawl.helper.json.JsonHelper;
import com.github.kingschan1204.easycrawl.helper.regex.RegexHelper;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.Header;

@Slf4j
public class ApacheHttpResultImpl implements HttpResult {
  // 请求耗时  毫秒
  private final Long timeMillis;
  private final CloseableHttpResponse response;
  private final HttpRequestConfig requestConfig;
  //    private final CloseableHttpClient httpClient;
  private String bodyString;
  private final byte[] bytes;
  // 内部
  private String charset;
  private String contentType;
  private Map<String, String> cookies;
  private Map<String, String> headMap;

  public ApacheHttpResultImpl(
      Long timeMillis,
      CloseableHttpResponse response,
      HttpRequestConfig requestConfig,
      byte[] bytes) {
    this.timeMillis = timeMillis;
    this.response = response;
    this.requestConfig = requestConfig;
    this.bytes = bytes;
  }

  /* public ApacheHttpResultImpl(Long timeMillis, HttpRequestConfig requestConfig, byte[] bytes, String charset, String contentType, Map<String, String> cookies, Map<String, String> headMap) {
      this.timeMillis = timeMillis;
      this.requestConfig = requestConfig;
      this.bytes = bytes;
      this.charset = charset;
      this.contentType = contentType;
      this.cookies = cookies;
      this.headMap = headMap;
  }*/

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
    return response.getCode();
  }

  @Override
  public String charset() {
    if (null == charset) {
      this.charset = RegexHelper.findFirst(contentType(), "charset\\s*=\\s*(.*)", 1);
    }
    return this.charset;
  }

  @Override
  public String contentType() {
    if (contentType == null) {
      this.contentType = response.getFirstHeader(HttpHeadKey.ContentType.getValue()).getValue();
    }
    return this.contentType;
  }

  @Override
  public Map<String, String> cookies() {
    if (this.cookies == null) {
      Map<String, String> cookieMap = new HashMap<>();
      // 获取 Set-Cookie 头
      Header[] cookies = response.getHeaders(HttpHeadKey.SetCookie.getValue());
      // 解析 Set-Cookie 头中的 cookies
      for (Header cookieHeader : cookies) {
        String cookieValue = cookieHeader.getValue();
        String[] parts = cookieValue.split(";");
        String[] cookieParts = parts[0].split("=");
        if (cookieParts.length == 2) {
          String name = cookieParts[0].trim();
          String value = cookieParts[1].trim();
          cookieMap.put(name, value);
        }
      }
      this.cookies = cookieMap;
    }
    return this.cookies;
  }

  @Override
  public byte[] bodyAsByes() {
    /* if (null == this.bytes) {
                try {
                    this.bytes = EntityUtils.toByteArray(response.getEntity());
                    String contentEncoding = headers().get("content-encoding");
                    log.info("{} 编码：{} 字节数:{} ", this.requestConfig.getUrl(), contentEncoding, this.bytes.length);
    //                httpClient.close(CloseMode.GRACEFUL);
    //                httpClient.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }*/
    return this.bytes;
  }

  @Override
  public String body() {
    if (this.bodyString == null) {
      try {
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
    if (null == headMap) {
      Map<String, String> headMap = new HashMap<>();
      Header[] headers = response.getHeaders();
      for (Header header : headers) {
        if (header.getName().equalsIgnoreCase(HttpHeadKey.SetCookie.getValue())) {
          continue;
        }
        headMap.put(header.getName(), header.getValue());
      }
      this.headMap = headMap;
    }
    return headMap;
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
