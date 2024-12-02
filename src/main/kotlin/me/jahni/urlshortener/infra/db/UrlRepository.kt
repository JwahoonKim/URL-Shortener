package me.jahni.urlshortener.infra.db;

import org.springframework.data.jpa.repository.JpaRepository

interface UrlRepository : JpaRepository<Url, Long> {
    fun findByOriginalUrl(originalUrl: String): Url?
    fun findByShortUrl(shortUrl: String): Url?
}