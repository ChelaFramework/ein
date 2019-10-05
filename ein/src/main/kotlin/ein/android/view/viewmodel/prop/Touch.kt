package ein.android.view.viewmodel.prop

import android.view.MotionEvent
import android.view.View
import java.util.*

interface Touch{
    companion object{
        private val touches = WeakHashMap<View, MutableMap<String, Touch>>()
        private val hasTouch = WeakHashMap<View, Boolean>()
        fun touch(view:View, v:Any):MutableMap<String, Touch>?{
            if(v !is Touch) return null
            if(hasTouch[view] == null){
                hasTouch[view] = true
                view.setOnTouchListener{_, e->
                    return@setOnTouchListener touches[view]?.let {
                        when(e.action){
                            MotionEvent.ACTION_DOWN->it["down"]?.onTouch(e)
                            MotionEvent.ACTION_UP-> it["up"]?.onTouch(e)
                            MotionEvent.ACTION_MOVE-> it["move"]?.onTouch(e)
                            else->true
                        }
                    } ?: true
                }
                touches[view] = mutableMapOf()
            }
            return touches[view]
        }
    }
    fun onTouch(e:MotionEvent):Boolean
}