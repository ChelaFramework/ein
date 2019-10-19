package ein.android.app

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import ein.core.log.log

object eIntent{
    private const val IMAGE = 1111
    private var act:AppCompatActivity? = null
    operator fun invoke(a:AppCompatActivity){act = a}
    fun browser(url:String) = act?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    fun share(body:String, title:String = "Share URL") = act?.let {
        ShareCompat.IntentBuilder.from(it)
            .setType("text/plain")
            .setChooserTitle(title)
            .setText(body)
            .startChooser()
    }
    private var imageBlock:((Bitmap)->Unit)? = null
    fun image(block:(Bitmap)->Unit) {
        imageBlock = block
        act?.startActivityForResult(Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }, IMAGE)
    }

    fun result(requestCode:Int, resultCode:Int, data:Intent?) {
        if(data == null || data.data == null || resultCode != RESULT_OK) return
        val d = data.data!!
        when(requestCode){
            IMAGE->imageBlock?.let {
                act?.contentResolver?.openInputStream(d)?.let{ins->
                    val bitmap = BitmapFactory.decodeStream(ins)
                    ins.close()
                    it(bitmap)
                }
            }
        }
    }
}