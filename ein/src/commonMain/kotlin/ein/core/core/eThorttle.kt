package ein.core.core

import ein.core.looper.getLooper

fun eThrottle(rate:Int, vararg arg:Any, f:(Array<out Any>)->Unit):()->Unit{
    var ticking = false
    return r@{
        if(ticking) return@r
        ticking = true
        getLooper().run(rate){
            f(arg)
            ticking = false
        }
    }
}