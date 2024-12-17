package com.gxx.networksdkkotlin.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.flow.Flow

@Parcelize
class BannerRequestModel(
    @SerializedName("userId") val userId:String,
    @SerializedName("list") val list: MutableList<String>,
    @SerializedName("sexValue") val sexValue:Int,
    @SerializedName("money") val money:Float,
    @SerializedName("distance") val distance:Double,
    @SerializedName("time") val time:Long,
):Parcelable {
}