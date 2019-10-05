package ein.core.core

import java.net.URLDecoder
import java.net.URLEncoder

actual fun eEncodeUrl(v:String) = URLEncoder.encode(v, "UTF-8")
actual fun eDecodeUrl(v:String) = URLDecoder.decode(v, "UTF-8")