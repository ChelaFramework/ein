package ein.android.view.viewmodel

import android.view.View
import ein.android.view.viewmodel.prop.*
import ein.core.view.viewmodel.eProperty

object propertyAndroid:eProperty<View>(){
    init{
        PropButton()
        PropEvent()
        PropImage()
        PropLayout()
        PropSwitch()
        PropText()
        PropView()
        PropWebview()
    }
}