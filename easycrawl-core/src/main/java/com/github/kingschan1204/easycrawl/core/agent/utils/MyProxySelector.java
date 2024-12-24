package com.github.kingschan1204.easycrawl.core.agent.utils;

import com.github.kingschan1204.easycrawl.core.agent.dto.ProxyConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.List;

@Slf4j
public class MyProxySelector extends ProxySelector {
    private ProxyConfig proxyConfig;

    public MyProxySelector(ProxyConfig proxyConfig) {
        this.proxyConfig = proxyConfig;
    }

    @Override
    public List<Proxy> select(URI uri) {
        Proxy proxy = new Proxy(this.proxyConfig.getType(), new java.net.InetSocketAddress(this.proxyConfig.getHost(), this.proxyConfig.getPort()));
        return List.of(proxy);
    }

    @Override
    public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
        System.err.println("连接代理服务器失败，URI:" + uri + ", 原因: " + ioe.getMessage());
        log.error("连接代理服务器失败，URI:{}, 原因: {} ", uri, ioe.getMessage());
    }
}
