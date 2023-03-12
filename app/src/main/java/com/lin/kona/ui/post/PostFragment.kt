package com.lin.kona.ui.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.chad.library.adapter.base.QuickAdapterHelper
import com.chad.library.adapter.base.loadState.leading.LeadingLoadStateAdapter
import com.chad.library.adapter.base.loadState.trailing.TrailingLoadStateAdapter
import com.lin.kona.databinding.FragmentPostBinding
import com.lin.kona.ui.post.adapter.PostAdapter

class PostFragment : Fragment() {
    private val viewModel by lazy { ViewModelProvider(this)[PostViewModel::class.java] }
    private val binding by lazy { FragmentPostBinding.inflate(layoutInflater) }

    private val postAdapter by lazy { PostAdapter() }
    private val postAdapterHelper by lazy {
        QuickAdapterHelper.Builder(postAdapter)
            .setLeadingLoadStateAdapter(object : LeadingLoadStateAdapter.OnLeadingListener {
                override fun onLoad() {
                    viewModel.refreshData()
                }
            })
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

        viewModel.posts.observe(viewLifecycleOwner) {
            postAdapter.addAll(it)
        }

        // binding.swiper.setOnRefreshListener { viewModel.refreshData() }
        binding.postRecycler.adapter = postAdapterHelper.adapter

        viewModel.refreshData()
    }
}