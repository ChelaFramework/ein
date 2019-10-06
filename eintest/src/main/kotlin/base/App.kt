package base

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ein.android.looper.mainLooperAndroid
import ein.android.looper.netLooper
import ein.core.looper.getLooper

@SuppressLint("StaticFieldLeak")
class App:Application(){
    companion object{
        private var spinner:View? = null
        private var splash:View? = null
        var test:View? = null
        var seller:View? = null
        fun init(act:AppCompatActivity, bundle:Bundle?){

        }
        fun splashHide() = getLooper().run{
            splash?.visibility = View.GONE
            spinner?.visibility = View.GONE
        }
    }
    override fun onTerminate() {
        super.onTerminate()
        mainLooperAndroid.stopScheduler()
        netLooper.stopScheduler()
    }
}