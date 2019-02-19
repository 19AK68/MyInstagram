package com.ak.user.myinstagram.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CameraPictureTaker (private val activity: Activity){

    val REQUEST_CODE = 1
    val simpleDateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
    private lateinit var mCurrentPhotoPath: String
    var imageUri: Uri?=null



    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = simpleDateFormat.format(Date())
        val storageDir: File = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath
        }
    }


    fun takeCameraPicture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(activity.packageManager)?.also {
                // Create the File where the photo should go
                val imageFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File

                    null
                }
                // Continue only if the File was successfully created
                imageFile?.also {
                    imageUri = FileProvider.getUriForFile(
                        activity,
                        "com.ak.user.myinstagram.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                    activity.startActivityForResult(takePictureIntent, REQUEST_CODE)

                }
            }
        }

    }
}