package ein.android.view.viewmodel.prop

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import ein.android.app.eApp
import ein.android.view.viewmodel.propertyAndroid

fun PropImage()= propertyAndroid.let{
    it["image"] = fun(view: View, v:Any){
        if(view !is ImageView) return
        when(v){
            is Int -> view.setImageResource(v)
            is String -> view.setImageDrawable(eApp.drawable(v))
            is Drawable -> view.setImageDrawable(v)
            is Bitmap -> view.setImageBitmap(v)
        }
    }
}