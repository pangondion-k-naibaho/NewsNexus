package com.newsnexus.client.model.dataclass.dummy

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "CnS")
data class CritiqueSuggestions(
    @field:SerializedName("username")
    @PrimaryKey(autoGenerate = false)
    val userName: String,

    @field:SerializedName("critique")
    val critique: String,

    @field:SerializedName("suggestion")
    val suggestion: String,
):Parcelable