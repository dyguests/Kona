package com.fanhl.kona.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Tag(
        var id: Int? = null,
        var name: String?,
        var count: Int? = null,
        var type: Int? = null,
        var ambiguous: Boolean? = null
) {
    constructor() : this(name = "")
}
