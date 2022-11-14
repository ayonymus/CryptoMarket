package com.ayon.cryptomarket.framework

import java.text.SimpleDateFormat
import java.util.*

interface TimeProvider {
    fun currentMillis(): Long
    fun nowHHMMSS(): String
}

class SystemTimeProvider: TimeProvider {
    override fun currentMillis() = System.currentTimeMillis()
    override fun nowHHMMSS() = SimpleDateFormat("HH:mm:ss")
        .format(Date(currentMillis()))
        .toString()
}
