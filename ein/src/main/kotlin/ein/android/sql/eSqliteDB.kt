package ein.android.sql

import android.annotation.SuppressLint
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ein.android.app.eApp
import ein.android.app.eAsset
import ein.core.value.eJsonArray
import ein.core.value.eJsonObject
import ein.core.log.log
import ein.core.resource.eLoader
import ein.core.sql.eDB
import ein.core.sql.eQuery
import java.io.File
import java.io.FileOutputStream

/*
{
    "db":{
        "dbName":{
            "ver":1 //madatory
            "create":"query1,query2..", //madatory - ""
            "asset":"db.mp4" //optional
        }
    }
}
 */
class eSqliteDB(db:String, ver:Int, create:String):eDB(db, ver, create) {
    companion object:eLoader {
        fun init(){eLoader += eSqliteDB}
        override fun load(res:eJsonObject) {
            (res["db"] as? eJsonObject)?.forEach {(k, v)->
                val data = v as eJsonObject
                eDB[k] = {eSqliteDB(k, (data["ver"]?.v as Long).toInt(), "${data["create"]?.v}")}
                data["asset"]?.let{a->
                    val root = "/" + "data/data/${eApp.packName}/databases/"
                    val db = File(root + k)
                    if(!db.exists()) {
                        File(root).mkdirs()
                        if(db.createNewFile()) FileOutputStream(db).use {
                            it.write(eAsset.bytes("${a.v}"))
                        }
                    }
                }
            }
        }
    }
    private val sqlite = object:SQLiteOpenHelper(eApp.app, db, null, ver){
        val writer = writableDatabase
        val reader = readableDatabase
        override fun onCreate(db:SQLiteDatabase?) {
            if(create.isNotBlank()) create.split(",").forEach{query(it){_,_->}}
        }
        override fun onUpgrade(db:SQLiteDatabase?, oldVersion:Int, newVersion:Int){}
    }
    override fun query(k:String, vararg arg:Pair<String, Any>, block:(Array<Array<Any?>>?, String?)->Unit){
        val (cursor, msg) = runQuery(k, arg)
        cursor?.let {
            val r = if(it.count > 0 && it.moveToFirst()) eCursor(it, false) else null
            it.close()
            if(r == null) block(null, "no data") else block(r.rs, msg)
        } ?: block(null, msg)
    }
    override fun remove(block:()->Unit) {
        sqlite.close()
        block()
    }
    override fun transactionBegin() {
        sqlite.writer.beginTransaction()
    }
    override fun transactionCommit() {
        sqlite.writer.setTransactionSuccessful()
        sqlite.writer.endTransaction()
    }
    override fun transactionRollback() {
        sqlite.writer.endTransaction()
    }
    @SuppressLint("Recycle")
    private fun runQuery(k:String, a:Array<out Pair<String, Any>>):Pair<Cursor?, String?>{
        val query = eQuery[k] ?: return null to "invalid query:$k"
        val arg = query.param(a) ?: return null to query.msg
        var c:Cursor? = null
        if(!isBegin && arg.size > 1) sqlite.writer.beginTransaction()
        arg.forEach{(query, a)->
            val q = query.substring(1)
            //log("$q, ${a.joinToString(",")}")
            try {
                when(query[0]){
                    'r' -> {
                        c?.close()
                        c = sqlite.reader.rawQuery(q, if(a.isEmpty()) null else a) ?: return null to "no result:$k"
                    }
                    'w'->if(a.isEmpty()) sqlite.writer.execSQL(q) else sqlite.writer.execSQL(q, a)
                }
            }catch(e:Throwable){
                if(!isBegin && arg.size > 1) sqlite.writer.endTransaction()
                return null to "error - $e"
            }
        }
        if(!isBegin && arg.size > 1){
            sqlite.writer.setTransactionSuccessful()
            sqlite.writer.endTransaction()
        }
        return c to null
    }
}