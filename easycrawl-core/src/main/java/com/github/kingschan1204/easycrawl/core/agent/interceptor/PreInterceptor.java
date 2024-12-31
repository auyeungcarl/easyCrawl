package com.github.kingschan1204.easycrawl.core.agent.interceptor;

import com.github.kingschan1204.easycrawl.core.agent.WebAgent;
import com.github.kingschan1204.easycrawl.core.agent.dto.HttpRequestConfig;
import java.util.Map;

/**
 * @author kingschan 2024-12-31
 */
public interface PreInterceptor {

  HttpRequestConfig interceptor(Map<String, Object> data, WebAgent webAgent);
}
