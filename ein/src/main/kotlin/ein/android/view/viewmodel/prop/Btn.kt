package ein.android.view.viewmodel.prop

import ein.android.app.eApp

class Btn(t:Any?, r:Any?, b:Any?, l:Any?){
    val top = eApp.drawable(t)
    val right = eApp.drawable(r)
    val bottom = eApp.drawable(b)
    val left = eApp.drawable(l)
}