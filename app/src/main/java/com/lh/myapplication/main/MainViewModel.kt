package com.lh.myapplication.main

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.lh.myapplication.base.BaseViewModel
import com.lh.myapplication.bean.ListDatas

class MainViewModel :BaseViewModel() {

    // 获取列表的数据内容，使用LiveData来观察该数据
    var articlesData = MutableLiveData<ListDatas>()
    // TextView的数据展示
    var model = MutableLiveData<Model>()


    //获取服务器数据
    fun getListData(page:Int){
        model.value?.page?.value = page.toString()
        launch({httpUtil.getArticleList(page)},
            articlesData)
    }

}