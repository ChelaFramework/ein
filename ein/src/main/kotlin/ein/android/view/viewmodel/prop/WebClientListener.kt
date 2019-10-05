package ein.android.view.viewmodel.prop

import ein.core.core.elazy

interface WebClientListener{
    companion object{
        val EMPTY by elazy(true){object:WebClientListener{
            override fun urlChanged(url:String){}
        }}
    }
    fun urlChanged(url:String)
}