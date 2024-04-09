package com.api.common

import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.beans.factory.getBean
import org.springframework.stereotype.Component
import org.springframework.test.context.junit.jupiter.SpringExtension

@Component
class CleanUpExtension : AfterEachCallback {

    override fun afterEach(context: ExtensionContext) {
        val applicationContext = SpringExtension.getApplicationContext(context)
        val databaseCleaner = applicationContext.getBean<DatabaseCleaner>()
        databaseCleaner.execute()
    }
}