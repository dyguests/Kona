package com.lin.kona.ui.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lin.kona.databinding.FragmentPostBinding
import com.lin.kona.ui.post.adapter.PostAdapter

class PostFragment : Fragment() {
    private val viewModel by lazy { ViewModelProvider(this)[PostViewModel::class.java] }
    private val binding by lazy { FragmentPostBinding.inflate(layoutInflater) }

    private val postAdapter by lazy { PostAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.posts.observe(viewLifecycleOwner) {
            postAdapter.addAll(it)
        }

        binding.swiper.setOnRefreshListener { viewModel.refreshData() }
        binding.postRecycler.adapter = postAdapter

        viewModel.refreshData()
    }
}