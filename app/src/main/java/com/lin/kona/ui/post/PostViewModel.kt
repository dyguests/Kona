package com.lin.kona.ui.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lin.kona.model.Post
import com.lin.kona.net.KonaClient
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {
    val posts = MutableLiveData<List<Post>>()

    fun refreshData() {
        loadData()
    }

    private fun loadData(loadMore: Boolean = false) {
        viewModelScope.launch {
            posts.postValue(KonaClient.postService.getPosts())
        }
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is post Fragment"
    }
    val text: LiveData<String> = _text
}