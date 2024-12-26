package com.github.kingschan1204.easycrawl.task;

import com.github.kingschan1204.easycrawl.core.agent.WebAgent;
import com.github.kingschan1204.easycrawl.core.agent.result.HttpResult;

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

    String autoCurl(String text){
        String cmd = String.valueOf(text).trim();
        if(cmd.startsWith("curl")){
            return text;
        }
        return String.format("curl '%s'",cmd);
    }

    public HttpResult execute() {
        return super.execute();
    }
}
