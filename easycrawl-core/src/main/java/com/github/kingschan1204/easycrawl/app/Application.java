package com.github.kingschan1204.easycrawl.app;

import com.github.kingschan1204.easycrawl.app.config.AppDefaultConfig;
import com.github.kingschan1204.easycrawl.app.config.ApplicationConfig;

public class Application {
    private ApplicationConfig config = YamlParser.parseYaml();

    private Application() {
    }

    // Holer singleton
    private static class Holder {
        private static Application instance = new Application();
    }

    public static Application getInstance() {
        return Holder.instance;
    }


    public ApplicationConfig getConfig() {
        return config;
    }

    public AppDefaultConfig getDefaultConfig() {
        return config.getDefaultConfig();
    }
}