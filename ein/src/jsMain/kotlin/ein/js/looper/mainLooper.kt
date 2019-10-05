package ein.js.looper

import ein.core.looper.eLooper
import ein.core.looper.eScheduler
import kotlin.browser.window

val mainLooper = eLooper(object:eScheduler() {
    private var isStop = true
    private val f:(Double)->Unit = ::ani
    private fun ani(t:Double) {
        looper.loop()
        if(!isStop) window.requestAnimationFrame(f)
    }

    override fun start() {
        if(!isStop) return
        isStop = false
        window.requestAnimationFrame(f)
    }

    override fun stop() {
        isStop = true
    }
})