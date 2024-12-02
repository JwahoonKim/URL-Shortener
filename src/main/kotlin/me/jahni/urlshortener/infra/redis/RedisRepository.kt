package me.jahni.urlshortener.infra.redis

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class RedisRepository(
    redisTemplate: RedisTemplate<String, String>
) {
    val ops = redisTemplate.opsForValue()
    val setOps = redisTemplate.opsForSet()

    fun get(key: String): String? {
        return ops.get(key)
    }

    fun set(key: String, value: String) {
        ops.set(key, value)
    }

    fun increment(key: String, delta: Long): Long {
        return ops.increment(key, delta)!!
    }

    fun isMember(key: String, value: String): Boolean {
        return setOps.isMember(key, value)!!
    }

    fun add(key: String, value: String) {
        setOps.add(key, value)
    }
}