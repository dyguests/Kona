package com.fanhl.kona.net.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * 图片帖
 */
@Parcelize
data class Post(
        @SerializedName("id") val id: Int?, //263791
        @SerializedName("tags") val tags: String?, //2girls aqua_eyes blonde_hair breasts cleavage demon dress elizabeth_bathory_(fate) fate/extra fate/grand_order fate_(series) feixiang_de_huojiren gloves horns long_hair microphone red_hair saber saber_extra short_hair tail
        @SerializedName("created_at") val createdAt: Int?, //1524028252
        @SerializedName("creator_id") val creatorId: Int?, //156425
        @SerializedName("author") val author: String?, //RyuZU
        @SerializedName("change") val change: Int?, //1475939
        @SerializedName("source") val source: String?, //https://www.pixiv.net/member_illust.php?mode=medium&illust_id=68274578
        @SerializedName("score") val score: Int?, //4
        @SerializedName("md5") val md5: String?, //d15289c1abb0200fe9d1dac575845051
        @SerializedName("file_size") val fileSize: Int?, //897754
        @SerializedName("file_url") val fileUrl: String?, ////konachan.net/image/d15289c1abb0200fe9d1dac575845051/Konachan.com%20-%20263791%202girls%20aqua_eyes%20blonde_hair%20breasts%20cleavage%20demon%20dress%20fate_extra%20gloves%20horns%20long_hair%20microphone%20red_hair%20saber%20saber_extra%20short_hair%20tail.jpg
        @SerializedName("is_shown_in_index") val isShownInIndex: Boolean?, //true
        @SerializedName("preview_url") val previewUrl: String?, ////konachan.net/data/preview/d1/52/d15289c1abb0200fe9d1dac575845051.jpg
        @SerializedName("preview_width") val previewWidth: Int?, //150
        @SerializedName("preview_height") val previewHeight: Int?, //114
        @SerializedName("actual_preview_width") val actualPreviewWidth: Int?, //300
        @SerializedName("actual_preview_height") val actualPreviewHeight: Int?, //227
        @SerializedName("sample_url") val sampleUrl: String?, ////konachan.net/image/d15289c1abb0200fe9d1dac575845051/Konachan.com%20-%20263791%202girls%20aqua_eyes%20blonde_hair%20breasts%20cleavage%20demon%20dress%20fate_extra%20gloves%20horns%20long_hair%20microphone%20red_hair%20saber%20saber_extra%20short_hair%20tail.jpg
        @SerializedName("sample_width") val sampleWidth: Int?, //1319
        @SerializedName("sample_height") val sampleHeight: Int?, //1000
        @SerializedName("sample_file_size") val sampleFileSize: Int?, //0
        @SerializedName("jpeg_url") val jpegUrl: String?, ////konachan.net/image/d15289c1abb0200fe9d1dac575845051/Konachan.com%20-%20263791%202girls%20aqua_eyes%20blonde_hair%20breasts%20cleavage%20demon%20dress%20fate_extra%20gloves%20horns%20long_hair%20microphone%20red_hair%20saber%20saber_extra%20short_hair%20tail.jpg
        @SerializedName("jpeg_width") val jpegWidth: Int?, //1319
        @SerializedName("jpeg_height") val jpegHeight: Int?, //1000
        @SerializedName("jpeg_file_size") val jpegFileSize: Int?, //0
        @SerializedName("rating") val rating: String?, //s
        @SerializedName("has_children") val hasChildren: Boolean?, //false
        @SerializedName("status") val status: String?, //active
        @SerializedName("width") val width: Int?, //1319
        @SerializedName("height") val height: Int?, //1000
        @SerializedName("is_held") val isHeld: Boolean?, //false
        @SerializedName("frames_pending_string") val framesPendingString: String?,
//        @SerializedName("frames_pending") val framesPending: List<Any?>?,
//        @SerializedName("frames") val frames: List<Any?>?,
        @SerializedName("frames_string") val framesString: String?
) : Parcelable