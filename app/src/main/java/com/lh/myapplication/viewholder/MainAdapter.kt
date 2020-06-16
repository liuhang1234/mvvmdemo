package com.lh.myapplication.viewholder

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.lh.myapplication.R
import com.lh.myapplication.bean.Data
import com.lh.myapplication.databinding.ItemArticleBinding

class MainAdapter() : BaseQuickAdapter<Data, BaseDataBindingHolder<ItemArticleBinding>>(R.layout.item_article) {
    override fun convert(holder: BaseDataBindingHolder<ItemArticleBinding>, item: Data) {
        var bind = holder.dataBinding
        bind?.let {
            it.data=item
            it.executePendingBindings()
        }
    }


}