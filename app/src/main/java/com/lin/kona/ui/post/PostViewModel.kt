package com.lin.kona.ui.post

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lin.kona.model.Post
import com.lin.kona.net.KonaClient
import com.lin.kona.net.util.whenSuccess
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {
    val posts = MutableLiveData<List<Post>>()

    fun refreshData() {
        loadData()
    }

    private fun loadData(loadMore: Boolean = false) {
        viewModelScope.launch {
            KonaClient.postService.getPosts().whenSuccess {
                posts.postValue(body())
            }
        }
    }
}
