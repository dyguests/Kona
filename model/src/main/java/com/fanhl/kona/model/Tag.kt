package com.fanhl.kona.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Tag(
        @PrimaryKey val id: Int?,
        val name: String?,
        val count: Int?,
        val type: Int?,
        val ambiguous: Boolean?
)