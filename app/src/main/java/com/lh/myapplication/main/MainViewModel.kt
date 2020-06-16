package com.lh.myapplication.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.lh.myapplication.base.BaseViewModel
import com.lh.myapplication.bean.ListDatas

class MainViewModel :BaseViewModel() {


    var articlesData = MutableLiveData<ListDatas>()

    var text = MutableLiveData<String>()

    fun click(){
        Log.d("click","dayin")
    }

    fun getListData(page:Int){
        launch({httpUtil.getArticleList(page)},
            articlesData)
    }

}