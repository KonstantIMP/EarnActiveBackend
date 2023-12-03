package org.kimp.earnactive.utils

import com.google.protobuf.Timestamp

private const val MILLIS_IN_SECOND: Int = 1000
private const val NANOS_IN_MILLI: Int = 1000

fun Long.millisToTimestamp(): Timestamp = Timestamp.newBuilder()
    .setSeconds(this / MILLIS_IN_SECOND)
    .setNanos((this % NANOS_IN_MILLI).toInt() * NANOS_IN_MILLI)
    .build()

fun Timestamp.toMillis() = this.seconds * MILLIS_IN_SECOND + this.nanos / NANOS_IN_MILLI
