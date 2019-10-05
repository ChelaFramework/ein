package ein.android.view.viewmodel.prop

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Build
import android.util.Log
import android.view.View
import android.webkit.*
import ein.android.view.viewmodel.propertyAndroid
import ein.core.log.log
import java.io.File
@Suppress("DEPRECATION")
private object w {
    fun optimization(view:View, v:Any) { //성능 향상(최적화)
        if(view !is WebView || v !is Boolean) return
        if(v) {
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) view.settings.setRenderPriority(WebSettings.RenderPriority.HIGH) //강제적으로 랜더러 우선순위를 높임(deprecated in API level 18)
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) view.settings.setEnableSmoothTransition(true) //부드러운 전환 허용 여부(deprecated in API level 17)
            //하드웨어 가속, 4.4이상이면 하드웨어가속이 유리함
            view.setLayerType(
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) View.LAYER_TYPE_HARDWARE else View.LAYER_TYPE_SOFTWARE,
                    null
            )
        }
    }

    fun debuggingEnabled(view:View, v:Any) {
        if(view !is WebView || v !is Boolean) return
        //디버깅 허용(킷켓 이상부터 데스크탑 크롬으로 원격 디버깅이 가능)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) WebView.setWebContentsDebuggingEnabled(v)
    }

    fun javascriptEnabled(view:View, v:Any) {
        if(view !is WebView) return
        if(v !is Boolean) return
        view.settings.javaScriptEnabled = v  //자바스크립트 사용 여부
    }
    fun localstorageEnabled(view:View, v:Any){
        if(view !is WebView || v !is Boolean) return
        view.settings.databaseEnabled = v //데이터베이스 저장 API 사용 여부
        view.settings.domStorageEnabled = v //DOM Storage API 사용 여부
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            view.settings.databasePath = "/data/data/" + view.context.packageName + "/databases/" //database 경로 설정
        }
    }
    fun openWindowEnabled(view:View, v:Any){
        if(view !is WebView || v !is Boolean) return
        view.settings.javaScriptCanOpenWindowsAutomatically = v //javascript가 팝업창을 사용할 수 있도록 설정. 해당 속성을 추가해야 window.open()을 사용할 수 있음.
        view.settings.setSupportMultipleWindows(v) //여러개의 윈도우를 사용할 수 있도록 설정(새창 띄우기 허용 여뷰)
    }
    fun cacheEnabled(view:View, v:Any){
        if(view !is WebView || v !is Boolean) return
        view.settings.setAppCacheEnabled(v) //앱 캐시 사용여부
        //view.settings.setAppCacheMaxSize(v) //앱 캐시 크기설정(deprecated)
        if(v){
            val appCacheDir = File(view.context.cacheDir, "appCache")
            if (!appCacheDir.exists()) appCacheDir.mkdirs()
            view.settings.setAppCachePath(appCacheDir.absolutePath) //캐시 파일 경로 설정
        }
    }
    fun cacheMode(view:View, v:Any){
        if(view !is WebView || v !is Int) return
        /*  캐시 방식 설정
        WebSettings.LOAD_DEFAULT //기본적인 모드로 캐시를 사용하고 만료된 경우 네트워크를 사용해 로드
        WebSettings.LOAD_NORMAL //기본적인 모드로 캐시를 사용(deprecated in API level 17)
        WebSettings.LOAD_CACHE_ELSE_NETWORK //캐쉬를 사용할수 있는경우 기간이 만료되도 사용. 사용할 수 없으면 네트워크를 사용
        WebSettings.LOAD_NO_CACHE //캐시모드를 사용하지 않고 네트워크를 통해서만 호출
        WebSettings.LOAD_CACHE_ONLY //네트워크를 사용하지 않고 캐시를 불러옴 */
        when(v){
            WebSettings.LOAD_DEFAULT,WebSettings.LOAD_NORMAL,WebSettings.LOAD_CACHE_ELSE_NETWORK,WebSettings.LOAD_NO_CACHE,WebSettings.LOAD_CACHE_ONLY -> view.settings.cacheMode =  v
        }
    }
    fun fitContent(view:View, v:Any) {
        if(view !is WebView || v !is Boolean) return
        /*  랜더링 방식 변경
         *  WebSettings.LayoutAlgorithm.NORMAL : 렌더링이 변경되지 않음을 의미. 이는 다양한 플랫폼 및 Android 버전에서 최대한의 호환성을 위해 권장되는 선택입니다.
         *  WebSettings.LayoutAlgorithm.SINGLE_COLUMN : 모든 내용을 화면에 보이도록 맞춤(Deprecated in API level 12)
         *  WebSettings.LayoutAlgorithm.NARROW_COLUMNS : 모든 열을 화면보다 넓게 만듬.(Deprecated in API level 12)
         *  WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING : 텍스트를 읽을 수 있도록 단락의 글꼴 크기를 향상. 줌 지원이 활성화되어 있어야 함.(KITKAT부터 지원)  */
        view.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
        view.settings.loadWithOverviewMode = v //화면에 문서 전체가 보이게 설정, 웹뷰에 맞게 출력(컨텐츠가 웹뷰보다 클 경우 스크린 크기에 맞게 조정)
        view.settings.useWideViewPort = v //뷰 wide viewport 허용 여부.  html 컨텐츠가 웹뷰에 맞게 나타나도록 함
    }
    fun supportZoom(view:View, v:Any){
        if(view !is WebView || v !is Boolean) return
        view.settings.setSupportZoom(v) //확대 축소 기능을 사용할 수 있도록 설정
        view.settings.builtInZoomControls = v //내장 줌 컨트롤 사용
        view.settings.displayZoomControls = false //내장 중 컨트롤 표시 여부
    }
    fun encoding(view:View, v:Any){
        if(view !is WebView || v !is String) return
        view.settings.defaultTextEncodingName = v //기본 인코딩 설정("UTF-8")
    }
    fun mixedContentMode(view:View, v:Any){
        if(view !is WebView || v !is Boolean) return
        if(v) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) view.settings.mixedContentMode =
                    WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
    }
    fun allowFileAccess(view:View, v:Any){
        if(view !is WebView || v !is Boolean) return
        view.settings.allowFileAccess = v //웹 뷰 내에서 파일 액세스 활성화
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ) view.settings.allowFileAccessFromFileURLs = v //파일에 접근하는 것을 허용
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) view.settings.allowUniversalAccessFromFileURLs = v
    }
    fun clearCache(view:View, v:Any){
        if(view !is WebView || v !is Boolean) return
        view.clearCache(v)
        if(v){
            view.clearHistory()
            view.clearFormData()
        }
    }
    fun setting(view:View, v:Any){
        if (view !is WebView) return
        when(v){
            "default" -> {
                optimization(view, true)
                debuggingEnabled(view, true)
                javascriptEnabled(view, true)
                localstorageEnabled(view, true)
                openWindowEnabled(view, true)
                cacheEnabled(view, true)
                cacheMode(view, WebSettings.LOAD_DEFAULT)
                fitContent(view, true)
                supportZoom(view, false)
                encoding(view, "UTF-8")
                mixedContentMode(view, true)
                allowFileAccess(view, true)
                clearCache(view, true)
                view.settings.userAgentString = view.settings.userAgentString.replace("; wv","")
            }
        }
    }
}
private class Chrome(private val alert:ChromeAlert):WebChromeClient(){
    override fun onJsAlert(view:WebView?, url:String?, message:String?, result:JsResult?):Boolean{
        alert.alert("$message", result)
        result?.confirm()
        return true
    }
    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
        consoleMessage?.let{
            val msg = "${it.message()}(${it.lineNumber()} in ${it.sourceId()})"
            if(it.messageLevel() === ConsoleMessage.MessageLevel.ERROR) Log.e(TAG, msg)
            else Log.d(TAG, msg)
        }
        return true
    }
    override fun onConsoleMessage(message:String, lineNumber:Int, sourceID:String){
        Log.d(TAG, "$message($lineNumber in $sourceID)")
    }
}
@SuppressLint("AddJavascriptInterface")
fun PropWebview() = propertyAndroid.let{
    it["webviewSetting"] = w::setting
    it["optimization"] = w::optimization
    it["debuggingEnabled"] = w::debuggingEnabled
    it["javascriptEnabled"] = w::javascriptEnabled
    it["localstorageEnabled"] = w::localstorageEnabled
    it["openWindowEnabled"] = w::openWindowEnabled
    it["cacheEnabled"] = w::cacheEnabled
    it["cacheMode"] = w::cacheMode
    it["fitContent"] = w::fitContent
    it["supportZoom"] = w::supportZoom
    it["encoding"] = w::encoding
    it["mixedContentMode"] = w::mixedContentMode
    it["allowFileAccess"] = w::allowFileAccess
    it["clearCache"] = w::clearCache
    it["client"] = fun(view: View, v:Any){
        if(view !is WebView) return
        when(v) {
            "default"->view.webViewClient = WebClient(view)
            is WebClientListener->view.webViewClient = WebClient(view, v)
            is WebViewClient->view.webViewClient = v
        }
    }
    it["chromeClient"] = fun(view:View, v:Any){
        if(view !is WebView) return
        when(v){
            is ChromeAlert->view.webChromeClient = Chrome(v)
            is WebChromeClient->view.webChromeClient = v
        }
    }
    it["loadsImagesAutomatically"] = fun(view:View, v:Any) {
        if(view !is WebView || v !is Boolean) return
        view.settings.loadsImagesAutomatically = v //앱에 등록되어 있는 이미지 리소스를 자동으로 로드하도록 설정하는 속성
    }
    it["usePlugin"] = fun(view:View, v:Any) {
        if(view !is WebView || v !is Boolean) return
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) view.settings.pluginState = WebSettings.PluginState.ON //플러그인 사용(Deprecated in API level 18)
    }
    it["mediaPlaybackRequiresUserGesture"] = fun(view:View, v:Any) {
        if(view !is WebView || v !is Boolean) return
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) view.settings.mediaPlaybackRequiresUserGesture = v //미디어를 재생할 때 사용자의 조작이 필요한지 여부를 설정
    }
    it["userAgent"] = fun(view:View, v:Any){
        if(view !is WebView || v !is String) return
        view.settings.userAgentString = v
    }
    it["loadUrl"] = fun(view:View, v:Any){
        if(view !is WebView || v !is String) return
        view.loadUrl(v)
    }
    it["loadJavascript"] = fun(view:View, v:Any){
        if(view !is WebView || v !is String) return
        view.loadUrl("javascript:$v")
    }
    it["back"] = fun(view: View, v:Any){
        if(view !is WebView) return
        if(view.canGoBack()) view.goBack()
    }
    it["reload"] = fun(view:View, v:Any){
        if(view !is WebView) return
        view.reload()
    }
    it["javascriptInterface"] = fun(view:View, v:Any){
        if(view !is WebView || v !is JSInterface) return
        view.addJavascriptInterface(v, v.name)
    }
}
