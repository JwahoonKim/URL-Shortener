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
        val cached = redisRepository.get(key)

        if (cached != null) {
            return objectMapper.readValue(cached, Url::class.java)
        }

        val urlEntity = urlRepository.findByOriginalUrl(originalUrl)
        if (urlEntity != null) {
            val url = Url(
                id = urlEntity.id,
                shortUrl = urlEntity.shortUrl,
                originalUrl = urlEntity.originalUrl
            )
            redisRepository.set(
                key = key,
                value = objectMapper.writeValueAsString(url),
                ttl = Duration.ofSeconds(60),
            )
            return url
        }

        return null
    }

    fun save(url: Url) {
        urlRepository.save(url)

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

    fun findByShortUrl(shortUrl: String): Url? {
        val key = generateShortUrlKey(shortUrl)
        val cached = redisRepository.get(key)

        if (cached != null) {
            return objectMapper.readValue(cached, Url::class.java)
        }

        val urlEntity = urlRepository.findByShortUrl(shortUrl)

        if (urlEntity != null) {
            redisRepository.set(
                key = key,
                value = objectMapper.writeValueAsString(urlEntity),
                ttl = Duration.ofSeconds(60),
            )
            return urlEntity
        }

        return null
    }

    private fun generateOriginalUrlKey(originalUrl: String): String {
        return "original_url:$originalUrl"
    }

    private fun generateShortUrlKey(shortUrl: String): String {
        return "short_url:$shortUrl"
    }
}