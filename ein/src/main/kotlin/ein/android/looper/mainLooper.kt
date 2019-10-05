package ein.android.looper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import ein.android.core.eLockAndroid
import ein.android.thread.eThread
import ein.core.looper.eLooper
import ein.core.looper.eScheduler

private class Ani(ctx:Context): View(ctx){
    init{tag = "EIN_ANI"}
    override fun onDraw(canvas:Canvas?){
        mainScheduler.draw()
        invalidate()
    }
}
@SuppressLint("StaticFieldLeak")
object mainScheduler:eScheduler(){
    private var isStop = true
    private var root:ViewGroup? = null
    internal fun draw(){if(!isStop) looper.loop()}
    fun act(act:AppCompatActivity){
        root = act.window.decorView as ViewGroup
        root?.apply{
            if(findViewWithTag<Ani>("EIN_ANI") == null) eThread.main(Runnable {addView(Ani(act))})
        }
    }
    override fun start() {
        if(!isStop) return
        isStop = false
    }
    override fun stop(){isStop = true}
}
val mainLooperAndroid = eLooper(mainScheduler, eLockAndroid())
