package com.lh.myapplication

import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lh.myapplication.base.BaseActivity
import com.lh.myapplication.bean.Data
import com.lh.myapplication.databinding.ActivityMainBinding
import com.lh.myapplication.main.MainViewModel
import com.lh.myapplication.viewholder.MainAdapter

class MainActivity : BaseActivity<MainViewModel,ActivityMainBinding>() {
    var adapter: MainAdapter? = null
    var list: ArrayList<Data>? = null
    var page :Int = 0
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
            mVM.text.value=page.toString()

        }
        mVB.refreshLayout.setOnLoadMoreListener {
            page++;
            mVM.getListData(page)
            mVM.text.value=page.toString()
        }

        mVB.refreshLayout.autoRefresh()

    }

    override fun initVM() {

        mVM.articlesData.observe(this, Observer {
            mVB.refreshLayout.finishRefresh()
            mVB.refreshLayout.finishLoadMore()
            if (page == 0) list!!.clear()
            it.datas?.let { it1 -> list!!.addAll(it1) }
            Log.d("liuhang","jajaj")
            adapter?.let {it2->
                it2.setList(list)
                it2.notifyDataSetChanged()
            }
        })
        mVM.text.observe(this, Observer {
            mVB.tvShow.text = it
        })
    }

}