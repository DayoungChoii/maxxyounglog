package com.api.common.file

import org.springframework.web.multipart.MultipartFile

interface FileUploader {
    fun upload(file: MultipartFile): String
}