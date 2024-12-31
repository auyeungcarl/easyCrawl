package com.github.kingschan1204.easycrawl.core.agent.interceptor.afterimpl;

import com.github.kingschan1204.easycrawl.core.agent.WebAgent;
import com.github.kingschan1204.easycrawl.core.agent.interceptor.AfterInterceptor;
import com.github.kingschan1204.easycrawl.core.agent.result.HttpResult;
import com.github.kingschan1204.easycrawl.helper.http.ResponseAssertHelper;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kingschan
 */
@Slf4j
public class StatusPrintInterceptorImpl implements AfterInterceptor {

  @Override
  public HttpResult interceptor(Map<String, Object> data, WebAgent webAgent) {
    HttpResult result = webAgent.getResult();
    log.info(
        "{} -> ContentType : {} 编码 {} 耗时 {} 毫秒",
        webAgent.getClass().getSimpleName(),
        result.contentType(),
        result.charset(),
        result.timeMillis());
    ResponseAssertHelper.of(result).infer();
    return result;
  }
}
