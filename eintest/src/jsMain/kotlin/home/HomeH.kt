package home

import ein.core.core.elazy
import ein.core.looper.ani
import ein.core.view.router.eAni
import ein.core.view.viewmodel.eScanner
import ein.js.view.router.eDomHolder
import ein.js.view.viewmodel.eDomVM
import ein.js.view.viewmodel.processorDom
import ein.js.view.viewmodel.propertyDom
import org.w3c.dom.HTMLElement
import kotlin.browser.document

class HomeH(routerKey:String, holderKey:String, data:Any?):eDomHolder(routerKey, holderKey, data){
    companion object{
        val base by elazy(true){
            document.querySelector("#home")?.apply {
                removeAttribute("id")
            } ?: throw Throwable("no #home")
        }
    }
    private val vm = Home()
    override val el by elazy(true) {
        base.cloneNode(true) as HTMLElement
    }
    override fun render(){
        render(vm)
    }
    override val view get() = super.view.apply {
        scanned = eScanner.scan(this, processorDom, propertyDom, "")
    }
    override fun push(isAni:Boolean, end:()->Unit, pushAni:eAni, pushTime:Int) {
        ani().invoke {
            ani({
                time = pushTime.toDouble()
            }){
                vm.x = "${it.backOut(100.0, 0.0)}px"
                render()
            }
            once(0, end)
        }
    }
    override fun pushed() {

    }
}
