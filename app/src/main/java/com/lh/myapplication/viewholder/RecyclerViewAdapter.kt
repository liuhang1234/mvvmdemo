package com.lh.myapplication.viewholder

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.lh.myapplication.R
import com.lh.myapplication.bean.Data
import com.lh.myapplication.databinding.ItemArticleBinding

class RecyclerViewAdapter(private var data: List<Data>) : RecyclerView.Adapter<RecyclerViewAdapter.TestViewHolder>() {
    class TestViewHolder(private val binding: ItemArticleBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Data) {
            //方法一：
//            binding.setVariable(BR.user,data)
            //方法二：
            binding.tvTitle.text = data.title
            binding.tvDes.text = data.desc
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        val binding = DataBindingUtil.inflate<ItemArticleBinding>(
            LayoutInflater.from(parent?.context),
            R.layout.item_article,
            parent,
            false
        )
        return TestViewHolder(binding)

    }

    override fun getItemCount(): Int = data.size


    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        holder?.bind(data[position])
    }
}