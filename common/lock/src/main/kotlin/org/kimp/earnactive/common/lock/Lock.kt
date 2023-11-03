package org.kimp.earnactive.common.lock

interface Lock {
    fun release()

    fun acquire(): Boolean

    fun isAcquired(): Boolean
}
