package ein.android.view.viewmodel.prop

import android.graphics.Color

private val colors = mutableMapOf<String, Int>()
fun color(v:String):Int{
    if(colors[v] == null) colors[v] = Color.parseColor(v)
    return colors[v]!!
}