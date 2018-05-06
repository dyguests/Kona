package com.fanhl.kona.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * 图片帖
 */
@Entity
@Parcelize
data class Post(
        @PrimaryKey @SerializedName("id") var id: Long, //263791
        @SerializedName("tags") var tags: String? = null, //2girls aqua_eyes blonde_hair breasts cleavage demon dress elizabeth_bathory_(fate) fate/extra fate/grand_order fate_(series) feixiang_de_huojiren gloves horns long_hair microphone red_hair saber saber_extra short_hair tail
        @SerializedName("created_at") var createdAt: Int? = null, //1524028252
        @SerializedName("creator_id") var creatorId: Int? = null, //156425
        @SerializedName("author") var author: String? = null, //RyuZU
        @SerializedName("change") var change: Int? = null, //1475939
        @SerializedName("source") var source: String? = null, //https://www.pixiv.net/member_illust.php?mode=medium&illust_id=68274578
        @SerializedName("score") var score: Int? = null, //4
        @SerializedName("md5") var md5: String? = null, //d15289c1abb0200fe9d1dac575845051
        @SerializedName("file_size") var fileSize: Int? = null, //897754
        @SerializedName("file_url") var fileUrl: String? = null, ////konachan.net/image/d15289c1abb0200fe9d1dac575845051/Konachan.com%20-%20263791%202girls%20aqua_eyes%20blonde_hair%20breasts%20cleavage%20demon%20dress%20fate_extra%20gloves%20horns%20long_hair%20microphone%20red_hair%20saber%20saber_extra%20short_hair%20tail.jpg
        @SerializedName("is_shown_in_index") var isShownInIndex: Boolean?, //true
        @SerializedName("preview_url") var previewUrl: String? = null, ////konachan.net/data/preview/d1/52/d15289c1abb0200fe9d1dac575845051.jpg
        @SerializedName("preview_width") var previewWidth: Int? = null, //150
        @SerializedName("preview_height") var previewHeight: Int? = null, //114
        @SerializedName("actual_preview_width") var actualPreviewWidth: Int? = null, //300
        @SerializedName("actual_preview_height") var actualPreviewHeight: Int? = null, //227
        @SerializedName("sample_url") var sampleUrl: String? = null, ////konachan.net/image/d15289c1abb0200fe9d1dac575845051/Konachan.com%20-%20263791%202girls%20aqua_eyes%20blonde_hair%20breasts%20cleavage%20demon%20dress%20fate_extra%20gloves%20horns%20long_hair%20microphone%20red_hair%20saber%20saber_extra%20short_hair%20tail.jpg
        @SerializedName("sample_width") var sampleWidth: Int? = null, //1319
        @SerializedName("sample_height") var sampleHeight: Int? = null, //1000
        @SerializedName("sample_file_size") var sampleFileSize: Int? = null, //0
        @SerializedName("jpeg_url") var jpegUrl: String? = null, ////konachan.net/image/d15289c1abb0200fe9d1dac575845051/Konachan.com%20-%20263791%202girls%20aqua_eyes%20blonde_hair%20breasts%20cleavage%20demon%20dress%20fate_extra%20gloves%20horns%20long_hair%20microphone%20red_hair%20saber%20saber_extra%20short_hair%20tail.jpg
        @SerializedName("jpeg_width") var jpegWidth: Int? = null, //1319
        @SerializedName("jpeg_height") var jpegHeight: Int? = null, //1000
        @SerializedName("jpeg_file_size") var jpegFileSize: Int? = null, //0
        @SerializedName("rating") var rating: String? = null, //s
        @SerializedName("has_children") var hasChildren: Boolean?, //false
        @SerializedName("status") var status: String? = null, //active
        @SerializedName("width") var width: Int? = null, //1319
        @SerializedName("height") var height: Int? = null, //1000
        @SerializedName("is_held") var isHeld: Boolean?, //false
        @SerializedName("frames_pending_string") var framesPendingString: String? = null,
//        @SerializedName("frames_pending") var framesPending: List<Any?>?,
//        @SerializedName("frames") var frames: List<Any?>?,
        @SerializedName("frames_string") var framesString: String? = null
) : Parcelable {
    constructor() : this(
            System.currentTimeMillis(),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
    )

    var updateTime: Long? = System.currentTimeMillis()
}