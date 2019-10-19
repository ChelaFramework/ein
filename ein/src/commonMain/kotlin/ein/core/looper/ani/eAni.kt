package ein.core.looper.ani

import ein.core.core.eLock
import ein.core.looper.eLooper
import ein.core.looper.eScheduler

class eAni(term:Double, looper:eLooper, lock:eLock = eLock.EMPTY):eScheduler<eAniTask, eAniSerial>(term, looper, lock){
    override fun serial() = eAniSerial(this)
    override fun task() = eAniTask()
    override fun loop(c:Double, task:eAniTask){
        if(task.isPaused) return
        task.current = c
        task.rate = if(task.end <= c){
            task.turn++
            if(!task.isInfinity && --task.loop < 1) {
                task.stop()
                1.0
            }else{
                setStart(task)
                task.end = task.start + task.time
                0.0
            }
        }else (c - task.start) / task.time
    }
}