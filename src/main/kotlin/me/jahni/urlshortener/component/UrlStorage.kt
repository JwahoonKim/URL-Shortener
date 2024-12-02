package me.jahni.urlshortener.component

import com.fasterxml.jackson.databind.ObjectMapper
import me.jahni.urlshortener.infra.db.Url
import me.jahni.urlshortener.infra.db.UrlRepository
import me.jahni.urlshortener.infra.redis.RedisRepository
import org.springframework.stereotype.Component

@Component
class UrlStorage(
    private val redisRepository: RedisRepository,
    private val urlRepository: UrlRepository,
    private val objectMapper: ObjectMapper,
) {

    fun findByOriginalUrl(originalUrl: String): Url? {
        val key = generateOriginalUrlKey(originalUrl)
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

    fun save(url: Url) {
        urlRepository.save(url)
        redisRepository.set(generateOriginalUrlKey(url.originalUrl), objectMapper.writeValueAsString(url))
        redisRepository.set(generateShortUrlKey(url.shortUrl), objectMapper.writeValueAsString(url))
    }

    private fun generateOriginalUrlKey(originalUrl: String): String {
        return "original_url:$originalUrl"
    }

    private fun generateShortUrlKey(shortUrl: String): String {
        return "short_url:$shortUrl"
    }
}