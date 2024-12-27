package com.github.kingschan1204.easycrawl.core.agent.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kingschan
 */
@NoArgsConstructor
@Data
public class HttpRequestConfig {

    public Map<String, String> cookie;

    public Map<String, String> head;

    public String referer;

    public String url;
    /**
     * 超时时间  毫秒
     */
    public Integer connectTimeout;

    public String useAgent;

    public String requestBody;

    /**
     * 代理
     * curl -x http://user:password@192.168.1.100:8080 https://example.com
     * curl -x http://192.168.1.100:8080 https://example.com
     * curl -x socks5://proxy_host:proxy_port [URL]
     * 如果 SOCKS5 代理需要认证，curl目前没有像 HTTP 代理那样直接在 URL 中嵌入用户名和密码的语法。通常需要结合其他工具或者配置来实现 一种常见的方法是使用proxychains工具
     */
    public ProxyConfig proxy;
    /**
     * http 请求方式
     */
    public Method method = Method.GET;


    //下载文件相关配置
    /**
     * 下载文件保存的目录
     */
    private String folder;
    /**
     * 写入磁盘时的文件名，不传值的时候自动识别，传值的时候用手动指定的文件名
     */
    private String fileName;


    public enum Method {
        GET,
        POST,
        PUT,
//        DELETE,
//        PATCH,
//        HEAD,
//        OPTIONS,
//        TRACE

    }

    /**
     * 添加请求头
     * @param key
     * @param value
     */
    public void addHead(String key, String value) {
        if(null == this.head){
            this.head = new HashMap<>();
        }
        this.head.put(key,value);
    }

    /**
     * 添加cookie
     * @param key
     * @param value
     */
    public void addCookie(String key, String value) {
        if(null == this.cookie){
            this.cookie = new HashMap<>();
        }
        this.cookie.put(key,value);
    }



}
