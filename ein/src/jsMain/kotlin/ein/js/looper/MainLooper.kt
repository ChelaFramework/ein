package ein.js.looper

import ein.core.core.elazy
import ein.core.looper.ani.eAni
import ein.core.looper.async.eAsync
import ein.core.looper.eLooper
import ein.core.looper.eScheduler
import kotlin.browser.window

object MainLooper:eLooper(){
    val aniJS by elazy(true){eAni(0.0, MainLooper)}
    val asyncJS by elazy(true){eAsync(100.0, MainLooper)}
    private val f:(Double)->Unit = ::ani
    private fun ani(t:Double) {
        loop()
        if(isStarted) window.requestAnimationFrame(f)
    }
    override fun start(){window.requestAnimationFrame(f)}
}