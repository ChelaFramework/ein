import ein.core.view.router.eRouter
import ein.js.view.router.eDomBase
import ein.js.view.router.eDomHolder
import home.HomeH
import org.w3c.dom.HTMLElement

object Router{
    private lateinit var router:eRouter<eDomBase, eDomHolder, HTMLElement>
    operator fun invoke(group:HTMLElement){
        eRouter("", eDomBase(group, 1)).apply{
            router = this
            routingTable()
        }
    }
    fun action(k:String, v:Any? = null) = router.action(k, v)
    fun push(k:String, data:Any? = null){router.push(k, data)}
    fun pop(){router.pop()}
    private fun routingTable(){
        router[Holder.MAIN] = {r, h, d-> HomeH(r, h, d)}
    }
}
object Holder{
    const val MAIN = ""
}