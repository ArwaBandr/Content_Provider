package com.example.contentprovider

import android.Manifest
import android.content.ContentUris
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import com.example.contentprovider.ui.theme.ContentProviderTheme

class MainActivity : ComponentActivity() {
    private val viewmodel by viewModels<ContentProviderViewmodel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContentProviderTheme {
                // A surface container using the 'background' color from the theme
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.READ_MEDIA_IMAGES
                    ),
                    0
                )
                val projection = arrayOf(
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DISPLAY_NAME
                )
                val dateDuration = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_YEAR, -10)
                }.timeInMillis
                val selection ="${MediaStore.Images.Media.DATE_TAKEN} >=?"
                contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    selection,
                    arrayOf(dateDuration.toString()),
                    "${MediaStore.Images.Media.DATE_TAKEN} DESC"
                )?.use {
                    val idColumn = it.getColumnIndex(MediaStore.Images.Media._ID)
                    val dispayNameColumn =it.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
                    val images = mutableListOf<ContentProviderDataItem>()
                    while (it.moveToNext()){
                        val id= it.getLong(idColumn)
                        val displayName =it.getString(dispayNameColumn)
                        val uri = ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            id
                        )
                        images.add(ContentProviderDataItem(id,displayName,uri))

                    }
                    viewmodel.updateImages(images)

                }

            }
        }
    }
}

data class ContentProviderDataItem(
    val id: Long,
    val name: String,
    val uri: Uri
)