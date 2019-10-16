package ein.core.core

import ein.core.looper.ani

fun eThrottle(rate:Int, vararg arg:Any, f:(Array<out Any>)->Unit):()->Unit{
    var ticking = false
    return r@{
        if(ticking) return@r
        ticking = true
        ani().invoke{
            once(rate) {
                f(arg)
                ticking = false
            }
        }
    }
}