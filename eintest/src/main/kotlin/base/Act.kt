package base

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import ein.core.value.eValue
import ein.core.log.log

class Act:AppCompatActivity(){
    companion object{
        const val DATA = "DATA"
        const val FCM = 0
        const val PUSH = 1
    }
    private var bundle:Bundle? = null
    override fun onCreate(savedInstanceState: Bundle?){
        log("${System.currentTimeMillis()}")
        super.onCreate(savedInstanceState)
        bundle = savedInstanceState
        setContentView(R.layout.activity_main)
        onNewIntent(intent)
        log("${System.currentTimeMillis()}")
    }
    override fun onSaveInstanceState(outState:Bundle?, outPersistentState:PersistableBundle?){
        super.onSaveInstanceState(outState, outPersistentState)
    }
    override fun onNewIntent(intent:Intent?) {
        super.onNewIntent(intent)
        if(intent?.action == Intent.ACTION_MAIN) App.init(this, bundle)
        else{
            val data = intent?.getStringExtra(DATA)?.let{
               eValue.json(it)
            }
            when(intent?.flags){
                FCM->{}
                PUSH->{}
            }
        }
    }
    override fun onResume() {
        super.onResume()
    }
    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?) =
        if (keyCode == KeyEvent.KEYCODE_BACK && Router.action("canGoBack") == true){
            Router.action("goBack")
            true
        }else super.onKeyDown(keyCode, event)
}
