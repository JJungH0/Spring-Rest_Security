package nhn.academy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // Redis 작업을 위한 템플릿 객체 생성
        RedisTemplate<String, Object> sessionRedisTemplate = new RedisTemplate<>();


        // Redis 연결 설정
        sessionRedisTemplate.setConnectionFactory(redisConnectionFactory);

        // 키-값 직렬화 방식 설정
        sessionRedisTemplate.setKeySerializer(new StringRedisSerializer()); // 문자열을 그대로 저장(=키)
        sessionRedisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // 객체를 JSON 문자열로 저장/읽기 가능 (=값)

        // Hash 구조용 직렬화 방식 설정
        sessionRedisTemplate.setHashKeySerializer(new StringRedisSerializer()); // Hash 키도 문자열로 직렬화
        sessionRedisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer()); // Hash 값도 Json으로 직렬화
        return sessionRedisTemplate; // 설정 완료된 RedisTemplate 반환
    }
}