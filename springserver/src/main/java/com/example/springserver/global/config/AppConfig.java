package com.example.springserver.global.config;

import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
public class AppConfig {

    @Bean(name = "TrieConfigBean")
    public Trie<String, String> trie() { // Bean으로 trie 하나만 쓰는 것 일관성 유지, 서비스마다 초기화
        return new PatriciaTrie<String>();
    }
}
