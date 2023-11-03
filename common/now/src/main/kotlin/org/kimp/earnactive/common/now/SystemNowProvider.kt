package org.kimp.earnactive.common.now

import java.time.Clock
import java.time.Instant
import java.time.ZoneId

class SystemNowProvider : NowProvider {
    private val instant = Instant.now(Clock.system(ZoneId.systemDefault()))
    override fun nowMillis(): Long = instant.toEpochMilli()
}
