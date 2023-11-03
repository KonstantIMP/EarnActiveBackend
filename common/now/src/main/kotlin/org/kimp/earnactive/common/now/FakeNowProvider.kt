package org.kimp.earnactive.common.now

import java.util.concurrent.atomic.AtomicLong

class FakeNowProvider (
    now: Long = 0L
) : NowProvider {
    private val nowMillis: AtomicLong = AtomicLong(now)

    override fun nowMillis(): Long = nowMillis.get()

    fun setNowMillis(now: Long) {
        nowMillis.set(now)
    }
}
