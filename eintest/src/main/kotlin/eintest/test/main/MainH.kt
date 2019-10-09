package eintest.test.main

import ein.android.app.eWindow
import ein.android.view.router.eViewHolder
import ein.android.view.viewmodel.eAndroidVM
import ein.android.view.viewmodel.processorAndroid
import ein.android.view.viewmodel.propertyAndroid
import ein.core.log.log
import ein.core.looper.getLooper
import ein.core.view.router.eAni
import ein.core.view.viewmodel.eScanner
import eintest.test.R

class MainH(routerKey:String, holderKey:String, data:Any?):eViewHolder(routerKey, holderKey, data){
    private val vm = Main().apply{
        x = eWindow.width.toFloat()
        title = "Main Title"
        contents.text = "contents Text"
        contents.textColor = "#ff0000"
    }
    override fun render(){
        render(vm)
    }
    override val layout = R.layout.main
    override val view get() = super.view.apply {
        scanned = eScanner.scan(this, processorAndroid, propertyAndroid, "main")
    }
    override fun push(isAni:Boolean, end:()->Unit, pushAni:eAni, pushTime:Int) {
        getLooper().item {
            time = pushTime
            block = {
                vm.x = it.backOut(eWindow.width.toDouble(), 0.0).toFloat()
                render()
            }
        } run(end)
    }
    override fun pushed() {

    }
}
class Main:eAndroidVM(){
    override var x  by f
    var title by s
    val contents by v(object:eAndroidVM(){
        override var text by s
        override var textColor by s
    })
}