package com.crm.report.controller

import com.crm.common.StatusResult
import com.crm.common.toResponse
import com.crm.report.constant.ReportStatus
import com.crm.report.service.ReportService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ReportController (
    private val reportService: ReportService
) {

    @PatchMapping("block/{userId}")
    fun blockUser(@PathVariable userId: Long): ResponseEntity<StatusResult<ReportStatus>> {
        return StatusResult(reportService.blockUser(userId)).toResponse()
    }
}