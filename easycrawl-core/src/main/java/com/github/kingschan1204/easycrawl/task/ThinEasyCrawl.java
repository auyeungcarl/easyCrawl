package com.github.kingschan1204.easycrawl.task;

import com.github.kingschan1204.easycrawl.core.agent.WebAgent;
import com.github.kingschan1204.easycrawl.core.agent.result.HttpResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThinEasyCrawl extends EasyCrawl<HttpResult> {
    private String curl;

    public ThinEasyCrawl(String curl) {
        this(curl, null);
    }

    public ThinEasyCrawl(String curl, WebAgent.Engine engine) {
        String cmd = autoCurl(curl);
        super.webAgent(cmd, engine);
        init();
    }

    void init() {
        this.parserFunction = (webAgent) -> webAgent.getResult();
    }

    String autoCurl(String text) {
        String cmd = String.valueOf(text).trim();
        if (cmd.startsWith("curl")) {
            return text;
        }
        return String.format("curl '%s'", cmd);
    }

    @Override
    public HttpResult execute() {
        HttpResult result = webAgent.execute(this.argsMap).getResult();
        return result;
    }
}
