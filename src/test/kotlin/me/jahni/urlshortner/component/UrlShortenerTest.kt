package me.jahni.urlshortner.component

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.Math.pow
import kotlin.math.pow

class UrlShortenerTest {

    private val urlShortener = UrlShortener()

    @Test
    fun `ID를 Base62로 변환했을 때 결과가 올바르게 반환된다`() {
        // Given
        val id = 125L

        // When
        val result = urlShortener.shorten(id)

        // Then
        assertEquals("0000012", result)
    }

    @Test
    fun `ID가 0일 때 올바르게 패딩 처리된다`() {
        // Given
        val id = 0L

        // When
        val result = urlShortener.shorten(id)

        // Then
        assertEquals("0000000", result) // ID 0 is represented as "0" padded to 7 characters
    }

    @Test
    fun `큰 ID 값을 Base62로 변환했을 때 결과가 올바르게 반환된다`() {
        // Given
        val id = 123456789L

        // When
        val result = urlShortener.shorten(id)

        // Then
        assertEquals("00Xk0M8", result) // Expected Base62 encoding for 123456789
    }

    @Test
    fun `7자리로 표현 가능한 최대 값을 변환했을 때 결과가 올바르다`() {
        // Given
        val id = 62.0.pow(7.0) - 1 // The maximum number that fits in 7 Base62 characters

        // When
        val result = urlShortener.shorten(id.toLong())

        // Then
        assertEquals("zzzzzzz", result) // Max Base62 value with 7 characters
    }
}
