package ein.android.view.viewmodel.prop

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import ein.android.app.eApp
import ein.android.app.eDrawable
import ein.android.view.viewmodel.propertyAndroid
import ein.core.log.log
import ein.core.regex.eReg

fun PropView() = propertyAndroid.let{
    it["tag"] = fun(view:View, v:Any){
        if(v !is String) return
        view.tag = v
    }
    it["isEnabled"] = fun(view:View, v:Any){
        if(v !is Boolean) return
        view.isEnabled = v
    }
    it["visibility"] = fun(view:View, v:Any){
        view.visibility = when(v){
            is Boolean->if(v) View.VISIBLE else View.GONE
            is Number-> v.toInt()
            else -> View.VISIBLE
        }
    }
    @Suppress("DEPRECATION")
    it["background"] = fun(view:View, v:Any){
        when(v){
            is String ->{
                when(v[0]){
                    '#'->view.setBackgroundColor(color(v))
                    else-> {
                        eDrawable[v]?.let {
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) view.background = it
                            else view.setBackgroundDrawable(it)
                        } ?: view.setBackgroundResource(eApp.resDrawable(v))
                    }
                }
            }
            is Int -> view.setBackgroundResource(v)
            is Drawable -> if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) view.background = v
                else view.setBackgroundDrawable(v)
            is Bitmap -> if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) view.background = eApp.bitmap2Drawable(v)
                else view.setBackgroundDrawable(eApp.bitmap2Drawable(v))
        }
    }
    it["shadow"] = fun(view:View, v:Any){
        if(v !is Number || Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return
        view.elevation = v.toFloat()
    }
    it["x"] = fun(view:View, v:Any){
        if(v !is Number) return
        view.translationX = v.toFloat()
    }
    it["y"] = fun(view:View, v:Any){
        if(v !is Number) return
        view.translationY = v.toFloat()
    }
    it["z"] = fun(view:View, v:Any){
        if(v !is Number || Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return
        view.translationZ = v.toFloat()
    }
    it["scaleX"] = fun(view:View, v:Any){
        if(v !is Number) return
        view.scaleX = v.toFloat()
    }
    it["scaleY"] = fun(view:View, v:Any){
        if(v !is Number) return
        view.scaleY = v.toFloat()
    }
    it["rotation"] = fun(view:View, v:Any){
        if(v !is Number) return
        view.rotation = v.toFloat()
    }
    it["alpha"] = fun(view:View, v:Any){
        if(v !is Number) return
        view.alpha = v.toFloat()
    }
    it["paddingStart"] = fun(view:View, v:Any){
        if(v !is Number) return
        view.setPadding(v.toInt(), view.paddingTop, view.paddingEnd, view.paddingBottom)
    }
    it["paddingEnd"] = fun(view:View, v:Any){
        if(v !is Number) return
        view.setPadding(view.paddingStart, view.paddingTop, v.toInt(), view.paddingBottom)
    }
    it["paddingTop"] = fun(view:View, v:Any){
        if(v !is Number) return
        view.setPadding(view.paddingStart, v.toInt(), view.paddingEnd, view.paddingBottom)
    }
    it["paddingBottom"] = fun(view:View, v:Any){
        if(v !is Number) return
        view.setPadding(view.paddingStart, view.paddingTop, view.paddingEnd, v.toInt())
    }
    it["padding"] = fun(view:View, v:Any){
        if(v !is String) return
        val a = v.split(" ").map{eReg.value.num(it)?.toInt() ?: it.toInt()}
        view.setPadding(a[3], a[0], a[1], a[2])//left,top,right,bottom
    }
}