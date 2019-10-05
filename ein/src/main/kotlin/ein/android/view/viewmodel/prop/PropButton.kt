package ein.android.view.viewmodel.prop

import android.view.View
import android.widget.Button
import ein.android.app.eApp
import ein.android.view.viewmodel.propertyAndroid

private val rex = """^(left|right|top|bottom)@(.+)$""".toRegex()
fun PropButton() = propertyAndroid.let{
    it["buttonDrawable"] = fun(view:View, v:Any){
        if(view !is Button) return
        when(v){
            is Btn -> view.setCompoundDrawablesWithIntrinsicBounds(v.left, v.top, v.right, v.bottom)
            is String -> rex.matchEntire(v)?.let{
                val r = it.groupValues[2]
                when(it.groupValues[1].toLowerCase()){
                    "left"->view.setCompoundDrawablesWithIntrinsicBounds(eApp.drawable(r), null, null, null)
                    "top"->view.setCompoundDrawablesWithIntrinsicBounds(null, eApp.drawable(r), null, null)
                    "right"->view.setCompoundDrawablesWithIntrinsicBounds(null, null, eApp.drawable(r), null)
                    "bottom"->view.setCompoundDrawablesWithIntrinsicBounds(null, null, null, eApp.drawable(r))
                }
            }
        }
    }
}