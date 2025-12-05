package br.com.api.ecommerce.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator; // Importar
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

import java.time.Duration;

@Configuration
public class RedisConfig {

    @Value("${api.redis.timeout}")
    private Integer redisTimeout;

    @Bean
    public RedisCacheConfiguration defaultCacheConfiguration() {
        ObjectMapper objectMapper = new ObjectMapper();
        
        objectMapper.registerModule(new JavaTimeModule());

        objectMapper.activateDefaultTyping(
            LaissezFaireSubTypeValidator.instance,
            ObjectMapper.DefaultTyping.NON_FINAL
        );

        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(redisTimeout))
                .disableCachingNullValues()
                .serializeValuesWith(SerializationPair.fromSerializer(jsonSerializer));
    }
}
