package com.github.kingschan1204.easycrawl.execute;

import com.github.kingschan1204.easycrawl.core.agent.WebAgent;

/**
 * @author kingschan
 * 2024-9-21
 */
public interface Crawl<T> {

    Crawl<T> webAgent(WebAgent webAgent) ;
}
