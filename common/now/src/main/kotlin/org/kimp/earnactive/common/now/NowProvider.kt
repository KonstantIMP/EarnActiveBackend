package org.kimp.earnactive.common.now

interface NowProvider {
    fun nowMillis(): Long
}
