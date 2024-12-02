package me.jahni.urlshortner.component

import me.jahni.urlshortner.infra.redis.RedisRepository
import org.springframework.stereotype.Component

private const val REDIS_UNIQUE_ID_KEY = "unique_id"
private const val ID_DELTA = 1L

@Component
class UniqueIdGenerator(
    private val redisRepository: RedisRepository,
) {
    fun generate(): Long {
        return redisRepository.increment(REDIS_UNIQUE_ID_KEY, ID_DELTA)
    }
}