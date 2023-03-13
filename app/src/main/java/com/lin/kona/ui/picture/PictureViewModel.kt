package com.lin.kona.ui.picture

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lin.kona.model.Post

class PictureViewModel : ViewModel() {
    val post = MutableLiveData<Post>()
}
