package me.jahni.urlshortner.component

import com.fasterxml.jackson.databind.ObjectMapper
import me.jahni.urlshortner.infra.db.Url
import me.jahni.urlshortner.infra.db.UrlRepository
import me.jahni.urlshortner.infra.redis.RedisRepository
import org.springframework.stereotype.Component

@Component
class UrlStorage(
    private val redisRepository: RedisRepository,
    private val urlRepository: UrlRepository,
    private val objectMapper: ObjectMapper,
) {

    fun findByOriginalUrl(originalUrl: String): Url? {
        val key = generateRedisKey(originalUrl)
        val cacheEntity = redisRepository.get(key)

        if (cacheEntity != null) {
            return objectMapper.readValue(cacheEntity, Url::class.java)
        }

        val urlEntity = urlRepository.findByOriginalUrl(originalUrl)
        if (urlEntity != null) {
            val url = Url(
                id = urlEntity.id,
                shortUrl = urlEntity.shortUrl,
                originalUrl = urlEntity.originalUrl
            )
            redisRepository.set(key, objectMapper.writeValueAsString(url))
            return url
        }

        return null
    }

    private fun generateRedisKey(originalUrl: String): String {
        return "original_url:$originalUrl"
    }

    fun save(url: Url) {
        urlRepository.save(url)
        redisRepository.set(generateRedisKey(url.originalUrl), objectMapper.writeValueAsString(url))
    }
}