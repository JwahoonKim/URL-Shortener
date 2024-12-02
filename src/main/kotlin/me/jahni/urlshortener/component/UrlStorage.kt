package me.jahni.urlshortener.component

import com.fasterxml.jackson.databind.ObjectMapper
import me.jahni.urlshortener.infra.db.Url
import me.jahni.urlshortener.infra.db.UrlRepository
import me.jahni.urlshortener.infra.redis.RedisRepository
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class UrlStorage(
    private val redisRepository: RedisRepository,
    private val urlRepository: UrlRepository,
    private val objectMapper: ObjectMapper,
) {

    fun findByOriginalUrl(originalUrl: String): Url? {
        val key = generateOriginalUrlKey(originalUrl)
        return findUrlInCacheOrDb(key, originalUrl) {
            urlRepository.findByOriginalUrl(originalUrl)
        }
    }

    fun findByShortUrl(shortUrl: String): Url? {
        val key = generateShortUrlKey(shortUrl)
        return findUrlInCacheOrDb(key, shortUrl) {
            urlRepository.findByShortUrl(shortUrl)
        }
    }

    fun save(url: Url) {
        urlRepository.save(url)
        cacheUrl(url)
    }

    private fun findUrlInCacheOrDb(key: String, originalUrl: String, dbQuery: () -> Url?): Url? {
        val cached = redisRepository.get(key)
        if (cached != null) {
            return objectMapper.readValue(cached, Url::class.java)
        }

        val url = dbQuery()
        if (url != null) {
            redisRepository.set(
                key = key,
                value = objectMapper.writeValueAsString(url),
                ttl = Duration.ofSeconds(60),
            )
            return url
        }

        return null
    }

    private fun cacheUrl(url: Url) {
        redisRepository.set(
            key = generateOriginalUrlKey(url.originalUrl),
            value = objectMapper.writeValueAsString(url),
            ttl = Duration.ofSeconds(60),
        )

        redisRepository.set(
            key = generateShortUrlKey(url.shortUrl),
            value = objectMapper.writeValueAsString(url),
            ttl = Duration.ofSeconds(60),
        )
    }


    private fun generateOriginalUrlKey(originalUrl: String): String {
        return "original_url:$originalUrl"
    }

    private fun generateShortUrlKey(shortUrl: String): String {
        return "short_url:$shortUrl"
    }
}