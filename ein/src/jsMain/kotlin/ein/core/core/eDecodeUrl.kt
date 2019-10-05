package ein.core.core

external fun decodeURIComponent(encodedURI: String): String
external fun encodeURIComponent(encodedURI: String): String

actual fun eEncodeUrl(v:String) = encodeURIComponent(v)
actual fun eDecodeUrl(v:String) = decodeURIComponent(v)