package com.lh.myapplication.http

class HttpUtils {
    private val mApiService by lazy {
        RetrofitClient.createService()
    }

    companion object{
        @Volatile
        private var mHttpUtils:HttpUtils? =null
        fun  getInstance() : HttpUtils {
            if (null == mHttpUtils) {
                mHttpUtils = HttpUtils()
            }
            return mHttpUtils as HttpUtils
        }
    }
    fun getApiService():ApiService{
        return mApiService
    }
}