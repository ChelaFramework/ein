package ein.android.app

import android.net.Uri
import android.provider.MediaStore

object eContent{
    fun query(uri: Uri, projection: Array<out String>, selection:String?, selectionArgs:Array<String>?, sortOrder:String)
        = eApp.app.contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)
    fun getImage(sort:String, isAsc:Boolean, vararg projection:String)
        = query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection, null, null, "$sort ${if(isAsc) "ASC" else "DESC"}"
        )
}