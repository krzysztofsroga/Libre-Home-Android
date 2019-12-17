package com.krzysztofsroga.librehome.ui.mylists

sealed class GroupImageRef {
    class InternalImage(val resourceId: Int) : GroupImageRef()
    class StorageImage(val path: String) : GroupImageRef()
}