package ein.js.view.viewmodel

import ein.core.view.viewmodel.eViewModel

abstract class eDomVM(isStored:Boolean = false):eViewModel(isStored){

    open var cacheEnabled = true
    open var cacheMode = -1
    open var fitContent = true
    open var supportZoom = true
    open var encoding = "UTF-8"
    open var mixedContentMode = true
    open var allowFileAccess = true
    open var clearCache = true
    open var client:Any = "default" //default, WebClient
    open var chromeClient:Any = 0 //ChromeAlert, WebChromeClient
    open var loadsImagesAutomatically = true
    open var usePlugin = true
    open var mediaPlaybackRequiresUserGesture = true
    open var userAgent = "agent"
    open var loadUrl = "url"
    open var loadJavascript = "script"
    open var back = true
    open var reload = true
}