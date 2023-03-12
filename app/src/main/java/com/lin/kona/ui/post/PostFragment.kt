package com.lin.kona.ui.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.chad.library.adapter.base.QuickAdapterHelper
import com.chad.library.adapter.base.loadState.LoadState
import com.chad.library.adapter.base.loadState.trailing.TrailingLoadStateAdapter
import com.lin.kona.databinding.FragmentPostBinding
import com.lin.kona.ui.post.adapter.PostAdapter

class PostFragment : Fragment() {
    private val viewModel by lazy { ViewModelProvider(this)[PostViewModel::class.java] }
    private val binding by lazy { FragmentPostBinding.inflate(layoutInflater) }

    private val postAdapter by lazy {
        PostAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                val post = adapter.getItem(position) ?: return@setOnItemClickListener

            }
        }
    }
    private val postAdapterHelper by lazy {
        QuickAdapterHelper.Builder(postAdapter)
            .setTrailingLoadStateAdapter(object : TrailingLoadStateAdapter.OnTrailingListener {
                override fun onFailRetry() {
                    viewModel.loadData(true)
                }

                override fun onLoad() {
                    viewModel.loadData(true)
                }
            })
            .build()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isRefreshing.observe(viewLifecycleOwner) {
            binding.swiper.isRefreshing = it
        }
        viewModel.posts.observe(viewLifecycleOwner) {
            postAdapter.submitList(it)
            postAdapterHelper.trailingLoadState = LoadState.NotLoading(false)
        }

        binding.swiper.setOnRefreshListener { viewModel.refreshData() }
        binding.postRecycler.adapter = postAdapterHelper.adapter

        viewModel.refreshData()
    }
}