package com.lh.myapplication

import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lh.myapplication.base.BaseActivity
import com.lh.myapplication.bean.Data
import com.lh.myapplication.databinding.ActivityMainBinding
import com.lh.myapplication.main.MainViewModel
import com.lh.myapplication.main.Model
import com.lh.myapplication.viewholder.MainAdapter

class MainActivity : BaseActivity<MainViewModel,ActivityMainBinding>() {
    //RV的adapter
    var adapter: MainAdapter? = null
    // RV的数据源
    var list: ArrayList<Data>? = null
    // 分页请求
    var page :Int = 0
    // 布局id
    override fun getLayoutResID(): Int = R.layout.activity_main
    override fun init() {
        list = ArrayList()
        adapter = MainAdapter()
        mVB.mRecyclerView.layoutManager = LinearLayoutManager(this)
        mVB.mRecyclerView.adapter = adapter
        adapter!!.setList(list)

        mVB.refreshLayout.setOnRefreshListener {
            page = 0
            mVM.getListData(page)
            // 给text

        }
        mVB.refreshLayout.setOnLoadMoreListener {
            page++;
            mVM.getListData(page)
        }
        mVM.model.value = Model()
        mVB.test=mVM.model.value
        mVB.activity=this
        mVB.refreshLayout.autoRefresh()
        // 不添加。不会更新UI
        mVB.lifecycleOwner=this

    }

    override fun initVM() {
        //观察ViewModel中的model数据，更新adapter 绑定数据
        mVM.articlesData.observe(this, Observer {
            mVB.refreshLayout.finishRefresh()
            mVB.refreshLayout.finishLoadMore()
            if (page == 0) {
                list!!.clear()
            }
            it.datas?.let { it1 -> list!!.addAll(it1) }
            adapter?.let {it2->
                it2.setList(list)
            }
        })
    }

    fun click(view : View){
        Log.d("click","print")
    }

}