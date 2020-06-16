package com.lh.myapplication.base

import android.util.Log
import androidx.lifecycle.ComputableLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lh.myapplication.bean.BaseResult
import com.lh.myapplication.http.HttpUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class BaseViewModel:ViewModel() {


    val httpUtil by lazy { HttpUtils.getInstance().getApiService() }


    fun <T> launch(
        block: suspend CoroutineScope.() -> BaseResult<T>,
        liveData: MutableLiveData<T>
    ){
        viewModelScope.launch{
            var result: BaseResult<T>? = null
            withContext(Dispatchers.IO) {
                result = block()
            }
            if (result!!.errorCode == 0) {//请求成功
//                liveData.postValue(result!!.data)
                liveData.value=result!!.data

            } else {
                Log.d("LIU","error")
            }
        }
    }
}