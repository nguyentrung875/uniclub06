package com.cybersoft.uniclub.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private int redisPort;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisHost);
        config.setPort(redisPort);

        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(redisHost, redisPort));
    }

//    Tạo ra dạng lưu trữ dữ liệu mới cho Redis (String thuần)
    @Bean
    @Primary
    public RedisTemplate<String, String> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory){
        RedisTemplate<String, String> template = new RedisTemplate<>();
        //Mở kết nối với csdl redis
        template.setConnectionFactory(lettuceConnectionFactory);

        //Hủy kiểu lưu dữ liệu mặc định của redis, chuyển về kiểu String chuẩn để tất cả ngôn ngữ đều dùng được
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }
}
