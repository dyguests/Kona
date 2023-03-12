package com.lin.kona.ui.post

import android.util.Log
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

    fun loadData(loadMore: Boolean = false) {
        viewModelScope.launch {
            Log.d(TAG, "loadData: loadMore:$loadMore")
            KonaClient.postService.getPosts("landscape").whenSuccess {
                if (loadMore) {
                    posts.postValue(posts.value!! + body()!!)
                } else {
                    posts.postValue(body())
                }
            }
        }
    }

    companion object {
        private const val TAG = "PostViewModel"
    }
}
