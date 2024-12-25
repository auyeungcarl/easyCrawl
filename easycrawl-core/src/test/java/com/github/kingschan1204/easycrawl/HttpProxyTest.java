package com.github.kingschan1204.easycrawl;

import com.github.kingschan1204.easycrawl.core.agent.WebAgent;
import com.github.kingschan1204.easycrawl.core.agent.dto.ProxyConfig;
import com.github.kingschan1204.easycrawl.core.agent.result.HttpResult;
import com.github.kingschan1204.easycrawl.task.EasyCrawl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.Proxy;

@Slf4j
@DisplayName("http代理测试")
public class HttpProxyTest {
    String apiUrl = "https://myip.ipip.net/json";
    ProxyConfig proxy = ProxyConfig.of(Proxy.Type.HTTP, "127.0.0.1", 27890);

    @DisplayName("http代理测试")
    @Test
    public void ipTest() {
        HttpResult result = new EasyCrawl<HttpResult>()
                .webAgent(WebAgent.agent(WebAgent.Engine.JDK)
                        .url(apiUrl)
                        .proxy(proxy)
                )
                .analyze(r -> r.getResult()).execute();
        System.out.println(result.body());
    }
}
