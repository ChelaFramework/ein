package ein.android.view.router

import android.view.LayoutInflater
import android.view.View
import ein.core.view.router.eHolder

abstract class eViewHolder(
    routerKey:String, key:String, data:Any? = null,
    vararg channels:Pair<String, (channel:String, value:Any?)->Unit>
):eHolder<View>(routerKey, key, data, *channels){
    protected abstract val layout:Int
    protected open val isMerge = false
    internal var inflater:LayoutInflater? = null
    private var v:View? = null
    private var inf:LayoutInflater? = null
    override val view:View
        get() = inflater?.let{
            if(inf == it) return v ?: throw Throwable("no view")
            inf = it
            v = it.inflate(layout, null, isMerge)
            v
        } ?: throw Throwable("no inflate")
    abstract fun render()
}