package com.fanhl.kona.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Tag(
        var id: Long? = null,
        @PrimaryKey var name: String,
        var count: Int? = null,
        var type: Int? = null,
        var ambiguous: Boolean? = null,
        var updateTime: Long? = System.currentTimeMillis()
) {
    constructor() : this(System.currentTimeMillis(), "", 0, 0, false)

    companion object {
        const val TAG_SAMPLE = "landscape"
    }
}
