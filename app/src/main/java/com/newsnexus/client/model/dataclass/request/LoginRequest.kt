package com.newsnexus.client.model.dataclass.request

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginRequest(
    val email: String,
    val password: String,
): Parcelable