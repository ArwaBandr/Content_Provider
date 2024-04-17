package com.example.contentprovider

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ContentProviderViewmodel: ViewModel(){
    var imagesState = mutableStateOf(emptyList<ContentProviderDataItem>())

    fun updateImages(image: List<ContentProviderDataItem>){

        imagesState.value=image
    }

}