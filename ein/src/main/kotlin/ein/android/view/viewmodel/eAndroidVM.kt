package ein.android.view.viewmodel

import android.view.MotionEvent
import android.view.View
import android.webkit.WebView
import android.widget.EditText
import android.widget.TextView
import ein.android.view.viewmodel.prop.OnTextChanged
import ein.android.view.viewmodel.prop.Touch
import ein.core.view.viewmodel.eViewModel

abstract class eAndroidVM(isStored:Boolean = false):eViewModel(isStored){
    companion object{
        private val emptyF:()->Unit = {}
        private val emptyClick = View.OnClickListener{}
        private val emptyLongClick = View.OnLongClickListener{true}
        private val emptyFocusChange = View.OnFocusChangeListener{_,_->}
        private val emptyTextChange = object:OnTextChanged(){
            override fun onChanged(view:EditText, s:CharSequence, start:Int, before:Int, count:Int) {}
        }
        private val emptyEditorActionListener = TextView.OnEditorActionListener{_,_,_->true}
        private val emptyTouch:Touch = object:Touch{
            override fun onTouch(e:MotionEvent) = true
        }
    }
    //Button
    open var buttonDrawable:Any = "" //Btn, String:left|top|right|bottom@drawable
    //Event
    open var click = emptyClick
    open var longClick = emptyLongClick
    open var clickable = true
    open var longClickable = true
    open var focusChange = emptyFocusChange
    open var focusable = true
    open var focusableInTouchMode = true
    open var focus = true
    open var textChanged = emptyTextChange
    open var editorAction = emptyEditorActionListener
    open var down = emptyTouch
    open var up = emptyTouch
    open var move = emptyTouch
    //Image
    open var image:Any = 0 //Int, String, Drawable, Bitmap
    //Layout
    open var width = 0
    open var height = 0
    open var margin = "0 0 0 0"
    open var marginStart = 0
    open var marginEnd = 0
    open var marginTop = 0
    open var marginBottom = 0
    //Switch
    open var ison = true
    open var track:Any = "" //String,Drawable,Int
    open var thumb:Any = "" //String,Drawable,Int
    open var thumbpadding = 0
    open var textoff = ""
    open var texton = ""
    open var switchminwidth = 0
    //Text
    open var text = ""
    open var fromHtml = ""
    open var textSize = 0.0
    open var textScaleX = 0F
    open var lineSpacing = 0F
    open var textColor = "#"
    open var textAlignment = "" //center,gravity,inherit,textend,textstart,viewend,viewstart
    open var hint = ""
    open var hintColor = "#"
    open var maxLines = 0
    open var ellipsize = "" //start,middle,end,marquee
    open var maxLength = 0
    open var allCaps = true
    open var fontFamily = ""
    open var font = ""
    open var inputType = "" /*
        date,datetime,none,number,numberdecimal,numberpassword,numbersigned,phone,
        text,textautocomplete,textautocorrect,textcapcharacters,textcapsentences,
        textcapwords,textemailaddress,textemailsubject,textfilter,textimemultiline,
        textlongmessage,textmultiline,textnosuggestions,textpassword,textpersonname,
        textphonetic,textpostaladdress,textshortmessage,texturi,textvisiblepassword,
        textwebedittext,textwebemailaddress,textwebpassword,time
    */
    //View
    open var tag = ""
    open var isEnabled = true
    open var visibility = true
    open var background:Any = "" //String, Int, Drawable, Bitmap
    open var shadow = 0F
    open var x = 0F
    open var y = 0F
    open var z = 0F
    open var scaleX = 0F
    open var scaleY = 0F
    open var rotation = 0F
    open var alpha = 0F
    open var paddingStart = 0
    open var paddingEnd = 0
    open var paddingTop = 0
    open var paddingBottom = 0
    open var padding = "0 0 0 0"
    //PropWebview
    open var webviewSetting = "default"
    open var optimization = true
    open var debuggingEnabled = false
    open var javascriptEnabled = true
    open var localstorageEnabled = true
    open var openWindowEnabled = true
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