package com.async.user

interface UserPointService {
    fun createPoint(userId: Long, point: Int)
    fun addPoint(userId: Long, point: Int)
    fun subPoint(userId: Long, point: Int)
}