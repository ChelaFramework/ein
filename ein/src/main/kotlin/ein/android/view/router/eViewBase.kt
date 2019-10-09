package ein.android.view.router

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import ein.core.log.log
import ein.core.view.router.eBase

class eViewBase(el:ViewGroup, historyLevel:Int = 1):eBase<eViewHolder, View>(historyLevel){
    private var inflater = (el.context as Activity).layoutInflater
    var group = el
        set(el:ViewGroup){
            if(el === field) return
            el.removeAllViews()
            inflater = (el.context as Activity).layoutInflater
            field = el
            restore()
        }
    private fun restore() = curr?.let {
        var i = historyLevel
        it.prev{h->if(i-- > 0) add(h as eViewHolder, false, 0)}
        add(it, true)
        i = historyLevel
        it.next{h->if(i-- > 0) add(h as eViewHolder, false)}
    }
    override fun add(holder:eViewHolder, isCurr:Boolean, idx:Int){
        holder.restored(isCurr)
        holder.inflater = inflater
        if(idx == -1) group.addView(holder.view)
        else group.addView(holder.view, idx)
        holder.render()
    }

    override fun remove(holder:eViewHolder) {
        holder.inflater = inflater
        group.removeView(holder.view)
    }
}