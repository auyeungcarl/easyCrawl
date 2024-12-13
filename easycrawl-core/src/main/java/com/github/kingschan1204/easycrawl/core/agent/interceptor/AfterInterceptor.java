package com.github.kingschan1204.easycrawl.core.agent.interceptor;

import com.github.kingschan1204.easycrawl.core.agent.WebAgent;
import com.github.kingschan1204.easycrawl.core.agent.result.HttpResult;

import java.util.Map;

/**
 * @author kingschan
 */
public interface AfterInterceptor {

    HttpResult interceptor(Map<String, Object> data, WebAgent webAgent);

}
