package com.github.kingschan1204.easycrawl.core.agent.utils;

import com.github.kingschan1204.easycrawl.core.agent.dto.ProxyConfig;

import java.net.InetSocketAddress;
import java.net.Proxy;

public class ProxyHelper {

    public static Proxy proxy(ProxyConfig proxyConfig){
        if(proxyConfig==null){
            return Proxy.NO_PROXY;
        }
        Proxy proxy = new Proxy(proxyConfig.getType(), new InetSocketAddress(proxyConfig.getHost(), proxyConfig.getPort()));
        return proxy;
    }
}
