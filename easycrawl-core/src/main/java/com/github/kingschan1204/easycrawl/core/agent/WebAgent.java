package com.github.kingschan1204.easycrawl.core.agent;


import com.github.kingschan1204.easycrawl.app.Application;
import com.github.kingschan1204.easycrawl.core.agent.dto.HttpRequestConfig;
import com.github.kingschan1204.easycrawl.core.agent.dto.ProxyConfig;
import com.github.kingschan1204.easycrawl.core.agent.impl.ApacheHttpClientAgent;
import com.github.kingschan1204.easycrawl.core.agent.impl.JdkHttpAgent;
import com.github.kingschan1204.easycrawl.core.agent.impl.JsoupHttp1Agent;
import com.github.kingschan1204.easycrawl.core.agent.interceptor.impl.StatusPrintInterceptorImpl;
import com.github.kingschan1204.easycrawl.core.agent.interceptor.impl.TranscodingInterceptorImpl;
import com.github.kingschan1204.easycrawl.core.agent.result.HttpResult;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * @author kings.chan
 * 2023-04-21
 **/

public interface WebAgent {

    enum Engine {
        JDK("jdk"),
        JSOUP("jsoup"),
        HTTPCLIENT5("httpclient5");

        private String value;

        Engine(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public static Engine fromValue(String value) {
            return Arrays.stream(Engine.values())
                    .filter(engine -> engine.getValue().equals(value))
                    .findFirst()
                    .orElse(null);
        }

        public static Optional<Engine> fromValueAsOptional(String value) {
            return Arrays.stream(Engine.values())
                    .filter(engine -> engine.getValue().equals(value))
                    .findFirst();
        }
    }

    /**
     * 默认agent
     *
     * @return GenericHttp1Agent
     */
    static WebAgent agent() {
        return agent(null, null);
    }

    static WebAgent agent(Engine engine) {
        return agent(null, engine);
    }

    static WebAgent agent(HttpRequestConfig config) {
        return agent(config, null);
    }

    static WebAgent agent(HttpRequestConfig config, Engine engine) {
        WebAgent agent = null;
        if (engine == null) {
            engine = Engine.fromValue(Application.getInstance().getDefaultConfig().getHttpEngine());
        }
        switch (engine) {
            case JSOUP -> agent = new JsoupHttp1Agent();
            case JDK -> agent = new JdkHttpAgent();
            case HTTPCLIENT5 -> agent = new ApacheHttpClientAgent();
        }
        GenericHttp1AgentProxy proxy = new GenericHttp1AgentProxy(
                agent,
                new StatusPrintInterceptorImpl(),
                new TranscodingInterceptorImpl()
        );
        proxy.config(config);
        return proxy;
    }


    HttpRequestConfig getConfig();

    WebAgent config(HttpRequestConfig config);

    WebAgent url(String url);

    WebAgent referer(String referer);

    WebAgent method(HttpRequestConfig.Method method);

    WebAgent head(Map<String, String> head);

    WebAgent head(String key, String value);

    WebAgent useAgent(String useAgent);

    WebAgent cookie(Map<String, String> cookie);

    WebAgent cookie(String key, String value);

    /**
     * 超时时间单位毫秒
     *
     * @param timeOut 超时时间(毫秒)
     * @return
     */
    WebAgent timeOut(Integer timeOut);

    WebAgent proxy(ProxyConfig config);

    WebAgent body(String body);

    WebAgent folder(String folder);

    WebAgent fileName(String fileName);

    WebAgent execute(Map<String, Object> data);

    HttpResult getResult();


}
