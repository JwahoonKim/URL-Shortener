package me.jahni.urlshortener.infra.redis

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class RedisRepository(
    redisTemplate: RedisTemplate<String, String>
) {
    val ops = redisTemplate.opsForValue()

    fun get(key: String): String? {
        return ops.get(key)
    }

    fun set(
        key: String,
        value: String,
        ttl: Duration = Duration.ofSeconds(60)
    ) {
        ops.set(key, value, ttl)
    }

    fun increment(key: String, delta: Long): Long {
        return ops.increment(key, delta)!!
    }
}