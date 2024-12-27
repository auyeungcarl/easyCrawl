package com.github.kingschan1204.easycrawl.app.config;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class AppDefaultConfig {
    String httpEngine;
    String useAgent;
    Integer connectTimeout;
    String fileFolder;
    Boolean httpCompress;
}
