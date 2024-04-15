package com.consumer.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory

@Configuration
@EnableKafka
class KafkaConsumerConfig (
) {
    @Bean
    fun consumerFactory(): ConsumerFactory<String, String> {
        val config = HashMap<String, Any>()
        config[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:29092"
        config[ConsumerConfig.GROUP_ID_CONFIG] = "point"
        config[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] =
            org.apache.kafka.common.serialization.StringDeserializer::class.java
        config[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] =
            org.apache.kafka.common.serialization.StringDeserializer::class.java
        config[ConsumerConfig.ISOLATION_LEVEL_CONFIG] = "read_committed"
        return DefaultKafkaConsumerFactory(config)
    }

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> {
        val factory: ConcurrentKafkaListenerContainerFactory<String, String> =
            ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.consumerFactory = consumerFactory()
        return factory
    }
}