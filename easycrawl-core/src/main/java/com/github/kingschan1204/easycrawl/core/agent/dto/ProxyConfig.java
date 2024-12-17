package com.github.kingschan1204.easycrawl.core.agent.dto;

import lombok.Getter;
import lombok.ToString;

import java.net.Proxy;

@ToString
@Getter
public class ProxyConfig {
    /**
     * 代理类型 http socks
     */
    private Proxy.Type type;
    private String host;
    private Integer port;
    /**
     * 用户名 要认证的情况下
     */
    private String user;
    /**
     * 要认证的情况下
     */
    private String pwd;

    public ProxyConfig(Proxy.Type type, String host, Integer port, String user, String pwd) {
        this.type = type;
        this.host = host;
        this.port = null == port ? 80 : port;
        this.user = user;
        this.pwd = pwd;
    }

    public static ProxyConfig of(Proxy.Type type, String host, Integer port) {
        return new ProxyConfig(type, host, port, null, null);
    }


}
