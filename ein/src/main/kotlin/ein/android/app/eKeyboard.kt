package ein.android.app

import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import ein.core.looper.getLooper

object eKeyboard{
    fun hide(act:AppCompatActivity){
        getLooper().run(100){
            eApp.imm.hideSoftInputFromWindow(
                act.window.decorView.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
    fun show() = eApp.imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    fun show(et:EditText) = eApp.imm.showSoftInput(et, 0)
}