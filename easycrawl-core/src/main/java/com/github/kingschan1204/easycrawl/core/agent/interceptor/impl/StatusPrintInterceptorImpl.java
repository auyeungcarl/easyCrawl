package com.github.kingschan1204.easycrawl.core.agent.interceptor.impl;

import com.github.kingschan1204.easycrawl.core.agent.WebAgent;
import com.github.kingschan1204.easycrawl.core.agent.interceptor.AfterInterceptor;
import com.github.kingschan1204.easycrawl.core.agent.result.HttpResult;
import com.github.kingschan1204.easycrawl.helper.http.ResponseAssertHelper;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
/**
 * @author kingschan
 */
@Slf4j
public class StatusPrintInterceptorImpl implements AfterInterceptor {

    @Override
    public HttpResult interceptor(Map<String, Object> data, WebAgent webAgent) {
        HttpResult result = webAgent.getResult();
        log.debug("ContentType : {}", result.contentType());
        log.debug("编码 {} ",result.charset());
        log.debug("Headers : {}", result.headers());
        log.debug("Cookies : {}", result.cookies());
        log.debug("耗时 {} 毫秒",result.timeMillis());
        ResponseAssertHelper.of(result).infer();
        return result;
    }
}
