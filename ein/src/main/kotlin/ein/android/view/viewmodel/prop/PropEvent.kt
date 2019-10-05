package ein.android.view.viewmodel.prop

import android.os.Build
import android.view.View
import android.widget.EditText
import android.widget.TextView
import ein.android.view.viewmodel.propertyAndroid

fun focusable(view:View, v:Any){
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) view.focusable = when(v){
        is Boolean->if(v) View.FOCUSABLE else View.NOT_FOCUSABLE
        is Number-> v.toInt()
        else -> View.FOCUSABLE
    }
}
fun focusableInTouchMode(view:View, v:Any){
    if(v !is Boolean) return
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) view.isFocusableInTouchMode = v
}
fun PropEvent() = propertyAndroid.let{
    it["click"] = fun(view: View, v:Any){
        if(v !is View.OnClickListener) return
        view.isClickable = true
        view.setOnClickListener(v)
    }
    it["longClick"] = fun(view: View, v:Any){
        if(v !is View.OnLongClickListener) return
        view.isLongClickable = true
        view.setOnLongClickListener(v)
    }
    it["clickable"] = fun(view: View, v:Any){
        if(v !is Boolean) return
        view.isClickable = true
    }
    it["longClickable"] = fun(view: View, v:Any){
        if(v !is Boolean) return
        view.isLongClickable = true
    }
    it["focusChange"] = fun(view:View, v:Any){
        if(v !is View.OnFocusChangeListener || view !is EditText) return
        view.setOnFocusChangeListener(v)
    }
    it["focusable"] = ::focusable
    it["focusableInTouchMode"] = ::focusableInTouchMode
    it["focus"] = fun(view:View, v:Any){
        if(v !is Boolean) return
        focusable(view, v)
        if(v) view.requestFocus()
        focusableInTouchMode(view, v)
    }
    it["textChanged"] = fun(view: View, v:Any){
        if(v !is OnTextChanged || view !is EditText) return
        v.text = view
        view.addTextChangedListener(v)
    }
    it["editorAction"] = fun(view: View, v:Any){
        if(v !is TextView.OnEditorActionListener || view !is EditText) return
        view.setOnEditorActionListener(v)
    }
    it["down"] = fun(view:View, v:Any){Touch.touch(view, v)?.put("down", v as Touch)}
    it["up"] = fun(view:View, v:Any){Touch.touch(view, v)?.put("up", v as Touch)}
    it["move"] = fun(view:View, v:Any){Touch.touch(view, v)?.put("move", v as Touch)}
}