package com.lh.myapplication.http

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor(){
    companion object {
        @Volatile
        private var retrofit: Retrofit? = null

        fun createService():ApiService {
            if (retrofit == null) {
                synchronized(RetrofitClient::class) {
                    if (retrofit == null) {
                        retrofit =Retrofit.Builder()
                            .baseUrl(EnvControl.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(OkHttpUtil.getOkHttpClient())
                            .build()
                    }
                }
            }
            return retrofit!!.create(ApiService::class.java)
        }

    }
}