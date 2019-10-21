package ein.android.view.viewmodel

import android.view.MotionEvent
import android.view.View
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
    protected fun text(V:String) = kv("text", V)
    open var fromHtml = ""
    protected fun fromHtml(V:String) = kv("fromHtml", V)
    open var textSize = 0.0
    protected fun textSize(V:Double) = kv("textSize", V)
    open var textScaleX = 0F
    protected fun textScaleX(V:Float) = kv("textScaleX", V)
    open var lineSpacing = 0F
    protected fun lineSpacing(V:Float) = kv("lineSpacing", V)
    open var textColor = ""
    protected fun textColor(V:String) = kv("textColor", V)
    open var textAlignment = ""
    protected fun textAlignmentCenter() = kv("textAlignment", "center")
    protected fun textAlignmentGravity() = kv("textAlignment", "gravity")
    protected fun textAlignmentInherit() = kv("textAlignment", "inherit")
    protected fun textAlignmentTextend() = kv("textAlignment", "textend")
    protected fun textAlignmentTextstart() = kv("textAlignment", "textstart")
    protected fun textAlignmentViewend() = kv("textAlignment", "viewend")
    protected fun textAlignmentViewstart() = kv("textAlignment", "viewstart")
    open var hint = ""
    protected fun hint(V:String) = kv("hint", V)
    open var hintColor = ""
    protected fun hintColor(V:String) = kv("hintColor", V)
    open var maxLines = 0
    protected fun maxLines(V:Int) = kv("maxLines", V)
    open var ellipsize = ""
    protected fun ellipsizeStart() = kv("ellipsize", "start")
    protected fun ellipsizeMiddle() = kv("ellipsize", "middle")
    protected fun ellipsizeEnd() = kv("ellipsize", "end")
    protected fun ellipsizeMarquee() = kv("ellipsize", "marquee")
    open var maxLength = 0
    protected fun maxLength(V:Int) = kv("maxLength", V)
    open var allCaps = true
    protected fun allCaps(V:Boolean) = kv("allCaps", V)
    open var fontFamily = ""
    protected fun fontFamily(V:String) = kv("fontFamily", V)
    open var font = ""
    protected fun font(V:String) = kv("font", V)
    open var inputType = ""
    protected fun inputTypeDate() = kv("inputType", "date")
    protected fun inputTypeDatetime() = kv("inputType", "datetime")
    protected fun inputTypeNone() = kv("inputType", "none")
    protected fun inputTypeNumber() = kv("inputType", "number")
    protected fun inputTypeNumberdecimal() = kv("inputType", "numberdecimal")
    protected fun inputTypeNumberpassword() = kv("inputType", "numberpassword")
    protected fun inputTypeNumbersigned() = kv("inputType", "numbersigned")
    protected fun inputTypePhone() = kv("inputType", "phone")
    protected fun inputTypeText() = kv("inputType", "text")
    protected fun inputTypeTextautocomplete() = kv("inputType", "textautocomplete")
    protected fun inputTypeTextautocorrect() = kv("inputType", "textautocorrect")
    protected fun inputTypeTextcapcharacters() = kv("inputType", "textcapcharacters")
    protected fun inputTypeTextcapsentences() = kv("inputType", "textcapsentences")
    protected fun inputTypeTextcapwords() = kv("inputType", "textcapwords")
    protected fun inputTypeTextemailaddress() = kv("inputType", "textemailaddress")
    protected fun inputTypeTextemailsubject() = kv("inputType", "textemailsubject")
    protected fun inputTypeTextfilter() = kv("inputType", "textfilter")
    protected fun inputTypeTextimemultiline() = kv("inputType", "textimemultiline")
    protected fun inputTypeTextlongmessage() = kv("inputType", "textlongmessage")
    protected fun inputTypeTextmultiline() = kv("inputType", "textmultiline")
    protected fun inputTypeTextnosuggestions() = kv("inputType", "textnosuggestions")
    protected fun inputTypeTextpassword() = kv("inputType", "textpassword")
    protected fun inputTypeTextpersonname() = kv("inputType", "textpersonname")
    protected fun inputTypeTextphonetic() = kv("inputType", "textphonetic")
    protected fun inputTypeTextpostaladdress() = kv("inputType", "textpostaladdress")
    protected fun inputTypeTextshortmessage() = kv("inputType", "textshortmessage")
    protected fun inputTypeTexturi() = kv("inputType", "texturi")
    protected fun inputTypeTextvisiblepassword() = kv("inputType", "textvisiblepassword")
    protected fun inputTypeTextwebedittext() = kv("inputType", "textwebedittext")
    protected fun inputTypeTextwebemailaddress() = kv("inputType", "textwebemailaddress")
    protected fun inputTypeTextwebpassword() = kv("inputType", "textwebpassword")
    protected fun inputTypeTime() = kv("inputType", "time")
    //View
    open var tag = ""
    open var isEnabled = true
    open var visibility = true
    open var background:Any = "" //String, Int, Drawable, Bitmap
    open var shadow = 0F
    open var x = 0F
    protected fun x(V:Float) = kv("x", V)
    open var y = 0F
    protected fun y(V:Float) = kv("y", V)
    open var z = 0F
    protected fun z(V:Float) = kv("z", V)
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