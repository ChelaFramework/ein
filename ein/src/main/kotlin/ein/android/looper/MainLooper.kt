package ein.android.looper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import ein.android.core.eLockAndroid
import ein.android.thread.eThread
import ein.core.core.elazy
import ein.core.log.log
import ein.core.looper.ani.eAni
import ein.core.looper.async.eAsync
import ein.core.looper.eLooper
import java.util.concurrent.Executors

@SuppressLint("StaticFieldLeak")
object MainLooper:eLooper(){
    val aniAndroid by elazy(true){eAni(0.0, MainLooper, eLockAndroid())}
    val asyncAndroid by elazy(true){eAsync(100.0, MainLooper, eLockAndroid())}
    val netLooper by elazy(true) {
        eAsync(100.0, object:eLooper() {
            private val ex = Executors.newSingleThreadExecutor()
            override fun start() {
                ex.execute {
                    while(!Thread.interrupted()) {
                        loop()
                        Thread.sleep(100)
                    }
                }
            }

            override fun stop() = ex.shutdown()
        }, eLockAndroid())
    }
    private class Ani(ctx:Context): View(ctx){
        init{tag = "EIN_ANI"}
        override fun onDraw(canvas:Canvas?){
            if(isStarted) loop()
            invalidate()
        }
    }
    private var root:ViewGroup? = null
    fun act(act:AppCompatActivity){
        root = act.window.decorView as ViewGroup
        root?.apply{
            if(findViewWithTag<Ani>("EIN_ANI") == null) eThread.main(Runnable {addView(Ani(act))})
        }
    }
    fun terminate(){
        end()
        netLooper.looper.end()
    }
}
