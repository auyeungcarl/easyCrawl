package com.github.kingschan1204.easycrawl;

import com.github.kingschan1204.easycrawl.task.EasyCrawl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

@Slf4j
@DisplayName("cookie相关测试")
public class CookieTest {
    @DisplayName("获取响应cookie")
    @Test()
    public void responseCookieTest() {
        String curl = """
                curl 'https://www.xueqiu.com/about'
                """;
        Map<String, String> cookies = new EasyCrawl<Map<String, String>>()
                .webAgent(curl)
                .analyze(r -> r.getResult().cookies())
                .execute();
        System.out.println(cookies);
    }


}
