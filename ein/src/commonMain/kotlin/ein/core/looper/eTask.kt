package ein.core.looper

import ein.core.looper.eTaskState.GO
import ein.core.looper.eTaskState.STOP

abstract class eTask{
    companion object:eTask()
    internal var next:eTask? = null
    var start = 0.0
    internal var state:eTaskState = GO
    var error = EMPTYBLOCK
    var delay = 0
    fun stop(){state = STOP}
    fun error(block:(()->Unit)? = null){state = ERROR(block ?: error)}
    open fun start(){}
}