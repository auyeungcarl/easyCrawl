package com.github.kingschan1204.easycrawl.helper.http;

import com.github.kingschan1204.easycrawl.core.agent.dto.HttpRequestConfig;
import com.github.kingschan1204.easycrawl.core.agent.dto.ProxyConfig;
import com.github.kingschan1204.easycrawl.helper.regex.RegexHelper;

import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * 2024-9-1
 * 将浏览器中F12 -> 网络 -> http请求 ->右键 copy curl (bash)
 *
 * @author kingschan
 */
public class CURLHelper {
    private HttpRequestConfig config;
    private String curlText;

    public CURLHelper(String curlText) {
        this.curlText = curlText;
        this.config = new HttpRequestConfig();
    }

    static Map<String, HttpRequestConfig.Method> methodMap;

    static {
        methodMap = new HashMap<>(3);
        methodMap.put("GET", HttpRequestConfig.Method.GET);
        methodMap.put("POST", HttpRequestConfig.Method.POST);
        methodMap.put("PUT", HttpRequestConfig.Method.PUT);
    }

    /**
     * 提取单引号内容
     */
    final String QUOTATION_MARKS = "'(.*?)'";

    public HttpRequestConfig getConfig() {
        String[] list = curlText.split("\n");
        for (String cmd : list) {
            String text = cmd.replaceAll("(^\\s+)|\\s+\\\\$", "");
            if (text.matches("^(?i)curl\\s+'http(s)?://.*'")) {
                this.config.setUrl(RegexHelper.findFirst(text, QUOTATION_MARKS, 1));
                continue;
            }
            if (text.startsWith("-X")) {
                String method = RegexHelper.findFirst(text, QUOTATION_MARKS, 1);
                this.config.setMethod(methodMap.get(method));
                continue;
            }

            if (text.startsWith("-H")) {
                String head = RegexHelper.findFirst(text, QUOTATION_MARKS, 1);
                String[] headKv = head.split(":");
                String key = headKv[0];
                String value = headKv[1].replaceAll("^\\s+", "");
                if (key.matches("(?i)referer|origin")) {
                    String urls = RegexHelper.findFirst(head, "http(s)?://.*");
                    this.config.addHead(key.toLowerCase(), urls);
                    continue;
                }
                if (key.equalsIgnoreCase("User-Agent")) {
                    this.config.setUseAgent(value);
                    continue;
                } else if (key.equalsIgnoreCase("Cookie")) {
                    String[] items = value.split(";");
                    for (String s : items) {
                        String[] cookiekv = s.split("=");
                        this.config.addCookie(cookiekv[0], cookiekv[1]);
                    }
                    continue;
                } else {
                    this.config.addHead(key, value);
                }
                continue;
            }
            if (text.startsWith("--data-raw")) {
                String body = RegexHelper.findFirst(text, QUOTATION_MARKS, 1);
                this.config.setRequestBody(body);
            }
            if (text.matches("^(--proxy|-x).*")) {
                String body = RegexHelper.findFirst(text, QUOTATION_MARKS, 1);
                if (body.startsWith("http")) {
                    body = body.replace("http://", "");
                    String[] array = body.split(":");
                    this.config.setProxy(new ProxyConfig(Proxy.Type.HTTP, array[0], Integer.parseInt(array[1]), null, null));
                }
            }

        }
        return this.config;
    }
}
