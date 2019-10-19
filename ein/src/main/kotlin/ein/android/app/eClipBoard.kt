package ein.android.app

object eClipBoard{
    fun copy(v:String){
        eApp.clip.primaryClip = android.content.ClipData.newPlainText("text", v)
    }
}
