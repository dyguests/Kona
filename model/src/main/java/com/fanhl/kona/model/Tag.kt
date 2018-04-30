package com.fanhl.kona.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Tag(
        @PrimaryKey var id: Long? = null,
        @ColumnInfo(name = "name") var name: String?,
        @ColumnInfo(name = "count") var count: Int? = null,
        @ColumnInfo(name = "type") var type: Int? = null,
        @ColumnInfo(name = "ambiguous") var ambiguous: Boolean? = null
) {
    constructor() : this(System.currentTimeMillis(), "name", 0, 0, false)
}
