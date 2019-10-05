package ein.android.view.viewmodel.prop

import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.widget.SwitchCompat
import ein.android.app.eApp
import ein.android.view.viewmodel.propertyAndroid

fun PropSwitch() = propertyAndroid.let{
    it["ison"] = fun(view: View, v:Any){
        if(view !is SwitchCompat || v !is Boolean) return
        view.isChecked = v
    }
    it["track"] = fun(view: View, v:Any){
        if(view !is SwitchCompat) return
        when(v){
            is String->view.trackDrawable = eApp.drawable(v)
            is Drawable ->view.trackDrawable = v
            is Int->view.setTrackResource(v)
            else->throw Throwable("Invalid track resource : $v")
        }
    }
    it["thumb"] = fun(view: View, v:Any){
        if(view !is SwitchCompat) return
        when(v){
            is String->view.thumbDrawable = eApp.drawable(v)
            is Drawable ->view.thumbDrawable = v
            is Int->view.setThumbResource(v)
            else->throw Throwable("Invalid thumb resource : $v")
        }
    }
    it["thumbpadding"] = fun(view: View, v:Any){
        if(view !is SwitchCompat || v !is Int) return
        view.thumbTextPadding = v
    }
    it["textoff"] = fun(view: View, v:Any){
        if(view !is SwitchCompat) return
        view.textOff = "$v"
    }
    it["texton"] = fun(view: View, v:Any){
        if(view !is SwitchCompat) return
        view.textOn = "$v"
    }
    it["switchminwidth"] = fun(view: View, v:Any){
        if(view !is SwitchCompat || v !is Int) return
        view.switchMinWidth = v
    }
}
