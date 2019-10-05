package ein.android.view.viewmodel.prop

import android.os.Build
import android.view.View
import android.view.ViewGroup
import ein.android.view.viewmodel.propertyAndroid

private inline fun param(view: View, block:(ViewGroup.LayoutParams)->ViewGroup.LayoutParams){
    view.layoutParams = block(view.layoutParams ?: ViewGroup.MarginLayoutParams(-1, -1))
}
private fun maginParam(param:ViewGroup.LayoutParams) = param as? ViewGroup.MarginLayoutParams ?: ViewGroup.MarginLayoutParams(param.width, param.height)
fun PropLayout() = propertyAndroid.let{
    it["width"] = fun(view: View, v:Any){
        if(v !is Number) return
        param(view){
            it.width = v.toInt()
            it
        }
    }
    it["height"] = fun(view: View, v:Any){
        if(v !is Number) return
        param(view){
            it.height = v.toInt()
            it
        }
    }
    it["margin"] = fun(view: View, v:Any){
        param(view){
            maginParam(it).apply {
                @Suppress("UNCHECKED_CAST")
                if(v is String){
                    val a = v.split(" ").map {it.toDouble().toInt()}
                    setMargins(a[3], a[0], a[1], a[2])//left,top,right,bottom
                }else (v as? List<Int>)?.let{setMargins(it[3], it[0], it[1], it[2])}
            }
        }
    }
    it["marginStart"] = fun(view: View, v:Any){
        if(v is Number) param(view){
            maginParam(it).apply {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) marginStart = v.toInt()
                else leftMargin = v.toInt()
            }
        }
    }
    it["marginEnd"] = fun(view: View, v:Any){
        if(v is Number) param(view){
            maginParam(it).apply{
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) marginEnd = v.toInt()
                else rightMargin = v.toInt()
            }
        }
    }
    it["marginTop"] = fun(view: View, v:Any){
        if(v is Number) param(view){
            maginParam(it).apply{topMargin = v.toInt()}
        }
    }
    it["marginBottom"] = fun(view: View, v:Any){
        if(v is Number) param(view){
            maginParam(it).apply {bottomMargin = v.toInt()}
        }
    }
}