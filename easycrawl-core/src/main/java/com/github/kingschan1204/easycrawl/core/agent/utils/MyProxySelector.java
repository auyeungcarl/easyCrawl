package com.github.kingschan1204.easycrawl.core.agent.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MyProxySelector extends ProxySelector {
    private Proxy proxy;

    public MyProxySelector(Proxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public List<Proxy> select(URI uri) {
        List<Proxy> result = new ArrayList<>();
        result.add(proxy);
        return result;
    }

    @Override
    public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
        log.error("连接代理服务器失败，URI:{}, 原因: {} ", uri, ioe.getMessage());
    }
}
