package com.example.dogownerapp.domain.repository

import android.content.Context
import android.net.Uri

interface SaveImageService {
    suspend fun uploadFileToFTP(uri: Uri, context: Context, collection: String, id: String)
    suspend fun renameFileOnFTP(collection: String, oldFileName: String, newFileName: String) : Boolean
}