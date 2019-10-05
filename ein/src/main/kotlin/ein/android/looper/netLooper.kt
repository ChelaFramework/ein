package ein.android.looper

import ein.android.core.eLockAndroid
import ein.core.core.eLock
import ein.core.looper.eLooper
import ein.core.looper.eScheduler
import java.util.concurrent.Executors
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

val netLooper = eLooper(object:eScheduler() {
    private val ex = Executors.newSingleThreadExecutor()
    override fun start() {
        ex.execute {
            while(!Thread.interrupted()) {
                looper.loop()
                Thread.sleep(50)
            }
        }
    }
    override fun stop() = ex.shutdown()
}, eLockAndroid())