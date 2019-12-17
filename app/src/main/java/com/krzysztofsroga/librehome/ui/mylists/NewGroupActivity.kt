package com.krzysztofsroga.librehome.ui.mylists

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.krzysztofsroga.librehome.R
import com.krzysztofsroga.librehome.models.SwitchGroup
import com.krzysztofsroga.librehome.viewmodels.NewGroupViewModel
import com.krzysztofsroga.librehome.viewmodels.SwitchGroupViewModel
import kotlinx.android.synthetic.main.activity_new_group.*
import java.io.File
import java.io.FileOutputStream


class NewGroupActivity : AppCompatActivity() {

    companion object {
        private const val RESULT_LOAD_IMAGE = 1
    }

    private val tmpFile: File by lazy { File(filesDir, "tmp.jpg") }

    private val newGroupViewModel: NewGroupViewModel by lazy {
        ViewModelProvider(this)[NewGroupViewModel::class.java]
    }

    private val switchGroupViewModel: SwitchGroupViewModel by lazy {
        ViewModelProvider(this)[SwitchGroupViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_group)
        title = getString(R.string.new_group)

        new_group_photo.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(i, RESULT_LOAD_IMAGE)
        }

        button_done.setOnClickListener {
            if (validateFields()) {
                saveData()
                finish()
            }
        }

        button_cancel.setOnClickListener {
            finish()
        }

        newGroupViewModel.tmpImagePath.observe(this, Observer {file ->
            if (file == null) return@Observer
            Glide.with(this).load(file).signature(ObjectKey(System.currentTimeMillis().toString())).into(new_group_photo)
        })
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            val selectedImage: Uri = data.data!!
            //TODO launch coroutine
            uriToBitmap(selectedImage).scale(512, 512).saveAsJpeg(tmpFile)
            newGroupViewModel.tmpImagePath.value = tmpFile
        }
    }

    private fun Bitmap.scale(xRes: Int, yRes: Int): Bitmap { //TODO scale proportional and crop
        return Bitmap.createScaledBitmap(this, xRes, yRes, true)
    }

    private fun Bitmap.saveAsJpeg(file: File) {
        file.delete()
        file.createNewFile()
        FileOutputStream(file).use { ostream ->
            compress(CompressFormat.JPEG, 90, ostream)
        }
    }

    @SuppressWarnings("deprecation")
    private fun uriToBitmap(uri: Uri) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.contentResolver, uri))
    } else {
        MediaStore.Images.Media.getBitmap(contentResolver, uri)
    }

    private fun validateFields(): Boolean{
        var success = true
        if (edit_group_name.text.isEmpty()) {
            edit_group_name.error = getString(R.string.field_required)
            success = false
        }
        if(newGroupViewModel.tmpImagePath.value == null) {
            success = false
            Toast.makeText(this, "You have to select an image!", Toast.LENGTH_SHORT).show()
        }

        return success
    }

    private fun saveData() {
        val newFile = File(filesDir, "groupimage-${System.currentTimeMillis()}.jpg")
        newGroupViewModel.tmpImagePath.value!!.renameTo(newFile) //TODO load generic image if it isn't selected? If so, change check in validateFields
        val group = SwitchGroup(
            0,
            edit_group_name.text.toString(),
            edit_group_description.text.toString(),
            newFile.absolutePath
        )
        switchGroupViewModel.addGroup(group)
    }

}
