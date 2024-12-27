package com.github.kingschan1204.easycrawl.app;

import com.github.kingschan1204.easycrawl.app.config.ApplicationConfig;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;
@Slf4j
public class YamlParser {
    public static ApplicationConfig parseYaml() {
        log.info("开始解析yaml文件");
        try (InputStream inputStream = YamlParser.class.getClassLoader().getResourceAsStream("easy-crawl.yml")) {
            Constructor constructor = new Constructor(ApplicationConfig.class, new LoaderOptions());
            Yaml yaml = new Yaml(constructor);
            return yaml.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error loading YAML file", e);
        }
    }


}
