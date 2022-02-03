package com.micudasoftware.linepicker.other

import android.net.Uri

sealed class Event {
    data class OnGetFile(val uri: Uri) : Event()
    object OnRemoveDictionaries : Event()
}
