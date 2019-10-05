package base

import android.view.View
import android.view.ViewGroup
import ein.android.view.router.eViewBase
import ein.android.view.router.eViewHolder
import ein.core.view.router.eRouter

object Router{
    private var router:eRouter<eViewBase, eViewHolder, View>? = null
    operator fun invoke(group:ViewGroup){
        router?.apply {
            base.group = group
        } ?: eRouter("", eViewBase(group, 1)).apply{
            router = this
            routingTable()
        }
    }
    fun action(k:String, v:Any? = null) = router?.action(k, v)
    fun push(k:String, data:Any? = null){router?.push(k, data)}
    fun pop(){router?.pop()}
    private fun routingTable(){

    }
}
object Holder{
    const val MAIN = ""
}