package org.kimp.earnactive.common.lock

import java.io.RandomAccessFile
import java.nio.channels.FileLock as InnerFileLock
import java.nio.channels.OverlappingFileLockException
import java.nio.file.Path

class FileLock (
    val lockPath: Path
) : Lock {
    private var innerFileLock: InnerFileLock? = null

    override fun release() {
        if (isAcquired()) {
            innerFileLock!!.release()
            innerFileLock!!.channel().close()
        }
        innerFileLock = null
    }

    override fun acquire(): Boolean {
        RandomAccessFile(lockPath.toFile(), "rw").also { reader ->
            try {
                innerFileLock = reader.channel.tryLock()
            } catch (ignore: OverlappingFileLockException) {}
        }
        return isAcquired()
    }

    override fun isAcquired(): Boolean = innerFileLock != null
}
