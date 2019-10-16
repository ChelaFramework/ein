package ein.jvm.looper

import ein.core.core.elazy
import ein.core.looper.ani.eAni
import ein.core.looper.async.eAsync
import ein.core.looper.eLooper
import ein.core.looper.eScheduler
import ein.jvm.core.eLockJVM
import java.util.concurrent.Executors

object MainLooper:eLooper(){
    val aniJVM by elazy(true){eAni(0.0, MainLooper, eLockJVM())}
    val asyncJVM by elazy(true){eAsync(100.0, MainLooper, eLockJVM())}
    private val ex = Executors.newSingleThreadExecutor()
    override fun start() {
        ex.execute {
            while(!Thread.interrupted()) {
                loop()
                Thread.sleep(17)
            }
        }
    }
    override fun stop() = ex.shutdown()
}