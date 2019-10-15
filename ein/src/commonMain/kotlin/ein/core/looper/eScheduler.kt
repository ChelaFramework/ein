package ein.core.looper

import ein.core.core.eLock

abstract class eScheduler<T:eTask, R:eSerial<T>>(internal val term:Double, private var worker:eWorker, private val lock:eLock){
    init{worker += this}

    private val pool = mutableListOf<T>()
    private val tasks = mutableListOf<T>()
    private val blockMap = mutableMapOf<T, (T)->Unit>()
    private val remove = mutableListOf<T>()
    private val add = mutableListOf<T>()

    internal var prev = 0.0
    operator fun invoke(block:R.()->Unit){serial().block()}
    operator fun plusAssign(task:T){tasks += task}
    fun remove(){worker -= this}
    fun task(block:(T)->Unit) = task(0, block)
    fun task(ms:Int = 0, block:(T)->Unit):T{
        var task:T? = null
        lock.write {
            val size = tasks.size
            val t = if(size == 0) task() else tasks.removeAt(size - 1)
            blockMap -= t
            task = t
        }
        return task?.apply{
            delay = ms
            blockMap[this] = block
            isStop = false
            start(this)
        } ?: throw Throwable("invalid task")
    }

    private fun start(task:eTask){task.start = now().plus(task.delay)}
    protected open fun loop(c:Double, task:T){}
    protected abstract fun task():T
    protected abstract fun serial():R
    internal fun _loop(c:Double){
        if(tasks.isEmpty()) return
        var i = tasks.size
        while(i-- > 0){
            val task = tasks[i]
            if(task.start > c) continue
            loop(c, task)
            blockMap[task]?.invoke(task)
            if(task.isStop){
                task.next?.let {
                    start(it)
                    @Suppress("UNCHECKED_CAST")
                    add += it as T
                }
                remove += task
            }
        }
        if(remove.isNotEmpty() || add.isNotEmpty()) lock.write{
            if(remove.isNotEmpty()) {
                tasks -= remove
                pool += remove
                remove.clear()
            }
            if(add.isNotEmpty()){
                tasks += add
                add.clear()
            }
        }
    }
}
