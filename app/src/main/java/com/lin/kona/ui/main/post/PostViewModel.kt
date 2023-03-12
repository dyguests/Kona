package com.lin.kona.ui.main.post

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lin.kona.model.Post
import com.lin.kona.net.KonaClient
import com.lin.kona.net.util.whenSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newCoroutineContext

class PostViewModel : ViewModel() {
    val isRefreshing = MutableLiveData<Boolean>()
    val posts = MutableLiveData<List<Post>>()

    fun refreshData() {
        loadData()
    }

    fun loadData(loadMore: Boolean = false) {
        viewModelScope.launch {
            if (!loadMore) {
                isRefreshing.postValue(true)
            }
            // Log.d(TAG, "loadData: loadMore:$loadMore")
            KonaClient.postService.getPosts("landscape").whenSuccess {
                if (loadMore) {
                    posts.postValue(posts.value!! + body()!!)
                } else {
                    posts.postValue(body())
                }
            }
            if (!loadMore) {
                isRefreshing.postValue(false)
            }
        }
    }

    companion object {
        private const val TAG = "PostViewModel"
    }
}
