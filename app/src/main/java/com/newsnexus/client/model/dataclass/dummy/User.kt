package com.newsnexus.client.model.dataclass.dummy

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "User")
data class User (
    @field:SerializedName("email")
    @PrimaryKey(autoGenerate = false)
    val userEmail: String,

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("username")
    val userName: String,
): Parcelable