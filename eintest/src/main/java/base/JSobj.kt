package base

import android.webkit.JavascriptInterface
import ein.android.app.eApp
import ein.android.view.viewmodel.prop.JSInterface
import ein.core.channel.eChannel
import ein.core.core.*
import ein.core.log.log
import ein.core.sql.eDB

object JSobj:JSInterface {
    override val name = "SELLER"
    private var isLoaded = false
    @JavascriptInterface
    fun isConnected() = eApp.isConnected()
    @JavascriptInterface
    fun loaded(){
        if(isLoaded) return
        isLoaded = true
        App.splashHide()
    }
    @JavascriptInterface
    fun country():String{
        var r = "[]"
        eDB["seller"]?.query("country"){rs, _->
            rs?.let{r = ePrimitive.rs2Json(it)}
        }
        return r
    }
    @JavascriptInterface
    fun atoken() = eDB["seller"]?.s("atoken") ?: ""
    @JavascriptInterface
    fun login(email:String, atoken:String){
        eDB["seller"]?.let{
            it.begin()
            val sid = it.i("login0", 0,"email" to email, "atoken" to atoken)
            if(sid == 0) it.query("login2", "email" to email, "atoken" to atoken)
            else  it.query("login1", "sid" to sid)
            it.commit()
        }
    }
    @JavascriptInterface
    fun logout(email:String, atoken:String){
        eDB["seller"]?.query("logout", "email" to email, "atoken" to atoken)
    }
    @JavascriptInterface
    fun cNew(v:String){
        val json = ePrimitive.json(v) as eJsonObject
        if(json["type"]?.v != "General") return
        eDB["seller"]?.let {db->
            db.begin()
            var gid = db.i("cgroup", 0, "type" to 1)
            if(gid == 0){
                gid = db.i("cnewgroup", 0, "type" to 1)
                if(gid == 0){
                    db.rollback()
                    return
                }
            }
            val cid = db.i("cNew",
            "gid" to gid, "cid" to uuid(),
                "name" to "${json["name"]?.v}",
                "memo" to "${json["memo"]?.v}"
            )
            db.query("cNewPhone",
            "cid" to cid,
                "country" to "${json["country"]?.v}".toInt(),
                "phone" to "${json["phone"]?.v}"
            )
            db.query("cNewEmail",
                "cid" to cid,
                "email" to "${json["email"]?.v}"
            )
            db.commit()
        }
    }
    @JavascriptInterface
    fun cEdit(v:String){
        val json = ePrimitive.json(v) as eJsonObject
        eDB["seller"]?.query("cEdit",
            "cid" to "${json["cid"]?.v}",
            "country" to "${json["country"]?.v}".toInt(),
            "phone" to "${json["phone"]?.v}",
            "email" to "${json["email"]?.v}",
            "memo" to "${json["memo"]?.v ?: ""}",
            "name" to "${json["name"]?.v}"
        )
    }
    @JavascriptInterface
    fun cList(s:String, t:String):String{
        var r = "[]"
        eDB["seller"]?.query("cList",
        "search" to if(s.isBlank()) "" else
                "and (e.email like'%$s%'or "+
                "p.phone like'%$s%'or "+
                "c.username like'%$s%'or "+
                "c.memo like'%$s%')",
            "type" to when(t){
                "All"->"1,10,20"
                "General"->1
                "Line@"->"10"
                "Facebook Messanger"->"20"
                else->""
            }
        ){rs, m->
            rs?.let{r = ePrimitive.rs2Json(it)}
        }
        return r
    }
    @JavascriptInterface
    fun cView(cid:String):String{
        var r = "{}"
        eDB["seller"]?.query("cView", "cid" to cid){rs, _->
            rs?.let{
                val v = it[0]
                val json = eJsonObject()
                json["cid"] = eString(cid)
                json["type"] = eString("${v[0]}")
                json["name"] = eString("${v[1]}")
                json["memo"] = eString("${v[2]}")
                json["phone"] = eString("${v[3]}")
                json["email"] = eString("${v[4]}")
                val country = eJsonObject()
                json["country"] = country
                country["id"] = eString("${v[5]}")
                country["name"] = eString("${v[6]}")
                country["iso"] = eString("${v[7]}")
                country["isd"] = eString("${v[8]}")
                r = json.stringify()
            }
        }
        return r
    }
    @JavascriptInterface
    fun cDel(cid:String){
        eDB["seller"]?.query("cDel", "cid" to cid)
    }
    @JavascriptInterface
    fun cAddrEdit(cid:String, country:Int, data:String){
        val json = ePrimitive.json(data) as eJsonObject
        eDB["seller"]?.let{
            it.begin()
            fun f(type:Int, v:eJsonObject){
                val id = it.i("cAddrId", 0,"cid" to cid, "type" to type, "country" to country)
                if(id == 0) it.query("cAddrNew",
                        "name" to "${v["name"]?.v}",
                        "phone" to "${v["phone"]?.v}",
                        "zip" to "${v["zip"]?.v}",
                        "addr" to "${v["addr"]?.v}",
                        "type" to type,
                        "cid" to cid,
                        "country" to country
                ) else it.query("cAddrEdit",
                        "name" to "${v["name"]?.v}",
                        "phone" to "${v["phone"]?.v}",
                        "zip" to "${v["zip"]?.v}",
                        "addr" to "${v["addr"]?.v}",
                        "id" to id
                )
            }
            f(10, json["billing"] as eJsonObject)
            f(20, json["shipping"] as eJsonObject)
            it.commit()
        }
    }
    @JavascriptInterface
    fun cAddr(cid:String, country:Int):String{
        var r = "{}"
        eDB["seller"]?.let{
            val json = eJsonObject()
            val bill = eJsonObject()
            val ship = eJsonObject()
            json["billing"] = bill
            json["shipping"] = ship
            fun f(type:Int, json:eJsonObject) {
                it.query("cAddr", "cid" to cid, "country" to country, "type" to type){rs, _->
                    rs?.let {
                        val v = it[0]
                        json["name"] = eString("${v[0]}")
                        json["phone"] = eString("${v[1]}")
                        json["zipcode"] = eString("${v[2]}")
                        json["addr"] = eString("${v[3]}")
                    }
                }
            }
            f(10, bill)
            f(20, ship)
            r = json.stringify()
        }
        return r
    }
}