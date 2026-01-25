package com.assessment.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class HazelcastConfig {
    @Bean
    public com.hazelcast.core.HazelcastInstance hazelcastInstance() {
        Config config = new Config();
        config.setClusterName("dev-cluster");

        MapConfig productsMap = new MapConfig();
        productsMap.setName("products");
        productsMap.setTimeToLiveSeconds(600);
        config.addMapConfig(productsMap);
        return com.hazelcast.core.Hazelcast.newHazelcastInstance(config);
    }
}