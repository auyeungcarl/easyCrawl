package com.github.kingschan1204.easycrawl.app.config;

import com.github.kingschan1204.easycrawl.core.agent.interceptor.AfterInterceptor;
import com.github.kingschan1204.easycrawl.core.agent.interceptor.PreInterceptor;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class AppDefaultConfig {
  @Getter @Setter String httpEngine;
  @Getter @Setter String useAgent;
  @Getter @Setter Integer connectTimeout;
  @Getter @Setter String fileFolder;
  @Getter @Setter Boolean httpCompress;
  @Setter String[] afterInterceptors;
  @Setter String[] preInterceptors;
  @Getter List<AfterInterceptor> afterInterceptorList;
  @Getter List<PreInterceptor> preInterceptorList;

  public List<AfterInterceptor> getAfterInterceptorList() {
    if (null == afterInterceptorList && afterInterceptors.length > 0) {
      afterInterceptorList = new ArrayList<>();
      for (String afterInterceptor : afterInterceptors) {
        try {
          afterInterceptorList.add(
              (AfterInterceptor) Class.forName(afterInterceptor).newInstance());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return afterInterceptorList;
  }

  public List<PreInterceptor> getPreInterceptorList() {
    preInterceptorList = new ArrayList<>();
    if (null == preInterceptorList && preInterceptors.length > 0) {
      for (String preInterceptor : preInterceptors) {
        try {
          preInterceptorList.add((PreInterceptor) Class.forName(preInterceptor).newInstance());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return preInterceptorList;
  }
}
