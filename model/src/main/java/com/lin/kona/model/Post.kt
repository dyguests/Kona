package com.lin.kona.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("tags") var tags: String? = null,
    @SerializedName("created_at") var createdAt: Int? = null,
    @SerializedName("creator_id") var creatorId: Int? = null,
    @SerializedName("author") var author: String? = null,
    @SerializedName("change") var change: Int? = null,
    @SerializedName("source") var source: String? = null,
    @SerializedName("score") var score: Int? = null,
    @SerializedName("md5") var md5: String? = null,
    @SerializedName("file_size") var fileSize: Int? = null,
    @SerializedName("file_url") var fileUrl: String,
    @SerializedName("is_shown_in_index") var isShownInIndex: Boolean? = null,
    @SerializedName("preview_url") var previewUrl: String,
    @SerializedName("preview_width") var previewWidth: Int,
    @SerializedName("preview_height") var previewHeight: Int,
    @SerializedName("actual_preview_width") var actualPreviewWidth: Int? = null,
    @SerializedName("actual_preview_height") var actualPreviewHeight: Int? = null,
    @SerializedName("sample_url") var sampleUrl: String? = null,
    @SerializedName("sample_width") var sampleWidth: Int? = null,
    @SerializedName("sample_height") var sampleHeight: Int? = null,
    @SerializedName("sample_file_size") var sampleFileSize: Int? = null,
    @SerializedName("jpeg_url") var jpegUrl: String? = null,
    @SerializedName("jpeg_width") var jpegWidth: Int? = null,
    @SerializedName("jpeg_height") var jpegHeight: Int? = null,
    @SerializedName("jpeg_file_size") var jpegFileSize: Int? = null,
    @SerializedName("rating") var rating: String? = null,
    @SerializedName("has_children") var hasChildren: Boolean? = null,
    // @SerializedName("parent_id") var parentId: Any? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("width") var width: Int? = null,
    @SerializedName("height") var height: Int? = null,
    @SerializedName("is_held") var isHeld: Boolean? = null,
    @SerializedName("frames_pending_string") var framesPendingString: String? = null,
    // @SerializedName("frames_pending") var framesPending: List<*>? = null,
    @SerializedName("frames_string") var framesString: String? = null,
    // @SerializedName("frames") var frames: List<*>? = null,
) : Parcelable
