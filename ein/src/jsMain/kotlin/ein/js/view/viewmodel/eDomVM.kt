package ein.js.view.viewmodel

import ein.core.core.eRunnable
import ein.core.view.viewmodel.eViewModel

abstract class eDomVM(isStored:Boolean = false):eViewModel(isStored){
    open var className = ""
    open var className_ = ""
    open var html = ""
    open var html_ = ""
    open var _html = ""
    open var name = ""
    open var runSubmit = true
    open var runFocus = true
    open var runBlur = true
    open var disabled = true
    open var checked = true
    open var selected = true
    open var selectedIndex = 0
    open var unselect = true
    open var value:Any = true
    open var lazySrc = "" to ""
    open var topViewPort = eRunnable
    open var rectHeight = true
    open var Aattr = ""

    open var color = ""
}