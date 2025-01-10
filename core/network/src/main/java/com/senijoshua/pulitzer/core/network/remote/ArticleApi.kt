package com.senijoshua.pulitzer.core.network.remote

import com.senijoshua.pulitzer.core.network.model.ArticleResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface for executing network requests via Retrofit
 */
interface ArticleApi {
    @GET("search")
    suspend fun getNewsArticles(
        @Query("q") query: String = "technology",
        @Query("order-by") orderBy: String = "newest",
        @Query("show-fields") showFields: String = "headline,byline,body,thumbnail,lastModified",
        @Query("page") page: Int = 1,
        @Query("page-size") pageSize: Int = 20
    ): ArticleResponse
}
