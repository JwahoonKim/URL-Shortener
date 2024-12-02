package me.jahni.urlshortener.component

import com.fasterxml.jackson.databind.ObjectMapper
import me.jahni.urlshortener.infra.db.Url
import me.jahni.urlshortener.infra.db.UrlRepository
import me.jahni.urlshortener.infra.redis.RedisRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UrlStorageTest {

    private val redisRepository: RedisRepository = mock()
    private val urlRepository: UrlRepository = mock()
    private val objectMapper: ObjectMapper = ObjectMapper()
    private val urlStorage = UrlStorage(redisRepository, urlRepository, objectMapper)

    @Test
    fun `원본_주소로_찾기_캐시_적중`() {
        // Given
        val originalUrl = "https://example.com"
        val url = Url(1L, "abc123", originalUrl)

        `when`(redisRepository.get("original_url:$originalUrl"))
            .thenReturn(objectMapper.writeValueAsString(url))

        // When
        val result = urlStorage.findByOriginalUrl(originalUrl)!!

        // Then
        assertNotNull(result)
        assertThat(result.id).isEqualTo(url.id)
        assertThat(result.shortUrl).isEqualTo(url.shortUrl)
        assertThat(result.originalUrl).isEqualTo(url.originalUrl)
        verify(urlRepository, never()).findByOriginalUrl(originalUrl)
    }

    @Test
    fun `원본_주소로_찾기_DB_조회`() {
        // Given
        val originalUrl = "https://example.com"
        val url = Url(1L, "abc123", originalUrl)

        `when`(redisRepository.get("original_url:$originalUrl"))
            .thenReturn(null)

        `when`(urlRepository.findByOriginalUrl(originalUrl))
            .thenReturn(url)

        // When
        val result = urlStorage.findByOriginalUrl(originalUrl)!!

        // Then
        assertNotNull(result)
        assertEquals(url.id, result.id)
        assertEquals(url.shortUrl, result.shortUrl)
        assertEquals(url.originalUrl, result.originalUrl)
        verify(redisRepository).set("original_url:$originalUrl", objectMapper.writeValueAsString(url))
    }

    @Test
    fun `원본_주소로_찾기_실패`() {
        // Given
        val originalUrl = "https://nonexistent.com"

        `when`(redisRepository.get("original_url:$originalUrl"))
            .thenReturn(null)

        `when`(urlRepository.findByOriginalUrl(originalUrl))
            .thenReturn(null)

        // When
        val result = urlStorage.findByOriginalUrl(originalUrl)

        // Then
        assertNull(result)
    }

    @Test
    fun `URL_저장_테스트`() {
        // Given
        val url = Url(1L, "abc123", "https://example.com")

        // When
        urlStorage.save(url)

        // Then
        verify(urlRepository).save(url)
        verify(redisRepository).set("original_url:${url.originalUrl}", objectMapper.writeValueAsString(url))
    }
}
