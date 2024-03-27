package com.newsnexus.client.model.dataclass.response.login

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginResponse(
    var status: String?= null,
    var token: String?= null,
    var userName: String?= null,
):Parcelable