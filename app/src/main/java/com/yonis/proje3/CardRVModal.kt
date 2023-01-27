package com.yonis.proje3

import android.graphics.Bitmap

data class CardRVModal(
    // on below line we are creating a
    // two variable one for course name
    // and other for course image.
    var point: Int?,
    var status:Boolean,
    var id:String,
    var courseImg1: Bitmap,
    var courseImg: Int
)
