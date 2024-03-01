package com.gxx.networksdkkotlin.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class BannerRequestModel(
    @SerializedName("userId") val userId:String,
    @SerializedName("list") val list: MutableList<String>,
):Parcelable {
}