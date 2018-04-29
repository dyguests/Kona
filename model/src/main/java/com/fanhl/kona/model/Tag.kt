package com.fanhl.kona.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Tag(
        val id: Int? = null,
        val name: String?,
        val count: Int? = null,
        val type: Int? = null,
        val ambiguous: Boolean? = null
)