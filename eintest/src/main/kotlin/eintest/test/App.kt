package eintest.test

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import ein.android.app.eApp
import ein.android.app.eAsset
import ein.android.looper.MainLooper
import ein.android.view.router.eViewBase
import ein.android.view.router.eViewHolder
import ein.core.log.log
import ein.core.looper.ani
import ein.core.resource.eLoader
import ein.core.value.eJsonObject
import ein.core.value.eValue
import ein.core.view.router.eRouter
import eintest.test.main.MainH

@SuppressLint("StaticFieldLeak")
class App:Application(){
    companion object{
        var isInited = false
        private var router:eRouter<eViewBase, eViewHolder, View>? = null
        fun init(act:AppCompatActivity, bundle:Bundle?){
            MainLooper.act(act)
            act.findViewById<ViewGroup>(R.id.main)?.let {
                router?.run{base.group = it} ?: eRouter("main", eViewBase(it)).run{
                    router = this
                    this["main"] = {rk, hk, d->MainH(rk, hk, d)}
                }
            }
            ani().invoke {
                ani({time = 1000.0}) {
                    if(act.window.decorView.width != 0) {
                        it.stop()
                        eApp(act.application, true)
                        if(!isInited) {
                            isInited = true
                            eAsset.string("setting.json")?.let {
                                eLoader.load(eValue.json(it) as eJsonObject)
                            }
                        }
                        router?.push("main")
                    }
                }
            }
        }
    }
    override fun onTerminate() {
        super.onTerminate()
        MainLooper.terminate()
    }
}