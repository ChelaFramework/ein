package ein.android.view.viewmodel.prop

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import ein.android.app.eView

abstract class OnTextChanged:TextWatcher {
    lateinit var text:EditText
    override fun afterTextChanged(s:Editable?){}
    override fun beforeTextChanged(s:CharSequence?, start:Int, count:Int, after:Int){}
    override fun onTextChanged(s:CharSequence?, start:Int, before:Int, count:Int) = onChanged(text, s ?: "", start, before, count)
    abstract fun onChanged(view:EditText, s:CharSequence, start:Int, before:Int, count:Int)
    fun pos() = eView.cursorPos(text)
}