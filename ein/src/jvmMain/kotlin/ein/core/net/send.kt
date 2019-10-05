package ein.core.net

import ein.jvm.net.eResponseOkHttp
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit

private val JSON = MediaType.parse("application/json; charset=utf-8")!!
private val BODY = MediaType.parse("plain/text; charset=utf-8")!!
private val BODYBYTE = MediaType.parse("application/octet-stream; charset=utf-8")!!
internal actual fun send(request:eRequest, block:(eResponse)->Unit) {
    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(request.connectTimeout.toLong(), TimeUnit.MILLISECONDS)
        .readTimeout(request.readTimeout.toLong(), TimeUnit.MILLISECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    if(request.method != "GET") {
        (request.files?.let {
            val multi = MultipartBody.Builder().setType(MultipartBody.FORM)
            it.forEach {(k, v)->
                multi.addFormDataPart(k, v.filename, RequestBody.create(MediaType.parse(v.mine), v.file))
            }
            request.blob?.let {multi.addPart(RequestBody.create(BODYBYTE, it))}
                    ?: request.json?.let {multi.addPart(RequestBody.create(JSON, it))}
                    ?: request.text?.let {multi.addPart(RequestBody.create(BODY, it))} ?: request.form?.let {
                        val form = FormBody.Builder()
                        it.forEach {(k, v)-> form.add(k, v)}
                        multi.addPart(form.build())
                    }
            multi.build()
        } ?: request.blob?.let {RequestBody.create(BODYBYTE, it)} ?: request.json?.let {RequestBody.create(JSON, it)}
        ?: request.text?.let {RequestBody.create(BODY, it)} ?: request.form?.let {
            val form = FormBody.Builder()
            it.forEach {(k, v)-> form.add(k, v)}
            form.build()
        })?.let {
            val req = Request.Builder().url(request.url).method(request.method, it)
            request.header?.forEach {(k, v)-> req.addHeader(k, v)}
            okHttpClient.newCall(req.build()).enqueue(object:Callback {
                override fun onFailure(call:Call, e:IOException) = block(eResponseOkHttp(null, e.toString()))
                override fun onResponse(call:Call, response:Response) = block(eResponseOkHttp(response, null))
            })

        }
    }
}
