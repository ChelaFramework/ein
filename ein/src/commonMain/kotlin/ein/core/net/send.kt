package ein.core.net

internal expect fun send(request:eRequest, block:(eResponse)->Unit)