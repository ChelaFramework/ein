package ein.android.thread

import android.os.Handler
import android.os.Looper
import android.os.Message
import java.util.concurrent.Executors

object eThread {
    private var id = 0
    private val msgKey = mutableMapOf<String, Int>()
    private val msgBlock = mutableMapOf<Int, (Any)->Unit>()
    fun setMsg(k:String, block:(Any)->Unit) {
        msgKey[k] = id
        msgBlock[id] = block
        id++
    }

    private val mainHnd = object:Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg:Message) {
            msgBlock[msg.what]?.invoke(msg.obj)
            msg.recycle()
        }
    }
    private val que = Executors.newSingleThreadExecutor()
    private val pool = Executors.newFixedThreadPool(3)
    fun flushAll() {
        que.shutdownNow()
        pool.shutdownNow()
        mainHnd.looper.quit()
    }

    fun isMain():Boolean = Looper.getMainLooper().thread === Thread.currentThread()
    fun que(task:Runnable) = que.execute(task)
    fun pool(task:Runnable) =  pool.execute(task)
    fun main(task:Runnable) = main(0, task)
    fun main(delay:Long, task:Runnable){
        if(delay == 0L) {
            if(isMain()) task.run() else mainHnd.post(task)
        } else mainHnd.postDelayed(task, delay)
    }
    fun mainCancel(task:Runnable) = mainHnd.removeCallbacks(task)
    fun msg(k:String, v:Any) = msg(0, k, v)
    fun msg(ms:Long, k:String, v:Any) = msgKey[k]?.let {
        val msg = mainHnd.obtainMessage(it, v)
        if(ms == 0L) {
            if(isMain()) mainHnd.handleMessage(msg) else mainHnd.sendMessage(msg)
        } else mainHnd.sendMessageDelayed(msg, ms)
    }
    fun msgCancel(k:String, v:Any) = msgKey[k]?.let {
        mainHnd.removeMessages(it, v)
    }
}