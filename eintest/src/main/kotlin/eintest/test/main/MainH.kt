package eintest.test.main

import ein.android.app.eWindow
import ein.android.view.router.eViewHolder
import ein.android.view.viewmodel.processorAndroid
import ein.android.view.viewmodel.propertyAndroid
import ein.core.looper.ani
import ein.core.view.router.eAni
import ein.core.view.viewmodel.eScanner
import eintest.test.R

class MainH(routerKey:String, holderKey:String, data:Any?):eViewHolder(routerKey, holderKey, data){
    private val vm = Main()
    override fun render(){
        render(vm)
    }
    override val layout = R.layout.main
    override val view get() = super.view.apply {
        scanned = eScanner.scan(this, processorAndroid, propertyAndroid, "main")
    }
    override fun push(isAni:Boolean, end:()->Unit, pushAni:eAni, pushTime:Int) {
        ani().invoke {
            ani({
                time = pushTime.toDouble()
            }){
                vm.x = it.backOut(eWindow.width.toDouble(), 0.0).toFloat()
                render()
            }
            once(0, end)
        }
    }
    override fun pushed() {

    }
}
