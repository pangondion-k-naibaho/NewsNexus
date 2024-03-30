package com.newsnexus.client.model.dataclass.dummy

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "CnS")
data class CritiqueSuggestions(
    @PrimaryKey(autoGenerate = true)
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("username")
    val userName: String,

    @field:SerializedName("critique")
    val critique: String,

    @field:SerializedName("suggestion")
    val suggestion: String,
):Parcelable