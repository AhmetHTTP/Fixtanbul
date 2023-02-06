package com.acm431.complaintmanagement.database

import android.net.Uri
import androidx.lifecycle.MutableLiveData

object GlobalValues {
    val userName = MutableLiveData<String>()
    val imageUrl = MutableLiveData<Uri>()
}
