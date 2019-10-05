package ein.jvm.looper

import ein.core.looper.eLooper
import ein.core.looper.eScheduler
import ein.jvm.core.eLockJVM
import java.util.concurrent.Executors

val mainLooper = eLooper(object:eScheduler() {
    private val ex = Executors.newSingleThreadExecutor()
    override fun start() {
        ex.execute {
            while(!Thread.interrupted()) {
                looper.loop()
                Thread.sleep(17)
            }
        }
    }
    override fun stop() = ex.shutdown()
}, eLockJVM())
