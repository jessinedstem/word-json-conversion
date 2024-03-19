package com.edstem.kakfa.wordjsonkafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class WordJsonKafkaConfig {
    @Bean
    public NewTopic newTopic(){
        return TopicBuilder.name("word_json_converter")
                .build();
    }
}
