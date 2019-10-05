package ein.jvm.core

import ein.core.core.eLock
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

class eLockJVM:eLock {
    private val lock = ReentrantReadWriteLock()
    override fun read(block:()->Unit) = lock.read(block)
    override fun write(block:()->Unit) = lock.write(block)
}