package com.github.kingschan1204.easycrawl.app;

import com.github.kingschan1204.easycrawl.app.config.AppDefaultConfig;
import com.github.kingschan1204.easycrawl.app.config.ApplicationConfig;

public class Application {
    private static Application instance;
    private ApplicationConfig config;

    private Application() {
        loadConfig();
    }

    public static synchronized Application getInstance() {
        if (instance == null) {
            instance = new Application();
        }
        return instance;
    }

    private void loadConfig() {
        config = YamlParser.parseYaml();
    }

    public ApplicationConfig getConfig() {
        return config;
    }

    public AppDefaultConfig getDefaultConfig(){
        return config.getDefaultConfig();
    }
}