package ein.core.looper

import ein.core.log.log

abstract class eSerial<T:eTask>{
    private var last:T? = null
    protected fun last(vararg tasks:T) = tasks.forEach {
        last?.apply{next = it} ?: run{addScheduler(it)}
        last = it
    }
    protected abstract fun addScheduler(task:T)
}