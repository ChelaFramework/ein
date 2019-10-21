package eintest.test.main

import ein.android.app.eWindow
import ein.android.view.viewmodel.eAndroidVM
import ein.core.view.viewmodel.template.eTemplateData

class Main:eAndroidVM(){
    override var x  by x(eWindow.width.toFloat())
    var title by kv("title", "Main Title")
    val contents by v(object:eAndroidVM(){
        override var text by text("contents Text")
        override var textColor by textColor("#ff0000")
    })
    val box by kv("box", object :eAndroidVM(){
        override var x by x(0F)
        override var y by y(0F)
    })
    val rview by kv("rview", object :eAndroidVM(){
        override var template by template {

        }
    })
}