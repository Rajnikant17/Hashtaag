package com.example.hashtag.repository

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.hashtag.utils.DataState
import com.example.localdatabase.Entity
import com.example.localdatabase.RoomDao
import com.example.myapiservicesmodule.di.ApiServices
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.*
import java.io.*
import java.net.URL
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BusinessLogic
@Inject
constructor(val apiServices: ApiServices, val roomDao: RoomDao) {
    suspend fun execute(context: Context): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)
        try {
            emit(DataState.Success(downloadImage(apiServices.getImageUrl().image_path, context)))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    //Download image
     fun downloadImage(urlImage_string: String, context: Context): Boolean = runBlocking {
        val urlImage = URL(urlImage_string)
        val result: Deferred<Bitmap?> = GlobalScope.async {
            urlImage.toBitmap()
        }
        return@runBlocking storeInRoomDatabase(result,context)
}

    // store in room database
     suspend fun storeInRoomDatabase(result: Deferred<Bitmap?>, context: Context) :Boolean {
         var output_status = -1L
         val bitmap: Bitmap? = result.await()
         bitmap?.apply {
             // get saved bitmap internal storage uri
             val savedUri = saveToInternalStorage(context)
             output_status = roomDao.insertItem(
                     Entity(
                             0, savedUri.toString())
             )
         }
         return output_status==1L
     }

    // extension function to get / download bitmap from url
    fun URL.toBitmap(): Bitmap? {
        return try {
            BitmapFactory.decodeStream(openStream())
        } catch (e: IOException) {
            null
        }
    }

    // extension function to save an image to internal storage
    fun Bitmap.saveToInternalStorage(context: Context): Uri? {
        // get the context wrapper instance
        val wrapper = ContextWrapper(context)

        // initializing a new file
        // bellow line return a directory in internal storage
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)

        // create a file to save the image
        file = File(file, "${UUID.randomUUID()}.jpg")

        return try {
            // get the file output stream
            val stream: OutputStream = FileOutputStream(file)

            // compress bitmap
            compress(Bitmap.CompressFormat.JPEG, 100, stream)

            // flush the stream
            stream.flush()

            // close stream
            stream.close()

            // return the saved image uri
            Uri.parse(file.absolutePath)
        } catch (e: IOException) { // catch the exception
            e.printStackTrace()
            null
        }
    }
}