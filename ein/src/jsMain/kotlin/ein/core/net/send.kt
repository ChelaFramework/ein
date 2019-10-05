package ein.core.net

import ein.js.js.obj
import ein.js.net.eResponseFetch
import org.khronos.webgl.Int8Array
import org.w3c.fetch.Request
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag
import org.w3c.xhr.FormData
import kotlin.browser.window
import kotlin.js.Promise

internal actual fun send(request:eRequest, block:(eResponse)->Unit) {
    val req = obj{
        method = request.method
        headers = js("{}")
        headers["Cache-Control"] = "no-cache"
        body = null
    }
    if(request.method == "GET") req.headers["Content-Type"] = "text/plain; charset=utf-8"
    else{
        request.files?.let {
            req.body = FormData().apply {
                it.forEach{(k, v)->
                    append(k, Blob(arrayOf(Int8Array(v.file.toTypedArray())), BlobPropertyBag(v.mine)))
                }
                request.json?.let {
                    append("json", it)
                } ?: request.text?.let {
                    append("text", it)
                } ?: request.form?.let {
                    it.forEach{(k, v)->append(k, v)}
                }
            }
        } ?: request.blob?.let {
            req.headers["Content-Type"] = "application/octet-stream; charset=utf-8"
            req.body = Blob(arrayOf(Int8Array(it.toTypedArray())), BlobPropertyBag("application/octet-stream"))
        } ?: request.json?.let {
            req.headers["Content-Type"] = "application/json; charset=utf-8"
            req.body = it
        } ?: request.text?.let {
            req.headers["Content-Type"] = "application/text; charset=utf-8"
            req.body = it
        } ?: request.form?.let {
            req.body = FormData().apply {
                it.forEach{(k, v)->append(k, v)}
            }
        }
    }
    var id = -1
    Promise.race(arrayOf(
            window.fetch(Request(request.url, req)),
            Promise {_, r->
                id = window.setTimeout({r(Throwable("timeout"))}, request.readTimeout)
            }
    )).then{
        window.clearTimeout(id)
        if(!it.ok) throw Throwable("not ok")
        block(eResponseFetch(it, null))
    }.catch{
        block(eResponseFetch(null, it.message))
    }
}
