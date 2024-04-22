package com.api.common.file

import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Component
class FileUploaderImpl: FileUploader {
    override fun upload(file: MultipartFile): String {
//        TODO("S3에 파일 업로드")
        return UUID.randomUUID().toString()
    }
}