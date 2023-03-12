package com.lin.kona.ui.post.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.lin.kona.R
import com.lin.kona.databinding.ItemPostBinding
import com.lin.kona.model.Post
import com.lin.kona.util.loadBy

class PostAdapter : BaseQuickAdapter<Post, PostAdapter.ViewHolder>() {
    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(ItemPostBinding.inflate(LayoutInflater.from(context), parent, false))
    override fun onBindViewHolder(holder: ViewHolder, position: Int, item: Post?) = holder.bind(item!!)

    class ViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Post) {
            binding.cover.loadBy(item.previewUrl) {
                apply(RequestOptions().dontTransform().placeholder(R.drawable.img_cover).override(item.previewWidth, item.previewHeight))
            }
        }
    }
}