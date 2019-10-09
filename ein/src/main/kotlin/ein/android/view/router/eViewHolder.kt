package ein.android.view.router

import android.view.LayoutInflater
import android.view.View
import ein.android.view.viewmodel.processorAndroid
import ein.android.view.viewmodel.propertyAndroid
import ein.core.looper.ItemBlock
import ein.core.view.router.eAni
import ein.core.view.router.eHolder
import ein.core.view.viewmodel.eProcessor
import ein.core.view.viewmodel.eProperty
import ein.core.view.viewmodel.eScanned
import ein.core.view.viewmodel.eScanner

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