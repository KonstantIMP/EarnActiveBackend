package org.kimp.earnactive.common.lock

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class FileLockTest {
    private lateinit var testDirPath: Path

    @BeforeTest
    fun createTestDir() {
        testDirPath = Files.createTempDirectory("FileLock")
    }

    @AfterTest
    fun removeTestDir() {
        testDirPath.toFile().deleteRecursively()
    }

    @Test
    fun simpleLockAcquire() {
        val fileLock = FileLock(testDirPath.resolve("acquire_test"))
        assertThat(fileLock.acquire(), equalTo(true))
        assertThat(fileLock.isAcquired(), equalTo(true))
    }

    @Test
    fun unableToAcquireLockedFile() {
        val lockPath = testDirPath.resolve("lock_race")

        val firstLock = FileLock(lockPath)
        assertThat(firstLock.acquire(), equalTo(true))

        val secondLock = FileLock(lockPath)
        assertThat(secondLock.acquire(), equalTo(false))
        assertThat(secondLock.isAcquired(), equalTo(false))
    }

    @Test
    fun lockRelease() {
        val lockPath = testDirPath.resolve("lock_race")

        val firstLock = FileLock(lockPath)
        assertThat(firstLock.acquire(), equalTo(true))

        firstLock.release()
        assertThat(firstLock.isAcquired(), equalTo(false))

        val secondLock = FileLock(lockPath)
        assertThat(secondLock.acquire(), equalTo(true))
    }
}
