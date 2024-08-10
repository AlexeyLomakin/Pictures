package com.domain

data class Picture(
    val id: String,
    val download_url: String,
    val localPath: String? = null
)