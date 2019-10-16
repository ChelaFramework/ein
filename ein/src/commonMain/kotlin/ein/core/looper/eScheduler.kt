package ein.core.looper

import ein.core.core.eLock
import ein.core.looper.eTaskState.GO
import ein.core.looper.eTaskState.STOP

abstract class eScheduler<T:eTask, R:eSerial<T>>(internal val term:Double, var looper:eLooper, private val lock:eLock){
    init{looper += this}

    private val pool = mutableListOf<T>()
    private val tasks = mutableListOf<T>()
    private val blockMap = mutableMapOf<T, (T)->Unit>()
    private val remove = mutableListOf<T>()
    private val add = mutableListOf<T>()

    internal var prev = 0.0
    operator fun invoke(block:R.()->Unit){serial().block()}
    operator fun plusAssign(task:T){tasks += task}
    fun remove(){looper -= this}
    fun task(init:(T.()->Unit)? = null, block:(T)->Unit):T{
        var task:T? = null
        lock.write {
            val size = pool.size
            val t = if(size == 0) task() else pool.removeAt(size - 1)
            blockMap -= t
            task = t
        }
        return task?.apply{
            delay = 0
            error = EMPTYBLOCK
            init?.invoke(this)
            blockMap[this] = block
            start(this)
        } ?: throw Throwable("invalid task")
    }

    protected fun start(task:eTask){
        task.start = now().plus(task.delay)
        task.start()
    }
    protected open fun loop(c:Double, task:T){}
    protected abstract fun task():T
    protected abstract fun serial():R
    internal fun _loop(c:Double){
        if(tasks.isEmpty()) return
        var i = tasks.size
        while(i-- > 0){
            val task = tasks[i]
            if(task.start > c) continue
            task.state = GO
            loop(c, task)
            blockMap[task]?.invoke(task)
            when(task.state){
                STOP->{
                    task.next?.let {
                        start(it)
                        @Suppress("UNCHECKED_CAST")
                        add += it as T
                    }
                    remove += task
                }
                is ERROR->{
                    (task.state as ERROR).block.invoke()
                    remove += task
                }
            }
        }
        if(remove.isNotEmpty() || add.isNotEmpty()) lock.write{
            if(remove.isNotEmpty()) {
                tasks -= remove
                blockMap -= remove
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
