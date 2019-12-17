package com.krzysztofsroga.librehome.ui.mylists

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.krzysztofsroga.librehome.R
import kotlinx.android.synthetic.main.activity_new_group.*


class NewGroupActivity : AppCompatActivity() {
    companion object {
        private const val RESULT_LOAD_IMAGE = 1

        private const val REQUEST_EXTERNAL_STORAGE = 1
        private val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_group)
        title = getString(R.string.new_group)
        new_group_photo.setOnClickListener {
//            verifyStoragePermissions(this)

            val i = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(i, RESULT_LOAD_IMAGE)
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            val selectedImage: Uri? = data.data
            Glide.with(this).load(selectedImage).into(new_group_photo)
        }
//        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
//            val selectedImage: Uri? = data.data
//            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
//            val cursor: Cursor = contentResolver.query(
//                selectedImage!!,
//                filePathColumn, null, null, null
//            )!!
//            cursor.moveToFirst()
//            val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
//            val picturePath: String = cursor.getString(columnIndex)
//            hello.setText(picturePath)
//            cursor.close()
//            vm.getsth(File(picturePath))
//        }
    }

    private fun getRealPathFromURI(contentUri: Uri): String? {
        return contentResolver.query(contentUri, arrayOf(MediaStore.Images.Media.DATA), null, null, null)?.use { cursor ->
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(columnIndex)
        }
    }


    fun verifyStoragePermissions(activity: Activity?) { // Check if we have write permission
        val permission = ActivityCompat.checkSelfPermission(
            activity!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) { // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }
}
