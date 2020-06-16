package com.lh.myapplication.http

import com.lh.myapplication.bean.BaseResult
import com.lh.myapplication.bean.ListDatas
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("article/listproject/{page}/json")
    suspend fun getArticleList(@Path("page") page: Int): BaseResult<ListDatas>
}